package com.cari.voip.keyboard.stack.events;

public class ReplyListenerWrapper{
	private ReplyListener replyListener;
	private PacketFilter packetFilter;
	
	public ReplyListenerWrapper(ReplyListener replyListener,
			PacketFilter packetFilter){
		this.replyListener  = replyListener;
		this.packetFilter = packetFilter;
	}
	
	public void notifyListener(Packet command,Packet reply){
		if(packetFilter == null || packetFilter.accept(command)){
			replyListener.processReply(command, reply);
		}
	}
}
