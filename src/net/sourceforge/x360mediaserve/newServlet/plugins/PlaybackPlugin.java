package net.sourceforge.x360mediaserve.newServlet.plugins;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;

public interface PlaybackPlugin extends Plugin{

	public abstract void onStartedPlaying(MediaItem item);

	public abstract void onInterruptedPlaying(MediaItem item);

	public abstract void onFinishedPlaying(MediaItem item);

}