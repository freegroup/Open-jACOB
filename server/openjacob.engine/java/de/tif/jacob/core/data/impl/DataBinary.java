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

/**
 * This class represents the internal value of jACOB binary fields.
 *  
 * @author Andreas Sonntag
 */
public abstract class DataBinary extends DataReference implements Nullable, Serializable
{
  static public transient final String        RCS_ID = "$Id: DataBinary.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  private transient byte[] binary = null;
  private transient boolean fetched = false;

  protected DataBinary(byte[] binary)
  {
    this.binary = binary;
    this.fetched = true;
  }

  protected DataBinary()
  {
    this.binary = null;
    this.fetched = false;
  }

  public static DataBinary createByReference(String referenceString)
  {
    return (DataBinary) createInstanceByReference(referenceString);
  }

  protected abstract byte[] fetchBinary() throws Exception;

  public final byte[] getValue()
  
  {
    try
    {
      if (!this.fetched)
      {
        this.binary = fetchBinary();
        this.fetched = true;
      }
      return this.binary;
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
    if (anObject instanceof DataBinary)
    {
      DataBinary other = (DataBinary) anObject;

      if (this.fetched && other.fetched)
      {
        if (this.binary == null)
        {
          return other.binary == null;
        }
        if (other.binary != null && this.binary.length == other.binary.length)
        {
          for (int i = 0; i < this.binary.length; i++)
          {
            if (this.binary[i] != other.binary[i])
              return false;
          }
          return true;
        }
      }

      // otherwise if not fetched -> return false for performance reasons
    }
    return false;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.misc.Nullable#isNull()
	 */
	public boolean isNull()
	{
    return this.binary == null && this.fetched == true;
  }

}
