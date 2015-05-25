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
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IApplicationDefinition;

/**
 *
 */
public abstract class SchedulerTaskSystem extends SchedulerTask
{
  static public final transient String RCS_ID = "$Id: SchedulerTaskSystem.java,v 1.2 2010/07/07 21:02:36 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private TaskContextSystem context;
  
  final void setApplication(IApplicationDefinition application)
  {
    context = new TaskContextSystem(application);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationName()
   */
  public final String getApplicationName()
  {
    // we rely that setApplication() has been called at an
    // very early stage.
    return this.context.getApplicationDefinition().getName();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationVersion()
   */
  public final Version getApplicationVersion()
  {
    // we rely that setApplication() has been called at an
    // very early stage.
    return this.context.getApplicationDefinition().getVersion();
  }
  
  public String getSchedulerName()
	{
		return "default";
	}
  
  public abstract ScheduleIterator iterator();
  public abstract void             run(TaskContextSystem context) throws Exception;
  
  /**
   * By default system tasks are executed on single jACOB runtime instance, i.e.
   * this method returns <code>false</code>.<p>
   * 
   * Nevertheless, concrete implementations might overwrite this behaviour!
   * 
   * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
   */
  public boolean runPerInstance()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getScope()
   */
  public final Scope getScope()
  {
    return runPerInstance() ? NODE_SCOPE : CLUSTER_SCOPE;
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
    run(this.context);
  }
}
