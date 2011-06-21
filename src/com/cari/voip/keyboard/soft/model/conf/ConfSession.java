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
		
		SwitchsGroup switchs = new SwitchsGroup(root,"��������������");
		SwitchsEntry mainSwitch = new SwitchsEntry(switchs,
				"��������","http://192.168.1.11");
		SwitchsEntry slaveSwitchA = new SwitchsEntry(switchs,
				"���ݷ�����1","http://192.168.1.11");
		slaveSwitchA.setPresence(Presence.OFF_LINE);
		SwitchsEntry slaveSwitchB = new SwitchsEntry(switchs,
				"���ݷ�����2","http://192.168.1.11");
		slaveSwitchB.setPresence(Presence.OFF_LINE);
		
		IADsGroup IADs = new IADsGroup(root,"IAD����");
		IADsEntry IAD1 = new IADsEntry(IADs,"MG6008_1","http://192.168.1.40");
		IADsEntry IAD2 = new IADsEntry(IADs,"MG6008_2","http://192.168.1.40");
		IADsEntry IAD3 = new IADsEntry(IADs,"Ѷʱmx8s","http://192.168.1.40");
		
		APsGroup APs = new APsGroup(root,"AP����");
		APsEntry AP1 = new APsEntry(APs,"UBNT","http://192.168.1.40");
		APsEntry AP2 = new APsEntry(APs,"MOTOROLA","http://192.168.1.40");
		
	}
}
