package com.cari.voip.keyboard.stack.events;

public interface ReplyListener {

	public void processReply(Packet command,Packet reply);
}
