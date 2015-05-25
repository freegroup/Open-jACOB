/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.util.upload;



public class MimeBodyPart 
{
	private String name;
	private String contentType;
	private String fileName;
	private byte[] data;
	private boolean isFile;
	private String err = "";

	public MimeBodyPart(String name, byte[] b, String fileName, String contentType) 
	{
		this.name = name;
		this.data = b;
		this.contentType = contentType;
		this.fileName = fileName;
		this.isFile = (fileName != null);
	}

	/**
	 * Return this object's form Name attribute, the same as HTML input name.
	 * @return Return this object's form Name attribute, the same as HTML input name.
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * 
	 * @return If this object's a file, returns a String with its fileName; otherwise, returns default string.
	 */
	public String getFileName() 
	{
		if(isFile) 
		{
			return fileName;
		}
		return "Either this is not a file, but a form text field, or it's empty.";
	}

	/**
	 * 
	 * @return If this object's a file, returns an int with its fileSize; otherwise, returns 0.
	 */
	public int getFileSize() 
	{
		if(isFile && data!=null) 
		{
			return data.length;
		}
		return 0;
	}

	/**
	 * 
	 * @return returns true if this part of the request body a file.
	 */
	public boolean isFile() 
	{
		return isFile;
	}

	/**
	 * 
	 * @return A byte[] array from the data source.
	 */
	public byte[] getBytes() 
	{
		return data;
	}


	/**
	 * 
	 * @return A String representing content-type, or null if it's not a file.
	 */
	public String getContentType() 
	{
		if(isFile) 
			return contentType;
		else 
		  return null;
	}

	public String getErrString() 
	{
		return err;
	}
}