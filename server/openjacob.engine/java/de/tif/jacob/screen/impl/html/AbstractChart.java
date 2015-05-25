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

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.ImageProvider;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractChart extends GuiHtmlElement implements IChart
{
  static public final transient String RCS_ID = "$Id: AbstractChart.java,v 1.6 2010/12/03 11:51:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  byte[] imageData;
  
  public abstract void   calculateImageHTML(ClientContext context,Writer w) throws Exception;
  public abstract String getImageMapName(ClientContext context)             throws Exception;

  protected AbstractChart(IApplication app, String name, String label, boolean isVisible, Rectangle boundingRect, Map properties)
  {
  	super(app, name, label,isVisible,boundingRect, properties);
  }

  /*
   * 
   */
  public final void refresh()
  {
    resetCache();
  }
  
  public final void resetCache()
  {
    super.resetCache();
    imageData = null;
  }
  
  /**
   * return the HTML representation of this object
   */
  public final void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
  
    if(getCache()==null)
    {
      Writer w = newCache();
    	
      w.write("<img style=\"");
      getCSSStyle(context, w, boundingRect);
      w.write("\" name=\"");
      w.write(getEtrHashCode());
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      if(getImageMapName(context)!=null)
      {
        w.write("\" border=0 usemap=\"#");
        w.write(getImageMapName(context));
      }
      w.write("\"");
      if(getDataStatus() == UPDATE || getDataStatus()==NEW)
	      w.write(" class='chart_update' ");
      else if(getDataStatus() == SELECTED)
        w.write(" class='image_selected' ");
      else
        w.write(" class='image_empty' ");
      
      w.write(" src=\"image?browser=");
      w.write(context.clientBrowser);//+"&image="+button.getName()+"');
      w.write("&dbImage=");
      w.write(getName());
      w.write("&dummy=");
      w.write(Long.toString(System.currentTimeMillis()));
      w.write("\">\n");
      calculateImageHTML(context,w);
    }
  }

  public final byte[] getImageData(IClientContext context, String imageId)
  {
    if(imageData!=null)
      return imageData;
    
    return ImageProvider.getBlankImage();
  }

  protected final void addDataFields(Vector fields)
  {
  }

  /** 
   */
  public final void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
  }

  public final GroupState getDataStatus()
	{
	  if(parent!=null)
	    return parent.getDataStatus();
	  return super.getDataStatus(); 
	}
	
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId() && event.equals("click") )
      return true;
    
    return super.processEvent(context, guid,event,value);
  }
}
