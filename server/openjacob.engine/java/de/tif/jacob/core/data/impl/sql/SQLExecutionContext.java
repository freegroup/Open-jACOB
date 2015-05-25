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

package de.tif.jacob.core.data.impl.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataTransaction;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class SQLExecutionContext extends DataExecutionContext
{
  static public transient final String        RCS_ID = "$Id: SQLExecutionContext.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  static private final transient Log logger = LogFactory.getLog(SQLExecutionContext.class);
  
  private Connection connection; 
  private String currentDBFieldName;

	/**
	 *  
	 */
	protected SQLExecutionContext(SQLDataSource dataSource, DataTransaction transaction) throws SQLException
	{
    super(dataSource, transaction);
    
    // activated postpone functionality, i.e. to allow multiple active prepared statements for MS SQL Server
		this.connection = dataSource.getConnection(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataExecutionContext#commit()
	 */
	public void commit() throws Exception
	{
		this.connection.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataExecutionContext#rollback()
	 */
	public void rollback()
	{
    if (this.connection != null)
    {
      try
      {
        this.connection.rollback();
      }
      catch (Exception ex)
      {
        // ignore
        logger.warn("Rollback failed!", ex);
      }
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.DataExecutionContext#close()
	 */
	public void close()
	{
		if (this.connection != null)
		{
			try
			{
				this.connection.close();
			}
			catch (Exception ex)
			{
				// ignore
				logger.warn("Close failed!", ex);
			}
      this.connection = null;
		}
	}

	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection()
	{
    if (this.connection == null)
      throw new RuntimeException("Connection is closed");
		return connection;
	}

	/**
	 * @return Returns the currentDBFieldName.
	 */
	public String getCurrentDBFieldName()
	{
		return currentDBFieldName;
	}

	/**
	 * @param currentDBFieldName The currentDBFieldName to set.
	 */
	protected void setCurrentDBFieldName(String currentDBFieldName)
	{
		this.currentDBFieldName = currentDBFieldName;
	}

  public SQLDataSource getSQLDataSource()
  {
    return (SQLDataSource) getDataSource();
  }
  
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		close();
	}

}
