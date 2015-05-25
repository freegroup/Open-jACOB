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

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataDocument;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.util.ObjectUtil;

/**
 * @author andreas
 *
 */
public final class JacobDocument extends DataDocument implements Serializable
{
	static public final transient String RCS_ID = "$Id: JacobDocument.java,v 1.2 2008/09/17 16:15:19 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	
	static private final transient Log logger = LogFactory.getLog(JacobDocument.class);
	
	private final String dataSourceName;
	private UUID key;

	protected JacobDocument(String dataSourceName, UUID key)
	{
	  super();
		this.dataSourceName = dataSourceName;
		this.key = key;
	}
	
	protected JacobDocument(String dataSourceName, UUID key, String name, byte[] content)
	{
		super(name, content);
		this.dataSourceName = dataSourceName;
		this.key = key;
	}

	protected JacobDocument(String dataSourceName)
	{
		super(null, null);
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
    if (anObject instanceof JacobDocument)
    {
      JacobDocument other = (JacobDocument) anObject;

      if (ObjectUtil.equals(this.key, other.key))
      {
        return super.equals(anObject);
      }
    }
    return false;
  }

  protected DataDocumentValue fetchDocumentValue() throws Exception
  {
    // hack: it is always assumed that a document field comes together with an
    // SQL datasource
    SQLDataSource dataSource = (SQLDataSource) DataSource.get(this.dataSourceName);

    try
    {
      Connection connection = dataSource.getConnection();
      try
      {
        PreparedStatement statement = connection.prepareStatement("SELECT docname, doccontent FROM jacob_document WHERE id = ?");
        try
        {
          statement.setString(1, this.key.toString());
          ResultSet rs = statement.executeQuery();
          try
          {
            if (rs.next())
            {
              return DataDocumentValue.create(rs.getString(1), rs.getBytes(2));
            }
            else
            {
              logger.warn("Content of document id '" + this.key.toString() + "' missing!");
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
      logger.error("Fetching document content failed!", ex);
      throw new RuntimeException(ex.toString());
    }
  }

  protected String fetchDocumentName() throws Exception
  {
    // hack: it is always assumed that a document field comes together with an
    // SQL datasource
    SQLDataSource dataSource = (SQLDataSource) DataSource.get(this.dataSourceName);

    try
    {
      Connection connection = dataSource.getConnection();
      try
      {
        PreparedStatement statement = connection.prepareStatement("SELECT docname FROM jacob_document WHERE id = ?");
        try
        {
          statement.setString(1, this.key.toString());
          ResultSet rs = statement.executeQuery();
          try
          {
            if (rs.next())
            {
              return rs.getString(1);
            }
            else
            {
              logger.warn("Name of document id '" + this.key.toString() + "' missing!");
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
      logger.error("Fetching document name failed!", ex);
      throw new RuntimeException(ex.toString());
    }
  }

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("JacobDocument[");
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
	 * @see de.tif.jacob.core.data.DataDocument#toReference(de.tif.jacob.core.data.IDataTableRecord, java.lang.String)
	 */
	protected String toReference(IDataTableRecord record, String fieldName)
	{
		// ignore arguments
		return this.toReferenceInternal();
	}

	
}
