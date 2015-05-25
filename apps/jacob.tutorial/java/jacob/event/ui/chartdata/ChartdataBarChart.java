package jacob.event.ui.chartdata;

import java.awt.Color;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IBarChartEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * @author andherz
 *
 */
public class ChartdataBarChart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id: ChartdataBarChart.java,v 1.1 2005/03/17 12:07:53 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  private final static String[] labels;
  static 
	{
    labels = new String[7];
    labels[0]="Monday";
    labels[1]="Tuesday";
    labels[2]="Wednesday";
    labels[3]="Thursday";
    labels[4]="Friday";
    labels[5]="Saturday";
    labels[6]="Sunday";
  }
  
  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The title of the line chart
   */
  public String getTitle(IClientContext context, IChart chart)
  {
    return "Simple Chart";
  }
  
  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the x-axis
   */
  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return "Day of Week";
  }
  
  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the y-axis
   */
  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return "Sold";
  }
  
  /**
   * Return the labels for the x-axis. The count of the labels <b>must</b> be match
   * with the count of the returnd data elements.
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return the labels for the x-axis
   */
  public String[] getXAxisLabels(IClientContext context, IChart chart)
  {
		return labels;
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
		String[] labels=new String[ 1 ];
		labels[ 0 ]="Vehicle TypA";
		
		return labels;
  }
  
  /**
   * Return the different colors of the chart lines.
   * 
   * @param context
   * @param chart
   * @return Return the different colors of the chart lines.
   */
  public  Color[] getColors(IClientContext context, IChart chart)
  {
		Color[] colors=new Color[ 1 ];
	  colors[ 0 ]= Color.green;
	  
		return colors;
  }
 
  /**
   * Return a array of data values.<br>
		*	
   * @param context the current working context
   * @param chart the chart GUI element
   * @return
   */
  public double[][] getDataElements(IClientContext context, IChart chart)
  {
		double[][] data=new double[ 1 ][ 7 ];
   	try 
		{
    	IDataTableRecord record =context.getSelectedRecord();

    	data[ 0 ][ 0 ]=record.getdoubleValue("monday");
   		data[ 0 ][ 1 ]=record.getdoubleValue("tuesday");
   		data[ 0 ][ 2 ]=record.getdoubleValue("wednesday");
   		data[ 0 ][ 3 ]=record.getdoubleValue("thursday");
   		data[ 0 ][ 4 ]=record.getdoubleValue("friday");
   		data[ 0 ][ 5 ]=record.getdoubleValue("saturday");
   		data[ 0 ][ 6 ]=record.getdoubleValue("sunday");
		} 
   	catch (Exception e) 
		{
   		ExceptionHandler.handle(context,e);
		}
   	
		return data;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBarChartEventHandler#onClick(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IChart, int)
	 */
	public void onClick(IClientContext context, IChart chart, int barIndex) 
	{
		IMessageDialog dialog = context.createMessageDialog("You clicked on ["+labels[barIndex]+"]");
		dialog.show();
	}
}
