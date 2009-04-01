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



package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.newServlet.plugins.PlaybackPlugin;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.MediaDB;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.X360MSService;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.media.server.action.BrowseAction;
import org.cybergarage.upnp.media.server.action.SearchAction;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;
import org.cybergarage.upnp.media.server.object.DIDLLite;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;


public class ContentDirectory extends X360MSService {
	
	static Logger logger = Logger.getLogger("x360mediaserve");

	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:ContentDirectory:1";
	
	File basePath=null;
	
	MediaDB musicDB=new MediaDB();
	
	String SERVICE_STRING="urn:schemas-upnp-org:service:ContentDirectory:1";
	
	private int systemUpdateID=0;
	
	public ContentDirectory(){
		super();
		try{
			logger.debug(System.currentTimeMillis()+ " Servlet init");			
			loadSCPD(new File("files/ContentDirectory.xml"));
			buildActionList();
			this.setServiceString(SERVICE_STRING);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public boolean loadSCPD(File scpdFile)
	{
		this.scpdNode=net.sourceforge.x360mediaserve.utils.XML.parseXMLFile(scpdFile);
		if(this.scpdNode==null)
		{
			return false;
		}
		return true;		
	}
	
	
	boolean browseActionReceived(BrowseAction action,String ServerAddress){		
		logger.debug("Browse action called");
		String containerID = action.getArgumentValue("ContainerID");	
		logger.debug("Browsing: "+containerID);
		logger.debug("Search started");
		ContentNodeList sortedContentNodeList = new ContentNodeList();
		musicDB.getBrowseList(sortedContentNodeList,containerID,ServerAddress);
		
		int nChildNodes=sortedContentNodeList.size();
		
		int startingIndex = action.getStartingIndex();
		if (startingIndex <= 0)
			startingIndex = 0;
		int requestedCount = action.getRequestedCount();
		if (requestedCount == 0)
			requestedCount = nChildNodes;
		
		DIDLLite didlLite = new DIDLLite();
		int numberReturned = 0;
		for (int n=startingIndex; (n<nChildNodes && numberReturned<requestedCount); n++) {		
			ContentNode cnode = sortedContentNodeList.getContentNode(n);
			didlLite.addContentNode(cnode);
			numberReturned++;
		}
		
		AttributeList atrrlist1=new AttributeList();	
		atrrlist1.add(new Attribute("dt:dt","ui4"));
		
		AttributeList atrrlist2=new AttributeList();
		atrrlist2.add(new Attribute("xmlns:dt","urn:schemas-microsoft-com:datatypes"));
		atrrlist2.add(new Attribute("dt:dt","ui4"));
		
		String result = didlLite.toString();		
		action.setResult(result);		
		action.setNumberReturned(numberReturned);
		action.setTotalMaches(nChildNodes);
		action.setUpdateID(getSystemUpdateID());
		return true;		
	}
	
	@Override
	protected boolean doAction(Action action,String ServerAddress) {
		// TODO Auto-generated method stub
		
		String actionName = action.getName();
		
		if (actionName.equals("Browse")) {
			BrowseAction browseAct = new BrowseAction(action);
			return browseActionReceived(browseAct,ServerAddress);
		}
		
		else if (actionName.equals("Search")) {
			SearchAction searchAct = new SearchAction(action);
			searchActionReceived(searchAct,ServerAddress);
			return true;
//			return searchActionReceived(searchAct);
		}
		
		//@id,@parentID,dc:title,dc:date,upnp:class,res@protocolInfo
		else if (actionName.equals("GetSearchCapabilities") == true) {
			Argument searchCapsArg = action.getArgument("SearchCaps");
//			String searchCapsStr = getSearchCapabilities();
//			searchCapsArg.setValue(searchCapsStr);
			return true;
		}
		
		//dc:title,dc:date,upnp:class
		if (actionName.equals("GetSortCapabilities") == true) {
			Argument sortCapsArg = action.getArgument("SortCaps");
			return true;
		}
		
		if (actionName.equals("GetSystemUpdateID") == true) {
//			Argument idArg = action.getArgument(ID);
//			idArg.setValue(getSystemUpdateID());
			return true;
		}
		
		return false;		
	}
	

	public void doGet(HttpServletRequest req,HttpServletResponse resp){
		
		try{
			logger.debug("Request address:"+req.getRemoteAddr());
			logger.debug("Contextpath:"+req.getContextPath());
			logger.debug("pathinfo:"+req.getPathInfo());
			logger.debug("request uri:"+req.getRequestURI());
			logger.debug("path translated:"+req.getPathTranslated());
			logger.debug("Query string"+req.getQueryString());
			if(req.getPathInfo().contains("Music")){
				logger.debug("Music Requested");
				streamMedia(req,resp);
			}
			else if(req.getPathInfo().contains("Dump"))
			{
				// dump music

			}
			else
			{
				resp.getWriter().write(this.scpdNode.toString());
			}

			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	public synchronized int getSystemUpdateID()
	{
		return systemUpdateID;
	}
	
	/* (non-Javadoc)
	 * Cheap configure script until someone writes a better one
	 * @see net.sourceforge.x360mediaserve.upnpmediaserver.upnp.Service#handleOtherPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void handleOtherPost(HttpServletRequest req, HttpServletResponse resp){
		if(req.getRemoteHost().contains("127.0.0.1")){
			logger.debug(req.getPathInfo()+" "+req.getRemoteHost());
			if(req.getPathInfo().endsWith("configure")){				
				logger.info("Doing configure");
				try{
					BufferedReader reader=new BufferedReader(req.getReader());
					String str;					
					while((str=reader.readLine())!=null){
						String[] confoptions=str.split("&");
						for(String s:confoptions){
							logger.debug(s);
							String[] option=s.split("=");
							if(option.length>1){
								if(option[0].contains("musicdir")){
									File f=new File(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
									if(f.exists() && f.isDirectory()){									
										logger.info("Adding music dir"+f.toString());
										musicDB.addDirectory(f);
									}
								}
								if(option[0].contains("itunesfile")){
									File f=new File(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
									logger.info(f.toString());
									if(f.exists() && f.isFile()){									
										logger.info("Adding iTunes dir"+f.toString());
										//musicDB.addItunesDataBase(f);
									}
								}
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
			else if(req.getPathInfo().endsWith("addStream")){
				try
				{
					BufferedReader reader=new BufferedReader(req.getReader());
					String str;
					String urlString=null;
					String streamName=null;
					while((str=reader.readLine())!=null){
						String[] confoptions=str.split("&");
						for(String s:confoptions){
							logger.debug(s);
							String[] option=s.split("=");
							if(option.length>1){
								if(option[0].contains("name")){
									logger.info("Should be using http string conversion");
									streamName=new String(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
			
								}
								if(option[0].contains("URL")){
									logger.info("Should be using http string conversion");
									urlString=new String(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
								}
							}
						}
					}
					
					
					if(urlString!=null && streamName!=null)
					{						
							//String url=new URL(urlString);
							//musicDB.addStream(streamName,urlString,0);
						logger.info("Added new Stream:"+streamName+" at "+urlString);
						
						
						
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addStream(String name,String url,int type){
		//musicDB.addStream(name,url,type);
	}
	
	public void addiTunesDB(File file){
		musicDB.addItunesDataBase(file);
	}
	
	public void addMusicDir(File file){
		musicDB.addDirectory(file);
		System.gc();
	}
	
	public void setVideoDirectory(File dir)
	{
		logger.info("Video dir:"+dir);
		musicDB.setVideoDirectory(dir);
	}
	
	public void setPhotoDirectory(File dir)
	{
		logger.info("Photo dir:"+dir);
		musicDB.setPhotoDirectory(dir);
	}
	
	
	private void streamMedia(HttpServletRequest req,HttpServletResponse resp){
		
				
		try{									
			String containerID=req.getPathInfo().split("Music/")[1];
			logger.debug("Got media path:"+containerID);
			String idString=containerID.substring(0,containerID.indexOf("."));
			String extension=containerID.substring(containerID.lastIndexOf(".")+1);			
			this.musicDB.playMediaWithExtension(containerID, req, resp);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean searchActionReceived(SearchAction action,String ServerAddress)
	{
		String containerID = action.getContainerID();
		String searchCriteria = action.getSearchCriteria();
		
		logger.debug("Search started");
		ContentNodeList sortedContentNodeList = new ContentNodeList();
		
		
		int startingIndex = action.getStartingIndex();
		if (startingIndex <= 0)
			startingIndex = 0;
		int requestedCount = action.getRequestedCount();
		if (requestedCount == 0)
			requestedCount = Integer.MAX_VALUE;
			//requestedCount = nChildNodes;
		
		int nChildNodes=musicDB.getHackSearchList(sortedContentNodeList,containerID,searchCriteria,ServerAddress,startingIndex,startingIndex+requestedCount-1);
		//int nChildNodes=sortedContentNodeList.size();
		
		DIDLLite didlLite = new DIDLLite();
		int numberReturned = 0;
		for (int n=startingIndex; (n<nChildNodes && numberReturned<requestedCount-1); n++) {		
			ContentNode cnode = sortedContentNodeList.getContentNode(n);
			didlLite.addContentNode(cnode);
			numberReturned++;
		}
		
		AttributeList atrrlist1=new AttributeList();	
		atrrlist1.add(new Attribute("dt:dt","ui4"));
		
		AttributeList atrrlist2=new AttributeList();
		atrrlist2.add(new Attribute("xmlns:dt","urn:schemas-microsoft-com:datatypes"));
		atrrlist2.add(new Attribute("dt:dt","ui4"));
		
		String result = didlLite.toString();		
		action.setResult(result);		
		action.setNumberReturned(numberReturned);
		action.setTotalMaches(nChildNodes);
		action.setUpdateID(getSystemUpdateID());
		return true;
	}
	
	public synchronized void updateSystemUpdateID()
	{
		systemUpdateID++;
	}
	
	public void setPCMOption(boolean option){
		musicDB.setPCMOption(option);
	}
	
	
	
	public void addPlaybackPlugin(PlaybackPlugin plugin)
	{
		musicDB.addPlaybackPlugin(plugin);
	}
	
	
}
