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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/** Uses external commands to output a file in a compatible stream type
 * @author tom
 *
 */
public class External implements Streamer {
	
	private String command;
	private String scriptDir;
	private long contentSize=Long.MAX_VALUE;
	private File file;
	private String mimeType;
	
	
	/**
	 * @param command The command in the script directory to be executed
	 * @param handler The format handler
	 */
	public External(String command,String scriptDir,String mimeType,File file){
		this.command=command;
		this.scriptDir=scriptDir;
		this.mimeType=mimeType;
		this.file=file;
	}
	
	/** Simple thread that reads from a stream as fast as it can, needed to prevent stderr buffer from getting full and blocking the decoder
	 * @author tom
	 *
	 */
	private class Consumer implements Runnable{
		
		BufferedInputStream inputStream;
		
		
		public Consumer(InputStream is){
			inputStream=new BufferedInputStream(is);
		}
		
		public void run(){
			if(inputStream!=null){
				try{
					byte[] input=new byte[4096];
					while(inputStream.read(input)!=-1){				
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
				finally
				{
					try{
						inputStream.close();
					}
					catch(IOException e){
						
					}
					
					
				}
				
			}
		}
		
	}
	
	protected String getCommand(){
		return command;
	}
	
	public void setSizeOfContent(long size)
	{
		this.contentSize=size;
	}
	
	protected void setCommand(String command){
		this.command=command;
	}
	
	protected File getScriptDir(){
		return new File(this.scriptDir);
	}
	
	public boolean writeToStream(OutputStream os){
		System.out.println("Writing stream for file "+file);
		Process p=null;
		BufferedInputStream is=null;
		
		try{
			String convertcommand;
			if(System.getProperty("os.name").toLowerCase().contains("windows")){
				convertcommand=this.getScriptDir().toString()+"\\"+getCommand()+".bat";							
			}
			else{
				convertcommand=this.getScriptDir().toString()+"/"+getCommand();							
			}
			
			String []cmd={convertcommand,"\""+file+"\""};
			byte input[]=new byte[1000];
			
			System.out.println(convertcommand);
			System.out.println(file.toString());
			
			p=Runtime.getRuntime().exec(cmd,null,this.getScriptDir());
			
			is=new BufferedInputStream(p.getInputStream());;			
			BufferedInputStream es=new BufferedInputStream(p.getErrorStream());;					
			
			Consumer a=new Consumer(es);
			new Thread(a).start();
			
			
			
			
			int count;			
			while((count=is.read(input))!=-1){	
				os.write(input,0,count);
			}
			
			
			return true;
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		finally{
			if(p!=null) p.destroy();
			try{
				if(is!=null) is.close();
			}
			catch(Exception e){
				
			}
		}
		return false;
	}
	
	public boolean setStartPoint(long startIndex)
	{
		return false;
	}

	
	public long getSizeOfContent()
	{
		return this.contentSize;
	}
	
	public void setFile(File file)
	{
		this.file=file;
	}
	
	//function to clean up any mess left
	public void cleanUp()
	{
		
	}
	public boolean supportsRanges()
	{
		return false;
	}
	
	public String getContentType()
	{
		return this.mimeType;
	}
	
	
}
