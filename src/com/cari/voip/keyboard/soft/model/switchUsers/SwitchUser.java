package com.cari.voip.keyboard.soft.model.switchUsers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.cari.voip.keyboard.soft.model.Presence;

public class SwitchUser extends SwitchEntity implements Comparable {

	
	private String userId;//1001,1008
	private String userAgent;//mg6008,mx8s,xlite
	//private String userType;//user,gateway
	private String desc;
	private boolean isRegOK;
	private boolean isDispat = false;
	private String IP;
	
	private String callName;
	private Presence presence;
	private Hashtable<String,Presence> callsPresenceHash;
	
	public static final String PRESENCE_PROP="SwitchUser.Presence";
	public static final String PROP_USER_AGENT="SwitchUser.UserAgent";
	public static final String PROP_IS_REG = "SwitchUser.IsReg";
	public static final String PROP_IP = "SwitchUser.IP";
	public static final String PROP_REMOVE_FROM_GROUP = "SwitchUser.RemoveFromGroup";
	public static final String PROP_USER_DESC = "SwitchUser.UserDesc";
	
	private ArrayList<String> ofGroups;
	//private Hashtable<String,LongWrap> inGroups;
	//private List<Listener> presenceListener;
	
	protected SwitchUser(String userId){
		this.userId = userId;
		this.presence = Presence.OFF_LINE;
		this.ofGroups = new ArrayList<String>(3);
		this.callsPresenceHash = new Hashtable<String,Presence>(2);
		this.callName = null;
		//this.inGroups = new Hashtable<String,LongWrap>(3);
	}
	public  String getName(){
		/*if(this.userAgent != null){
			return this.userId+"("+this.userAgent+")";
		}*/
		if(this.isDispat){
			return "调度";
		}
		if(this.desc != null && this.desc.length() > 0){
			return this.desc;
		}
		
		return this.userId;
	}
	public String getUserId(){
		return this.userId;
	}
	public String getDesc(){
		return this.desc;
	}
	public String getTooltipString(){
		String tooltip = " ";
		if(this.isDispat){
			tooltip = tooltip.concat("调度电话  \n ");
		}
		tooltip =tooltip.concat(this.userId);
		if(this.desc != null && this.desc.length() > 0){
			tooltip =tooltip.concat("["+this.desc +"]");
		}
		tooltip = tooltip.concat(" \n");
		if(this.presence == Presence.OFF_LINE){
			tooltip = tooltip.concat(" 未注册 ");
		}else if(this.presence == Presence.ON_LINE){
			tooltip =tooltip.concat(" 空闲 ");
		}else if(this.presence == Presence.CALL_SPEAK){
			tooltip =tooltip.concat(" 通话（听说） ");
		}else if(this.presence == Presence.CALL_SPEAK){
			tooltip =tooltip.concat(" 通话（单听）  ");
		}
		int n = this.size();
		if(n > 1){
			tooltip = tooltip.concat("/"+String.valueOf(n));
		}
		
		
		return tooltip;
	}
/*	public String setUserId(String userId){
		String oldId = this.userId;
		this.userId = userId;
		
		return oldId;
	}*/
	public boolean getIsDispat(){
		return this.isDispat;
	}
	public boolean setIsDispat(boolean newValue){
		boolean oldvalue = this.isDispat;
		if(oldvalue != newValue){
			this.isDispat = newValue;
			this.firePropertyChange(PROP_USER_DESC, null, null);
		}
		return oldvalue;
	}
	public Presence getPresence(){
		return this.presence;
	}
	public Presence setPresence(Presence presence){
		
		Presence oldPresence = this.presence;
		if(presence == null){
			return oldPresence;
		}
		this.presence = presence;
		this.firePropertyChange(PRESENCE_PROP, null, presence);
		return oldPresence;
	}
	public String setAgent(String newValue){
		String oldValue = this.userAgent;
		this.userAgent = newValue;
		this.firePropertyChange(PROP_USER_AGENT, oldValue, newValue);
		return oldValue;
	}
	public String setIP(String newValue){
		String oldValue = this.IP;
		this.IP = newValue;
		this.firePropertyChange(PROP_IP, oldValue, newValue);
		return oldValue;
	}
	
