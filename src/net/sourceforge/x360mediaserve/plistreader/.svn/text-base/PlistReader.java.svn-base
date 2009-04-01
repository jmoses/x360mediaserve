/**
 * one line to give the program's name and an idea of what it does.
 Copyright (C) 2006  Thomas Walker
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


package net.sourceforge.x360mediaserve.plistreader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;

public class PlistReader {
	
	static public Object getPlistFromFile(File file){
		SAXParserFactory factory=SAXParserFactory.newInstance();
		Object result=null;
		try{
			
			factory.setValidating(false);
			
			SAXParser parser=factory.newSAXParser();					
			Handler handler=new Handler();
						
			parser.parse(file,handler);
			
			result=handler.getResult();
			
		}
		catch(Exception e){			
			e.printStackTrace();
		}
		return result;
	}
	
	
	static class Handler extends DefaultHandler{
		
		Stack stack=new Stack(); // Stack to put containers in
		
		private Object plist;
		private String key;
		private Object container=null;
		
		private String currentType;
		private Object currentObject;
		
		private void addObjectToContainer(Object object) throws SAXException{
			if(container instanceof HashMap){
				((HashMap<String,Object>)container).put(key,object);
			}
			else if(container instanceof ArrayList){				
				((ArrayList)container).add(object);								
			}
			else{
				// oops we have ended up with an object not in a container
				throw new SAXException("Object outside of container");
			}
		}
		
		
		public void startElement(String uri,
				String localName,
				String qName,
				Attributes attributes)
		throws SAXException{
			currentdata="";
			if(qName.toLowerCase().equals("plist")){
				
			}
			else if(qName.toLowerCase().equals("dict")){
				Object newcontainer=new HashMap<String,Object>();
				if(container!=null){
					stack.push(container);
					addObjectToContainer(newcontainer);
				}
				
				
				container=newcontainer;
				
			}
			else if(qName.toLowerCase().equals("array")){
				
				Object newcontainer=new ArrayList();
				if(container!=null){
					stack.push(container);
					addObjectToContainer(newcontainer);
				}
				
				
				
				container=newcontainer;
				
			}
			else if(qName.toLowerCase().equals("true")){
				
				currentObject=new Boolean(true);
			}
			else if(qName.toLowerCase().equals("false")){
				
				currentObject=new Boolean(false);
			}
			else{
				currentType=qName.toLowerCase();
			}
			
		}
		
		public Object getResult(){
			return plist;
		}
		
		public void endElement(String uri,
				String localName,
				String qName)
		throws SAXException{
			if(qName.toLowerCase().equals("plist")){
				plist=container;
			}
			else if(qName.toLowerCase().equals("dict") && !stack.isEmpty()){
				container=stack.pop();
			}
			else if(qName.toLowerCase().equals("array") && !stack.isEmpty()){
				
				container=stack.pop();
			}
			else if(currentType.equals("key")){
				generateObject();
				// if its a key thats finished do nowt
			}
			else{
				generateObject();
				
				addObjectToContainer(currentObject);
			}
		}
		
		String currentdata;
		
		public void characters(char[] ch,
				int start,
				int length)
		throws SAXException{
			String data=String.copyValueOf(ch,start,length);
			currentdata+=data;
		}
		
		private void generateObject() throws SAXException{
			if(currentType.equals("key")){
				key=currentdata;
			}
			else if(currentType.equals("string")){
				currentObject=currentdata;
			}
			else if(currentType.equals("integer")){
				currentObject=Long.parseLong(currentdata);
			}
			else if(currentType.equals("date")){
				currentObject=currentdata;
			}
			else if(currentType.equals("real")){
				currentObject=Double.parseDouble(currentdata);
			}
			else if(currentType.equals("data")){
				currentObject=currentdata;
			}
			else{
				throw new SAXException("Unidentified element type:"+key);
			}
		}
		
		
		
	}
	
	public static void main(String[] args){
		System.out.println("Starting");
		Object plist=PlistReader.getPlistFromFile(new File("D:\\Documents and Settings\\Tom\\My Documents\\My Music\\iTunes\\iTunes Music Library.xml"));
		
		
		if(plist instanceof HashMap){
			HashMap<String,Object> dictionary=(HashMap<String,Object>)plist;			
			
			for(String str:dictionary.keySet()){
				System.out.println(str+" "+dictionary.get(str).getClass().toString());
				if(str.equals("Playlists")){
					ArrayList pl=(ArrayList)dictionary.get(str);
					for(Object o:pl){
						System.out.println(o.getClass().toString());
					}
				}
			}
			System.out.println("Is HashMap");
		}
	
	}
	
}
