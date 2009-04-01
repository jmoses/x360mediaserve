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

package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Format;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Tag;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Type;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ImageItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.PlaybackType;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.Streamer;


public class FormatHandler {
	// need to be able to find format based on Mime type
	// and on extension
	static Logger logger = Logger.getLogger("x360mediaserve");
	
	ArrayList<Format> formats=new ArrayList<Format>();	
	ArrayList<Format> videoFormats=new ArrayList<Format>();
	ArrayList<Format> audioFormats=new ArrayList<Format>();
	ArrayList<Format> imageFormats=new ArrayList<Format>();
	ArrayList<Format> mixedFormats=new ArrayList<Format>();
	
	HashMap<String,Format> formatsMimeMap=new HashMap<String,Format>();
	HashMap<String,Type> typesExtensionMap=new HashMap<String,Type>();

	
	public Collection<Format> getFormats(){
		return formats;
	}
	
	public void addFormat(Format format){
		formats.add(format);
		formatsMimeMap.put(format.nativeType.mimeType, format);
		if(format.isVideo())
		{
			videoFormats.add(format);
		}
		if(format.isPhoto())
		{
			imageFormats.add(format);
		}
		format.setHandler(this);
	}
	
	public void addAudioFormat(Format format)
	{
		addFormat(format);
		audioFormats.add(format);
	}
	
	public void addType(Type type)
	{
		typesExtensionMap.put(type.defaultExtension, type);
	}
	
	File scriptDir=null;

	public File getScriptDir() {
		return scriptDir;
	}

	public void setScriptDir(File scriptDir) {
		this.scriptDir = scriptDir;
	}
	
	public Format getFormatFromItunesKind(String kind){
		for(Format format:formats){
			if(kind.equals(format.getITunesKind())){
				return format;
			}
		}
		return null;
	}
	
	
	public FileFilter getVideoFilter()
	{
		FileFilter videoFilter = new FileFilter() {
			public boolean accept(File file) {
				for(Format format:videoFormats)
				{
					if(format.isFormat(file)) return true;
				}
				return false;
			}
		};
		return videoFilter;
	}
	
	
	
	public FileFilter getAudioFilter()
	{
		FileFilter audioFilter = new FileFilter() {
			public boolean accept(File file) {
				for(Format format:audioFormats)
				{
					if(format.isFormat(file)) return true;
				}
				return false;
			}
		};
		return audioFilter;
	}
	
	public FileFilter getImageFilter()
	{
		FileFilter imageFilter = new FileFilter() {
			public boolean accept(File file) {
				for(Format format:imageFormats)
				{
					if(format.isFormat(file)) return true;
				}
				return false;
			}
		};
		return imageFilter;
	}
	
	public PlaybackInformation getPlaybackInformationForAudioItem(AudioItem audioItem)
	{
		PlaybackInformation result=new PlaybackInformation();	
		Format fileFormat=formatsMimeMap.get(audioItem.mimeType);
		if(fileFormat==null) 
		{
			// if we have info on the format (could still have files in db) then we cant play it
			result.canPlay=false;
		}
		else
		{
			
			//Determine if we are transcoding the file
			// if we are use transcoders
			if(fileFormat.getPlaybackForType(typesExtensionMap.get("mp3"))!=null)
			{				
				PlaybackType playback=fileFormat.getPlaybackForType(typesExtensionMap.get("mp3"));
				return playback.getPlaybackInformation(audioItem);
																				
			}
			// if not spit out native information
			else
			{				
				result.canPlay=true;
				result.audioChannels=audioItem.nativeNrAudioChannels;
				result.bitRate=audioItem.nativeBitRate;
				result.bitsPerSample=audioItem.nativeBitsPerSample;
				result.extension=fileFormat.getNativeType().defaultExtension;				
				result.mimeType=audioItem.mimeType;
				result.sampleRate=audioItem.nativeSampleFrequency;				
			}
		}		
		return result;
	}
	
