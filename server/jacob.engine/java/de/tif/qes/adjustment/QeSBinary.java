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

package de.tif.qes.adjustment;

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
import de.tif.jacob.core.data.impl.sql.SQLDataSource;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSBinary extends DataBinary implements Serializable
{
  static public final transient String RCS_ID = "$Id: QeSBinary.java,v 1.1 2006-12-21 11:31:25 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  static private final transient Log logger = LogFactory.getLog(QeSBinary.class);
  
  private final String dataSourceName;
  private long key;

  protected QeSBinary(String dataSourceName, long key)
  {
    this.dataSourceName = dataSourceName;
    this.key = key;
  }
  
  protected QeSBinary(String dataSourceName, long key, byte[] binaryData)
  {
    super(binaryData);
    this.dataSourceName = dataSourceName;
    this.key = key;
  }

  protected QeSBinary(String dataSourceName)
  {
    super(null);
    this.dataSourceName = dataSourceName;
    this.key = -1;
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
    if (anObject instanceof QeSBinary)
    {
      QeSBinary other = (QeSBinary) anObject;

      if (this.key == other.key)
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
        PreparedStatement statement = connection.prepareStatement("SELECT text FROM qw_byte WHERE qwkey=?");
        try
        {
          statement.setLong(1, this.key);
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

  public static void main(String[] args)
  {
//    try
//    {
//      System.setProperty("jacob.configfile", System.getProperty("user.dir") + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + "jacob.properties");
//      new Database().init();
//      
//      QeSBinary qesBinary = new QeSBinary("caretaker", 505);
//      String reference = qesBinary.toReference();
//      System.out.println("Reference: " + reference);
//      DataBinary binary = QeSBinary.createByReference(reference);
//
//      System.out.println("FINISHED: " + binary.getValue().length);
//    }
//    catch (Throwable ex)
//    {
//      ex.printStackTrace();
//    }
  }
  
	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("QeSBinary[");
		buffer.append("dataSourceName = ").append(dataSourceName);
		buffer.append(", key = ").append(key);
		buffer.append("]");
		return buffer.toString();
	}
  
  /**
   * @return Returns the key.
   */
  protected long getKey()
  {
    return key;
  }

  /**
   * @return Returns the isNew.
   */
  protected boolean isNew()
  {
    return this.key == -1;
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
  protected void setKey(long key)
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
