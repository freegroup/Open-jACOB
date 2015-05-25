/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jul 26 16:59:35 CEST 2006
 */
package jacob.common.gui.tc_object;

import java.awt.Color;

import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBarChartEventHandler;

/**
 *
 * @author andreas
 */
public class SlotSelectChart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id: SlotSelectChart.java,v 1.2 2006/09/15 11:32:45 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The title of the line chart
   */
  public String getTitle(IClientContext context, IChart chart)
  {
    return ChartState.getChartState(context).getTitle();
  }

  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the x-axis
   */
  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return ChartState.getChartState(context).getXAxisTitle();
  }

  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the y-axis
   */
  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return ChartState.getChartState(context).getYAxisTitle();
  }

  /**
   * Returns the labels for the x-axis. The number of the labels <b>must</b> match
   * with the number of the returned data elements.
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return the labels for the x-axis
   */
  public String[] getXAxisLabels(IClientContext context, IChart chart)
  {
    return ChartState.getChartState(context).getXAxisLabels();
  }

  private static final String[] LEGENDS = new String[]
    { "" };

  /**
   * Returns the legend label for each data set/line in the chart.
   * 
   * @param context
   * @param chart
   * @returnReturn the legend label for each data set/line in the chart.
   */
  public String[] getLegendLabels(IClientContext context, IChart chart)
  {
    return LEGENDS;
  }

  /**
   * Special color desired by ServiceDesk (Frau Bentfeld)
   */
  private static final Color[] COLORS = new Color[]
    { new Color(140, 173, 216) };

  /**
   * Returns the different colors of the chart lines.
   * 
   * @param context
   * @param chart
   * @return Return the different colors of the chart lines.
   */
  public Color[] getColors(IClientContext context, IChart chart)
  {
    return COLORS;
  }

  /**
   * Returns an array of data values.<br>
   *  
   * @param context the current working context
   * @param chart the chart GUI element
   * @return
   */
  public double[][] getDataElements(IClientContext context, IChart chart)
  {
    // each time data is retrieved we refresh back button state
    //
    ChartState chartState = ChartState.getChartState(context);
    context.getGroup().findByName("backButton").setEnable(chartState.hasPreviousState());
    
    return chartState.getDataElements();
  }

  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    ChartState chartState = ChartState.getChartState(context);
    chartState.onClick(context, chart, barIndex);
  }
}