	public PlaybackInformation getPlaybackInformationForVideoItem(VideoItem videoItem)
	{
		PlaybackInformation result=new PlaybackInformation();
		Format fileFormat=formatsMimeMap.get(videoItem.mimeType);
		if(fileFormat==null)
		{
			result.canPlay=false;
		}
		else
		{
			result.canPlay=true;
			result.extension=fileFormat.getNativeType().defaultExtension;				
			result.mimeType=videoItem.mimeType;
			result.size=(new File(videoItem.location)).length();
			result.playLength=videoItem.playLength;			
		}
		return result;
	}
	
	public PlaybackInformation getPlaybackInformationForImageItem(ImageItem imageItem)
	{
		PlaybackInformation result=new PlaybackInformation();
		Format fileFormat=formatsMimeMap.get(imageItem.mimeType);
		if(fileFormat==null)
		{
			result.canPlay=false;
		}
		else
		{
			result.canPlay=true;
			result.extension=fileFormat.getNativeType().defaultExtension;				
			result.mimeType=imageItem.mimeType;
			result.size=(new File(imageItem.location)).length();			
		}
		return result;
	}
	
	public Streamer getStreamerForMediaItem(MediaItem item,String extension)
	{
		Format fileFormat=formatsMimeMap.get(item.mimeType);
		Type outputType=this.typesExtensionMap.get(extension);
		return fileFormat.getPlaybackForType(outputType).createStreamerForMediaItem(item);
	}
	
	public Format getFormatForFile(File file)
	{
		for(Format format:formats)
		{
			if(format.isFormat(file)) return format;
		}		
		// else
		return null;
	}
	
	public AudioItem getAudioForFile(File file)
	{
		Format format=getFormatForFile(file);
		if(format!=null)
		{
			Tag tag=format.getTag(file);
			AudioItem result=new AudioItem();
			result.mimeType=format.nativeType.mimeType;
			//result.location=file.getAbsolutePath();
			result.location=file.toURI();
			logger.info(result.location);
			
			result.nativeBitRate=tag.getBitrate();
			result.nativeSampleFrequency=tag.getSamplerate();
			result.size=file.length();
			result.nativeNrAudioChannels=tag.getNumberOfChannels();
			
			result.playLength=tag.getTime();
			
			result.artistString=tag.getArtist();			
			result.albumString=tag.getAlbum();
			result.title=tag.getTitle();
			result.year=tag.getYear();
			result.albumOrder=tag.getTracknumber();
			return result;
		}
		else
		{
			return null;
		}
		
	}
	
	public VideoItem getVideoForFile(File file)
	{
		logger.info("Getting video for:"+file);
		Format format=getFormatForFile(file);
		if(format!=null && format.isVideo())
		{		
			Tag tag=format.getTag(file);
			VideoItem result=new VideoItem();
			result.mimeType=format.nativeType.mimeType;
			//result.location=file.getAbsolutePath();
			result.location=file.toURI();
			logger.debug(result.location);
			
					
			result.size=file.length();																		
			result.title=tag.getTitle();
			result.isTVShow=tag.isTVShow;
			if(result.isTVShow)
			{
				result.showName=tag.showName;
				result.seasonNumber=tag.seasonNumber;
				result.episodeNumber=tag.episodeNumber;				
			}
						
			return result;
		}
		else
		{
			logger.error("No applicable format for:"+file);
			return null;
		}
		
	}
	
	public ImageItem getImageForFile(File file)
	{
		logger.info("Getting video for:"+file);
		Format format=getFormatForFile(file);	
		if(format!=null && format.isPhoto())
		{		
			Tag tag=format.getTag(file);
			ImageItem result=new ImageItem();
			result.mimeType=format.nativeType.mimeType;
			//result.location=file.getAbsolutePath();
			result.location=file.toURI();
			logger.debug(result.location);
			
					
			result.size=file.length();																		
			result.title=tag.getTitle();
						
			return result;
		}
		else
		{
			logger.error("No applicable format for:"+file);
			return null;
		}
		
	}
	
	
	
	
	
}
