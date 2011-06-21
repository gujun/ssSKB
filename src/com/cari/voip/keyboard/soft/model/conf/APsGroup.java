package com.cari.voip.keyboard.soft.model.conf;

public class APsGroup extends ContactsGroup {

	public APsGroup(ContactsGroup parent,String name){
		super(parent,name);
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
}
