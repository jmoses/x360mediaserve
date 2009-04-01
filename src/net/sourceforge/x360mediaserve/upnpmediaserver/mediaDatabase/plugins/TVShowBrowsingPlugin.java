package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.MediaDB;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.TVSeasonItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.TVShowItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.media.server.object.ContentNodeList;

public class TVShowBrowsingPlugin extends BrowsePlugin {	
	static String TV_SEASONS_PREFIX="tvseasons";
	static String TV_EPISODES_PREFIX="episodes";
	
	

	public TVShowBrowsingPlugin(String browsePath, String mediaPath,MediaDB mediaDB) {		
		super(browsePath, mediaPath,mediaDB);
		this.name="TV Shows";
		this.hasVideo=true;
		// TODO Auto-generated constructor stub
		logger.info("Loaded Plugin");
	}

	@Override
	public ContentNodeList getVideoContentForPath(String containerID, String prefix,String serverAddress) {
		ContentNodeList contentList=new ContentNodeList();
		
		//containerID=containerID.substring(TV_SHOWS_PREFIX.length()+1);
		logger.info("Browsing TV shows: Path:"+containerID+"  prefix:"+prefix+"  serverAddress:"+serverAddress);
		if(containerID.length()==0)
		{
			logger.debug("Browsing TV shows root");
			for(TVShowItem tvShow:mediaDB.backend.getTVShows(0, 1000))
			{
				contentList.add(this.mediaDB.nodeCreator.getBrowseSummaryContentForContainer(tvShow,prefix+"/"+TV_SEASONS_PREFIX+"/"+tvShow.id,MediaDB.VIDEO_PREFIX));
			}
		}
		else if(containerID.startsWith(TV_SEASONS_PREFIX))
		{
			logger.debug("Getting seasons for show"+containerID);
			containerID=containerID.substring(TV_SEASONS_PREFIX.length()+1);
			//ContainerItem containerItem=backend.get(Long.parseLong(containerID));
			for(TVSeasonItem tvSeasonItem:mediaDB.backend.getSeasonsForShow(Long.parseLong(containerID)))
			{
				contentList.add(this.mediaDB.nodeCreator.getBrowseSummaryContentForContainer(tvSeasonItem,prefix+"/"+TV_EPISODES_PREFIX+"/"+tvSeasonItem.id,MediaDB.VIDEO_PREFIX ));
			}
		}
		else 
		{
			logger.debug("Getting episodes for season:"+containerID);
			containerID=containerID.substring(TV_EPISODES_PREFIX.length()+1);
			for(VideoItem episode:mediaDB.backend.getEpisodesForSeason(Long.parseLong(containerID)))
			{				
				contentList.add(this.mediaDB.nodeCreator.getContentNodeForVideo(serverAddress, episode, prefix+"/"+"/"+TV_EPISODES_PREFIX+"/"+episode.id, this.mediaPath+"/"+episode.id));
			}
		}
		return contentList;
	}

	@Override
	public MediaItem getMediaForID(String id) {
		logger.debug("Getting Media for id:"+id);
		return this.mediaDB.backend.getMediaForID(Long.parseLong(id));
		// TODO Auto-generated method stub	
	}

}
