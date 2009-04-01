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


import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sourceforge.x360mediaserve.newServlet.MediaServer;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/** Class to run server
 * @author Tom
 *
 */
public class Run {
	
	static Logger logger = Logger.getLogger("x360mediaserve");
	
	public static void main(String[] args)
	{
		System.out.println("xbox360mediaserve, Copyright (C) 2006 Thomas Walker\n"+
				"xbox360mediaserve comes with ABSOLUTELY NO WARRANTY; for details see license\n"+
				"This is free software, and you are welcome to redistribute it\n"+
		"under certain conditions; see license for details.\n");


		
		
		String pattern= "%d{ISO8601}::%p: %C{1}:  %m %n";		
		ConsoleAppender appender = new ConsoleAppender(new PatternLayout(pattern));		
		logger.addAppender(appender);
		logger.info("OS Detected:"+System.getProperty("os.name"));
		logger.setLevel(Level.DEBUG);
		logger.info("Started Logger");
		
		MediaServer mediaServer;
		if(args.length>0){										
			// if we are given an address then use it
			try{
				logger.debug("About to start media server on:"+args[0]);
				mediaServer=new MediaServer(InetAddress.getByName(args[0]).getHostAddress());
			}
			catch(UnknownHostException e)
			{
				logger.fatal("Address supplied not valid");
			}
		}
		else{ 
			// use first interface as address
			logger.info("No address provided, will choose first interface");
			mediaServer=new MediaServer();
		}
	}
}
