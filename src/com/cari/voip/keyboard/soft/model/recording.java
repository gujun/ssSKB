package com.cari.voip.keyboard.soft.model;


import java.sql.Connection;
import java.sql.Statement;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class recording {
	public String caller_id_name;
	public String caller_id_number;
	public String uuid;
	public String bound;
	public String call_name;
	public String start_stamp;
	public String end_stamp;
	
	public String file_path;
	public int file_len;
	public int flags;
	public int read_flags;
	
	private String urlStart = null;
	public recording(){}
	
	public boolean setDeletedFalg(SwitchUsersSession session) throws Exception{
		boolean ret = true;
		
		String sql = "update recordings set flags=flags+1 where caller_id_number='"+
			this.caller_id_number+
			"' and start_stamp='"+
			this.start_stamp+
			"' and uuid='"+
			this.uuid+
			"'";
	
		Connection dbcon = session.getDBConnection();
		Statement st = dbcon.createStatement();
		st.execute(sql);
		st.close();
		
		return ret;
	}
	
	protected String getUrlStart(SwitchUsersSession session){
		if(this.urlStart == null){
			try{
				String host = session.getConnectionDetail().getServerHost(); 
				if(host != null && host.length() > 0){
					this.urlStart ="http://"+host+"/switch/recording/";
					//browser.setUrl(startUrl);
				}
			}catch(Exception e){
				
			}
			
		}
		
		return this.urlStart;
	}
	public String getTryUrl(SwitchUsersSession session) throws Exception{
		String url = "";
		String Start = this.getUrlStart(session);
		if(Start != null){
			url = url.concat(Start);
		}
		if(this.file_path == null || this.file_path.length() == 0){
			throw new Exception("文件名错误！");
		}
		/*html = "<html><head><title>rec</title></head>"+
		"<body onload=\"window.open('"+
		startUrl+
		"hear.php?file="+
		java.net.URLEncoder.encode(rec.file_path,"utf-8")+
		"','hearRecording','fullscreen=no,titlebar=no,menubar=no,toolbar=no,location=no,scrollbars=yes,resizable=yes,status=no,width=240,height=66,directories=no,screenX=400,screenY=200');\">"+
		"</body></html>";
		System.out.println(html);
		this.browser.setText(html);*/
		url = url.concat("try.php?file="+java.net.URLEncoder.encode(this.file_path,"utf-8"));
		
		return url;
	}
	
	public String getDeleteUrl(SwitchUsersSession session) throws Exception{
		String url = "";
		String Start = this.getUrlStart(session);
		if(Start != null){
			url = url.concat(Start);
		}
		if(this.file_path == null || this.file_path.length() == 0){
			throw new Exception("文件名错误！");
		}
		url = url+"trash.php?trash="+java.net.URLEncoder.encode(this.file_path,"utf-8");
		
		
		return url;
	}
	
	public String getDownloadUrl(SwitchUsersSession session) throws Exception{
		String url = "";
		String Start = this.getUrlStart(session);
		if(Start != null){
			url = url.concat(Start);
		}
		if(this.file_path == null || this.file_path.length() == 0){
			throw new Exception("文件名错误！");
		}
		url = url+"down.php?file="+	java.net.URLEncoder.encode(this.file_path,"utf-8");
		
		return url;
	}
}
