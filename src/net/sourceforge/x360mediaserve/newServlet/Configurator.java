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

package net.sourceforge.x360mediaserve.newServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.netUtils.NetUtils;
import net.sourceforge.x360mediaserve.newServlet.plugins.ConfigItem;
import net.sourceforge.x360mediaserve.newServlet.plugins.ConfigPlugin;
import net.sourceforge.x360mediaserve.newServlet.plugins.Scrobbling;
import net.sourceforge.x360mediaserve.newServlet.plugins.TextConfigItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediareceiverregistrar.MediaReceiverRegistrar;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory.ContentDirectory;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;

/** Class to handle configuration of server
 * @author Tom
 *
 */
public class Configurator { 
	// TODO simplify element setup  
	static Logger logger = Logger.getLogger("x360mediaserve");

	/**
	 * @author Tom
	 *
	 */
	private interface ConfiguratorListener{
		void process(HashMap<String,String> formdata);
	}

	private class ConfiguratorElement{
		String url;
		String formElement;
		ConfiguratorListener listener;
		/**
		 * @param url
		 * @param element
		 * @param listener
		 */
		public ConfiguratorElement(String url, String element, ConfiguratorListener listener) {
			super();
			// TODO Auto-generated constructor stub
			this.url = url;
			formElement = element;
			this.listener = listener;
		}
	}

	ArrayList<ConfigPlugin> plugins;
	MediaServer server;
	ContentDirectory contentdir;
	MediaReceiverRegistrar mediarecreg;
	//String path;
	MediaServerConfigData configData;

	ArrayList<ConfiguratorElement> elements;

