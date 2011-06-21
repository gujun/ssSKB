package com.cari.voip.keyboard.stack.events;

import java.util.HashMap;
import java.util.Map;

public class XMLNode {
	//tag name
	private String name = null;
	// tag attributes
	private Map<String,String> attr = null;
	// tag character contect,empty string if none
	private String txt = null;
	// tag offset from start of parent tag character content
	private int off = -1;
	//next tag with same name in same section and depth
	private XMLNode next = null;
	private XMLNode lastNext = null;
	
	//next tag with different name in same section and depth
	private XMLNode sibling = null;
	//next tag
	private XMLNode ordered = null;
	private XMLNode lastOrdered = null;
	
	// head of sub tag list, null if none
	private XMLNode child = null;
	// parent tag ,null if current tag is root tag
	private XMLNode parent = null;
	//flags
	private int flags = 0;
	
	protected static int HAVE_SORTED_BY_NAME = 1<<0;
	
	private boolean hasChildNode;
	
	public XMLNode(){
		this(null,null);
	}
	public XMLNode(XMLNode parent){
		this(parent,null);
	}
	
	public XMLNode(XMLNode parent,String name){
		this.attr = new HashMap<String,String>();
		this.name = name;
		this.parent = parent;
		if(parent != null){
			
			if(parent.child == null){
				parent.child = this;
				
				this.off = 0;
			}
			else{
				parent.child.addBrotherOrdered(this);
				if(this.name != null){
					parent.child.addBrotherSiblingOrNext(this);
					this.flags |= XMLNode.HAVE_SORTED_BY_NAME;
				}
				
			}
		}
		
	}
	
	public XMLNode getParent(){
		return this.parent;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		if(name == null ||
				name.equals(this.name)){
			return;
		}
		if(this.parent != null &&
				this.parent.child != null){
			if(this.parent.child == this){
				XMLNode downNode = this.next;
				
				XMLNode prevNode = this;
				XMLNode nextNode = this.sibling;
				while(nextNode != null && name.equals(nextNode.name) == false){
					prevNode = nextNode;
					nextNode = nextNode.sibling;
				}
				if(nextNode != null){
					if(downNode != null){
						prevNode.sibling = downNode;
						downNode.sibling = nextNode.sibling;
					}
					else{
						prevNode.sibling = nextNode.sibling;
						nextNode.sibling = null;
					}
					
					this.next = nextNode;
				}
				else{
					if(downNode != null){
						downNode.sibling = this.sibling;
						this.sibling = downNode;
						this.next = null;
					}
				}
				this.name = name;
			}
			else{
				if((this.flags & XMLNode.HAVE_SORTED_BY_NAME) != 0){
					this.parent.child.removeBrotherSiblingOrNext(this);
				}
				this.name = name;
				this.parent.child.addBrotherSiblingOrNext(this);
				this.flags |= XMLNode.HAVE_SORTED_BY_NAME;
			}
			
		}
		else{
			this.name = name;
			
		}
	}
	public int getOff(){
		return this.off;
	}
	public XMLNode getNext(){
		return this.next;
	}
	public void setNext(XMLNode tag){
		this.next = tag;
	}
	public XMLNode getSiblingNext(){
		return this.sibling;
	}
	
