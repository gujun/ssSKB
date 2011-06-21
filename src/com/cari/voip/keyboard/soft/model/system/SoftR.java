package com.cari.voip.keyboard.soft.model.system;

import com.cari.voip.keyboard.soft.resources.LoginInfo;

public class SoftR {
	public static final String REG_FILE = "configuration/reginfo.ini";
	private  LoginInfo regFile;
	private final String reg_key = "";
	private String machineString;
	public SoftR(){
		
		machineString = calMachineString();
		regFile = new LoginInfo(REG_FILE);
	}
	
	protected  byte[] getMD5(byte[] source){
		String s = null;
		  try
		   {
		    java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
		    md.update( source );
		    byte dg[] = md.digest();          // MD5 �ļ�������һ�� 128 λ�ĳ�������
/*		                                                // ���ֽڱ�ʾ���� 16 ���ֽ�
		    char str[] = new char[16 * 2];   // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
		                                                 // ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
		    int k = 0;                                // ��ʾת������ж�Ӧ���ַ�λ��
		    for (int i = 0; i < 16; i++) {          // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
		                                                 // ת���� 16 �����ַ���ת��
		     byte byte0 = tmp[i];                 // ȡ�� i ���ֽ�
		     str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // ȡ�ֽ��и� 4 λ������ת��, 
		                                                             // >>> Ϊ�߼����ƣ�������λһ������
		     str[k++] = hexDigits[byte0 & 0xf];            // ȡ�ֽ��е� 4 λ������ת��
		    }
		    s = new String(str);                                 // ����Ľ��ת��Ϊ�ַ���
*/
		    return dg;
		   }catch( Exception e )
		   {
		    //e.printStackTrace();
		   }
		   return new byte[]{};
	}
	private String calMachineString(){
		StringBuilder builder = new StringBuilder();
		 builder.append( "c:"+SystemId.getCPUID()+"("+String.valueOf(SystemId.getNumberOfProcessors())+");");
		 String[] macArray = SystemId.getMACAddresses();
		 if(macArray != null && macArray.length > 0){
		 for(String mac:macArray){
			 builder.append("e:"+mac+";");
		 }}
		 /*String[] macArrayJava = SystemId.getMACAddressesJava();
		 for(String mac:macArrayJava){
			 builder.append("j:"+mac+";");
		 }*/
		return builder.toString();
	}
	public String getMachineString(){
		return this.machineString;
	}
	protected String getStringFromMD5Byte(byte[] md5){
		String ret = "";
		char hexDigits[] = {       // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
			     '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',  'E', 'F'};
		
		if(md5 == null || md5.length != 16){
			return ret;
		}
		try{
			StringBuilder builder = new StringBuilder();
			for(int i = 0 ;i<16;i++){
				if((i > 0) && ((i%4)==0)){
					 builder.append('-');
				 }
				 byte b = md5[i];
				 //builder.append(hexDigits[(b>>>4)&0xf]);
				 builder.append(hexDigits[b&0xf]);
				 
			}
			/*
			
			for(int i = 0 ;i<4;i++){
				int b1 = (int)(md5[i*4 + 0]) &0xFF;
				int b2 = (int)(md5[i*4 + 1]) &0xFF;
				int b3 = (int)(md5[i*4 + 2]) &0xFF;
				int b4 = (int)(md5[i*4 + 3]) &0xFF;
				
				int j = (b1<<8) +b2 + b3+ b4;
				
				builder.append(String.valueOf(j));
				
				builder.append("-");
			}
			builder.deleteCharAt(builder.length() - 1);
			*/
			
			ret = builder.toString();
		}catch( Exception e )
		   {
		    //e.printStackTrace();
		   }
		return ret;
	}
	
	public   String getMachineCode(){
		String mstr = this.getMachineString();
		
		if(mstr != null && mstr.length() > 0){
			String t = reg_key+mstr;
			//byte[] md5 = this.getMD5(mstr.getBytes());
			return this.getStringFromMD5Byte(this.getMD5(t.getBytes()));
		}
		return "";
	}
	
	public String getRegCodeFromMachineCode(String m){
		if(m != null){
			String t = reg_key+m;
			return this.getStringFromMD5Byte(this.getMD5(t.getBytes()));
		}
		return "";
	}
	
	public  boolean checkRegisterCode(String m,String r){
		if(m == null || m.length() == 0 ||
				r == null || r.length() ==0){
			return false;
			
		}
		String t = reg_key+m;
		String tt = this.getStringFromMD5Byte(this.getMD5(t.getBytes()));
		return r.equals(tt);
	}
	public String getRegCode(){
		return this.regFile.getProperty("rcode");
		
	}
	public boolean isAreadyRegOK(){
		/*String m = this.getMachineCode();
		//String m_= this.regFile.getProperty("machine");
		
		String v = this.regFile.getProperty("rcode");
		
		return checkRegisterCode(m,v);
		*/
		return false;
	}
	public boolean saveReg(String mStr,String m,String r){
		this.regFile.setProperty("machine", mStr);
		this.regFile.setProperty("mcode", m);
		this.regFile.setProperty("rcode", r);
		return this.regFile.storeProperty();
	}
}
