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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.misc.Nullable;
import de.tif.jacob.util.StringUtil;

/**
 * This class represents the internal value of jACOB long text fields.
 *  
 * @author Andreas Sonntag
 */
public abstract class DataLongText extends DataReference implements Nullable, Serializable
{
	static public transient final String RCS_ID = "$Id: DataLongText.java,v 1.2 2010/06/29 12:39:04 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

	private transient String longText = null;
	private transient boolean fetched = false;

  protected DataLongText(DataLongText other)
  {
    this.longText = other.longText;
    this.fetched = other.fetched;
  }

  protected DataLongText(String longText)
  {
    // treat empty string as null!
    this.longText = "".equals(longText) ? null : longText;
    this.fetched = true;
  }

  protected DataLongText()
	{
		this.longText = null;
		this.fetched = false;
	}
  
  /**
   * IBIS: Wird von mir nicht mehr benötigt. Kann eventuell entfernt werden. (A.Herz)
   * @deprecated 
   * @param referenceString
   * @return
   */
	public static DataLongText createByReference(String referenceString)
	{
		return (DataLongText) createInstanceByReference(referenceString);
	}

	protected abstract DataLongText append(String text);

	protected abstract DataLongText prepend(String text);

	protected abstract String fetchLongText() throws Exception;

	public String getValue()
  {
    try
    {
      if (!this.fetched)
      {
        this.longText = fetchLongText();
        this.fetched = true;
      }
      return this.longText;
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
		return this.longText == null && this.fetched == true;
	}

	/*
  * IBIS: Wird von mir nicht mehr benötigt. Kann eventuell entfernt werden. (A.Herz)
  * @deprecated
  */ 
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
		if (anObject instanceof DataLongText)
		{
		  DataLongText other = (DataLongText) anObject;
		  
		  if (this.fetched && other.fetched)
		  {
		    return StringUtil.saveEquals(this.longText, other.longText);
		  }
		  
		  // otherwise if not fetched -> return false for performance reasons
		}
		return false;
	}

  public String toString()
  {
    return this.longText;
  }
}
