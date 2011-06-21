package com.cari.voip.keyboard.soft.model.conf;

import com.cari.voip.keyboard.soft.model.Presence;

public class ConfSession {

	private ContactsGroup root;
	
	public ConfSession(){
		this.root = new ContactsGroup(null,"root");
	}
	public ContactsGroup getRoot(){
		return this.root;
	}
	public boolean init(){
		 this.addTestModel();
		 return true;
	}
	public void addTestModel(){
		ContactsGroup root = this.getRoot();
		
		SwitchsGroup switchs = new SwitchsGroup(root,"软交换服务器集合");
		SwitchsEntry mainSwitch = new SwitchsEntry(switchs,
				"主服务器","http://192.168.1.11");
		SwitchsEntry slaveSwitchA = new SwitchsEntry(switchs,
				"备份服务器1","http://192.168.1.11");
		slaveSwitchA.setPresence(Presence.OFF_LINE);
		SwitchsEntry slaveSwitchB = new SwitchsEntry(switchs,
				"备份服务器2","http://192.168.1.11");
		slaveSwitchB.setPresence(Presence.OFF_LINE);
		
		IADsGroup IADs = new IADsGroup(root,"IAD集合");
		IADsEntry IAD1 = new IADsEntry(IADs,"MG6008_1","http://192.168.1.40");
		IADsEntry IAD2 = new IADsEntry(IADs,"MG6008_2","http://192.168.1.40");
		IADsEntry IAD3 = new IADsEntry(IADs,"讯时mx8s","http://192.168.1.40");
		
		APsGroup APs = new APsGroup(root,"AP集合");
		APsEntry AP1 = new APsEntry(APs,"UBNT","http://192.168.1.40");
		APsEntry AP2 = new APsEntry(APs,"MOTOROLA","http://192.168.1.40");
		
	}
}
