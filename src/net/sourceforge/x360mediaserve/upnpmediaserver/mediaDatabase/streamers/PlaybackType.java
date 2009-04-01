package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.PlaybackInformation;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.AudioItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;

public interface PlaybackType {
	public Streamer createStreamerForMediaItem(MediaItem item);
	public PlaybackInformation getPlaybackInformation(MediaItem item);
}
