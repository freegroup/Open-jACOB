/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.event;

import java.util.Date;

import de.tif.jacob.screen.ICalendar;
import de.tif.jacob.screen.ICalendarAppointment;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract event handler class for own draw areas. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to own draw areas.
 * 
 * @author Andreas Herz
 */
public abstract class ICalendarEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ICalendarEventHandler.java,v 1.2 2008/12/01 21:06:35 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public abstract ICalendarAppointment[] getAppointments(IClientContext context, ICalendar calendar, Date from, Date to) throws Exception;

  public void onClick(IClientContext context, ICalendar calendar, ICalendarAppointment appointment) throws Exception
  {
  }
}
