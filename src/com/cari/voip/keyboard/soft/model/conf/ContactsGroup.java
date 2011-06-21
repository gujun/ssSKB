package com.cari.voip.keyboard.soft.model.conf;

//import java.util.List;
import java.util.ArrayList;

public class ContactsGroup extends Contact {

	private ContactsGroup parent;
	private String name;
	private ArrayList<Object> entries;
	
	public ContactsGroup(ContactsGroup parent, String name){
		this.parent = parent;
		this.name = name;
		this.entries = new ArrayList<Object>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public ContactsGroup getParent(){
		return this.parent;
	}
	
	public String rename(String newName){
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
