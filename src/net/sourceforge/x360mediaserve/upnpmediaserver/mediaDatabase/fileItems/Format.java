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

import java.io.File;
import java.util.HashMap;

import net.sourceforge.x360mediaserve.formats.identifiers.Identifier;
import net.sourceforge.x360mediaserve.formats.tagging.Tagger;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.FormatHandler;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.PlaybackType;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.Streamer;






public class Format {
	
	public Type nativeType;
	private Identifier identifier;
	private Tagger tagger;
	
	private FormatHandler handler=null;
	private String iTunesKind=null;
	
	private HashMap<Type,PlaybackType> playbackTypes=new HashMap<Type,PlaybackType>();
		
	private boolean canCarryAudio=false;
	private boolean canCarryVideo=false;
	private boolean canCarryImage=false;
	
	
	public Type getNativeType()
	{
		return nativeType;
	}
	
	public void setNativeType(Type nativeType){
		this.nativeType=nativeType;
	}
	
	public boolean supportsType(Type type){
		return playbackTypes.get(type)!=null;
	}
	
	public PlaybackType getPlaybackForType(Type type)
	{
		return playbackTypes.get(type);
	}
	
	public void addPlaybackForType(Type type,PlaybackType streamer)
	{
		playbackTypes.put(type,streamer);
	}
	
	public void removeStreamerForType(Type type)
	{
		playbackTypes.remove(type);
	}
	
	public void setIdentifier(Identifier identifier)
	{
		this.identifier=identifier;
	}
	public void setTagger(Tagger tagger)
	{
		this.tagger=tagger;
	}
	
	public void setCanCarryAudio(boolean canCarryAudio)
	{
		this.canCarryAudio=canCarryAudio;
	}
	public void setCanCarryVideo(boolean canCarryVideo)
	{
		this.canCarryVideo=canCarryVideo;
	}
	
	public void setCanCarryImage(boolean canCarryImage)
	{
		this.canCarryImage=canCarryImage;
	}
	
	
	public Format()
	{		
	}
	
	public Format(Identifier identifier,Tagger tagger,FormatHandler handler,Streamer mp3Streamer,Streamer pcmStreamer,Streamer wmaStreamer,Streamer wavStreamer,boolean isSong, boolean isVideo,boolean isPhoto){
		this.identifier=identifier;
		this.tagger=tagger;
		this.handler=handler;
		this.canCarryAudio=isSong;
		this.canCarryVideo=isVideo;
		this.canCarryImage=isPhoto;
	}
	
//	public void writeTypetoStream(Type type,File file,OutputStream os)
//	{
//		Streamer streamer=streamers.get(type);
//		if(streamer!=null) streamer.writeToStream(file, os);	
//	}
	
	public boolean isFormat(File file)
	{
		return identifier.isFormat(file);
	}
	
	public Tag getTag(File file)
	{
		return tagger.getTag(file);
	}
	
	public void setHandler(FormatHandler handler)
	{
		this.handler = handler;
	}
	
	protected FormatHandler getHandler()
	{
		return handler;
	}


	public String getITunesKind()
	{
		return iTunesKind;
	}

	public void setITunesKind(String tunesKind)
	{
		iTunesKind = tunesKind;
	}
	
	public boolean isMusic()
	{
		return this.canCarryAudio;
	}
	
	public boolean isVideo()
	{
		return this.canCarryVideo;
	}
	
	public boolean isPhoto()
	{
		return this.canCarryImage;
	}
		
	
	public long getSize(Type type,File file)
	{
		if(this.nativeType.equals(type))
			return file.length();
		else return Long.MAX_VALUE;
	}
	
	// should these be part of the tag
	/** get length of object in milliseconds
	 * @return
	 */
	public long getTime()
	{
		
		return (long)Integer.MAX_VALUE;
	}
	
	/** Get resolution
	 * @return
	 */
	public int[] getResolution()
	{
		int[] result=new int[2];
		
		return result;
	}
	
}
