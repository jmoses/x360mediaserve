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


package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.addons;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sourceforge.x360mediaserve.plistreader.PlistReader;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.FormatHandler;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.MediaDB;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.fileItems.Tag;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.PlaylistItem;



/** System to import the ItunesDB
 * @author tom
 *
 */
public class ITunesDBImporter{
	static Logger logger = Logger.getLogger("x360mediaserve");
	MediaDB mediaDB;
	
	public ITunesDBImporter(MediaDB mediaDB){
		this.mediaDB=mediaDB;
	}

	HashMap<Long,AudioItem> songs;
	ArrayList<PlaylistItem> playlists;

	public Collection<AudioItem> getSongs(){
		return songs.values();
	}

	public ArrayList<PlaylistItem> getPlaylists(){
		return playlists;
	}
	
	public String getMimeTypeFromItunesKind(String kind)
	{
		if(kind.equals("MPEG audio file"))
		{
			return "audio/mpeg";
		}
		else if(kind.equals("AAC audio file"))
		{
			return "audio/m4a";
		}
		return "";
	}

	private void getSongsFromTrackDictionary(HashMap<String,Object> dictionary){

		long trackid=0;
		for(Object nextobj:dictionary.values()){
			HashMap<String,Object> songDict=(HashMap<String,Object>)nextobj;
			AudioItem song=new AudioItem();
			

			String trackType=null;
			String fileURL=null;
			//NewFormat format=null;
			//String mimeType=null;

			for(String key:songDict.keySet()){
				if(key.equals("Track ID")){
					trackid=(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Name")){
					song.title=(String)(songDict.get(key));
					//tag.setTitle((String)(songDict.get(key)));
				}
				else if(key.equals("Artist")){
					song.artistString=(String)(songDict.get(key));
					//tag.setArtist((String)(songDict.get(key)));
				}
				else if(key.equals("Composer")){

				}
				else if(key.equals("Album")){
					song.albumString=((String)(songDict.get(key)));	
					if(song.albumString.contains("<")){
						song.albumString="";
					}
				}
				else if(key.equals("Genre")){

				}
				else if(key.equals("Kind")){
					song.mimeType=this.getMimeTypeFromItunesKind((String)(songDict.get(key)));
				}
				else if(key.equals("Size")){
					song.size=(((Long)(songDict.get(key))));					
				}
				else if(key.equals("Total Time")){
					song.playLength=(((Long)(songDict.get(key))));					
				}
				else if(key.equals("Disc Number")){

				}
				else if(key.equals("Disc Count")){

				}
				else if(key.equals("Track Number")){
					song.albumOrder=(((Long)(songDict.get(key))).intValue());					
				}
				else if(key.equals("Track Count")){
					//tag.setTracknumber((Integer)(songDict.get(key)));
				}
				else if(key.equals("Year")){
					song.year=(""+(Long)(songDict.get(key)))					;
				}
				else if(key.equals("Date Modified")){

				}
				else if(key.equals("Date Added")){

				}
				else if(key.equals("Bit Rate")){
					song.nativeBitRate=(((Long)(songDict.get(key))).intValue());					
				}
				else if(key.equals("Sample Rate")){
					song.nativeSampleFrequency=(((Long)(songDict.get(key))).intValue());					
				}
				else if(key.equals("Comments")){

				}
				else if(key.equals("Artwork Count")){

				}
				else if(key.equals("Play Count")){

				}
				else if(key.equals("Play Date")){

				}
				else if(key.equals("Play Date UTC")){

				}
				else if(key.equals("Rating")){

				}
				else if(key.equals("Release Date")){

				}
				else if(key.equals("Persistent ID")){

				}
				else if(key.equals("Track Type")){
					trackType=(String)(songDict.get(key));
				}
				else if(key.equals("File Type")){

				}
				else if(key.equals("File Creator")){

				}
				else if(key.equals("Location")){
					fileURL=(String)(songDict.get(key));
				}
				else if(key.equals("File Folder Count")){

				}
				else if(key.equals("Library Folder Count")){
				}
				else{
					String title="";
					if(song.title!=null){
						title=song.title;
					}
					
				}


			}
			// now we need to create the fileitem entry
			if(trackType.equals("File")  && fileURL!=null && song.mimeType!=null) // if its a file and we have a format to decode it
			{ 
				try{
					logger.debug("Adding song from iTunes");
					// remove first instance of localhost/ from sting
					// string hackery
					String newfileURL=fileURL.subSequence(0,7).toString()+fileURL.substring(16);

					URI uri=new URI(newfileURL);

					File file=new File(uri);
					//System.out.println(file.toString());
					if(!file.exists()){
						logger.info("File doesnt exists:" +fileURL);
					}
					else
					{
						song.location=file.toURI();					
						song=mediaDB.addAudioItem(song);
						songs.put(trackid,song);
					}										


				}
				catch(Exception e){					
					e.printStackTrace();
				}
			}
			else
			{
				logger.debug("Invalid item:"+song+" "+trackType+" "+fileURL);
				
			}
		}
	}
	
	private void addItemsToPlaylist(PlaylistItem playlist,ArrayList items)
	{
		for(Object dictobj:items){
			//System.out.println("Reading dictionary");
			HashMap<String,Object> dict=(HashMap<String,Object>)dictobj;
			for(String akey:dict.keySet()){
				if(akey.equals("Track ID")){
					AudioItem dsong;
					if((dsong=songs.get(((Long)(dict.get(akey)))))!=null)
					{
						mediaDB.backend.addTrackToPlaylist(playlist, dsong);												
					}

				}
			}

		}
	}

	private void getPlaylistsFromDictionary(ArrayList playlistArray){		
		logger.debug("Reading playlists");
		for(Object nextobj:playlistArray){
			//long lastPlaylistID=0;
			try // try block so that if the playlist fails to work then it still carries on building the DB
			{					
				PlaylistItem playlist=null;
				ArrayList items=null;
				

				HashMap<String,Object> playlistDict=(HashMap<String,Object>)nextobj;									


				for(String key:playlistDict.keySet()){
					//System.out.println(key);
					if(key.equals("Name")){											
						playlist=mediaDB.backend.addPlaylist((String)(playlistDict.get(key)));
						if(items!=null) addItemsToPlaylist(playlist,items);
						//System.out.println("Playlist Name:"+playlist.getName());
					}																
					else if(key.equals("File Folder Count")){

					}
					else if(key.equals("Playlist ID")){
						//lastPlaylistID=(Long)playlistDict.get(key);
						//System.out.println("Got Playlist ID:"+lastPlaylistID);
					}
					else if(key.equals("Playlist Persistent ID")){

					}
					else if(key.equals("Smart Info")){

					}
					else if(key.equals("Smart Criteria")){

					}
					else if(key.equals("Playlist Items")){
						//System.out.println("Playlist items being read");
						//ArrayList playlistSongs=new ArrayList();
						items=(ArrayList)(playlistDict.get(key));
						if(playlist!=null) this.addItemsToPlaylist(playlist, items);
					}
					else{
						logger.debug("Unhandled Key in playlist dictionary:"+key);
					}


				}


				//playlists.add(playlist);
								
				
			}
			catch(Exception e)
			{
				logger.error("Error in a playlist:");
			}
			// now we need to create the fileitem entry

		}
	}





	public void readiTunesXML(File file){
		
		Object plist=PlistReader.getPlistFromFile(file);		
		if(plist instanceof HashMap){
			songs=new HashMap<Long,AudioItem>();
			playlists=new ArrayList<PlaylistItem>();
			getSongsFromTrackDictionary((HashMap<String,Object>)((HashMap<String,Object>)plist).get("Tracks"));		
			getPlaylistsFromDictionary((ArrayList)((HashMap<String,Object>)plist).get("Playlists"));
			System.out.println("Got:"+songs.size()+" songs and "+playlists.size()+" playlists");
			songs=null;
			playlists=null;
			System.gc();
		}
		else{
			logger.error("Error reading iTunes Xml file:"+file.getAbsolutePath());
		}
	}

}
