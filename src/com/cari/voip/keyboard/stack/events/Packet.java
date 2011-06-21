package com.cari.voip.keyboard.stack.events;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cari.voip.keyboard.stack.parsers.PacketParseException;
import com.cari.voip.keyboard.stack.parsers.PacketParser;

public class Packet {

	
	public static final int TYPE_INVALIDE = 0;
	public static final int TYPE_AUTH_REQUEST = 1;
	public static final int TYPE_AUTH = TYPE_AUTH_REQUEST+1;
	public static final int TYPE_AUTH_REPLY = TYPE_AUTH_REQUEST+2;

	
	public static final int TYPE_TRAP = 5;
	public static final int TYPE_TRAP_USER = TYPE_TRAP+1;
	public static final int TYPE_TRAP_REG_USER = TYPE_TRAP+2;
	public static final int TYPE_TRAP_DISPATCHER = TYPE_TRAP+3;
	public static final int TYPE_TRAP_CALL = TYPE_TRAP+4;
	
	
	public static final int TYPE_TRAP_ALL_USERS = TYPE_TRAP+5;
	public static final int TYPE_TRAP_ALL_REG_USERS = TYPE_TRAP+6;
	public static final int TYPE_TRAP_ALL_DISPATCHERS = TYPE_TRAP+7;
	public static final int TYPE_TRAP_ALL_CURRENT_CALLS = TYPE_TRAP+8;
	public static final int TYPE_TRAP_MESSAGE = TYPE_TRAP+9;
	
	public static final int TYPE_TRAP_ALL_GATEWAYS = TYPE_TRAP+10;
	public static final int TYPE_TRAP_CALL_DISPATCHER_PROGRESS = TYPE_TRAP+11;
	public static final int TYPE_TRAP_CALL_DISPATCHER_HANGUP = TYPE_TRAP+12;
	public static final int TYPE_TRAP_CALL_DISPATCHER_FAIL = TYPE_TRAP+13;
	public static final int TYPE_TRAP_CALL_DISPATCHER_ANSWER = TYPE_TRAP+14;
	
	public static final int TYPE_TRAP_CFG_DISPAT_CALL = TYPE_TRAP+15;
	public static final int TYPE_TRAP_END = TYPE_TRAP+16;

	public static final int TYPE_REPLY = 30;
	public static final int TYPE_REPLY_DISPAT = TYPE_REPLY+1;
	public static final int TYPE_REPLY_QUERY_ALL_USERS = TYPE_REPLY+2;
	public static final int TYPE_REPLY_QUERY_ALL_REG_USERS = TYPE_REPLY+3;
	public static final int TYPE_REPLY_QUERY_ALL_DISPATCHERS = TYPE_REPLY+4;
	
	public static final int TYPE_REPLY_QUERY_ALL_CURRENT_CALLS = TYPE_REPLY+5;
	public static final int TYPE_REPLY_MESSAGE = TYPE_REPLY+6;
	public static final int TYPE_REPLY_QUERY_ALL_GATEWAYS = TYPE_REPLY+7;
	public static final int TYPE_REPLY_M_REGISTER = TYPE_REPLY+8;
	public static final int TYPE_REPLY_API = TYPE_REPLY+9;
	
	public static final int TYPE_REPLY_ERROR = TYPE_REPLY+10;
	public static final int TYPE_REPLY_END = TYPE_REPLY+11;
	
	public static final int TYPE_COMMAND = 50;
	public static final int TYPE_COMMAND_DISPAT =  TYPE_COMMAND + 1;
	public static final int TYPE_COMMAND_QUERY_ALL_USERS =  TYPE_COMMAND + 2;
	public static final int TYPE_COMMAND_QUERY_ALL_REG_USERS =  TYPE_COMMAND + 3;
	public static final int TYPE_COMMAND_QUERY_ALL_DISPATCHERS =  TYPE_COMMAND + 4;
	
