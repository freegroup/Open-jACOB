/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 11 14:34:46 CET 2006
 */
package jacob.event.ui.opencalls;

import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Groupmember;
import jacob.model.Opencalls;

import java.awt.Color;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IBarChartEventHandler;

/**
 * 
 * @author mike
 */
public class OpencallsBarChart extends IBarChartEventHandler
{

  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    String[] status = new String[4];
    status[0] = Call.callstatus_ENUM._New;
    status[1] = Call.callstatus_ENUM._Assigned;
    status[2] = Call.callstatus_ENUM._Owned;
    status[3] = Call.callstatus_ENUM._QA;

      // read DB jump to Form
    context.clearDomain();
    IDataTable call = context.getDataTable(Call.NAME);
    IDataTable groupmember = context.getDataTable(Groupmember.NAME);
    call.qbeClear();
    groupmember.qbeClear();
    IDataBrowser browser= context.getDataBrowser("callBrowser");

    if (status[barIndex].equals(Call.callstatus_ENUM._Assigned))
    {
      groupmember.qbeSetKeyValue(Groupmember.employee_key, context.getUser().getKey());  
    }
    if (status[barIndex].equals(Call.callstatus_ENUM._Owned)||status[barIndex].equals(Call.callstatus_ENUM._QA))
    {
      call.qbeSetKeyValue(Call.callowner_key,  context.getUser().getKey());
    }
    call.qbeSetValue(Call.callstatus, status[barIndex]);
    browser.search("r_call");
    if (browser.recordCount()==1)
    {
      browser.setSelectedRecordIndex(0);
      browser.propagateSelections();
    }

    IForm newForm = (IForm) context.getDomain().findByName("callManage");
    IGroup group = (IGroup) newForm.findByName("callGroup");

    newForm.setCurrentBrowser(group.getBrowser());
    context.setCurrentForm(newForm.getName());

    super.onClick(context, chart, barIndex);
  }
  static public final transient String RCS_ID = "$Id: OpencallsBarChart.java,v 1.3 2006/11/10 07:51:57 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  int numberOfDatasets = 1;
  int numToGenerate = 4;

  /**
   * 
   * @param context
   *          the current working context
   * @param chart
   *          the chart GUI element
   * @return The title of the line chart
   */
  public String getTitle(IClientContext context, IChart chart)
  {
    return ApplicationMessage.getLocalized("OpencallsBarChart.1");
  }

  /**
   * 
   * @param context
   *          the current working context
   * @param chart
   *          the chart GUI element
   * @return The caption for the x-axis
   */
  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return ApplicationMessage.getLocalized("OpencallsBarChart.2");
  }

  /**
   * 
   * @param context
   *          the current working context
   * @param chart
   *          the chart GUI element
   * @return The caption for the y-axis
   */
  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return ApplicationMessage.getLocalized("OpencallsBarChart.3"); 
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
  public String[] getXAxisLabels(IClientContext context, IChart chart)
  {
    String[] result = new String[4];

    result[0] = Call.callstatus_ENUM._New;
    result[1] = Call.callstatus_ENUM._Assigned;
    result[2] = Call.callstatus_ENUM._Owned;
    result[3] = Call.callstatus_ENUM._QA;

    return result;
  }

  /**
   * Return the legend label for each data set/line in the chart.
   * 
   * @param context
   * @param chart
   * @returnReturn the legend label for each data set/line in the chart.
   */
  public String[] getLegendLabels(IClientContext context, IChart chart)
  {
    String[] labels = new String[1];
    labels[0] = ApplicationMessage.getLocalized("OpencallsBarChart.4"); 
    return labels;
  }

  /**
   * Return the different colors of the chart lines.
   * 
   * @param context
   * @param chart
   * @return Return the different colors of the chart lines.
   */
  public Color[] getColors(IClientContext context, IChart chart)
  {
    Color[] colors = new Color[numberOfDatasets];
    for (int j = 0; j < numberOfDatasets; j++)
    { // RED GREEN BLUE ALPHA
      colors[j] = new Color((int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), 255);
    }
    return colors;
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
    double[][] data = new double[numberOfDatasets][numToGenerate];
    data[0][0] = getRecordcount(context, Call.callstatus_ENUM._New);
    data[0][1] = getRecordcount(context, Call.callstatus_ENUM._Assigned);
    data[0][2] = getRecordcount(context, Call.callstatus_ENUM._Owned);
    data[0][3] = getRecordcount(context, Call.callstatus_ENUM._QA);

    return data;

  }

  /**
   * little help method for test data
   */
  private static double getRandomNumber(double minValue, double maxValue)
  {
    return (minValue + (Math.random() * (maxValue - minValue)));
  }

  private static double getRecordcount(IClientContext context, String status) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable call = acc.getTable(Opencalls.NAME);
    IDataTable groupmember = acc.getTable(Groupmember.NAME);
    call.qbeClear();
    groupmember.qbeClear();
    IDataBrowser browser;
    browser = acc.getBrowser("opencallsBrowser");
    if (status.equals(Call.callstatus_ENUM._Assigned))
    {
      groupmember.qbeSetKeyValue(Groupmember.employee_key, context.getUser().getKey());  
    }
    if (status.equals(Call.callstatus_ENUM._Owned)||status.equals(Call.callstatus_ENUM._QA))
    {
      call.qbeSetKeyValue(Call.callowner_key,  context.getUser().getKey());
    }
    call.qbeSetValue(Call.callstatus, status);
    browser.search("r_opencalls");

    return browser.recordCount();
  }
}
