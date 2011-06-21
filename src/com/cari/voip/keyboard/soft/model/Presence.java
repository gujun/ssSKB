package com.cari.voip.keyboard.soft.model;

public class Presence {
	
	public static Presence ON_LINE = new Presence("on line");
	public static Presence OFF_LINE = new Presence("off line");
	
	public static Presence CALL_SPEAK = new Presence("call and speak");
	public static Presence CALL_MUTE = new Presence("call but mute");
	
	public static Presence CALL_RING = new Presence("ring",null,false);
	public static Presence CALL_RING_BACK = new Presence("ring back",null,true);
	
	public static Presence CALL_ANSWER = new Presence("answer",null,true);
	public static Presence CALL_HANGUP = new Presence("hangup",null,true);
	
	private String description;
	private String callName;
	private boolean isCaller;
	
	public Presence(String description,String callName,boolean isCaller){
		this.description = description;
		this.callName = callName;
		this.isCaller = isCaller;
	}
	public Presence(String description){
		this(description, null, false);
	}
	
	
	public String toString(){
		return this.getClass().getName()+":"+this.description;
	}
}
