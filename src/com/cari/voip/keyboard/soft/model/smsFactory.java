package com.cari.voip.keyboard.soft.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class smsFactory {
	public static String getQuerryString(String start_time,String end_time,String id){
		String sql = "SELECT * FROM sms_ok";
		boolean firstWhere = true;
		
		if(start_time != null){
			String start_time_t = start_time.trim();
			if(start_time_t != null && start_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" start_stamp>='"+start_time_t+"'");
			}
		}
		
		if(end_time != null){
			String end_time_t = end_time.trim();
			if(end_time_t != null && end_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" start_stamp<='"+end_time_t+"'");
			}
		}
		
		if(id != null){
			String user = id.trim();
			if(user != null && user.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" (sms_to like '%/"+user+"' or sms_to like '"+user+"@%')");
			}
		}
		sql = sql.concat(" order by sms_to,start_stamp");
		
		return sql;
	}
	
	public static String getDeldeledString(String start_time,String end_time,String id){
		String sql = "DELETE FROM sms_ok";
		
		boolean firstWhere = true;
		
		
		if(start_time != null){
			String start_time_t = start_time.trim();
			if(start_time_t != null && start_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" start_stamp>='"+start_time_t+"'");
			}
		}
		
		if(end_time != null){
			String end_time_t = end_time.trim();
			if(end_time_t != null && end_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" start_stamp<='"+end_time_t+"'");
			}
		}
		
		if(id != null){
			String user = id.trim();
			if(user != null && user.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" WHERE");
					
				} else {
					sql = sql.concat(" AND");
				}
				sql = sql.concat(" (sms_to like '%/"+user+"' or sms_to like '"+user+"@%')");
				}
		}
		
		return sql;
	}
	public static boolean deldeled(SwitchUsersSession session,String sql)  throws Exception{
		Connection con = session.getDBConnection();
		Statement st = con.createStatement();
		boolean ret = st.execute(sql);
		st.close();
		return ret;
	}
	public static List getSMS(SwitchUsersSession session,String sql) throws Exception{
	    
		List recordings = new LinkedList();
			Connection con = session.getDBConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				sms rec = new sms();
				rec.chat = rs.getString("chat");
				rec.sms_to = rs.getString("sms_to");
				rec.proto = rs.getString("proto");
				rec.sms_from = rs.getString("sms_from");
				rec.body = rs.getString("body");
				rec.sms_type = rs.getString("sms_type");
				rec.start_stamp = rs.getString("start_stamp");
				
				recordings.add(rec);
			}
			rs.close();
			st.close();
		
		return recordings;
	}
}
