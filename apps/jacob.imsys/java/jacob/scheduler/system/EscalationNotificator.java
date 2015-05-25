package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;

import com.sun.jndi.toolkit.url.UrlUtil;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author mike
 * 
 */
public class EscalationNotificator extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: EscalationNotificator.java,v 1.1 2006/05/15 13:50:56 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this to log relvant information....not the System.out.println(...) ;-)
  //
  static protected final transient Log logger = AppLogger.getLogger();

  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  //
  final ScheduleIterator iterator = new MinutesIterator(1);

  /**
   * Returns the Iterator which defines the run interval of this job.<br>
   * 
   */
  public ScheduleIterator iterator()
  {
    return iterator;
  }
  
  private void sendAlert(TaskContextSystem context,String table, String pkey,String userKey,String message) throws Exception
  {
    IDataTransaction trans =context.getDataAccessor().newTransaction();
    try
    {
        IDataTable qw_alerttable=context.getDataTable("qw_alert");
        IDataTableRecord bookmark=qw_alerttable.newRecord(trans);
        
        bookmark.setValue(trans,"addressee",userKey);
        bookmark.setValue(trans,"sender",context.getUser().getLoginId());
        bookmark.setValue(trans,"tablename",table);
        bookmark.setValue(trans,"tablekey",pkey);
        bookmark.setStringValueWithTruncation(trans,"message",message);
        bookmark.setValue(trans,"alerttype","Senden");
        bookmark.setValue(trans,"dateposted","now");
        bookmark.setValue(trans,"severity","0");
        trans.commit();
    }
    finally
    {
      trans.close();
    }
      
 
  }
  private void notifyGrpMember(TaskContextSystem context,IDataAccessor accessor,IDataTableRecord eventRecord) throws Exception
  {
    String grpKey =eventRecord.getSaveStringValue("workgroupkey");
    String message = eventRecord.getSaveStringValue("message");
    IDataTable grpMember = accessor.getTable("groupmember");
    grpMember.clear();
    grpMember.qbeClear();
    grpMember.qbeSetKeyValue("workgroupgroup",grpKey);
    grpMember.qbeSetValue("notifymethod", "!Keine");
    grpMember.search("r_employee");
    for (int i = 0; i < grpMember.recordCount(); i++)
    {
      IDataTableRecord grpMemberRecord = grpMember.getRecord(i);
      String method = grpMemberRecord.getSaveStringValue("notifymethod");
      IDataTableRecord employeeRecord = grpMemberRecord.getLinkedRecord("employee");
      if (method.equals("Signal"))
      {
        String address = employeeRecord.getSaveStringValue("loginname");
        sendAlert(context,eventRecord.getSaveStringValue("tablename"),eventRecord.getSaveStringValue("tablekey"),address,message);
        continue;
      }
      if (method.equals("Email"))
      {
        String address = employeeRecord.getSaveStringValue("emailcorr");
        Message.sendEMail(address,message);
        continue;
      }
      if (method.equals("FAX"))
      {
        String address = employeeRecord.getSaveStringValue("faxcorr");
        Message.sendFAX(address,message);
        continue;
      }
      if (method.equals("Funkruf"))
      {
        String address = employeeRecord.getSaveStringValue("pager");
        Message.sendSMS(address,message);
      }

      
    }
  }
  private void notifyWorkgroup(TaskContextSystem context,IDataAccessor accessor, IDataTableRecord eventRecord) throws Exception
  {
    String grpKey =eventRecord.getSaveStringValue("workgroupkey");
    String message = eventRecord.getSaveStringValue("message");
    accessor.clear();
    IDataTable workgroup = accessor.getTable("workgroup");
    workgroup.clear();
    workgroup.qbeClear();
    workgroup.qbeSetKeyValue("pkey",grpKey);
    workgroup.search();
    if (workgroup.recordCount()!=1) // sollte nie passieren
      return;
    IDataTableRecord workgroupRecord = workgroup.getRecord(0);
    String method = workgroupRecord.getSaveStringValue("notifymethod");
    String address = workgroupRecord.getSaveStringValue("notificationaddr");
    if (method.equals("Bearbeiter"))
    {
      notifyGrpMember(context,accessor,eventRecord);
      return;
    }
    if (method.equals("Signal"))
    {
      sendAlert(context,eventRecord.getSaveStringValue("tablename"),eventRecord.getSaveStringValue("tablekey"),address,message);
      return;
    }
    if (method.equals("Email"))
    {
      Message.sendEMail(address,message);
      return;
    }
    if (method.equals("FAX"))
    {
      Message.sendFAX(address,message);
      return;
    }
    if (method.equals("Funkruf"))
    {
      Message.sendSMS(address,message);
    }
  }

  private void processRecord(TaskContextSystem context,IDataAccessor accessor, IDataTableRecord record)
  {
    IDataTransaction trans = record.getTable().startNewTransaction();
    try
    {
      notifyWorkgroup(context,accessor,record);
      if (record.getValue("repeatinterval")!=null)
      {
        Calendar cal = Calendar.getInstance();    
        cal.add(Calendar.MINUTE, record.getintValue("repeatinterval"));
        record.setDateValue(trans, "when", cal.getTime()); 
        record.setValue(trans,"datemodified","now");
      }
      else
      {
        record.delete(trans);
      }
      trans.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * The run method of the job.<br>
   * The object
   * <code>context>/code> defines your current context in the jACOB application
   * server.<br>
   * You can use it to access the database or other relevatn application data.<br>
   */
  public void run(TaskContextSystem context) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    IDataTable qw_events = accessor.getTable("qw_events");
    // Datensätze suchen
    qw_events.clear();
    qw_events.qbeClear();
    qw_events.qbeSetValue("when", "<now");
    qw_events.search();
    int count = qw_events.recordCount();
    IDataAccessor newAccessor =context.getDataAccessor().newAccessor();
    for (int i = 0; i < count; i++)
    {
      IDataTableRecord record = qw_events.getRecord(count-1-i);
      processRecord(context,newAccessor, record);
    }

  }



}
