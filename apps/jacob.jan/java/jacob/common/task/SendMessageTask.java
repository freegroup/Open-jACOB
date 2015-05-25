package jacob.common.task;

import jacob.common.AppLogger;
import jacob.model.Jan_queue;
import jacob.model.TaskProperties;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.util.clazz.ClassUtil;

public abstract class SendMessageTask extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: SendMessageTask.java,v 1.2 2010-03-24 15:14:12 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  static protected final transient Log logger = AppLogger.getLogger();

  /**
   * Default task interval in seconds.
   */
  static public final int DEFAULT_TASK_INTERVAL = 60;

  private class IntervalIterator implements ScheduleIterator
  {
    private final String taskname;
    private boolean first = true;
    private int interval = DEFAULT_TASK_INTERVAL;
    private final Calendar calendar = Calendar.getInstance();

    private IntervalIterator(Class clazz)
    {
      this.taskname = ClassUtil.getShortClassName(clazz);
    }

    private void checkInterval()
    {
      try
      {
        IDataAccessor accessor = SendMessageTask.this.getContext().getDataAccessor();
        IDataTable taskProps = accessor.getTable(TaskProperties.NAME);
        taskProps.qbeClear();
        taskProps.qbeSetKeyValue(TaskProperties.taskname, this.taskname);
        if (1 == taskProps.search())
        {
          this.interval = taskProps.getSelectedRecord().getintValue(TaskProperties.taskinterval);
        }
        else
        {
          this.interval = DEFAULT_TASK_INTERVAL;
        }
      }
      catch (Exception ex)
      {
        if (logger.isWarnEnabled())
          logger.warn("Could not retrieve interval for task: " + this.taskname, ex);
      }
    }

    public Date next()
    {
      // immediately execution at first
      if (first)
      {
        first = false;
        return new Date();
      }

      checkInterval();
      this.calendar.setTimeInMillis(System.currentTimeMillis());
      this.calendar.add(Calendar.SECOND, this.interval);
      return this.calendar.getTime();
    }
  }

  private final ScheduleIterator iterator = new IntervalIterator(this.getClass());

  public final ScheduleIterator iterator()
  {
    return iterator;
  }

  /**
   * Returns the concret message send protocol implementation.
   * 
   * @return the message send protocol implementation
   */
  protected abstract SendProtocol getProtocol();

  /**
   * By default use a different scheduler per protocol.
   */
  public String getSchedulerName()
  {
    return getProtocol().getProtocolName();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#getTaskDetails()
   */
  public final String getTaskDetails()
  {
    return getProtocol().getProtocolName();
  }

  /**
   * Always run once in a jACOB cluster.
   */
  public final boolean runPerInstance()
  {
    return false;
  }

  private static int NBR_OF_MESSAGES_TO_FETCH_AT_ONCE = 50;

  public final void run(TaskContextSystem context) throws Exception
  {
    // get protocol implementation
    SendProtocol protocol = getProtocol();

    // Initialise message send task
    if (protocol.initialize(context))
    {
      IDataAccessor accessor = context.getDataAccessor();
      IApplicationDefinition appDef = accessor.getApplication();

      // create adhoc browser because we want to get oldest messages first
      //
      IAdhocBrowserDefinition messageBrowserDef = appDef.createAdhocBrowserDefinition(appDef.getTableAlias(Jan_queue.NAME));
      messageBrowserDef.addBrowserField(Jan_queue.NAME, Jan_queue.pkey, Jan_queue.pkey, SortOrder.ASCENDING);

      IAdhocBrowserDefinition attachmentBrowserDef = SendProtocol.getAttachmentBrowserDefinition(appDef);

      IDataBrowser messageBrowser = accessor.createBrowser(messageBrowserDef);
      messageBrowser.setMaxRecords(NBR_OF_MESSAGES_TO_FETCH_AT_ONCE);

      // Retrieve all valid messages for the given protocol from the message
      // queue and send them one be one.
      //
      int sendCount = 0, failedCount = 0;
      do
      {
        accessor.qbeClearAll();
        IDataTable messageTable = accessor.getTable(Jan_queue.NAME);
        messageTable.qbeClear();
        messageTable.qbeSetValue(Jan_queue.state, Jan_queue.state_ENUM._New);
        messageTable.qbeSetValue(Jan_queue.url, "^" + protocol.getProtocolName() + Message.PROTOCOL_HEADER_SUFFIX);
        messageBrowser.search(appDef.getLocalRelationSet());
        for (int i = 0; i < messageBrowser.recordCount(); i++)
        {
          if (protocol.send(messageBrowser.getRecord(i), attachmentBrowserDef))
            sendCount++;
          else
            failedCount++;
        }
      }
      while (messageBrowser.hasMoreRecords());

      if (sendCount > 0 || failedCount > 0)
      {
        if (logger.isInfoEnabled())
          logger.info("Protocol '" + protocol.getProtocolName() + "': " + sendCount + " new messages sent, " + failedCount + " failed");
      }

      // Repeating of failed messages allowed?
      if (protocol.repeatSending())
      {
        sendCount = 0;
        failedCount = 0;

        long startMillis = System.currentTimeMillis();

        // Retrieve invalid messages for the given protocol from the message
        // queue and try to send them once more.
        //
        boolean overdue = false;
        do
        {
          accessor.qbeClearAll();
          IDataTable messageTable = accessor.getTable(Jan_queue.NAME);
          messageTable.qbeClear();
          messageTable.qbeSetValue(Jan_queue.state, Jan_queue.state_ENUM._Error);
          messageTable.qbeSetValue(Jan_queue.url, "^" + protocol.getProtocolName() + Message.PROTOCOL_HEADER_SUFFIX);
          messageTable.qbeSetValue(Jan_queue.errorat, "<now-" + protocol.repeatTimespanInMins() + "min");
          messageTable.qbeSetValue(Jan_queue.tries, "<=" + protocol.repeatMaxRetries());
          messageBrowser.search(appDef.getLocalRelationSet());
          for (int i = 0; i < messageBrowser.recordCount(); i++)
          {
            if (protocol.send(messageBrowser.getRecord(i), attachmentBrowserDef))
              sendCount++;
            else
              failedCount++;

            if (System.currentTimeMillis() - startMillis > 1000L * protocol.repeatTaskTimeInSecs())
            {
              // further messages to sent
              if (i + 1 < messageBrowser.recordCount() || messageBrowser.hasMoreRecords())
              {
                if (logger.isInfoEnabled())
                  logger.info("Protocol '" + protocol.getProtocolName() + "': Sending of old messages aborted");
              }

              overdue = true;
              break;
            }
          }
        }
        while (messageBrowser.hasMoreRecords() && !overdue);

        if (sendCount > 0 || failedCount > 0)
        {
          if (logger.isInfoEnabled())
            logger.info("Protocol '" + protocol.getProtocolName() + "': " + sendCount + " old messages sent, " + failedCount + " failed again");
        }
      }
    }
  }
}
