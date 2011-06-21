package com.cari.voip.keyboard.soft.model;

import java.sql.Connection;
import java.sql.Statement;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class cdr {
	
	public String caller_id_name;
	public String caller_id_number;
	public String destination_number;
	public String dialed_extension;
	public String context_extension;

	public String start_stamp;
	public String billsec;
	
	public String hangup_cause;
	
	public cdr(){}
	
	/*public String getDispatcherFailSetCheckString(){
		String sql = "UPDATE cdr SET flags=1 WHERE caller_id_number='"+
		this.caller_id_number+"' AND start_stamp='"+
		this.start_stamp+"' AND context_extension='dispatcher'";
		return sql;
	}*/

	public boolean setCheckFlag(SwitchUsersSession session)throws Exception {
		boolean ret = true;
		
		String sql = "UPDATE cdr SET flags=1 WHERE caller_id_number='"+
		this.caller_id_number+"' AND start_stamp='"+
		this.start_stamp+"' AND context_extension='dispatcher'";
	
		Connection dbcon = session.getDBConnection();
		Statement st = dbcon.createStatement();
		st.execute(sql);
		st.close();
		
		return ret;
		
	}
	public boolean callBack(SwitchUsersSession session)throws Exception{
		boolean ret = true;
		session.input(SwitchUsersSession.INPUT_TYPE_DIAL, this.caller_id_number);
		return ret;
	}
	
}