	public static final int TYPE_COMMAND_QUERY_ALL_CURRENT_CALLS =  TYPE_COMMAND + 5;
	public static final int TYPE_COMMAND_MESSAGE =  TYPE_COMMAND + 6;
	public static final int TYPE_COMMAND_QUERY_ALL_GATEWAYS = TYPE_COMMAND + 7;
	public static final int TYPE_COMMAND_M_REGISTER = TYPE_COMMAND + 8;
	public static final int TYPE_COMMAND_API = TYPE_COMMAND + 9;
	
	public static final int TYPE_COMMAND_END = TYPE_COMMAND + 10;
	
	public static final String[] TYPES={
		"invalid-packet-type",
		"auth/request",
		"auth",
		"auth/reply",
		"",
		
		"",
		"trap/user",
		"trap/registratioin",
		"trap/dispatcher",
		"trap/call",
		
		"trap/all-users",
		"trap/all-reg-users",
		"trap/all-dispatchers",
		"trap/all-current-calls",
		"trap/message",
		
		"trap/all-gateways",
		"trap/call-dispatcher-progress",
		"trap/call-dispatcher-hangup",
		"trap/call-dispatcher-fail",
		"trap/call-dispatcher-answer",
		
		"trap/cfg-dispat-call",
		"trap/call-dispatcher-wait",
		"trap/call-dispatcher-hold",
		"",
		"",
		
		"",
		"",
		"",
		"",
		"",
		
		"",
		"reply/dispatch",
		"reply/query-all-users",
		"reply/query-all-reg-users",
		"reply/query-all-dispatchers",
		
		"reply/query-all-current-calls",
		"reply/message",
		"reply/query-all-gateways",
		"reply/m-register",//ok,err
		"reply/api",
		
		"reply/error",
		"",
		"",
		"",
		"",
		
		"",
		"",
		"",
		"",
		"",
		
		"",
		"command/dispatch",
		"command/query-all-users",
		"command/query-all-reg-users",
		"command/query-all-dispatchers",
		
		"command/query-all-current-calls",
		"command/message",
		"command/query-all-gateways",
		"command/m-register",//mcode rcode
		"command/api",
		
		""
		
	};
	
	public static final String PACKET_START_TOKEN = "content-type";
	public static final String ERR_DISCRIPTION = "discription";
	public static final String BODY_LENGTH = "content-length";
	
	public static Map<String,Integer> typeMap = null;
	public static Map<Integer,PacketDescription> descriptionMap = null;
	
	public static int getPacketTypeFromString(String typeString){
		int typeInt = TYPE_INVALIDE;
		if(typeMap == null){
			typeMap = new HashMap<String,Integer>();
			try{
				for(int i = 0; i< TYPES.length;i++){
					if(TYPES[i] != null &&
						TYPES[i].length() != 0){
						typeMap.put(TYPES[i], new Integer(i));
					}
				}
			}
			catch(Exception e){
				
			}
		}
		if(typeString != null && typeString.length() > 0){
			Integer typeInteger = (Integer)(typeMap.get(typeString));
			if(typeInteger != null){
				typeInt = typeInteger.intValue();
			}
		}
		//System.out.print("\t"+typeString+":"+String.valueOf(typeInt)+"\n");
		return typeInt;
	}
	

	public static int getPacketTypeFromLine(String line){
		int typeInt = -1;
		int k = -1;
		int len = 0;
		if(line != null && (len =line.length()) > 0){
			String lowerCaseLine = line.toLowerCase();
			if((k = lowerCaseLine.indexOf(PACKET_START_TOKEN)) >= 0){
				int state = 0;
				k += PACKET_START_TOKEN.length();
				int typeStartIndex= k;
				while(k < len){
					char c = line.charAt(k);
					switch(state){
					case 0://null
						if(Character.isLetter(c)){
							typeStartIndex = k;
							state = 1;
						}
						break;
					case 1://xxx
						if(Character.isSpaceChar(c)){
							String typeString = line.substring(typeStartIndex, k);
							typeInt = getPacketTypeFromString(typeString);
							state = 3;
						}
						break;
					default:
						break;
					}
					
					if(state == 3){
						break;
					}
					k++;
				}
				
				if(typeInt == -1){
					String typeString = line.substring(typeStartIndex);
					typeInt = getPacketTypeFromString(typeString);
				}
			}
		}
		
		if(typeInt == -1){
			typeInt = TYPE_INVALIDE;
		}
		
		return typeInt;
	}
	
	
	public static final String AUTH_REQUEST = "auth/request";
	public static final String AUTH_RESPONSE = "auth";
	public static final String AUTH_REPLY = "auth/reply";
	
