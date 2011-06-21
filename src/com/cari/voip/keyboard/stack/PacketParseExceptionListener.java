package com.cari.voip.keyboard.stack;

import com.cari.voip.keyboard.stack.parsers.PacketParseException;

public interface PacketParseExceptionListener {

	public void precessPacketParseException(PacketParseException e);
}
