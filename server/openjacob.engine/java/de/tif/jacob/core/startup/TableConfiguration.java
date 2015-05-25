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
/*
 * Created on 12.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.startup;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.InMemorySQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TableConfiguration extends BootstrapEntry
{
  static public transient final String RCS_ID = "$Id: TableConfiguration.java,v 1.2 2009/07/30 15:00:06 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
	static private final transient Log logger = LogFactory.getLog(TableConfiguration.class);
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.BootstrapEntry#init()
	 */
	public void init() throws Throwable
	{
		createTables();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }
  
  /**
   * This method is for backward compatibility to ensure recreation of
   * incompatible tables of previous jACOB versions.
   * 
   * @param currentTable
   *          the table to check for recreation
   * @return <code>true</code> if recreation should be done, otherwise
   *         <code>false</code>
   */
  private static boolean enforceTableRecreation(ISchemaTableDefinition currentTable, Reconfigure reconfigure)
  {
    // check whether we have an old-style (version 2.4 and below) table, which
    // does not have a pkey field as primary key and uses field systemtime
    // instead!
    // 
    if ("memory_history".equalsIgnoreCase(currentTable.getDBName()))
    {
      Iterator iter = currentTable.getSchemaColumnDefinitions();
      while (iter.hasNext())
      {
        ISchemaColumnDefinition column = (ISchemaColumnDefinition) iter.next();
        if ("pkey".equalsIgnoreCase(column.getDBName()))
        {
          return false;
        }
      }
      
      // no pkey found -> drop
      return true;
    }

    // check whether we have an old-style (version 2.4 and below) table, which
    // does not have a nodename field!
    // 
    if ("process_history".equalsIgnoreCase(currentTable.getDBName()))
    {
      Iterator iter = currentTable.getSchemaColumnDefinitions();
      while (iter.hasNext())
      {
        ISchemaColumnDefinition column = (ISchemaColumnDefinition) iter.next();
        if ("nodename".equalsIgnoreCase(column.getDBName()))
        {
          return false;
        }
      }
      
      // no nodename found -> drop
      reconfigure.addTableToRecreate("process_info");
      return true;
    }

    // all others should of course not be recreated!
    return false;
  }
  
	/**
   * Create and reconfigure all jACOB internal tables, if necessary.
   * 
   * @throws Exception
   *           on any error
   */
	private static void createTables() throws Exception
	{
		Set processedDataSources = new HashSet();
		
		List tables = AdminApplicationProvider.getApplication().getTableDefinitions();
		for (int i=0; i<tables.size(); i++)
		{
			ITableDefinition tableDefinition = (ITableDefinition) tables.get(i);
			
			// fetch datasource
			DataSource dataSource = DataSource.get(tableDefinition.getDataSourceName());
			boolean notProcessedSoFar = processedDataSources.add(dataSource);
			
			if (notProcessedSoFar)
			{
				if (dataSource instanceof SQLDataSource)
				{
					if (logger.isInfoEnabled())
						logger.info("Starting to reconfigure jACOB data source '" + dataSource.getName() + "'");
					
				  SQLDataSource sqlDS = (SQLDataSource) dataSource;
				  
				  // get reconfigure implementation
				  Reconfigure reconfigure = sqlDS.getReconfigureImpl();
				  
				  // fetch current schema
				  ISchemaDefinition currentSchema = reconfigure.fetchSchemaInformation();
				  
					// setup datasource (as standard JACOB datasource)
				  sqlDS.setup(currentSchema);

				  // check whether we have to recreate some tables
				  Iterator iter = currentSchema.getSchemaTableDefinitions();
				  while (iter.hasNext())
          {
            ISchemaTableDefinition currentTable = (ISchemaTableDefinition) iter.next();
            if (enforceTableRecreation(currentTable, reconfigure))
            {
              reconfigure.addTableToRecreate(currentTable.getDBName());
            }
          }
					
				  // and setup jacob internal schema
				  // Note: abort on errors for InMemorySQLDataSource
					CommandList commandList = reconfigure.reconfigure(AdminApplicationProvider.getApplication().getSchemaDefinition(dataSource.getName()), currentSchema, true);
					commandList.execute(sqlDS, sqlDS instanceof InMemorySQLDataSource);
					
					if (commandList.hasExecutionError())
					{
					  logger.warn("Could not completely reconfigure jACOB predefined datasource '" + dataSource.getName() + "':\n"+commandList.getExecutionSummary());
//					  throw new Exception("Could not reconfigure jACOB predefined datasource '" + dataSource.getName() + "'");
					}
					else
					{
						if (logger.isInfoEnabled())
							logger.info("Finished reconfigure of jACOB data source '" + dataSource.getName() + "'");
					}
				}
			}
		}
	}
}
