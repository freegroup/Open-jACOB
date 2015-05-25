/*
 * Created on 13.11.2007
 *
 */
package de.tif.jacob.screen.impl;

import java.util.Date;
import java.util.GregorianCalendar;
import de.tif.jacob.screen.IClientContext;

public class CalendarActionPreviousMonth extends CalendarAction
{
  public void execute(IClientContext context, HTTPCalendar calendar, String value) throws Exception
  {
    Date current = calendar.getDisplayMonth();
    java.util.Calendar cal = GregorianCalendar.getInstance(context.getLocale());
    cal.setTime(current);
    cal.add(java.util.Calendar.MONTH,-1);
    calendar.setDisplayMonth(cal.getTime());
  }

  public String getIcon()
  {
    return "calendar_previous_month.png";
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
    return "CALENDAR_ACTION_PREVIOUS_MONTH";
  }
}