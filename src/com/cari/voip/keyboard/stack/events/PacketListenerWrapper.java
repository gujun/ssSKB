package com.cari.voip.keyboard.stack.events;

public class PacketListenerWrapper {
	private PacketListener packetListener;
	private PacketFilter packetFilter;
	
	public PacketListenerWrapper(PacketListener packetListener,
			PacketFilter packetFilter){
		this.packetListener  = packetListener;
		this.packetFilter = packetFilter;
	}
	
	public void notifyListener(Packet packet){
		if(packetFilter == null || packetFilter.accept(packet)){
			packetListener.processPacket(packet);
		}
	}
}
