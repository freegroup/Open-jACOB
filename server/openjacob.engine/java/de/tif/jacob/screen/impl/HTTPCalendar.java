/*
 * Created on 15.11.2007
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.screen.ICalendar;
import de.tif.jacob.screen.ICalendarAppointment;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.GuiEventHandler;

public interface HTTPCalendar extends ICalendar
{
  public ICalendarAppointment[] getCurrentAppointments();
  
  public GuiEventHandler getEventHandler(IClientContext context);
}
