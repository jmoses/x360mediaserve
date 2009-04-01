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
import java.io.InputStream;
import java.io.OutputStream;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.FormatHandler;


/** Uses external commands to output a file in a compatible stream type
 * @author tom
 *
 */
public class ExternalFile {
	
	private String command;
	FormatHandler handler=null;
	
	/**
	 * @param command The command in the script directory to be executed
	 * @param handler The format handler
	 */
	public ExternalFile(String command,FormatHandler handler){
		this.command=command;
		this.handler=handler;
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
	

public void writePCMtoStream(File file,OutputStream os){
		
	System.out.println("Writing pcm of "+file.toString());
		Process p=null;
		BufferedInputStream is=null;
		
		try{
			
			String convertcommand;
			String argumentstring;
			if(System.getProperty("os.name").toLowerCase().contains("windows")){
				convertcommand=this.getScriptDir().toString()+"\\"+getCommand()+".bat";							
				argumentstring="\""+file+"\"";
			}
			else{
				convertcommand=this.getScriptDir().toString()+"/"+getCommand();							
				argumentstring=file.getCanonicalPath();
				
			}
			
			String []cmd={convertcommand,argumentstring};
			
			
			byte input[]=new byte[1000];
			
			System.out.println(convertcommand);
			System.out.println(cmd[1]);
			
			p=Runtime.getRuntime().exec(cmd,null,this.getScriptDir());
			
			is=new BufferedInputStream(p.getInputStream());;			
			BufferedInputStream es=new BufferedInputStream(p.getErrorStream());;					
			
			Consumer a=new Consumer(es);
			new Thread(a).start();
			
			Consumer b=new Consumer(is);
			new Thread(b).start();
			
			
			
			FileInputStream fis=new FileInputStream("C:\\temp\\buffer.wmv");
			File testFile=new File("C:\\temp\\buffer.wmv");								
			System.out.println("Reading from file");
			int count;
			long counter=0;	
			while((count=fis.read(input))!=-1){
				os.write(input,0,count);
				counter+=input.length;
				long waitcounter=0;
				while(counter+4096>testFile.length() && waitcounter < 10000){
					waitcounter+=100;
					Thread.sleep(100);
				}
				if(counter%(4096*100)==0) System.out.println(counter);
			}
			
			
			
			
			
						
			
			
			
			
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
	}
	
	public void writeMP3toStream(File file,OutputStream os){
		System.out.println("Writing mp3 to stream for file "+file);
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
	}
	
	public void writeWMAtoStream(File file,OutputStream stream){
		
	}
	
	protected String getCommand(){
		return command;
	}
	
	protected void setCommand(String command){
		this.command=command;
	}
	
	protected File getScriptDir(){
		return handler.getScriptDir();
	}
	
	public void writeToStream(File file,OutputStream os){
		System.out.println("Writing stream for file using buffer"+file);
		Process p=null;
		BufferedInputStream is=null;
		long startTime=System.currentTimeMillis();
		
		try{
			
			File testFile=new File("C:\\temp\\buffer.wmv");		
			if(testFile.exists()) testFile.delete();
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
			
			
			
			Consumer b=new Consumer(is);
			new Thread(b).start();
			
			
			while(!testFile.exists()){
				Thread.sleep(100);
			}
			
			FileInputStream fis=new FileInputStream("C:\\temp\\buffer.wmv");
									
			System.out.println("Reading from file");
			
			
			long waitcounter=0;
			while(input.length+1>testFile.length() && waitcounter < 10000){
				waitcounter+=100;
				Thread.sleep(100);
			}
			
			int count;
			long counter=0;	
			while((count=fis.read(input))!=-1){
				os.write(input,0,count);
				counter+=count;
				waitcounter=0;
				while(counter+input.length+1>testFile.length() && waitcounter < 10000){
					waitcounter+=100;
					Thread.sleep(100);
				}
				if(counter>1000000&&(counter/1000000)==0) System.out.println("Wrote:"+counter+" bytes in time:"+"Time taken:"+(System.currentTimeMillis()-startTime)/1000);
			}
			System.out.println("Wrote:"+counter+" bytes in time:"+"Time taken:"+(System.currentTimeMillis()-startTime)/1000);
			
			
			
		}
		catch(Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			System.out.println("Time taken:"+(System.currentTimeMillis()-startTime)/1000);
		}
		finally{
			if(p!=null) p.destroy();
			try{
				if(is!=null) is.close();
			}
			catch(Exception e){
				
			}
		}
	}
	
	
}
