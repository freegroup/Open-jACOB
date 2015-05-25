/*
 * Created on 13.11.2007
 *
 */
package de.tif.jacob.screen.impl;

import java.util.Date;

import de.tif.jacob.screen.IClientContext;

public class CalendarActionCurrentMonth extends CalendarAction
{
  public void execute(IClientContext context, HTTPCalendar calendar, String value) throws Exception
  {
    calendar.setDisplayMonth(new Date());
  }


  public String getIcon()
  {
    return "calendar_current_month.png";
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
    return "CALENDAR_ACTION_CURRENT_MONTH";
  }
}
