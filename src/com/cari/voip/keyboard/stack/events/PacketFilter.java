package com.cari.voip.keyboard.stack.events;

public interface PacketFilter {

	public boolean accept(Packet packet);
}
