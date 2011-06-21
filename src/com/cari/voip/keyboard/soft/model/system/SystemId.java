package com.cari.voip.keyboard.soft.model.system;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import org.sf.feeling.swt.win32.extension.system.SystemInfo;

public class SystemId {
	private static SystemInfo SystemInfoInstance = SystemInfo.getInstance();

	public static String getCPUID(){

		return SystemInfo.getCPUID();
		
	}
	public static int getNumberOfProcessors(){
		
		return SystemInfoInstance.getNumberOfProcessors();
	}
	public static String[] getMACAddressesJava(){
		List<String> ret = new LinkedList<String>();
		char hexDigits[] = {       // 用来将字节转换成 16 进制表示的字符
			     '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',  'E', 'F'};
			   
		 try {
				Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
				while (el.hasMoreElements()) {
			        byte[] mac = el.nextElement().getHardwareAddress();
			        if (mac == null)
			          continue;

			         StringBuilder builder = new StringBuilder();
			        for (byte b : mac) {
			          
			           builder.append(hexDigits[(b >>> 4) & 0xf]);
			           builder.append(hexDigits[b & 0xf]);
			           builder.append("-");
			         }
			         builder.deleteCharAt(builder.length() - 1);
			       
			         ret.add(builder.toString());
			       }
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			 
			 return ret.toArray(new String[]{});
	}
	public static String[] getMACAddresses(){
		String[] fromExtension = SystemInfo.getMACAddresses();
		
		
		int n = -1;
		if(fromExtension != null){
			n = fromExtension.length;
		}
		if(n > 0){
			return fromExtension;
		}
		
		return new String[]{};
		
	}
}
