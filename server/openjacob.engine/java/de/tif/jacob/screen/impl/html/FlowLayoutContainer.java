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
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import de.tif.jacob.core.definition.guielements.FlowLayoutContainerDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IFlowLayoutContainer;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.GuiElement;

/**
 * @author Andreas Herz
 *
 */
public class FlowLayoutContainer extends GuiHtmlElement implements IFlowLayoutContainer
{
  static public final transient String RCS_ID = "$Id: FlowLayoutContainer.java,v 1.6 2010/10/20 17:04:21 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  private final FlowLayoutContainerDefinition definition;
  private boolean duringCalculate = false;
  
  protected FlowLayoutContainer(IApplication app, FlowLayoutContainerDefinition def)
  {
    super(app, def.getName(), "<no label>", true, def.getRectangle(), def.getProperties());
    
    this.definition=def;
    this.calculateChildrenPosition();
  }

  
  public void addChild(IGuiElement child)
  {
    super.addChild(child);
    ((GuiElement)child).addPropertyChangeListener(this);
  }


  /* 
	 * @see de.tif.jacob.screen.GUIElement#addDataFields(java.Util.Vector)
	 */
	protected void addDataFields(Vector fields)
	{
	}
	

  public String getEventHandlerReference()
  {
    return null;
  }


  public void calculateHTML(ClientContext context) throws Exception
  {
    if(getCache()==null)
    {
      // the cache is the indicator for the child recalculation
      Writer w = newCache();
      if(duringCalculate)
        return;
      duringCalculate = true;
      try
      {
        calculateChildrenPosition();
      }
      finally
      {
        duringCalculate = false;
      }
    }
    
    for (int i=0; i<getChildren().size();i++)
    {
      GuiHtmlElement child = (GuiHtmlElement)getChildren().get(i);
      if(child.isEnabled()==false && this.hiddeDisabledElements())
        continue;
      child.calculateHTML(context);
    }    
    //super.calculateHTML(context);
  }


  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(definition.isHorizontal())
    {
      w.write("<div style=\"");
      getCSSStyle(context, w, boundingRect);
      w.write("\">");
      super.writeHTML(context, w);
      w.write("</div>");
    }
    else
    {
      super.writeHTML(context,w);
    }
  }


  public void propertyChange(PropertyChangeEvent arg0)
  {
    resetCache();
  }


  private void calculateChildrenPosition()
  {
    if(definition.isHorizontal())
    {
      Rectangle rect = new Rectangle();
      rect.width = -1;
      rect.height = this.boundingRect.height;
      rect.x = -1;
      rect.y = -1;
      // recalculate the position of all buttons
      //
      Iterator iter = getChildren().iterator();
      while(iter.hasNext())
      {
        GuiHtmlElement element = (GuiHtmlElement)iter.next();
        if(element.isVisible())
          element.setBounding(rect);
      }
    }
    else
    {
      Rectangle rect = new Rectangle();
      rect.width = this.boundingRect.width;
      rect.height = 20;
      rect.x = this.boundingRect.x;
      rect.y = this.boundingRect.y;
      // recalculate the position of all buttons
      //
      Iterator iter = getChildren().iterator();
      while(iter.hasNext())
      {
        GuiHtmlElement element = (GuiHtmlElement)iter.next();
        if(element.isVisible() && !(element.isEnabled()==false && this.hiddeDisabledElements()))
        {
          element.setBounding(rect );
          rect = new Rectangle(rect.x,rect.y+25,rect.width,rect.height);
        }
      }
    }
  }
  
  private boolean hiddeDisabledElements()
  {
    return this.getProperty("hideDisabledElements")!=null;
  }
}
