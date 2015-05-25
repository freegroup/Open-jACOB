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
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
import javax.imageio.ImageIO;
import de.tif.jacob.core.definition.guielements.BarChartDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBarChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBarChartEventHandler;
import de.tif.jacob.util.chart.GenericChart;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BarChart extends AbstractChart implements IBarChart
{
  static public final transient String RCS_ID = "$Id: BarChart.java,v 1.5 2011/02/15 08:13:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  BarChartDefinition definition = null;
  
  protected BarChart(IApplication app, BarChartDefinition chart)
  {
  	super(app, chart.getName(), "undef",chart.isVisible(), chart.getRectangle(), chart.getProperties());
  	definition = chart;
  }

  /**
   * return the HTML representation of this object
   */
  public void calculateImageHTML(ClientContext context,Writer w) throws Exception
  {
	    try
	    {
	      IBarChartEventHandler hook = getBarChartHook(context);
	      Color[]    colors       = hook.getColors(context, this);
	      String[]   xAxisLabels  = hook.getXAxisLabels(context, this);
	      String     xAxisTitle   = hook.getXAxisTitle(context,this);
	      String     yAxisTitle   = hook.getYAxisTitle(context,this);
	      String     title        = hook.getTitle(context,this);
	      double[][] data         = hook.getDataElements(context,this);
	      
	      GenericChart chart = new de.tif.jacob.util.chart.BarChart(boundingRect.width, boundingRect.height);
        chart.setDrawBackground(definition.isDrawBackground());
        chart.setDrawLegendX(definition.isDrawLegendX());
        chart.setDrawLegendY(definition.isDrawLegendY());
        chart.setDrawTitle(definition.isDrawTitle());
        chart.setDrawGrid(definition.isDrawGrid());
        chart.setData(data[0]);
        chart.setColor(colors[0]);
        chart.setTitle(title);
        chart.setYLabel(xAxisLabels);
        chart.setTitleX(xAxisTitle);
        chart.setTitleY(yAxisTitle);
        
	      ByteArrayOutputStream bytes =new ByteArrayOutputStream();
        ImageIO.write(chart.createImage(), "png", bytes);
	      imageData= bytes.toByteArray();

        Rectangle[] imageMap = chart.getFulcrumBounds();
        
	      w.write("<map name='chartMap_"+getEtrHashCode()+"'>\n");

        for (int i = 0; i < imageMap.length; i++)
        {
            Rectangle rectangle = imageMap[i];
            w.write("<area href=\"javascript:FireEventData('");
            w.write(Integer.toString(getId()));
            w.write("','click','");
            w.write(Integer.toString(i));
            w.write( "');\" coords=\"");
            w.write(Integer.toString(rectangle.x));
            w.write(",");
            w.write(Integer.toString(rectangle.y));
            w.write(",");
            w.write(Integer.toString(rectangle.x+rectangle.width));
            w.write(",");
            w.write(Integer.toString(rectangle.y+rectangle.height));
            w.write("\" title=\"");
            w.write(Double.toString(data[0][i]));
            w.write("\" shape=\"rect\"/>\n");
	      } 
	      w.write("</map>\n");
        
	    }
	    catch (Exception e)
	    {
	      ExceptionHandler.handle(context, e);
	    }
  }
  

  public String getImageMapName(ClientContext context) throws Exception
  {
    return "chartMap_"+getEtrHashCode();
  }
  
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId() && event.equals("click") )
    {
      int index= Integer.parseInt(value);
      getBarChartHook(context).onClick(context, this, index);
      return true;
    }
    
    return super.processEvent(context, guid,event,value);
  }
  
  private IBarChartEventHandler getBarChartHook(IClientContext context) throws UserException
  {
	  Object obj = getEventHandler(context);
	    if(obj instanceof IBarChartEventHandler)
	      return (IBarChartEventHandler)obj;
    throw new UserException("Object ["+getPathName()+"] must implement a hook of type ["+IBarChartEventHandler.class.getName()+"]");
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
