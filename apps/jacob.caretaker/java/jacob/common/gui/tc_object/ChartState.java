package jacob.common.gui.tc_object;

import jacob.common.tc.Slot;
import jacob.common.tc.TC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBERangeExpression;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.util.StringUtil;

public class ChartState
{
  private static final String SLOTCHART_STATE = "Slotchart_State";
  
  private static final int WEEKS_STATE = 0;
  private static final int WEEK_STATE = 1;
  private static final int DAY_STATE = 2;
  private static final int HOUR_STATE = 3;
  private static final int NO_CAPACITY_STATE = 4;
  private static final int UNAVAILABLE_STATE = 5;

  private int currentState = UNAVAILABLE_STATE;
  
  private int currentWeekIndex = -1;
  
  private Date currentDay = null;
  
  private int currentHour = -1;

  /**
   * Map(Date -> DayValue)
   */
  private final Map capacityMap = new HashMap();

  /**
   * List(WeekValue)
   */
  private final List weekValues = new ArrayList();
  
  /**
   * Helper class to collect slot data of a day.
   * 
   */
  private static class DayValue
  {
    int totalSlots = 0;

    /**
     * slots per hour
     */
    int[] slotsPerHour = new int[24];

    /**
     * slots per hour slot of 15 min
     */
    int[][] slotsPerHourSlot = new int[24][Slot.TOTAL_HOURS];

    void addSlot(int hour, int mins)
    {
      slotsPerHour[hour]++;
      slotsPerHourSlot[hour][mins/Slot.SLOT_SIZE]++;
      if (hour >= Slot.FIRST_HOUR && hour < Slot.FIRST_HOUR + Slot.TOTAL_HOURS)
      {
        totalSlots++;
      }
    }
  }

  private static final String[] HOUR_NAMES =
    { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

  private static final String[] HOUR_SLOT_NAMES =
    { ":00", ":15", ":30", ":45" };

  private static final int[] HOUR_SLOTS =
    { 0, 15, 30, 45 };

  private static final String[] WEEK_DAY_NAMES =
    { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };

  /**
   * Array to use as <code>WEEK_DAY_NAMES_PER_JAVA[Calendar.MONDAY]</code>
   */
  private static final String[] WEEK_DAY_NAMES_PER_JAVA =
    { "??", "So", "Mo", "Di", "Mi", "Do", "Fr", "Sa" };

  private static final DateFormat weekdayFormatter = new SimpleDateFormat("dd.MM.yy");
  
  /**
   * Helper class to collect slot data of a week.
   * 
   */
  private static class WeekValue
  {
    final long weekBeginInMillis;
    final int weekOfYear;
    final String label;
    final String shortLabel;
    final String[] dayLabels;
    int totalSlots = 0;

    WeekValue(Calendar cal)
    {
      this.weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

      // calculate day labels
      //
      Calendar tempCal = (Calendar) cal.clone();
      tempCal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
      tempCal.set(Calendar.HOUR_OF_DAY, 0);
      tempCal.set(Calendar.MINUTE, 0);
      tempCal.set(Calendar.SECOND, 0);
      this.weekBeginInMillis = tempCal.getTimeInMillis();
      this.dayLabels = new String[7];
      String startday = weekdayFormatter.format(tempCal.getTime());
      for (int i = 0; i < 7; i++)
      {
        this.dayLabels[i] = WEEK_DAY_NAMES[i] + ", " + weekdayFormatter.format(tempCal.getTime());
        tempCal.add(Calendar.DAY_OF_MONTH, 1);
      }
      tempCal.add(Calendar.DAY_OF_MONTH, -1);
      this.label = "KW " + this.weekOfYear + ", " + startday;
      this.shortLabel = "KW " + this.weekOfYear;
    }

    String[] getDayLabels()
    {
      return dayLabels;
    }
  }
  
  private static class TimeFilter
  {
    /**
     * [hour,min]
     */
    private final int[] from, to;
    private final boolean range;
    
    TimeFilter(String fromHour, String toHour) throws Exception
    {
      this.from = parse(fromHour);
      this.to = parse(toHour);
      this.range = true;
    }
    
    TimeFilter(String hour) throws Exception
    {
      this.from = this.to = parse(hour);
      this.range = false;
    }

    private static int[] parse(String hour) throws Exception
    {
      int [] res = new int[2];
      
      hour = hour.trim();
      int idx = hour.indexOf(":");
      if (idx == -1)
      {
        res[0] = Integer.parseInt(hour);
        res[1] = 0;
      }
      else
      {
        res[0] = Integer.parseInt(hour.substring(0, idx));
        res[1] = Integer.parseInt(hour.substring(idx+1));
      }
      return res;
    }
    
    private static void append(StringBuffer buffer, int[] hour)
    {
      buffer.append(hour[0]);
      buffer.append(":");
      if (hour[1] <= 9)
        buffer.append("0");
      buffer.append(hour[1]);
    }
    
    public boolean contains(Calendar slot)
    {
      int hour = slot.get(Calendar.HOUR_OF_DAY);
      int min = slot.get(Calendar.MINUTE);
      if (this.range)
      {
        long slotMillis = slot.getTimeInMillis();
        Calendar cal = (Calendar) slot.clone();
        cal.set(Calendar.HOUR_OF_DAY, this.from[0]);
        cal.set(Calendar.MINUTE, this.from[1]);
        long fromMillis = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, this.to[0]);
        cal.set(Calendar.MINUTE, this.to[1]);
        long toMillis = cal.getTimeInMillis();
        return slotMillis >= fromMillis && slotMillis <= toMillis;
      }
      return hour == this.from[0] && min == this.from[1];
    }

    public String toString()
    {
      StringBuffer buffer = new StringBuffer();

      append(buffer, this.from);
      if (this.range)
      {
        buffer.append("..");
        append(buffer, this.to);
      }
      return buffer.toString();
    }
  }
  
