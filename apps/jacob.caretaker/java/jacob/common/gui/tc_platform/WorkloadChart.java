/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 25 16:19:43 CEST 2006
 */
package jacob.common.gui.tc_platform;

import java.awt.Color;

import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBarChartEventHandler;

/**
 *
 * @author andreas
 */
public class WorkloadChart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id: WorkloadChart.java,v 1.2 2006/08/01 15:55:54 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public String getTitle(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getWorkloadTitle();
  }

  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return "Datum";
  }

  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return "Prozent";
  }

  public String[] getXAxisLabels(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getXAxisLabels();
  }

  public String[] getLegendLabels(IClientContext context, IChart chart)
  {
    return new String[]
      { "Auslastung" };
  }

  public Color[] getColors(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getWorkloadColors();
  }

  public double[][] getDataElements(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getWorkloadData();
  }
  
  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    AvailableCapacityChart.onClick(context, false, barIndex);
  }
}
