package com.cari.voip.keyboard.soft.views.shell;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class userShell {
	private static  Shell shell;
	private static Display displayDevice;
	private static TreeViewer treeViewer;
	private static IAdapterFactory adapterFactory = new SwitchUsersAdapterFactory();
	private static SwitchUsersSession session;
	private static IDoubleClickListener dlistern;
	
	public  static Shell getShell(Display displayDevice){
		if(shell == null){
			//displayDevice = Activator.getDisplay();
			shell = new Shell(displayDevice,SWT.ON_TOP);
			shell.setLayout(new FillLayout());
			treeViewer = new TreeViewer(shell,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
			Platform.getAdapterManager().registerAdapters(adapterFactory,SwitchUsersManager.class);
			Platform.getAdapterManager().registerAdapters(adapterFactory,SwitchUsersGroup.class);
			Platform.getAdapterManager().registerAdapters(adapterFactory,SwitchUser.class);
			Platform.getAdapterManager().registerAdapters(adapterFactory,SwitchUsersSession.class);
			session = Activator.getSwitchUsersSession();
			//getSite().setSelectionProvider(this.treeViewer);
			treeViewer.setLabelProvider(new WorkbenchLabelProvider());
			treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
			//this.treeViewer.setInput(this.session.getTreeRoot());
			treeViewer.setInput(session.getlocalUserManager());
			shell.pack();
			treeViewer.getControl().addFocusListener(new FocusListener (){
				public void focusGained(FocusEvent e){
					
				}

				
				public void focusLost(FocusEvent e){
					if(shell.isVisible()){
						
						shell.setVisible(false);
						
					}
				}
			});
			treeViewer.addDoubleClickListener(new IDoubleClickListener(){

				@Override
				public void doubleClick(DoubleClickEvent event) {
					if(shell.isVisible()){
						
						shell.setVisible(false);
						
					}
				}
				
			});
			
		}else{
			treeViewer.refresh();
			shell.pack();
		}
		return shell;
	}
	
	public static void setVisible(Display displayDevice,Rectangle dateRect,IDoubleClickListener listener){
		//if(shell == null){
			getShell(displayDevice);
		//}
		if(shell == null || treeViewer == null){
			return;
		}
		if(listener != dlistern){
			if(dlistern != null){
				treeViewer.removeDoubleClickListener(dlistern);
			}
			if(listener != null){
				treeViewer.addDoubleClickListener(listener);
				dlistern = listener;
			}
		}
		Rectangle calRect= shell.getBounds();
		
		shell.setBounds(
				dateRect.x, dateRect.y+dateRect.height-200, calRect.width, calRect.height<100?100:calRect.height
						);
		shell.setVisible(true);
		treeViewer.getControl().setFocus();
		
	}
	
}
