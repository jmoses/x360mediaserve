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

package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems;

public class Tag {
	
	public void fillBlanks(java.io.File file){
		if(artist==null) artist="";
		if(album==null) album="";
		if(title==null) title=file.getName();
		if(year==null || year=="") year="2012";		
	}
	
	String artist;
	String album;
	String title;
	String year;
	int tracknumber=-1; // number of track on the album
	long time=60000; // length in ms
	
	int bitrate=-1;
	int size=-1;
	int samplerate=-1;
	int numberOfChannels=-1;
	
	public boolean isTVShow=false;
	public String showName="";
	public int seasonNumber=0;
	public int episodeNumber=0;
	
	public void setArtist(String artist){
		this.artist=artist;
	}
	
	public void setAlbum(String album){
		this.album=album;
	}
	
	public void setYear(String year){
		this.year=year;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public int getTracknumber() {
		return tracknumber;
	}
	
	public void setTracknumber(int tracknumber) {
		this.tracknumber = tracknumber;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getYear() {
		return year;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getTimeString(){
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

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public int getSamplerate() {
		return samplerate;
	}

	public void setSamplerate(int samplerate) {
		this.samplerate = samplerate;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String toString(){
		String result="";
		if(title!=null){
			result+="Title:"+title+"\n";
		}
		if(artist!=null){
			result+="Artist:"+artist+"\n";
		}
		if(album!=null){
			result+="Album:"+album+"\n";
		}
		if(year!=null){
			result+="Year:"+year+"\n";
		}
		result+="Track:"+tracknumber+"\n";
		result+="Time:"+time+"\n";
		result+="Bit Rate:"+bitrate+"\n";
		result+="Sample Rate:"+samplerate+"\n";
		result+="Size:"+size+"\n";
		
		return result;
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
	}
	
	
	
	
}
