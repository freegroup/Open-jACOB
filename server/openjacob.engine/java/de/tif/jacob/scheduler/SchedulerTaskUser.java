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

package de.tif.jacob.scheduler;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.Version;

/**
 *
 */
public abstract class SchedulerTaskUser extends SchedulerTask
{
  static public final transient String RCS_ID = "$Id: SchedulerTaskUser.java,v 1.1 2007/01/19 09:50:45 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private TaskContextUser context;
  private String taskName;
  
  public final void setSession(Session session)
  {
    context = new TaskContextUser(session);
    taskName = super.getName() + ":" + session.getUser().getLoginId();
  }
  
  public abstract ScheduleIterator iterator();
  public abstract void             run(TaskContextUser context) throws Exception;
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getName()
   */
  public String getName()
  {
    // we rely that setSession() has been called at an
    // very early stage.
    if (this.taskName == null)
      throw new IllegalStateException("setSession() has not been called so far");
    return this.taskName;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationName()
   */
  public final String getApplicationName()
  {
    // we rely that setSession() has been called at an
    // very early stage.
    return this.context.getApplicationDefinition().getName();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationVersion()
   */
  public final Version getApplicationVersion()
  {
    // we rely that setSession() has been called at an
    // very early stage.
    return this.context.getApplicationDefinition().getVersion();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getScope()
   */
  public final Scope getScope()
  {
    return SESSION_SCOPE;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
   */
  public final boolean runPerInstance()
  {
    // returning <code>true</code> here does not mean that a user task (which is bound to a 
    // client session) is executed multiple times on each instance. It only means that each
    // jACOB runtime instance should execute <b>its</b> user task instances of users logged
    // on that jACOB instance.
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getContext()
   */
  protected final Context getContext()
  {
    return this.context;
  }
  
  /* 
   * @see java.lang.Runnable#run()
   */
  public final void run() throws Exception
  {
    try
    {
      run(this.context);
    }
    catch(Exception exc)
    {
      throw exc;
      // FREEGROUP: Notify or logout the user?!
    }
  }
}
