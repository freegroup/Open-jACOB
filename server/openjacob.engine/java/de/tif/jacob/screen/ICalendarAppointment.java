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

package de.tif.jacob.screen;

import java.awt.Color;
import java.util.Date;

/**
 * @author Andreas Herz
 * @since 2.7.1
 */
public interface ICalendarAppointment
{
  static public final String RCS_ID = "$Id: ICalendarAppointment.java,v 1.3 2008/02/13 14:57:06 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.3 $";

  /**
   * The event date of the appointment
   * 
   * @return the date of the appointment
   * 
   */
  public Date getDate();
  
  /**
   * The label of the appointment
   * 
   * @return the appointment label
   */
  public String getLabel();
  
  /**
   * The background color of the appointment
   * @return the background color or null for the defult color
   */
  public Color getBackgroundColor();
  
  
  /**
   * The decoration icon of the appointment 
   * 
   * @return the decoration icon of the appointment or null
   */
  public Icon getIcon();
}
