package com.cari.voip.keyboard.soft.editors.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;

public class SwitchUserInput implements IEditorInput {

	private SwitchUsersGroup group;
	
	public SwitchUserInput(SwitchUsersGroup group){
		this.group = group;
	}
	public SwitchUsersGroup getSwitchUsersGroup(){
		return this.group;
	}
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SwitchUserInput";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "SwitchUserInput";
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
