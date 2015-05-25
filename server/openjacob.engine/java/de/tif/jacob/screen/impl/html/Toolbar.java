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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.IToolbarDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IToolbar;
import de.tif.jacob.screen.IToolbarButton;

/**
 * @author Andreas Herz
 *
 */
public class Toolbar extends GuiHtmlElement implements IToolbar
{
  static public final transient String RCS_ID = "$Id: Toolbar.java,v 1.5 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  final ContextMenu contextMenu;
  private final IToolbarDefinition definition;
  private boolean configureable = true;
  
  protected Toolbar(IApplication app, IToolbarDefinition def)
  {
    super(app, def.getName(), "<no label>", true, null, Collections.EMPTY_MAP);
    
    this.definition=def;
    contextMenu = new ContextMenu(app);
    contextMenu.addChild(new ContextMenuEntryConfigureToolbar(app,this));
    
    addChild(contextMenu);
  }
  
  
  public void setConfigureable(boolean flag)
  {
    this.configureable = flag;
    resetCache();
  }


  public IToolbarButton getButton(ActionType type)
  {
    for (Iterator iter = getChildren().iterator(); iter.hasNext();)
    {
      GuiHtmlElement element = (GuiHtmlElement) iter.next();
      if(element instanceof ToolbarButton)
      {
        if(((ToolbarButton)element).isTypeOf(type))
          return (IToolbarButton)element;
      }
    }
    return null;
  }

  /**
   * Return all IToolbarButtons of the Toolbar.
   * 
   * @return List[IToolbarButton] 
   */
	public List getButtons()
  {
	  List result = new ArrayList();
    for (Iterator iter = getChildren().iterator(); iter.hasNext();)
    {
      GuiHtmlElement element = (GuiHtmlElement) iter.next();
      if(element instanceof IToolbarButton)
        result.add(element);
    }
    return result;
  }


  /* 
	 * @see de.tif.jacob.screen.GUIElement#addDataFields(java.Util.Vector)
	 */
	protected void addDataFields(Vector fields)
	{
	}
	
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(getCache()==null)
    {
      super.calculateHTML(context);
      Writer w = newCache();
      
      if(this.configureable==true)
      {
        w.write("\n<div style=\"width:100%;height:100%\" oncontextmenu=\"");
        w.write(contextMenu.getContextMenuFunction());
        w.write("\" >");
      }
      else
      {
        w.write("\n<div style=\"width:100%;height:100%\">");
      }
      super.writeHTML(context, w);
      w.write("</div>\n");
    }
    super.calculateHTML(context);
  }
  
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(this.configureable==true)
    {
      w.write("\n<div style=\"width:100%;height:100%\" oncontextmenu=\"");
      w.write(contextMenu.getContextMenuFunction());
      w.write("\" >");
    }
    else
    {
      w.write("\n<div style=\"width:100%;height:100%\">");
    }
    super.writeHTML(context, w);
    w.write("</div>\n");
  }
  
  public String getEventHandlerReference()
  {
    return null;
  }
}
