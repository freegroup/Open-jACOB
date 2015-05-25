package jacob.event.ui.chartconfig;

import jacob.common.AppLogger;
import jacob.model.Chartconfig;

import java.awt.Color;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.model.Alert;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IBarChartEventHandler;

/**
 * @author achim
 *
 */
public class Activitychart extends IBarChartEventHandler
{
    static public final transient String RCS_ID = "$Id$";
    static public final transient String RCS_REV = "$Revision$";

    // use this logger to write messages and NOT the System.out.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();
    
    static private final String ACTIVITY_CHARTDATA = "ACTIVITY_CHARTDATA";
    static public final String[] STATUS_CONSTRAINT;
    static private final int ROW_TYPE_KEY =0;
    static private final int ROW_TYPE_NAME =1;
	static
    {
      STATUS_CONSTRAINT= new String[]{ "Not Started","In Progress","Deferred"};  
    } 
    // private class for collecting chart data
    static private class ChartData
    {

      private String title;
      private String xAxis;
      private String yAxis;
      private String[] xAxisLabels;
      private String[] legendLabels;
      private Color[] color;
      private double[][] dataElements;
      private boolean isMyTaskSearch;
      public  String[][] typeInfo;
  
      
      public Color[] getColor()
      {
        return color;
      }
      public String[][] getTypeInfo()
      {
        return typeInfo;
      }

      public double[][] getDataElements()
      {
        return dataElements;
      }

      public String[] getLegendLabels()
      {
        return legendLabels;
      }

      public String getXAxis()
      {
        return xAxis;
      }
      public boolean chekIsMyTaskSearch()
      {
        return isMyTaskSearch;
      }
      public String[] getXAxisLabels()
      {
        return xAxisLabels;
      }

      public String getYAxis()
      {
        return yAxis;
      }

      public String getTitle()
      {
        return title;
      }

      protected double getTypeCountbyGroup(IDataAccessor accessor, String userKey, String typeConstraint, String statusConstraint) throws Exception
      {
        accessor.clear();
        IDataTable table = accessor.getTable("activity");
        IDataTable membertable = accessor.getTable("activityWorkgroupMember");
        // contraint groupmember with userKey
        membertable.qbeSetValue("employee_key", userKey);

        // constraint table to display
        table.qbeSetKeyValue("activitytype_key", typeConstraint);
        table.qbeSetValue("status", statusConstraint);

        return table.search("r_sales");
      }

      protected double getStatusCountbyGroup(IDataAccessor accessor, String userKey, String statusConstraint) throws Exception
      {
        accessor.clear();
        IDataTable table = accessor.getTable("activity");
        IDataTable membertable = accessor.getTable("activityWorkgroupMember");
        // contraint groupmember with userKey
        membertable.qbeSetValue("employee_key", userKey);

        // constraint table to display
        table.qbeSetValue("status", statusConstraint);

        return table.search("r_sales");
      }

      protected double getTypeCount(IDataAccessor accessor, String typeConstraint, String statusConstraint) throws Exception
      {
        accessor.clear();
        IDataTable table = accessor.getTable("activity");

        // constraint table to display
        table.qbeSetKeyValue("activitytype_key", typeConstraint);
        table.qbeSetValue("status", statusConstraint);

        return table.search();
      }

