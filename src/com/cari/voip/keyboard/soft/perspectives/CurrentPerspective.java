package com.cari.voip.keyboard.soft.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


import com.cari.voip.keyboard.soft.views.CallFailView;
import com.cari.voip.keyboard.soft.views.CallProgressView;
import com.cari.voip.keyboard.soft.views.ControllerView;
import com.cari.voip.keyboard.soft.views.DialView;
import com.cari.voip.keyboard.soft.views.EventView;
import com.cari.voip.keyboard.soft.views.GraphGroupView;
import com.cari.voip.keyboard.soft.views.GraphNodeView;
import com.cari.voip.keyboard.soft.views.MsgView;
import com.cari.voip.keyboard.soft.views.StatusView;
import com.cari.voip.keyboard.soft.views.WebBrowserView;

public class CurrentPerspective implements IPerspectiveFactory {

	public static final String ID_PERSPECTIVE = 
		"com.cari.voip.keyboard.soft.perspectives.CurrentPerspective";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorAreaId = null;
		layout.setEditorAreaVisible(false);
		editorAreaId = layout.getEditorArea();
		
		IFolderLayout leftFolder = 
			layout.createFolder("left", IPageLayout.LEFT, 0.1f, editorAreaId);
		leftFolder.addView(ControllerView.ID_VIEW);
		
		/*IFolderLayout leftBottomFolder = 
			layout.createFolder("leftBottom", IPageLayout.BOTTOM, 0.8f, ControllerView.ID_VIEW);
		leftBottomFolder.addView(CallProgressView.ID_VIEW);*/
		
		IFolderLayout rightFolder = 
			layout.createFolder("right", IPageLayout.RIGHT, 0.76f, editorAreaId);
		rightFolder.addView(MsgView.ID_VIEW);
		rightFolder.addView(DialView.ID_VIEW);
		//rightFolder.addView(StatusView.ID_VIEW);
		
		IFolderLayout rightCenterFolder = 
			layout.createFolder("rightCenter", IPageLayout.BOTTOM, 0.5f, DialView.ID_VIEW);
		rightCenterFolder.addView(CallFailView.ID_VIEW);
		
		IFolderLayout rightBottomFolder = 
			layout.createFolder("rightBottom", IPageLayout.BOTTOM, 0.5f, CallFailView.ID_VIEW);
		
		rightBottomFolder.addView(CallProgressView.ID_VIEW);
		rightBottomFolder.addView(EventView.ID_VIEW);
		
		IFolderLayout centerFolder = 
			layout.createFolder("center", IPageLayout.TOP, 0.75f, editorAreaId);
		centerFolder.addPlaceholder(GraphNodeView.ID_VIEW+":*");
		centerFolder.addView(GraphNodeView.ID_VIEW);
		//centerFolder.addView(GraphNodeView.ID_VIEW+":1");
		
		IFolderLayout centerBottomFolder = 
			layout.createFolder("centerBottom", IPageLayout.BOTTOM, 0.5f, editorAreaId);
		centerBottomFolder.addPlaceholder(GraphGroupView.ID_VIEW+":*");
		centerBottomFolder.addView(GraphGroupView.ID_VIEW);
		//centerBottomFolder.addView(GraphGroupView.ID_VIEW);
		centerBottomFolder.addView(GraphGroupView.ID_VIEW+":1");
		
		
		layout.addPerspectiveShortcut(ID_PERSPECTIVE);
		layout.addPerspectiveShortcut(CDRPerspective.ID_PERSPECTIVE);
		
		layout.addShowViewShortcut(ControllerView.ID_VIEW);
		layout.addShowViewShortcut(EventView.ID_VIEW);
		layout.addShowViewShortcut(CallFailView.ID_VIEW);
		layout.addShowViewShortcut(CallProgressView.ID_VIEW);
		layout.addShowViewShortcut(StatusView.ID_VIEW);
		layout.addShowViewShortcut(DialView.ID_VIEW);
		
		layout.getViewLayout(ControllerView.ID_VIEW).setCloseable(false);
		//layout.getViewLayout(ControllerView.ID_VIEW).setMoveable(false);
		
		layout.getViewLayout(EventView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(CallFailView.ID_VIEW).setCloseable(false);
		
		layout.getViewLayout(CallProgressView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(StatusView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(DialView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(MsgView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(GraphGroupView.ID_VIEW).setCloseable(false);
		layout.getViewLayout(GraphGroupView.ID_VIEW+":1").setCloseable(false);
		
		//layout.getViewLayout(GroupView.ID_VIEW).setCloseable(false);
	     
		
		
	}
}
