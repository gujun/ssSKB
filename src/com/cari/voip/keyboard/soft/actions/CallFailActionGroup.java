package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.actions.ActionGroup;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.views.CallFailView;


public class CallFailActionGroup extends ActionGroup {
	private CallFailView v;
	private Action delete;
	private Action callBack;
	private class deleteAction extends Action {
		public deleteAction(){
			setText("»∑»œ");
			setImageDescriptor(Activator.getImageDescriptor("icons/check.gif"));
		}
		public void run(){
			v.onDeleteClick();
		}
	}
	private class callBackAction extends Action {
		public callBackAction(){
			setText("ªÿ∫Ù");
			setImageDescriptor(Activator.getImageDescriptor("icons/telephone16.png"));
		}
		public void run(){
			v.onCallBack();
		}
	}
	
	public CallFailActionGroup(CallFailView view){
		this.v = view;
	}
	
	public Action getDeleteAction(){
		if(delete == null){
			delete = new deleteAction();
		}
		return delete;
	}
	public Action getCallBackAction(){
		if(callBack == null){
			callBack = new callBackAction();
		}
		return callBack;
	}
	public Menu fillContextMemu(IMenuManager mgr){
		MenuManager menuManager = (MenuManager)mgr;
		
		menuManager.add(getDeleteAction());
		menuManager.add(getCallBackAction());
		
		Table tb = v.getTable();
		Menu menu = menuManager.createContextMenu(tb);
		tb.setMenu(menu);
		return menu;
	}
}
