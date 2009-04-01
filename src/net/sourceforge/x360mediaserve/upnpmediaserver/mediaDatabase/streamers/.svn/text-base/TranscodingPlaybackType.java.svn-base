package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers;

import java.io.File;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.PlaybackInformation;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Type;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;

public class TranscodingPlaybackType implements PlaybackType {
	
	private String command;
	private String path;
	private Type type;
	
	private int bitRate=320;
	private int sampleRate;
	private int audioChannels;
	
	
	
	public TranscodingPlaybackType(Type type,String command,String path)
	{
		this.command=command;
		this.type=type;
		this.path=path;
	}	
	
	public Streamer createStreamerForMediaItem(MediaItem item) {
		// TODO Auto-generated method stub
		File file=new File(item.location);
		External streamer=new External(command,path,type.mimeType,file);
		if(item instanceof AudioItem){
			long length=((AudioItem)item).playLength;
			streamer.setSizeOfContent((length*bitRate*1024)/(8000));
		}
		else streamer.setSizeOfContent(file.length()*5);
		System.out.println("Playing back:"+item.location);						
		return streamer;
	}
	
	public void setBitRate(int rate)
	{
		this.bitRate=rate;
	}

	public PlaybackInformation getPlaybackInformation(MediaItem item) {
		// TODO Auto-generated method stub
		if(item instanceof AudioItem)
		{
			AudioItem audioItem=(AudioItem)item;
			PlaybackInformation result=new PlaybackInformation();
			result.canPlay=true;
			result.audioChannels=this.audioChannels;
			result.bitRate=this.bitRate;
			result.sampleRate=this.sampleRate;
			result.extension=type.defaultExtension;
			result.mimeType=type.mimeType;
			return result;
		}
		else return null;
		
	}

}
