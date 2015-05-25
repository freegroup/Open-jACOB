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

package de.tif.jacob.core.data.impl;

import java.io.Serializable;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.misc.Nullable;
import de.tif.jacob.util.ObjectUtil;

/**
 * This class represents the internal value of jACOB document fields.
 *  
 * @author Andreas Sonntag
 */
public abstract class DataDocument extends DataReference implements Nullable, Serializable
{
	static public transient final String RCS_ID = "$Id: DataDocument.java,v 1.2 2008/09/17 16:15:30 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

  private transient DataDocumentValue value;
  private transient boolean fetched;
  
  private transient boolean nameLazyFetched;
  private transient String name;

  protected DataDocument(String name, byte[] content)
  {
    this.value = DataDocumentValue.create(name, content);
    this.fetched = true;
    this.nameLazyFetched = true;
    this.name = name;
  }

  protected DataDocument()
  {
    this.value = null;
    this.fetched = false;
    this.name = null;
    this.nameLazyFetched = false;
  }
  
	public static DataDocument createByReference(String referenceString)
	{
		return (DataDocument) createInstanceByReference(referenceString);
	}

  protected abstract DataDocumentValue fetchDocumentValue() throws Exception;

  /**
   * Lazy fetch method to avoid fetching Mbytes of document content if not needed.
   * @return
   * @throws Exception
   */
  protected abstract String fetchDocumentName() throws Exception;

  /**
   * Returns the document name.
   * 
   * @return document name.
   */
  public final String getName()
  {
    try
    {
      if (!this.fetched)
      {
        if (!this.nameLazyFetched)
        {
          this.name = fetchDocumentName();
          this.nameLazyFetched = true;
        }
        return this.name;
      }
      return this.value == null ? null : this.value.getName();
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
	/**
	 * Returns the document content.
	 * 
	 * @return document content.
	 */
	public final byte[] getContent()
  {
    try
    {
      if (!this.fetched)
      {
        this.value = fetchDocumentValue();
        this.fetched = true;
      }
      return this.value == null ? null : this.value.getContent();
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

	/**
	 * Returns the document value.
	 * 
	 * @return the document value
	 */
	public DataDocumentValue getValue()
  {
    try
    {
      if (!this.fetched)
      {
        this.value = fetchDocumentValue();
        this.fetched = true;
      }
      return this.value;
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.misc.Nullable#isNull()
	 */
	public boolean isNull()
	{
		return this.value == null && this.fetched == true;
	}

	protected abstract String toReference(IDataTableRecord record, String fieldName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject)
  {
    if (this == anObject)
    {
      return true;
    }
    if (anObject instanceof DataDocument)
    {
      DataDocument other = (DataDocument) anObject;

      if (this.fetched && other.fetched)
      {
        return ObjectUtil.equals(this.value, other.value);
      }

      // otherwise if not fetched -> return false for performance reasons
    }
    return false;
  }
}
