/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author {author}
 */
public class {class} extends de.tif.jacob.screen.event.ICalendarEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ICalendarEventHandler.java,v 1.1 2007/11/26 11:20:39 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * It is not allowed to return null;
   */
  public ICalendarAppointment[] getAppointments(IClientContext context, ICalendar calendar, Date from, Date to) throws Exception
  {
    return new CalendarAppointment[0];
  }

  public void onClick(IClientContext context, ICalendar calendar, ICalendarAppointment appointment) throws Exception
  {
  }
}



