/*
 * Created on 24.05.2005 by mike
 * 
 *
 */
package jacob.scheduler.system.reminder;

import de.tif.jacob.scheduler.TaskContextSystem;

/**
 * @author mike
 *  
 */
public interface IScheduleReminder
{
  public void run(TaskContextSystem context) throws Exception;
}
