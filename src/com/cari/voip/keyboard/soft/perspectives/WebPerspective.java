package com.cari.voip.keyboard.soft.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.cari.voip.keyboard.soft.views.BookMarksView;
import com.cari.voip.keyboard.soft.views.DialView;
import com.cari.voip.keyboard.soft.views.WebBrowserView;

public class WebPerspective implements IPerspectiveFactory {

	public static final String ID_PERSPECTIVE = 
		"com.cari.voip.keyboard.soft.perspectives.webPerspective";
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		String editorAreaId = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		IFolderLayout leftFolder = 
			layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorAreaId);
		
		leftFolder.addView(BookMarksView.ID_VIEW);
		
		IFolderLayout rightFolder = 
			layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorAreaId);
		rightFolder.addPlaceholder(WebBrowserView.ID_VIEW+":*");

		//rightFolder.addView(WebBrowserView.ID_VIEW);
		//rightFolder.addView(WebBrowserView.ID_VIEW+":1");
		
		//layout.addView(BookMarksView.ID_VIEW, IPageLayout.LEFT, 0.2f, editorAreaId);
		//layout.addView(WebBrowserView.ID_VIEW, IPageLayout.RIGHT, 0.8f, editorAreaId);
		
		layout.getViewLayout(BookMarksView.ID_VIEW).setCloseable(false);
	}

}
