package com.cari.voip.keyboard.soft;


import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import com.cari.voip.keyboard.soft.dialogs.LoginDialog;
import com.cari.voip.keyboard.soft.dialogs.mnDialog;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.perspectives.CDRPerspective;
import com.cari.voip.keyboard.soft.perspectives.VRPerspective;
import com.cari.voip.keyboard.soft.perspectives.smsPerspective;
import com.cari.voip.keyboard.soft.resources.LoginInfo;

import com.cari.voip.keyboard.stack.CCKPConnectionException;
import com.cari.voip.keyboard.stack.ConnectionConfiguration;

/**
 * This class "Application" controls all aspects of the application's execution
 */
public class Application implements IApplication {
	private String connectionMsg = null;
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		Display display = Activator.getDisplay();
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS, true);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_OTHER_IN_PERSPECTIVE_MENU, false);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_OPEN_ON_PERSPECTIVE_BAR, false);
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, 
				VRPerspective.ID_PERSPECTIVE
				+","+CDRPerspective.ID_PERSPECTIVE
				+","+smsPerspective.ID_PERSPECTIVE
				//+","+WebPerspective.ID_PERSPECTIVE
				);
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_RIGHT);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.OPEN_NEW_PERSPECTIVE, IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_WINDOW);
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, false);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.INITIAL_FAST_VIEW_BAR_LOCATION, IWorkbenchPreferenceConstants.BOTTOM);
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.VIEW_TAB_POSITION,SWT.TOP);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.EDITOR_TAB_POSITION,SWT.TOP);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.ENABLE_DETACHED_VIEWS,false);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID,myWorkbenchPresentationFactory.ID);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_SIZE,340);
		
		//PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.ENABLE_NEW_MIN_MAX,false);
		//PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID,R21PresentationFactory.ID_PRESENTATION);
		
		
		/*ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		
		CCKPConnection connection = new CCKPConnection();
		
		try{
			connection.connect(connectionConfig);
		}
		catch(CCKPConnectionException e){
			e.printStackTrace();
			return IApplication.EXIT_OK;
		}
		
		session.setConnection(connection);
		*/
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		
		try {
			context.applicationRunning();
			int returnCode =IApplication.EXIT_OK;
		
			if(login(Activator.getSwitchUsersSession()))
			{
				returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			}
			
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			if(session != null){
			if(session.getConnection() != null){
				session.getConnection().disconnect();
				session.getConnection().cleanup();
			}
			if(session.dbConnection() != null){
				session.dbConnection().close();
			}
			}
			if(display != null){
				display.dispose();
			}
		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		if (display == null)
			return;
		try{
		display.syncExec(new Runnable() {
			public void run() {
				try{
					if (!display.isDisposed())
						workbench.close();
				}
				catch(Exception e){
					
				}
			}
		});
		}
		catch(Exception e){
			
		}
	}
	
	
	private boolean login( final SwitchUsersSession session){
		
		//IDialogConstants.OK_LABEL = "确定";
		//IDialogConstants.CANCEL_LABEL = "取消";
		ConnectionConfiguration detail = new ConnectionConfiguration();
		
		/*MyResource resouce = MyResource.getResource("loginfo.ini");
		if(resouce != null){
			if(resouce.loadProperty()){
			
				detail.setServerHost(resouce.getProperty("host"));
				detail.setServerTcpPort(resouce.getProperty("port"));
				detail.setPhoneId(resouce.getProperty("id"));
				detail.setUser(resouce.getProperty("user"));
			}
			
		}
		*/
		
		LoginInfo info = LoginInfo.getSingleton();
		if(info != null && info.exists()){
			String host = info.getProperty("host");
			String port = info.getProperty("port");
			String id = info.getProperty("id");
			String user = info.getProperty("user");
			if(host != null && host.length()>0){
				detail.setServerHost(host);
			}
			if(port != null && port.length()>0){
				detail.setServerTcpPort(port);
			}
			if(id != null && id.length()>0){
				detail.setPhoneId(id);
			}
			if(user != null && user.length()>0){
				detail.setUser(user);
			}
			
		
		}
		while(session.getConnection() == null ||
				!session.getConnection().isAuthed() ){
			
			LoginDialog dialog  = new LoginDialog(null,this.connectionMsg,detail);
			
			if(dialog.open() != Window.OK){
				return false;
			}
			detail = dialog.getConnectionDetails();
			if(info != null){
				info.setProperty("host", detail.getServerHost());
				info.setProperty("port", String.valueOf(detail.getServerTcpPort()));
				info.setProperty("id", detail.getPhoneId());
				info.setProperty("user", detail.getUser());
				
				info.storeProperty();
			}
			session.setConnectionDetails(detail);
			connectWithProgress(session);
			
		}
		return true;
	}

	private void connectWithProgress(final SwitchUsersSession session) {
		mnDialog progress = new mnDialog(null);
	     
		 // ProgressMonitorDialog.setDefaultImage(null);
		  
		  progress.setCancelable(false);
		 // progress.setOpenOnRun(true);
		  try {
		    progress.run(true, true, new IRunnableWithProgress() {
		      public void run(IProgressMonitor monitor)
		          throws InvocationTargetException {
		        try {
		        	//monitor.setTaskName("");
		          session.connectAndLogin(monitor);
		        } catch (CCKPConnectionException e) {
		        	
				  connectionMsg = e.getMessage();
		          throw new InvocationTargetException(e);
		        }
		        finally{
		        	try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}
		        }
		       
		      }
		    });
		  } catch (InvocationTargetException e) {
			  
		  } catch (InterruptedException e) {
		  }
	}
}
