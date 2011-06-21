package com.cari.voip.keyboard.soft.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class recordingsFactory {
	public static String getQuerryString(String start_time,String end_time,String id){
		String sql = "select * from recordings";
		boolean firstWhere = true;
		
		if(start_time != null){
			String start_time_t = start_time.trim();
			if(start_time_t != null && start_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" Where");
					
				} else {
					sql = sql.concat(" and");
				}
				sql = sql.concat(" start_stamp>='"+start_time_t+"'");
			}
		}
		
		if(end_time != null){
			String end_time_t = end_time.trim();
			if(end_time_t != null && end_time_t.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" Where");
					
				} else {
					sql = sql.concat(" and");
				}
				sql = sql.concat(" start_stamp<='"+end_time_t+"'");
			}
		}
		
		if(id != null){
			String user = id.trim();
			if(user != null && user.length() > 0){
				if(firstWhere){
					firstWhere = false;
					sql = sql.concat(" Where");
					
				} else {
					sql = sql.concat(" and");
				}
				sql = sql.concat(" caller_id_number='"+user+"'");
			}
		}
		sql = sql.concat(" order by caller_id_number,start_stamp");
		
		return sql;
	}
	
	public static String getDeldeledString(String start_time,String end_time,String id){
		String sql = "delete from recordings where flags<>0";
		
		if(start_time != null){
			String start_time_t = start_time.trim();
			if(start_time_t != null && start_time_t.length() > 0){
				
				sql = sql.concat(" and start_stamp>='"+start_time_t+"'");
			}
		}
		
		if(end_time != null){
			String end_time_t = end_time.trim();
			if(end_time_t != null && end_time_t.length() > 0){
				
				sql = sql.concat(" and start_stamp<='"+end_time_t+"'");
			}
		}
		
		if(id != null){
			String user = id.trim();
			if(user != null && user.length() > 0){
				
				sql = sql.concat(" and caller_id_number='"+user+"'");
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
	public static List getRecordings(SwitchUsersSession session,String sql) throws Exception{
	    
		List recordings = new LinkedList();
			Connection con = session.getDBConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				recording rec = new recording();
				rec.caller_id_name = rs.getString("caller_id_name");
				rec.caller_id_number = rs.getString("caller_id_number");
				rec.uuid = rs.getString("uuid");
				rec.bound = rs.getString("bound");
				rec.start_stamp = rs.getString("start_stamp");
				rec.end_stamp = rs.getString("end_stamp");
				rec.file_path = rs.getString("file_path");
				rec.file_len = rs.getInt("file_len");
				rec.flags = rs.getInt("flags");
				rec.read_flags = rs.getInt("read_flags");
				
				recordings.add(rec);
			}
			rs.close();
			st.close();
		
		return recordings;
	}
}
