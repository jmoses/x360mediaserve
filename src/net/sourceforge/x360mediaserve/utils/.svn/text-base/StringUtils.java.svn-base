package net.sourceforge.x360mediaserve.utils;

import java.io.UnsupportedEncodingException;


public class StringUtils {
	public static String getHtmlString(String inputString){
		String result;
		try {
			result=java.net.URLEncoder.encode(inputString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result=inputString.replace("&","&amp;");
			result.replace("ö","&ouml;");
			result.replace("ï","&iuml;");
		}
		return result;
	}
	
	public static String decodeHtmlString(String inputString)
	{
		String result;
		try{
			result=java.net.URLDecoder.decode(inputString, "UTF8");
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result="";
		}
		return result;
	}
	
	public static String encodeStringToUPNP(String string)
	{
		return org.apache.commons.lang.StringEscapeUtils.escapeXml(string);
	}
	
	public static String getTimeString(long time){
		String result="";
		long hrs=(time/(1000*60*60));
		result=result+hrs+":";
		long mins=(time%(1000*60*60))/(1000*60);
		if(mins<10) result=result+"0"+mins+":";
		else result=result+mins+":";
		long seconds=(time%(1000*60*60))%(1000*60)/1000;
		if(seconds<10) result=result+"0"+seconds+".";
		else result=result+seconds+".";
		long ms=(time%(1000*60*60))%(1000*60)%1000;
		if(ms==0) result=result+"000";
		else if(ms<10) result=result+"00"+ms;
		else if(ms<100) result=result+"0"+ms;
		else result=result+ms;
		
		return result;
	}
}