	public Configurator(MediaServer server,ContentDirectory contentdir, MediaReceiverRegistrar mediarecreg,MediaServerConfigData configData){
		this.server=server;
		this.contentdir=contentdir;
		this.mediarecreg=mediarecreg;
		this.configData=configData;
		//this.path=configData.configurePath;
		elements=new ArrayList<ConfiguratorElement>();
		addElements();

		if(configFile.exists()){			
				try {
					loadConfig();
				} catch (InvalidDescriptionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
		}
		else if(configData.externalAddress==""){
			configData.externalAddress=NetUtils.getExternalAddress();
			logger.debug("External Address set to:"+configData.externalAddress);
		}
		plugins=new ArrayList<ConfigPlugin>();
		
	}

	int nextConfiguratorElementID=1;

	public int nextConfiguratorElement(){
		return nextConfiguratorElementID++;
	}
	
	public void addPlugin(ConfigPlugin plugin)
	{
		plugins.add(plugin);
		//new Scrobbling()
		Node config=this.getConfigNodeForPlugin(plugin.getID());
		
		HashMap<String,String> map=new HashMap<String,String>();
		for(int i=0;i<config.getNNodes();i++)
		{
			Node n=config.getNode(i);
			logger.debug(n.getName()+":"+n.getValue());
			map.put(n.getName(), n.getValue());
		}
		plugin.setConfig(map);
		
	}

	public void addElement(String formdata, ConfiguratorListener listener){
		elements.add(new ConfiguratorElement(nextConfiguratorElement()+".cgi",formdata,listener));
	}

	public void addElements(){
		//ConfiguratorElement element;
		ConfiguratorListener listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String musicDir=formdata.get("musicdir");
				if(musicDir!=null){
					File file=new File(musicDir);
					if(file.exists() && file.isDirectory()){
						//contentdir.addMusicDir(file);
						setMusicDir(musicDir);
					}
				}
			}
		};
		addElement("Music Dir: <input type=\"text\" name=\"musicdir\">",listener);

		
		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String videoDir=formdata.get("videodir");
				if(videoDir!=null){
					File file=new File(videoDir);
					if(file.exists() && file.isDirectory()){
						//contentdir.addMusicDir(file);
						setVideoDir(videoDir);
					}
				}
			}
		};
		addElement("Video Dir: <input type=\"text\" name=\"videodir\">",listener);		
		
		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String photoDir=formdata.get("photodir");
				if(photoDir!=null){
					File file=new File(photoDir);
					if(file.exists() && file.isDirectory()){
						//contentdir.addMusicDir(file);
						setPhotoDir(photoDir);
					}
				}
			}
		};
		addElement("Photo Dir: <input type=\"text\" name=\"photodir\">",listener);
		
		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String iTunesFile=formdata.get("itunesfile");
				if(iTunesFile!=null){
					setiTunesFile(iTunesFile);
				}
			}
		};

		addElement("iTunesFile Dir: <input type=\"text\" name=\"itunesfile\">",listener);

		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String friendlyName=formdata.get("friendlyname");
				if(friendlyName!=null){
					setFriendlyName(friendlyName);
				}
			}
		};

		addElement("Friendly Name: <input type=\"text\" name=\"friendlyname\">",listener);




		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				logger.info("Doing pcm config");
				String formatString=formdata.get("format");

				if(formatString!=null){
					logger.debug("formatString !=null");
					if(formatString.equals("mp3")) setPCMoption(false);
					else if(formatString.equals("pcm")) setPCMoption(true);
				}

			}
		};
		addElement("Output Format:" +
				"<SELECT NAME=\"format\">"+
				"<OPTION VALUE=\"mp3\">MP3"+
				"<OPTION VALUE=\"pcm\">PCM"+
				"</SELECT>",listener);



		listener=new ConfiguratorListener(){
			public void process(HashMap<String,String> formdata){
				String streamName=formdata.get("streamname");
				String url=formdata.get("URL");
				String formatString=formdata.get("format");
				if(streamName!=null && url!=null && formatString!=null){
					try{						
						int format=-1;
						if(formatString.equals("mp3")) format=0;
						else if(formatString.equals("wma")) format=1;
						else if(formatString.equals("generic")) format=2;

						if(format!=-1){
							addStream(streamName,url,format);
						}
					}
					catch(Exception urlException){
						logger.error(urlException.toString());
					}

				}

			}
		};
		addElement("Stream Name:<input type=\"text\" name=\"streamname\"><br>Stream URL:<input type=\"text\" name=\"URL\"><br>" +
				"<SELECT NAME=\"format\">"+
				"<OPTION VALUE=\"mp3\">MP3"+
				"<OPTION VALUE=\"wma\">WMA"+
				"<OPTION VALUE=\"generic\">Generic"+
				"</SELECT>",listener);
	}




	protected  void doPost(HttpServletRequest req, HttpServletResponse resp){		
		logger.info("doing post in configurator");
		if(req.getRemoteHost().contains("127.0.0.1")){
			logger.debug(req.getPathInfo()+" "+req.getRemoteHost());


			HashMap<String,String> params=new HashMap<String,String>();
			
				try {
					BufferedReader reader=req.getReader();
					logger.debug("Opened reader");

					String lineData="";

					{
						String line;
						while((line=reader.readLine())!=null){
							lineData+=line;
						}
					}

					for(String data:lineData.split("&"))
					{

						String[] option=data.split("=");
						if(option.length>1){
							logger.debug("Replace with http string handling");
							params.put(option[0],option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," ").replace("%3F","?").replace("%3D","="));
							logger.debug(option[0]+": "+option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			String file=req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1);
			for(ConfiguratorElement element:elements){
				if(file.equals(element.url)){
					element.listener.process(params);
				}
			}
			for(ConfigPlugin plugin:plugins)
			{
				if(file.equals(plugin.getID()))
				{
					this.setConfigForPlugin(plugin, params);
					plugin.setConfig(params);
				}
			}
		}

	}


	protected void doGet(HttpServletRequest req,HttpServletResponse resp){


			try {
				logger.info(req.getPathInfo());
				PrintWriter writer=resp.getWriter();
				//resp.getWriter().write("<form action=\"http://127.0.0.1:7000/configure/addDir\" method=\"POST\">Music Dir: <input type=\"text\" name=\"musicdir\"><br>iTunes File: <input type=\"text\" name=\"itunesfile\"><br><br><input type=\"submit\" value=\"Send\"></form><form action=\"http://127.0.0.1:7000/configure/addStream\" method=\"POST\">Stream Name:<input type=\"text\" name=\"name\"><br>Stream URL:<input type=\"text\" name=\"URL\"><br><br><input type=\"submit\" value=\"Send\"></form>");
				writer.write("<HTML>\n");
				for(ConfiguratorElement element:elements){
					//writer.write("<Form action=\"http://127.0.0.1:7000"+configData.configurePath+"/"+element.url+"\" method=\"POST\">\n");
					writer.write("<Form action=\""+configData.configurePath+"/"+element.url+"\" method=\"POST\">\n");
					writer.write(element.formElement);
					writer.write("<input type=\"submit\" value=\"Send\">\n");
					writer.write("</Form>\n");
				}
				for(ConfigPlugin plugin : plugins)
				{
					writer.write(plugin.getName());
					writer.write("<Form action=\""+configData.configurePath+"/"+plugin.getID()+"\" method=\"POST\">\n");
					for(ConfigItem item: plugin.getConfigItems())
					{
						if(item instanceof TextConfigItem)
						{
							writer.write(item.friendlyName+": <input type=\"text\" name=\""+item.id+"\">");
						}
					}
					writer.write("<input type=\"submit\" value=\"Send\">\n");
					writer.write("</Form>\n");
				}
				
				writer.write("</HTML>\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

	


	public ContentDirectory getContentdir() {
		return contentdir;
	}


	public MediaReceiverRegistrar getMediarecreg() {
		return mediarecreg;
	}


	public MediaServer getServer() {
		return server;
	}



	// simple xml configure stuff
	Node configNode=new Node("Configuration");
	File configFile=new File("config.xml");

	private void processConfigData()
	{
		// take the config data, check everything is correct and then load it into the server

		// server needs to have a UDN, generate one if needs be
		if(configData.udn.equals("")){			
			configData.udn=generateUDN();
		}
		logger.info("Setting UDN to "+configData.udn);
		this.setUDN(configData.udn);


		// set Friendly name (has a default value so should be ok
		if(configData.friendlyName.equals("")) configData.friendlyName="x360mediaserve";
		logger.info("Setting Friendly Name to \""+configData.friendlyName+"\"");		
		this.setFriendlyName(configData.friendlyName);

		// get ip Address to use (if not set then use the current ip)
		if(configData.externalAddress==""){
			configData.externalAddress=NetUtils.getExternalAddress();
		}
		logger.info("External Address set to:"+configData.externalAddress);
	}

	

	private boolean loadConfig() throws InvalidDescriptionException
	{
		try {
			Parser parser = UPnP.getXMLParser();
			Node configureNode = parser.parse(configFile);
			if (configureNode == null)
				return false;			
			this.configNode=configureNode;
			processConfigNode();			
		}
		catch (ParserException e) {
			throw new InvalidDescriptionException(e);
		}

		processConfigData(); // process the config data and reconfigure the server accordingly
		System.gc();
		return true;
	}

	private void processConfigNode(){
		for(int i=0;i<configNode.getNNodes();i++){
			Node node=configNode.getNode(i);
			if(node.getName().equals("iTunesFile")){
				File file=new File(node.getValue());
				if(file.exists() && file.isFile()){
					contentdir.addiTunesDB(file);
				}
				else{
					logger.info("iTunes XML file path wrong:"+file.toString());
				}
			}
			else if(node.getName().equals("MusicDir")){
				File file=new File(node.getValue());
				if(file.exists() && file.isDirectory()){
					contentdir.addMusicDir(file);
				}
				else{
					logger.info("Music Dir file path wrong:"+file.toString());
				}
			}
			else if(node.getName().equals("VideoDir")){
				File file=new File(node.getValue());
				if(file.exists() && file.isDirectory()){
					contentdir.setVideoDirectory(file);
				}
				else{
					logger.info("Video Dir file path wrong:"+file.toString());
				}
			}
			else if(node.getName().equals("PhotoDir")){
				File file=new File(node.getValue());
				if(file.exists() && file.isDirectory()){
					contentdir.setPhotoDirectory(file);
				}
				else{
					logger.info("Photo Dir file path wrong:"+file.toString());
				}
			}
			else if(node.getName().equals("FriendlyName")){
				//server.setFriendlyName(node.getValue());

				configData.friendlyName=node.getValue();
			}
			else if(node.getName().equals("UDN")){
				configData.udn=node.getValue();
			}
			else if(node.getName().equals("Streams")){
				for(int j=0;j<node.getNNodes();j++){
					processStreamNode(node.getNode(j));
				}
			}
			else if(node.getName().equals("PCMOutput")){
				if(node.getValue().equals("1")) contentdir.setPCMOption(true);
				else contentdir.setPCMOption(false);
			}
		}
	}

	private void addStream(String name,String url,int type){
		contentdir.addStream(name,url,type);

		Node streamNode=configNode.getNode("Streams");

		if(streamNode==null){
			streamNode=new Node("Streams");
			configNode.addNode(streamNode);
		}

		Node newStreamNode=new Node("Stream");

		Node nameNode=new Node("Name");
		nameNode.setValue(name);
		newStreamNode.addNode(nameNode);


		Node urlNode=new Node("URL");
		urlNode.setValue(url.toString());
		newStreamNode.addNode(urlNode);


		Node typeNode=new Node("Type");
		typeNode.setValue(type);
		newStreamNode.addNode(typeNode);


		streamNode.addNode(newStreamNode);

		saveConfig();

	}

	private void processStreamNode(Node streamNode){
		
			try {
				Node tmpnode;
				String url=null;
				String name=null;
				int type=-1;
				if((tmpnode=streamNode.getNode("URL"))!=null){
					url=tmpnode.getValue();
					//url=new URL(tmpnode.getValue());
				}

				if((tmpnode=streamNode.getNode("Name"))!=null){
					name=tmpnode.getValue();
				}

				if((tmpnode=streamNode.getNode("Type"))!=null){
					type=Integer.parseInt(tmpnode.getValue());
				}

				if(url!=null && name!=null && type!=-1)
					contentdir.addStream(name,url,type);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			
	}

	private void setiTunesFile(String iTunesFile){
		File file=new File(iTunesFile);
		if(file.exists() && file.isFile()){
			contentdir.addiTunesDB(file);
			Node node=configNode.getNode("iTunesFile");
			if(node!=null){
				node.setValue(iTunesFile);
			}
			else{
				node=new Node("iTunesFile");
				node.setValue(iTunesFile);
				configNode.addNode(node);
			}
			saveConfig();
		}
	}

	private void setMusicDir(String musicDir){
		File file=new File(musicDir);
		if(file.exists() && file.isDirectory()){
			contentdir.addMusicDir(file);
			Node node=configNode.getNode("MusicDir");
			if(node!=null){
				node.setValue(musicDir);
			}
			else{
				node=new Node("MusicDir");
				node.setValue(musicDir);
				configNode.addNode(node);
			}
			saveConfig();
		}
	}
	
	private void setVideoDir(String videoDir){
		File file=new File(videoDir);
		if(file.exists() && file.isDirectory()){
			contentdir.setVideoDirectory(file);
			Node node=configNode.getNode("VideoDir");
			if(node!=null){
				node.setValue(videoDir);
			}
			else{
				node=new Node("VideoDir");
				node.setValue(videoDir);
				configNode.addNode(node);
			}
			saveConfig();
		}
	}
	
	private void setPhotoDir(String photoDir){
		File file=new File(photoDir);
		if(file.exists() && file.isDirectory()){
			contentdir.setPhotoDirectory(file);
			Node node=configNode.getNode("PhotoDir");
			if(node!=null){
				node.setValue(photoDir);
			}
			else{
				node=new Node("PhotoDir");
				node.setValue(photoDir);
				configNode.addNode(node);
			}
			saveConfig();
		}
	}

	private void setFriendlyName(String friendlyName){
		Node friendlyNameNode=configNode.getNode("FriendlyName");
		//friendlyName+=":1";
		if(friendlyNameNode==null){
			friendlyNameNode=new Node("FriendlyName");
			configNode.addNode(friendlyNameNode);
		}
		friendlyNameNode.setValue(friendlyName);		
		server.getSCPDNode().getNode("device").getNode("friendlyName").setValue(friendlyName+":1: Windows Media Connect");							
		saveConfig();		
	}

	private void setPCMoption(boolean option){
		logger.info("Setting PCMoption to "+option);
		Node pcmOptionNode=configNode.getNode("PCMOutput");
		if(pcmOptionNode==null){
			pcmOptionNode=new Node("PCMOutput");
			configNode.addNode(pcmOptionNode);
		}
		pcmOptionNode.setValue(option?"1":"0");
		contentdir.setPCMOption(option);
		saveConfig();
	}


	private void setUDN(String UDN){
		Node udnNode=configNode.getNode("UDN");
		if(udnNode==null){
			udnNode=new Node("UDN");
			configNode.addNode(udnNode);
		}
		udnNode.setValue(UDN);
		server.getSCPDNode().getNode("device").setNode("UDN", UDN);
		saveConfig();
	}

	private void saveConfig(){
		try{
			PrintWriter writer=new PrintWriter(new FileWriter(configFile));
			writer.write(configNode.toString());
			writer.close();
		}
		catch(IOException e){
			logger.error(e.toString());
		}
	}

	private String generateUDN(){
		return "uuid:492a4242-22c2-25b1-a112-00000" + (System.currentTimeMillis()%1000000+1000000);
	}
	
	private Node getConfigNodeForPlugin(String pluginName)
	{
		Node pluginsNode=configNode.getNode("plugins");
		if(pluginsNode==null){
			configNode.addNode(new Node("plugins"));
			pluginsNode=configNode.getNode("plugins");
		}
		Node result=pluginsNode.getNode(pluginName);
		if(result==null)
		{
			pluginsNode.addNode(new Node(pluginName));
			result=pluginsNode.getNode(pluginName);
		}
		return result;
	}
	
	private void setConfigForPlugin(ConfigPlugin plugin,HashMap<String,String> values)
	{
		Node pluginConfigNode=getConfigNodeForPlugin(plugin.getID());
		for(String key:values.keySet())
		{
			String value=values.get(key);
			if(value!=null)
			{
				Node node=pluginConfigNode.getNode(key);
				if(node==null)
				{
					pluginConfigNode.addNode(new Node(key));
					node=pluginConfigNode.getNode(key);
				}
				node.setValue(value);				
			}
		}
		saveConfig();
	}
	
	

}
