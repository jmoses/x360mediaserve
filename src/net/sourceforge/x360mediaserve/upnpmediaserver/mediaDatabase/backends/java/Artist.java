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



package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.backends.java;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ArtistItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;


public class Artist extends Container{
	String name="";
	
	ArrayList<Album> albums=new ArrayList<Album>();
	
	
	protected TreeSet<AudioItem> songs=new TreeSet<AudioItem>(
			new Comparator<AudioItem>(){
				public int compare(AudioItem o1,
						AudioItem o2){
					// If we're in the same album
					if( o1.albumString.equals( o2.albumString ) ) {
					  // use the albumOrder 
					  int cmp = o1.albumOrder - o2.albumOrder;
					  
					  if( cmp != 0 ) {
					    return cmp;
					  } else if( o1.title == null ) {
					    return 1;
					  } else if( o2.title == null ) {
					    return -1;
					  } else {
					    return o1.title.compareToIgnoreCase( o2.title );
					  }
					} else if( o1.albumString != null && o2.albumString != null ) {
					  return o1.albumString.compareToIgnoreCase( o2.albumString );
					} else {
					  return -1;
					}
				}
			}
	);

		
	public Artist(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
			
	public void addAlbum(Album album){
		albums.add(album);
	}
	
	public void addSong(AudioItem song){
		songs.add(song);
	}
	
	protected ArtistItem toArtistItem()
	{
		ArtistItem artistItem=new ArtistItem();
		artistItem.name=this.name;
		artistItem.numberOfAlbums=this.albums.size();
		artistItem.numberOfSongs=this.songs.size();
		artistItem.id=""+this.id;
		return artistItem;
	}
	
}
