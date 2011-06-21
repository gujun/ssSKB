package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.actions.ActionGroup;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.views.VRView;

public class recordingActionGroup extends ActionGroup {
	private VRView v;
	private class tryAction extends Action {
		public tryAction(){
			setText(" ‘Ã˝¬º“Ù");
			setImageDescriptor(Activator.getImageDescriptor("icons/hear.gif"));
		}
		public void run(){
			v.onTryClick();
		}
	}
	private class deleteAction extends Action {
		public deleteAction(){
			setText("…æ≥˝¬º“Ù");
			setImageDescriptor(Activator.getImageDescriptor("icons/cut_red.png"));
		}
		public void run(){
			v.onDeleteClick();
		}
	}
	private class downloadAction extends Action {
		public downloadAction(){
			setText("œ¬‘ÿ¬º“Ù");
			setImageDescriptor(Activator.getImageDescriptor("icons/down.png"));
		}
		public void run(){
			v.onDownloadClick();
		}
	}
	public recordingActionGroup(VRView view){
		this.v = view;
	}
	
	public Menu fillContextMemu(IMenuManager mgr){
		MenuManager menuManager = (MenuManager)mgr;
		menuManager.add(new tryAction());
		menuManager.add(new downloadAction());
		menuManager.add(new deleteAction());
		
		Table tb = v.getTable();
		Menu menu = menuManager.createContextMenu(tb);
		tb.setMenu(menu);
		return menu;
	}
}
