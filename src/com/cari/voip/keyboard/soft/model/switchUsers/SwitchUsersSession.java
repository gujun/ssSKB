package com.cari.voip.keyboard.soft.model.switchUsers;

import java.sql.*;
import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;

import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.stack.CCKPConfiguration;
import com.cari.voip.keyboard.stack.CCKPConnection;
import com.cari.voip.keyboard.stack.CCKPConnectionException;
import com.cari.voip.keyboard.stack.ConnectionConfiguration;
import com.cari.voip.keyboard.stack.ConnectionListener;
import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.ReplyListener;
import com.cari.voip.keyboard.stack.events.TrapEventListener;
import com.cari.voip.keyboard.stack.events.XMLBody;
import com.cari.voip.keyboard.stack.events.XMLNode;
import com.cari.voip.keyboard.stack.events.XMLParseException;
import org.postgresql.*;
import java.io.*;
import jxl.*;
import jxl.write.*;

public class SwitchUsersSession extends SwitchEntity{
	private String dispatcher;
	private boolean dispatcherOnline = false;
	public static final String PROP_DISPATCHER_ONLINE="SwitchUsersSession.dispatcheronline";
	
	private SwitchDispatchCtrl ctrl_call = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_CALL);
	private SwitchDispatchCtrl ctrl_wiretapping = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_WIRE_TAPPING);
	private SwitchDispatchCtrl ctrl_insert = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_INSERT);
	private SwitchDispatchCtrl ctrl_grab = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_GRAB);
	private SwitchDispatchCtrl ctrl_unbridge = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_UNBRIDGE);
	private SwitchDispatchCtrl ctrl_group_call = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_GROUP_CALL);
	private SwitchDispatchCtrl ctrl_just_hear = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_JUST_HEAR);
	private SwitchDispatchCtrl ctrl_hear_and_speak = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_HEAR_AND_SPEAK);
	private SwitchDispatchCtrl ctrl_add_member = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_ADD_MEMBER);
	private SwitchDispatchCtrl ctrl_kick_member = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_KICK_MEMBER);
	private SwitchDispatchCtrl ctrl_moderator = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_MODERATOR);
	private SwitchDispatchCtrl ctrl_no_moderator = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_NO_MODERATOR);	
	
	private SwitchDispatchCtrl ctrl_answer =new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_ANSWER);	
	private SwitchDispatchCtrl ctrl_replace =new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_REPLACE);	
	private SwitchDispatchCtrl ctrl_direct = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_DIRECT);	
	private SwitchDispatchCtrl ctrl_forward = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_FORWARD);	
	private SwitchDispatchCtrl ctrl_cancel = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_CANCEL);	
	private SwitchDispatchCtrl ctrl_hold = new SwitchDispatchCtrl(SwitchDispatchCtrl.CTRL_TYPE_HOLD);	
	
	
	
	
	private SwitchUsersManager localUserManager;
	private SwitchUsersManager gatewayUserManager;
	
	
	//private static  SwitchUsersSession singleton = null;
	private ConnectionConfiguration connectionDetail;
	private CCKPConnection connection = null;
	
	private ReplyListener replyListener;
	private TrapEventListener trapListener;
	
	private Thread queryThread = null;
	private boolean done = true;
	
	private static final int INPUT_STATE_ZERO = 0;
	private static final int INPUT_STATE_CTRL = INPUT_STATE_ZERO+1;
	
	public static final int INPUT_TYPE_DIAL = 0;
	public static final int INPUT_TYPE_CTRL = INPUT_TYPE_DIAL+1;
	public static final int INPUT_TYPE_USER = INPUT_TYPE_DIAL+2;
	public static final int INPUT_TYPE_GROUP = INPUT_TYPE_DIAL+3;
	
	
	private Semaphore querySemaphore;
	
	private int inputState;
	private SwitchDispatchCtrl currentCtrl;
	
	private String dburl;
	private Connection dbconnection;
	
	//ConnectionListener connectionListener;
	public SwitchUsersSession(){
		this.localUserManager = new SwitchUsersManager("本地");
		this.gatewayUserManager = new SwitchUsersManager("网关");
		this.inputState = INPUT_STATE_ZERO;
		
		this.trapListener = new TrapEventListener(){
			public void processTrap(Packet event){
				
				switch(event.getPacketType()){
				case Packet.TYPE_TRAP_CALL:
					processTrapCall(event);
					break;
				case Packet.TYPE_TRAP_DISPATCHER:
					processTrapDispatcher(event);
					break;
				case Packet.TYPE_TRAP_REG_USER:
					processTrapRegistration(event);
					break;
				case Packet.TYPE_TRAP_ALL_USERS:
					processTrapAllUsers(event);
					break;
				case Packet.TYPE_TRAP_ALL_GATEWAYS:
					processTrapAllGateways(event);
					break;
				case Packet.TYPE_TRAP_ALL_DISPATCHERS:
					processTrapAllDispatchers(event);
				default:
					break;
				}
			}
		};
		this.replyListener = new ReplyListener(){
			public void processReply(Packet command,Packet reply){
				switch(reply.getPacketType()){
				case Packet.TYPE_REPLY_QUERY_ALL_USERS:
					processReplyQueryAllUsers(reply);
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_REG_USERS:
					processReplyQueryAllRegUsers(reply);
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_GATEWAYS:
					processReplyQueryAllGateways(reply);
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_DISPATCHERS:
					processReplyQueryAllDispatchers(reply);
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_CURRENT_CALLS:
					processReplyQueryAllCurrentCalls(reply);
					break;
				default:
					break;
				}
			}
		};
		
		
		
	}
	
	
	protected void processTrapAllUsers(Packet event) {
		processReplyQueryAllUsers(event);
	}
	protected void processTrapAllGateways(Packet event) {
		processReplyQueryAllGateways(event);
	}


	protected void processTrapCall(Packet event){
		
		if(event == null){
			return;
		}
		
		String userId = (String)event.getProperty("id");
		String action = (String)event.getProperty("action");
		String callName =  (String)event.getProperty("call-name");
		String member =  (String)event.getProperty("member");
		
		
		
		if(userId == null || userId.length() == 0 ||
				action == null || action.length() == 0 ||
					callName == null || callName.length() == 0 ||
					member == null || member.length() == 0){
			return;
		}
		String type =  (String)event.getProperty("type");
		String gateway_name = (String)event.getProperty("gateway-name");
		
		SwitchUser userLocal =null;
		if(type != null && type.equals("gateway") && 
				gateway_name != null && gateway_name.length() > 0){
				
				SwitchUsersGroup gw = this.gatewayUserManager.getSwitchUsersGroup(gateway_name);
				SwitchUsersGroup gwDefault = this.gatewayUserManager.getMembersGroup();
				
				String userLocalId = userId+"-"+gateway_name;//+"-"+callName+"-"+member;
				//userLocal = this.gatewayUserManager.getSwitchUserOfGroup(userLocalId, gateway_name);
				if(gwDefault != null){
					userLocal = gwDefault.getSwitchUser(userLocalId);
				}
				if(action.equals("ring")){
					if(gw != null && userLocal == null){
						userLocal = this.gatewayUserManager.addSwitchUserOfGroup(userLocalId, gateway_name);
					}
					if(userLocal != null){
						String bound =  (String)event.getProperty("bound");
						if(bound != null && bound.indexOf("in")>=0){
							userLocal.callPresenceSet(null, Presence.CALL_RING_BACK,false);
						}
						else{
							userLocal.callPresenceSet(null, Presence.CALL_RING,false);
						}
					}
				}
				else if(action.equals("answer")){
					if(gw != null && userLocal == null){
						userLocal = this.gatewayUserManager.addSwitchUserOfGroup(userLocalId, gateway_name);
					}
					if(userLocal != null){
							userLocal.callPresenceSet(null, Presence.CALL_ANSWER,false);
					}
				}
				else if(action.equals("add") || action.equals("flag")){
					if(gw != null && userLocal == null){
						userLocal = this.gatewayUserManager.addSwitchUserOfGroup(userLocalId, gateway_name);
					}
					if(userLocal != null){
					String flag = (String)event.getProperty("flag");
					
					if(flag != null){ 
						if(flag.indexOf("speak") > 0){
							if(flag.indexOf("talk") > 0){
								userLocal.callPresenceSet(callName, Presence.CALL_SPEAK,true);
							}
							else{
								userLocal.callPresenceSet(callName, Presence.CALL_SPEAK,false);
							}
						}else{
							if(flag.indexOf("talk") > 0){
								userLocal.callPresenceSet(callName, Presence.CALL_MUTE,true);
							}
							else{
								userLocal.callPresenceSet(callName, Presence.CALL_MUTE,false);
							}
							//userLocal.setPresence(Presence.CALL_SPEAK);
						}
						
					}
					else{
						userLocal.callPresenceSet(callName, Presence.CALL_MUTE,false);
					}
					}
				}
				//else if(action.equals("flag")){}
				else if(action.equals("del")){
					if(userLocal != null){
						userLocal.callPresenceRemove(callName);
						this.gatewayUserManager.removeSwitchUser(userLocal);
					}
				}
				else if(action.equals("hangup")){
					if(userLocal != null){
						userLocal.callPresenceRemove(null);
						this.gatewayUserManager.removeSwitchUser(userLocal);
					}
				}
				
			
		}else{

			userLocal =this.localUserManager.getSwitchUser(userId);
			if(userLocal != null){
				if(action.equals("ring")){
					String bound =  (String)event.getProperty("bound");
					if(bound != null && bound.indexOf("in")>=0){
						userLocal.callPresenceSet(null, Presence.CALL_RING_BACK,false);
					}
					else{
						userLocal.callPresenceSet(null, Presence.CALL_RING,false);
					}
				}
				else if(action.equals("answer")){
					userLocal.callPresenceSet(null, Presence.CALL_ANSWER,false);
				}
				else if(action.equals("add") || action.equals("flag")){
					String flag = (String)event.getProperty("flag");
					
					if(flag != null){ 
						if(flag.indexOf("speak") > 0){
							if(flag.indexOf("talk") > 0){
								userLocal.callPresenceSet(callName, Presence.CALL_SPEAK,true);
							}
							else{
								userLocal.callPresenceSet(callName, Presence.CALL_SPEAK,false);
							}
						}else{
							if(flag.indexOf("talk") > 0){
								userLocal.callPresenceSet(callName, Presence.CALL_MUTE,true);
							}
							else{
								userLocal.callPresenceSet(callName, Presence.CALL_MUTE,false);
							}
							//userLocal.setPresence(Presence.CALL_SPEAK);
						}
						
					}
					else{
						userLocal.callPresenceSet(callName, Presence.CALL_MUTE,false);
					}
				}
				else if(action.equals("del")){
					//userLocal.callPresenceSet(callName, Presence.ON_LINE,false);
					userLocal.callPresenceRemove(callName);
				}
				else if(action.equals("hangup")){
					
					userLocal.callPresenceRemove(null);
				}
			}
		}
		

	
	}
	protected void dispatcherHover(){
		this.ctrl_call.setEnable(true);
		this.ctrl_wiretapping.setEnable(true);
		this.ctrl_insert.setEnable(true);
		this.ctrl_grab.setEnable(true);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(true);

		this.ctrl_just_hear.setEnable(false);
		this.ctrl_hear_and_speak.setEnable(false);
		this.ctrl_add_member.setEnable(false);
		this.ctrl_kick_member.setEnable(false);
		this.ctrl_moderator.setEnable(false);
		this.ctrl_no_moderator.setEnable(false);
		
		this.ctrl_answer.setEnable(true);
		this.ctrl_replace.setEnable(true);
		this.ctrl_direct.setEnable(true);
		this.ctrl_forward.setEnable(false);
		this.ctrl_cancel.setEnable(false);
		
		this.ctrl_hold.setEnable(false);
	}
	protected void dispatcherBridge(){
		
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	protected void dispatcherHear(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(true);
		this.ctrl_grab.setEnable(true);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	protected void dispatcherGrab(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	protected void dispatcherInsert(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	protected void dispatcherGroup(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	
	protected void dispatcherForward(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(true);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(true);
		this.ctrl_hear_and_speak.setEnable(true);
		this.ctrl_add_member.setEnable(true);
		this.ctrl_kick_member.setEnable(true);
		this.ctrl_moderator.setEnable(true);
		this.ctrl_no_moderator.setEnable(true);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(true);
		this.ctrl_cancel.setEnable(true);
		
		this.ctrl_hold.setEnable(true);
	}
	protected void dispatcherNone(){
		this.ctrl_call.setEnable(false);
		this.ctrl_wiretapping.setEnable(false);
		this.ctrl_insert.setEnable(false);
		this.ctrl_grab.setEnable(false);
		this.ctrl_unbridge.setEnable(false);
		this.ctrl_group_call.setEnable(false);
		
		this.ctrl_just_hear.setEnable(false);
		this.ctrl_hear_and_speak.setEnable(false);
		this.ctrl_add_member.setEnable(false);
		this.ctrl_kick_member.setEnable(false);
		this.ctrl_moderator.setEnable(false);
		this.ctrl_no_moderator.setEnable(false);
		
		this.ctrl_answer.setEnable(false);
		this.ctrl_replace.setEnable(false);
		this.ctrl_direct.setEnable(false);
		this.ctrl_forward.setEnable(false);
		this.ctrl_cancel.setEnable(false);
		
		this.ctrl_hold.setEnable(false);
	}
	protected void dispatchStatus(String dispatcherId,String status){
		
		if(dispatcherId == null || dispatcherId.length() == 0 ||
				status == null || status.length() == 0){
			return;
		}
		if(!dispatcherId.equals(this.dispatcher)){
			return;
		}
		dispatcherOnline = true;
		if(status.equals("hover")){
			dispatcherHover();
			
		}
		else if(status.equals("group")){
			dispatcherGroup();
		}
		else if(status.equals("forward")){
			dispatcherForward();
		}
		else if(status.equals("bridge")){
			dispatcherBridge();
		}
		else if(status.equals("hear")){
			dispatcherHear();
		}
		else if(status.equals("grab")){
			dispatcherGrab();
		}
		else if(status.equals("insert")){
			dispatcherInsert();
		}
		else{ //status.equals("none") || status.equals("end")) || other
			dispatcherNone();
			dispatcherOnline = false;
			this.inputState = INPUT_STATE_ZERO;
		}
		this.firePropertyChange(PROP_DISPATCHER_ONLINE, null, status);
	}
	protected void processTrapDispatcher(Packet event){
		if(this.dispatcher == null){
			return;
		}
		if(event == null){
			return;
		}
		String dispatcherId = (String)event.getProperty("id");
		String status = (String)event.getProperty("status");

		this.dispatchStatus(dispatcherId, status);
		
	}
	protected void processTrapRegistration(Packet event){
		if(event == null){
			return;
		}
		String userId = (String)event.getProperty("user-id");
		String status = (String)event.getProperty("status");
		if(userId == null || userId.length() == 0 ||
				status == null || status.length() == 0){
			return;
		}
		SwitchUser userLocal =this.localUserManager.getSwitchUser(userId);
		if(userLocal == null){
			return;
		}
		if(status.equals("reg")){
			String agent = (String)event.getProperty("agent");
			String ip = (String)event.getProperty("ip");
			String port = (String)event.getProperty("port");
			
			userLocal.setAgent(agent);
			userLocal.setIP(ip);
			userLocal.setReg(true);
			if(userLocal.getPresence() == Presence.OFF_LINE){
				userLocal.setPresence(Presence.ON_LINE);
			}
			
			
		}
		else if(status.equals("unreg") || status.equals("expire")){
			userLocal.setReg(false);
			userLocal.setPresence(Presence.OFF_LINE);
		}
		
		
	}
	protected void processReplyQueryAllCurrentCalls(Packet reply){
		
		
		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		/*<calls>
		<call name="1602-1601-192.168.1.7">
		<part member="2" type="user" bound="out" id="1602" flag="hear|speak"/>
		<part member="1" type="user" bound="in" id="1602" flag="hear|speak|floor"/>
		</call>
		</calls>*/
		XMLNode calls = body.getXMLRoot();
		if(calls == null){
			return;
		}
		XMLNode call = calls.getChild("call");
		
		while(call != null){
			String name = call.getAttr("name");
			if(name == null || name.length() == 0){
				call = call.getNext();
				continue;
			}
			XMLNode part = call.getChild("part");
			while(part != null){
				String id = part.getAttr("id");
				String flag = part.getAttr("flag");
				String member = part.getAttr("member");
				
				if(id == null || id.length() ==0 ||
					member == null || member.length() == 0	){
					part = part.getNext();
					continue;
				}
				//SwitchUser userLocal =this.localUserManager.getSwitchUser(id);
				String gateway_name = part.getAttr("gateway-name");
				String type = part.getAttr("type");
				//String bound = part.getAttr("bound");
				
				SwitchUser userLocal =null;
				if(type != null && type.equals("gateway") && 
						gateway_name != null && gateway_name.length() > 0){
					SwitchUsersGroup gw = this.gatewayUserManager.getSwitchUsersGroup(gateway_name);
					if(gw != null){
						String userLocalId = id+"-"+gateway_name+"-"+name+"-"+member;
						
						userLocal = this.gatewayUserManager.getSwitchUserOfGroup(userLocalId, gateway_name);
						if(userLocal == null){
							userLocal = this.gatewayUserManager.addSwitchUserOfGroup(userLocalId, gateway_name);
						}
					}
				}else{

					userLocal =this.localUserManager.getSwitchUser(id);
				}
				if(userLocal != null && flag != null){
					
					if(flag.indexOf("speak") > 0){
						if(flag.indexOf("talk") > 0){
							userLocal.callPresenceSet(name, Presence.CALL_SPEAK, true);
						}else{
							userLocal.callPresenceSet(name, Presence.CALL_SPEAK, false);
						}
					}
					else{
						if(flag.indexOf("talk") > 0){
							userLocal.callPresenceSet(name, Presence.CALL_MUTE, true);
						}else{
							userLocal.callPresenceSet(name, Presence.CALL_MUTE, false);
						}
						//userLocal.setPresence(Presence.CALL_MUTE);
					}
				}
				part = part.getNext();
			}

			
			call = call.getNext();
		}
		
	}
	/*protected void processReplyQueryAllRegUsers(Packet reply){
		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		XMLNode xmlRoot = body.getXMLRoot();
		if(xmlRoot == null){
			return;
		}
		XMLNode users = xmlRoot.getChild("users");
		if(users == null){
			return;
		}
		XMLNode user = users.getChild("user");
		
		while(user != null){
			String id = user.getAttr("id");
			SwitchUser userLocal =this.localUserManager.getSwitchUser(id);
			if(userLocal != null){
				String agent = user.getAttr("agent");
				String ip = user.getAttr("ip");
				//String port = user.getAttr("port");
				userLocal.setAgent(agent);
				userLocal.setIP(ip);
				userLocal.setReg(true);
				userLocal.setPresence(Presence.ON_LINE);
				
			}
			
			user = user.getNext();
		}
		
	}*/
	protected void processAllDispatchers(XMLNode dispatchers){
		if(dispatchers  == null){
			return;
		}
		long dispatStampTimer = this.localUserManager.dispatStampInc();
		XMLNode dispatcher = dispatchers.getChild("dispatcher");
		
		while(dispatcher != null){
			String id = dispatcher.getAttr("id");
			SwitchUser userLocal =this.localUserManager.getSwitchUser(id);
			if(userLocal != null){
				userLocal.setIsDispat(true);
				userLocal.setDispatStamp(dispatStampTimer);
			}
			dispatcher = dispatcher.getNext();
		}
		
		this.localUserManager.removeOldUserIsDispat();
	}
	protected void processTrapAllDispatchers(Packet event){
		if(event == null){
			return;
		}
		XMLBody body = event.getXMLBody();
		if(body == null){
			return;
		}
		/*<configuration>
		 * <dispatchers>
        <dispatcher id="1001" state="none"/>
        <dispatcher id="1000" state="hover"/>
</dispatchers>
</configuration>*/
		XMLNode root = body.getXMLRoot();
		if(root == null){
			return;
		}
		
		
		XMLNode dispatchers = root.getChild("dispatchers");
		if(dispatchers == null){
			return;
		}
		processAllDispatchers(dispatchers);
	}
	protected void processReplyQueryAllDispatchers(Packet reply){
		
		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		/*<dispatchers>
        <dispatcher id="1001" state="none"/>
        <dispatcher id="1000" state="hover"/>
</dispatchers>*/
		XMLNode dispatchers = body.getXMLRoot();
		if(dispatchers == null){
			return;
		}
		processAllDispatchers(dispatchers);
		if(this.dispatcher == null){
			return;
		}
		XMLNode dispatcher = dispatchers.getChild("dispatcher");
		
		while(dispatcher != null){
			String id = dispatcher.getAttr("id");
			String status = dispatcher.getAttr("status");//status

			this.dispatchStatus(id, status);
			
			dispatcher = dispatcher.getNext();
		}
		
	}
	protected void processReplyQueryAllRegUsers(Packet reply){
		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		XMLNode xmlRoot = body.getXMLRoot();
		if(xmlRoot == null){
			return;
		}
		XMLNode users = xmlRoot.getChild("users");
		if(users == null){
			return;
		}
		XMLNode user = users.getChild("user");
		
		while(user != null){
			String id = user.getAttr("id");
			SwitchUser userLocal =this.localUserManager.getSwitchUser(id);
			if(userLocal != null){
				String agent = user.getAttr("agent");
				String ip = user.getAttr("ip");
				//String port = user.getAttr("port");
				userLocal.setAgent(agent);
				userLocal.setIP(ip);
				userLocal.setReg(true);
				if(userLocal.getPresence() == Presence.OFF_LINE){
					userLocal.setPresence(Presence.ON_LINE);
				}
				
			}
			
			user = user.getNext();
		}
		
	}
	protected void processReplyQueryAllUsers(Packet reply){
		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		XMLNode xmlRoot = body.getXMLRoot();
		if(xmlRoot == null){
			return;
		}
		XMLNode domain = xmlRoot.getChild("domain");
		if(domain == null){
			return;
		}
		XMLNode groups = domain.getChild("groups");
		if(groups == null){
			return;
		}
		long refreshTimer = this.localUserManager.refreshTimerInc();
		
		XMLNode group = groups.getChild("group");
		while(group != null){
			String groupName = group.getAttr("name");
			if( groupName != null){
				if(groupName.equals("default")){
					XMLNode users = group.getChild("users");
					if(users != null){
						XMLNode user = users.getChild("user");
						while(user != null){
							String id = user.getAttr("id");
							SwitchUser userLocal = this.localUserManager.getSwitchUser(id);
							if(userLocal != null){
								userLocal.setRefreshTimer(refreshTimer);
							}
							else{
								userLocal =this.localUserManager.addSwitchUser(id);
							}
							if(userLocal != null){
								XMLNode variables = user.getChild("variables");
								if(variables != null){
									XMLNode variable = variables.getChild("variable");
									while(variable != null){
										String vname = variable.getAttr("name");
										String vvalue = variable.getAttr("value");
										if(vname != null && vname.equals("desc")){
											userLocal.setDesc(vvalue);
											
										}
										variable = variable.getNext();
									}
								}
							}
							user = user.getNext();
						}
					}
					
					
				}
				else{
					SwitchUsersGroup groupLocal = 
						this.localUserManager.getSwitchUsersGroup(groupName);
					if(groupLocal != null){
						groupLocal.setRefreshTimer(refreshTimer);
						
					}
					else
					{
						groupLocal = this.localUserManager.addSwitchUsersGroup(groupName);
					}
					
					XMLNode users = group.getChild("users");
					if(users != null){
						XMLNode user = users.getChild("user");
						while(user != null){
							String id = user.getAttr("id");
							String type = user.getAttr("type");
							SwitchUser userLocal =null;
							if(type != null && type.equals("pointer")){
								userLocal = this.localUserManager.getSwitchUser(id);
								
							}
							if(userLocal != null){
								if(userLocal.isOfGroup(groupLocal.getName())){
									groupLocal.updateRelationTimer(userLocal);
								}
								else{
									groupLocal.addSwitchUser(userLocal);
								}
								
							}
							user = user.getNext();
						}
					}
				}
			}
			group = group.getNext();
		}
		this.localUserManager.removeOldUser();
		this.localUserManager.removeOldGroups();
		this.localUserManager.removeOldRelation();
		
	}
	protected void processReplyQueryAllGateways(Packet reply){

		if(reply == null){
			return;
		}
		XMLBody body = reply.getXMLBody();
		if(body == null){
			return;
		}
		XMLNode xmlRoot = body.getXMLRoot();
		if(xmlRoot == null){
			return;
		}
		XMLNode profiles = xmlRoot.getChild("profiles");
		if(profiles == null){
			return;
		}
		XMLNode profile = profiles.getChild("profile");
		if(profile == null){
			return;
		}
		
		String profileName = profile.getAttr("name");
		while(profileName == null || !profileName.equals("internal")){
			profile = profile.getNext();
			if(profile == null){
				return;
			}
			profileName = profile.getAttr("name");
		}
		XMLNode internal = profile;
		if(internal == null){
			return;
		}
		XMLNode gateways = internal.getChild("gateways");
		if(gateways == null){
			return;
		}
		long refreshTimer = this.gatewayUserManager.refreshTimerInc();
		
		XMLNode gateway = gateways.getChild("gateway");
		
		while(gateway != null){
			String gatewayName = gateway.getAttr("name");
			if( gatewayName != null){
					SwitchUsersGroup groupLocal = 
						this.gatewayUserManager.getSwitchUsersGroup(gatewayName);
					if(groupLocal != null){
						groupLocal.setRefreshTimer(refreshTimer);
						
					}
					else
					{
						groupLocal = this.gatewayUserManager.addSwitchUsersGroup(gatewayName);
					}
					
			}
			gateway = gateway.getNext();
		}
		
		this.gatewayUserManager.removeOldGroups();
		
	}
	/*public static  SwitchUsersSession getSingleton(){
		if(SwitchUsersSession.singleton == null){
			SwitchUsersSession.singleton = new SwitchUsersSession();
		}
		return SwitchUsersSession.singleton;
	}
	*/
	protected void queryLoop(Thread thread){
		while(!done && this.queryThread == thread){
		
			query();
			
			try {
				Thread.sleep(CCKPConfiguration.getQueryLoopInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	protected void startQueryThread(){
		this.done = false;

		this.queryThread = new Thread(){
			public void run(){
				queryLoop(this);
			}
		};
		this.queryThread.setName("SwitchUsersSession query loop");
		this.queryThread.setDaemon(true);
		this.queryThread.start();
	}
	protected void endQueryThread(){
		this.done = true;
		try {
			this.queryThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		this.queryThread = null;
	}
	protected void addListener(){

		this.dispatcher = this.getConnectionDetail().getPhoneId();
		this.connection.addTrapEventListeners(this.trapListener, null);
		this.connection.addReplyListeners(this.replyListener, null);
		
	}
	protected void removeListener(){
		
		this.connection.removeTrapEventListeners(this.trapListener);
		this.connection.removeReplyListeners(this.replyListener);
		
		
	}
	public void setConnection(CCKPConnection connection){
		if(connection == null){
			if(this.connection != connection){
				removeListener();
				endQueryThread();
			}
			return;
		}
		if(this.connection == null ){
			this.connection = connection;
			addListener();
			startQueryThread();
		}
		else if(this.connection != connection){
			removeListener();
			endQueryThread();
			this.connection = connection;
			addListener();
			startQueryThread();
		}
	}
	
	
	public SwitchUsersSession getRoot(){
		return this;
	}
	public SwitchUsersManager getlocalUserManager(){
		return this.localUserManager;
	}
	public SwitchUsersManager getgatewayUserManager(){
		return this.gatewayUserManager;
	}
	public SwitchUsersGroup getMembersGroup(){
		if(this.localUserManager != null){
			return this.localUserManager.getMembersGroup();
		}
		return null;
	}
	public Object[] toGroupsArray(){
		return new SwitchUsersManager[]{this.localUserManager,this.gatewayUserManager};
	}
	/*
	public void init(){
		Packet packet = null;
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_USERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_REG_USERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_DISPATCHERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_CURRENT_CALLS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		packet = new Packet(Packet.TYPE_COMMAND_DISPAT);
		packet.setProperty("dispatch-type", "hear");
		packet.setProperty("dispatcher", "1000");
		packet.setProperty("destination", "1001");
		
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		//Packet packet = getReplyQueryAllUsersTestPacket();
		
		//processReplyQueryAllUsers(packet);
		//this.initModel();
	}*/
	public void sendMRegister(String mcode,String rcode,ReplyListener replyHandler){
		Packet packet = null;

		
		packet = new Packet(Packet.TYPE_COMMAND_M_REGISTER);
		packet.setProperty("mcode", mcode);
		packet.setProperty("rcode", rcode);
		packet.setReplyHandler(replyHandler);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	public void query(){
		Packet packet = null;

		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_USERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_REG_USERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_DISPATCHERS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_GATEWAYS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		packet = new Packet(Packet.TYPE_COMMAND_QUERY_ALL_CURRENT_CALLS);
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		/*packet = new Packet(Packet.TYPE_COMMAND_DISPAT);
		packet.setProperty("dispatch-type", "hear");
		packet.setProperty("dispatcher", "1000");
		packet.setProperty("destination", "1001");
		
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}*/
		
	}
	private Packet getReplyQueryAllUsersTestPacket() {
		Packet packet= new Packet(Packet.TYPE_REPLY_QUERY_ALL_USERS);
		String body =  "<?xml version=\"1.0\"?>"+
		"<!--asd\n"+
		"asdf\n"+
		"adf-->"+
		"<section name=\"directory\" description=\"User Directory\">"+
		"<!--asd\n"+
		"asdf\n"+
		"adf-->"+
		  "<domain name=\"$${domain}\">"+
		    "<params>"+
		      "<param name=\"dial-string\" value=\"{presence_id=${dialed_user}@${dialed_domain}}${sofia_contact(${dialed_user}@${dialed_domain})}\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"record_stereo\" value=\"true\"/>"+
		      "<variable name=\"default_gateway\" value=\"$${default_provider}\"/>"+
		      "<variable name=\"default_areacode\" value=\"$${default_areacode}\"/>"+
		      "<variable name=\"transfer_fallback_extension\" value=\"operator\"/>"+
		    "</variables>"+
		    "<groups>"+
		      "<group name=\"default\">"+
			"<users>"+
			  "<user id=\"1000\" mailbox=\"1000\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1001\" mailbox=\"1001\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1002\" mailbox=\"1002\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1003\" mailbox=\"1003\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1004\" mailbox=\"1004\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1005\" mailbox=\"1005\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1007\" mailbox=\"1007\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1008\" mailbox=\"1008\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1009\" mailbox=\"1009\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
		  "<user id=\"1010\" mailbox=\"1010\">"+
		    "<params>"+
		      "<param name=\"password\" value=\"1234\"/>"+
		      "<param name=\"vm-password\" value=\"1000\"/>"+
		    "</params>"+
		    "<variables>"+
		      "<variable name=\"toll_allow\" value=\"domestic,international,local\"/>"+
		      "<variable name=\"accountcode\" value=\"1000\"/>"+
		      "<variable name=\"user_context\" value=\"default\"/>"+
		      "<variable name=\"effective_caller_id_name\" value=\"Extension 1000\"/>"+
		      "<variable name=\"effective_caller_id_number\" value=\"1000\"/>"+
		      "<variable name=\"outbound_caller_id_name\" value=\"$${outbound_caller_name}\"/>"+
		      "<variable name=\"outbound_caller_id_number\" value=\"$${outbound_caller_id}\"/>"+
		      "<variable name=\"callgroup\" value=\"techsupport\"/>"+
		    "</variables>"+
		  "</user>"+
			"</users>"+
		      "</group>"+
		      "<group name=\"sales\">"+
			"<users>"+
			  "<user id=\"1000\" type=\"pointer\"/>"+
			  "<user id=\"1001\" type=\"pointer\"/>"+
			  "<user id=\"1002\" type=\"pointer\"/>"+
			  "<user id=\"1003\" type=\"pointer\"/>"+
			  "<user id=\"1004\" type=\"pointer\"/>"+
			"</users>"+
		      "</group>"+
		      "<group name=\"billing\">"+
			"<users>"+
			  "<user id=\"1005\" type=\"pointer\"/>"+
			  "<user id=\"1006\" type=\"pointer\"/>"+
			  "<user id=\"1007\" type=\"pointer\"/>"+
			  "<user id=\"1008\" type=\"pointer\"/>"+
			  "<user id=\"1009\" type=\"pointer\"/>"+
			"</users>"+
		      "</group>"+
		      "<group name=\"support\">"+
			"<users>"+
			  "<user id=\"1010\" type=\"pointer\"/>"+
			  "<user id=\"1011\" type=\"pointer\"/>"+
			  "<user id=\"1012\" type=\"pointer\"/>"+
			  "<user id=\"1013\" type=\"pointer\"/>"+
			  "<user id=\"1014\" type=\"pointer\"/>"+
			"</users>"+
		      "</group>"+
		    "</groups>"+
		  "</domain>"+
		  "</section>";
		try {
			packet.setBody(body);
		} catch (XMLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			packet = null;
		}
		return packet;
	}
	private SwitchUsersGroup[] toGroupsArray(SwitchUsersManager man){
		if(man == null){
			return new SwitchUsersGroup[0];
		}
		return man.toGroupsArray();
	}
	
	/*private void getTreeFromModel() {
		SwitchUsersGroup[] groups= this.toGroupsArray(this.localUserManager);
		for(int i = 0;i<groups.length;i++){
			
			SwitchUsersGroup group = groups[i];
			if(groups == null || group.size() <= 0){
				continue;
			}
			SwitchUsersTreeGroup treeGroup = 
				new SwitchUsersTreeGroup(this.treeRoot,group.getName());
			Object[] users = group.getSwitchUsersArray();
			for(int j = 0 ;j < users.length; j++){
				SwitchUser user = (SwitchUser)(users[j]);
				if(user == null){
					continue;
				}
				SwitchUsersTreeEntry treeEntry = 
					new SwitchUsersTreeEntry(treeGroup,user.getName(),Presence.ON_LINE);
			}
		}
	}*/

	private void initModel(){

		
		for(int i = 1011;i<=1101; i++){
			SwitchUser user = this.localUserManager.addSwitchUser(Integer.toString(i));
			user.setPresence(Presence.ON_LINE);
		}
		SwitchUsersGroup group = this.localUserManager.addSwitchUsersGroup("技术");
		for(int i = 1011;i<=1021; i++){
			SwitchUser user = this.localUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
		group = this.localUserManager.addSwitchUsersGroup("业务");
		for(int i = 1022;i<=1041; i++){
			SwitchUser user = this.localUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
		group = this.localUserManager.addSwitchUsersGroup("市场");
		for(int i = 1042;i<=1061; i++){
			SwitchUser user = this.localUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
		
		
		for(int i = 2011;i<=2101; i++){
			SwitchUser user = this.gatewayUserManager.addSwitchUser(Integer.toString(i));
			user.setPresence(Presence.ON_LINE);
		}
	   group = this.gatewayUserManager.addSwitchUsersGroup("1矿");
		for(int i = 2011;i<=2021; i++){
			SwitchUser user = this.gatewayUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
		group = this.gatewayUserManager.addSwitchUsersGroup("2矿");
		for(int i = 2022;i<=2041; i++){
			SwitchUser user = this.gatewayUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
		group = this.gatewayUserManager.addSwitchUsersGroup("矿务局");
		for(int i = 2042;i<=2061; i++){
			SwitchUser user = this.gatewayUserManager.getSwitchUser(Integer.toString(i));
			if(user != null){
				group.addSwitchUser(user);
			}
		}
	}
	public SwitchDispatchCtrl[] getControls(){
		
		return new SwitchDispatchCtrl[]{
				this.ctrl_call,
				this.ctrl_wiretapping,
				this.ctrl_insert,
				this.ctrl_replace,
				this.ctrl_answer,
				this.ctrl_direct,
				this.ctrl_grab,
				
				this.ctrl_group_call,
				this.ctrl_unbridge,
				this.ctrl_just_hear,
				this.ctrl_hear_and_speak,
				this.ctrl_add_member,
				this.ctrl_kick_member,
				this.ctrl_moderator,
				this.ctrl_no_moderator,
				this.ctrl_hold,
				this.ctrl_forward,
				this.ctrl_cancel
		};
	}
	public synchronized void message(String from,String to,String subject,String body,String type){
		
		Packet packet = new Packet(Packet.TYPE_COMMAND_MESSAGE);
		packet.setProperty("message-from", from.replace('\n', ' ').replace('\r', ' '));
		packet.setProperty("message-to", to.replace('\n', ' ').replace('\r', ' '));
		packet.setProperty("message-subject", subject.replace('\n', ' ').replace('\r', ' '));
		//packet.setProperty("message-body", body);
		packet.setProperty("message-type", type.replace('\n', ' ').replace('\r', ' '));
		try {
			packet.setBody(body);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	public synchronized void call(String number){
		//this.ctrl_call.setSelect(true);
		this.ctrl("call",number);
	}
	public synchronized void groupCall(String name){
		//this.ctrl_group_call.setSelect(true);
		this.ctrl("group_call",name);
	}
	public synchronized void ctrl(String cmd,String data){
		if(this.dispatcher == null || this.dispatcher.length() ==0 || 
				this.dispatcherOnline == false ||
				cmd == null || cmd.length() == 0 ||
				data == null || data.length() == 0){
			return;
		}
		Packet packet = new Packet(Packet.TYPE_COMMAND_DISPAT);
		packet.setProperty("dispatch-type", cmd);
		packet.setProperty("dispatcher", this.dispatcher);
		packet.setProperty("destination", data);
		
		try {
			this.connection.sendPacket(packet);
		} catch (CCKPConnectionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	public  synchronized void input(int type,Object data){
		if(data == null){
			return;
		}
		switch(this.inputState){
		case INPUT_STATE_ZERO:
			switch(type){
			case INPUT_TYPE_DIAL:
				if(data instanceof String){
					this.call((String)data);
				}
				break;
			case INPUT_TYPE_CTRL:
				if(data instanceof SwitchDispatchCtrl){
					SwitchDispatchCtrl ctrl= (SwitchDispatchCtrl)data;
					if(this.ctrl_hold == ctrl ||
							this.ctrl_cancel == ctrl){
						
							ctrl.setSelect(true);
							this.ctrl(ctrl.getName(),this.dispatcher);
							ctrl.setSelect(false);
						
					}
					else{
					
						ctrl.setSelect(true);
						this.currentCtrl= ctrl;
						this.inputState = INPUT_STATE_CTRL;
					}
					
				}
				break;
			case INPUT_TYPE_USER:
				if(data instanceof SwitchUser){
					SwitchUser user = (SwitchUser)data;
					if(user.getPresence() == Presence.OFF_LINE){
						break;
					} else if(user.getPresence() == Presence.ON_LINE){
						this.call(user.getUserId());
					} else {
						this.ctrl(this.ctrl_insert.getName(),user.getUserId());
					}
					
				}
				break;
			case INPUT_TYPE_GROUP:
				if(data instanceof SwitchUsersGroup){
					SwitchUsersGroup group = (SwitchUsersGroup)data;
					if(group.getParent() == this.localUserManager){
						this.groupCall(group.getGroupCallName());
					}
					
				}
				break;
			default://do nothing;
				break;
			}
			break;
		case INPUT_STATE_CTRL:
			switch(type){
			case INPUT_TYPE_DIAL:
				if(data instanceof String){
					if(this.currentCtrl != null && this.currentCtrl != this.ctrl_group_call ){
						this.ctrl(this.currentCtrl.getName(),(String)data);
					}else{
						this.call((String)data);
					}
					
				}
				break;
			case INPUT_TYPE_CTRL:
				if(data instanceof SwitchDispatchCtrl){
					SwitchDispatchCtrl ctrl= (SwitchDispatchCtrl)data;
					
					if(this.ctrl_hold == ctrl ||
							this.ctrl_cancel ==  ctrl){
						ctrl.setSelect(true);
						this.ctrl(ctrl.getName(),this.dispatcher);
						this.currentCtrl = null;
						this.inputState = INPUT_STATE_ZERO;
						ctrl.setSelect(false);
					}
					else{
					if(this.currentCtrl == ctrl){
						ctrl.setSelect(false);
						this.currentCtrl= null;
						this.inputState = INPUT_STATE_ZERO;
					}
					else{
							this.currentCtrl = ctrl;
						}
						
					}
					
					
				}
				break;
			case INPUT_TYPE_USER:
				if(data instanceof SwitchUser){
					SwitchUser user = (SwitchUser)data;
					if(this.currentCtrl == this.ctrl_group_call || 
							user.getPresence() == Presence.OFF_LINE){
						break;
					}else{
						this.ctrl(this.currentCtrl.getName(),user.getUserId());
					}
					
				}
				break;
			case INPUT_TYPE_GROUP:
				if(data instanceof SwitchUsersGroup){
					SwitchUsersGroup group = (SwitchUsersGroup)data;
					if(this.currentCtrl == this.ctrl_group_call){
						this.ctrl(this.currentCtrl.getName(),group.getGroupCallName());
					}
				}
				break;
			default://do nothing;
				break;
			}
			break;
		default:  //do nothing
			break;
		}
	}
	
	public void setConnectionDetails(ConnectionConfiguration detail){
		this.connectionDetail = detail;
	}
	public ConnectionConfiguration getConnectionDetail(){
		if(this.connectionDetail == null){
			this.connectionDetail = new ConnectionConfiguration();
		}
		return this.connectionDetail;
	}
	public CCKPConnection getConnection(){
		return this.connection;
	}
	public void connectAndLogin(final IProgressMonitor monitor) throws CCKPConnectionException{
		ConnectionListener connectionListener = new ConnectionListener(){
		//connectionListener = new ConnectionListener(){
			 public void connectionClosed(){
				 
			 }
			 
			 public void connectionClosedOnError(Exception e){
				 
			 }
			 public void socketConnectSuccessful(){
				 monitor.subTask("建立网络Socket");
			 }
			 public void authenticationSuccessful(){
				 monitor.subTask("身份验证成功");
			 }
		};
		ReplyListener monitorReplyListener = new ReplyListener(){
			public void processReply(Packet command,Packet reply){
				switch(reply.getPacketType()){
				case Packet.TYPE_REPLY_QUERY_ALL_USERS:
					monitor.subTask("获取用户信息");
					if(querySemaphore != null){
						querySemaphore.release();
					}
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_REG_USERS:
					monitor.subTask("获取注册信息");
					if(querySemaphore != null){
						querySemaphore.release();
					}
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_DISPATCHERS:
					monitor.subTask("获取调度电话状态");
					if(querySemaphore != null){
						querySemaphore.release();
					}
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_GATEWAYS:
					monitor.subTask("获取网关信息");
					if(querySemaphore != null){
						querySemaphore.release();
					}
					break;
				case Packet.TYPE_REPLY_QUERY_ALL_CURRENT_CALLS:
					monitor.subTask("获取当前通话状态");
					if(querySemaphore != null){
						querySemaphore.release();
					}
					break;
				default:
					monitor.subTask("获取其他信息");
					break;
				}
			}
		};
		try{
			monitor.subTask("连接中...");
			/*try
			{
				WritableWorkbook book = Workbook.createWorkbook(new File("f:/cdr.xls"));
				WritableSheet sheet = book.createSheet("sheet1", 0);
				
				Class.forName("org.postgresql.Driver").newInstance();
				String url = "jdbc:postgresql://"+this.connectionDetail.getServerHost()+":5432/switchdb";
				System.out.println(url);
				Connection con = DriverManager.getConnection(url, "postgres", "postgres");
				Statement st = con.createStatement();
				String sql = "select * from cdr";
				ResultSet rs = st.executeQuery(sql);
				
				int i = 0;
				while(rs.next()){
					jxl.write.Label num = new jxl.write.Label(0,i,rs.getString("caller_id_number"));
					jxl.write.Label stamp = new jxl.write.Label(1,i,rs.getString("start_stamp"));
					sheet.addCell(num);
					sheet.addCell(stamp);
					i++;
				}
				rs.close();
				st.close();
				con.close();
				
				book.write();
				book.close();
			}
			catch(Exception ee){
					System.out.println(ee.getMessage());
			}*/
			
			
			this.connection = new CCKPConnection(this.getConnectionDetail().getServerHost(),
					this.getConnectionDetail().getServerTcpPort());
			
			this.connection.addConnectionListener(connectionListener);
			
			this.connection.login(this.getConnectionDetail().getPhoneId(), 
					this.getConnectionDetail().getPwd());
			
			this.querySemaphore = new Semaphore(0);
			
			this.connection.addReplyListeners(monitorReplyListener, null);
			this.addListener();
			
			this.query();
			try {
				int waitTime = CCKPConfiguration.getPacketReplyTimeout();
				this.querySemaphore.tryAcquire(5,8*waitTime, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				//ignore;
			}
			
		}
		catch(CCKPConnectionException e){
			monitor.subTask(e.getMessage());
			throw e;
		}
		finally{
			if(this.connection != null){
				this.connection.removeConnectionListener(connectionListener);
				this.connection.removeReplyListeners(monitorReplyListener);
				this.querySemaphore = null;
			}
			monitor.done();
		}
	}
	public Connection getDBConnection(){
		if(this.dbconnection == null){
			this.dburl = "jdbc:postgresql://"+this.connectionDetail.getServerHost()+":5432/switchdb";
			try {
				Class.forName("org.postgresql.Driver").newInstance();
				this.dbconnection = DriverManager.getConnection(this.dburl, "postgres", "postgres");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		return this.dbconnection;
	}
	
	public Connection dbConnection(){
		return this.dbconnection;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isDispatcherOnline(){
		return this.dispatcherOnline;
	}
	public boolean canDail(){
		/*boolean ret = false;
		if(this.dispatcherOnline != false){
			if(this.ctrl_call.isEnable() || 
					this.ctrl_forward.isEnable()){
				ret = true;
			}
		}
		return ret;*/
		return isDispatcherOnline();
	}
	
}