  private static TimeFilter getFilter(IClientContext context, String wishedTime)
  {
    try
    {
      try
      {
        // first: try with time format
        //
        QBEExpression expr = QBEExpression.parseTime(wishedTime);
        if (expr != null)
        {
          if (expr instanceof QBERangeExpression)
          {
            int idx = wishedTime.indexOf("..");
            return new TimeFilter(wishedTime.substring(0, idx), wishedTime.substring(idx + 2));
          }
          return new TimeFilter(wishedTime);
        }
      }
      catch (InvalidExpressionException ex)
      {
        // second: try with integer format
        //
        QBEExpression expr = QBEExpression.parseInteger(wishedTime);
        if (expr != null)
        {
          if (expr instanceof QBERangeExpression)
          {
            int idx = wishedTime.indexOf("..");
            return new TimeFilter(wishedTime.substring(0, idx), wishedTime.substring(idx + 2));
          }
          return new TimeFilter(wishedTime);
        }
      }
    }
    catch (Exception ex)
    {
      // Show an error dialog, if wished time could not be considered
      context.createMessageDialog("Ungültige Wunschzeit wurde nicht ausgewertet!").show();
    }

    return null;
  }

  private ChartState(IClientContext context)
  {
    // nothing more to do
  }
  
  private ChartState(IClientContext context, IDataTableRecord activeCampaign)
  {
    try
    {
      // determine time filter if existing
      //
      ISingleDataGuiElement wishedTimeElement = (ISingleDataGuiElement) context.getGroup().findByName("wishedTime");
      String wishedTime = StringUtil.toSaveString(wishedTimeElement.getValue());
      TimeFilter timeFilter = getFilter(context, wishedTime);
      if (timeFilter != null)
      {
        // properly format filter
        wishedTimeElement.setValue(timeFilter.toString());
      }
      
      // fetch unlocked capacity entries for the active campaign
      //
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      IDataTable platformTable = accessor.getTable("tc_platform");
      platformTable.qbeClear();
      platformTable.qbeSetKeyValue("locked", "0");
      IDataTable capacityTable = accessor.getTable("tc_capacity");
      capacityTable.qbeClear();
      capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
      capacityTable.qbeSetKeyValue("locked", "0");
      capacityTable.qbeSetValue("slot", ">now");
      capacityTable.qbeSetValue("tc_order_key", "null");
      capacityTable.setMaxRecords(IDataBrowser.UNLIMITED_RECORDS);
      capacityTable.search("r_tc_capacity_platform");

      // examine capacity entries
      //
      Calendar cal = new GregorianCalendar();
      // always start week with monday
      cal.setFirstDayOfWeek(Calendar.MONDAY);
      for (int i = 0; i < capacityTable.recordCount(); i++)
      {
        IDataTableRecord capacityRecord = capacityTable.getRecord(i);
        Date slot = capacityRecord.getDateValue("slot");
        cal.setTime(slot);
        cal.set(Calendar.SECOND, 0);
        if (timeFilter != null)
        {
          // time filter defined
          if (!timeFilter.contains(cal))
            // ignore slots which do not match
            continue;
        }
        int slotHour = cal.get(Calendar.HOUR_OF_DAY);
        int slotMins = cal.get(Calendar.MINUTE);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date date = cal.getTime();
        DayValue capacityData = (DayValue) capacityMap.get(date);
        if (capacityData == null)
        {
          capacityData = new DayValue();
          capacityMap.put(date, capacityData);
        }
        capacityData.addSlot(slotHour, slotMins);
      }

      // collect weekly based values across active campaign range
      //
      Date fromCampaign = activeCampaign.getDateValue("from");
      Date toCampaign = activeCampaign.getDateValue("to");
      cal.setTime(fromCampaign);
      do
      {
        DayValue capacityData = (DayValue) capacityMap.get(cal.getTime());
        if (capacityData != null)
        {
          WeekValue weekValue;
          int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
          if (weekValues.isEmpty())
          {
            weekValue = new WeekValue(cal);
            weekValues.add(weekValue);
          }
          else
          {
            // get last value
            weekValue = (WeekValue) weekValues.get(weekValues.size() - 1);

            if (weekValue.weekOfYear != currentWeek)
            {
              weekValue = new WeekValue(cal);
              weekValues.add(weekValue);
            }
          }
          weekValue.totalSlots += capacityData.totalSlots;
        }

        // add one day
        cal.add(Calendar.DATE, 1);
      }
      while (cal.getTimeInMillis() <= toCampaign.getTime());

      // calculate initial state
      //
      if (this.weekValues.size() > 1)
      {
        this.currentState = WEEKS_STATE;
      }
      else if (this.weekValues.size() == 0)
      {
        this.currentState = NO_CAPACITY_STATE;
      }
      else if (this.weekValues.size() == 1)
      {
        this.currentWeekIndex = 0;
        
        if (capacityMap.size() == 1)
        {
          this.currentDay = (Date) this.capacityMap.keySet().iterator().next();
          this.currentState = DAY_STATE;
        }
        else
        {
          this.currentState = WEEK_STATE;
        }
      }
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

  public String getTitle()
  {
    switch (this.currentState)
    {
      case WEEKS_STATE:
        return "Wählen Sie eine Kalenderwoche aus!";

      case WEEK_STATE:
        return "Wählen Sie einen Wochentag von Kalenderwoche " + ((WeekValue) this.weekValues.get(this.currentWeekIndex)).shortLabel + " aus!";

      case DAY_STATE:
        {
          Calendar cal = new GregorianCalendar();
          cal.setTime(this.currentDay);
          return "Wählen Sie eine Stunde von " + WEEK_DAY_NAMES_PER_JAVA[cal.get(Calendar.DAY_OF_WEEK)] + ", " + weekdayFormatter.format(cal.getTime())
              + " aus!";
        }
        
      case HOUR_STATE:
        return "Wählen Sie einen Termin aus!";
        
      case NO_CAPACITY_STATE:
        return "Zur Zeit sind keine freien Kapazitäten vorhanden!";

      default:
        return " ";
    }
  }

  private static final String[] XAXIS_TITLES =
    { "Kalenderwoche", "Wochentag", "Stunde", "Termin", "Nicht vorhanden", "Termine noch nicht berechnet" };

  public String getXAxisTitle()
  {
    return XAXIS_TITLES[this.currentState];
  }

  public String getYAxisTitle()
  {
    return "Freie Kapazität";
  }
  
  private static final String[] NO_LABELS = new String[0];
  
  public String[] getXAxisLabels()
  {
    switch (this.currentState)
    {
      case WEEKS_STATE:
      {
        String[] result = new String[this.weekValues.size()];
        for (int i = 0; i < this.weekValues.size(); i++)
        {
          result[i] = ((WeekValue) this.weekValues.get(i)).label;
        }
        return result;
      }

      case WEEK_STATE:
        return ((WeekValue) this.weekValues.get(this.currentWeekIndex)).getDayLabels();

      case DAY_STATE:
      {
        String[] res = new String[Slot.TOTAL_HOURS];
        System.arraycopy(HOUR_NAMES, Slot.FIRST_HOUR, res, 0, Slot.TOTAL_HOURS);
        return res;
      }
          
      case HOUR_STATE:
      {
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.currentDay);
        String dayStr = WEEK_DAY_NAMES_PER_JAVA[cal.get(Calendar.DAY_OF_WEEK)] + ", " + weekdayFormatter.format(cal.getTime());
        String[] result = new String[Slot.SLOTS_PER_HOUR];
        for (int i = 0; i < Slot.SLOTS_PER_HOUR; i++)
        {
          result[i] = dayStr + " " + this.currentHour + HOUR_SLOT_NAMES[i] + " Uhr";
        }
        return result;
      }
          
      default:
        return NO_LABELS;
    }
  }

  private static final double[][] NO_DATA = new double[1][0];
  
  public double[][] getDataElements()
  {
    switch (this.currentState)
    {
      case WEEKS_STATE:
      {
        double[][] result = new double[1][this.weekValues.size()];
        for (int i = 0; i < this.weekValues.size(); i++)
        {
          result[0][i] = TC.adjustHack(((WeekValue) this.weekValues.get(i)).totalSlots);
        }
        return result;
      }

      case WEEK_STATE:
      {
        WeekValue weekValue = (WeekValue) this.weekValues.get(this.currentWeekIndex);
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(weekValue.weekBeginInMillis);
        double[][] result = new double[1][7];
        for (int i = 0; i < 7; i++)
        {
          Date day = cal.getTime();
          DayValue dayValue = (DayValue) this.capacityMap.get(day);
          result[0][i] = TC.adjustHack(dayValue == null ? 0 : dayValue.totalSlots);

          // add one day
          cal.add(Calendar.DATE, 1);
        }
        return result;
      }

      case DAY_STATE:
        {
          DayValue dayValue = (DayValue) this.capacityMap.get(this.currentDay);
          double[][] result = new double[1][Slot.TOTAL_HOURS];
          for (int i = 0; i < Slot.TOTAL_HOURS; i++)
          {
            result[0][i] = TC.adjustHack(dayValue == null ? 0 : dayValue.slotsPerHour[i + Slot.FIRST_HOUR]);
          }
          return result;
        }

      case HOUR_STATE:
        {
          DayValue dayValue = (DayValue) this.capacityMap.get(this.currentDay);
          double[][] result = new double[1][Slot.SLOTS_PER_HOUR];
          for (int i = 0; i < Slot.SLOTS_PER_HOUR; i++)
          {
            result[0][i] = TC.adjustHack(dayValue == null ? 0 : dayValue.slotsPerHourSlot[this.currentHour][i]);
          }
          return result;
        }

      default:
        return NO_DATA;
    }
  }
  
  public static final DateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  
  private void createOrderRecord(IClientContext context, int barIndex) throws Exception
  {
    // Check whether free capacities are still unlocked.
    // If yes select one, i.e. make it the selected record
    //
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);

    Calendar cal = new GregorianCalendar();
    cal.setTime(this.currentDay);
    cal.add(Calendar.HOUR_OF_DAY, this.currentHour);
    cal.add(Calendar.MINUTE, HOUR_SLOTS[barIndex]);
    String from = timeFormatter.format(cal.getTime());
    cal.add(Calendar.MINUTE, 15);
    String to = timeFormatter.format(cal.getTime());
    
    IDataTable platformTable = context.getDataTable("tc_platform");
    platformTable.qbeClear();
    platformTable.qbeSetKeyValue("locked", "0");
    IDataTable capacityTable = context.getDataTable("tc_capacity");
    capacityTable.qbeClear();
    capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
    capacityTable.qbeSetKeyValue("locked", "0");
    capacityTable.qbeSetValue("tc_order_key", "null");
    // range is exclusive <to>, i.e. [from..to[
    capacityTable.qbeSetValue("slot", from + ".." + to);
    // we just want one record which should become the selected one
    capacityTable.setMaxRecords(1);
    if ( 0 == capacityTable.search("r_tc_capacity_platform"))
    {
      context.createMessageDialog("Es sind keine freien Kapazitäten mehr vorhanden!").show();
      return;
    }

    // create new order record, if not already done
    //
    IDataTable orderTable = context.getDataTable("tc_order");
    IDataTableRecord orderRecord;
    IDataTransaction trans = orderTable.getTableTransaction();
    if (trans == null)
    {
      trans = orderTable.startNewTransaction();
      orderRecord = orderTable.newRecord(trans);
    }
    else
    {
      // we are already in new mode
      orderRecord = orderTable.getSelectedRecord();
    }
  }
  
  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    switch (this.currentState)
    {
      case WEEKS_STATE:
        this.currentWeekIndex = barIndex;
        this.currentState = WEEK_STATE;
        this.currentDay = null;
        break;

      case WEEK_STATE:
        WeekValue weekValue = (WeekValue) this.weekValues.get(this.currentWeekIndex);
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(weekValue.weekBeginInMillis);
        cal.add(Calendar.DATE, barIndex);
          
        this.currentState = DAY_STATE;
        this.currentDay = cal.getTime();
        break;

      case DAY_STATE:
        this.currentState = HOUR_STATE;
        this.currentHour = Slot.FIRST_HOUR + barIndex;
        break;

      case HOUR_STATE:
        createOrderRecord(context, barIndex);
        break;

      default:
        // do nothing
        return;
    }

