package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.plugins;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.MediaDB;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ContainerItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.DirectoryItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.ImageItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.MediaItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.items.VideoItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.ContainerNode;

import org.cybergarage.upnp.media.server.object.ContentNodeList;

public class FileSystemBrowsingPlugin extends BrowsePlugin {
	HashMap<File,DirectoryItem> dirFileMap=new HashMap<File,DirectoryItem>();
	HashMap<Long,DirectoryItem> dirMap=new HashMap<Long,DirectoryItem>();
	static String FILE_PREFIX="files";
	
	public FileSystemBrowsingPlugin(String browsePath, String mediaPath,
			MediaDB mediaDB) {
		super(browsePath, mediaPath, mediaDB);
		this.name="Browse Filesystem";
		this.hasVideo=true;
		this.hasImages=true;
		logger.info("Loaded plugin");
	}

	@Override
	public ContentNodeList getVideoContentForPath(String path, String prefix,
			String serverAddress) {
		ContentNodeList contentlist=new ContentNodeList();
		logger.info("Browsing Video Files:"+path);
		File videoDir=mediaDB.getVideoDirectory();
		if(videoDir!=null)
		{
			boolean browsingFilesOnly=false;
			if(path.startsWith(this.FILE_PREFIX))
			{
				path=path.substring(this.FILE_PREFIX.length()+1);
				browsingFilesOnly=true;
			}
			DirectoryItem videoDirObject=null;
			if(path.length()==0){
				videoDirObject=getDirectoryForFile(videoDir,null);							
			}
			else
			{
				//String containerIDString=containerID.substring(3);
				videoDirObject=dirMap.get(Long.parseLong(path));					
			}					
			if(videoDirObject!=null)
			{
				// list directories
				File[] directories=videoDirObject.dir.listFiles(this.directoryFilter);
				File[] files=videoDirObject.dir.listFiles(this.mediaDB.formatHandler.getVideoFilter());
				
				if(!browsingFilesOnly)
				{						
					logger.debug("Not just browsing files");
					if(files.length>0 && directories.length>0) // dir has both files and folders so need to provide a way to get it
					{
						logger.debug("Path contains files and directories");
						ContainerItem containerItem=new ContainerItem();
						ContainerItem container=new ContainerItem();
						container.name="Files In this Directory";
						container.id=prefix+"/"+FILE_PREFIX+"/"+path;
						contentlist.add(mediaDB.nodeCreator.getBrowseSummaryContentForContainer(container,container.id,prefix+"/"+path));
					}
					
					for(File directory:directories)
					{
						DirectoryItem dirItem=this.getDirectoryForFile(directory, videoDirObject.id);
						if(dirItem!=null)
						{
							contentlist.add(this.getContentNodeForDirectory(mediaDB.VIDEO_PREFIX,dirItem,prefix+"/"+dirItem.id));
						}
					}
				}
				if(browsingFilesOnly || directories.length==0)
				{
					for(File videoFile:files)
					{
						logger.info("Adding video:"+videoFile);
						
						MediaItem videoItem=this.mediaDB.backend.getItemForFile(videoFile);
						if(videoItem==null)
						{
							VideoItem video=this.mediaDB.formatHandler.getVideoForFile(videoFile);
							logger.info("Adding video:"+video.location+" "+video.title);
							videoItem=mediaDB.backend.addVideo(video);							
						}
						VideoItem videoItem2=(VideoItem)videoItem;
						contentlist.add(this.mediaDB.nodeCreator.getContentNodeForVideo(serverAddress, videoItem2,prefix+"/"+videoItem2.id,this.mediaPath+"/"+videoItem2.id));
					}
				}
			}
		}	
		return contentlist;
	}
	
	public ContentNodeList getImageContentForPath(String path,String prefix,String serverAddress)
	{
		ContentNodeList contentlist=new ContentNodeList();
		logger.info("Browsing Image Files:"+path);
		File imageDir=mediaDB.getImageDirectory();
		
		if(imageDir!=null)
		{
			DirectoryItem imageDirObject=null;
			if(path.length()==0){
				imageDirObject=getDirectoryForFile(imageDir,null);							
			}
			else
			{
				//String containerIDString=containerID.substring(3);
				imageDirObject=dirMap.get(Long.parseLong(path));					
			}					
			if(imageDirObject!=null)
			{
				for(File directory:imageDirObject.dir.listFiles(this.directoryFilter))
				{
					DirectoryItem dirItem=this.getDirectoryForFile(directory, imageDirObject.id);
					if(dirItem!=null)
					{
						contentlist.add(this.getContentNodeForDirectory(mediaDB.IMAGE_PREFIX,dirItem,prefix+"/"+dirItem.id));
					}
				}
				for(File imageFile:imageDirObject.dir.listFiles(this.mediaDB.formatHandler.getImageFilter()))
				{
					logger.info("Adding image:"+imageFile);
					
					MediaItem imageItem=this.mediaDB.backend.getItemForFile(imageFile);
					if(imageItem==null)
					{
						ImageItem image=this.mediaDB.formatHandler.getImageForFile(imageFile);
						logger.info("Adding image:"+image.location+" "+image.title);
						imageItem=mediaDB.backend.addImage(image);							
					}
					ImageItem videoItem2=(ImageItem)imageItem;
					contentlist.add(this.mediaDB.nodeCreator.getContentNodeForImage(serverAddress, videoItem2,prefix+"/"+videoItem2.id,this.mediaPath+"/"+videoItem2.id));
				}		
			}
		}	
		return contentlist;
	}

	@Override
	public MediaItem getMediaForID(String id) {
		// TODO Auto-generated method stub
		logger.debug("Getting Media for id:"+id);
		return this.mediaDB.backend.getMediaForID(Long.parseLong(id));
	}
	
	long nextLocalContainerid=1;
	public long nextLocalContainerID()
	{
		return (this.nextLocalContainerid++);
	}
	
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
	
	FileFilter directoryFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	
	public DirectoryItem getDirectoryForFile(File file,String parent)
	{
		
		DirectoryItem dir=this.dirFileMap.get(file);		
		if(dir==null){
			logger.debug("Directory id was null,adding");
			dir=addDirectory(file,parent);
			//id=dirIdMap.get(file.toString());
		}
		//if(dir==null) return null;
		//return (Directory)containerMap.get(id);
		return dir;
	}
	
	private ContainerNode getContentNodeForDirectory(String type,DirectoryItem dirItem,String idToUse)
	{		
		ContainerNode result=new ContainerNode();

		result.setTitle(dirItem.dir.getName());		
		result.setRestricted(1);
		if(dirItem.parentId==null) result.setParentID(type);
		else result.setParentID(type+"/"+dirItem.parentId);
		result.setID(idToUse);
		result.setUPnPClass("object.container.storageFolder");							
		return result;
	}

}
