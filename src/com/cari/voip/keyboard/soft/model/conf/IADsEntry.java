package com.cari.voip.keyboard.soft.model.conf;

public class IADsEntry extends ContactsEntry {

	public IADsEntry(ContactsGroup group,String name,String url){
		super(group,name,url);
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
}
