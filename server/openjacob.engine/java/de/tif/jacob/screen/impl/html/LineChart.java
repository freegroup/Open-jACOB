/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl.html;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
import javax.imageio.ImageIO;
import de.tif.jacob.core.definition.guielements.LineChartDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILineChart;
import de.tif.jacob.screen.event.ILineChartEventHandler;
import de.tif.jacob.util.chart.GenericChart;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LineChart extends AbstractChart implements ILineChart
{
  static public final transient String RCS_ID = "$Id: LineChart.java,v 1.2 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  LineChartDefinition definition = null;
  
  protected LineChart(IApplication app, LineChartDefinition chart)
  {
  	super(app, chart.getName(), "undef",chart.isVisible(), chart.getRectangle(), chart.getProperties());

  	definition = chart;
  }

  
  public String getImageMapName(ClientContext context) throws Exception
  {
    return null;
  }
  
  /**
   * return the HTML representation of this object
   */
  public void calculateImageHTML(ClientContext context,Writer w) throws Exception
  {
	    try
	    {
	      ILineChartEventHandler hook = getLineChartHook(context);
	
	      Color[]    colors       = hook.getColors(context, this);
		    String[]   legendLabels = hook.getLegendLabels(context, this);
	      String[]   xAxisLabels  = hook.getXAxisLabels(context, this);
	      String     xAxisTitle   = hook.getXAxisTitle(context,this);
	      String     yAxisTitle   = hook.getYAxisTitle(context,this);
	      String     title        = hook.getTitle(context,this);
	      double[][] data         = hook.getDataElements(context,this);
	      
	      // Falls keine Chart gezeichnet werden soll, oder keine Messwerte vorhanden sind, dann
	      // werden vernüftige 'Null' werte eingetragen
	      if(data==null || data[0]==null || data[0].length==0)
	      {
	        data=new double[][]{{Double.NaN}};
	        legendLabels = new String[]{""};
	        xAxisLabels = new String[]{""};
	      }

        GenericChart chart = new de.tif.jacob.util.chart.LineChart(boundingRect.width, boundingRect.height);
        chart.setDrawBackground(definition.isDrawBackground());
        chart.setDrawLegendX(definition.isDrawLegendX());
        chart.setDrawLegendY(definition.isDrawLegendY());
        chart.setDrawTitle(definition.isDrawTitle());
        chart.setDrawGrid(definition.isDrawGrid());
        chart.setData(data[0]);
        chart.setTitle(title);
        chart.setYLabel(xAxisLabels);
        chart.setTitleX(xAxisTitle);
        chart.setTitleY(yAxisTitle);
        
        ByteArrayOutputStream bytes =new ByteArrayOutputStream();
        ImageIO.write(chart.createImage(), "png", bytes);
        imageData= bytes.toByteArray();
	
	    }
	    catch (Exception e)
	    {
	      // FREEGROUP: Warum hier überhaupt die Exception fangen?
	      ExceptionHandler.handle(context, e);
	    }
  }

  private ILineChartEventHandler getLineChartHook(IClientContext context) throws UserException
  {
	  Object obj = getEventHandler(context);
    if(obj instanceof ILineChartEventHandler)
      return (ILineChartEventHandler)obj;
    throw new UserException("Object ["+getPathName()+"] must implement a hook of type ["+ILineChartEventHandler.class.getName()+"]");
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
