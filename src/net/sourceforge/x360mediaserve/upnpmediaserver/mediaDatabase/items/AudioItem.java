package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items;



public class AudioItem  extends MediaItem{		
	
	//public Object ref;
	public Object artistRef;
	public Object albumRef;
	
	public String albumID;
	public String artistID;

	public String artistString;
	public String albumString;
	public String year;
	
	public int albumOrder;			
	
	public long playLength;
	
	
	public long nativeBitRate;
	public long nativeSampleFrequency;
	public long nativeBitsPerSample;
	public int nativeNrAudioChannels;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.title+" "+this.artistString+" "+this.albumString+" "+this.playLength+" "+location+" "+this.mimeType;
	}
	
	
	
}
