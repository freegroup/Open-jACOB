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

import java.io.ByteArrayOutputStream;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.report.impl.transformer.classic.ExcelTransformer;
import de.tif.jacob.report.impl.transformer.classic.PlainTextTransformer;
import de.tif.jacob.report.impl.transformer.csv.CSVTransformer;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTask;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.CronIterator;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.UserManagement;

/**
 * The Task for the scheduler to create and deliver the report to the notifyee.
 * 
 */
public final class ReportRollOutTask extends SchedulerTask
{
  static public final transient String RCS_ID = "$Id: ReportRollOutTask.java,v 1.4 2010/03/21 22:49:57 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  protected static final String TASK_NAME_PREFIX = "ReportRollOut:";
  
  private static final String DIRECTORY_PROTOCOL = Message.DIRECTORY_PROTOCOL_NAME + Message.PROTOCOL_HEADER_SUFFIX;
  
  private final String reportId;
  private final String taskId;
  private final String sendUrl;
  private final String mimeType;
  private final String owner;
  private final String reportName;
  private final String applicationName;
  private final ReportNotifyee notifyee;
  private final String taskName;
  
  /**
   * Constructor
   * @param report the scheduled report
   * @param notifyee the notifyee to send report to
   */
  protected ReportRollOutTask(IReport report, ReportNotifyee notifyee)
  {
    this.notifyee = notifyee;
    this.reportId = report.getGUID();
    this.taskId   = getTaskId(report, notifyee);
    this.sendUrl  = notifyee.getProtocol() + notifyee.getAddress();
    this.mimeType = notifyee.getMimeType();
    this.owner = getOwnerId(report, notifyee);
    this.reportName     = report.getName();
    this.applicationName = report.getApplicationName();
    this.taskName = getTaskName(report, notifyee);
  }
  
  private static String getOwnerId(IReport report, ReportNotifyee notifyee)
  {
    // return notifyee as task owner
    //
    String notifyeeId = notifyee.getUserLoginId();
    if (notifyeeId != null)
      return notifyeeId;
    
    // otherwise the report owner (should not be the case)
    return report.getOwnerId();
  }
  
  protected static String getTaskId(IReport report, ReportNotifyee notifyee)
  {
    return report.getGUID() + ":" + notifyee.getUserId();
  }
  
  protected static String getTaskName(IReport report, ReportNotifyee notifyee)
  {
    if (report.getOwnerId().equals(notifyee.getUserLoginId()))
      return TASK_NAME_PREFIX + report.getOwnerId() + ":" + report.getName();
    
    return TASK_NAME_PREFIX + report.getOwnerId() + ":" + report.getName() + ":" + notifyee.getUserLoginId();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getName()
   */
  public String getName()
  {
    return this.taskName;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationName()
   */
  public String getApplicationName()
  {
    return this.applicationName;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getApplicationVersion()
   */
  public Version getApplicationVersion()
  {
    // return null because we always use the most recent active application for
    // rolling out a report
    return null;
  }
  
  public ScheduleIterator iterator()
  {
    return new CronIterator(notifyee.getCronEntry());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#runPerInstance()
   */
  public boolean runPerInstance()
  {
    // IBIS: Offener Punkt: Bei manchen Reports wï¿½re es wï¿½nschenswert diese pro jACOB
    // Instance zugeschickt zu bekommen. Beispiel: active Benutzersessions einer Instanz.
    
    // to avoid sending multiple reports 
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getScope()
   */
  public Scope getScope()
  {
    return CLUSTER_SCOPE;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getTaskDetails()
   */
  public String getTaskDetails()
  {
    // return report name as task detail
    return this.reportName;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getTaskOwner()
   */
  public String getTaskOwner()
  {
    return this.owner;
  }
  
  /**
   * @return Returns the reportId.
   */
  public String getReportId()
  {
    return reportId;
  }

  /**
   * The identifier which indicates this task. Is required for task cancel action.
   * 
   */
  public String getKey()
  {
    return this.taskId;
  }
  
  private static String REPORT_FILENAME_VALID_CHARS = "!§$()[]_-={}%&";
  
  private String getReportFilename()
  {
    StringBuffer buffer = new StringBuffer(this.reportName.length());
    for (int i=0; i< this.reportName.length();i++)
    {
      char c = this.reportName.charAt(i);
      if (Character.isSpaceChar(c))
      {
        buffer.append("_");
      }
      else if (Character.isLetterOrDigit(c) || REPORT_FILENAME_VALID_CHARS.indexOf(c)>=0)
      {
        buffer.append(c);
      }
    }
    return buffer.toString();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#run()
   */
  public void run() throws Exception
	{
		IReport report = ReportManager.getReport(reportId);
		
		// The report has been deleted. Cancel the task and return.
		// If you cancel the task it will never be called/scheduled.
		if (report == null)
		{
			cancel();
			return;
		}
		
		Context schedulerContext = Context.getCurrent();
		try
    {
  		IApplicationDefinition reportApplication = report.getApplication();
      IUser reportUser = UserManagement.getUser(reportApplication.getName(), reportApplication.getVersion().toString(), this.notifyee.getUserLoginId());
      
  		// The report must run in the context of the report user.
  		// This is important for the {mandator_id} in the table alias constraints
  		//
			Context.setCurrent(new TaskContextSystem(reportApplication, reportUser));
		  
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      report.render(stream, mimeType);
      
      // do not send empty reports, if not desired
      if (report.getRecordCount() > 0 || this.notifyee.isOmitEmpty() == false)
      {
        Message msg;
        if (mimeType.equals(PlainTextTransformer.TEXT_PLAIN_MIME_TYPE) && !sendUrl.startsWith(DIRECTORY_PROTOCOL))
        {
          Object[] args = new Object[]
            { report.getName(), report.getName(), new Integer(report.getRecordCount()), new Long(report.getRealRecordCount()) };
          String body = I18N.getCoreLocalized("REPORTROLLOUT_MSG_PLAIN", reportApplication, reportUser.getLocale(), args) + stream.toString("ISO-8859-1");

          msg = new Message(sendUrl, body);
        }
        else
        {
          Object[] args = new Object[]
            { report.getName(), report.getName(), new Integer(report.getRecordCount()), new Long(report.getRealRecordCount()) };
          String body = I18N.getCoreLocalized("REPORTROLLOUT_MSG_ATTACHMENT", reportApplication, reportUser.getLocale(), args);

          msg = new Message(sendUrl, body);
          if (mimeType.equals(ExcelTransformer.EXCEL_MIME_TYPE) || mimeType.equals("application/excel") || mimeType.equals("application/vnd.ms-excel"))
          {
            msg.addAttachment("application/vnd.ms-excel", getReportFilename() + ".xls", stream.toByteArray());
          }
          else if (mimeType.equals(CSVTransformer.CSV_MIME_TYPE))
          {
            msg.addAttachment(mimeType, getReportFilename() + ".csv", stream.toByteArray());
          }
          else if (mimeType.equals("text/xml"))
          {
            msg.addAttachment(mimeType, getReportFilename() + ".xml", stream.toByteArray());
          }
          else if (mimeType.startsWith("text/"))
          {
            msg.addAttachment(mimeType, getReportFilename() + ".txt", stream.toByteArray());
          }
          else
          {
            msg.addAttachment(mimeType, getReportFilename(), stream.toByteArray());
          }
        }
        msg.setSender(null);
        msg.setSenderAddr(null);
        msg.send();
      }
    }
    finally
    {
    	// switch back to the original Context
    	//
    	Context.setCurrent(schedulerContext);
    }
	}
}
