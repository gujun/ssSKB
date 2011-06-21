package com.cari.voip.keyboard.soft;



import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.editors.SwitchUserGraphicalEditor;
import com.cari.voip.keyboard.soft.editors.input.SwitchUserInput;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.views.GraphGroupView;
import com.cari.voip.keyboard.soft.views.GraphNodeView;
import com.cari.voip.keyboard.stack.ConnectionConfiguration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.cari.voip.keyboard.soft";

	//public static ISharedImages sharedImgs = PlatformUI.getWorkbench().getSharedImages();
	// The shared instance
	private static Activator plugin;
	
	private static  SwitchUsersSession switchUsersSession = null;
	
	private static Display display = null;// PlatformUI.createDisplay();
	
	public static boolean usersFirstShow = true;
	public static boolean groupsFirstShow = true;
	
	public static SwitchUsersAdapterFactory usersAdapterFactory = new SwitchUsersAdapterFactory();
	
	private static int viewIndex = 0;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static IViewPart getView(IWorkbenchWindow workbenchWindow,
			String idView) {
		// TODO Auto-generated method stub
		IViewReference[] refs = workbenchWindow.getActivePage().getViewReferences();
		for(IViewReference viewReference:refs){
			if(viewReference.getId().equals(idView)){
				return viewReference.getView(true);
			}
		}
		return null;
	}
	/* public static Image createImage(String name) {
		 InputStream stream = Activator.class.getResourceAsStream(name);
			Image image = new Image(null,stream);
			try {
				stream.close();
			} catch (IOException ioe) {
			}
			return image;
	 }*/
	
	public static  SwitchUsersSession getSwitchUsersSession(){
		if( Activator.switchUsersSession == null){
			Activator.switchUsersSession  = new SwitchUsersSession();
		}
		return Activator.switchUsersSession ;
	}
	
	public static Display getDisplay(){
		if(display == null){
			display = PlatformUI.createDisplay();
		}
		return display;
	}
	
	/*public static  void showGroupViewFromModel(IWorkbenchPage page,Object obj){
		if(page == null || obj == null){
			return;
		}
		if(obj instanceof SwitchUsersManager){
			GraphGroupView view = null;
			IViewReference[] viewRefs = page.getViewReferences();
			for(IViewReference viewRef:viewRefs){
				if(viewRef.getId().equals(GraphGroupView.ID_VIEW)){
					GraphGroupView viewTmp = (GraphGroupView)viewRef.getView(true);
					if(viewTmp != null && viewTmp.getModel().equals(obj)){
						view = viewTmp;
					}
				}
			}
			if(view == null){
				try{
				view =
				(GraphGroupView)page.showView(GraphGroupView.ID_VIEW, 
						Integer.toString(viewIndex++), IWorkbenchPage.VIEW_ACTIVATE);
				view.setName(((SwitchEntity)obj).getName());
				view.setAdapterFactory(new SwitchUsersAdapterFactory());
				view.setModel(obj);
				view.makeGraphNodesFromModel();
				
				}catch(Exception e){
				if(view != null){
					view.dispose();
				}
				e.printStackTrace();
				}
			}else{
				page.activate(view);
			}
		}
	}*/
	public static  void showUserViewFromModel(IWorkbenchPage page,Object obj){
		if(page == null || obj == null){
			return;
		}
		if(obj instanceof SwitchUsersGroup){
			GraphNodeView view = null;
			IViewReference[] viewRefs = page.getViewReferences();
			for(IViewReference viewRef:viewRefs){
				if(viewRef.getId().equals(GraphNodeView.ID_VIEW)){
					GraphNodeView viewTmp = (GraphNodeView)viewRef.getView(true);
					if(viewTmp != null && viewTmp.getModel().equals(obj)){
						view = viewTmp;
					}
				}
			}
			if(view == null){
				try{
				view =
				(GraphNodeView)page.showView(GraphNodeView.ID_VIEW, 
						Integer.toString(viewIndex++), IWorkbenchPage.VIEW_ACTIVATE);
				view.setName(((SwitchEntity)obj).getName());
				view.setAdapterFactory(new SwitchUsersAdapterFactory());
				view.setModel(obj);
				view.makeGraphNodesFromModel();
				
				}catch(Exception e){
				if(view != null){
					view.dispose();
				}
				e.printStackTrace();
				}
			}else{
				page.activate(view);
			}
		}
	}
	protected void activeEditorFromModel(IWorkbenchPage page,Object obj){
		if(page == null && obj == null){
			return;
		}
		if(obj instanceof SwitchUsersGroup){
			//IWorkbenchPage page = getSite().getPage();
			IEditorPart editor = null;
			IEditorInput input = null;
			IEditorReference[] editorRefs = page.getEditorReferences();
			for(IEditorReference editorRef:editorRefs){
				IEditorInput inputTmp = null;
				try {
					inputTmp = editorRef.getEditorInput();
				} catch (PartInitException e) {
					inputTmp = null;
				}
				if(inputTmp == null){
					continue;
				}
				if(inputTmp instanceof SwitchUserInput){
					if(((SwitchUserInput)inputTmp).getSwitchUsersGroup().equals(obj)){
						editor = editorRef.getEditor(true);
						break;
					}
				}
			}
			if(editor == null){
				input = new SwitchUserInput((SwitchUsersGroup)obj);
			
				try{
					editor = page.openEditor(input, SwitchUserGraphicalEditor.ID, true);
					if(editor instanceof SwitchUserGraphicalEditor){
						((SwitchUserGraphicalEditor)editor).setName(
							((SwitchUsersGroup)obj).getName());
					}
				
				
				}catch(Exception e){
					if(editor != null){
						editor.dispose();
					}
					e.printStackTrace();
				
				}
			}//if(editor == null)
			else{
				page.activate(editor);
			}
		}//if(obj instanceof SwitchUsersGroup)
	}
	public static String getHeaderText(){
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			ConnectionConfiguration detail = session.getConnectionDetail();
			if(detail != null){
				return detail.getUser();
			}
		}
		return "天地(常州)自动化股份有限公司调度指挥中心";
	}
}
