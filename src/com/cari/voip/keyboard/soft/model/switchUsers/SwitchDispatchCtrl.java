package com.cari.voip.keyboard.soft.model.switchUsers;

import org.eclipse.swt.SWT;

import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.views.zest.ControlKeyGraphNode;

public class SwitchDispatchCtrl extends SwitchEntity {

	public  static final String PROP_CTRL_ENABLE = "SwitchDispatchCtrl.Enable";
	public static final String PROP_CTRL_SELECT = "SwitchDispatchCtrl.Select";
	private int type;
	private boolean enable;
	private boolean select;
	/*ControlKeyGraphNode bridge = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlBridge);
		
		ControlKeyGraphNode wiretapping = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlWiretapping);
		ControlKeyGraphNode insert = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlInsert);
		ControlKeyGraphNode grab = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlGrab);
		ControlKeyGraphNode unbridge = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ǿ    ��  ",NodeImage.ControlUnbridge);
		
		ControlKeyGraphNode conference = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlConference);
		

		ControlKeyGraphNode justHear = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ��  ",NodeImage.ControlJustHear);
		ControlKeyGraphNode hearAndSpeak = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��    ˵  ",NodeImage.ControlHearAndSpeak);
		
		ControlKeyGraphNode addMember = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ��ӳ�Ա  ",NodeImage.ControlAddMember);
		
		ControlKeyGraphNode kickMember = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ɾ����Ա  ",NodeImage.ControlKickMember);
		
		
		ControlKeyGraphNode moderator = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  �趨��ϯ  ",NodeImage.ControlModerator);
		ControlKeyGraphNode noModerator = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  ȡ����ϯ  ",NodeImage.ControlNoModerator);
		*/
	public static final int CTRL_TYPE_CALL = 			1;
	public static final int CTRL_TYPE_WIRE_TAPPING = 	CTRL_TYPE_CALL+1;
	public static final int CTRL_TYPE_INSERT = 			CTRL_TYPE_CALL+2;
	public static final int CTRL_TYPE_GRAB = 			CTRL_TYPE_CALL+3;
	public static final int CTRL_TYPE_UNBRIDGE = 		CTRL_TYPE_CALL+4;
	public static final int CTRL_TYPE_GROUP_CALL = 		CTRL_TYPE_CALL+5;
	public static final int CTRL_TYPE_JUST_HEAR = 		CTRL_TYPE_CALL+6;
	public static final int CTRL_TYPE_HEAR_AND_SPEAK = 	CTRL_TYPE_CALL+7;
	public static final int CTRL_TYPE_ADD_MEMBER = 		CTRL_TYPE_CALL+8;
	public static final int CTRL_TYPE_KICK_MEMBER = 	CTRL_TYPE_CALL+9;
	public static final int CTRL_TYPE_MODERATOR = 		CTRL_TYPE_CALL+10;
	public static final int CTRL_TYPE_NO_MODERATOR = 	CTRL_TYPE_CALL+11;
	
	public static final int CTRL_TYPE_ANSWER = CTRL_TYPE_CALL+12;
	public static final int CTRL_TYPE_REPLACE = CTRL_TYPE_CALL+13;
	public static final int CTRL_TYPE_DIRECT = CTRL_TYPE_CALL+14;
	public static final int CTRL_TYPE_FORWARD = CTRL_TYPE_CALL+15;
	public static final int CTRL_TYPE_CANCEL = CTRL_TYPE_CALL+16;

	public static final int CTRL_TYPE_HOLD = CTRL_TYPE_CALL+17;
	
	public static final int CTRL_TYPE_LAST = CTRL_TYPE_CALL+18;
	
	public static String CTRL_TYPE_STRING[] ={
		"",
		
		"  ��    ��  ",
		"  ��    ��  ",
		"  ǿ    ��  ",
		"  ǿ    ��  ",
		"  ǿ    ��  ",
		
		"  ��    ��  ",
		"  ��    ��  ",
		"  ��    ˵  ",
		"  ��ӳ�Ա  ",
		"  ɾ����Ա  ",
		
		"  �趨��ϯ  ",
		"  ȡ����ϯ  ",
		"  ǿ    ��  ",
		"  ��    ��  ",
		"  ǿ    ͨ  ",
		
		"  ת    ��  ",
		"  ȡ    ��  ",
		"  ��    ��  ",
		""
	};
	public static String CTRL_TYPE_NAME[] ={
		"",
		
		"call",
		"hear",
		"insert",
		"grab",
		"unbridge",
		
		"group_call",
		"mute",
		"unmute",
		"add",
		"kick",
		
		"moderator",
		"unmoderator",
		"answer",
		"replace",
		"direct",
		
		"forward",
		"cancel",
		"hold",
		""
	};
	public SwitchDispatchCtrl(int type){
		this.type = 0;
		if(type>= CTRL_TYPE_CALL && type < CTRL_TYPE_LAST){
			this.type= type;
		}
		this.enable = false;
		this.select = false;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return CTRL_TYPE_NAME[this.type];
	}
	public String getLable(){
		return CTRL_TYPE_STRING[this.type];
	}
	public int getCtrlType(){
		return this.type;
	}
	public void setEnable(boolean value){
		this.enable = value;
		this.firePropertyChange(PROP_CTRL_ENABLE, null, new Boolean(value));
	}
	public boolean isEnable(){
		return this.enable;
	}
	
	public void setSelect(boolean value){
		this.select = value;
		this.firePropertyChange(PROP_CTRL_SELECT, null, new Boolean(value));
	}
	
}
