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
import java.io.OutputStream;

/** Handles files that are already in the correct format
 * @author tom
 *
 */
public class Native implements Streamer {
	
	/** Copies a given file to the OutputStream
	 * @param file
	 * @param os
	 */
	File file;
	long startPoint=0;
	String mimeType;
	
	public Native()
	{
		
	}
	
	public void setMimeType(String type)
	{
		this.mimeType=type;
	}
	
	public void setFile(File file)
	{
		this.file=file;
	}
	
	public boolean setStartPoint(long index)
	{
		this.startPoint=index;
		return true;
	}
	
	public boolean writeToStream(OutputStream os){
		BufferedInputStream is=null;
		boolean success=false;
		try{
			System.out.println("Playing:"+file+" from "+this.startPoint);
			is=new BufferedInputStream(new FileInputStream(file));
			if(this.startPoint!=0) is.skip(this.startPoint);
			byte input[]=new byte[4096];
			int bytesread;
			long totalbytes=0;
			while((bytesread=is.read(input))!=-1){				
				os.write(input,0,bytesread);
				totalbytes+=bytesread;
			}
			os.flush();			
			
//			while((bytesread=is.read(input))!=-1){
//				
//				os.write(input,0,bytesread);
//			}
			
			System.out.println("End of File Reached, wrote:"+totalbytes);
			System.out.println("End of File Reached, wrote:"+is.read());
			success=true;						
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		finally
		{
			if(is!=null) 
				try{
					is.close();
				}
			catch(Exception e){
				
			}
		}
		return success;
	}
	
	public void cleanUp()
	// reading from file so nothing to fix
	{
		
	}
	
	public void setSizeOfContent(long size)
	{		
	}
	
	public long getSizeOfContent()
	{
		return file.length();
	}
	
	public boolean supportsRanges()
	{
		return true;
	}
	
	public String getContentType()
	{
		return this.mimeType;
	}
	

}
