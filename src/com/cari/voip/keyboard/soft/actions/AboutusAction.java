package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.cari.voip.keyboard.soft.dialogs.AboutDialog;
import com.cari.voip.keyboard.soft.dialogs.LoginDialog;

public class AboutusAction extends Action implements IWorkbenchAction {
	private IWorkbenchWindow window;
	public AboutusAction(IWorkbenchWindow window) {
		super();
		this.window = window;
		this.setId("org.eclipse.jface.action.Action.AboutusAction");
	}
	@Override
	public void dispose() {
		
	}
	public void run(){
		AboutDialog dialog  = new AboutDialog(null);
		dialog.open();
		
	}

}
