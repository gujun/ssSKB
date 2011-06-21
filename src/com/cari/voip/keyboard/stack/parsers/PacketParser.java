package com.cari.voip.keyboard.stack.parsers;

import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;

import com.cari.voip.keyboard.stack.events.Packet;

public class PacketParser implements IPacketParser {

	private int packetType = Packet.TYPE_INVALIDE;
    private Reader reader = null;
	
	private static boolean prevEndR = false;
	private static boolean prevEndN = false;
	private static int invalidLine = 0;
	
	private String firstLine = null;
	
	private char []cbuf = new char[1];
	
	public char []bodyBuf = new char[128];
	
	
	@Override
	public synchronized Packet getNextPacket() throws PacketParseException{
		Packet packet = null;
		int type = Packet.TYPE_INVALIDE;
		int invalidPeek = 3;
		String line = null;
		StringBuilder invalidLineBuilder = null;
		line = this.readLine();
		if(line == null){
			throw new PacketParseException("连接中断");
		}
		while(invalidPeek > 0){
			//System.out.print(line+"\n");
			type = Packet.getPacketTypeFromLine(line);
			if(type != Packet.TYPE_INVALIDE){
				packet = new Packet(type);
				break;
			}
			invalidLine++;
			invalidPeek--;
			if(invalidLineBuilder == null){
				invalidLineBuilder = new StringBuilder();
			}
			invalidLineBuilder.append(line);
			line = this.readLine();

		}
		
		if(invalidPeek < 1 ){
			//System.out.print("包头失配：\n");
			throw new PacketParseException("包头失配："+
					((invalidLineBuilder==null)?"":invalidLineBuilder.toString()));
		}
		if(packet != null){
			try{
				packet.setPacketFromParser(this);
			}
			catch(PacketParseException e){
				packet = null;
				throw e;
			}
		}
		
		return packet;
	}



	public synchronized char readChar(){
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
	public synchronized String readLine() throws PacketParseException {
		

		
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
		else {
			throw new PacketParseException("错误：包解析中reader为空");
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
	public synchronized void setInput(Reader in) {
		reset();
		this.reader = in;
	}

	protected void reset() {
		packetType = Packet.TYPE_INVALIDE;
	}

	public Reader getReader(){
		return this.reader;
	}
}
