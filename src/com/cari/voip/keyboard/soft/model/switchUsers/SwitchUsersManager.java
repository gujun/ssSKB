package com.cari.voip.keyboard.soft.model.switchUsers;

import java.util.Collection;
import java.util.Enumeration;
//import java.util.Collections;
import java.util.Hashtable;
//import java.util.TreeMap;

public class SwitchUsersManager extends SwitchEntity {

	public static final String PROP_GROUP_ADD = "SwitchUsersManager.GroupAdd";
	public static final int gateway_capacity = 5;
	
	private SwitchUsersGroup members;
	private Hashtable<String,SwitchUsersGroup> groups;
	private int groupMemberCapacity=5;
	private String name;
	//private TreeMap<String,SwitchUser> members;
	public SwitchUsersManager(String name,int usersCapacity,int groupsCapacity){
		if(usersCapacity <= 0){
			usersCapacity = 101;
		}
		if(groupsCapacity <= 0){
			groupsCapacity = 5;
		}
		this.name = name;
		this.members = new SwitchUsersGroup(this,this.name+"所有用户",usersCapacity);
		this.members.setGroupCallName("default");
		this.groups = new Hashtable<String,SwitchUsersGroup>(groupsCapacity);
		
		this.groupMemberCapacity = usersCapacity/(groupsCapacity*2);
	}
	public SwitchUsersManager(int usersCapacity){
		this("缺省",usersCapacity,5);
	}
	public SwitchUsersManager(String name){
		this(name,101,5);
	}
	public SwitchUsersManager(){
		this("缺省",101,5);
	}
	
	//user get
	public SwitchUser getSwitchUser(String userId){
		SwitchUser user=null;
		if(userId != null){
			user = (SwitchUser)this.members.getSwitchUser(userId);
		}
		return user;
	}
	public Collection<SwitchUser> getAllSwitchUser(){
		if(this.members.isEmpty()){
			return null;
		}
		Collection<SwitchUser> userCollection =
			this.members.getAllSwitchUser();
		return userCollection;
	}
	
	//user add(not check reduntency)
	public SwitchUser addSwitchUser(String userId){
		if(userId == null || userId.length() ==0){
			return null;
		}
		SwitchUser user = new SwitchUser(userId);
		this.addSwitchUser(user);
		return user;
	}
	//user add(not check reduntency)
	private SwitchUser addSwitchUser(SwitchUser user){
		if(user == null || user.getUserId()==null ||
				user.getUserId().length()==0){
			return null;
		}
		user.setRefreshTimer(this.getRefreshTimer());
		return (SwitchUser)this.members.addSwitchUser(user);
	}
	
	
	//group get
	public  SwitchUsersGroup getSwitchUsersGroup(String groupName){
		SwitchUsersGroup group=null;
		if(groupName != null){
			group = (SwitchUsersGroup)this.groups.get(groupName);
		}
		return group;
	}
	public SwitchUsersGroup getMembersGroup(){
		return this.members;
	}
	public Collection<SwitchUsersGroup> getAllSwitchUsersGroup(){
		if(this.groups.isEmpty()){
			return null;
		}
		Collection<SwitchUsersGroup> groupCollection =
			this.groups.values();
		return groupCollection;
	}
	
	public SwitchUsersGroup[] toGroupsArray(){
		
		Collection<SwitchUsersGroup> groupCollection = 
			this.getAllSwitchUsersGroup();
		int ArrayLen = 1;
		SwitchUsersGroup[] groups;
		if(this.groupSize() > 0){
			Object[] groupsT = groupCollection.toArray();
			ArrayLen += groupsT.length;
			groups = new SwitchUsersGroup[ArrayLen];
			for(int i = 1; i < ArrayLen ; i++){
				groups[i]=(SwitchUsersGroup)(groupsT[i-1]);
			}
		}else{
			groups = new SwitchUsersGroup[ArrayLen];
		}
		
		groups[0] = this.getMembersGroup();
		
		return groups;
	}
	
	//group add(not check reduntency)
	private SwitchUsersGroup addSwitchUsersGroup(SwitchUsersGroup group){
		if(group==null || group.getName()==null ||
				group.getName().length() ==0){
			return null;
		}
		group.setRefreshTimer(this.getRefreshTimer());
		
		SwitchUsersGroup oldGroupForThisName = this.groups.put(group.getName(), group);
		this.firePropertyChange(PROP_GROUP_ADD, oldGroupForThisName, group);
		return oldGroupForThisName;
	}
	//group add(not check reduntency)
	public SwitchUsersGroup addSwitchUsersGroup(String groupName,int groupCapacity){
		if(groupName == null || groupName.length() == 0){
			return null;
		}
		if(groupCapacity <=0){
			groupCapacity = this.groupMemberCapacity;
		}
		SwitchUsersGroup group = new SwitchUsersGroup(this,groupName,groupCapacity);
		this.addSwitchUsersGroup(group);
		return group;
	}
	//group add(not check reduntency)
	public SwitchUsersGroup addSwitchUsersGroup(String groupName){
		
		return (SwitchUsersGroup)this.addSwitchUsersGroup(groupName, this.groupMemberCapacity);
	}
	
