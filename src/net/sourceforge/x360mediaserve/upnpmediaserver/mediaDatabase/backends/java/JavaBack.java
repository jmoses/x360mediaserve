package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.backends.java;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AlbumItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ArtistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ImageItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.PlaylistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.TVSeasonItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.TVShowItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;

import org.apache.log4j.Logger;


public class JavaBack {
	static Logger logger = Logger.getLogger("x360mediaserve");
	
	// generic stuff
	HashMap<Long,MediaItem> mediaMap;
	HashMap<Long,Container> containerMap;
	HashMap<URI,Long> fileContainerMap;
	
	// music stuff
	TreeMap<String,Album> albumMap;
	TreeMap<String,Artist> artistMap;
	
	TreeSet<AudioItem> songSet;
	
	//Playlist streamPlaylist;
	TreeMap<String,Playlist> playlistNameMap;
	HashMap<Long,Playlist> playlists;

	// video stuff
	TreeMap<String,TVShow> tvShowMap;
	TreeSet<VideoItem> videoSet;
	ArrayList<TVShow> tvShows;
	boolean tvShowsUpdated=true;
	
	
	TreeSet<ImageItem> photoSet;
	
	
	
	
	boolean albumsUpdated=true;
	ArrayList<Album> albums;
	
	boolean artistsUpdated=true;
	ArrayList<Artist> artists;
	
	
	
	boolean songsUpdated=true;
	ArrayList<AudioItem> songs;
	
	boolean videosUpdated=true;
	ArrayList<VideoItem> videos;
	
	boolean photosUpdated=true;
	ArrayList<ImageItem> photos;
	
	int nextContainerID=0;
	int nextSongID=0;
	
	public JavaBack()
	{
		reset();
	}
	
	private void reset(){
		tvShowMap=new TreeMap<String,TVShow>();
		containerMap = new HashMap<Long,Container>();
		albumMap = new TreeMap<String,Album>();
		artistMap= new TreeMap<String,Artist>();
		playlists= new HashMap<Long,Playlist>();
		fileContainerMap = new HashMap<URI,Long>();
		songSet=new TreeSet<AudioItem>(			
				new Comparator<AudioItem>(){
					public int compare(AudioItem o1,
							AudioItem o2){
						if(o1.title==null) return 1;
						else if(o2.title==null) return -1;
						else return o1.title.compareToIgnoreCase(o2.title);
					}
				});

		videoSet=new TreeSet<VideoItem>(			
				new Comparator<VideoItem>(){
					public int compare(VideoItem o1,
							VideoItem o2){

						if(o1.title==null) return 1;
						else if(o2.title==null) return -1;
						else return o1.title.compareToIgnoreCase(o2.title);
					}
				});

		photoSet=new TreeSet<ImageItem>(			
				new Comparator<ImageItem>(){
					public int compare(ImageItem o1,
							ImageItem o2){

						if(o1.title==null) return 1;
						else if(o2.title==null) return -1;
						else return o1.title.compareToIgnoreCase(o2.title);
					}
				});

		mediaMap=new HashMap<Long,MediaItem>();
		
		playlistNameMap=new TreeMap<String,Playlist>();
	}
	
	public Object findArtistByString(String str)
	{
		
		return artistMap.get(str.toLowerCase().trim());
	}
	
	public Object findAlbumByString(String albumString)
	{		
		return albumMap.get(albumString.toLowerCase().trim());
	}
	
	/** Create an album with the given name and artist
	 * @param albumString Name of Album
	 * @param artist Artist
	 * @return The created album
	 */
	public Object addAlbum(String albumName, Object artistRef)
	{
		Artist artist=(Artist)artistRef;
		Album album=new Album(albumName,artist);
		
		album.setContainerID(nextContainerID());
		logger.info("Adding album:"+albumName);
		albumMap.put(albumName.toLowerCase().trim(),album);
		containerMap.put(album.getContainerID(),album);
		artist.addAlbum(album);	
		this.albumsUpdated=true;
		return album;
	}
	
	public Object addArtist(String artistString){
		Artist artist=new Artist(artistString);
		logger.info("Adding artist:"+artistString.toLowerCase().trim());
		artist.setContainerID(nextContainerID());
		artistMap.put(artistString.toLowerCase().trim(),artist);
		containerMap.put(artist.getContainerID(),artist);
		this.artistsUpdated=true;
		return artist;
	}
	
	
	
	public PlaylistItem addPlaylist(String playlistName)
	{
		logger.info("Adding playlist:"+playlistName);
		Playlist playlist=new Playlist(playlistName);		
		playlist.setContainerID(nextContainerID());
			
		containerMap.put(playlist.getContainerID(),playlist);
		playlists.put(playlist.getContainerID(), playlist);
		playlistNameMap.put(playlistName.toLowerCase().trim(), playlist);
		logger.debug("Added playlist:"+playlist.getName()+" with id "+playlist.getContainerID());
		return playlist.toPlaylistItem();
	}
	
