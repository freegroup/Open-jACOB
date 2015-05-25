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
package de.tif.jacob.core.adjustment.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataBinary;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.util.ObjectUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class JacobBinary extends DataBinary implements Serializable
{
	static public final transient String RCS_ID = "$Id: JacobBinary.java,v 1.1 2007/01/19 09:50:32 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	static private final transient Log logger = LogFactory.getLog(JacobBinary.class);
	
	private final String dataSourceName;
	private UUID key;

	protected JacobBinary(String dataSourceName, UUID key)
	{
		this.dataSourceName = dataSourceName;
		this.key = key;
	}
	
	protected JacobBinary(String dataSourceName, UUID key, byte[] binaryData)
	{
		super(binaryData);
		this.dataSourceName = dataSourceName;
		this.key = key;
	}

	protected JacobBinary(String dataSourceName)
	{
		super(null);
		this.dataSourceName = dataSourceName;
		this.key = null;
	}

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object anObject)
  {
    if (this == anObject)
    {
      return true;
    }
    if (anObject instanceof JacobBinary)
    {
      JacobBinary other = (JacobBinary) anObject;

      if (ObjectUtil.equals(this.key, other.key))
      {
        return super.equals(anObject);
      }
    }
    return false;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.DataBinary#fetchBinary()
	 */
	protected byte[] fetchBinary() throws Exception
	{
		// hack: it is always assumed that a binary field comes together with an
		// SQL datasource
		SQLDataSource dataSource = (SQLDataSource) DataSource.get(this.dataSourceName);

		try
		{
			Connection connection = dataSource.getConnection();
			try
			{
				PreparedStatement statement = connection.prepareStatement("SELECT bdata FROM jacob_binary WHERE id = ?");
				try
				{
					statement.setString(1, this.key.toString());
					ResultSet rs = statement.executeQuery();
					try
					{
						if (rs.next())
						{
							return rs.getBytes(1);
						}
						else
						{
							// TODO: create warning
							return null;
						}
					}
					finally
					{
						rs.close();
					}
				}
				finally
				{
					statement.close();
				}
			}
			finally
			{
				connection.close();
			}
		}
		catch (SQLException ex)
		{
			logger.error("Fetching binary value failed!", ex);
			throw new RuntimeException(ex.toString());
		}
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("JacobDataBinary[");
		buffer.append("dataSourceName = ").append(dataSourceName);
		buffer.append(", key = ").append(key);
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * @return Returns the key.
	 */
	protected UUID getKey()
	{
		return key;
	}

	/**
	 * @return Returns the isNew.
	 */
	protected boolean isNew()
	{
		return this.key == null;
	}

	/**
	 * @return Returns the dataSourceName.
	 */
	protected String getDataSourceName()
	{
		return dataSourceName;
	}

	/**
	 * @param key The key to set.
	 */
	protected void setKey(UUID key)
	{
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.DataBinary#toReference(de.tif.jacob.core.data.DataTableRecord, java.lang.String)
	 */
	protected String toReference(IDataTableRecord record, String fieldName)
	{
		// ignore arguments
		return this.toReferenceInternal();
	}

	
}
