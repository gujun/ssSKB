package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class DisconnectAction extends Action implements IWorkbenchAction {

	private IWorkbenchWindow window;
	public DisconnectAction(IWorkbenchWindow window) {
		super();
		this.window = window;
		this.setId("org.eclipse.jface.action.Action.DisconnectAction");
	}
	@Override
	public void dispose() {
		
	}
	public void run(){
		this.window.getWorkbench().restart();
	}
}