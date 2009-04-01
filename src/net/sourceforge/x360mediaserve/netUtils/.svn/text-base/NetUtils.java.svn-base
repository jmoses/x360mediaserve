package net.sourceforge.x360mediaserve.netUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

/** NetUtils - Class containing useful network statics
 * @author Tom
 *
 */
public class NetUtils {
	
	static Logger logger = Logger.getLogger("x360mediaserve");
	public static String getExternalAddress()
	{
		String ipAddress=null;
		
			try {
				java.util.Enumeration<NetworkInterface> interfaces=NetworkInterface.getNetworkInterfaces();
				NetworkInterface testnetworkInterface=null;

				while(interfaces.hasMoreElements() && ipAddress==null){
					testnetworkInterface=interfaces.nextElement();
					Enumeration<InetAddress> addresses=testnetworkInterface.getInetAddresses();
					while(addresses.hasMoreElements() && ipAddress==null){
						InetAddress address=addresses.nextElement();						
						// ignore loopback and ipv6 addresses
						if(!address.isLoopbackAddress() && address.getAddress().length==4)
						{
						ipAddress=address.getHostAddress();
						logger.debug("Testing interface:"+ipAddress);
						logger.debug("Interface size:"+address.getAddress().length);																		
						}

					}							
				}
				logger.info("Got Interface:"+ipAddress);
			} catch (SocketException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
			}


		return ipAddress;
	}
}
