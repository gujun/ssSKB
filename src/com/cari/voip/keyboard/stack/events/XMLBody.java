package com.cari.voip.keyboard.stack.events;

import com.cari.voip.keyboard.stack.parsers.PacketParser;

public class XMLBody {

	private enum XMLState{
		start,
		document,
		cdata,
		dtd,
		instructions,
		newTag,
		name,
		attrName,
		attrValue1,
		attrValue2,
		afterNameOrAttrValue,
		closeTag,
		closeName,
		end;
		   public static XMLState fromString(String name) {
	            try {
	                return XMLState.valueOf(name);
	            }
	            catch (Exception e) {
	                return start;
	            }
	        }
	}
	
	private enum NextNodeType{
		root,
		child,
		brother;
	}
	XMLNode root;
	
	String bodyString;
	
	public XMLBody(){
		
	}
	
	
	public void parseStr(String xmlString) throws XMLParseException{
		if(xmlString == null ||
				xmlString.length() == 0){
			return;
		}
		
		int nextK = 0;
		int endOff = xmlString.length()-1;
		
		XMLState state = XMLState.start;
		NextNodeType nextNodeType = NextNodeType.root;
		int dtdl = 0;
		boolean error = false;
		char c ;
		int nameStart = -1;
		int nameEnd = -1;
		
		int attrNameStart = -1;
		int attrNameEnd = -1;
		int attrValueStart = -1;
		int attrValueEnd = -1;
		
		int txtStart = -1;
		int txtEnd = -1;
		
		int closeNameStart = -1;
		int closeNameEnd = -1;
		
		XMLNode currentNode = null;
		while(nextK <= endOff){
			switch(state){
			case start:
				txtStart = nextK;
				nextK = xmlString.indexOf('<',nextK);
				if(nextK >=0 && nextK < endOff){
					txtEnd = nextK;
					nextK++;
					c = xmlString.charAt(nextK);
					if(c == '!'){
						if(xmlString.startsWith("!--", nextK)){
							state = XMLState.document;
							nextK += 3;
						}
						else if(xmlString.startsWith("![CDATA[", nextK)){
							state = XMLState.cdata;
							nextK += 8;
						}
						else if(xmlString.startsWith("!DOCTYPE", nextK)){
							state = XMLState.dtd;
							dtdl = 0;
							nextK += 8;
						}
						else{
							nextK = xmlString.indexOf('>',nextK);
							if(nextK < 0){
								error = true;
							}
							else{
								nextK++; //still start;
							}
						}
					}
					else if(c == '?'){
						state = XMLState.instructions;
						nextK += 1;
					}
					else if(c == '/'){
						
						state = XMLState.closeTag;
						nextK += 1;
					}
					else {
						state = XMLState.newTag;
					}
				}
				else{
					state = XMLState.end;
				}
				break;
			case document:
				nextK = xmlString.indexOf('>',nextK);
				if(nextK < 0){
					error = true;
				}
				else{
					if(nextK > 2 && xmlString.startsWith("-->", nextK-2)){
						state = XMLState.start;
					}
					nextK++;//still document;
				}
				break;
			case cdata:
				nextK = xmlString.indexOf('>',nextK);
				if(nextK < 0){
					error = true;
				}
				else{
					if(nextK > 2 && xmlString.startsWith("]]>", nextK-2)){
						state = XMLState.start;
					}
					nextK++;//still cdata;
				}
				break;
			case dtd:
				if(dtdl == 1){
					nextK = xmlString.indexOf('>',nextK);
					if(nextK > 0){
						int i = nextK-1;
						while(i > 0){
							c = xmlString.charAt(i);
							if(c == '\t' || c == '\r' || c=='\n'){
								i--;
							}
							else{
								break;
							}
							
						}
						if(i > 0 && xmlString.charAt(i) == ']'){
							state = XMLState.start;
						}
					}
					else{
						error = true;
					}
				}
				else{
					c = xmlString.charAt(nextK);
					if(c == '['){
						dtdl = 1;
					}
					else if(c == '>'){
							state = XMLState.start;
					}
					
				}
				
				nextK++;
				break;
			case instructions:
				nextK = xmlString.indexOf('>',nextK);
				if(nextK < 0){
					error = true;
				}
				else{
					if(nextK > 1 && xmlString.startsWith("?>", nextK-1)){
						state = XMLState.start;
					}
					nextK++;//still instructions;
				}
				break;
			case newTag:
				c = xmlString.charAt(nextK);
				if(Character.isLetter(c)){
					nameStart = nextK;
					state = XMLState.name;
				}
				nextK++;
				break;
			case name://<xxx
				c = xmlString.charAt(nextK);
				if(c == ' ' || c == '>'){
					if(c == '>' && (xmlString.charAt(nextK-1) == '/')){
							nextK--;
					}
					state = XMLState.afterNameOrAttrValue;
					nameEnd = nextK;
					
					String name = xmlString.substring(nameStart, nameEnd).trim().toLowerCase();
					if(nextNodeType == NextNodeType.root){
						this.root = new XMLNode(null,name);
						currentNode = this.root;
					}
					else if(nextNodeType == NextNodeType.child){
						if(currentNode != null){
							XMLNode newNode = new XMLNode(currentNode,name);
							currentNode = newNode;
						}
						else{
							error = true;
						}
					}
					else if(nextNodeType == NextNodeType.brother){
						XMLNode parent = null;
						if(currentNode != null &&
								(parent = currentNode.getParent()) != null){
							XMLNode newNode = new XMLNode(parent,name);
							currentNode = newNode;
						}
						else{
							error = true;
						}
					}
					else{
						error = true;
					}
					
				}
				else{
					nextK++;
				}
				
				break;
			case attrName://<xxx xxx
				c = xmlString.charAt(nextK);
				
				if(c == '='){
					attrNameEnd = nextK;
					
					nextK++;
					while(nextK <= endOff && (c = xmlString.charAt(nextK))==' '){
						nextK++;
					}
					if(nextK >= endOff){
						error = true;
					}
					else{
						c = xmlString.charAt(nextK);
						if(c == '"'){
							state = XMLState.attrValue1;
							nextK++;
							
						}
						else{
							state = XMLState.attrValue2;
						}
						attrValueStart = nextK;
						//nextK++;
					}
					
					
				}
				else{
					nextK++;
				}
				
				
				break;
			case attrValue1://<xxx xxx="
				c = xmlString.charAt(nextK);
				if(c == '"'){
					attrValueEnd = nextK;
					
					String attrName = xmlString.substring(attrNameStart,attrNameEnd);
					String attrValue = xmlString.substring(attrValueStart, attrValueEnd);
					if(attrName != null){
						attrName = attrName.trim();
					}
					if(attrValue != null){
						attrValue = attrValue.trim();
					}
					
					if(attrName != null && attrName.length() > 0){
						
						if(currentNode != null){
							if(attrValue == null){
								attrValue = "";
							}
							
							currentNode.setAttr(attrName.toLowerCase(), attrValue);
							//System.out.println(attrName+"="+attrValue);
						}
						else{
							error = true;
						}
					}
					
					state= XMLState.afterNameOrAttrValue;
					
				}

				nextK++;
				
				break;
			case attrValue2://<xxx
				c = xmlString.charAt(nextK);
				
				if(c == ' ' || c == '>'){
					if(c == '>' && (xmlString.charAt(nextK-1) == '/')){
							nextK--;
					}
					attrValueEnd = nextK;
					
					String attrName = xmlString.substring(attrNameStart,attrNameEnd);
					String attrValue = xmlString.substring(attrValueStart, attrValueEnd);
					if(attrName != null){
						attrName = attrName.trim();
					}
					if(attrValue != null){
						attrValue = attrValue.trim();
					}
					
					if(attrName != null && attrName.length() > 0){
						if(currentNode != null){
							if(attrValue == null){
								attrValue = "";
							}
							currentNode.setAttr(attrName.toLowerCase(), attrValue);
							//System.out.println(attrName+"="+attrValue);
						}
						else{
							error = true;
						}
					}
					
					state= XMLState.afterNameOrAttrValue;
					
				}
				else{
					nextK++;
				}
				

				break;
			case afterNameOrAttrValue:
				c = xmlString.charAt(nextK);
				
				if(c != ' '){
					if(c == '>'){
						state = XMLState.start;
						nextNodeType = nextNodeType.child;
						nextK++;
					}
					else if(c == '/' &&
						nextK < endOff &&
							xmlString.charAt(nextK+1) == '>'){
						state = XMLState.start;
						nextNodeType = nextNodeType.brother;
						nextK +=2;
					}
					else if(Character.isLetter(c)){
						attrNameStart = nextK;
						state = XMLState.attrName;
						nextK++;
					}
					else{
						error = true;
					}
				
				
				}
				else{
					nextK++;
				}
				
				break;
			case closeTag:
				c = xmlString.charAt(nextK);
				if(Character.isLetter(c)){
					closeNameStart = nextK;
					state = XMLState.closeName;
				}
				nextK++;
				break;
			case closeName:
				c = xmlString.charAt(nextK);
				if(c == '>'){
					closeNameEnd = nextK;
					String name = xmlString.substring(closeNameStart, closeNameEnd).trim().toLowerCase();
					if(nextNodeType == NextNodeType.brother){//内部有子节点（子节点结束将nextNodeType设置nextNodeType.brother）
						XMLNode parent = null;
						if(currentNode != null &&
								(parent = currentNode.getParent())!= null){
							if(parent.getName().equals(name) == false){
								error = true;
							}
							currentNode = parent;
						}
						else{
							error = true;
						}
					}
					else if(nextNodeType == NextNodeType.child){
						if(currentNode == null){
							error = true;
						}
						else{
							if(currentNode.getName().equals(name)== false){
								error= true;
							}
							else{
								//set txt
								currentNode.setTxtFromString(xmlString, txtStart, txtEnd);
							}
						}
					}
					
					nextNodeType = NextNodeType.brother;//我结束了，下一个newtag节点是 NextNodeType.brother
					state = XMLState.start;
				}
				nextK++;
				break;
			default:
				break;
			}
			
			if(state == XMLState.end || error == true){
				break;
			}
			
		}
		if(error == true){
			throw new XMLParseException("错误：xml串在"+state.toString()+"段有错误");
		}
		else if((nextK >=endOff && state != XMLState.start) || 
				(nextK < endOff && state != XMLState.end)){
			throw new XMLParseException("错误：xml串在"+state.toString()+"段非正常结束");
		}
		/*XMLNode newRoot = new XMLNode();
		try{
			newRoot.parseStr(xmlString,0);
			this.bodyString = xmlString;
			this.root = newRoot;
		}
		catch(XMLParseException e){
			newRoot = null;
			throw e;
		}*/
		
	}
	public XMLNode getXMLRoot(){
		return this.root;
	}
	
	public String getBodyString(){
		return this.bodyString;
	}
}
