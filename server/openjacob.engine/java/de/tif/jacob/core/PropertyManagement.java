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
 * Created on 26.07.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.UniqueViolationException;
import de.tif.jacob.core.model.Administrator;
import de.tif.jacob.core.model.Japroperty;
import de.tif.jacob.core.model.Jproperty;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PropertyManagement extends BootstrapEntry
{
	static public transient final String RCS_ID = "$Id: PropertyManagement.java,v 1.4 2010/01/25 13:52:09 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.4 $";

	static private final transient Log logger = LogFactory.getLog(PropertyManagement.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.BootstrapEntry#init()
	 */
	public void init() throws Throwable
	{
		refreshFromDataSource();
    ensureAdministrators();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }
  
  /**
   * Ensures that at minimum one administrator entry exists.
   * If not, an "admin" entry with no password will be created.
   * @throws Exception
   */
  private static void ensureAdministrators() throws Exception
  {
    // IBIS: gehört eigentlich nicht hier her
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable administratorTable = accessor.getTable(Administrator.NAME);
    administratorTable.setMaxRecords(1);
    administratorTable.search();
    if (administratorTable.recordCount() == 0)
    {
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        IDataTableRecord adminRecord = administratorTable.newRecord(transaction);
        adminRecord.setValue(transaction, Administrator.loginid, "admin");
        adminRecord.setValue(transaction, Administrator.fullname, "default jACOB administrator");
        transaction.commit();
      }
      catch (UniqueViolationException ex)
      {
        // ignore - just in case 2 nodes are trying to add default admin simultaneously
      }
      finally
      {
        transaction.close();
      }
    }
  }
  
  // IBIS: Für Clusterbetrieb müssen die Properties von der Datenbank refreshed werden
  public static void refreshFromDataSource() throws Exception
	{
		IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

		// read jacob properties
		Set missingPropertyNames = Property.getPropertyNames();
		IDataTable jpropertyTable = accessor.getTable(Jproperty.NAME);
		jpropertyTable.search();
		for (int i = 0; i < jpropertyTable.recordCount(); i++)
		{
			IDataTableRecord record = jpropertyTable.getRecord(i);
			String propertyName = record.getStringValue(Jproperty.name);
			Property property = Property.getProperty(propertyName);
			if (null != property)
			{
        property.setValue(record.getStringValue(Jproperty.value));
        missingPropertyNames.remove(propertyName);
			}
		}

		// create missing properties in data source
		if (missingPropertyNames.size() > 0)
		{
			IDataTransaction transaction = accessor.newTransaction();
			try
			{
				IDataTable table = accessor.getTable(Jproperty.NAME);
				Iterator iter = missingPropertyNames.iterator();
				while (iter.hasNext())
				{
					Property property = Property.getProperty((String) iter.next());
					IDataTableRecord record = table.newRecord(transaction);
					record.setStringValue(transaction, Jproperty.name, property.getName());
          record.setStringValue(transaction, Jproperty.value, property.getDefaultValue());
          record.setStringValue(transaction, Jproperty.description, property.getInitialDescription());
          
					if (logger.isInfoEnabled())
						logger.info("Initialising property '" + property.getName() + "'");
				}
				transaction.commit();
			}
			finally
			{
				transaction.close();
			}
		}

		// read jacob application properties
		IDataTable japropertyTable = accessor.getTable(Japroperty.NAME);
		japropertyTable.search();
		for (int i = 0; i < japropertyTable.recordCount(); i++)
		{
			IDataTableRecord record = japropertyTable.getRecord(i);

			Property property = Property.getProperty(record.getStringValue(Japroperty.name));
			if (null != property)
			{
				property.setValue(record.getStringValue(Japroperty.applicationname), record.getStringValue(Japroperty.value));
			}
		}
	}
}