	public XMLNode getChild(String name){
		if(name == null){
			return this.child;
		}
		XMLNode node = null;
		
		if(this.child != null){
			if(name.equals(this.child.name)){
				node = this.child;
			}
			else{
				XMLNode nextNode = this.child.sibling;
				while(nextNode != null && name.equals(nextNode.name) == false){
					nextNode = nextNode.sibling;
				}
				if(nextNode != null){
					node = nextNode;
				}
			}
		}
		return node;
		
	}
	public void setSibling(XMLNode tag){
		this.sibling = tag;
	}
	public XMLNode getOrderedNext(){
		return this.ordered;
	}
	public void setOrderedNext(XMLNode tag){
		this.ordered = tag;
	}
	
	
	protected void removeBrotherNext(XMLNode tag){
		if(this.next == null){
			return;
		}
		if(this.next == tag){
			this.next = tag.next;
		}
		else{
			XMLNode prevNode = this.next;
			XMLNode nextNode = prevNode.next;
			while(nextNode != null && nextNode != tag){
				prevNode = nextNode;
				nextNode = prevNode.next;
			}
			if(nextNode != null){
				prevNode.next = nextNode.next;
			}
		}
		if(this.lastNext == tag){
			this.lastNext = null;
		}
	}
	protected void addBrotherNext(XMLNode tag){
		if(this.next == null){
			this.next = tag;
		}
		else{
			if(this.lastNext != null){
				this.lastNext.next = tag;
			}
			else {
				XMLNode prevNode = this.next;
				XMLNode nextNode = prevNode.next;
				while(nextNode != null){
					prevNode = nextNode;
					nextNode = prevNode.next;
				}
				prevNode.next = tag;
			}
		}
		
		this.lastNext = tag;
	}
	protected void removeBrotherSibling(XMLNode tag){
		if(this.sibling == null){
			return;
		}
		if(this.sibling == tag){
			if(tag.next != null){
				this.sibling = tag.next;
				this.sibling.sibling = tag.sibling;
			}
			else{
				this.sibling = tag.sibling;
			}
		}
		else{
			this.sibling.removeBrotherSiblingOrNext(tag);
		}
	}
	protected void addBrotherSibling(XMLNode tag){
		if(this.sibling == null){
			this.sibling = tag;
		}
		else{
			this.sibling.addBrotherSiblingOrNext(tag);
		}
	}
	protected void addBrotherOrdered(XMLNode tag){
		if(this.ordered == null){
			this.ordered = tag;
			
			tag.off = this.off+1;
		}
		else{
			if(this.lastOrdered != null){
				this.lastOrdered.ordered = tag;
				tag.off = this.lastOrdered.off+1;
			}
			else{
				XMLNode prevNode = this.ordered;
				XMLNode nextNode = prevNode.ordered;
				while(nextNode != null){
					prevNode = nextNode;
					nextNode = prevNode.ordered;
				}
				prevNode.ordered = tag;
				tag.off = prevNode.off + 1;
			}
			
		}
		this.lastOrdered = tag;
	}
	public void removeBrotherSiblingOrNext(XMLNode brother){
		if(brother != null && brother.name != null && brother != this){
			if(this.name.equals(brother.getName())){
				this.removeBrotherNext(brother);
			}
			else{
				this.removeBrotherSibling(brother);
			}
		}
	}
	public void addBrotherSiblingOrNext(XMLNode brother){
		if(brother != null && brother.name != null && brother != this){
			
			if(this.name.equals(brother.getName())){
				this.addBrotherNext(brother);
			}
			else{
				this.addBrotherSibling(brother);
			}
			
			
		}
	}
	
	public void setAttr(String AttrName,String value){
		this.attr.put(AttrName, value);
	}
	
	public String getAttr(String AttrName){
		return this.attr.get(AttrName);
	}
	

