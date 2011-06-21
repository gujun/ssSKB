package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.cari.voip.keyboard.soft.Activator;

public class RefreshAction extends Action implements IWorkbenchAction {

	
	public RefreshAction(){
		super();
		this.setId("org.eclipse.jface.action.Action.RefreshAction");
	}
	
	@Override
	public void dispose() {
		
	}
	public void run(){
		try{
			Activator.getSwitchUsersSession().query();
		}
		catch(Exception e){
			
		}
	}
}
