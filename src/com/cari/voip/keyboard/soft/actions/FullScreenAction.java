package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.cari.voip.keyboard.soft.Activator;

public class FullScreenAction extends Action implements IWorkbenchAction {

	private IWorkbenchWindow window;
	public FullScreenAction(IWorkbenchWindow window) {
		super();
		this.window = window;
		this.setId("org.eclipse.jface.action.Action.FullScreenAction");
		//checkScreen();
		
	}
	@Override
	public void dispose() {
		
	}
	public void run(){
		if(this.window.getShell().getFullScreen()){
			this.window.getShell().setFullScreen(false);
		}
		else{
			this.window.getShell().setFullScreen(true);
		}
		checkScreen();
	}
	public void checkScreen(){
		if(this.window.getShell().getFullScreen()){
			this.setText("ȡ��ȫ��");
			this.setToolTipText("ȡ��ȫ����ʾģʽ");
			this.setImageDescriptor(Activator.getImageDescriptor("icons/notfull.gif"));
		}
		else{
			this.setText("ȫ��");
			this.setToolTipText("����ȫ����ʾģʽ");
			this.setImageDescriptor(Activator.getImageDescriptor("icons/full.gif"));
		}
	}
}
