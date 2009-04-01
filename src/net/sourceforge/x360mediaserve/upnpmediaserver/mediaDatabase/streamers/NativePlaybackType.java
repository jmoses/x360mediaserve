package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers;

import java.io.File;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.PlaybackInformation;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Type;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;

public class NativePlaybackType implements PlaybackType {

	
	String outputMimeType;
	String outputExtension;
	
	public NativePlaybackType(Type type)
	{
		this.outputMimeType=type.mimeType;
		this.outputExtension=type.defaultExtension;
	}
	
	public Streamer createStreamerForMediaItem(MediaItem item) {
		// TODO Auto-generated method stub
		Native streamer=new Native();
		streamer.setMimeType(outputMimeType);
		System.out.println("Playing back:"+item.location);
		File file=new File(item.location);
		streamer.setSizeOfContent(file.length());
		streamer.setFile(file);
		
		return streamer;
	}

	public PlaybackInformation getPlaybackInformation(MediaItem item) {
		// TODO Auto-generated method stub
		PlaybackInformation result=new PlaybackInformation();
		result.canPlay=true;
		result.mimeType=item.mimeType;
		result.extension=this.outputExtension;
		
		if(item instanceof AudioItem)
		{
		AudioItem audioItem=(AudioItem)item;
		result.audioChannels=audioItem.nativeNrAudioChannels;
		result.bitRate=audioItem.nativeBitRate;
		result.bitsPerSample=audioItem.nativeBitsPerSample;		
		result.sampleRate=audioItem.nativeSampleFrequency;
		}
		
		return result;
		
						
	}

}
