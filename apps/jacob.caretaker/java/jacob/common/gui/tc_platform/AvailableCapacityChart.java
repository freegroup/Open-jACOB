/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 25 15:41:04 CEST 2006
 */
package jacob.common.gui.tc_platform;

import jacob.common.tc.TC;

import java.awt.Color;
import java.util.Date;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IBarChartEventHandler;

/**
 * 
 * @author andreas
 */
public class AvailableCapacityChart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id: AvailableCapacityChart.java,v 1.5 2006/08/10 19:13:53 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  public String getTitle(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getAvailableTitle();
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
      { "verfügbare Kapazität" };
  }

  public Color[] getColors(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getAvailableColors();
  }

  public double[][] getDataElements(IClientContext context, IChart chart)
  {
    return PlatformChartData.get(context).getAvailableData();
  }

  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    onClick(context, true, barIndex);
  }

  protected static void onClick(IClientContext context, boolean availableNotBooked, int barIndex) throws Exception
  {
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);
    IDataTableRecord platformRec = context.getSelectedRecord();
    
    Date day = PlatformChartData.get(context).getDate(barIndex);
    IDataAccessor accessor = context.getDataAccessor();
    accessor.qbeClearAll();
    IDataTable capacityTable = accessor.getTable("tc_capacity");
    capacityTable.qbeSetValue("slot", GenerateCapacity.dateFormatter.format(day));
    if (!availableNotBooked)
      capacityTable.qbeSetValue("tc_order_key", "!null");
    capacityTable.qbeSetKeyValue("tc_platform_key", platformRec.getValue("pkey"));
    capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
    IDataBrowser capacityBrowser = accessor.getBrowser("tc_capacityBrowser");
    capacityBrowser.search("r_tc_platform", Filldirection.BACKWARD);
    if (capacityBrowser.recordCount() == 1)
    {
      capacityBrowser.setSelectedRecordIndex(0);
      capacityBrowser.propagateSelections();
    }
    
    // Searchbrowser in den Vordergrund bringen
    IGroup group = (IGroup) context.getForm().findByName("tc_capacityGroup");
    if (group != null)
    {
      context.getForm().setCurrentBrowser(group.getBrowser());
    }
  }
}
