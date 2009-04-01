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


package net.sourceforge.x360mediaserve.formats.tagging;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Tag;


public class VideoFileTagger implements Tagger {

	static Pattern tvShowPattern=Pattern.compile("(.+)\\.(s?[0-9]+[esx][0-9]+).*",Pattern.CASE_INSENSITIVE);
	static Pattern episodeNumberPattern=Pattern.compile("s?([0-9]+)[esx]([0-9]+)",Pattern.CASE_INSENSITIVE);
	
	static Pattern tvShowPattern2=Pattern.compile("(.+)\\.([0-9]{3,4}).*",Pattern.CASE_INSENSITIVE);	
	
	public Tag getTag(File file) {
		Tag result=new Tag();
		//result.setTitle("Video Test");
		result.setTitle(file.getName());
		result.fillBlanks(file);		
		
		
		//p=Pattern.compile("(.*)");
		Matcher m=tvShowPattern.matcher(file.getName());
		//m=m.groupCount()	
		if(m.matches())
		{
			result.isTVShow=true;
			result.showName=m.group(1).replace(".", " ").trim();
			m=episodeNumberPattern.matcher(m.group(2));
			m.matches();
			result.seasonNumber=Integer.parseInt(m.group(1));
			result.episodeNumber=Integer.parseInt(m.group(2));
			
			String seasonNumberString=Integer.toString(result.seasonNumber);
			if(result.seasonNumber<10)
			{
				seasonNumberString="0"+seasonNumberString;
			}
			String episodeNumberString=Integer.toString(result.episodeNumber);
			if(result.episodeNumber<10)
			{
				episodeNumberString="0"+episodeNumberString;
			}
			result.setTitle(result.showName+" S"+seasonNumberString+"x"+episodeNumberString);
		}
		
		m=tvShowPattern2.matcher(file.getName());
		if(m.matches())
		{
			result.isTVShow=true;
			result.showName=m.group(1).replace(".", " ").trim();
						
			
			if(m.group(2).length()==4)
			{
				result.seasonNumber=Integer.parseInt(m.group(2).substring(0, 2));
				result.episodeNumber=Integer.parseInt(m.group(2).substring(2));
			}
			else 
			{
				result.seasonNumber=Integer.parseInt(m.group(2).substring(0, 1));
				result.episodeNumber=Integer.parseInt(m.group(2).substring(1));
			}
			String seasonNumberString=Integer.toString(result.seasonNumber);
			if(result.seasonNumber<10)
			{
				seasonNumberString="0"+seasonNumberString;
			}
			String episodeNumberString=Integer.toString(result.episodeNumber);
			if(result.episodeNumber<10)
			{
				episodeNumberString="0"+episodeNumberString;
			}
			result.setTitle(result.showName+" S"+seasonNumberString+"x"+episodeNumberString);				
		}
				
		// TODO Auto-generated method stub
		return result;
	}

}
