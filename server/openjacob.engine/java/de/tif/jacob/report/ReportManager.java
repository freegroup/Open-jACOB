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

package de.tif.jacob.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.IDataFieldConstraint;
import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.model.Enginetaskstatus;
import de.tif.jacob.report.impl.DatabaseReport;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.scheduler.Scheduler;
import de.tif.jacob.scheduler.SchedulerTask;
import de.tif.jacob.scheduler.SchedulerTaskState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 *
 */
public class ReportManager extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: ReportManager.java,v 1.6 2010/07/07 14:11:36 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
  
  static private final transient Log logger = LogFactory.getLog(ReportManager.class);
  
  private static final ReportScheduler scheduler = new ReportScheduler();
  
  /**
   * Scheduler extension to register all report rollout task ids per report.
   * 
   * @author Andreas Sonntag
   */
  private static class ReportScheduler extends Scheduler
  {
    /**
     * Map(reportId->Set(taslId))
     */
    private final Map reportidTaskidMap = new HashMap();
    
    protected ReportScheduler()
    {
      super("reports");
    }
    
    /**
     * Cancel all rollout tasks of the given report
     * 
     * @param reportId
     *          the id of the report
     */
    public synchronized void cancelReportTasks(String reportId)
    {
      Set taskids = (Set) reportidTaskidMap.remove(reportId);
      if (taskids != null)
      {
        Iterator iter = taskids.iterator();
        while (iter.hasNext())
        {
          String taskId = (String) iter.next();
          cancel(taskId);

          if (logger.isDebugEnabled())
            logger.debug("Report rollout task " + taskId + " canceled for report " + reportId);
        }
      }
    }

    public synchronized void schedule(SchedulerTask schedulerTask, SchedulerTaskState initialState)
    {
      ReportRollOutTask rollOutTask = (ReportRollOutTask) schedulerTask;
      
      String reportId = rollOutTask.getReportId();
      Set taskids = (Set) reportidTaskidMap.get(reportId);
      if (taskids == null)
      {
        taskids = new HashSet();
        reportidTaskidMap.put(reportId, taskids);
      }
      taskids.add(rollOutTask.getKey());
      
      super.schedule(schedulerTask, initialState);
    }
  }
  
  
  public static IReport getReport(IApplicationDefinition appDef, IUser owner, String name) throws Exception
  {
    return DatabaseReport.getReport(appDef, owner,name);
  }

  /**
   * Returns the (own) reports for the hands over user (owner) and application definition.
   * 
   * @param appDef
   * @param owner
   * @return List[IReport]
   * @throws Exception
   */
  public static List getReports(IApplicationDefinition appDef, IUser owner) throws Exception
  {
    return DatabaseReport.getReports(appDef, owner, DatabaseReport.OWN_MODE);
  }
  
  /**
   * Returns all reports for a given user and application definition.
   * 
   * @param appDef
   * @param owner
   * @return List[IReport]
   * @throws Exception
   */
  public static List getAllReports(IApplicationDefinition appDef, IUser user) throws Exception
  {
    return DatabaseReport.getReports(appDef, user, DatabaseReport.ALL_MODE);
  }
  
  /**
   * Returns (foreign) public reports for a given user and application definition.
   * 
   * @param appDef
   * @param owner
   * @return List[IReport]
   * @throws Exception
   */
  public static List getPublicReports(IApplicationDefinition appDef, IUser user) throws Exception
  {
    return DatabaseReport.getReports(appDef, user, DatabaseReport.PUBLIC_MODE);
  }
  
  /**
   * Returns the report definition with the hands over guid.
   * 
   * @return the report definition or <code>null</code> if the report does not
   *         exist anymore.
   */
  public static IReport getReport(String guid) throws Exception
  {
    return DatabaseReport.getReport(guid);
  }
  
  /**
   * Transforms the QueryByExample in the IClientContext to an report definition.
   * 
   * @param context
   * @return the created report definition .
   * @throws Exception
   */
  public static IReport transformToReport(IClientContext context,IDataFieldConstraints scriptConstraints) throws Exception
  {
    if(context.getGUIBrowser()==null)
      throw new Exception("Unable to determine required GUI-SearchBrowser.");
    
    String applicationName    = context.getApplication().getName();
    String applicationVersion = context.getApplication().getVersion();
    String mainTableAlias     = context.getGUIBrowser().getGroupTableAlias();
    
    IDataBrowserInternal dataBrowser = (IDataBrowserInternal) context.getDataBrowser();

    Report report = new DatabaseReport("default name", applicationName, context.getDomain().getName(),context.getForm().getName(), applicationVersion, null, mainTableAlias);
    if(dataBrowser != null && dataBrowser.getLastSearchConstraints() != null)
    {
      
      Iterator constraints = dataBrowser.getLastSearchConstraints().getConstraints();
      while (constraints.hasNext())
      {
        IDataFieldConstraintPrivate constraint = (IDataFieldConstraintPrivate) constraints.next();
        String guiSource = constraint.guiElementName();
        String table     = constraint.getTableAlias().getName();
        String field     = constraint.getTableField().getName();
        String value     = constraint.getQbeValue();
        boolean isKeyValue = constraint.isQbeKeyValue();
        
        report.addConstraint(guiSource, table, field, value, isKeyValue);
  		}
    }
      // Die Constraints die per Script gesetzt worden sind müssen jetzt wieder gesetzt werden
      //
      Iterator constraints = scriptConstraints.getConstraints();
      while (constraints.hasNext())
      {
        Object obj = constraints.next();
        if(obj instanceof IDataFieldConstraintPrivate)
        {
          IDataFieldConstraintPrivate constraint = (IDataFieldConstraintPrivate)obj;
          if(constraint.guiElementName()!=null && constraint.guiElementName().length()>0)
          {
            // ignore. This is not a script constraint.
          }
          else
          {
		        String gui       = constraint.guiElementName();
		        String table     = constraint.getTableAlias().getName();
		        String field     = constraint.getTableField().getName();
		        String value     = constraint.getQbeValue();
		        boolean isKeyValue = constraint.isQbeKeyValue();
		        
		        report.addConstraint(gui, table, field, value, isKeyValue);
          }
        }
        else if(obj instanceof IDataFieldConstraint)
        {
          IDataFieldConstraint constraint = (IDataFieldConstraint)obj;
	        String table     = constraint.getTableAlias().getName();
	        String field     = constraint.getTableField().getName();
	        String value     = constraint.getQbeValue();
	        boolean isKeyValue = constraint.isQbeKeyValue();
	        
	        report.addConstraint( table, field, value, isKeyValue);
        }
  		}
      return report; 
  }
  
  /* 
   * Read all reports and create the notifyee list (SchedulerTask) for this.
   * 
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    try
    {
      // Prefetch persistent task states to accelerate performance
      // Note: Otherwise for each scheduled report a single database request
      //       would be performed!
      //
      
      // Map(String:applname->Map(String:taskname->SchedulerTaskState))
      Map persistentTaskStateMap = new HashMap();
      
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable table = accessor.getTable(Enginetaskstatus.NAME);
      table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
      table.qbeClear();
      table.qbeSetValue(Enginetaskstatus.taskname, ReportRollOutTask.TASK_NAME_PREFIX+"*");
      table.search();
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord record = table.getRecord(i);
        String applname = record.getStringValue(Enginetaskstatus.applicationname);
        Map map = (Map) persistentTaskStateMap.get(applname);
        if (map == null)
        {
          map = new HashMap();
          persistentTaskStateMap.put(applname, map);
        }
        map.put(record.getStringValue(Enginetaskstatus.taskname), SchedulerTaskState.get(record.getStringValue(Enginetaskstatus.taskstatus)));
      }

      
      // start scheduler tasks for all scheduled reports
      //
      List scheduledReports = DatabaseReport.getScheduledReports();
      for (int i = 0; i < scheduledReports.size(); i++)
      {
        IReport report = (IReport) scheduledReports.get(i);
        
        ReportNotifyee[] notifyees = report.getReportNotifyees();
        for (int j = 0; j < notifyees.length; j++)
        {
          ReportNotifyee notifyee = notifyees[j];
          
          // determine initial state
          //
          SchedulerTaskState initialState = SchedulerTaskState.SCHEDULED;
          Map map = (Map) persistentTaskStateMap.get(report.getApplicationName());
          if (map != null)
          {
            SchedulerTaskState taskState = (SchedulerTaskState) map.get(ReportRollOutTask.getTaskName(report, notifyee));
            if (taskState != null)
              initialState = taskState;
          }
          
          // start rollout task
          //
          startReportScheduler(report, notifyee, initialState);
        }
      }
      if (logger.isInfoEnabled())
        logger.info(scheduledReports.size() + " reports scheduled");
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }
  
  /**
   * (Re)Starts report scheduler for scheduled reports. If the report does not exit anymore
   * the scheduler task (if existing) will be deleted.
   * 
   * @param guid the id of the report
   * @throws Exception on any problem
   */
  public static void restartReportScheduler(String guid) throws Exception
  {
    // plausibility check
    if (null == guid)
      throw new NullPointerException("guid is null");
    
    // cancel the old scheduler tasks for this report. It is possible that the
    // cron rules has been changed.
    //
    scheduler.cancelReportTasks(guid);

    // report still exists?
    //
    IReport report = getReport(guid);
    if (null != report)
    {
      ReportNotifyee[] notifyees = report.getReportNotifyees();
      for (int i = 0; i < notifyees.length; i++)
      {
        startReportScheduler(report, notifyees[i], null);
      }
      
      if (logger.isInfoEnabled())
        logger.info("Report scheduler (re)started for report " + guid);
    }
    else
    {
      if (logger.isInfoEnabled())
        logger.info("Report scheduler canceled for report " + guid);
    }
  }
  
  private static void startReportScheduler(IReport report, ReportNotifyee notifyee, SchedulerTaskState initialState) throws Exception
  {
    // Do not rollout private reports to users which are not the owner.
    // This might be the case, if a report is (temporary) changed from public 
    // to private.
    //
    if (report.isPrivate() && !report.getOwnerId().equals(notifyee.getUserLoginId()))
      return;
    
    // enter new scheduler task for the report
    //
    ReportRollOutTask task = new ReportRollOutTask(report, notifyee);
    scheduler.schedule(task, initialState);

    if (logger.isDebugEnabled())
      logger.debug("Report rollout task " + task.getKey() + " started for report " + report.getGUID());
  }
}
