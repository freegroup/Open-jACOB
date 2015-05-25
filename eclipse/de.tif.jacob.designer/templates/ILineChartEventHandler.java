/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.event.ILineChartEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author {author}
 */
public class {class} extends ILineChartEventHandler
{
	static public final transient String RCS_ID = "$Id: ILineChartEventHandler.java,v 1.1 2007/05/18 16:13:26 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	private int numberOfDatasets = 1;
	private int numToGenerate    = 10;
  
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
	 * Returns the labels for the x-axis. The number of the labels <b>must</b> match
	 * with the number of the returned data elements.
	 * 
	 * @param context the current working context
	 * @param chart the chart GUI element
	 * @return the labels for the x-axis
	 */
	public String[] getXAxisLabels(IClientContext context, IChart chart) 
	{
		String[] result = new String[numToGenerate];
		for( int i=0; i < numToGenerate; i++ )
		{
			result[i]=Integer.toString(i);
		}
		return result;
	}
  
	/**
	 * Returns the legend label for each data set/line in the chart.
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
			labels[j] = "Line "+j;
		}
		return labels;
	}
  
	/**
	 * Returns the different colors of the chart lines.
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
			colors[j] = new Color((int)getRandomNumber(1,255),(int)getRandomNumber(1,255),(int)getRandomNumber(1,255),255);
		}
		return colors;
	}
 
	/**
	 * Returns an array of data values.<br>
	 *	
	 * @param context the current working context
	 * @param chart the chart GUI element
	 * @return
	 */
	public double[][] getDataElements(IClientContext context, IChart chart) 
	{
		double[][] data=new double[ numberOfDatasets ][ numToGenerate ];
		for( int j=0; j < numberOfDatasets; j++ )
		{
			for( int i=0; i < numToGenerate; i++ )
			{
				data[j][i] = getRandomNumber(100, 500);
			}
		}
		return data;
	}
  
	/**
	 * Little helper method for test data.
	 */
	private static double getRandomNumber(double minValue, double maxValue)
	{
		return minValue + Math.random() * (maxValue - minValue);
	}
}
