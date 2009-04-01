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



package net.sourceforge.x360mediaserve.upnpmediaserver.mediareceiverregistrar;



import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.X360MSService;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.Action;



public class MediaReceiverRegistrar extends X360MSService {
	
	// Servlet to handle the MediaReceiverRegistrar stuff in Windows Media Connect
	static Logger logger = Logger.getLogger("x360mediaserve");
		
	String SERVICE_TYPE="urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1";
	
//	String SCPDString = 
//		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" +
//		"   <specVersion>\n" +
//		"      <major>1</major>\n" +
//		"      <minor>0</minor>\n" +
//		"	</specVersion>\n" +
//		"	<actionList>\n" +
//		"		<action>\n" +
//		"         <name>IsAuthorized</name>\n" +
//		"         <argumentList>\n" +
//		"            <argument>\n" +
//		"               <name>DeviceID</name>\n" +
//		"               <direction>in</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_DeviceID</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"            <argument>\n" +
//		"               <name>Result</name>\n" +
//		"               <direction>out</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"         </argumentList>\n" +
//		"      </action>\n" +
//		"      <action>\n" +
//		"         <name>RegisterDevice</name>\n" +
//		"         <argumentList>\n" +
//		"            <argument>\n" +
//		"               <name>RegistrationReqMsg</name>\n" +
//		"               <direction>in</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_RegistrationReqMsg</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"            <argument>\n" +
//		"               <name>RegistrationRespMsg</name>\n" +
//		"               <direction>out</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_RegistrationRespMsg</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"         </argumentList>\n" +
//		"      </action>\n" +
//		"      <action>\n" +
//		"         <name>IsValidated</name>\n" +
//		"         <argumentList>\n" +
//		"            <argument>\n" +
//		"               <name>DeviceID</name>\n" +
//		"               <direction>in</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_DeviceID</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"            <argument>\n" +
//		"               <name>Result</name>\n" +
//		"               <direction>out</direction>\n" +
//		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
//		"            </argument>\n" +
//		"         </argumentList>\n" +
//		"      </action>\n" +
//		"   </actionList>\n" +
//		"   <serviceStateTable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>A_ARG_TYPE_DeviceID</name>\n" +
//		"         <dataType>string</dataType>\n" +
//		"      </stateVariable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>A_ARG_TYPE_Result</name>\n" +
//		"         <dataType>int</dataType>\n" +
//		"      </stateVariable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>A_ARG_TYPE_RegistrationReqMsg</name>\n" +
//		"         <dataType>bin.base64</dataType>\n" +
//		"      </stateVariable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>A_ARG_TYPE_RegistrationRespMsg</name>\n" +
//		"         <dataType>bin.base64</dataType>\n" +
//		"      </stateVariable>\n" +
//		
//		
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>AuthorizationGrantedUpdateID</name>\n" +
//		"         <dataType>ui4</dataType>\n" +
//		"      </stateVariable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>AuthorizationDeniedUpdateID</name>\n" +
//		"         <dataType>ui4</dataType>\n" +
//		"      </stateVariable>\n" +
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>ValidationSucceededUpdateID</name>\n" +
//		"         <dataType>ui4</dataType>\n" +
//		"      </stateVariable>\n" +		
//		"      <stateVariable sendEvents=\"no\">\n" +
//		"         <name>ValidationRevokedUpdateID</name>\n" +
//		"         <dataType>ui4</dataType>\n" +
//		"      </stateVariable>\n" +				
//		"   </serviceStateTable>\n" +
//		"</scpd>";	

	
	public MediaReceiverRegistrar(){
		super();
		try{
		logger.debug(System.currentTimeMillis()+ " Servlet init");
		loadSCPD(new File("files/MediaReceiverRegistrar.xml"));

		buildActionList();
		//this.getServletContext().log("boo");
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void doGet(HttpServletRequest req,HttpServletResponse resp){				
		try{
			resp.getWriter().write(this.scpdNode.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	


	
	
	protected boolean doAction(Action action,String ServerAddress){
		logger.debug("Action received:"+action.getName());
		if(action.getName().contains("IsAuthorized"))
			return isAuthorized(action);
		else if(action.getName().contains("IsValidated"))
			return isValidated(action);

		return false;
	}
	
	private boolean isValidated(Action action)
	{	
		action.getArgument("Result").setValue("1");
		return true;
	}
	
	private boolean isAuthorized(Action action)
	{	
		logger.debug("isAuthorized received, responding");		
		action.getArgument("Result").setValue("1");
		logger.debug("isAuthorized received, responded with "+action.getArgument("Result").getValue());
		
		return true;
	}

	
}
