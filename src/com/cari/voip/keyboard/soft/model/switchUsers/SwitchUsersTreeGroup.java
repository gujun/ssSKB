package com.cari.voip.keyboard.soft.model.switchUsers;

import java.util.ArrayList;

public class SwitchUsersTreeGroup extends SwitchUsersTree {

	private SwitchUsersTreeGroup parent;
	private String name;
	private ArrayList<Object> entries;
	
	public SwitchUsersTreeGroup(SwitchUsersTreeGroup parent,String name){
		this.parent = parent;
		this.name = name;
		this.entries = new ArrayList<Object>();
		
		if(this.getParent() != null){
			this.getParent().addEntry(this);
		}
	}
	
	public SwitchUsersTreeGroup getParent(){
		return this.parent;
	}
	public String getName(){
		return this.name;
	}
	public String setName(String newName){
		String oldName = this.name;
		this.name = newName;
		return oldName;
	}
	
	public boolean addEntry(Object entry ){
		return this.entries.add(entry);
	}
	
	public boolean removeEntry(Object entry){
		return this.entries.remove(entry);
	}
	
	public Object[] getEntries(){
		return this.entries.toArray();
	}
	
}
