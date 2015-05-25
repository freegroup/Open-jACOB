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
 * Created on 03.09.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class SchedulerTaskState
{
	static public final transient String RCS_ID = "$Id: SchedulerTaskState.java,v 1.2 2009/03/02 19:06:42 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	
  static private final transient Log logger = LogFactory.getLog(SchedulerTaskState.class);
  
	public static final SchedulerTaskState VIRGIN = new SchedulerTaskState("virgin", false);
	public static final SchedulerTaskState SCHEDULED = new SchedulerTaskState("scheduled", true);
	public static final SchedulerTaskState HIBERNATED = new SchedulerTaskState("hibernated", true);
	public static final SchedulerTaskState CANCELED = new SchedulerTaskState("canceled", false);
		
  private final String name;
  private final boolean regular;
  
  public static SchedulerTaskState get(String name)
	{
		if (SCHEDULED.name.equals(name))
		{
			return SCHEDULED;
		}
		if (HIBERNATED.name.equals(name))
		{
			return HIBERNATED;
		}
		if (CANCELED.name.equals(name))
		{
			return CANCELED;
		}
		if (VIRGIN.name.equals(name))
		{
			return VIRGIN;
		}
		throw new RuntimeException("Unknown task state '" + name + "'");
	}
  
	/**
	 * Private constructor
	 */
	private SchedulerTaskState(String name, boolean regular)
	{
    this.name = name;
    this.regular = regular;
	}
	
	/**
   * Determines the persistent state of a given task.
   * 
   * @param applicationname
   *          the application name of the task
   * @param taskname
   *          the task name
   * @return the persistent task state or <code>null</code> if not existing
   */
  protected static SchedulerTaskState getPersistent(String applicationname, String taskname)
  {
    try
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable table = accessor.getTable("enginetaskstatus");
      table.qbeClear();
      table.qbeSetKeyValue("applicationname", applicationname);
      table.qbeSetKeyValue("taskname", taskname);
      table.search();
      if (table.recordCount() > 0)
      {
        IDataTableRecord record = table.getRecord(0);
        return get(record.getStringValue("taskstatus"));
      }
    }
    catch (Exception ex)
    {
      logger.warn("Could not determine persistent state of " + applicationname + "." + taskname, ex);
    }
    return null;
  }
	
	/**
   * Determines whether this state is a regular state of a running task.
   * 
   * @return <code>true</code> if regular, otherwise <code>false</code>
   */
	public boolean isRegular()
	{
	  return this.regular;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
}