      protected double getStatusCount(IDataAccessor accessor, String statusConstraint) throws Exception
      {
        accessor.clear();
        IDataTable table = accessor.getTable("activity");

        // constraint table to display
        table.qbeSetValue("status", statusConstraint);

        return table.search();
      }
      protected String[][] getActivityTypeInfo(IDataAccessor accessor) throws Exception
      {
          IDataTable activityType = accessor.getTable("activitytype");
          activityType.clear();
          activityType.search();
          
          int recordCount = activityType.recordCount();
          String[][] info = new String[2][recordCount];
          for (int i = 0; i < recordCount; i++)
        {
            IDataTableRecord record = activityType.getRecord(i); 
            info[ROW_TYPE_KEY][i]= record.getSaveStringValue("pkey");
            info[ROW_TYPE_NAME][i]= record.getSaveStringValue("name").substring(0,2);
        }
        return info;  
      }
      ChartData(IClientContext context) throws Exception
      {
       

        
        String chartType = (String) context.getProperty("ct");
        System.out.println("6----------");
        String searchType = (String) context.getProperty("charttype");
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        this.isMyTaskSearch = "mytasks".equals(searchType);
        if ("Type".equals(chartType))
        {
          this.typeInfo = getActivityTypeInfo(accessor);
          this.xAxis = ApplicationMessage.getLocalized("Activitychart.Type");
          
          this.xAxisLabels = typeInfo[ROW_TYPE_NAME];

          String userKey = context.getUser().getKey();
          double[][] data = new double[1][typeInfo[ROW_TYPE_NAME].length];
          String statusConstraint = "Not Started|In Progress|Deferred";
          if (isMyTaskSearch)
          {
            this.title = ApplicationMessage.getLocalized("Activitychart.MyOpenActivitiesByType");
            for (int i = 0; i < typeInfo[ROW_TYPE_NAME].length; i++)
            {
                data[0][i] = getTypeCountbyGroup(accessor, userKey, typeInfo[ROW_TYPE_KEY][i], statusConstraint);   
            }
          }
          else
          {
            this.title = ApplicationMessage.getLocalized("Activitychart.AllOpenActivitiesByType");
            for (int i = 0; i < typeInfo[ROW_TYPE_NAME].length; i++)
            {
                data[0][i] = getTypeCount(accessor, typeInfo[ROW_TYPE_KEY][i], statusConstraint);   
            }
          }
          this.dataElements=data;
        }
        else
        {
          this.xAxis = ApplicationMessage.getLocalized("Activitychart.State");
          this.xAxisLabels = new String[]{ "Not Started","In Progress","Deferred"}; 
          String userKey = context.getUser().getKey();
          double[][] data = new double[1][3];
          if (isMyTaskSearch)
          {
            this.title = ApplicationMessage.getLocalized("Activitychart.MyOpenActivitiesByState");
            data[0][0] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[0]);
            data[0][1] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[1]);
            data[0][2] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[2]);
          }
          else
          {
            this.title = ApplicationMessage.getLocalized("Activitychart.AllOpenActivitiesByState");
            data[0][0] = getStatusCount(accessor, STATUS_CONSTRAINT[0]);
            data[0][1] = getStatusCount(accessor, STATUS_CONSTRAINT[1]);
            data[0][2] = getStatusCount(accessor, STATUS_CONSTRAINT[2]);
          }
          this.dataElements=data;
        }
        this.yAxis = ApplicationMessage.getLocalized("Activitychart.Activities");
        this.legendLabels = null;
        // RED GREEN BLUE ALPHA
        this.color = new Color[]
        { new Color((int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), 255) };
      }

      /**
       * little help method for test data
       */
      private static double getRandomNumber(double minValue, double maxValue) throws Exception
      {
        return (minValue + (Math.random() * (maxValue - minValue)));
      }
    }

    // -----------------------------------------------------------------

    private static ChartData getChartData(IClientContext context) throws Exception
    {
      ChartData data = (ChartData) context.getProperty(ACTIVITY_CHARTDATA);
      if (data != null)
        return data;

      data = new ChartData(context);
      context.setPropertyForRequest(ACTIVITY_CHARTDATA, data);
      return data;
    }

    /**
     * 
     * @param context
     *          the current working context
     * @param chart
     *          the chart GUI element
     * @return The title of the line chart
     */
    public String getTitle(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getTitle();
    }

    /**
     * 
     * @param context
     *          the current working context
     * @param chart
     *          the chart GUI element
     * @return The caption for the x-axis
     */
    public String getXAxisTitle(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getXAxis();
    }

    /**
     * 
     * @param context
     *          the current working context
     * @param chart
     *          the chart GUI element
     * @return The caption for the y-axis
     */
    public String getYAxisTitle(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getYAxis();
    }

    /**
     * Return the labels for the x-axis. The count of the labels <b>must</b> be
     * match with the count of the returnd data elements.
     * 
     * @param context
     *          the current working context
     * @param chart
     *          the chart GUI element
     * @return the labels for the x-axis
     */
    public String[] getXAxisLabels(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getXAxisLabels();
    }

    /**
     * Return the legend label for each data set/line in the chart.
     * 
     * @param context
     * @param chart
     * @returnReturn the legend label for each data set/line in the chart.
     */
    public String[] getLegendLabels(IClientContext context, IChart chart) throws Exception
    {

      return getChartData(context).getLegendLabels();
    }

    /**
     * Return the different colors of the chart lines.
     * 
     * @param context
     * @param chart
     * @return Return the different colors of the chart lines.
     */
    public Color[] getColors(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getColor();
    }

    /**
     * Return a array of data values.<br>
     * 
     * @param context
     *          the current working context
     * @param chart
     *          the chart GUI element
     * @return
     */
    public double[][] getDataElements(IClientContext context, IChart chart) throws Exception
    {
      return getChartData(context).getDataElements();
    }

    public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
    {
      // In die neue Form springen
      //
      context.setCurrentForm("f_sales","activity");
      // Anmerkung: ab jetzt bezieht sich der DatenAccessor auf die NEUE Form

      
      IDataAccessor accessor =context.getDataAccessor();
      accessor.clear();
      IDataTable activityTable = accessor.getTable("activity");
      IDataTable membertable = accessor.getTable("incidentWorkgroupMember");

      boolean myActivities = getChartData(context).chekIsMyTaskSearch();
      String chartType = (String) context.getProperty("ct");
      String statusConstraint = "Not Started|In Progress|Deferred";
      if ("Type".equals(chartType))
      {
        activityTable.qbeSetKeyValue("activitytype_key",getChartData(context).getTypeInfo()[ROW_TYPE_KEY][barIndex]); 
        activityTable.qbeSetValue("status", statusConstraint);
      }
      else // chartType is Status
      {
        activityTable.qbeSetValue("status",STATUS_CONSTRAINT[barIndex]);
      }
      // constraint user if necessary
      if(myActivities)
      {
        membertable.qbeSetValue("employee_key", context.getUser().getKey());
      }

      
      IDataBrowser browser= context.getDataBrowser("activityBrowser");
      
      browser.search("r_sales");
      if (browser.recordCount()==1)
      {
        browser.setSelectedRecordIndex(0);
        browser.propagateSelections();
      }

      IForm newForm = (IForm) context.getDomain("f_sales").findByName("activity");
      System.out.println(newForm.getName());
      IGroup group = (IGroup) newForm.findByName("activityGroup");

      group.getBrowser().setData(context,browser);
      newForm.setCurrentBrowser(group.getBrowser());


    }

}
