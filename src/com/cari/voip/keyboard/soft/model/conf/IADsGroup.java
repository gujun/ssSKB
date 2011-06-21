package com.cari.voip.keyboard.soft.model.conf;

public class IADsGroup extends ContactsGroup {
	public IADsGroup(ContactsGroup parent, String name){
		super(parent,name);
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
}
