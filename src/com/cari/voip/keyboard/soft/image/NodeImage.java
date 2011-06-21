package com.cari.voip.keyboard.soft.image;

import org.eclipse.swt.graphics.Image;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchDispatchCtrl;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;

public class NodeImage {

	public static Image Aboutus = 
		Activator.getImageDescriptor("icons/Telephone_n.png").createImage();
		
	public static Image SwitchUserOnLine = 
		Activator.getImageDescriptor("icons/Telephone_h.gif").createImage();
	public static Image SwitchUserOffLine =
		Activator.getImageDescriptor("icons/Telephone_d.gif").createImage();
	
	public static Image SwitchUserCallSpeak =
		Activator.getImageDescriptor("icons/Telephone_sp.gif").createImage();
	public static Image SwitchUserCallMute =
		Activator.getImageDescriptor("icons/Telephone_t2.gif").createImage();
	public static Image SwitchUserRing = 
		Activator.getImageDescriptor("icons/ring3.gif").createImage();
	public static Image SwitchUserRingback = 
		Activator.getImageDescriptor("icons/ringback2.gif").createImage();
	public static Image SwitchUserAnswer = 
		Activator.getImageDescriptor("icons/answer2.gif").createImage();
	
	
	public static Image ControlBridge = 
		Activator.getImageDescriptor("icons/telephone16.png").createImage();
	
	
	public static Image ControlWiretapping = 
		Activator.getImageDescriptor("icons/telephone_key.png").createImage();
	
	public static Image ControlInsert = 
		Activator.getImageDescriptor("icons/telephone_go.png").createImage();
	
	
	public static Image ControlGrab = 
		Activator.getImageDescriptor("icons/telephone_error.png").createImage();
	
	public static Image ControlDirect = 
		Activator.getImageDescriptor("icons/telephone_link.png").createImage();
	
	
	public static Image ControlAnswer = 
		Activator.getImageDescriptor("icons/telephone16.png").createImage();
	
	public static Image ControlReplace = 
		Activator.getImageDescriptor("icons/telephone16.png").createImage();
	
	
	
	public static Image ControlUnbridge = 
		Activator.getImageDescriptor("icons/delete_obj.gif").createImage();
		
	public static Image ControlConference = 
		Activator.getImageDescriptor("icons/plugin.png").createImage();
	
	
	public static Image ControlJustHear = 
		Activator.getImageDescriptor("icons/sound_none.png").createImage();
		
	public static Image ControlHearAndSpeak = 
		Activator.getImageDescriptor("icons/sound.png").createImage();
	
	public static Image ControlModerator = 
		Activator.getImageDescriptor("icons/status_online.png").createImage();
	public static Image ControlNoModerator = 
		Activator.getImageDescriptor("icons/status_offline.png").createImage();
	
	public static Image ControlAddMember = 
		Activator.getImageDescriptor("icons/telephone_add.png").createImage();
	
	public static Image ControlKickMember = 
		Activator.getImageDescriptor("icons/telephone_delete.png").createImage();
	
	public static Image ControlForward = 
		Activator.getImageDescriptor("icons/e_forward.gif").createImage();
	
	public static Image ControlCancel = 
		Activator.getImageDescriptor("icons/e_back.gif").createImage();
	
	public static Image ControlHold = 
		Activator.getImageDescriptor("icons/telephone16.png").createImage();
	
	
	public static Image groupImage = 
		Activator.getImageDescriptor("icons/userG.gif").createImage();
	
	public static Image LoadingImage = 
		Activator.getImageDescriptor("icons/Loading.gif").createImage();
	
	
	public static Image getImageFromSwitchUserPresence(SwitchUser user){
		if(user == null){
			return SwitchUserOffLine;
		}
		Presence presence = user.getPresence();
		if(presence != null){
			if(presence == Presence.OFF_LINE){
				return SwitchUserOffLine;
			}
			else if(presence== Presence.ON_LINE){
				return SwitchUserOnLine;
			}
			else if(presence == Presence.CALL_SPEAK){
				return SwitchUserCallSpeak;
			}
			else if(presence == Presence.CALL_MUTE){
				return SwitchUserCallMute;
			}
			else if(presence == Presence.CALL_RING){
				return SwitchUserRing;
			}
			else if(presence == Presence.CALL_RING_BACK){
				return SwitchUserRingback;
			}
			else if(presence == Presence.CALL_ANSWER){
				return SwitchUserAnswer;
			}
			else if(presence == Presence.CALL_HANGUP){
				return SwitchUserOnLine;
			}
		}
		return SwitchUserOffLine;
	}
	public static Image getImageFromSwitchCtrlType(int type){
		
		switch(type){
		case SwitchDispatchCtrl.CTRL_TYPE_CALL:
			return ControlBridge;
		case SwitchDispatchCtrl.CTRL_TYPE_WIRE_TAPPING:
			return ControlWiretapping;
		case SwitchDispatchCtrl.CTRL_TYPE_INSERT:
			return ControlInsert;
		case SwitchDispatchCtrl.CTRL_TYPE_GRAB:
			return ControlGrab;
		case SwitchDispatchCtrl.CTRL_TYPE_UNBRIDGE:
			return ControlUnbridge;
		case SwitchDispatchCtrl.CTRL_TYPE_GROUP_CALL:
			return ControlConference;
		case SwitchDispatchCtrl.CTRL_TYPE_JUST_HEAR:
			return ControlJustHear;
		case SwitchDispatchCtrl.CTRL_TYPE_HEAR_AND_SPEAK:
			return ControlHearAndSpeak;
		case SwitchDispatchCtrl.CTRL_TYPE_MODERATOR:
			return ControlModerator;
		case SwitchDispatchCtrl.CTRL_TYPE_NO_MODERATOR:
			return ControlNoModerator;
		case SwitchDispatchCtrl.CTRL_TYPE_ADD_MEMBER:
			return ControlAddMember;
		case SwitchDispatchCtrl.CTRL_TYPE_KICK_MEMBER:
			return ControlKickMember;
		case SwitchDispatchCtrl.CTRL_TYPE_ANSWER:
			return ControlAnswer;
		case SwitchDispatchCtrl.CTRL_TYPE_REPLACE:
			return ControlReplace;
		case SwitchDispatchCtrl.CTRL_TYPE_DIRECT:
			return ControlDirect;
		case SwitchDispatchCtrl.CTRL_TYPE_FORWARD:
			return ControlForward;
		case SwitchDispatchCtrl.CTRL_TYPE_CANCEL:
			return ControlCancel;
		case SwitchDispatchCtrl.CTRL_TYPE_HOLD:
			return ControlHold;
		default:
			break;
		}
		return ControlBridge;
	}
}