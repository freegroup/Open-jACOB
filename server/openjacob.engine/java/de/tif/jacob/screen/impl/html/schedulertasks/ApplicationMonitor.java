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

import java.util.Iterator;

import de.tif.jacob.core.Property;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskUser;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.scheduler.iterators.SecondsIterator;
import de.tif.jacob.screen.impl.html.Application;
import de.tif.jacob.screen.impl.html.ClientSession;

/**
 *
 */
public class ApplicationMonitor extends SchedulerTaskUser
{
  public ApplicationMonitor(ClientSession session)
  {
    setSession(session);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new SecondsIterator(Property.CHECKINTERVAL_APPLICATION);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskUser#run(de.tif.jacob.scheduler.TaskContextUser)
   */
  public void run(TaskContextUser context) throws Exception
  {
    ClientSession session = (ClientSession) context.getSession();
    Iterator iter=session.getApplications().iterator();
    while (iter.hasNext())
    {
      Application obj = (Application) iter.next();
      obj.decrementSecondsBeforeDestroy(Property.CHECKINTERVAL_APPLICATION.getIntValue());
    }
    
    session.freeUnusedResources();
  }

}
