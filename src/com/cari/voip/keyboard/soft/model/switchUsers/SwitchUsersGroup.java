package com.cari.voip.keyboard.soft.model.switchUsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;



public class SwitchUsersGroup extends SwitchEntity {
	
	public static final String PROP_USER_ADD="SwitchUsersGroup.userAdd";
	public static final String PROP_USER_REMOVE="SwitchUsersGroup.userRemove";
	
	public static final String PROP_GROUP_REMOVE = "SwitchUsersGroup.groupRemove";
	
	private SwitchUsersManager parent;
	private String name;
	private String groupCallName;
	private Hashtable<String,SwitchUser> members;
	private Hashtable<String,Long> membersRelation;
	protected SwitchUsersGroup(SwitchUsersManager parent,String name){
		this(parent,name,5);
	}
	protected SwitchUsersGroup(SwitchUsersManager parent,String name,int initialCapacity){
		this.parent = parent;
		this.name = name;
		this.groupCallName = name;
		this.members = new Hashtable<String,SwitchUser>(initialCapacity);
		this.membersRelation = new  Hashtable<String,Long>(5);
	}
	public String getName(){
		return this.name;
	}
	public String getTooltipString(){
		String tooltip = " ";
		tooltip = tooltip.concat(this.name + "£® "+String.valueOf(this.size())+" ≥…‘±£© ");
		return tooltip;
	}
	public SwitchUsersManager getParent(){
		return this.parent;
	}
	
	public String getGroupCallName(){
		return this.groupCallName;
	}
	/*public String setName(String newName){
		String oldName = this.name;
		this.name = newName;
		return oldName;
	}*/
	public String setGroupCallName(String newGroupCallName){
		String oldGroupCallName = this.groupCallName;
		this.groupCallName = newGroupCallName;
		return oldGroupCallName;
	}
	public SwitchUser getSwitchUser(String userId){
		SwitchUser user=null;
		if(userId != null){
			user = (SwitchUser)this.members.get(userId);
		}
		return user;
	}
	public Collection<SwitchUser> getAllSwitchUser(){
		if(this.members.isEmpty()){
			return null;
		}
		Collection<SwitchUser> userCollection =
			this.members.values();
		return userCollection;
	}
	public Object[] getSwitchUsersArray(){
		if(this.members.isEmpty()){
			return new SwitchUser[0];
		}
		Collection<SwitchUser> userCollection =
			this.members.values();
		return userCollection.toArray(new SwitchUser[] {});
	}
	public List<SwitchUser> getAllSwitchUsersList(){
		ArrayList<SwitchUser> list= 
			new ArrayList<SwitchUser>(this.members.values());
		
		return list;
	}
	public SwitchUser addSwitchUser(SwitchUser user){
		SwitchUser oldUserForThisId = null;
		if(user == null || user.getUserId()==null){ 
			return null;
		}
		user.addOfGroup(this.name);
		oldUserForThisId =(SwitchUser)this.members.put(user.getUserId(), user);
		this.updateRelationTimer(user);
		this.firePropertyChange(PROP_USER_ADD, oldUserForThisId, user);
		return oldUserForThisId;
	}
	public void updateRelationTimer(SwitchUser user){
		this.membersRelation.put(user.getUserId(), new Long(this.getRefreshTimer()));
	}
	public SwitchUser removeSwitchUser(SwitchUser user){
		SwitchUser userForThisId = null;
		if(user == null){
			return null;
		}
		userForThisId = this.removeSwitchUser(user.getUserId());
		
		return userForThisId;
	}
	public SwitchUser removeSwitchUser(String userId){
		SwitchUser user = null;
		if(userId != null){
			user = this.members.remove(userId);
			if(user != null){
				user.removeOfGroup(this.name);
				this.firePropertyChange(PROP_USER_REMOVE, null, user);
			}
		}
		
		return user;
	}
	public void removeAll(){
		if(!this.members.isEmpty()){
			Enumeration<SwitchUser> userEnum =
				this.members.elements();
			while(userEnum.hasMoreElements()){
				SwitchUser user = userEnum.nextElement();
				user.removeOfGroup(this.name);
			}
		}
		
		this.members.clear();
		this.firePropertyChange(PROP_GROUP_REMOVE, null, this.parent.getName());
	}
	public void removeOldRelation(){
		if(this.membersRelation.isEmpty()){
			return;
		}
		String[] ids = (String [])this.membersRelation.keySet().toArray(new String[] {});
		for(String id:ids){
			if(id != null){
				Long timer = this.membersRelation.get(id);
				if(timer.longValue() < this.getRefreshTimer()){
					this.removeSwitchUser(id);
					this.membersRelation.remove(id);
				}
			}
		}
	}
	public int size(){
		return this.members.size();
	}
	public boolean isEmpty(){
		return this.members.isEmpty();
	}
	
}
