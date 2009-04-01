package net.sourceforge.x360mediaserve.formats.tagging;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Tag;



public class MP4Tagger implements Tagger{
	HashMap tokens;
	
	public static final String[] CONTAINERS = {
		"moov", "trak", "udta", "tref", "imap",
		"mdia", "minf", "stbl", "edts", "mdra", 
		"rmra", "imag", "vnrp", "dinf","meta","ilst"
	};
	
	public void getTokens(File file,String endToken,Map<String,byte[]> result,Collection<String> tokens){
		try{
			RandomAccessFile rafile=new RandomAccessFile(file,"r");
			getTokens(rafile,0,1000,endToken,result,tokens);
			rafile.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	boolean isContainer(String string){
		for(String str:CONTAINERS){
			if(str.equals(string)) return true;
		}
		return false;
	}
	
	boolean isTokenOfInterest(String string, Collection<String> tokens){
		return tokens.contains(string);
	}
	
	
	
	public boolean getTokens(RandomAccessFile rafile,long start,long end,String stop,Map<String,byte[]> result,Collection<String> tokens) throws IOException{
		// assume we are starting on a token size
		rafile.seek(start);
		while(rafile.getFilePointer()<end){
			byte[] sizebuffer=new byte[4];
			
			rafile.readFully(sizebuffer);
			
			long size=(new BigInteger(sizebuffer)).longValue();

			
			byte[] typ=new byte[4];
			rafile.readFully(typ);
			
			String type=new String(typ,"ISO-8859-1");
			//System.out.println(type+" "+size+" "+rafile.getFilePointer());
			if(size<0 || size>Integer.MAX_VALUE) // if size is invalid
			{
				throw new IOException("Problem with size:"+size);
			}
			
			if(type.equals(stop)) return false;
			else if(isContainer(type))
			{
				if(type.equals("meta")){ // hack to make meta work
					rafile.readInt();
				}
				if(!getTokens(rafile,rafile.getFilePointer(),start+size,stop,result,tokens)){
					return false;
				}
			}
			else if(isTokenOfInterest(type,tokens))
			{
				byte[] object=new byte[(int)size-8];
				//System.out.println("Object of interest:"+type);
				rafile.readFully(object);
				result.put(type,object);
			}
			else // skip the data
			{
				//System.out.println("Skipping:"+size);
				rafile.skipBytes((int)(size-8));
			}
		}
		
		
		return true;
	}
	
	public void process_stts(byte[] dat,Tag tag){
		//System.out.println("stts");		
		byte[] sizeArray=new byte[4];
		System.arraycopy(dat,4,sizeArray,0,4);
		int numberOfSamples=(new BigInteger(sizeArray)).intValue();
		double length=0;
		int pointer=8;
		for(int i=0;i<numberOfSamples;i++){
			System.arraycopy(dat,pointer,sizeArray,0,4);
			pointer+=4;
			int samplecount=(new BigInteger(sizeArray)).intValue();
			System.arraycopy(dat,pointer,sizeArray,0,4);
			pointer+=4;					
			int sampleduration=(new BigInteger(sizeArray)).intValue();
			length+=(double)samplecount*((double)sampleduration)/(double)(tag.getSamplerate());
		}
		//System.out.println("Got time"+length+" "+tag.getSamplerate());
		tag.setTime((long)(1000*length));
	}
	
	public Tag getTag(File file){
		
		Tag tag= new Tag();
		
		try{
		HashSet<String> hashset=new HashSet<String>();
		hashset.add("©ART"); // artist
		hashset.add("©nam"); // track name
		hashset.add("©wrt"); // writer
		hashset.add("©alb"); // album 
		hashset.add("©day"); // year
		hashset.add("©too"); // ???
		hashset.add("trkn");
	//	hashset.add("gnre");
		hashset.add("disk");
		hashset.add("stsd");
		hashset.add("stts");
		//hashset.add("st");
//		hashset.add("covr"); //cover art
		//System.out.println("Hashset:"+hashset.size());
		
		HashMap<String,byte[]> result=new HashMap<String,byte[]>();
		MP4Tagger parse=new MP4Tagger();
		parse.getTokens(file,"mdat",result,hashset);
		boolean stsdprocessed=false;
		byte[] sttsdata=null;
		for(String token:result.keySet()){
			//System.out.println(token);
			if(token.equals("stsd")){
				// currently only need the sample rate from this one
				try{
				//System.out.println("stsd");
				byte[] dat=result.get(token);
				//stsddata=dat;
				byte[] sizeArray=new byte[4];
				System.arraycopy(dat,4,sizeArray,0,4);
				int contcount=(new BigInteger(sizeArray)).intValue();
				//System.out.println("Cont count:"+contcount);
				int pointer=8;
				for(int i=0;i<contcount;i++){
					System.arraycopy(dat,pointer,sizeArray,0,4);
					int size=(new BigInteger(sizeArray)).intValue();
					pointer+=4;
					String str= new String(dat,pointer,4,"ISO-8859-1");
				//	System.out.println(str+" "+size);
					pointer+=4;
					if(str.equals("mp4a")) // if we have found the mp4a block
					{
						System.arraycopy(dat,pointer+22,sizeArray,0,4);
						int rate=(new BigInteger(sizeArray)).intValue();
					//	System.out.println("rate:"+rate);
						tag.setSamplerate(rate);
					}
				}
				if(sttsdata!=null) process_stts(sttsdata,tag);
				stsdprocessed=true;
				}
				catch(Exception e){
					System.out.println(e.toString());
					System.out.println("Couldnt decode the stsd block");
				}
				
			}
			else if(token.equals("stts")){
				sttsdata=result.get(token);
				if(stsdprocessed){
					process_stts(sttsdata,tag);
				}
				
			}
			else if(token.contains("©")){
				try{
					byte[] dat=result.get(token);
					String data=new String(dat,16,dat.length-16,"ISO-8859-1");
					//System.out.println(token+" "+data);
					if(token.equals("©ART")) tag.setArtist(data);
					else if(token.equals("©day")) tag.setYear(data);
					else if(token.equals("©nam")) tag.setTitle(data);
					else if(token.equals("©alb")) tag.setAlbum(data);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			else{ // currently assume its numerical
				byte[] dat=result.get(token);
				int num=dat[18];
				num=num<<8;
				num+=dat[19];
				
				if(token.equals("trkn")) tag.setTracknumber(num);
				//System.out.println(token+" "+num);
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			tag.fillBlanks(file);
		}
		return tag;
	}
	
	public static void main(String[] args){
		File file=new File(args[0]);
		MP4Tagger tagger=new MP4Tagger();
		System.out.println(tagger.getTag(file).toString());
		
	}
	
	
	
	
}
