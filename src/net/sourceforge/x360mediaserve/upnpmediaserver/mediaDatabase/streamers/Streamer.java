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


package net.sourceforge.x360mediaserve.upnpmediaserver.mediaDatabase.streamers;

import java.io.File;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/** Interface to handle writing a file out in a particular format
 * @author tom
 *
 */
public interface Streamer {
	
	static Logger logger = Logger.getLogger("x360mediaserve");
//	public void writePCMtoStream(File file,OutputStream os);
//	public void writeMP3toStream(File file,OutputStream os);
//	public void writeWMAtoStream(File file,OutputStream os);

	//function to write file to stream
	public boolean setStartPoint(long startIndex);
	public void setSizeOfContent(long size);
	public long getSizeOfContent();
	public void setFile(File file);
	public boolean writeToStream(OutputStream os);
	//function to clean up any mess left
	public void cleanUp();
	public boolean supportsRanges();
	public String getContentType();
}
