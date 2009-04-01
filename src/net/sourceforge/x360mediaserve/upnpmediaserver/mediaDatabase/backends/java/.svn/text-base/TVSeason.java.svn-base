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

import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.TVSeasonItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;


public class TVSeason extends Container implements Comparable{
	private TVShow tvShow;
	private int season;
	

	protected TreeSet<VideoItem> videos=new TreeSet<VideoItem>(
			new Comparator<VideoItem>(){
				public int compare(VideoItem o1,
						VideoItem o2){
					int cmp=o1.episodeNumber-o2.episodeNumber;
					if(cmp!=0) return cmp;
					else{
						if(o1.title==null) return 1;
						else if(o2.title==null) return -1;
						else return o1.title.compareToIgnoreCase(o2.title);
					}
				}
			}
	);

	

	public TVSeason(TVShow tvShow,int season){		
		this.tvShow=tvShow;
		this.season=season;
	}


	
	public void getNumberOfSeasons()
	{
		
	}
	
	public int getSeason()
	{
		return season;
	}


	public void addEpisode(VideoItem video){
		videos.add(video);
	}	
	
	public int compareTo(Object o)
	{
		if(o instanceof TVSeason)
		{
			return this.season-((TVSeason)o).season;
		}
		else return 0;
	}
	
	protected TVSeasonItem toTVSeasonItem()
	{
		TVSeasonItem item=new TVSeasonItem();
		item.id=""+this.getContainerID();
		item.seasonNumer=this.getSeason();
		item.name="Season "+item.seasonNumer;
		return item;
	}
	
}