	public static final String COMMAND = "command";
	public static final String REPLY = "reply";
	public static final String TRAP = "trap";
	
	public static final String PROP_REPLY_TEXT="reply-text";
	public static final String PROP_ERR_DISPCRIPTION="discription";
	
	public static final String PROP_CONNECTION_ID="connection-id";
	public static final String PROP_DISPATCHER="dispatcher";
	public static final String PROP_PASSWORD="password";
	public static final String PROP_REPLY_OK="ok";
	public static final String PROP_REPLY_ERROR = "err";

	private int packetType;
	
	private String head = null;
	
	private String body = null;
	private XMLBody xmlBody= null;
	
	private ReplyListener replyListener;
	
	public Packet(){
		
	}
	public Packet(int type){
		
		if(type >=0 && type< TYPES.length){
			this.packetType = type;
		}
	}
	
	
	public int getPacketType(){
		return this.packetType;
	}
	public boolean isTrap(){
		return (this.packetType> TYPE_TRAP)&& (this.packetType < TYPE_TRAP_END);
	}
	
	public boolean isReply(){
		return (this.packetType> TYPE_REPLY)&& (this.packetType < TYPE_REPLY_END);
	}
	
	public boolean isCommand(){
		return (this.packetType> TYPE_COMMAND)&& (this.packetType < TYPE_COMMAND_END);
	}
	public void setHead(String head){
		this.head = head;
	}
	public String getHead(){
		return this.head;
	}
	public void setBody(String body) throws XMLParseException{
		if(body == null || body.length() == 0){
			return;
		}
		this.body = body;
		//System.out.print(body.length()+"\n"+body);
		if(this.xmlBody == null){
			this.xmlBody = new XMLBody();
		}
		
		this.xmlBody.parseStr(this.body);
		
	}
	public String getBody(){
		return this.body;
	}
	public XMLBody getXMLBody(){
		return this.xmlBody;
	}
	public void setPacketFromParser(PacketParser parser) throws PacketParseException{
		boolean ok = true;
		Reader reader = null;
		if(parser == null ||
				(reader = parser.getReader()) == null){
			throw new PacketParseException("网络包接收错误：reader 为空");
		}

		String line = parser.readLine();
		int k = -1;
		StringBuilder hb = new StringBuilder(128);
		while(line != null &&
				line.length() > 0){
			
			k = -1;
			k = line.indexOf(':');
			if(k > 0){
				
				String name = line.substring(0, k).trim().toLowerCase();
				String value = line.substring(k+1).trim();
				this.setProperty(name, value);
			}
			
			hb.append(line);
			line = parser.readLine();
		}
		
		if(line == null){
			throw new PacketParseException("网络包接收错误：网络连接中断");
		}
		if(hb.length() > 0){
			this.setHead(hb.toString());
		}
		Object bodyLen = this.getProperty(Packet.BODY_LENGTH);
		int len =0;
		int lenOri = 0;
		if(bodyLen != null){
			try{
				len = Integer.parseInt(bodyLen.toString());
			}
			catch(NumberFormatException e){
				throw new PacketParseException("网络包接收错误：包长解析错误");
			}
		}
		if(len > 0){
			//System.out.print("\t len:"+String.valueOf(len)+"\n");
				StringBuilder bb = new StringBuilder(len);
				lenOri = len;
				//boolean first = true;
				while(len > 0){
				
					try{
						int reLen= 
							reader.read(parser.bodyBuf, 0, 1);//len>parser.bodyBuf.length?parser.bodyBuf.length:len);
						if(reLen == -1){
							break;//end of stream
						}
						if(reLen > 0){
							
							String ad = String.valueOf(parser.bodyBuf, 0, reLen);
							bb.append(ad);
							
							len -= ad.getBytes("UTF-8").length;
							/*if(first && (lenOri-len) !=  bb.toString().length()){
								first = false;
								System.out.print(reLen+"=>"+(lenOri-len)+"-"+bb.toString().length()+"\n"+bb.toString());
							}*/
						}
						
					}
					catch(IOException e){
						throw new PacketParseException("网络包接收错误：网络IO错误");
					}
					
				}
				
				//if(bb.length() == lenOri){
					try{
						this.setBody(bb.toString());
					}
					catch(XMLParseException e){
						throw new PacketParseException("网络包xml内容解析错误："+e.getMessage());
					}
				//}
				//else{
				//	throw new PacketParseException("网络包接收错误：网络中断导致包内部没有全部接收");
				//}
			
			
		}

	}
	 private final Map<String,Object> properties = new HashMap<String, Object>();
	 
