package com.cari.voip.keyboard.stack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.PacketListener;
import com.cari.voip.keyboard.stack.events.ReplyListener;
import com.cari.voip.keyboard.stack.events.TrapEventListener;
import com.cari.voip.keyboard.stack.parsers.PacketParseException;
import com.cari.voip.keyboard.stack.parsers.PacketParser;

public class CCKPConnection {

	
	public static final int DEFAULT_SERVER_PORT = 6608;
	
	
	private ConnectionConfiguration connectionConfig = null;

	
	private boolean isConnected = false;
	private boolean isAuthed = false;
	private boolean isRunning = false;
	

	
	private Socket socket;
	
	Writer writer;
	Reader reader;
	PacketParser parser;
	

	PacketWriter packetWriter;
	PacketReader packetReader;
	

	private Semaphore connectionSemaphore;
	private Runnable authRunnable = null;
	private Thread authThread= null;
	private String  authReplyTxt = null;
	private String authExceptionMessage = null;
	String connectionID;
	
	
	protected final Collection<ConnectionListener> connectionListeners = 
		new CopyOnWriteArrayList<ConnectionListener>();

	public CCKPConnection(String host,int port){
		this.connectionConfig = new ConnectionConfiguration();
		this.connectionConfig.setServerHost(host);
		this.connectionConfig.setServerTcpPort(port);

		this.packetReader = new PacketReader(this);
		this.packetWriter = new PacketWriter(this);
	}
	
	
	public boolean isConnect(){
		return this.isConnected;
	}
	public boolean isAuthed(){
		return this.isAuthed;
	}
	public void login(String id,String pwd)  throws CCKPConnectionException {
		
		this.connectionConfig.setPhoneId(id);
		this.connectionConfig.setPwd(pwd);

		connectUsingConfiguration();
		
		this.isConnected = true;
		this.notifySocketConnectOK();
		
		resetReaderAndWriter();
		
		
		//login auth
		auth();
			
		if(this.isAuthed == true){
			this.startup();
			this.notifyAuthenticationOK();
		}
		
	}
	
	
	private void connectUsingConfiguration()  throws CCKPConnectionException{
		
	   try {
			this.socket = new Socket(this.connectionConfig.getServerHost(),
					this.connectionConfig.getServerTcpPort());
		} catch (UnknownHostException e) {
			throw new CCKPConnectionException("服务器地址"+
					this.connectionConfig.getServerHost()+"不可达！");
		} catch (IOException e) {
			throw new CCKPConnectionException("服务器["+
					this.connectionConfig.getServerHost()+":"+
					this.connectionConfig.getServerTcpPort()+"]无法连接！");
		}
		
	}
	public boolean isConnected(){
		return this.isConnected;
	}
	protected void resetReaderAndWriter() throws CCKPConnectionException {
		if(this.reader != null){
			try{
				this.reader.close();
			}
			catch(IOException e){
				throw new CCKPConnectionException("网络连接读资源释放错误");
			}
		}
		if(this.writer != null){
			try{
				this.writer.close();
			}catch(IOException e){
				throw new CCKPConnectionException("网络连接写资源释放错误");
			}
		}
		if(this.isConnected() == false ){
			throw new CCKPConnectionException("网络通信未连接");
		}
		
		try {
			this.reader = new BufferedReader(
					new InputStreamReader(this.socket.getInputStream(),"UTF-8")
					);
			this.writer = new BufferedWriter(
					new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8")
					);
		} catch (UnsupportedEncodingException e) {
			throw new CCKPConnectionException("错误，编码不支持");
		} catch (IOException e) {
			throw new CCKPConnectionException("网络通信错误，请检查网络连接");
		}
		
		
		
	}
	
	
	protected String getStringFromMD5Byte(byte[] md5){
		String ret = "";
		char hexDigits[] = {       // 用来将字节转换成 16 进制表示的字符
			     '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
		
		if(md5 == null || md5.length != 16){
			return ret;
		}
		try{
			StringBuilder builder = new StringBuilder();
			for(int i = 0 ;i<16;i++){
				
				 byte b = md5[i];
				 builder.append(hexDigits[(b>>>4)&0xf]);
				 builder.append(hexDigits[b&0xf]);
				 
			}
			/*
			
			for(int i = 0 ;i<4;i++){
				int b1 = (int)(md5[i*4 + 0]) &0xFF;
				int b2 = (int)(md5[i*4 + 1]) &0xFF;
				int b3 = (int)(md5[i*4 + 2]) &0xFF;
				int b4 = (int)(md5[i*4 + 3]) &0xFF;
				
				int j = (b1<<8) +b2 + b3+ b4;
				
				builder.append(String.valueOf(j));
				
				builder.append("-");
			}
			builder.deleteCharAt(builder.length() - 1);
			*/
			
			ret = builder.toString();
		}catch( Exception e )
		   {
		    //e.printStackTrace();
		   }
		return ret;
	}
	protected void authbg() throws CCKPConnectionException{
		parser = new PacketParser();
		parser.setInput(this.reader);
		
		Packet authRequest;
		try {
				authRequest = parser.getNextPacket();
				
		} catch (PacketParseException e) {
			throw new CCKPConnectionException(e.getMessage());
		}
		if(authRequest != null &&
				authRequest.getPacketType() == Packet.TYPE_AUTH_REQUEST){
			this.connectionID = (String) authRequest.getProperty(Packet.PROP_CONNECTION_ID);
			if(this.connectionID == null){
				throw new CCKPConnectionException("错误：服务端认证请求包格式有误");
			}
		}
		else{
			throw new CCKPConnectionException("错误：服务端没有响应");
		}
	
		Packet auth = new Packet(Packet.TYPE_AUTH);
		String serviceName = null;
		//serviceName = this.connectionConfig.getServiceName();
		String phoneId = this.connectionConfig.getPhoneId();

		auth.setProperty(Packet.PROP_DISPATCHER, (serviceName== null)?phoneId:phoneId+"@"+serviceName);
		String pwd = this.connectionConfig.getPwd();
		if(pwd == null){
			pwd = "";
		}
		try
		   {
		    java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
		    md.update( pwd.getBytes() );
		    byte dg[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
		    pwd = getStringFromMD5Byte(dg);
		   }catch( Exception e )
		   {
			   pwd = "error";
		   }
		
		   
		auth.setProperty(Packet.PROP_PASSWORD, pwd);//+this.connectionID);
		try {
			String trafer = auth.toString();
			this.writer.write(trafer);
			this.writer.flush();
		} catch (IOException e) {
			throw new CCKPConnectionException("错误：发送认证信息错误，请检查网络连接");
		}
		
		
		Packet authReply;
		try {
				authReply = parser.getNextPacket();
				
		} catch (PacketParseException e) {
			throw new CCKPConnectionException(e.getMessage());
		}
		if(authReply != null &&
				authReply.getPacketType() == Packet.TYPE_AUTH_REPLY){
			String replyTxt = (String) authReply.getProperty(Packet.PROP_REPLY_TEXT);
			if(replyTxt == null){
				throw new CCKPConnectionException("错误：服务端认证响应包格式有误");
			}
			String replyTxtLowerCase =  replyTxt.toLowerCase();
			if(replyTxtLowerCase.startsWith(Packet.PROP_REPLY_OK)){
				this.isAuthed = true;
			}
			else if(replyTxtLowerCase.startsWith(Packet.PROP_REPLY_ERROR)){
				this.isAuthed = false;
				
				/*int k = replyTxt.indexOf(',');
				if(k > 0){
					this.authReplyTxt = replyTxt.substring(k+1);
				}
				String discription = (String) authReply.getProperty(Packet.PROP_ERR_DISPCRIPTION);
				if(discription != null){
					this.authReplyTxt = discription;
				}
				*/
				this.authReplyTxt = "非调度用户或口令错误";
			}
			else{
				throw new CCKPConnectionException("错误：服务端认证响应指示有误");
			}
		}
		else{
			throw new CCKPConnectionException("错误：服务端没有认证请求");
		}
		
	
		
	}
	protected void auth() throws CCKPConnectionException{
		if(this.authRunnable == null){
			this.authRunnable = new Runnable(){
				public void run(){
					try {
						authbg();
					} catch (CCKPConnectionException e) {
						authExceptionMessage = e.getLocalizedMessage();
					}
					catch(Exception e){
						//do nothing;
					}
					if(connectionSemaphore != null){
						connectionSemaphore.release();
					}
				}
			};
		}
		if(this.authThread == null){
			this.authThread = new Thread(this.authRunnable);
			
			this.authThread.setName("auth thread");
			this.authThread.setDaemon(true);
		}
		
		this.authReplyTxt = null;
		this.authExceptionMessage = null;
		this.connectionID = null;
		this.isAuthed = false;
		
		this.connectionSemaphore = new Semaphore(0);
		
		this.authThread.start();
		
		
		try {
			int waitTime = CCKPConfiguration.getPacketReplyTimeout();
			this.connectionSemaphore.tryAcquire(6*waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			//ignore;
		}
		try{
			this.authThread.interrupt();
		}
		catch(Exception e){
			//do nothing;
		}
		this.authThread = null;
		
		if(this.connectionID == null){
			throw new CCKPConnectionException(
					(this.authExceptionMessage==null)?"错误：服务器没有响应":this.authExceptionMessage);
		}
		if(this.isAuthed == false){
			throw new CCKPConnectionException(
					(this.authExceptionMessage==null)?
							((this.authReplyTxt == null)?"错误：服务器没有响应":this.authReplyTxt)
							:this.authExceptionMessage);
		
		}
	}
	
	
	public void startup(){

		if(this.isRunning == false){
			this.packetWriter.startup();
			this.packetReader.startup();
		}
		else{
			this.packetWriter.resetWriter();
			this.packetReader.resetParser();
		}
		this.isRunning = true;
		
	}
	
	protected void shutdown(){
		if(this.isRunning){
			for(ConnectionListener listener : this.connectionListeners){
				try{
					listener.connectionClosed();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			this.packetReader.shutdown();
			this.packetWriter.shutdown();
		}
		this.isRunning = false;
		
		  // Wait 150 ms for processes to clean-up, then shutdown.
        try {
            Thread.sleep(150);
        }
        catch (Exception e) {
            // Ignore.
        }
        
        if(this.reader != null){
        	try {
				this.reader.close();
			} catch (Throwable e) {
				//do nothing
			}
			this.reader = null;
        }
        
        if(this.writer != null){
        	
        	try {
				this.writer.close();
			} catch (Throwable e) {
				//do nothing
			}
			this.writer = null;
        }
        
        if(this.socket != null){
        	
        	try {
				this.socket.close();
			} catch (Throwable e) {
				//do nothing
			}
			
			this.socket = null;
        }
        
        this.isAuthed = false;
        this.isConnected = false;
	}
	
	public void disconnect(){
		
		this.shutdown();
		
	}
	public void cleanup(){
		this.packetReader.cleanup();
		this.packetReader = null;
		this.packetWriter.cleanup();
		this.packetWriter = null;
		this.connectionListeners.clear();
	}
	public void sendPacket(Packet packet) throws CCKPConnectionException{
		if(packet == null){
			return;
		}
		if(this.isRunning == false){
			throw new CCKPConnectionException("错误：read和write任务未启动");
		}
		this.packetWriter.sendPacket(packet);
	}
	

	
	public void addConnectionListener(ConnectionListener connectionListener){
		if(connectionListener ==  null){
			return;
		}
		this.connectionListeners.add(connectionListener);
	}

	public void removeConnectionListener(ConnectionListener connectionListener){
		if(connectionListener == null){
			return;
		}
		this.connectionListeners.remove(connectionListener);
	}
	
	
	
	public void addTrapEventListeners(TrapEventListener trapEventListener,PacketFilter packetFilter){
		if(trapEventListener == null){
			return;
		}
		this.packetReader.addTrapEventListeners(trapEventListener, packetFilter);
		
	}
	public void removeTrapEventListeners(TrapEventListener trapEventListener){
		if(trapEventListener == null){
			return;
		}
		this.packetReader.removeTrapEventListeners(trapEventListener);
		
	}
	
	
	
	public void addReplyListeners(ReplyListener replyListener,PacketFilter packetFilter){
		if(replyListener == null){
			return;
		}
		this.packetReader.addReplyListeners(replyListener, packetFilter);
		
	}
	public void removeReplyListeners(ReplyListener replyListener){
		if(replyListener == null){
			return;
		}
		this.packetReader.removeReplyListeners(replyListener);
		
	}
	
	
	public void addPacketParseExceptionListener(PacketParseExceptionListener listener){
		if(listener == null){
			return;
		}
		this.packetReader.addPacketParseExceptionListener(listener);
	}
	public void remove(PacketParseExceptionListener listener){
		if(listener == null){
			return;
		}
		this.packetReader.removePacketParseExceptionListener(listener);
	}
	
	
	
	public void addPacketSendListener(PacketListener packetListener,PacketFilter packetFilter){
		if(packetListener == null){
			return;
		}
		this.packetWriter.addPacketSendListener(packetListener, packetFilter);
		
	}

	public void removePakcetSendListener(PacketListener packetListener){
		if(packetListener == null){
			return;
		}
		
		this.packetWriter.removePakcetSendListener(packetListener);
	}
	
	
	public void addPacketWriteListener(PacketListener packetListener,PacketFilter packetFilter){
		if(packetListener == null){
			return;
		}
		
		this.packetWriter.addPacketWriteListener(packetListener, packetFilter);
	}
	
	public void removePakcetWriteListener(PacketListener packetListener){
		if(packetListener == null){
			return;
		}
		
		this.packetWriter.removePakcetWriteListener(packetListener);
	}
	
	public void notifySocketConnectOK() throws CCKPConnectionException{
		for(ConnectionListener listener: this.connectionListeners){
			try{
				listener.socketConnectSuccessful();
			}
			catch(Exception e){
				throw new CCKPConnectionException("用户退出");
			}
		}
	}
	public void notifyAuthenticationOK() throws CCKPConnectionException{
		for(ConnectionListener listener: this.connectionListeners){
			try{
				listener.authenticationSuccessful();
			}
			catch(Exception e){
				throw new CCKPConnectionException("用户退出");
			}
		}
	}
	public void notifyConnectionError(Exception e){
		//e.printStackTrace();

		this.shutdown();
		for(ConnectionListener listener: this.connectionListeners){
			try{
				listener.connectionClosedOnError(e);
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
	}
	
}
