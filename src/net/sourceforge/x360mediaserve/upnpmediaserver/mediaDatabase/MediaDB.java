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


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.formats.identifiers.ExtensionIdentifier;
import net.sourceforge.x360mediaserve.formats.tagging.EntaggedTagger;
import net.sourceforge.x360mediaserve.formats.tagging.FileTagger;
import net.sourceforge.x360mediaserve.formats.tagging.MP4Tagger;
import net.sourceforge.x360mediaserve.formats.tagging.VideoFileTagger;
import net.sourceforge.x360mediaserve.newServlet.plugins.PlaybackPlugin;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.addons.ITunesDBImporter;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.backends.java.JavaBack;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Format;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Type;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AlbumItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ArtistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ContainerItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.DirectoryItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.PlaylistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins.BrowsePlugin;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins.FileSystemBrowsingPlugin;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins.TVShowBrowsingPlugin;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.NativePlaybackType;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.Streamer;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers.TranscodingPlaybackType;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.SongNode;

import org.apache.log4j.Logger;
import org.cybergarage.upnp.media.server.object.ContentNodeList;


/** Basic Music Store
 * @author Tom
 *
 */
public class MediaDB {

	static Logger logger = Logger.getLogger("x360mediaserve");
	
	File photoDir=null;
	File videoDir=null;

	public JavaBack backend=new JavaBack();


	public FormatHandler formatHandler=new FormatHandler();
	
	public static String dbPath="db";
	
	//long dbOffset=10000000; // first 10 million ids are reserved for use by program for dirs etc
	long nextLocalContainerid=100;
	
	HashMap<Long,DirectoryItem> dirMap=new HashMap<Long,DirectoryItem>();
	HashMap<File,DirectoryItem> dirFileMap=new HashMap<File,DirectoryItem>(); 
	
	HashMap<String,BrowsePlugin> browsePluginsMap=new HashMap<String,BrowsePlugin>();
	HashMap<String,PlaybackPlugin> playbackPluginsMap=new HashMap<String,PlaybackPlugin>();
	
	ITunesDBImporter iTunesDBImporter;
	
	public long nextLocalContainerID()
	{
		return (this.nextLocalContainerid++);
	}
	
	public void reset(){
			this.iTunesDBImporter=new ITunesDBImporter(this);	
	}
	
	public NodeCreator nodeCreator;


