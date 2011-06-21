package com.cari.voip.keyboard.soft.resources;

import java.io.Reader;

public class StreamParser implements IStreamParser {
	private Reader reader = null;
	
	private static boolean prevEndR = false;
	private static boolean prevEndN = false;
	private static int invalidLine = 0;
	
	private String firstLine = null;
	
	private char []cbuf = new char[1];
	
	public char []bodyBuf = new char[128];
	
	@Override
	public synchronized char readChar() {
		char c = (char)-1;

		if(this.reader != null){
			try {
				int n = this.reader.read(cbuf);
				
				if(n > 0){
					c = cbuf[0];
				}
				else if(n == -1){
					//reach end of stream;
				}
				else {
					//error
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
		return c;
	}

	@Override
	public synchronized String readLine() {

		int endLen = 0;
		int lineLen = 0;
		boolean newLine = false;
		
		StringBuilder sb = new StringBuilder(64);
		
		if(this.reader != null){
			while(true){
				char c = this.readChar();
			
				if(c == '\n' || c == '\r'){
					endLen++;
					
					if(lineLen > 0 || 
							endLen >=2){
						newLine = true;	
					}
					else{
						if(c == '\n'){
							newLine = prevEndR?false:true;
						}
						else{
							newLine = prevEndN?false:true;
						}
					}
					if(newLine == true){
						if(c == '\n'){
							prevEndN = true;
							prevEndR = false;
						}else{
							prevEndN = false;
							prevEndR = true;
						}
						break;
					}
					
				}
				else if(c != (char)-1){
				
					lineLen++;
					sb.append(c);
				
				}
				else {
					prevEndR = false;
					prevEndN = false;
					break;
				}
			}
		}
		
		if(lineLen == 0){
			
			if(newLine == false){
				return null;
			}
			return "";
		}
		
		
		return sb.toString();
	}

	@Override
	public void setInput(Reader in) {
		this.reader = in;
	}

}
