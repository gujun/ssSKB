package com.cari.voip.keyboard.stack;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.PacketListener;
import com.cari.voip.keyboard.stack.events.PacketListenerWrapper;

public class PacketWriter {
	private CCKPConnection connection;
	private Writer writer;
	
	private final BlockingQueue<Packet> queue;
	private final BlockingQueue<Packet> queueSended;
	
	private Thread writerThread;
	//private Thread keepAliveThread;
	
	private boolean done = false;
	
	private final Map<PacketListener,PacketListenerWrapper> sendListeners = 
		new ConcurrentHashMap<PacketListener,PacketListenerWrapper>();
	private final Map<PacketListener,PacketListenerWrapper> writeListeners = 
		new ConcurrentHashMap<PacketListener,PacketListenerWrapper>();
	private long lastActive;
	
	
	protected PacketWriter(CCKPConnection connection){
		this.queue = new ArrayBlockingQueue<Packet>(500,true);
		this.queueSended  = new ArrayBlockingQueue<Packet>(500,true);
		
		this.connection = connection;
		init();
	}

	protected void init() {
		done = false;
		this.writerThread = new Thread(){
			public void run(){
				writePackets(this);
			}
		};
		this.writerThread.setName("CCKP Packet Writer");
		this.writerThread.setDaemon(true);

	}
	
	public void sendPacket(Packet packet){
		if(!done){

			try {
				queue.put(packet);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			
			synchronized(queue){
				queue.notifyAll();
			}
			
			processSendListeners(packet);
		}
	}
	public void addPacketSendListener(PacketListener packetListener,PacketFilter packetFilter){
		this.sendListeners.put(packetListener, 
				new PacketListenerWrapper(packetListener,packetFilter));
	}
	public void addPacketWriteListener(PacketListener packetListener,PacketFilter packetFilter){
		this.writeListeners.put(packetListener, 
				new PacketListenerWrapper(packetListener,packetFilter));
	}
	
	public void removePakcetSendListener(PacketListener packetListener){
		this.sendListeners.remove(packetListener);
	}
	public void removePakcetWriteListener(PacketListener packetListener){
		this.writeListeners.remove(packetListener);
	}
	
	public int getPacketSendListenerCount(){
		return this.sendListeners.size();
	}
	public int getPacketWriteListenerCount(){
		return this.writeListeners.size();
	}
	
	
	public void startup(){
		resetWriter();
		this.writerThread.start();
	}
	
	public void shutdown(){
		done = true;
		synchronized(queue){
			queue.notifyAll();
		}
	}
	void cleanup(){
		this.sendListeners.clear();
		this.writeListeners.clear();
	}
	
	void resetWriter(){
		this.writer = this.connection.writer;
	}
	
	public Packet nextPacketSended(){
		Packet packet = null;
		if(!done){
			packet = this.queueSended.poll();
		}
		
		return packet;
	}
	protected Packet nextPacket(){
		Packet packet = null;
		while(!done && (packet = this.queue.poll()) == null){
			try{
				synchronized(queue){
					queue.wait();
				}
			}catch(InterruptedException ie){
				//do nothing
			}
		}
		return packet;
	}
	protected void processSendListeners(Packet packet) {
		for(PacketListenerWrapper listenerWrapper: this.sendListeners.values()){
			listenerWrapper.notifyListener(packet);
		}
	}
	protected void queuePackets(Packet packet) {
		try {
			this.queueSended.put(packet);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		processWriteListeners(packet);
	}
	protected void processWriteListeners(Packet packet) {
		for(PacketListenerWrapper listenerWrapper: this.writeListeners.values()){
			listenerWrapper.notifyListener(packet);
		}
	}

	protected void writePackets(Thread thread) {
		try{
			openStream();
			while(!done && (this.writerThread == thread)){
				Packet packet = this.nextPacket();
				if(packet != null){

						writer.write(packet.toString());
						writer.flush();
						lastActive = System.currentTimeMillis();
					
					queuePackets(packet);
				}
			}
			
			
		   while(! queue.isEmpty()){
						Packet packet = queue.remove();
						writer.write(packet.toString());
			}
			writer.flush();
				
			
			
			queue.clear();
			
			//close the stream
			closeStream();
		}
		catch(IOException ioe){
			if(!done){
				done = true;
				this.connection.notifyConnectionError(ioe);
			}
		}
	}



	void closeStream() {
		try{
			writer.close();
		}catch(Exception e){
			// do nothing
		}
	}

	void openStream() {
		// TODO Auto-generated method stub
		
	}




}
