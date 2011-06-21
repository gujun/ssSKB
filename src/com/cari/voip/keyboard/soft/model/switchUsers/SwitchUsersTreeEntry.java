package com.cari.voip.keyboard.soft.model.switchUsers;

import com.cari.voip.keyboard.soft.model.Presence;

public class SwitchUsersTreeEntry extends SwitchUsersTree {
	
	private SwitchUsersTreeGroup parent;
	private String name;
	private Presence presence;
	
	public SwitchUsersTreeEntry(SwitchUsersTreeGroup parent,String name,Presence presence){
		this.parent = parent;
		this.name = name;
		this.presence = presence;
		
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
	
	public  SwitchUsersTreeGroup getParent(){
		return this.parent;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Presence getPresence(){
		return this.presence;
	}
	
	
}
