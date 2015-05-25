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
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.model.Userproperty;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas
 *
 * For internal use only
 */
class RuntimeProperty
{
	static public transient final String RCS_ID = "$Id: RuntimeProperty.java,v 1.3 2010/01/20 02:04:15 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.3 $";
	
	/**
   * <b>For internal use only!</b><br>
   * No caching. Use it only during close/open an Application or by an explicit user action.<br>
	 * It retrieves profile values for the user/application.
	 * 
	 * @param applicationDefinition
	 * @param user
	 * @param name
	 * @return
	 */
 	protected static String getValue(IApplicationDefinition applicationDefinition, IUser user, String name)
	{
	  try
    {
      IDataTableRecord record=getRecord(applicationDefinition,user,name);
      
      if(record!=null)
        return record.getStringValue(Userproperty.value);
      
      // no user-specific setting -> check for any common settings
      return Property.getPropertyValue(applicationDefinition, name);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unable to retrieve runtime property.", e); 
    }
	}
  
	/**
   * <b>For internal use only!</b><br>
   * No caching. Use it only during close/open an Application or by an explicit user action.<br>
	 * It stores profile values for the user/application.
	 * 
	 * @param applicationDefinition
	 * @param user
	 * @param name
	 * @param value
	 */
  protected static void setValue(IApplicationDefinition applicationDefinition, IUser user,String name,  String value)
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
	  IDataTransaction transaction = accessor.newTransaction();
		try
    {
		  IDataTableRecord record = getRecord(applicationDefinition, user, name);
		  if(record==null)
		  {
	      IDataTable table = accessor.getTable(Userproperty.NAME);
	      record = table.newRecord(transaction);
	
	      record.setStringValue(transaction, Userproperty.applicationname, applicationDefinition.getName());
        record.setStringValue(transaction, Userproperty.userid, user.getLoginId());
        record.setStringValue(transaction, Userproperty.name, name);
        record.setStringValue(transaction, Userproperty.value, value);
		  }
		  else
		  {
		    record.setValue(transaction, Userproperty.value, value);
		  }
      transaction.commit();
    }
		catch(Exception ex)
		{
		  throw new RuntimeException("Unable to set runtime property.", ex); 
		}
    finally
    {
      transaction.close();
    }
  }
  
  private static IDataTableRecord getRecord(IApplicationDefinition applicationDefinition, IUser user, String name) throws Exception
  {
		IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

		// read jacob properties
    IDataTable jpropertyTable = accessor.getTable(Userproperty.NAME);
    jpropertyTable.qbeSetKeyValue(Userproperty.name, name);
    jpropertyTable.qbeSetKeyValue(Userproperty.userid, user.getLoginId());
    jpropertyTable.qbeSetKeyValue(Userproperty.applicationname, applicationDefinition.getName());
    // the following line is important to avoid endless recursion!
    jpropertyTable.setMaxRecords(2);
    if (jpropertyTable.search() == 1)
		{
			return jpropertyTable.getRecord(0);
		}
		return null;
  }
}