    chart.refresh();
  }
  
  public boolean hasPreviousState()
  {
    switch (this.currentState)
    {
      case WEEK_STATE:
        return this.weekValues.size() > 1;

      case DAY_STATE:
        return this.capacityMap.size() > 1;

      case HOUR_STATE:
        return true;

      default:
        return false;
    }
  }

  public void setPreviousState()
  {
    switch (this.currentState)
    {
      case WEEK_STATE:
        this.currentState = WEEKS_STATE;
        this.currentWeekIndex = -1;
        break;

      case DAY_STATE:
        this.currentState = WEEK_STATE;
        this.currentDay = null;
        break;

      case HOUR_STATE:
        this.currentState = DAY_STATE;
        this.currentHour = -1;
        break;

      default:
        // do nothing
        return;
    }
  }

  public static ChartState getChartState(IClientContext context)
  {
    ChartState chartZustand = (ChartState) context.getProperty(SLOTCHART_STATE);
    if (chartZustand == null)
    {
      chartZustand = new ChartState(context);
      context.setPropertyForWindow(SLOTCHART_STATE, chartZustand);
    }

    return chartZustand;
  }
  
  public static void discardChartState(IClientContext context)
  {
    context.setPropertyForWindow(SLOTCHART_STATE, null);
  }
  
  public static ChartState createChartState(IClientContext context) throws Exception 
  {
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);
    ChartState chartZustand = new ChartState(context, activeCampaign);
    context.setPropertyForWindow(SLOTCHART_STATE, chartZustand);
    return chartZustand;
  }
}
