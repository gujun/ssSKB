package com.cari.voip.keyboard.soft.model.conf;

public class APsEntry extends ContactsEntry {

	public APsEntry(ContactsGroup group, String name, String url){
		super(group,name,url);
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
}
