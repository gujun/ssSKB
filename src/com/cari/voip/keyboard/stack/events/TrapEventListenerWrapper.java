package com.cari.voip.keyboard.stack.events;

public class TrapEventListenerWrapper {
	private TrapEventListener trapEventListener;
	private PacketFilter packetFilter;
	
	public TrapEventListenerWrapper(TrapEventListener trapEventListener,
			PacketFilter packetFilter){
		this.trapEventListener  = trapEventListener;
		this.packetFilter = packetFilter;
	}
	
	public void notifyListener(Packet event){
		if(packetFilter == null || packetFilter.accept(event)){
			trapEventListener.processTrap(event);
		}
	}
}
