package com.cari.voip.keyboard.soft.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.cari.voip.keyboard.soft.views.CDRView;

public class CDRPerspective implements IPerspectiveFactory {

	public static final String ID_PERSPECTIVE = 
		"com.cari.voip.keyboard.soft.perspectives.CDRPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		
		layout.addStandaloneView(CDRView.ID_VIEW, false, IPageLayout.RIGHT, 1.0f, layout.getEditorArea());
		
		layout.setEditorAreaVisible(false);
		
		layout.addPerspectiveShortcut(ID_PERSPECTIVE);
		layout.addPerspectiveShortcut(CurrentPerspective.ID_PERSPECTIVE);
		
		layout.addShowViewShortcut(CDRView.ID_VIEW);
		
	}

}
