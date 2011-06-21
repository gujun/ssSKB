package com.cari.voip.keyboard.soft.model.conf;

import com.cari.voip.keyboard.soft.model.Presence;

public class ContactsEntry extends Contact {
	
	private ContactsGroup group;
	private String name;
	private String url;
	private Presence presence;
	
	public ContactsEntry(ContactsGroup group,String name,String url){
		this.group = group;
		this.name = name;
		this.url = url;
		this.presence = Presence.ON_LINE;
	}
	
	public ContactsGroup getParent(){
		return this.group;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getUrl(){
		return this.url;
	}
	public Presence getPresence(){
		return this.presence;
	}
	
	public Presence setPresence(Presence newPresence){
		Presence oldPresence = this.presence;
		this.presence = newPresence;
		return oldPresence;
	}
}
