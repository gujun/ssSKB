package com.cari.voip.keyboard.stack;

public class PacketDispatcher {
	private CCKPConnection connection;
	
	public PacketDispatcher(CCKPConnection connection){
		this.connection = connection;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}
	
	public void shutdown(){
		
	}
	
	public void cleanup(){
		
	}
	
	void notifyConnectionError(Exception e){
		
	}
}
