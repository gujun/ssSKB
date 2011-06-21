package com.cari.voip.keyboard.soft;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.internal.presentations.util.ISystemMenu;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;

@SuppressWarnings("restriction")
public class myStandardViewSystemMenu implements ISystemMenu {
	MenuManager menuManager = new MenuManager();
	public myStandardViewSystemMenu(IStackPresentationSite site){
		site.addSystemActions(menuManager);
	}

	@Override
	public void dispose() {
		menuManager.dispose();
        menuManager.removeAll();
	}

	@Override
	public void show(Control parent, Point displayCoordinates,
			IPresentablePart currentSelection) {
		menuManager.removeAll();
		Menu aMenu = menuManager.createContextMenu(parent);
        //menuManager.update(true);
        aMenu.setLocation(displayCoordinates.x, displayCoordinates.y);
        aMenu.setVisible(false);
	}
}
