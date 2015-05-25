/*
 * Created on 13.11.2007
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.ICalendarEventHandler;

public class CalendarActionAppointmentClick extends CalendarAction
{
  public void execute(IClientContext context, HTTPCalendar calendar, String value) throws Exception
  {
    ICalendarEventHandler obj = (ICalendarEventHandler)calendar.getEventHandler(context);
    if(obj!=null)
      obj.onClick(context,calendar,calendar.getCurrentAppointments()[Integer.parseInt(value)]);
  }


  public String getLabelId()
  {
    return "<unused>";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "<unused>";
  }
}
