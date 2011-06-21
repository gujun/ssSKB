package com.cari.voip.keyboard.stack.parsers;

import java.io.Reader;

import com.cari.voip.keyboard.stack.events.Packet;

public interface IPacketParser {
	
	 //Packet.Type packetType = Packet.Type.error;
	 
	
	void setInput(Reader in);
	
	//Packet.Type getPacketType();
	
	Packet getNextPacket() throws PacketParseException;
	
	
}
