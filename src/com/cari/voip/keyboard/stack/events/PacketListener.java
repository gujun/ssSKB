package com.cari.voip.keyboard.stack.events;

import com.cari.voip.keyboard.stack.events.Packet;

public interface PacketListener {

	public void processPacket(Packet packet);
}
