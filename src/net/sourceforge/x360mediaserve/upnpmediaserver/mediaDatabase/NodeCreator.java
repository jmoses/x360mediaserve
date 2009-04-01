package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AlbumItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ArtistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ContainerItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.DirectoryItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ImageItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.PlaylistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.ContainerNode;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.SongNode;
import net.sourceforge.x360mediaserve.utils.StringUtils;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;

public class NodeCreator {
	protected static Logger logger = Logger.getLogger("x360mediaserve");
	static String HTTP_GET = "http-get";
	FormatHandler formatHandler;
	
	public NodeCreator(FormatHandler formatHandler)
	{
		this.formatHandler=formatHandler;
	}
	

	public ContentNode getContentNodeForAudio(String ServerAddress,AudioItem audioItem,String idToUse,String pathToUse,String parentID)
	{
		// should probably identify device here to allow for choosing transcoding type
		SongNode result=new SongNode();								
		//result.setArtist(artist.getName().replace("&","&amp;"));	
		result.setArtist(StringUtils.encodeStringToUPNP(audioItem.artistString));
		//result.setTitle(this.getTitle().replace("&","&amp;"));
		result.setTitle(StringUtils.encodeStringToUPNP(audioItem.title));
		result.setRestricted(1);
		result.setParentID(parentID);
		result.setID(idToUse);
		result.setUPnPClass("object.item.audioItem.musicTrack");		
		//result.setAlbum(album.getName().replace("&","&amp;"));
		result.setAlbum(StringUtils.encodeStringToUPNP(audioItem.albumString));
		result.setGenre("Unknown");		
		PlaybackInformation playbackInformation=this.formatHandler.getPlaybackInformationForAudioItem(audioItem);		
		AttributeList atrrlist=new AttributeList();
		atrrlist.add(new Attribute("duration",StringUtils.getTimeString(audioItem.playLength)));		
		
		String protocol=HTTP_GET + ":*:" + playbackInformation.mimeType + ":*";
		atrrlist.add(new Attribute("bitrate",""+playbackInformation.bitRate));
		atrrlist.add(new Attribute("sampleFrequency",""+playbackInformation.sampleRate));
		if(playbackInformation.bitsPerSample!=-1) atrrlist.add(new Attribute("bitsPerSample",""+playbackInformation.bitsPerSample));
		atrrlist.add(new Attribute("nrAudioChannels",""+playbackInformation.audioChannels));
		String url="http://"+ServerAddress+"/service/ContentDirectory/Music/"+pathToUse+"."+playbackInformation.extension;
		result.setResource(url,protocol,atrrlist);										
		
		return result;						
	}
	
	public SongNode getBrowseSummaryContentForContainer(ContainerItem containerItem,String idToUse,String parentToUse){
		SongNode result=new SongNode();						
		result.setTitle(StringUtils.encodeStringToUPNP(containerItem.name));		
		result.setRestricted(1);		
		result.setParentID(parentToUse);		
		result.setID(idToUse);
		result.setUPnPClass("object.container.storageFolder");
									
		return result;
	}
	
	public ContainerNode getContentNodeForDirectory(String type,DirectoryItem dirItem,String idToUse)
	{
		ContainerNode result=new ContainerNode();

		result.setTitle(dirItem.dir.getName());		
		result.setRestricted(1);
		//result.setArtist(artist.getName().replace("&","&amp;"));
		if(dirItem.parentId==null) result.setParentID(""+type);
		else result.setParentID(type+"/"+dirItem.parentId);
		result.setID(idToUse);
		result.setUPnPClass("object.container.storageFolder");							
		return result;
	}
	
	public ContentNode getContentNodeForImage(String ServerAddress,ImageItem image,String idToUse,String pathToUse){
		SongNode result=new SongNode();
				
		result.setTitle(StringUtils.encodeStringToUPNP(image.title));
		result.setRestricted(1);
		result.setParentID((int)16);
		result.setID(idToUse);
		result.setUPnPClass("object.item.imageItem.photo");		
		
		PlaybackInformation playbackInformation=this.formatHandler.getPlaybackInformationForImageItem(image);

		if(playbackInformation.canPlay){
			String protocol=HTTP_GET + ":*:" + playbackInformation.mimeType + ":*";
			AttributeList atrrlist=new AttributeList();
			// these are probably not all needed or correct but it seems to work, should be properly worked out at some point										
			result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+pathToUse+"."+playbackInformation.extension,protocol,atrrlist);
		}			
		logger.debug(result);
		return result;
	}
	
	
	
	public ContentNode getContentNodeForVideo(String ServerAddress,VideoItem videoItem,String idToUse,String pathToUse)
	{
		// should probably identify device here to allow for choosing transcoding type
		SongNode result=new SongNode();		
							
		result.setTitle(StringUtils.encodeStringToUPNP(videoItem.title));
		result.setRestricted(1);
		result.setParentID((int)4);
		result.setID(idToUse);
		result.setUPnPClass("object.item.videoItem");							
		
		AttributeList atrrlist=new AttributeList();
		
		PlaybackInformation playbackInformation=this.formatHandler.getPlaybackInformationForVideoItem(videoItem);
		
		if(playbackInformation.canPlay)
		{
		atrrlist.add(new Attribute("size",""+playbackInformation.size));		
		atrrlist.add(new Attribute("duration",StringUtils.getTimeString(playbackInformation.playLength)));	
		String protocol=HTTP_GET + ":*:" + playbackInformation.mimeType + ":*";						
		String url="http://"+ServerAddress+"/service/ContentDirectory/Music/"+pathToUse+"."+playbackInformation.extension;
		result.setResource(url,protocol,atrrlist);										
		}
		
		return result;						
	}
	
	public SongNode getSummaryContentNodeForArtist(ArtistItem artist,String idToUse){
		SongNode result=new SongNode();						
		result.setTitle(StringUtils.encodeStringToUPNP(artist.name));
		result.setAttribute("childCount",""+artist.numberOfAlbums);
		result.setParentID(6);
		result.setID(idToUse);
		result.setUPnPClass("object.container.person.musicArtist");		
		return result;
	}
	
	public SongNode getSummaryContentNodeForAlbum(AlbumItem albumItem,String idToUse){
		SongNode result=new SongNode();						
		result.setTitle(StringUtils.encodeStringToUPNP(albumItem.name));
		result.setAttribute("childCount",""+albumItem.numberOfSongs);
		result.setRestricted(1);
		//result.setArtist(artist.getName().replace("&","&amp;"));
		result.setArtist(StringUtils.encodeStringToUPNP(albumItem.artist));
		result.setParentID((int)7);
		result.setID(idToUse);
		result.setUPnPClass("object.container.album.musicAlbum");							
		return result;
	}
	
	public SongNode getSummaryContentNodeForPlaylist(PlaylistItem playlist,String idToUse)
	{
		SongNode result=new SongNode();						
		result.setTitle(StringUtils.encodeStringToUPNP(playlist.name));
		result.setAttribute("childCount",""+playlist.numberOfItems);
		result.setRestricted(1);		
		result.setParentID("F");
		result.setID(idToUse);
		result.setUPnPClass("object.container.playlistContainer");							
		return result;
	}
	
	
	
	
}
