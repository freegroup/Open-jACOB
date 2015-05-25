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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.model.Enginetask;
import de.tif.jacob.deployment.DeployManager;

/**
 * A facility for threads to schedule recurring tasks for future 
 * execution in a background thread.
 * 
 * This class is thread-safe: multiple threads can share a single Scheduler
 * object without the need for external synchronization.
 * 
 * Note: The Scheduler uses a java.util.Timer to schedule tasks.
 */
public class Scheduler
{
  static public final transient String RCS_ID = "$Id: Scheduler.java,v 1.3 2010/07/07 14:11:51 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  static private final transient Log logger = LogFactory.getLog(Scheduler.class);
  
  private static final Set allSchedulers = new HashSet();
  
  private static boolean schedulerServiceRunning = true;
  
  // Map {Key->SchedulerTimerTask}
  private final Map scheduledTimerTasks = new HashMap();
  
  private Timer timer = new Timer();
  private final String id;
    
  /**
   * @author Andreas Sonntag
   *
   * Scheduler timer task which is registered within this scheduler
   */
  private class SchedulerTimerTask extends TimerTask
	{
		private final SchedulerTask schedulerTask;
		private final ScheduleIterator iterator;

		public SchedulerTimerTask(SchedulerTask schedulerTask, ScheduleIterator iterator)
		{
			this.schedulerTask = schedulerTask;
			this.iterator = iterator;
		}
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public final void run()
    {
      // ------------------------------------------------------------
      // Try to fetch data record of this task from internal database
      // Note: Update task status if changed in data record.
      // ------------------------------------------------------------
      IDataTableRecord record = null;
      if (this.schedulerTask.isVisible())
      {
        IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
        try
        {
          IDataTable table = accessor.getTable(Enginetask.NAME);
          table.qbeClear();
          table.qbeSetKeyValue(Enginetask.schedulerid, getId());
          table.qbeSetKeyValue(Enginetask.taskid, schedulerTask.getKey());
          if (table.search() == 0)
          {
            // entry has been deleted by means of the admin application -> really cancel timer
            cancel();
            return;
          }
          record = table.getRecord(0);            
          this.schedulerTask.setState(SchedulerTaskState.get(record.getStringValue(Enginetask.taskstatus)));
        }
        catch (Exception ex)
        {
          if (logger.isWarnEnabled())
          {
            logger.warn("Could not fetch task from DB: " + schedulerTask, ex);
          }
        }
      }

      // ------------------------------------------------------------
      // Only execute the task when it is scheduled (not hibernated) 
      // and the task should run on this node, i.e. cluster tasks
      // are only executed on the coordinator node!
      // ------------------------------------------------------------
			Date lastexecution = null;
			long duration_ms = -1;
      String errorMessage = null;
      boolean runOnThisNode = schedulerTask.runPerInstance() || ClusterManager.isCoordinator();
      if (this.schedulerTask.getState() == SchedulerTaskState.SCHEDULED && runOnThisNode)
			{
				lastexecution = new Date();
				
        try
        {
  				// set task context
          // Note: inside try-catch in case getContext() throws an exception!
  				//
          Context taskContext = this.schedulerTask.getContext();
  	      Context.setCurrent(taskContext);
  	      
  	      // and really run the thing
          //
          this.schedulerTask.run();
          duration_ms = System.currentTimeMillis() - lastexecution.getTime();
        }
        catch (Throwable ex)
        {
          // IBIS: Add code to suspend this task if they throw an error!
          errorMessage = ex.toString();
          ExceptionHandler.handle(ex);
        }
        finally
        {
          // reset task context
          // Note: This will be done after ExceptionHandler.handle() is called
          //       (which will call Context.getCurrent())!
          Context.setCurrent(null);
        }
			}
			Date nextExecution = reschedule(this.schedulerTask, this.iterator);
      
      // ------------------------------------------------------------
      // and refresh data record with new information 
      // ------------------------------------------------------------
			if (this.schedulerTask.isVisible() && record != null && nextExecution != null)
			{
				IDataTransaction transaction = record.getAccessor().newTransaction();
				try
				{
					if (null != lastexecution)
					{
						record.setValue(transaction, Enginetask.lastexecution, lastexecution);
            if (duration_ms == -1)
              record.setValue(transaction, Enginetask.duration_ms, null);
            else
            	record.setLongValue(transaction, Enginetask.duration_ms, duration_ms);
          }
					record.setValue(transaction, Enginetask.nextexecution, nextExecution);
          record.setStringValueWithTruncation(transaction, Enginetask.errormessage, errorMessage);
          record.setValue(transaction, Enginetask.taskstatus, schedulerTask.getState().getName());
          transaction.commit();
				}
				catch (Exception ex)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("Could not update task in DB: " + schedulerTask, ex);
					}
				}
				finally
				{
					transaction.close();
				}
			}
		}
    
		/* (non-Javadoc)
		 * @see java.util.TimerTask#cancel()
		 */
		public boolean cancel()
		{
			super.cancel();
			return this.schedulerTask.setState(SchedulerTaskState.CANCELED);
		}
	}

  /**
   * Creates a new scheduler instance
   */
  public Scheduler()
  {
    this.id = ""+ hashCode();
    register();
  }
  
  public Scheduler(String id)
  {
    this.id = id;
    register();
  }
  
  private void register()
  {
    synchronized(allSchedulers)
    {
      // check whether service is still running
      if (schedulerServiceRunning == false)
      {
        throw new IllegalStateException("Scheduler service has already been stopped.");
      }
      allSchedulers.add(this);
    }
  }
  
  /**
   * This method is called to stop the scheduler service which gracefully stops all
   * timer threads.
   */
  static void destroyAll()
  {
    synchronized(allSchedulers)
    {
      // destroy all registered schedulers
      Iterator iter = allSchedulers.iterator();
      while (iter.hasNext())
      {
        Scheduler scheduler = (Scheduler) iter.next();
        
        // do not call cancel() since this method modifies allSchedulers
        scheduler.destroy();
      }
      
      if (logger.isInfoEnabled())
      {
        logger.info(allSchedulers.size() + " scheduler instances destroyed");
      }
      
      // be sure and clear set
      allSchedulers.clear();
      
      // and set flag that scheduler service ist stopped
      schedulerServiceRunning = false;
    }
  }
  
  /**
   * Terminates this scheduler, discarding any currently
   * scheduled tasks. Does not interfere with a currently executing task (if it
   * exists). Once a scheduler has been terminated, its execution thread
   * terminates gracefully, and no more tasks may be scheduled on it.
   * 
   * Note that calling this method from within the run method of a scheduler
   * task that was invoked by this scheduler absolutely guarantees that the
   * ongoing task execution is the last task execution that will ever be
   * performed by this scheduler.
   * 
   * This method may be called repeatedly; the second and subsequent calls have
   * no effect.
   */
  public final boolean cancel()
  {
    boolean destroyed = destroy();
    if (destroyed)
    {
      synchronized(allSchedulers)
      {
        allSchedulers.remove(this);
      }
    }
    return destroyed;
  }
  
  private synchronized boolean destroy()
  {
    if (timer != null)
		{
      // delete all tasks as well from HSQL
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        IDataTable table = accessor.getTable(Enginetask.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Enginetask.schedulerid, getId());
        table.fastDelete(transaction);
        transaction.commit();
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
      finally
      {
        transaction.close();
      }

      // and really delete all timer tasks
      timer.cancel();
			timer = null;
      return true;
		}
    
    return false;
  }
  
  /**
	 * Cancels a task given by its key which is scheduled by this scheduler
	 * 
	 * @param taskKey
	 *          the key of the task to cancel
	 * @return <code>true</code> if cancellation has been successful, otherwise
	 *         <code>false</code>, i.e. the task has either already been
	 *         canceled or the task has not been scheduled by this scheduler
	 */
  public synchronized boolean cancel(String taskKey)
  {
  	// the complete scheduler has already been canceled?
  	if (timer == null)
  		return false;
  	
  	SchedulerTimerTask timerTask = (SchedulerTimerTask) scheduledTimerTasks.remove(taskKey);
  	if (null == timerTask)
  	{
  		return false;
  	}
  	
    // delete from HSQL if visible
    if (timerTask.schedulerTask.isVisible())
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        IDataTable table = accessor.getTable(Enginetask.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Enginetask.schedulerid, getId());
        table.qbeSetKeyValue(Enginetask.taskid, taskKey);
        if (table.search() > 0)
        {
          table.getRecord(0).delete(transaction);
          transaction.commit();
        }
      }
      catch (Exception ex)
      {
        // convert exception
        throw new RuntimeException(ex);
      }
      finally
      {
        transaction.close();
      }
    }

    // and really cancel
    return timerTask.cancel();
  }

  public boolean hibernate(String taskKey)
  {
    return changeTaskState(taskKey, SchedulerTaskState.HIBERNATED);
  }

  public boolean wakeup(String taskKey)
  {
    return changeTaskState(taskKey, SchedulerTaskState.SCHEDULED);
  }
  
  public static void updateTaskStatus(String applicationname, String taskname, String taskstatus)
  {
    if (logger.isInfoEnabled())
      logger.info("Updating task state of " + applicationname + "." + taskname + " to " + taskstatus);
    
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      IDataTable table = accessor.getTable(Enginetask.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Enginetask.applicationname, applicationname);
      table.qbeSetKeyValue(Enginetask.name, taskname);
      if (table.search() > 0)
      {
        IDataTableRecord record = table.getRecord(0);            
        record.setValue(transaction, Enginetask.taskstatus, taskstatus);
      }
      transaction.commit();
    }
    catch (Exception ex)
    {
      if (logger.isWarnEnabled())
      {
        logger.warn("Could not update task in DB", ex);
      }
    }
    finally
    {
			transaction.close();
		}
  }

  private synchronized boolean changeTaskState(String taskKey, SchedulerTaskState newState)
  {
    // the complete scheduler has already been canceled?
    if (timer == null)
      return false;
    
    // the task has already been canceled
    SchedulerTimerTask timerTask = (SchedulerTimerTask) scheduledTimerTasks.get(taskKey);
    if (null == timerTask)
    {
      return false;
    }
    
    // and write changed state to internal db if needed
    if (timerTask.schedulerTask.isVisible())
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        IDataTable table = accessor.getTable(Enginetask.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Enginetask.schedulerid, getId());
        table.qbeSetKeyValue(Enginetask.taskid, timerTask.schedulerTask.getKey());
        if (table.search() > 0)
        {
          IDataTableRecord record = table.getRecord(0);            
          record.setValue(transaction, Enginetask.taskstatus, newState.getName());
          transaction.commit();
        }
      }
      catch (Exception ex)
      {
        if (logger.isWarnEnabled())
        {
          logger.warn("Could not update task in DB: " + timerTask.schedulerTask, ex);
        }
      }
      finally
      {
				transaction.close();
			}
    }
    return timerTask.schedulerTask.setState(newState);
  }

  private static Date getNextExecution(ScheduleIterator iterator)
  {
    try
    {
      return iterator.next();
    }
    catch (Exception ex)
    {
      // just in case an application programmer throws an exception in
      // his implementation
      ExceptionHandler.handle(ex);
      return null;
    }
  }

  /**
   * Schedules the specified task for execution according to the specified
   * schedule. If times specified by the ScheduleIterator are in
   * the past they are scheduled for immediate execution.
   * 
   * @param schedulerTask  task to be scheduled
   * @param initialState
   *          the initial state or <code>null</code> to determine state
   *          automatically
   * @throws IllegalStateException
   *           if task was already scheduled or canceled, scheduler was
   *           canceled, or scheduler thread terminated.
   */
  public synchronized void schedule(SchedulerTask schedulerTask, SchedulerTaskState initialState)
  {
    ScheduleIterator iterator = schedulerTask.iterator();
    if (timer == null)
			throw new IllegalStateException("Scheduler '" + getId() + "' has already been canceled");
    
    if (this.scheduledTimerTasks.containsKey(schedulerTask.getKey()))
      throw new IllegalStateException("Task '" + schedulerTask.getKey() + "' has already been scheduled by scheduler '" + getId() + "'");
    
    // register task to be scheduled
    schedulerTask.schedule(this, initialState);

    // determine the next execution time
    Date nextExecution = getNextExecution(iterator);
    if (nextExecution == null)
    {
      // no execution desired -> cancel
      schedulerTask.setState(SchedulerTaskState.CANCELED);
      return;
    }
    
    // register for HSQL
    if (schedulerTask.isVisible())
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        // ensure to create an entry in dapplication because of relation constraint
        // Note: Currently this is only needed for ReportRollOutTask since there might
        //       exist scheduled reports related to undeployed applications!
        //
        String applName = schedulerTask.getApplicationName();
        Version applVersion = schedulerTask.getApplicationVersion();
        if (applName != null)
          DeployManager.ensureTransientApplication(accessor, transaction, applName);
        else
        {
          // assign tasks with no specific application to the admin application
          //
          applName = DeployManager.ADMIN_APPLICATION_NAME;
          applVersion = Version.ADMIN;
        }
        
        IDataTable table = accessor.getTable(Enginetask.NAME);
        IDataTableRecord record = table.newRecord(transaction);
        record.setValue(transaction, Enginetask.schedulerid, getId());
        record.setValue(transaction, Enginetask.taskid, schedulerTask.getKey());
        // because of long task names (e.g. R
        record.setStringValueWithTruncation(transaction, Enginetask.name, schedulerTask.getName());
        if (schedulerTask.getScope() != null)
          record.setValue(transaction, Enginetask.scope, schedulerTask.getScope().toString());
        record.setStringValueWithTruncation(transaction, Enginetask.taskdetails, schedulerTask.getTaskDetails());
        record.setValue(transaction, Enginetask.ownerid, schedulerTask.getTaskOwner());
        record.setValue(transaction, Enginetask.applicationname, applName);
        record.setValue(transaction, Enginetask.applicationversion, applVersion);
        record.setValue(transaction, Enginetask.taskstatus, schedulerTask.getState().getName());
        record.setValue(transaction, Enginetask.nextexecution, nextExecution);
        transaction.commit();
      }    
      catch (Exception ex)
      {
        // just release a warning since the task itself has been registered for the timer
        if (logger.isWarnEnabled())
				{
					logger.warn("Could not register task in DB: " + schedulerTask, ex);
				}
      }
      finally
      {
        transaction.close();
      }
    }
    
    // Schedule the new task the first time
    //
    SchedulerTimerTask timerTask = new SchedulerTimerTask(schedulerTask, iterator);
    scheduledTimerTasks.put(schedulerTask.getKey(), timerTask);
    timer.schedule(timerTask, nextExecution);
  }

  private synchronized Date reschedule(SchedulerTask schedulerTask, ScheduleIterator iterator)
  {
    // the scheduler has been canceled
    // This happens if a task itself stops the scheduler!!
    //
		if (this.timer == null)
			return null;
    
    // the task has been canceled in the meanwhile?
    if (schedulerTask.getState()== SchedulerTaskState.CANCELED)
      return null;

    SchedulerTimerTask timerTask = new SchedulerTimerTask(schedulerTask, iterator);
    scheduledTimerTasks.put(schedulerTask.getKey(), timerTask);
    
    Date nextExecution = getNextExecution(timerTask.iterator);
    if (nextExecution == null)
    {
      // The task wants terminate
      //
      cancel(timerTask.schedulerTask.getKey());
    }
    else
    {
      // The task wants run again
      //
      timer.schedule(timerTask, nextExecution);
    }
    
    return nextExecution;
  }
  
	/**
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		if (cancel())
		{
			logger.warn("!!!Scheduler has been canceled by garbage collector!!!");
		}
	}

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public final boolean equals(Object obj)
  {
    // ensure that this method is not overwritten
    return super.equals(obj);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public final int hashCode()
  {
    // ensure that this method is not overwritten
    return super.hashCode();
  }

}
