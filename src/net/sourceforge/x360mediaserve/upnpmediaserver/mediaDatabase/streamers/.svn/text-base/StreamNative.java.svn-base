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


package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/** Handles files that are already in the correct format
 * @author tom
 *
 */
public class StreamNative implements StreamStreamer {
	
	/** Copies a given file to the OutputStream
	 * @param file
	 * @param os
	 */
	
	
	public void writeToStream(String urlString,OutputStream os){
		BufferedInputStream input=null;
		
		try{
			URL url=new URL(urlString);
			System.out.println("Opening stream:"+url.toString());
			input=new BufferedInputStream(url.openStream(),4*1024*1024);
			byte[] data=new byte[102400];
			int read;
			while((read=input.read(data))!=-1){
				os.write(data,0,read);
				//System.out.println(read);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if(input!=null)
			{
				
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			}
		}
		//streamPlayer.writeMP3(os,url);
	}

}
