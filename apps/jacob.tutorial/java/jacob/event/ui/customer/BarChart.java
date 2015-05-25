package jacob.event.ui.customer;

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IBarChartEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * @author andherz
 *
 */
public class BarChart extends IBarChartEventHandler
{
  static public final transient String RCS_ID = "$Id: IBarChartEventHandler.java,v 1.1 2005/02/23 17:30:09 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  int numberOfDatasets = 1;
  int[] numToGenerate    = {4,31,40};
  
  public static int state=0;
  
  public void onClick(IClientContext context, IChart chart, int barIndex) throws Exception
  {
    super.onClick(context, chart, barIndex);
    state=state+1;
    if(state==2)
      state=0;
    chart.refresh();
  }

  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The title of the line chart
   */
  public String getTitle(IClientContext context, IChart chart)
  {
    return "myTitle";
  }
  
  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the x-axis
   */
  public String getXAxisTitle(IClientContext context, IChart chart)
  {
    return "X-Axis";
  }
  
  /**
   * 
   * @param context the current working context
   * @param chart the chart GUI element
   * @return The caption for the y-axis
   */
  public String getYAxisTitle(IClientContext context, IChart chart)
  {
    return "Y-Axis";
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
    String[] result = new String[numToGenerate[state]];
		for( int i=0; i < numToGenerate[state]; i++ )
		{
			result[i]=Integer.toString(i);
		}
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
		String[] labels=new String[ numberOfDatasets ];
		for( int j=0; j < numberOfDatasets; j++ )
		{
				labels[ j ]="Line "+j;
		}
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
		Color[] colors=new Color[ numberOfDatasets ];
		for( int j=0; j < numberOfDatasets; j++ )
		{              //               RED                       GREEN                          BLUE                ALPHA
		  colors[ j ]= new Color((int)getRandomNumber(1,255),(int)getRandomNumber(1,255),(int)getRandomNumber(1,255),255);
		}
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
		double[][] data=new double[ numberOfDatasets ][ numToGenerate[state] ];
		for( int j=0; j < numberOfDatasets; j++ )
		{
			for( int i=0; i < numToGenerate[state]; i++ )
			{
				data[ j ][ i ]=getRandomNumber( 100, 500 );
			}
		}
		return data;
  }
  
  /**
   * little help method for test data
   */
	private static double getRandomNumber( double minValue, double maxValue )
	{
		return ( minValue + ( Math.random() * ( maxValue - minValue ) ) );
	}
}
