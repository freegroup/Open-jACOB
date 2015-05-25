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

package de.tif.jacob.core.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;


/**
 * Data document value instances could be obtained by means of
 * <li>{@link IDataRecord#getDocumentValue(int)}
 * <li>{@link IDataRecord#getValue(int)} for jACOB document field types.
 * 
 * @author Andreas Sonntag
 */
public final class DataDocumentValue
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: DataDocumentValue.java,v 1.1 2007/01/19 09:50:46 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.1 $";

  private final String name;
  private final byte[] content;
  
  /**
   * Create a document value instance from a file instance.
   * 
   * @param file
   *          the file instance
   * @return the document value created
   * @throws IOException
   *           if the content of <code>file</code> could not be accessed, i.e.
   *           <code>file</code> does not exist.
   */
  public static DataDocumentValue create(File file) throws IOException
  {
    FileInputStream fileInput = new FileInputStream(file);
    try
    {
      byte[] content = IOUtils.toByteArray(fileInput);

      return create(file.getName(), content);
    }
    finally
    {
      fileInput.close();
    }
  }

  /**
   * Create a document value instance by means of name and binary content.
   * 
   * @param name
   *          the document name
   * @param content
   *          the binary content or <code>null</code>
   * @return the document value created or <code>null</code> if name is not a
   *         valid document name.
   */
  public static DataDocumentValue create(String name, byte[] content)
  {
    return create(name, content, content == null ? 0 : content.length);
  }
  
  /**
   * Create a document value instance by means of name and binary content.
   * 
   * @param name
   *          the document name
   * @param content
   *          the binary content or <code>null</code>
   * @param len
   *          the maximum number of bytes to read from <code>content</code>
   * @return the document value created or <code>null</code> if name is not a
   *         valid document name.
   */
  public static DataDocumentValue create(String name, byte[] content, int len)
  {
    // return null if no proper name specified
    //
    if (null == name)
      return null;

    name = name.trim();
    if (name.length() == 0)
      return null;

    return new DataDocumentValue(name, content, len);
  }
  
  /**
   * Private constructor.
   * 
   * @param name
   *          the document name
   * @param content
   *          the document content
   * @param len
   *          the maximum number of bytes to read from <code>content</code>
   */
  private DataDocumentValue(String name, byte[] content, int len)
  {
    this.name = name;

    if (content != null && len != 0)
    {
      if (len < 0 || len > content.length)
      {
        throw new IndexOutOfBoundsException(Integer.toString(len));
      }

      // clone the array because it is mutable!
      if (len == content.length)
        this.content = (byte[]) content.clone();
      else
      {
        this.content = new byte[len];
        System.arraycopy(content, 0, this.content, 0, len);
      }
    }
    else
    {
      this.content = new byte[0];
    }
  }

  /**
   * Returns the content of this document.
   * <p>
   * Attention: This method does not return a copy of the content. Therefore the
   * returned content should not be modified.
   * <p>
   * Note: If the document is empty, <code>byte[0]</code> will be returned
   * instead of <code>null</code>.
   * 
   * @return the document content.
   */
  public byte[] getContent()
  {
    return this.content;
  }
  
  /**
   * Returns the name of this document.
   * 
   * @return the document name.
   */
  public String getName()
  {
    return this.name;
  }
  
	/**
   * Indicates whether some other object is "equal to" this one.
   * <p>
   * Different instances of this class are equal, if the document name and the
   * content are equal as well.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj instanceof DataDocumentValue)
    {
      DataDocumentValue other = (DataDocumentValue) obj;

      if (this.name.equals(other.name))
      {
        if (this.content.length == other.content.length)
        {
          for (int i = 0; i < this.content.length; i++)
          {
            if (this.content[i] != other.content[i])
              return false;
          }
          return true;
        }
      }
    }
    return false;
  }
  
	/**
	 * Returns the document value as string, i.e. "document [name]" will be returned.
	 * 
	 * @return the key value as string
	 */
  public String toString()
  {
    return "document "+name;
  }
}
