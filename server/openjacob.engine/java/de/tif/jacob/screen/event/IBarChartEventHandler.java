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

import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;


/**
 * Abstract event handler class for bar charts. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to bar charts.
 * 
 * @author Andreas Herz
 */
public abstract class IBarChartEventHandler extends IChartEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IBarChartEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This hook method will be called, if the user has clicked on one bar in the
   * chart. Upon this event business logic can be added by means of overwriting
   * this method.
   * 
   * @param context
   *          The current client context
   * @param chart
   *          The chart itself
   * @param barIndex
   *          The index of the bar which has been clicked
   */
  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
  }
}
