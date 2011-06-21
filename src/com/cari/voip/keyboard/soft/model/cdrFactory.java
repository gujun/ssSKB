package com.cari.voip.keyboard.soft.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class cdrFactory {
	public static String getQuerryString(String start_time,String end_time,String id){
		String sql = "SELECT * FROM cdr";
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
				sql = sql.concat(" caller_id_number='"+user+"'");
			}
		}
		sql = sql.concat(" order by caller_id_number,start_stamp");
		
		return sql;
	}
	
	public static String getDeldeledString(String start_time,String end_time,String id){
		String sql = "DELETE FROM cdr";
		
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
				sql = sql.concat(" caller_id_number='"+user+"'");
			}
		}
		
		return sql;
	}
	
	public static String getDispatcherFailQuerryString(){
		String sql = "SELECT * FROM cdr WHERE flags=0 AND context_extension='dispatcher' AND hangup_cause<>'SUCCESS'";
		return sql;
	}
	public static boolean checkAllDispatcherFail(SwitchUsersSession session)  throws Exception{
		String sql = "UPDATE cdr SET flags=1 WHERE flags=0 AND context_extension='dispatcher' AND hangup_cause<>'SUCCESS'";
		
		Connection con = session.getDBConnection();
		Statement st = con.createStatement();
		boolean ret = st.execute(sql);
		st.close();
		return ret;
	}
	public static boolean deldeled(SwitchUsersSession session,String sql)  throws Exception{
		Connection con = session.getDBConnection();
		Statement st = con.createStatement();
		boolean ret = st.execute(sql);
		st.close();
		return ret;
	}
	public static List getCDR(SwitchUsersSession session,String sql) throws Exception{
		
		List recordings = new LinkedList();
			Connection con = session.getDBConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				cdr rec = new cdr();
				rec.caller_id_name = rs.getString("caller_id_name");
				rec.caller_id_number = rs.getString("caller_id_number");
				rec.destination_number = rs.getString("destination_number");
				rec.dialed_extension = rs.getString("dialed_extension");
				rec.context_extension = rs.getString("context_extension");
				rec.start_stamp = rs.getString("start_stamp");
				rec.billsec = rs.getString("billsec");
				rec.hangup_cause = rs.getString("hangup_cause");
				
				recordings.add(rec);
			}
			rs.close();
			st.close();
		
		return recordings;
	}
}
