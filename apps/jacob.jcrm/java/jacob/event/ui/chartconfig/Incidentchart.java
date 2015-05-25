package jacob.event.ui.chartconfig;

import jacob.common.AppLogger;

import java.awt.Color;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
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
public class Incidentchart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id$";
  static public final transient String RCS_REV = "$Revision$";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  static private final String INCEDENT_CHARTDATA = "INCEDENT_CHARTDATA";
  static public final String[] TYPE_CONSTRAINT;
  static public final String[] STATUS_CONSTRAINT;
  static
  {
    TYPE_CONSTRAINT= new String[]{"Incident","Sales Activity","Customer Management","Quote","Order"};
    STATUS_CONSTRAINT= new String[]{"New","Assigned","In Progress","Routed"};  
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
    
 
    
    public Color[] getColor()
    {
      return color;
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

    private double getTypeCountbyGroup(IDataAccessor accessor, String userKey, String typeConstraint, String statusConstraint) throws Exception
    {
      accessor.clear();
      IDataTable table = accessor.getTable("incident");
      IDataTable membertable = accessor.getTable("incidentWorkgroupMember");
      // contraint groupmember with userKey
      membertable.qbeSetValue("employee_key", userKey);

      // constraint table to display
      table.qbeSetKeyValue("type", typeConstraint);
      table.qbeSetValue("status", statusConstraint);

      return table.search("r_incident");
    }

    private double getStatusCountbyGroup(IDataAccessor accessor, String userKey, String statusConstraint) throws Exception
    {
      accessor.clear();
      IDataTable table = accessor.getTable("incident");
      IDataTable membertable = accessor.getTable("incidentWorkgroupMember");
      // contraint groupmember with userKey
      membertable.qbeSetValue("employee_key", userKey);

      // constraint table to display
      table.qbeSetValue("status", statusConstraint);

      return table.search("r_incident");
    }

    private double getTypeCount(IDataAccessor accessor, String typeConstraint, String statusConstraint) throws Exception
    {
      accessor.clear();
      IDataTable table = accessor.getTable("incident");

      // constraint table to display
      table.qbeSetKeyValue("type", typeConstraint);
      table.qbeSetValue("status", statusConstraint);

      return table.search();
    }

    private double getStatusCount(IDataAccessor accessor, String statusConstraint) throws Exception
    {
      accessor.clear();
      IDataTable table = accessor.getTable("incident");

      // constraint table to display
      table.qbeSetValue("status", statusConstraint);

      return table.search();
    }

    ChartData(IClientContext context) throws Exception
    {
      String chartType = (String) context.getProperty("ct");;
      String searchType = (String) context.getProperty("charttype");
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      this.isMyTaskSearch = "mytasks".equals(searchType);
      if ("Type".equals(chartType))
      {
        this.xAxis = ApplicationMessage.getLocalized("Incidentchart.Type");
        this.xAxisLabels = new String[]{ "In", "Ac", "Cu", "Qu", "Or" };
//        { "Incident", "Activity", "Customer", "Quote", "Order" };


        String userKey = context.getUser().getKey();
        double[][] data = new double[1][5];
        String statusConstraint = "New|Assigned|In Progress|Routed";
        if (isMyTaskSearch)
        {
          this.title = ApplicationMessage.getLocalized("Incidentchart.MyOpenIncidentsByType");
          data[0][0] = getTypeCountbyGroup(accessor, userKey, TYPE_CONSTRAINT[0], statusConstraint);
          data[0][1] = getTypeCountbyGroup(accessor, userKey, TYPE_CONSTRAINT[1], statusConstraint);
          data[0][2] = getTypeCountbyGroup(accessor, userKey, TYPE_CONSTRAINT[2], statusConstraint);
          data[0][3] = getTypeCountbyGroup(accessor, userKey, TYPE_CONSTRAINT[3], statusConstraint);
          data[0][4] = getTypeCountbyGroup(accessor, userKey, TYPE_CONSTRAINT[4], statusConstraint);
        }
        else
        {
          this.title = ApplicationMessage.getLocalized("Incidentchart.AllOpenIncidentsByType");
          data[0][0] = getTypeCount(accessor, TYPE_CONSTRAINT[0], statusConstraint);
          data[0][1] = getTypeCount(accessor, TYPE_CONSTRAINT[1], statusConstraint);
          data[0][2] = getTypeCount(accessor, TYPE_CONSTRAINT[2], statusConstraint);
          data[0][3] = getTypeCount(accessor, TYPE_CONSTRAINT[3], statusConstraint);
          data[0][4] = getTypeCount(accessor, TYPE_CONSTRAINT[4], statusConstraint);
        }
        this.dataElements=data;
      }
      else
      {
        this.xAxis = ApplicationMessage.getLocalized("Incidentchart.State");
        this.xAxisLabels = new String[]
        { "New", "Assigned", "In Progress", "Routed" };
        String userKey = context.getUser().getKey();
        double[][] data = new double[1][4];
        if (isMyTaskSearch)
        {
          this.title = ApplicationMessage.getLocalized("Incidentchart.MyOpenIncidentsByState");
          data[0][0] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[0]);
          data[0][1] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[1]);
          data[0][2] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[2]);
          data[0][3] = getStatusCountbyGroup(accessor, userKey, STATUS_CONSTRAINT[3]);
        }
        else
        {
          this.title = ApplicationMessage.getLocalized("Incidentchart.AllOpenIncidentsByState");
          data[0][0] = getStatusCount(accessor, STATUS_CONSTRAINT[0]);
          data[0][1] = getStatusCount(accessor, STATUS_CONSTRAINT[1]);
          data[0][2] = getStatusCount(accessor, STATUS_CONSTRAINT[2]);
          data[0][3] = getStatusCount(accessor, STATUS_CONSTRAINT[3]);
        }
        this.dataElements=data;
      }
      this.yAxis = ApplicationMessage.getLocalized("Incidentchart.Incidents");
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
    ChartData data = (ChartData) context.getProperty(INCEDENT_CHARTDATA);
    if (data != null)
      return data;

    data = new ChartData(context);
    context.setPropertyForRequest(INCEDENT_CHARTDATA, data);
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
    try
    {
       context.getDomain("f_incident");
    }
    catch (Exception e)
    {
      return;
    }
    IDataAccessor accessor =context.getDataAccessor();
    accessor.clear();
    IDataTable incident = accessor.getTable("incident");
    IDataTable membertable = accessor.getTable("incidentWorkgroupMember");

   
    boolean myIncidens = getChartData(context).chekIsMyTaskSearch();
    String chartType = (String) context.getProperty("ct");
    String statusConstraint = "New|Assigned|In Progress|Routed";
      
    if ("Type".equals(chartType))
    {
      incident.qbeSetValue("type",TYPE_CONSTRAINT[barIndex]); 
      incident.qbeSetValue("status", statusConstraint);
    }
    else // chartType is Status
    {
      incident.qbeSetValue("status",STATUS_CONSTRAINT[barIndex]);
    }
    // constraint user if necessary
    if(myIncidens)
    {
      membertable.qbeSetValue("employee_key", context.getUser().getKey());
    }
    
    IDataBrowser browser= context.getDataBrowser("incidentBrowser");
    
    
    
    browser.search("r_incident");
    if (browser.recordCount()==1)
    {
      browser.setSelectedRecordIndex(0);
      browser.propagateSelections();
    }

    IForm newForm = (IForm) context.getDomain("f_incident").findByName("incident");
    System.out.println(newForm.getName());
    IGroup group = (IGroup) newForm.findByName("incidentGroup");
    group.getBrowser().setData(context,browser);
    newForm.setCurrentBrowser(group.getBrowser());


        
    context.setCurrentForm("f_incident","incident");
    
    
    
    
  }

}
