package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.backends.java;

import java.util.Comparator;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.PlaylistItem;

public class Playlist extends Container {
	
	private String name="";	
		
	
	protected TreeSet<AudioItem> songs=new TreeSet<AudioItem>(
			new Comparator<AudioItem>(){
				public int compare(AudioItem o1,
						AudioItem o2){
					int cmp=o1.albumOrder-o2.albumOrder;
					if(cmp!=0) return cmp;
					else{
						if(o1.title==null) return 1;
						else if(o2.title==null) return -1;
						else return o1.title.compareToIgnoreCase(o2.title);
					}
				}
			}
	);
	
	public Playlist(String name)
	{
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public void addAudioItem(AudioItem item)
	{
		logger.debug("Adding track to Playlist");
		songs.add(item);
	}
	
	public int getNumberOfItems()
	{
		return songs.size();	
	}
	
	protected PlaylistItem toPlaylistItem()
	{
		PlaylistItem playlistItem=new PlaylistItem();
		playlistItem.id=Long.toString(this.id);
		playlistItem.name=this.name;	
		playlistItem.numberOfItems=songs.size();
		return playlistItem;
	}
	
	
}
