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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.deployment.DeployNotifyee;

/**
 * @author Andreas Herz
 *
 */
public class SchedulerService extends BootstrapEntry implements DeployNotifyee
{
  static public final transient String RCS_ID = "$Id: SchedulerService.java,v 1.1 2007/01/19 09:50:45 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private static final Log logger = LogFactory.getLog(BootstrapEntry.class);
  
  // Map{sessionId->Scheduler}
  private static final Map sessionSchedulers = new HashMap();
  
  // Map{applicationId->Map{schedulerId->Scheduler}}
  private static final Map applicationSchedulers = new HashMap();
  
  /**
   * Create all required jobs for a application instance. An application is
   * assigned to a logged in user. 
   * 
   * 
   * @param app
   */
  public static void createJobs(Session session) throws Exception
  {
    List jobs = ClassProvider.createInstancesFromPackage(session.getApplicationDefinition(), "jacob.scheduler.user", SchedulerTaskUser.class);
    Iterator iter = jobs.iterator();
    
    // ------------------------------------------
    // create the scheduler for this user session
    //
    String sessionId = session.getId();
    Scheduler scheduler = new Scheduler("session:"+sessionId);
    try
    {
      // create and add all scheduler jobs to this user session
      //
      while (iter.hasNext())
      {
        SchedulerTaskUser obj =(SchedulerTaskUser)iter.next();
        obj.setSession(session);
        scheduler.schedule(obj, null);
      }
      
      // retrieve all job for scheduling from the session
      //
      
      // create the timeout job for the session, application and dialogs. The job handles the 
      // keep alive for the associated applications for this session.
      //
      iter = session.createSessionTasks().iterator();
      while(iter.hasNext())
      {
        SchedulerTask task=(SchedulerTask)iter.next();
        scheduler.schedule(task, null);
      }
    }
    catch (Exception ex)
    {
      // in case of any problem -> clear resources
      scheduler.cancel();
      throw ex;
    }

    // ------------------------------
    // and register session scheduler
    //
		synchronized (sessionSchedulers)
		{
			if (null != sessionSchedulers.put(sessionId, scheduler) )
      {
        logger.warn("!!!Old session scheduler found for session " + sessionId + "!!!");
      }
		}
  }
  
  /**
   * Create all jobs for an ApplicationDefinition. An applicationDefinition is not assigned to 
   * any user. 
   * The jobs exists once for an deployed application.
   * 
   * @param app
   * @author Andreas Herz
   */
  private static void createJobs(IApplicationDefinition app) throws Exception
  {
    String applicationId = getApplicationId(app);
    
    // --------------------------------------------------------
    // start all tasks for the different application schedulers
    //
    Map schedulerMap = new HashMap();
    try
    {
      List jobs = ClassProvider.getInstancesFromPackage(app, "jacob.scheduler.system",SchedulerTaskSystem.class);
      Iterator iter = jobs.iterator();
      while (iter.hasNext())
      {
				Object obj =iter.next();
				// FREEGROUP:  ClassProvider.getInstancesFromPackage liefert nicht nur elemente der übergebenen Klasse zurück sondern  alle Klassen
				//             in dem Package. Dies ist ein grober Bug welcher noch behoben werden sollte.
				//              Workaround: Hier prüfen ob die Klasse der geforderten Interface entspricht.
				if(obj instanceof SchedulerTaskSystem)
				{
						SchedulerTaskSystem task =(SchedulerTaskSystem)obj;
						task.setApplication(app);
						
						String realSchedulername = applicationId + ":" + task.getSchedulerName();
						Scheduler scheduler = (Scheduler)schedulerMap.get(realSchedulername);
						if(scheduler==null)
						{
						  scheduler = new Scheduler(realSchedulername);
						  schedulerMap.put(realSchedulername,scheduler);
						}
						
						scheduler.schedule(task, null);
				}
      }
    }
    catch (Exception ex)
    {
      // in case of any problem -> clear resources
      removeJobs(schedulerMap);
      throw ex;
    }

    // -----------------------------------
    // and register application schedulers
    //
    synchronized (applicationSchedulers)
    {
      if (null != applicationSchedulers.put(applicationId, schedulerMap) )
      {
        logger.warn("!!!Old application schedulers found for " + applicationId + "!!!");
      }
    }
  }
  
  
  /**
   * Removes all jobs for the hands over session. 
   */
  public static void removeJobs(Session session)
	{
		synchronized (sessionSchedulers)
		{
      String sessionId = session.getId();
			Scheduler scheduler = (Scheduler) sessionSchedulers.remove(sessionId);
			if (scheduler != null)
      {
        scheduler.cancel();
      }
      else
      {
        if (logger.isWarnEnabled())
          logger.warn("!!!No session scheduler found for session " + sessionId + " to cancel!!!");
      }
		}
	}
  
  private static String getApplicationId(IApplicationDefinition app)
	{
		return app.getName() + "-" + app.getVersion().toString();
	}
  
  /**
   * All jobs for the hands over application definition will be removed. 
   * This event occurs if an application will be removed from the system.
   * 
   * @param app
   */
  private static void removeJobs(IApplicationDefinition app)
  {
    synchronized (applicationSchedulers)
    {
      Map schedulerMap = (Map) applicationSchedulers.remove(getApplicationId(app));
      if (schedulerMap != null)
      {
        removeJobs(schedulerMap);
      }
    }
  }

  private static void removeJobs(Map schedulerMap)
	{
		Iterator iter = schedulerMap.values().iterator();
		while (iter.hasNext())
		{
			Scheduler scheduler = (Scheduler) iter.next();
			scheduler.cancel();
		}
	}

	/* 
	 * @see de.tif.jacob.core.BootstrapEntry#init()
	 * 
	 */
	public void init() throws Throwable
  {
    Iterator iter = DeployManager.getDeployEntries().iterator();
    while (iter.hasNext())
    {
      DeployEntry entry = (DeployEntry) iter.next();
      try
      {
        onDeploy(entry);
      }
      catch (Throwable th)
      {
        // catch throwables to avoid that jACOB could not startup due to an invalid
        // deployed application, e.g. NoClassDefFoundError error for instantiation of
        // a task class!
        logger.error("Could not start tasks for application " + entry.getFile(), th);
        DeployManager.setDeployError(entry, th.toString());
      }
    }

    // Erst NACH dem bootstrap bei dem Lifecycle handling des Deployment
    // Managers einhängen
    // 
    DeployManager.registerNotifyee(this);
  }

	/*
   * The new application has been successfully deployed. Now we can access the
   * new application from the Main class.
   * 
   * @see de.tif.jacob.deployment.DeployNotifyee#onDeploy(de.tif.jacob.deployment.DeployEntry)
   */
	public void onDeploy(DeployEntry entry) throws Exception
	{
		if (entry.getStatus().isActive())
    {
      // create the new tasks for the application
			IApplicationDefinition app =DeployMain.getApplication(entry.getName(), entry.getVersion());
      createJobs(app);
    }
	}
	
  public void onUndeploy(DeployEntry entry) throws Exception
  {
		IApplicationDefinition app =DeployMain.getApplication(entry.getName(), entry.getVersion());
		removeJobs(app);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#afterRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void afterRedeploy(DeployEntry newEntry) throws Exception
  {
    // restart tasks for the application
    onDeploy(newEntry);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#beforeRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void beforeRedeploy(DeployEntry oldEntry) throws Exception
  {
		// remove the old tasks if we refresh the application with the same version id
    onUndeploy(oldEntry);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    Scheduler.destroyAll();
  }
}
