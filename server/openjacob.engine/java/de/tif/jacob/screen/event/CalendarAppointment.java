/*
 * Created on 14.11.2007
 *
 */
package de.tif.jacob.screen.event;

import java.awt.Color;
import java.util.Date;
import de.tif.jacob.screen.ICalendarAppointment;
import de.tif.jacob.screen.Icon;

public class CalendarAppointment implements ICalendarAppointment
{
  private final Date date;
  private final String label;
  private final Icon icon;
  public CalendarAppointment(final Date date, final String label)
  {
    this.date = date;
    this.label = label;
    this.icon = Icon.clock;
  }
  
  public CalendarAppointment(final Date date, final String label, final Icon icon)
  {
    this.date = date;
    this.label = label;
    this.icon = icon;
  }

  public Date getDate()
  {
    return date;
  }

  public String getLabel()
  {
    return label;
  }

  public Color getBackgroundColor()
  {
    return null;
  }

  public Icon getIcon()
  {
    return icon;
  }
}
