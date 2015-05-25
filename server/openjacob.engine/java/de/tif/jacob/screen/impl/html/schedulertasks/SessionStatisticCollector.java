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

package de.tif.jacob.screen.impl.html.schedulertasks;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskUser;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.scheduler.iterators.SecondsIterator;
import de.tif.jacob.screen.impl.html.ClientSession;

/**
 * Collect session/user information and update the jACOB administration database with the values.
 * 
 */
public class SessionStatisticCollector extends SchedulerTaskUser
{
  static public final transient String RCS_ID = "$Id: SessionStatisticCollector.java,v 1.2 2009/03/02 19:07:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public SessionStatisticCollector(ClientSession session)
  {
    setSession(session);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new SecondsIterator(60);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskUser#run(de.tif.jacob.scheduler.TaskContextUser)
   */
  public void run(TaskContextUser context) throws Exception
  {
    ClientSession session = (ClientSession) context.getSession();
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
	  IDataTransaction transaction = accessor.newTransaction();
	  try
		{
			// Update the last usage of the Session in the database
	    //

			IDataTable table = accessor.getTable("activesession");
			table.qbeSetKeyValue("sessionid", session.getId());
      if (table.search() != 1)
      {
        // this could happen, if a session has already been deleted by administrator but not
        // destroyed so far!
//        throw new Exception("unable to find session record in jACOB-Admin database. Unable to update 'lastAccess' of the session.");
        return;
      }
			IDataTableRecord record= table.getRecord(0);

			record.setValue(transaction, "lastAccess", session.getLastAccess());

			// on success commit the new session in the registry.
			//
			transaction.commit();
	  }
		finally
		{
		  transaction.close();
		}		
  }
}
