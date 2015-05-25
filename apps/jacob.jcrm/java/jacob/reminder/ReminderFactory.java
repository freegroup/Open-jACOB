/*
 * Created on 20.05.2005 by mike
 * 
 *
 */
package jacob.reminder;

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author mike
 * 
 */
public class ReminderFactory
{
  /**
   * Just to prohihit that instances are created.
   */
  private ReminderFactory()
  {
  }

  public static IReminder getReminder(IDataTableRecord contextRecord)
  {
    return new OwnReminder(contextRecord);
  }
}