	public PlaylistItem getPlaylistByName(String name)
	{
		Playlist playlist=this.playlistNameMap.get(name.trim().toLowerCase());
		
		return (playlist==null) ? null : playlist.toPlaylistItem();
	}
	
	
	
	public void addTrackToPlaylist(PlaylistItem playlistItem,AudioItem item)
	{
		logger.info("Adding track to playlist:"+playlistItem.id+" "+item.id);
		Playlist playlist=playlists.get(Long.parseLong(playlistItem.id));
		AudioItem dbitem=(AudioItem)mediaMap.get(item.id);
		logger.debug("Found item:"+dbitem.id);
		logger.debug("Found playlist:"+playlist.getContainerID());
		playlist.addAudioItem(item);
		playlistItem.numberOfItems++;
	}
	
	public void addAudioToAlbum(Object audioRef,Object albumRef)
	{
		AudioItem song=(AudioItem)audioRef;
		Album album=(Album)albumRef;
		album.addSong(song);		
	}
	
	public void addAudioToArtist(Object audioRef,Object artistRef)
	{
		AudioItem song=(AudioItem)audioRef;
		Artist artist=(Artist)artistRef;
		artist.addSong(song);		
	}
	
	public void addAlbumToArtist(Object albumRef,Object artistRef)
	{
		Album album=(Album)albumRef;
		Artist artist=(Artist)artistRef;
		artist.addAlbum(album);		
	}
	
	public AudioItem addAudio(AudioItem audio)
	{
				
		audio.id=new Long(nextSongID());
		Album album=(Album)audio.albumRef;
		album.addSong(audio);

		audio.albumID=""+album.id;
		
		

		Artist artist=(Artist)audio.artistRef;
		artist.addSong(audio);
		
		audio.artistID=artist.id+"";
		
		//make it so all tracks have consistent artist and album names 
		audio.albumString=album.getName();
		audio.artistString=artist.getName();			
		
		mediaMap.put(audio.id,audio);
		fileContainerMap.put(audio.location, audio.id);

		
		songsUpdated=true;
		songSet.add(audio);
		logger.info("Added:"+audio);
		return audio;
	}
	
	public String correctCapitalization(String str)
	{
		String[] words=str.split(" ");
		String result="";
		for(String word:words)
		{			
			result+=Character.toUpperCase(word.charAt(0))+word.substring(1)+" ";						
		}
		return result;
	}
	
	public VideoItem addVideo(VideoItem video)
	{
		video.id=new Long(nextSongID());
		mediaMap.put(video.id, video);
		fileContainerMap.put(video.location, video.id);
		if(video.isTVShow)
		{
			TVShow show=tvShowMap.get(video.showName.toLowerCase());
			if(show==null)
			{
				logger.info("Adding new TV Show:"+video.showName);
				show=new TVShow(correctCapitalization(video.showName));
				show.setContainerID(this.nextContainerID());
				tvShowMap.put(video.showName.toLowerCase(), show);
				containerMap.put(show.id, show);
				this.tvShowsUpdated=true;
			}
			TVSeason season=show.getSeason(video.seasonNumber);
			if(season==null)
			{
				logger.debug("Adding new Season "+video.seasonNumber+" for " +show.getName());
				season=new TVSeason(show,video.seasonNumber);
				show.addSeason(season);
				season.setContainerID(this.nextContainerID());
				containerMap.put(season.id, season);				
			}
			
			String seasonNumberString=Integer.toString(video.seasonNumber);
			if(video.seasonNumber<10)
			{
				seasonNumberString="0"+seasonNumberString;
			}
			String episodeNumberString=Integer.toString(video.episodeNumber);
			if(video.episodeNumber<10)
			{
				episodeNumberString="0"+episodeNumberString;
			}
			
			video.title=show.getName()+" S"+seasonNumberString+"E"+episodeNumberString;
			season.addEpisode(video);
			logger.debug("Adding tv show:"+show.getName()+" "+video.seasonNumber+"E"+video.episodeNumber);
		}
		
		return video;
	}
	
	public ImageItem addImage(ImageItem image)
	{
		image.id=new Long(nextSongID());
		mediaMap.put(image.id, image);
		fileContainerMap.put(image.location, image.id);
		
		return image;		
	}
	
	
	public List<AudioItem> getAudio(int startIndex,int endIndex)
	{		
		if(songsUpdated)
		{
			if(logger.isDebugEnabled()){
				for(AudioItem item: songSet)
				{
					logger.debug(item);
				}
			}
		
			songs=new ArrayList<AudioItem>(songSet);
			songsUpdated=false;
		}
		return songs.subList(startIndex, Math.min(songs.size(),endIndex+1));
	}
	
