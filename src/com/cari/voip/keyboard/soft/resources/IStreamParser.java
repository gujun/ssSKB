package com.cari.voip.keyboard.soft.resources;

import java.io.Reader;

public interface IStreamParser {

	void setInput(Reader in);
	
	
	char readChar();
	
	String readLine();
}
