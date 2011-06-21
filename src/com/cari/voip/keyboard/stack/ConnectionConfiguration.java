package com.cari.voip.keyboard.stack;

import com.cari.voip.keyboard.stack.parsers.PacketParseException;

public class ConnectionConfiguration implements Cloneable {

	
	private String serverHost;
	public static final String DEFAULT_SERVER_HOST="192.168.1.7";
	
	private String serviceName = null;
	public static final String DEFAULT_SERVICE_NAME = "dispat";
	
	private int serverTcpPort;
	public static final int DEFAULT_SERVER_TCP_PORT = 6608;
	
	private String user;
	public static final String DEFAULT_USER = "天地（常州）自动化股份有限公司";
	
	private String pwd;
	public static final String DEFAULT_PWD = "";
	
	private String phoneId;
	public static final String DEFAULT_PHONEID = "1000";
	
	
	public ConnectionConfiguration(String serverHost, 
			int serverTcpPort, String user,String pwd,
			String serviceName,String phoneId){
		this.serverHost = serverHost;
		this.serverTcpPort = serverTcpPort;
		this.user = user;
		this.pwd = pwd;
		this.serviceName = serviceName;
		this.phoneId = phoneId;
	}
	public ConnectionConfiguration(){
		this(DEFAULT_SERVER_HOST,
				DEFAULT_SERVER_TCP_PORT,DEFAULT_USER,DEFAULT_PWD,
				DEFAULT_SERVICE_NAME,DEFAULT_PHONEID);
	}
			
			
	
	public String getServerHost(){
		return this.serverHost;
	}
	public void setServerHost(String host){
		if(host != null && host.length() != 0){
			this.serverHost = host;
		}
	}
	public int getServerTcpPort(){
		return this.serverTcpPort;
	}
	public void setServerTcpPort(int port){
		if(port > 0 && port < 65535){
			this.serverTcpPort = port;
		}
	}
	
	public String getUser(){
		return this.user;
	}
	public void setUser(String value){
		if(value != null && value.length() != 0){
			this.user = value;
		}
	}
	
	public String getPwd(){
		return this.pwd;
	}

	public void setPwd(String value){
		if(value == null){
			value = "";
		}
		this.pwd = value;
		
		
	}
	public String getPhoneId(){
		return this.phoneId;
	}
	public void setPhoneId(String value){
		if(value != null && value.length() != 0){
			this.phoneId = value;
		}
	}
	
	public String getServiceName(){
		return this.serviceName;
	}
	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}
	public void setServerTcpPort(String value) {
		if(value == null || value.length() == 0){
			return;
		}
		int port = -1;
		try{
			port = Integer.parseInt(value);
		}
		catch(Exception e){
			
		}
		if(port > 0){
			this.setServerTcpPort(port);
		}
		
	}
	
}