	public List<ArtistItem> getArtists(int startIndex,int endIndex)
	{
		ArrayList<ArtistItem> result=new ArrayList<ArtistItem>(endIndex+1-startIndex);
		if(artistsUpdated)
		{
			artists=new ArrayList<Artist>(this.artistMap.values());
			artistsUpdated=false;
		}
		for(Artist artist: artists.subList(startIndex, Math.min(artists.size(),endIndex)))
		{
			result.add(artist.toArtistItem());
		}
		return result;
	}
	
	public List<AlbumItem> getAlbums(int startIndex,int endIndex)
	{
		ArrayList<AlbumItem> result=new ArrayList<AlbumItem>(endIndex+1-startIndex);
		if(albumsUpdated)
		{
			albums=new ArrayList<Album>(this.albumMap.values());
			albumsUpdated=false;
		}
		for(Album album: albums.subList(startIndex, Math.min(albums.size(),endIndex+1)))
		{
			result.add(album.toAlbumItem());
		}
		return result;
	}
	
	public List<AlbumItem> getAlbumsForArtistString(String artistString)
	{
		Artist artist=(Artist)this.findArtistByString(artistString);
		ArrayList<AlbumItem> result=new ArrayList<AlbumItem>();
		if(artist==null)
		{
			return result;
		}
		else
		{
			
			for(Album album: artist.albums)
			{
				result.add(album.toAlbumItem());
			}
			return result;
		}
	}
	
	public List<MediaItem> getContentForContainer(Long containerID)
	{
		logger.debug("Getting content for container:"+containerID);
		Container container=containerMap.get(containerID);
		
		if(container instanceof Album)
		{
			logger.debug("Got album:"+((Album)container).getName());
			return new ArrayList<MediaItem>(((Album)container).songs);
		}
		else if(container instanceof Playlist)
		{
			logger.debug("Got playlist");
			return new ArrayList<MediaItem>(((Playlist)container).songs);
		}
		else if(container instanceof Artist)
		{
			logger.debug("Got Artist");
			return new ArrayList<MediaItem>(((Artist)container).songs);
		}
		return new ArrayList<MediaItem>();
	}
	
	public List<TVShowItem> getTVShows(int startIndex,int endIndex)
	{
		ArrayList<TVShowItem> result=new ArrayList<TVShowItem>(endIndex+1-startIndex);
		if(tvShowsUpdated)
		{
			tvShows=new ArrayList<TVShow>(this.tvShowMap.values());
			tvShowsUpdated=false;
		}
		for(TVShow tvshow: tvShows.subList(startIndex, Math.min(tvShows.size(),endIndex+1)))
		{
			result.add(tvshow.toTVShowItem());
		}
		return result;
	}
	
	public List<TVSeasonItem> getSeasonsForShow(long showID)
	{
		logger.debug("Getting seasons for Show:"+showID);
		ArrayList<TVSeasonItem> result=new ArrayList<TVSeasonItem>();
		TVShow show=(TVShow)containerMap.get(showID);
		logger.debug("Got Show:"+show.getName());
		for(TVSeason season:show.getSeasons())
		{			
			logger.debug("Got Season:"+season.getSeason());
			result.add(season.toTVSeasonItem());
		}
		return result;		
	}
	
	public List<VideoItem> getEpisodesForSeason(long seasonID)
	{
		ArrayList<VideoItem> result=new ArrayList<VideoItem>();
		TVSeason show=(TVSeason)containerMap.get(seasonID);
		for(VideoItem episode:show.videos)
		{
			result.add(episode);			
		}
		return result;		
	}
	
	public List<PlaylistItem> getPlaylists(int startIndex,int endIndex)
	{
		ArrayList<PlaylistItem> result=new ArrayList<PlaylistItem>();
		for(Playlist playlist:playlists.values())
		{
			result.add(playlist.toPlaylistItem());
		}
		return result;
	}
	
	public MediaItem getMediaForID(Long id)
	{
		logger.debug("Getting Media for ID:"+id);
		return mediaMap.get(id);
	}
	
	public MediaItem getItemForFile(File file)
	{
		Long id=fileContainerMap.get(file.toString());
		if(id==null) return null;
		else
		{
			return mediaMap.get(id);
		}
	}
	
	private int nextContainerID(){
		return nextContainerID++;
	}

	private int nextSongID(){
		return nextSongID++;
	}
	
	public int getNumberOfArtists()
	{
		return artistMap.size();
	}
	
	public int getNumberOfAlbums()
	{
		return albumMap.size();
	}
	
	public int getNumberOfAudio()
	{
		return songSet.size();
	}
	
	public int getNumberOfPlaylists()
	{
		return playlists.size();
	}
	
	
	
}
