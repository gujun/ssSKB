package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ExitAction extends Action implements IWorkbenchAction {

	private IWorkbenchWindow window;
	private  IWorkbenchAction exitAction_;
	public ExitAction(IWorkbenchWindow window) {
		super();
		this.window = window;
		if(this.window != null){
			this.exitAction_= ActionFactory.QUIT.create(this.window);
		}
		this.setId("org.eclipse.jface.action.Action.ExitAction");
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		if(this.exitAction_ != null){
			this.exitAction_.dispose();
		}
	}
	public void run(){
		if(this.window != null && this.exitAction_ != null){
			if(MessageDialog.openConfirm(this.window.getShell(), "确认", "      确定推出IP调度通信系统控制台软件？")){
				this.exitAction_.run();
			}
		}
	}
}
