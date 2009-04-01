package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.MediaDB;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.media.server.object.ContentNodeList;

public abstract class BrowsePlugin {
	protected static Logger logger = Logger.getLogger("x360mediaserve");
	
	protected String name;
	protected String browsePath;
	protected String mediaPath;
	protected MediaDB mediaDB;
	
	protected boolean hasVideo=false;
	protected boolean hasImages=false;
	protected boolean hasAudio=false;
	
	
	abstract public MediaItem getMediaForID(String id);

	public ContentNodeList getVideoContentForPath(String path,String prefix,String serverAddress){
		return null;
	}
	
	public ContentNodeList getImageContentForPath(String path,String prefix,String serverAddress)
	{
		return null;
	}
	
	public BrowsePlugin(String browsePath,String mediaPath,MediaDB mediaDB)
	{
		this.browsePath=browsePath;
		this.mediaPath=mediaPath;
		this.mediaDB=mediaDB;
	}
	
	public String getPath()
	{
		return browsePath;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasVideo()
	{
		return this.hasVideo;
	}
	
	public boolean hasImages()
	{
		return this.hasImages;
	}
	
	public boolean hasAudio()
	{
		return this.hasAudio;
	}
	
}
