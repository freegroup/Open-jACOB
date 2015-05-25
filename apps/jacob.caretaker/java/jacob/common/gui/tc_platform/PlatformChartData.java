package jacob.common.gui.tc_platform;

import jacob.common.tc.NoActiveCampaignException;
import jacob.common.tc.TC;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

abstract class PlatformChartData
{
  static public final transient String RCS_ID = "$Id: PlatformChartData.java,v 1.5 2006/10/12 09:32:10 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private static final String CHART_DATA = "PlatformChartData";
  private static final String CHART_OVERALL_MODE = "PlatformChartOverallMode";

  static void enableOverall(IClientContext context)
  {
    context.setPropertyForRequest(CHART_OVERALL_MODE, Boolean.TRUE);
  }
  
  static boolean isOverallEnabled(IClientContext context)
  {
    return context.getProperty(CHART_OVERALL_MODE) != null;
  }
  
  static PlatformChartData get(IClientContext context)
  {
    try
    {
      PlatformChartData chartData = (PlatformChartData) context.getProperty(CHART_DATA);
      if (chartData == null || chartData.needsRecalculation(context))
      {
        try
        {
          chartData = new RegularChartData(context, isOverallEnabled(context));
        }
        catch (NoActiveCampaignException ex)
        {
          chartData = new NoCampaignChartData();
        }
        context.setPropertyForRequest(CHART_DATA, chartData);
      }
      return chartData;
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Helper class to collect values
   * 
   */
  private static class ChartValue
  {
    final String label;
    final CapacityData capacity;
    final Date day;

    ChartValue(String label, Date day, CapacityData capacity)
    {
      this.label = label;
      this.capacity = capacity;
      this.day = day;
    }

    double getAvailableSlots()
    {
      if (this.capacity != null && this.capacity.availableSlots != 0)
        return this.capacity.availableSlots;

      // Hack: Because of chart bug
      return TC.adjustHack(0);
    }

    double getFreeSlots()
    {
      if (this.capacity != null && this.capacity.freeSlots != 0)
        return this.capacity.freeSlots;

      // Hack: Because of chart bug
      return TC.adjustHack(0);
    }

    double getWorkload()
    {
      if (this.capacity != null && this.capacity.availableSlots != 0)
      {
        return 100.0 * this.capacity.bookedSlots / this.capacity.availableSlots;
      }

      // Hack: Because of chart bug
      return TC.adjustHack(0);
    }
  }

  /**
   * Helper class to collect values
   * 
   */
  private static class CapacityData
  {
    int availableSlots = 0;
    int freeSlots = 0;
    int bookedSlots = 0;
  }

  private static class NoCampaignChartData extends PlatformChartData
  {
    private static final Color[] COLORS = new Color[]
      { Color.RED };
    
    private static final String TITLE = "Keine aktive Kampagne vorhanden";
    
    private static final double[][] NO_DATA = new double[1][0];
    
    private static final String[] LABELS = new String[0];

    public double[][] getAvailableData()
    {
      return NO_DATA;
    }

    public double[][] getFreeData()
    {
      return NO_DATA;
    }

    public double[][] getWorkloadData()
    {
      return NO_DATA;
    }

    public String[] getXAxisLabels()
    {
      return LABELS;
    }

    public Color[] getAvailableColors()
    {
      return COLORS;
    }

    public Color[] getFreeColors()
    {
      return COLORS;
    }

    public Color[] getWorkloadColors()
    {
      return COLORS;
    }

    public String getAvailableTitle()
    {
      return TITLE;
    }

    public String getFreeTitle()
    {
      return TITLE;
    }

    public String getWorkloadTitle()
    {
      return TITLE;
    }

    protected boolean needsRecalculation(IClientContext context)
    {
      return false;
    }
  }

  private static class RegularChartData extends PlatformChartData
  {
    private final String[] xAxisLabels;
    private final double[][] availableData, freeData, workloadData;
    private final Date[] xAxisDates;
    private final boolean platformLocked;
    private final boolean overallPlatforms;
    private final int totalAvailableSlots;
    private final int totalFreeSlots;
    private final int totalBookedSlots;

    private RegularChartData(IClientContext context, boolean overallPlatforms) throws NoActiveCampaignException, Exception
    {
      IDataTableRecord activeCampaign = TC.getActiveCampagne(context);
      IDataTableRecord platformRec = overallPlatforms ? null : context.getSelectedRecord();
      
      this.overallPlatforms = overallPlatforms;
      this.platformLocked = overallPlatforms ? false : platformRec.getintValue("locked") != 0;

      // fetch capacity entries for the current platform
      //
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      IDataTable capacity = accessor.getTable("tc_capacity");
      capacity.qbeClear();
      if (platformRec != null)
        capacity.qbeSetKeyValue("tc_platform_key", platformRec.getValue("pkey"));
      capacity.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
      capacity.setMaxRecords(IDataBrowser.UNLIMITED_RECORDS);
      capacity.search();

      // examine capacity entries
      //
      // Map(Date -> CapacityData)
      Map capacityMap = new HashMap();
      Calendar cal = new GregorianCalendar();
      int totalBookedSlots = 0, totalFreeSlots = 0, totalAvailableSlots = 0;
      for (int i = 0; i < capacity.recordCount(); i++)
      {
        IDataTableRecord capacityRecord = capacity.getRecord(i);
        cal.setTime(capacityRecord.getDateValue("slot"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        CapacityData capacityData = (CapacityData) capacityMap.get(date);
        if (capacityData == null)
        {
          capacityData = new CapacityData();
          capacityMap.put(date, capacityData);
        }
        if (capacityRecord.getValue("tc_order_key") != null)
        {
          capacityData.bookedSlots++;
          totalBookedSlots++;
        }
        else if (capacityRecord.getintValue("locked") == 0)
        {
          capacityData.freeSlots++;
          totalFreeSlots++;
        }
        capacityData.availableSlots++;
        totalAvailableSlots++;
      }
      
      // set final members
      this.totalBookedSlots = totalBookedSlots;
      this.totalFreeSlots = totalFreeSlots;
      this.totalAvailableSlots = totalAvailableSlots;

      // collect values across active campaign range
      //
      List chartValues = new ArrayList();
      Date fromCampaign = activeCampaign.getDateValue("from");
      Date toCampaign = activeCampaign.getDateValue("to");
      cal.setTime(fromCampaign);
      do
      {
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        Date day = cal.getTime();
        CapacityData capacityData = (CapacityData) capacityMap.get(day);
        if (currentDay == 1 || chartValues.isEmpty())
          chartValues.add(new ChartValue(currentDay + "." + (currentMonth + 1), day, capacityData));
        else
          chartValues.add(new ChartValue(currentDay + ".", day, capacityData));

        // add one day
        cal.add(Calendar.DATE, 1);
      }
      while (cal.getTimeInMillis() <= toCampaign.getTime());

      this.availableData = new double[1][chartValues.size()];
      this.freeData = new double[1][chartValues.size()];
      this.workloadData = new double[1][chartValues.size()];
      this.xAxisLabels = new String[chartValues.size()];
      this.xAxisDates = new Date[chartValues.size()];
      for (int j = 0; j < chartValues.size(); j++)
      {
        ChartValue chartValue = (ChartValue) chartValues.get(j);
        this.availableData[0][j] = chartValue.getAvailableSlots();
        this.freeData[0][j] = chartValue.getFreeSlots();
        this.workloadData[0][j] = chartValue.getWorkload();
        this.xAxisLabels[j] = chartValue.label;
        this.xAxisDates[j] = chartValue.day;
      }
    }

    public double[][] getAvailableData()
    {
      return availableData;
    }

    public double[][] getFreeData()
    {
      return freeData;
    }

    public double[][] getWorkloadData()
    {
      return workloadData;
    }

    public String[] getXAxisLabels()
    {
      return xAxisLabels;
    }

    public Color[] getAvailableColors()
    {
      return new Color[]
        { DARKER_GREEN };
    }
    
    public static final Color DARKER_RED = new Color(238, 56, 0);
    public static final Color DARKER_GREEN = new Color(34, 189, 3);

    public Color[] getFreeColors()
    {
      return new Color[]
        { this.platformLocked ? DARKER_RED : DARKER_GREEN };
    }

    public Color[] getWorkloadColors()
    {
      return new Color[]
        { Color.DARK_GRAY };
    }

    public String getAvailableTitle()
    {
      StringBuffer buffer = new StringBuffer();
      if (this.overallPlatforms)
        buffer.append("Verfügbare Kapazität über alle Bühnen (");
      else
        buffer.append("Verfügbare Kapazität (");
      buffer.append(this.totalAvailableSlots);
      buffer.append(" insgesamt verfügbar)");
      return buffer.toString();
    }

    public String getFreeTitle()
    {
      if (this.platformLocked)
        return "Freie Kapazität (gesperrte Bühne!)";

      StringBuffer buffer = new StringBuffer();
      if (this.overallPlatforms)
        buffer.append("Freie Kapazität über alle Bühnen (");
      else
        buffer.append("Freie Kapazität (");
      buffer.append(this.totalFreeSlots);
      buffer.append(" insgesamt frei)");
      return buffer.toString();
    }

    public String getWorkloadTitle()
    {
      NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.GERMANY);
      percentFormat.setMaximumFractionDigits(2);

      StringBuffer buffer = new StringBuffer();
      if (this.overallPlatforms)
        buffer.append("Auslastung über alle Bühnen in Prozent (");
      else
        buffer.append("Auslastung in Prozent (");
      buffer.append(this.totalBookedSlots);
      buffer.append(" insgesamt gebucht = ");
      buffer.append(percentFormat.format((double) this.totalBookedSlots / this.totalAvailableSlots));
      buffer.append(")");
      return buffer.toString();
    }

    public Date getDate(int barIndex)
    {
      return this.xAxisDates[barIndex];
    }

    protected boolean needsRecalculation(IClientContext context)
    {
      return this.overallPlatforms ^ isOverallEnabled(context);
    }
  }

  public abstract double[][] getAvailableData();

  public abstract double[][] getFreeData();

  public abstract double[][] getWorkloadData();
  
  public Date getDate(int barIndex)
  {
    return null;
  }

  protected abstract boolean needsRecalculation(IClientContext context);

  public abstract String[] getXAxisLabels();

  public abstract Color[] getAvailableColors();

  public abstract Color[] getFreeColors();

  public abstract Color[] getWorkloadColors();

  public abstract String getAvailableTitle();

  public abstract String getFreeTitle();

  public abstract String getWorkloadTitle();
}