	protected boolean setNameAndAttrFromString(String xmlString,int firstK,int endK){
		if(endK < firstK){
			
			return false;
		}
		boolean ok = true;
		try{
			String headString = xmlString.substring(firstK, endK);
			if(headString == null ||
					(headString = headString.trim()) == null ||
					headString.length() == 0){
				ok = false;
			}
			else{
				
				
				int spaceI = headString.indexOf(' ');
				
				if(spaceI < 0 ){
					//no attr
					this.setName(headString);
				}
				else{
					int i = spaceI;
					this.setName(headString.substring(0, i));
					int headLen = headString.length();
					int state = 0;
					
					int nameS = -1;
					int nameE = -1;
					int valueS = -1;
					int valueE = -1;
					String attrName = null;
					String attrValue = null;
					
					while( ++i < headLen){
						char c = headString.charAt(i);
						
						switch(state){
						case 0:// null
							if(c != ' '){
								nameS = i;
								state = 1;
							}
							break;
						case 1:// name
							if(c == '='){
								nameE = i;
								attrName = headString.substring(nameS, nameE);
								if(attrName == null || attrName.length() == 0){
									ok = false;
								}
								state = 2;
							}
							
							break;
						case 2:// name = 
							if(c == '"'){
								valueS = i+1;
								state = 3;
							}
							else if(c != ' '){
								valueS = i;
								state = 4;
							}
							break;
						
						case 3:// name = "
							if(c == '"'){
								valueE = i;
								attrValue = headString.substring(valueS, valueE);
								if(attrValue == null){
									ok = false;
								}
								else{
									this.setAttr(attrName, attrValue);
									state= 0;
								}
							}
							break;
						case 4: //name = xxx
							if(c == ' '){
								valueE = i;
								attrValue = headString.substring(valueS, valueE);
								if(attrValue == null){
									ok = false;
								}
								else{
									this.setAttr(attrName, attrValue);
									state= 0;
								}
							}
							break;
						default:
							ok = false;
							break;
						}
						
						if(ok == false){
							break;
						}
					}//while
					
					//check state if necessary
				}
			}
		}
		catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
	protected boolean setTxtFromString(String xmlString,int firstK,int endK){
		if(endK < firstK){
			
			return false;
		}
		boolean ok = true;
		try{
			String txtString = xmlString.substring(firstK, endK);
			if(txtString != null){
				this.txt = txtString.trim();
			}
		}
		catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
	protected boolean checkNameFromString(String xmlString,int firstK,int endK){
		if(endK < firstK){
			
			return false;
		}
		boolean ok = true;
		try{
			String nameString = xmlString.substring(firstK, endK);
			ok = (nameString==null)?
					((this.name==null)?true:false)
				: ((this.name==null)?false:this.name.equals(nameString.trim()));
		}
		catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
	public  int parseStr(String xmlString,int off) throws XMLParseException  {

		if(xmlString == null || xmlString.length() <= off){
			throw new XMLParseException("invalid xml string");
		}
		
		int startOff = off;
		int endOff = xmlString.length()-1;
		
		
		
		//head part
		boolean selfClosing = false;
		boolean hasName = false;
		
		int firstK = xmlString.indexOf('<', startOff);

		if(firstK < 0 || (firstK+1) >= endOff){
			throw new XMLParseException("head part no first tag");
		}
		
		int nextK = firstK+1;
		while(nextK <= endOff){
			if(xmlString.startsWith("!--", nextK)){
				nextK = xmlString.indexOf("-->", nextK+3);
				nextK+=3;
			}
			else if(xmlString.startsWith("?", nextK)){
				nextK = xmlString.indexOf("?>", nextK+1);
				nextK+=2;
			}
			else if(xmlString.startsWith("![CDATA[", nextK)){
				nextK = xmlString.indexOf("]]>", nextK+8);
				nextK+=3;
			}
			else if(xmlString.startsWith("!DOCTYPE", nextK)){
				nextK = xmlString.indexOf(">", nextK+8);
				nextK+=1;
			}
			else if(xmlString.startsWith("/", nextK)){
				nextK = xmlString.indexOf(">", nextK+1);
				nextK+=1;
			}
			else{
				break;
			}
			firstK = xmlString.indexOf('<', nextK);
			if(firstK < 0 || (firstK+1) >= endOff){
				throw new XMLParseException("head part no first tag");
			}
			nextK = firstK+1;
		}
		char c=xmlString.charAt(nextK);
		/*while(c == '!' || 
				c == '?' ||
				c == '/'){
			firstK = xmlString.indexOf('<', nextK);
			if(firstK < 0 || (firstK+1) >= endOff){
				throw new XMLParseException("head part no first tag");
			}
			nextK = firstK+1;
			c = xmlString.charAt(nextK);
		}*/
		
		
		while(nextK <= endOff ){
			c = xmlString.charAt(nextK);
			
			if(Character.isLetter(c)){
				hasName = true;
				break;
			}
			if(c=='>' || c=='/'){
				break;
			}
			nextK++;
		}
		
		if(hasName == false){
			throw new XMLParseException("head part no name");
		}
		
		firstK = nextK;
		
		nextK = xmlString.indexOf('>', firstK);
		if(nextK < 0 || nextK > endOff){
			throw new XMLParseException("head part no end");
		}
		int endK = nextK;
		if(xmlString.charAt(nextK-1) == '/'){
			selfClosing = true;
			endK--;
		}
		
		if(this.setNameAndAttrFromString(xmlString, firstK, endK) == false){
			
			throw new XMLParseException("head part setNameAndAttr error");
		}
		
		
		
		
		nextK++;
		//body part
		if(selfClosing == true){	
			return  nextK;
		}
		
		boolean hasChild = true;
		
		
		firstK = xmlString.indexOf('<', nextK);
		if(firstK < 0 || (firstK+1) >= endOff){
			throw new XMLParseException("body part no end tag");
		}
		
		if(xmlString.charAt(firstK+1) == '/'){
			hasChild = false;
		}
		
		if(hasChild == true){
		 do{
			XMLNode child = new XMLNode(this);
			nextK = child.parseStr(xmlString, firstK);
			
			firstK = xmlString.indexOf('<', nextK);
			if(firstK < 0 || (firstK+1) >= endOff){
				throw new XMLParseException("body part no end");
			}
			
			if(xmlString.charAt(firstK+1) == '/'){
				hasChild = false;
			}
		  }while(hasChild == true);
		}
		else{
			if(this.setTxtFromString(xmlString, nextK, firstK) == false){
				throw new XMLParseException("body part set text error");
			}
		}
		
		
		
		//end part
		nextK = xmlString.indexOf('>',firstK);
		if(nextK < 0 || nextK > endOff){
			throw new XMLParseException("end part no end tag");
		}
		if(this.checkNameFromString(xmlString, firstK+2, nextK) == false){
			throw new XMLParseException("end part checkName error");
		}
		nextK++;
		return nextK;
		
	}
	public String toString(){
		
		return null;
	}
	
	public String getTxt(){
		return this.txt;
	}
}