	public MediaDB(){
		reset();

		try {
			if(System.getProperty("os.name").toLowerCase().contains("windows")){
				formatHandler.setScriptDir(new File((new File(".").getCanonicalPath())+"\\ScriptDir"));							
			}
			else{
				formatHandler.setScriptDir(new File((new File(".").getCanonicalPath())+"/ScriptDir"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				
		}
		initFormats();
		this.nodeCreator=new NodeCreator(this.formatHandler);
		addBrowsePlugins();


		addArtist("");
		//addStreamPlaylist();
		backend.addPlaylist("All Music");
	}

	// add album to the DB , probably needs to be updated to handle compilations
	
	
	long pluginPath=0;
	String nextPluginPath()
	{
		return Long.toHexString(java.util.Calendar.getInstance().getTimeInMillis()+(pluginPath++));		
	}
	
	public void addPlaybackPlugin(PlaybackPlugin plugin)
	{
		this.playbackPluginsMap.put(plugin.getID(), plugin);
	}
	
	private void addBrowsePlugins()
	{
		
		{
			String path=nextPluginPath();
		TVShowBrowsingPlugin plugin=new TVShowBrowsingPlugin(path,path,this);
		this.browsePluginsMap.put(path, plugin);
		}
		{
			String path=nextPluginPath();
			FileSystemBrowsingPlugin plugin=new FileSystemBrowsingPlugin(path,path,this);
			this.browsePluginsMap.put(path, plugin);
		}
	}
	
	
	/** Setup formats
	 * 
	 */
	private void initFormats(){
		
		Type jpgType=new Type();
		jpgType.friendlyName="Jpeg";
		jpgType.defaultExtension="jpg";
		jpgType.mimeType="image/jpeg";		
		formatHandler.addType(jpgType);
		
		Type aviType=new Type();
		aviType.friendlyName="avi";
		aviType.defaultExtension="avi";
		aviType.mimeType="video/avi";
		formatHandler.addType(aviType);
		
		Type mp3Type=new Type();
		mp3Type.friendlyName="mp3";
		mp3Type.defaultExtension="mp3";
		mp3Type.mimeType="audio/mpeg";
		formatHandler.addType(mp3Type);
		
		Type pcmType=new Type();
		pcmType.friendlyName="pcm";
		pcmType.defaultExtension="pcm";
		pcmType.mimeType="audio/L16";
		formatHandler.addType(pcmType);
		
		Type m4atype=new Type();
		m4atype.friendlyName="m4a";
		m4atype.defaultExtension="m4a";
		m4atype.mimeType="audio/m4a";
		formatHandler.addType(m4atype);
		
		Type tiffType=new Type();
		tiffType.friendlyName="tiff";
		tiffType.defaultExtension="tif";
		tiffType.mimeType="image/tiff";
		formatHandler.addType(tiffType);
		
		Type mp4Type=new Type();
		mp4Type.friendlyName="mp4";
		mp4Type.defaultExtension="mp4";
		mp4Type.mimeType="video/mp4";
		formatHandler.addType(mp4Type);
		
		Type flacType=new Type();
		flacType.friendlyName="flac";
		flacType.defaultExtension="flac";
		flacType.mimeType="audio/flac";
		
						
		
		{
			Format mp3=new Format();
			mp3.setNativeType(mp3Type);
			mp3.setIdentifier(new ExtensionIdentifier("mp3"));
			mp3.setTagger(new EntaggedTagger());
			mp3.addPlaybackForType(mp3Type, new NativePlaybackType(mp3Type));
			mp3.setCanCarryAudio(true);
			mp3.setITunesKind("MPEG audio file");
			formatHandler.addAudioFormat(mp3);
		}
		
		{
			Format aac=new Format();
			aac.setNativeType(m4atype);
			aac.setIdentifier(new ExtensionIdentifier("m4a"));
			aac.setTagger(new MP4Tagger());
			aac.setCanCarryAudio(true);		
			TranscodingPlaybackType m4amp3=new TranscodingPlaybackType(mp3Type,"m4amp3",formatHandler.scriptDir.getAbsolutePath());
			m4amp3.setBitRate(320);
			aac.addPlaybackForType(mp3Type, m4amp3);
			aac.setITunesKind("AAC audio file");
			formatHandler.addAudioFormat(aac);
		}
		
				
		{
			Format avi=new Format();
			avi.setNativeType(aviType);
			avi.setIdentifier(new ExtensionIdentifier("avi"));			
			avi.setTagger(new VideoFileTagger());
			avi.setCanCarryVideo(true);
			avi.addPlaybackForType(aviType, new NativePlaybackType(aviType));
			formatHandler.addFormat(avi);
		}
		
		{
			Format mp4=new Format();
			mp4.setNativeType(mp4Type);
			mp4.setIdentifier(new ExtensionIdentifier("mp4"));			
			mp4.setTagger(new VideoFileTagger());
			mp4.setCanCarryVideo(true);
			mp4.addPlaybackForType(mp4Type, new NativePlaybackType(mp4Type));
			formatHandler.addFormat(mp4);
		}

		
		{
			Format jpg=new Format();
			jpg.setNativeType(jpgType);
			jpg.setIdentifier(new ExtensionIdentifier("jpg"));
			jpg.setTagger(new FileTagger());
			jpg.setCanCarryImage(true);
			jpg.addPlaybackForType(jpgType, new NativePlaybackType(jpgType));
			formatHandler.addFormat(jpg);
		}
		
		{
			Format tiff=new Format();
			tiff.setNativeType(tiffType);
			tiff.setIdentifier(new ExtensionIdentifier("tif"));
			tiff.setTagger(new FileTagger());
			tiff.setCanCarryImage(true);
			tiff.addPlaybackForType(tiffType, new NativePlaybackType(tiffType));
			formatHandler.addFormat(tiff);
		}
	}

	


	/** Add an Artist
	 * @param artistString Artist's name
	 * @return The Artist
	 */
	private Object addArtist(String artistString){
		return backend.addArtist(artistString);
	}

	private Object addAlbum(String albumString, Object artistRef)
	{
		return backend.addAlbum(albumString, artistRef);
	}

	// function to add Song to library
	/** Add Song 
	 * @param file 
	 * @return
	 */
	private AudioItem addAudioFile(File file){
		AudioItem audioItem=this.formatHandler.getAudioForFile(file);
		if(audioItem!=null)
			return addAudioItem(audioItem);
		else return null;
	}

	/** Add song to DB
	 * @param newsong AudioItem to add
	 * @return
	 */
	public AudioItem addAudioItem(AudioItem newsong){
		
		if(newsong.artistString==null) newsong.artistString="";
		//String artistString=newsong.artistString;
		Object artist=backend.findArtistByString(newsong.artistString);				
		if(artist==null){ // if the artist wasn't found in the library
			artist=addArtist(newsong.artistString);
		}
		
		if(newsong.albumString==null) newsong.albumString="";
		//String albumString=newsong.albumString;
		
		Object album=backend.findAlbumByString(newsong.albumString);
		if(album==null){
			logger.info("Adding album:"+newsong.albumString+" by "+newsong.artistString);
			album=addAlbum(newsong.albumString,artist);
		}				
					
		newsong.albumRef=album;	
		newsong.artistRef=artist;		
		AudioItem audioRef=backend.addAudio(newsong);
		PlaylistItem playlist=backend.getPlaylistByName("All Music");
		backend.addTrackToPlaylist(playlist, audioRef);


		return audioRef;
	}
	

	


	public void addDirectory(File directory)
	{
		if(directory.isDirectory())
		{
			try {
				logger.debug("Got directory:"+directory.getCanonicalPath());
				// get Audio Files
				for(File audioFile:directory.listFiles(this.formatHandler.getAudioFilter()))
				{
					if(audioFile.canRead())
					{
						this.addAudioFile(audioFile);
					}
				}
				// get Video Files
				// get Image Files
				for(File nextDir:directory.listFiles(this.directoryFilter))
				{
					if(nextDir.canRead())
					{
						addDirectory(nextDir);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addVideoDirectory(File directory)
	{
		if(directory.isDirectory())
		{
			try {
				logger.debug("Got directory:"+directory.getCanonicalPath());
				// get Audio Files
				for(File videoFile:directory.listFiles(this.formatHandler.getVideoFilter()))
				{
					if(videoFile.canRead())
					{
						backend.addVideo(formatHandler.getVideoForFile(videoFile));
						//this.addAudioFile(audioFile);
					}
				}
				// get Video Files
				// get Image Files
				for(File nextDir:directory.listFiles(this.directoryFilter))
				{
					if(nextDir.canRead())
					{
						addVideoDirectory(nextDir);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	public void playMediaWithExtension(String path,HttpServletRequest req,HttpServletResponse resp)
	{
		// uses http stuff so maybe should move into content dir
		logger.debug("Playing media:"+path);
		// fetch media
		MediaItem mediaItem;
		Streamer decoder;
		if(path.startsWith(dbPath))
		{
			logger.debug("Getting media from db:"+path);
			path=path.substring(dbPath.length()+1);
			String idString=path.substring(0,path.indexOf("."));
			String extension=path.substring(path.lastIndexOf(".")+1);
			long containerID=Long.parseLong(idString);			
			mediaItem=backend.getMediaForID(containerID);
			decoder=this.formatHandler.getStreamerForMediaItem(mediaItem,extension);
		}
		else if(path.contains("/")){ // we have a plugin
			logger.debug("Getting media from plugin");
			String extension=path.substring(path.lastIndexOf(".")+1);
			String pluginString=path.substring(0,path.indexOf("/"));
			BrowsePlugin plugin=this.browsePluginsMap.get(pluginString);
			mediaItem=plugin.getMediaForID(path.substring(path.indexOf("/")+1,path.lastIndexOf(".")));
			decoder=this.formatHandler.getStreamerForMediaItem(mediaItem,extension);
		}
		else // use the normal db
		{
			logger.debug("Shouldn't be here path is:"+path);
			String idString=path.substring(0,path.indexOf("."));
			String extension=path.substring(path.lastIndexOf(".")+1);
			long containerID=Long.parseLong(idString);			
			//mediaItem=backend.getMediaForID(containerID-this.dbOffset);
			mediaItem=backend.getMediaForID(containerID);
			decoder=this.formatHandler.getStreamerForMediaItem(mediaItem,extension);									
		}
				
		
		// get decoder for extension
					
		resp.setContentType(decoder.getContentType());		
		if(decoder.supportsRanges())
		{
			logger.debug("Decoder supports ranges");
			resp.setHeader("Accept-Ranges","bytes");
		}					
		// set content type and length
		long size=decoder.getSizeOfContent();
		String range=req.getHeader("RANGE");
		long index=0;
		if(range!=null) 
		{
			index=Long.parseLong(range.substring(6, range.length()-1));
			resp.setHeader("CONTENT-RANGE", "bytes "+index+"-"+size+"/"+size);
		}
		else
		{
			//if we have a range we dont want to notify that we started playing?
			for(PlaybackPlugin plugin:this.playbackPluginsMap.values())
			{
				plugin.onStartedPlaying(mediaItem);
			}
		}
		resp.setHeader("Content-Length", ""+(size-index));
		decoder.setStartPoint(index);
		// start streaming
		
		BufferedOutputStream os=null;
		try
		{
			// better to reuse buffers from some sort of queue?
			os=new BufferedOutputStream(resp.getOutputStream(),10000);
			decoder.writeToStream(os);
			os.close();
			
			for(PlaybackPlugin plugin:this.playbackPluginsMap.values())
			{
				plugin.onFinishedPlaying(mediaItem);
			}
		}
		catch(org.mortbay.jetty.EofException e)
		{
			logger.debug("Client hung up");
			for(PlaybackPlugin plugin:this.playbackPluginsMap.values())
			{
				plugin.onInterruptedPlaying(mediaItem);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
				
		}
				
	}



	public int  performFullSearch(ContentNodeList contentlist,String searchstring){
		logger.debug("Full search being done but not implemented!");
		int numberOfItems=0;
		if(searchstring.contains("upnp:artist")){							
			String tmp=searchstring.substring(searchstring.indexOf("upnp:artist"));
			tmp=tmp.substring(tmp.indexOf("\"")+1);
			tmp=tmp.substring(0,tmp.indexOf("\""));
			logger.info("got "+tmp+" from "+searchstring);
			List<AlbumItem> list=backend.getAlbumsForArtistString(tmp);
			numberOfItems=list.size();
			for(AlbumItem albumItem:list){
				logger.debug("Adding album:"+albumItem.name);
				contentlist.add(this.nodeCreator.getSummaryContentNodeForAlbum(albumItem, this.dbPath+"/"+albumItem.id));				
			}
		}
		return numberOfItems;
	}
	
	private static String encodeStringToUPNP(String string)
	{
		return org.apache.commons.lang.StringEscapeUtils.escapeHtml(string);
	}
	

		
	

	public int getHackSearchList(ContentNodeList contentlist,String containerID,String searchCriList,String serverAddress,int startIndex,int endIndex)
	{
		//int startIndex=0;
		//int endIndex=100;

		int numberOfItems=0;

		if(containerID.equals("1")){		
			return performFullSearch(contentlist,searchCriList);
		}
		else if(containerID.equals("4")){
			//case 4:
			logger.debug("Getting Songs using search");
			numberOfItems=backend.getNumberOfAudio();
			for(AudioItem audioItem: backend.getAudio(startIndex, endIndex)){
				logger.debug("Adding:"+audioItem);
				contentlist.add(this.nodeCreator.getContentNodeForAudio(serverAddress, audioItem, this.dbPath+"/"+audioItem.id, this.dbPath+"/"+audioItem.id,"4"));
			}
		}
		else if(containerID.equals("5"))
		{
			logger.debug("Browsing Genres");
		}
		else if(containerID.equals("6")){
			//case 6: // artists
			logger.debug("Getting artists using search");
			numberOfItems=backend.getNumberOfArtists();
			for(ArtistItem artistItem:backend.getArtists(startIndex, endIndex)){								
				contentlist.add(this.nodeCreator.getSummaryContentNodeForArtist(artistItem, this.dbPath+"/"+artistItem.id));
			}
		}
		else if(containerID.equals("7")){
			//case 7: // albums	
			logger.debug("Getting albums using search");
			numberOfItems=backend.getNumberOfAlbums();
			for(AlbumItem albumItem:backend.getAlbums(startIndex, endIndex)){
				logger.debug("Adding album:"+albumItem.name);				
				contentlist.add(this.nodeCreator.getSummaryContentNodeForAlbum(albumItem, this.dbPath+"/"+albumItem.id));
			}
		}
		else if(containerID.equals("8")){ // video stuff
			logger.info("Video search requested but not implemented");
		}
		else if(containerID.equals("F")){
			logger.debug("Getting Playlists");
			numberOfItems=backend.getNumberOfPlaylists();
			for(PlaylistItem playlist:backend.getPlaylists(startIndex,endIndex))
			{
				contentlist.add(this.nodeCreator.getSummaryContentNodeForPlaylist(playlist,this.dbPath+"/"+playlist.id));
			}						
		}
		else{
			//default:
			containerID=containerID.substring(this.dbPath.length()+1);
			long containerNumber=Long.parseLong(containerID);
			logger.debug("Getting content "+containerNumber);
			
			List<MediaItem> items=backend.getContentForContainer(containerNumber);
			numberOfItems=items.size();
			logger.debug("Got "+items.size()+" items");
			for(MediaItem item: items)
			{
				if(item instanceof AudioItem)
				{					
					AudioItem audioItem=(AudioItem)item;
					contentlist.add(this.nodeCreator.getContentNodeForAudio(serverAddress, (AudioItem)item, this.dbPath+"/"+item.id, this.dbPath+"/"+item.id,this.dbPath+"/"+audioItem.albumID));
				}
			}

		}
		return numberOfItems;
	}
	

	FileFilter directoryFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};


	
	public DirectoryItem addDirectory(File file,String parent){ // add file to the database

		if(file.isDirectory()){ // if it is a directory then add its children			
			try{
				long id=nextLocalContainerID();
				//Directory dir=new Directory(file,parent);
				//dir.setContainerID(nextContainerID());
				DirectoryItem dir=new DirectoryItem();
				dir.dir=file;
				dir.parentId=parent;
				dir.id=Long.toString(id);
				dirMap.put(id, dir);
				
				//containerMap.put(dir.getContainerID(),dir);
				//fileContainerMap.put(file.toString(), dir.getContainerID());
				logger.debug("added Directory:"+file.toString()+"   "+id);
				return dir;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}	
		return null;
	}
	
	
	
	
	
	public DirectoryItem getDirectoryForFile(File file,String parent)
	{
		
		DirectoryItem dir=this.dirFileMap.get(file);		
		if(dir==null){
			logger.debug("Directory id was null,adding");
			dir=addDirectory(file,parent);
			//id=dirIdMap.get(file.toString());
		}
		if(dir==null) return null;
		//return (Directory)containerMap.get(id);
		return dir;
	}
	
	public SongNode getBrowseSummaryContentForContainer(String prefix,ContainerItem containerItem){
		SongNode result=new SongNode();						
		result.setTitle(encodeStringToUPNP(containerItem.name));		
		result.setRestricted(1);		
		result.setParentID((int)16);
		result.setID(prefix+"/"+containerItem.id);
		result.setUPnPClass("object.container.storageFolder");									
		return result;
	}
	
	public static String VIDEO_PREFIX="15";
	public static String IMAGE_PREFIX="16";
		

	public void getBrowseList(ContentNodeList contentlist,String containerID,String serverAddress)
	{
		if(containerID.startsWith(VIDEO_PREFIX))
		{			
			logger.info("Browsing Videos");
			containerID=containerID.substring((VIDEO_PREFIX.length()));
			if(containerID.startsWith("/")){
				containerID=containerID.substring(1);
				{
					logger.debug("Browsing Plugins");
					String[] strings=containerID.split("/", 2);
					logger.info("Browsing Plugins:"+strings[0]+"   "+strings[1]);
					BrowsePlugin plugin=this.browsePluginsMap.get(strings[0]);
					if(plugin!=null)
					{
						logger.debug("Found Plugin:"+plugin.getName());
						contentlist.addAll(plugin.getVideoContentForPath(strings[1], VIDEO_PREFIX+"/"+strings[0], serverAddress));
					}
					
				}
			}
			else
			{				
				logger.info("Browsing Video Root");
				for(BrowsePlugin plugin:this.browsePluginsMap.values())
				{
					if(plugin.hasVideo()){
					ContainerItem container=new ContainerItem();
					container.name=plugin.getName();
					container.id=plugin.getPath()+"/";
					contentlist.add(this.getBrowseSummaryContentForContainer(VIDEO_PREFIX, container));
					}
				}
				
				// send video root
			}
		}
		else if(containerID.startsWith(IMAGE_PREFIX))
		{
			containerID=containerID.substring((IMAGE_PREFIX.length()));
			if(containerID.startsWith("/"))
			{
				containerID=containerID.substring(1);
				{
					logger.debug("Browsing Image Plugins");
					String[] strings=containerID.split("/", 2);
					logger.info("Browsing Plugins:"+strings[0]+"   "+strings[1]);
					BrowsePlugin plugin=this.browsePluginsMap.get(strings[0]);
					if(plugin!=null)
					{
						logger.debug("Found Plugin:"+plugin.getName());
						contentlist.addAll(plugin.getImageContentForPath(strings[1], IMAGE_PREFIX+"/"+strings[0], serverAddress));
					}
					
				}
			}
			else
			{
				logger.info("Browsing Image Root");
				for(BrowsePlugin plugin:this.browsePluginsMap.values())
				{
					if(plugin.hasImages()){
					ContainerItem container=new ContainerItem();
					container.name=plugin.getName();
					container.id=plugin.getPath()+"/";
					contentlist.add(this.getBrowseSummaryContentForContainer(IMAGE_PREFIX, container));
					}
				}
			}
		}


	}

	public void setScriptDir(File f){
		formatHandler.setScriptDir(f);
	}






	public void setPCMOption(boolean option){
	//	formatHandler.setUsePCM(option);
	}
	
	public void setVideoDirectory(File dir)
	{
		videoDir=dir;
		this.addVideoDirectory(videoDir);
	}
	
	public File getVideoDirectory()
	{
		return videoDir;		
	}
	
	public File getImageDirectory()
	{
		return this.photoDir;		
	}

	public void setPhotoDirectory(File dir)
	{
		photoDir=dir;
	}


	
	// Function graveyard	
	public void addItunesDataBase(File file)
	{
		this.iTunesDBImporter.readiTunesXML(file);
	}				
}
