package net.sourceforge.x360mediaserve.utils;

import java.io.File;

import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;

public class XML {
	
	public static Node parseXMLFile(File file)
	{
		try{
			Parser parser= org.cybergarage.upnp.UPnP.getXMLParser();
			Node rootNode = parser.parse(file);
			return rootNode;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