	public boolean setReg(boolean newValue){
		boolean oldValue = this.isRegOK;
		this.isRegOK = newValue;
		this.firePropertyChange(PROP_IS_REG, oldValue, newValue); 
		return oldValue;
	}
	public String setDesc(String desc){
		String oldValue = this.desc;
		this.desc = desc;
		this.firePropertyChange(PROP_USER_DESC, oldValue, desc);
		return oldValue;
	}
	public boolean callPresenceSet(String callName,Presence presence,boolean isTalking){
		if(presence == null){
			return false;
		}
		if (callName != null) {
			this.callsPresenceHash.put(callName, presence);
			if (isTalking) {
				this.callName = callName;
				this.setPresence(presence);
			} else {
				if (this.callName == null) {
					this.callName = callName;
					this.setPresence(presence);
				} else if (this.callName.equals(callName)) {
					this.setPresence(presence);
				}
			}
		}
		else{
			if(this.callName == null){
				this.setPresence(presence);
			}
		}
		return true;
	}
	public boolean callPresenceRemove(String callName){
		if(callName == null){
			/*if(this.callName == null){
				if (this.callsPresenceHash.isEmpty()) {
					this.setPresence(Presence.ON_LINE);
				} else {

						this.callName = this.callsPresenceHash.keys().nextElement();
						if (this.callName != null) {
							this.setPresence(this.callsPresenceHash
									.get(this.callName));
						}
					
				}
			}*/
			this.callsPresenceHash.clear();
			this.callName = null;
			this.setPresence(Presence.ON_LINE);
		}
		else {
			this.callsPresenceHash.remove(callName);
			if (this.callsPresenceHash.isEmpty()) {
				this.callName = null;
				if(this.getPresence() != Presence.ON_LINE &&
						this.getPresence() != Presence.OFF_LINE){
					this.setPresence(Presence.CALL_ANSWER);
				}
			} else {
				if (this.callName == null || this.callName.equals(callName)) {

					this.callName = this.callsPresenceHash.keys().nextElement();
					if (this.callName != null) {
						this.setPresence(this.callsPresenceHash
								.get(this.callName));
					}
				}
			}
		}
		return true;
	}
	public int size(){
		return this.callsPresenceHash.size();
	}
	public String[] getOfGroups(){
		if(this.ofGroups.isEmpty()){
			return new String[0];
		}
		return (String[])this.ofGroups.toArray(new String[]{});
		/*if(this.inGroups.isEmpty()){
			return new String[0];
		}
		return (String[])this.inGroups.keySet().toArray();*/
	}
	public boolean isOfGroup(String groupName){
		if(groupName == null || this.ofGroups.isEmpty()){
			return false;
		}
		//return this.inGroups.containsKey(groupName);
		return this.ofGroups.contains(groupName);
	}
	

	public void addOfGroup(String groupName){
		if(groupName != null){
			if(!this.isOfGroup(groupName)){
				this.ofGroups.add(groupName);
			}
		}
	}
	public void removeOfGroup(String groupName){
		if(groupName != null){
			if(this.isOfGroup(groupName)){
				this.ofGroups.remove(groupName);
				//this.inGroups.remove(groupName);
				this.firePropertyChange(PROP_REMOVE_FROM_GROUP, null, groupName);
			}
		}
	}
	
	public Object getPropertyValue(Object id) {
		if(PRESENCE_PROP.equals(id)){
			return this.getPresence();
		}
		return null;
	}
	
	public void setPropertyValue(Object id, Object value) {
		if(PRESENCE_PROP.equals(id)){
			if(value instanceof Presence){
				this.setPresence((Presence)value);
			}
		}
	}
	@Override
	public int compareTo(Object o) {
		int rv = 0;
		boolean cmpString = false;
		if(o instanceof SwitchUser){
			SwitchUser u2 = (SwitchUser)o;
			if(this.getPresence()== Presence.OFF_LINE){
				if(u2.getPresence() == Presence.OFF_LINE){
					cmpString = true;
				}
				else{
					rv = -1;
				}
			}
			else{
				if(u2.getPresence() == Presence.OFF_LINE){
					rv = 1;
				}
				else{
					cmpString = true;
				}
				
			}
			if(cmpString == true){
				cmpString = false;
				if(this.isDispat){
					if(u2.isDispat){
						cmpString = true;
					}else{
						rv = 1;
					}
				}else{
					if(u2.isDispat){
						rv = -1;
					}else{
						cmpString = true;
					}
				}
			}
			if(cmpString == true){
				String id1 = this.getUserId();
				String id2 = u2.getUserId();
				if(id2 != null && id1 != null){
					rv = id2.compareTo(id1);
				}
			}
		}
		return rv;
	}
	
	/*public void removeOldofGroups(){
		if(this.ofGroups.isEmpty()){
			return;
		}
		String[] groupNames = this.getOfGroups();
		for(String groupName:groupNames){
			LongWrap refreshTimer = this.ofGroups.get(groupName);
			if(refreshTimer.getValue() < this.getRefreshTimer()){
				this.removeOfGroup(groupName);
			}
		}
	}*/
}