	    /**
	     * Returns the packet property with the specified name or <tt>null</tt> if the
	     * property doesn't exist. Property values that were orginally primitives will
	     * be returned as their object equivalent. For example, an int property will be
	     * returned as an Integer, a double as a Double, etc.
	     *
	     * @param name the name of the property.
	     * @return the property, or <tt>null</tt> if the property doesn't exist.
	     */
	    public synchronized Object getProperty(String name) {
	        if (properties == null) {
	            return null;
	        }
	        return properties.get(name);
	    }

	    /**
	     * Sets a property with an Object as the value. The value must be Serializable
	     * or an IllegalArgumentException will be thrown.
	     *
	     * @param name the name of the property.
	     * @param value the value of the property.
	     */
	    public synchronized void setProperty(String name, Object value) {
	        if (!(value instanceof Serializable)) {
	            throw new IllegalArgumentException("Value must be serialiazble");
	        }
	        properties.put(name, value);
	    }
	    
	    
	    public void setReplyHandler(ReplyListener replyHandler){
	    	this.replyListener = replyHandler;
	    }
	    public ReplyListener getReplyHandler(){
	    	return this.replyListener;
	    }
	    
	    
	    public String toString(){
	    	int type = 0;
	    	if(this.packetType >=0 && this.packetType< TYPES.length){
				type = this.packetType;
			}
	    	
	    	String typeString = TYPES[type];
			
			StringBuilder sb = new StringBuilder(128);
			sb.append(PACKET_START_TOKEN+":"+typeString+"\n");
			
			int props = properties.size();
			if(props > 0){
				for(String key:properties.keySet()){
					if(key != null){
						String val = (String)properties.get(key);
						if(val != null){
							sb.append(key+":"+val+"\n");
						}
					}
				}
			}
			
			if(this.body != null){
				int len;
				try {
					len = this.body.getBytes("UTF-8").length;
					sb.append(Packet.BODY_LENGTH+":"+String.valueOf(len)+"\n");
					sb.append("\n");
					sb.append(body);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					sb.append("\n");
				}
				
				
			}
			else{
				sb.append("\n");
			}
			return sb.toString();
	    }
	    protected static void makeDescriptionMap(){
	    	if(descriptionMap != null){
	    		return;
	    	}
	    	descriptionMap = new HashMap<Integer,PacketDescription>();
	    	
	    	descriptionMap.put(new Integer(Packet.TYPE_REPLY_DISPAT), new PacketDescription(){

				@Override
				public String[] getDescription(Packet packet) {
					
					if(packet.getPacketType() == Packet.TYPE_REPLY_DISPAT){
						//StringBuilder sb = new StringBuilder();
						List<String> re = new ArrayList<String>(3);
						re.add("调度");
						Object replytext = packet.getProperty("reply-text");
						if(replytext != null && replytext instanceof String){
							String replytextstring = (String)replytext;
							if(replytextstring.indexOf("err") >= 0 ){
								re.add("不成功");
								Object description = packet.getProperty(Packet.ERR_DISCRIPTION);
								if(description != null && description instanceof String){
									String descriptionString = (String)description;
									re.add(descriptionString);
									
								}
							}
							else{
								re.add("成功");
							}
						}
						return re.toArray(new String[]{});
					}
					return null;
				}});
	    	
	    	
	    	descriptionMap.put(new Integer(Packet.TYPE_REPLY_MESSAGE), new PacketDescription(){

				@Override
				public String[] getDescription(Packet packet) {
					
					if(packet.getPacketType() == Packet.TYPE_REPLY_MESSAGE){
						//StringBuilder sb = new StringBuilder();
						List<String> re = new ArrayList<String>(3);
						//sb.append("短信");
						re.add("短信");
						Object replytext = packet.getProperty("reply-text");
						if(replytext != null && replytext instanceof String){
							String replytextstring = (String)replytext;
							if(replytextstring.indexOf("err") >= 0 ){
								re.add("不成功");
								
								Object description = packet.getProperty(Packet.ERR_DISCRIPTION);
								if(description != null && description instanceof String){
									String descriptionString = (String)description;
									re.add(descriptionString);
									
								}
							}
							else{
								re.add("成功");
							}
						}
						
						return re.toArray(new String[]{});
					}
					return null;
				}
				}
	    	);
	    	
	    	descriptionMap.put(new Integer(Packet.TYPE_TRAP_CALL), new PacketDescription(){

				@Override
				public String[] getDescription(Packet packet) {
					
					if(packet.getPacketType() == Packet.TYPE_TRAP_CALL){
						//StringBuilder sb = new StringBuilder();
						List<String> re = new ArrayList<String>(3);
						//sb.append("短信");
						
						String userId = (String)packet.getProperty("id");
						String action = (String)packet.getProperty("action");
						String bound = (String)packet.getProperty("bound");
						String flag = (String)packet.getProperty("flag");
						
						re.add(userId);
						
						if(action.equals("ring")){
						
							String d = "";
							if(bound !=null){
							if(bound.indexOf("in") >= 0){
								re.add("回铃");
								d += "主叫";
							}
							else{
								re.add("振铃");
								d += "被叫";
							}}
							else{
								re.add("铃声");
							}
							
							re.add(d);
						}
						else if(action.equals("answer")){
							re.add("应答");
							String d = "";
							if(bound != null){
								
							if(bound.indexOf("in") >= 0){
								d += "主叫";
							}
							else{
								d += "被叫";
							}
							
							}
							re.add(d);
						}
						else if(action.equals("add") || action.equals("flag")){
							re.add("开始通话");
							String d = "";
							if(bound !=null){
							if(bound.indexOf("in") >= 0){
								d += "主叫";
							}
							else{
								d += "被叫";
							}}
							
							if(flag != null){
							if(flag.indexOf("speak") >= 0){
								d += "(听说)";
							}
							else{
								d += "(听)";
							}}
							re.add(d);
						}else if(action.equals("del")){
							re.add("结束通话");
							String d = "";
							if(bound != null){
								
							if(bound.indexOf("in") >= 0){
								d += "主叫";
							}
							else{
								d += "被叫";
							}
							
							}
							re.add(d);
						}
						else if(action.equals("hangup")){
							re.add("挂机");
							String d = "";
							if(bound != null){
								
							if(bound.indexOf("in") >= 0){
								d += "主叫";
							}
							else{
								d += "被叫";
							}
							
							}
							re.add(d);
						}
						
						return re.toArray(new String[]{});
					}
					return null;
				}
				}
	    	);
	    	
	    		
	    }
	    public String[] getDiscription(){
	    	if(descriptionMap == null){
	    		makeDescriptionMap();
	    	}
	    	PacketDescription d = descriptionMap.get(new Integer(this.packetType));
	    	if(d != null){
	    		return d.getDescription(Packet.this);
	    	}
	    	return null;
	    	//return new String[]{TYPES[this.packetType]};
	    }
	    
}
