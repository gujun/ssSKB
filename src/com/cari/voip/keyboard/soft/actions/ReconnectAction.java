package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ReconnectAction extends Action implements IWorkbenchAction {

	private IWorkbenchWindow window;
	public ReconnectAction(){
		super();
		this.setId("org.eclipse.jface.action.Action.ReconnectAction");
	}
	public ReconnectAction(IWorkbenchWindow window) {
		super();
		this.window = window;
		this.setId("org.eclipse.jface.action.Action.ReconnectAction");
	}
	@Override
	public void dispose() {
		
	}
	public void run(){
		if(this.window != null){
			if(MessageDialog.openConfirm(this.window.getShell(), "ȷ��", "      ȷ���Ͽ���ǰ���ӣ����������ӣ�")){
				this.window.getWorkbench().restart();
			}
		}
	}
}