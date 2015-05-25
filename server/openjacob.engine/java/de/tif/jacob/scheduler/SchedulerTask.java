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

import de.tif.jacob.core.SystemContext;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * A task that can be scheduled for recurring execution by a {@link Scheduler}.
 */
public abstract class SchedulerTask
{
  static public final transient String RCS_ID = "$Id: SchedulerTask.java,v 1.4 2010/07/15 09:37:32 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  /**
   * ExecuteScope of the task.
   * 
   * @author Andreas Sonntag
   */
  public static class Scope
  {
    private final String name;
    
    private Scope(String name)
    {
      this.name = name;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.name;
    }
  }
  
  /**
   * Cluster tasks are executed on the coordinator of a cluster only.
   */
  public static final Scope CLUSTER_SCOPE = new Scope("cluster");
  
  /**
   * Node tasks are executed on each node. 
   */
  public static final Scope NODE_SCOPE = new Scope("node");
  
  /**
   * Session tasks are executed for each user session.
   */
  public static final Scope SESSION_SCOPE = new Scope("session");
  
  private final Object lock = new Object();

  private SchedulerTaskState state = SchedulerTaskState.VIRGIN;
  private Scheduler scheduler = null;
  private final String defaultTaskName;
  
  protected SchedulerTask()
  {
    this.defaultTaskName = ClassUtil.getShortClassName(getClass());
  }
  
  /**
   * Internal method to get context to set before {@link #run()} is called.
   * 
   * @return the context to set
   */
  protected Context getContext()
  {
    // create system context as default context.
    return new SystemContext();
  }
  
  /**
   * The action to be performed by this scheduler task.
   */
  public abstract void run() throws Exception;

  
   /**
   * Returns the identifier for this task. You can use this identifier to cancel
   * the task at the scheduler.
   * 
   * @return the task key
   * @see Scheduler#cancel(String)
   */
  public String getKey()
  {
    return "" + hashCode();
  }
  
  /**
   * Returns whether this task should be hibernated by default, i.e. to start the task it has to be enabled manually
   * by means of the jACOB admin application.<p>
   * 
   * By default this method returns <code>false</code>.
   * 
   * @return <code>true</code> if hibernated by default, otherwise <code>false</code>
   */
  public boolean hibernatedOnSchedule()
  {
    return false;
  }

  public abstract ScheduleIterator iterator();

  /**
   * Returns whether this task should be executed on each jACOB runtime instance. This method
   * is important, if a jACOB installation should be clustered (i.e. more than one jACOB runtime
   * instance) for scalability or availability reasons. Task implementations should overwrite
   * this method and return <code>true</code>, if a task should be executed on each jACOB instance.  
   * 
   * @return <code>true</code>, if this task should run per jACOB runtime instance, otherwise <code>false</code>
   */
  public abstract boolean runPerInstance();
  
  /**
   * Returns the name of the task.<br>
   * By default this is the (short) name of derived class. 
   * 
   * @return the task name
   */
  public String getName()
  {
    return this.defaultTaskName;
  }
  
  /**
   * Returns the scope of the task.
   * 
   * @return the task scope
   */
  public Scope getScope()
  {
    return null;
  }
  
  /**
   * Returns the name of the application this task belongs to.
   * 
   * @return the application name or <code>null</code>
   */
  public String getApplicationName()
  {
    // might be overwritten
  	return null; 
  }
  
  /**
   * Returns the version of the application this task belongs to.
   * 
   * @return the application version or <code>null</code>
   */
  public Version getApplicationVersion()
  {
    // might be overwritten
  	return null; 
  }
  
  /**
	 * Returns whether this task should be visible and maintainable within the
	 * admin application
	 * 
	 * @return <code>true</code> if visible otherwise <code>false</code>
	 */
	public boolean isVisible()
	{
		return true;
	}
	
  /**
	 * Returns additional task details which could be used for querying for tasks.
	 * 
	 * @return additional task details or <code>null</code>
	 */
	public String getTaskDetails()
	{
	  return null;
	}
	
  /**
	 * Returns task owner if existing.
	 * 
	 * @return task owner or <code>null</code>
	 */
	public String getTaskOwner()
	{
	  return null;
	}
  
  /**
   * Marks this task as being scheduled
   * 
   * @param scheduler
   *          the scheduler of the task
   * @param initialState
   *          the initial state or <code>null</code> to determine state
   *          automatically
   */
  final void schedule(Scheduler scheduler, SchedulerTaskState initialState)
	{
    synchronized (lock)
    {
  		// ensure that a task is only scheduled by one scheduler
  		if (this.scheduler != null)
  		{
  			throw new IllegalStateException("Task may not be scheduled by multiple schedulers");
  		}
  		this.scheduler = scheduler;
  		
  		// determine task state
  		//
  		if (initialState != null && initialState.isRegular())
  		  this.state = initialState;
  		else
  		{
  		  // for session tasks do not fetch persistent state
  		  //
  		  SchedulerTaskState persistentState = null;
  		  if (this.getScope() != SESSION_SCOPE)
  		  {
  		    persistentState = SchedulerTaskState.getPersistent(getApplicationName(), getName());
  		  }
  		  
  		  if (persistentState == null)
          this.state = hibernatedOnSchedule() ? SchedulerTaskState.HIBERNATED : SchedulerTaskState.SCHEDULED;
        else
          this.state = persistentState;
  		}
    }
	}
  
  /**
   * Cancels this scheduler task.
   * <p>
   * This method may be called repeatedly; the second and subsequent calls have
   * no effect.
   * 
   * @return <code>true</code> if this task was been canceled by this method call
   */
  public final boolean cancel()
  {
    synchronized (lock)
    {
      if (this.scheduler == null)
        return false;
    }
    
    return this.scheduler.cancel(getKey());
  }
  
  final boolean setState(SchedulerTaskState state)
  {
  	synchronized (lock)
  	{
  		boolean result = (this.state == state);
  		this.state = state;
  		return result;
  	}
  }
  
  /**
	 * Returns the current state of this task.
	 * 
	 * @return the task state
	 */
	public final SchedulerTaskState getState()
  {
  	synchronized (lock)
  	{
  		return this.state;
  	}
  }
  
  /**
   * Hibernates this scheduler task.
   * <p>
   * This method may be called repeatedly; the second and subsequent calls have
   * no effect.
   * 
   * @return <code>true</code> if this task was been hibernated by this method call
   */
  public final boolean hibernate()
  {
  	synchronized (lock)
  	{
  		if (this.scheduler == null)
  			return false;
  	}
  	
  	return this.scheduler.hibernate(getKey());
  }
  
  /**
   * Wakes up this scheduler task from hibernate status.
   * <p>
   * This method may be called repeatedly; the second and subsequent calls have
   * no effect.
   * 
   * @return <code>true</code> if this task was been waked up by this method call
   */
  public final boolean wakeup()
  {
  	synchronized (lock)
  	{
  		if (this.scheduler == null)
  			return false;
  	}
  	
  	return this.scheduler.wakeup(getKey());
  }
}
