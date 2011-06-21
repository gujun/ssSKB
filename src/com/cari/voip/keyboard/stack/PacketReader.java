package com.cari.voip.keyboard.stack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import com.cari.voip.keyboard.stack.events.*;
import com.cari.voip.keyboard.stack.parsers.*;


public class PacketReader {
	private CCKPConnection connection;
	
	private Thread readerThread;
	private Thread executorThread;
	
	private PacketParser parser;
	private boolean done;
	
	private final BlockingQueue<Packet> queue;
	
	protected final Map<TrapEventListener,TrapEventListenerWrapper> trapEventListeners = 
		new ConcurrentHashMap<TrapEventListener,TrapEventListenerWrapper>();
	
	protected final Map<ReplyListener,ReplyListenerWrapper> replyListeners = 
		new ConcurrentHashMap<ReplyListener,ReplyListenerWrapper>();
	
	
	protected final Collection<PacketParseExceptionListener> packetParseExceptionListeners = 
		new CopyOnWriteArrayList<PacketParseExceptionListener>();
	private static int invalidPacket = 0;

	protected PacketReader(CCKPConnection connection){
		this.queue = new ArrayBlockingQueue<Packet>(500,true);
		this.connection = connection;
		init();
	}

	protected void init() {
		done = false;
		this.readerThread = new Thread(){
			public void run(){
				parsePackets(this);
			}
		};
		this.readerThread.setName("CCKP Packet Reader");
		this.readerThread.setDaemon(true);
		
		this.executorThread = new Thread(){
			public void run(){
				executePackets(this);
			}
		};
		
		this.executorThread.setName("CCKP Packets Executor");
		this.executorThread.setDaemon(true);
		
		
	}
	
	public void resetParser() {
		
		this.parser = this.connection.parser;
		
		
	}

	
	public void startup() {
		resetParser();
		this.readerThread.start();
		
		this.executorThread.start();
		
	}
	public void shutdown(){
		done = true;
		synchronized(queue){
			queue.notifyAll();
		}
		/*try {
			this.readerThread.join();
			//this.executorThread.join();
		} catch (InterruptedException e) {
			
		}*/
	}
	
	public void cleanup(){
		
		this.trapEventListeners.clear();
		this.replyListeners.clear();
		
	}
	
	
	protected void parsePackets(Thread thread) {
		while(!done && this.readerThread == thread){
			if(this.parser != null){
				try{
					Packet packet = this.parser.getNextPacket();
					if(packet != null){
						try{
							this.queue.put(packet);
							synchronized(queue){
								queue.notifyAll();
							}
						}
						catch(InterruptedException e){
							
						}
						
					}
					//else{
					//	System.out.print("\nnull packet");
					//}
				}
				catch(PacketParseException e){
					this.notifyPacketParseException(e);
				}
				
			}
		}
	}

	
	protected void executePackets(Thread thread) {
		while(!done && this.executorThread == thread){
			Packet packet = this.nextPacket();
			if(packet != null){
				executePacket(packet);
			}
		}
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
	
	

	protected void executePacket(Packet packet) {
		if(packet.isReply()){
			Packet cmd = this.connection.packetWriter.nextPacketSended();
			if(cmd != null ){
				ReplyListener replyListener = cmd.getReplyHandler();
				if(replyListener != null ){
					replyListener.processReply(cmd,packet);
				}
				
			    this.fireReplyListeners(cmd,packet);
			}
		}
		else if(packet.isTrap()){
			this.fireTrapEventListeners(packet);
		}
	}

	public void addReplyListeners(ReplyListener replyListener,PacketFilter packetFilter){
		this.replyListeners.put(replyListener, 
				new ReplyListenerWrapper(replyListener,packetFilter));
	}
	public void removeReplyListeners(ReplyListener replyListener){
		this.replyListeners.remove(replyListener);
	}
	public void addTrapEventListeners(TrapEventListener trapEventListener,PacketFilter packetFilter){
		
		this.trapEventListeners.put(trapEventListener, 
				new TrapEventListenerWrapper(trapEventListener,packetFilter));
	}
	public void removeTrapEventListeners(TrapEventListener trapEventListener){
		this.trapEventListeners.remove(trapEventListener);
	}
	
	protected void fireReplyListeners(Packet command, Packet reply) {
		for(ReplyListenerWrapper replyListenerWrapper:this.replyListeners.values()){
			replyListenerWrapper.notifyListener(command, reply);
		}
	}
	
	protected void fireTrapEventListeners(Packet event) {
		for(TrapEventListenerWrapper trapEventListenerWrapper:this.trapEventListeners.values()){
			trapEventListenerWrapper.notifyListener(event);
		}
	}
	
	public void addPacketParseExceptionListener(PacketParseExceptionListener listener){
		this.packetParseExceptionListeners.add(listener);
	}

	public void removePacketParseExceptionListener(PacketParseExceptionListener listener){
		this.packetParseExceptionListeners.remove(listener);
	}
	

	
	public void notifyPacketParseException(PacketParseException e){
		invalidPacket++;
		for(PacketParseExceptionListener listener : this.packetParseExceptionListeners){
			try{
				listener.precessPacketParseException(e);
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		}
		//if(invalidPacket >= CCKPConfiguration.getInvalidPacketsNumber2shutdown()){
			this.connection.notifyConnectionError(e);
		//}
	}
}
