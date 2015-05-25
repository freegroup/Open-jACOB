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

import java.awt.Color;

import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract event handler class for charts. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to charts.
 * 
 * @author Andreas Herz
 */
public abstract class IChartEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IChartEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Returns the title of the chart.
   * <p>
   * Example:
   * 
   * <pre>
   * public String getTitle(IClientContext context, IChart chart)
   * {
   *   return ApplicationMessage.getLocalized(&quot;MyChart.Title&quot;);
   * }
   * </pre>
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return The title of the chart
   */
  public abstract String getTitle(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns the caption of the x-axis.
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return The caption of the x-axis
   */
  public abstract String getXAxisTitle(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns the caption of the y-axis.
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return The caption of the y-axis
   */
  public abstract String getYAxisTitle(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns the labels for the x-axis.
   * <p>
   * The dimension of the array returned must as follows:
   * <code>String [numberOfValues]</code><br>
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return the labels for the x-axis
   */
  public abstract String[] getXAxisLabels(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns the legend labels for the chart.
   * <p>
   * For line charts the dimension of the array returned must as follows:
   * <code>String [numberOfLines]</code><br>
   * Thereas, for bar charts as follows: <code>String [1]</code><br>
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return the legend label array
   */
  public abstract String[] getLegendLabels(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns the different colors for the chart lines and chart bars.
   * <p>
   * For line charts the dimension of the array returned must as follows:
   * <code>Color [numberOfLines]</code><br>
   * Thereas, for bar charts as follows: <code>Color [1]</code><br>
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return the color array
   */
  public abstract Color[] getColors(IClientContext context, IChart chart) throws Exception;

  /**
   * Returns an array of data values.
   * <p>
   * For line charts the dimension of the array returned must as follows:
   * <code>double [numberOfLines][numberOfValues]</code><br>
   * Thereas, for bar charts as follows: <code>double [1][numberOfValues]</code>
   * <br>
   * Example:
   * 
   * <pre>
   * double[][] data = new double[1][numberOfValues];
   * for (int j = 0; j &lt; numberOfValues; j++)
   * {
   *   data[0][j] = getRandomNumber(minValue, maxValue);
   * }
   * return data;
   * </pre>
   * 
   * @param context
   *          the current client context
   * @param chart
   *          the chart itself
   * @return the data values array
   */
  public abstract double[][] getDataElements(IClientContext context, IChart chart) throws Exception;
}
