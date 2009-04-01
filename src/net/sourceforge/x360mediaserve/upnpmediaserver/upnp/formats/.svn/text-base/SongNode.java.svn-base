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

package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats;

import java.io.InputStream;

import org.cybergarage.upnp.media.server.object.item.ItemNode;

public class SongNode extends ItemNode{
	
	public final static String ARTIST = "upnp:artist";
	public final static String ALBUM = "upnp:album";
	public final static String PLAYLIST= "upnp:playlist";
	public final static String GENRE = "upnp:genre";
	
	public SongNode(){
		setID(-1);
		setName(NAME);
		
	}
	
	public void setArtist(String name)
	{
		setProperty(ARTIST, name);
	}
	
	public void setPlaylist(String name)
	{
		setProperty(PLAYLIST, name);
	}
	
	public void setAlbum(String album)
	{
		setProperty(ALBUM, album);
	}
	
	public void setGenre(String genre)
	{
		setProperty(GENRE, genre);
	}
	
	public long getContentLength(){
		return 0;
	}
	public InputStream getContentInputStream(){
		return null;
	}
	public String getMimeType(){
		return "";
	}
}
