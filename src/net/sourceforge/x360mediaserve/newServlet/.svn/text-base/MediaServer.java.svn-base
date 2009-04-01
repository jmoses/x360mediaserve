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

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.clinkwrap.responder.UPNPResponder;
import net.sourceforge.x360mediaserve.newServlet.plugins.ConfigPlugin;
import net.sourceforge.x360mediaserve.newServlet.plugins.PlaybackPlugin;
import net.sourceforge.x360mediaserve.newServlet.plugins.Plugin;
import net.sourceforge.x360mediaserve.newServlet.plugins.Scrobbling;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediareceiverregistrar.MediaReceiverRegistrar;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory.ContentDirectory;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.xml.Node;

/** Servlet covering the whole media server
 * @author tom
 *
 */
public class MediaServer extends HttpServlet{
	//TODO maybe move servlet functionality into a smaller class to simplify things 
	static Logger logger = Logger.getLogger("x360mediaserve");


	protected MediaServerConfigData config; // Class to store the config data in

	protected Node scpdNode=null;


	MediaReceiverRegistrar mediaRecReg=new MediaReceiverRegistrar();
	ContentDirectory contentDirectory=new ContentDirectory();
	WebServer webserver;

	HashMap<String,Plugin> plugins=new HashMap<String,Plugin>();



	UPNPResponder upnpResponder;
	Configurator configurator;



	public MediaServer(){
		super();		
		logger.info("Starting media server");		
		setup("",7000);
	}

	public MediaServer(String externalAddress){
		super();		
		logger.info("Starting media server servlet with address:"+externalAddress);			
		setup(externalAddress,7000);

	}

	public MediaServer(String externalAddress,int port){
		super();		
		logger.info("Starting media server with address:"+externalAddress+" and port:"+port);		
		setup(externalAddress,port);
	}

	private void setup(String externalAddress,int port)
	{

		config=new MediaServerConfigData();
		config.externalAddress=externalAddress;
		config.port=port;
		try {
			loadSCPD(new File("files/MediaServer.xml"));
			configurator=new Configurator(this,contentDirectory,mediaRecReg,config);
			logger.debug(config.externalAddress+" "+config.port);
			upnpResponder = new UPNPResponder(config.externalAddress,config.port,scpdNode.toString());
			if(!upnpResponder.start()){
				logger.fatal("Couldnt start listener");					
			}
			else
			{
				setUpWebServer();
			}
		} catch (InvalidDescriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//this.addPlugin(new Scrobbling());

	}

	private void setUpWebServer()
	{
		webserver=new WebServer(this);
		webserver.addConnector(config.externalAddress, config.port);
		webserver.addConnector("127.0.0.1", config.port);
		webserver.start();
	}




	protected  void doPost(HttpServletRequest req, HttpServletResponse resp){		
		logger.debug("Do Post:"+req.getPathInfo());
		try{
			if(req.getPathInfo().contains(config.contentDirectoryPath)){
				logger.debug("Sending to content dir");
				contentDirectory.doPost(req,resp);
			}
			else if(req.getPathInfo().contains(config.mediaRecRegPath)){
				mediaRecReg.doPost(req,resp);
			}
			else if(req.getPathInfo().contains("configure")){
				logger.info("Doing configure");
				configurator.doPost(req,resp);
			}
		}
		catch(Exception e){
			logger.error(e.toString());
		}
	}






	protected void doGet(HttpServletRequest req,HttpServletResponse resp){

		try{
			logger.debug(req.getRemoteAddr()+":"+req.getPathInfo());
			if(req.getPathInfo().contains(config.contentDirectoryPath)){
				logger.debug("Got Content Dir");
				contentDirectory.doGet(req,resp);
			}
			else if(req.getPathInfo().contains(config.mediaRecRegPath)){
				mediaRecReg.doGet(req,resp);
			}
			else if(req.getPathInfo().contains(config.configurePath)){
				configurator.doGet(req,resp);
			}
			else{
				PrintWriter writer=resp.getWriter();
				writer.write("<?xml version=\"1.0\"?>\n");
				writer.write(this.getSCPDNode().toString());
			}

		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.toString());
		}
	}		


	private boolean loadSCPD(File scpdFile)
	{
		this.scpdNode=net.sourceforge.x360mediaserve.utils.XML.parseXMLFile(scpdFile);
		if(this.scpdNode==null)
		{
			return false;
		}
		return true;		
	}

	public Node getSCPDNode(){
		return scpdNode;
	}

	public void addPlugin(Plugin plugin)
	{
		if(plugin instanceof ConfigPlugin)
		{
			configurator.addPlugin((ConfigPlugin)plugin);
		}
		if(plugin instanceof PlaybackPlugin)
		{
			this.contentDirectory.addPlaybackPlugin((PlaybackPlugin)plugin);
		}

		//configurator.addPlugin(new Scrobbling());
	}

}
