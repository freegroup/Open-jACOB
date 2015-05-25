/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 08 13:35:24 CEST 2006
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
public class FreeCapacityChart extends IBarChartEventHandler
{
	static public final transient String RCS_ID = "$Id: FreeCapacityChart.java,v 1.1 2006/08/09 13:22:09 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public String getTitle(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getFreeTitle();
  }

  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return "Datum";
  }

  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return "Räderwechsel";
  }

  public String[] getXAxisLabels(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getXAxisLabels();
  }

  public String[] getLegendLabels(IClientContext context, IChart chart)
  {
    return new String[]
      { "freie Kapazität" };
  }

  public Color[] getColors(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getFreeColors();
  }

  public double[][] getDataElements(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getFreeData();
  }
}
