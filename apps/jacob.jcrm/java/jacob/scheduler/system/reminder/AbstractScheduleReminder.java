/*
 * Created on 24.05.2005 by mike
 * 
 *
 */
package jacob.scheduler.system.reminder;

import de.tif.jacob.messaging.Message;

/**
 * @author mike
 * 
 */
public abstract class AbstractScheduleReminder implements IScheduleReminder
{
    public void sendAlert(String recipient, String message) throws Exception
    {
        Message.sendAlert(recipient, message);
    }

    public void sendEmail(String recipient, String message) throws Exception
    {
        Message.sendEMail(recipient, message);
    }

    public void sendFAX(String recipient, String message) throws Exception
    {
        Message.sendFAX(recipient, message);
    }

    public void sendSMS(String recipient, String message) throws Exception
    {
        Message.sendSMS(recipient, message);
    }
}
