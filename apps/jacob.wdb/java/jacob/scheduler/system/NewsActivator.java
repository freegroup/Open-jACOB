package jacob.scheduler.system;
import jacob.common.AppLogger;
import jacob.model.News;
import jacob.model.News_noHook;

import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.SecondsIterator;
/**
 * @author andherz
 * 
 */
public class NewsActivator extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: NewsActivator.java,v 1.2 2010-09-08 21:42:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  // use this to log relvant information....not the System.out.println(...) ;-)
  //
  static protected final transient Log logger = AppLogger.getLogger();
  // Start the task every 1 minutes
  // for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
  //
  // final ScheduleIterator iterator = new MinutesIterator(1);
  final SecondsIterator iterator = new SecondsIterator(10);

  /**
   * Returns the Iterator which defines the run interval of this job.<br>
   * 
   */
  public ScheduleIterator iterator()
  {
    return iterator;
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
    IDataTable newsTable = context.getDataTable(News_noHook.NAME);
    
    // Erstmal alle News deaktivieren
    //
    newsTable.search(IRelationSet.LOCAL_NAME);
    activate(context, newsTable);
  }


  private static void activate(Context context, IDataTable newsTable) throws Exception
  {
    
    if (newsTable.recordCount() == 0)
      return;
    
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      for (int i = 0; i < newsTable.recordCount(); i++)
      {
        IDataTableRecord news = newsTable.getRecord(i);
        trans.omitLocking(news);
        activate(trans, news);
      }
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }
  
  public static boolean activate( IDataTransaction trans, IDataTableRecord news) throws Exception
  {
    Date nowInYear1970 = new Date();
    nowInYear1970.setYear(70);
    Date now = new Date();

    boolean renew = news.getSaveStringValue(News.renew).equals(News.renew_ENUM._year);
    boolean active = news.getSaveStringValue(News.status).equals(News.status_ENUM._active);
    Date start = news.getDateValue(News_noHook.intervall_start);
    Date end = news.getDateValue(News_noHook.intervall_end);
    Date nstart = start==null? new Date(0):new Date(start.getTime());
    Date nend = end==null?new Date(Long.MAX_VALUE):new Date(end.getTime());

    // Da Start und Ende über Silverster laufen können, kann man endDate nicht einfach auf das gleiche Jahr setzten 
    // wie der Start.
    nstart.setYear(70);
    int startYear = -1;
    
    if(start!=null)
    {
      startYear=start.getYear();
      if(end!=null)
        nend.setYear(70+end.getYear()-startYear);
    }
    
    long normalized_start = -1;
    long normalized_end = -1;

    if (active)
    {
      if (renew)
      {
        if (nstart.getTime() <= nowInYear1970.getTime() && nend.getTime() >= nowInYear1970.getTime())
        {
          normalized_start = nstart.getTime();
          normalized_end = nend.getTime();
        }
      }
      else
      {
        if ((start == null || start.getTime() <= now.getTime()) && (end == null || end.getTime() >= now.getTime()))
        {
          normalized_start = nstart.getTime();
          normalized_end = nend.getTime();
        }
      }
    }
    
    news.setValue(trans, News_noHook.normalized_start, normalized_start);
    news.setValue(trans, News_noHook.normalized_end, normalized_end);
    
    return news.hasChangedValues();
  }
  
}