	//group remove
	private SwitchUsersGroup removeSwitchUsersGroup(SwitchUsersGroup group){
		if(group == null || group.getName() == null){
			return null;
		}
		group.removeAll();
		return (SwitchUsersGroup)this.groups.remove(group.getName());
	}
	public SwitchUsersGroup removeSwitchUsersGroup(String groupName){
		SwitchUsersGroup group = this.getSwitchUsersGroup(groupName);
		if(group == null){
			return null;
		}
		return this.removeSwitchUsersGroup(group);
	}
	public void removeAllSwitchUsersGroup(){
		Enumeration<SwitchUsersGroup> groupEnum =
			this.groups.elements();
		while(groupEnum.hasMoreElements()){
			SwitchUsersGroup group = groupEnum.nextElement();
			this.removeSwitchUsersGroup(group);
		}
		this.groups.clear();
	}
	
	
	//userofgroup get
	public SwitchUser getSwitchUserOfGroup(String userId,String groupName){
		if(userId == null || groupName == null){
			return null;
		}
		SwitchUsersGroup group = this.getSwitchUsersGroup(groupName);
		if(group == null){
			return null;
		}
		
		return group.getSwitchUser(userId);
	}
	
	//userofgroup add (not chech redundency ,if redundency ,just replace)
	public SwitchUser addSwitchUserOfGroup(String userId,String groupName){
		if(userId == null || userId.length() == 0 ||
				groupName == null || groupName.length() ==0){
			return null;
		}
		SwitchUser user = this.getSwitchUser(userId);
		if(user == null){
			user = this.addSwitchUser(userId);
		}
		if(user == null){
			return null;
		}
		SwitchUsersGroup group = this.getSwitchUsersGroup(groupName);
		if(group == null){
			group = this.addSwitchUsersGroup(groupName);
		}
		if(group != null){
			group.addSwitchUser(user);
		}
		return user;
	}
	
	public SwitchUser removeSwitchUserOfGroup(String userId,String groupName){
		if(userId == null || userId.length() == 0 ||
				groupName == null || groupName.length() ==0){
			return null;
		}
		
		SwitchUsersGroup group = this.getSwitchUsersGroup(groupName);
		if(group == null){
			return null;
		}
		SwitchUser user = this.getSwitchUser(userId);
		if(user == null){
			return null;
		}
		return group.removeSwitchUser(user);
		
	}
	
	
	
	public SwitchUser removeSwitchUser(String userId){
		if(userId == null || userId.length() == 0){
			return null;
		}
		
		SwitchUser user = this.getSwitchUser(userId);
		if(user == null){
			return null;
		}
		String[] ofGroups = user.getOfGroups();
		int len = ofGroups.length;
		for(int i =0;i < len;i++){
			String groupName = ofGroups[i];
			SwitchUsersGroup group = this.getSwitchUsersGroup(groupName);
			if(group != null){
				group.removeSwitchUser(user);
			}
		}
		
		return this.members.removeSwitchUser(userId);
		
	}
	
	public SwitchUser removeSwitchUser(SwitchUser user){
		return this.removeSwitchUser(user.getUserId());
	}
	

	public void removeAllSwitchUsers(){
		//remove all group members
		if(!this.groups.isEmpty()){
			Enumeration<SwitchUsersGroup> groupEnum = this.groups.elements();
			while(groupEnum.hasMoreElements()){
				SwitchUsersGroup group = groupEnum.nextElement();
				if(group != null){
					group.removeAll();
				}
			}
		}
		
		this.members.removeAll();
		this.groups.clear();
		
	}
	public int userSize(){
		return this.members.size();
	}
	public int groupSize(){
		return this.groups.size();
	}
	
	public String getName(){
		return this.name;
	}
	public void removeOldUser(){
		if(!this.members.isEmpty()){
			SwitchUser[] users = (SwitchUser[])this.members.getSwitchUsersArray();
			for(SwitchUser user:users){
				if(user != null ){
					if(user.getRefreshTimer() < this.getRefreshTimer()){
						this.removeSwitchUser(user);
					}
				}
			}
		}
	}
	public void removeOldUserIsDispat(){
		if(!this.members.isEmpty()){
			SwitchUser[] users = (SwitchUser[])this.members.getSwitchUsersArray();
			for(SwitchUser user:users){
				if(user != null ){
					if(user.getDispatStamp() < this.getDispatStamp()){
						user.setIsDispat(false);
						
					}
				}
			}
		}
	}
	public void removeOldGroups(){
		if(!this.groups.isEmpty()){
			SwitchUsersGroup[] groups = (SwitchUsersGroup[])this.groups.values().toArray(new SwitchUsersGroup[] {});
			
			for(SwitchUsersGroup group:groups){
				if(group != null){
					if(group.getRefreshTimer() < this.getRefreshTimer()){
						this.removeSwitchUsersGroup(group);
					}
				}
			}
		}
	}
	public void removeOldRelation(){
		if(!this.groups.isEmpty()){
			SwitchUsersGroup[] groups = (SwitchUsersGroup[])this.groups.values().toArray(new SwitchUsersGroup[] {});
			
			for(SwitchUsersGroup group:groups){
				if(group != null){
					group.removeOldRelation();
				}
			}
		}
	}
}
