package com.cari.voip.keyboard.soft.model.conf;

public class SwitchsGroup extends ContactsGroup {
	
	public SwitchsGroup(ContactsGroup parent,String name){
		super(parent,name);
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
}
