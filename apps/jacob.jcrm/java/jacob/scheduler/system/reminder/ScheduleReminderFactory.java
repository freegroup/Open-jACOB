/*
 * Created on 24.05.2005 by mike
 * 
 *
 */
package jacob.scheduler.system.reminder;


/**
 * @author mike
 *
 */
public class ScheduleReminderFactory
{
    private ScheduleReminderFactory()
    {
        // verhindert das manuelle Instanzieren
    }

    public static IScheduleReminder getScheduler()
    {
        return new OwnReminderScheduler();
    }
}
