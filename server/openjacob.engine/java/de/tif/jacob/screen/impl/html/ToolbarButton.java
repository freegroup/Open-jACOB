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
import java.util.Collections;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IToolbarButton;
import de.tif.jacob.screen.ImageDecoration;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ToolbarButton extends ActionEmitter implements IToolbarButton
{
  static public final transient String RCS_ID = "$Id: ToolbarButton.java,v 1.5 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  boolean hasBeenConfigured=false;
  boolean emphasize = false;
  private ImageDecoration decoration=ImageDecoration.NONE;
  
  protected ToolbarButton(IApplication app, IToolbarButtonDefinition def)
  {
    super(app, def.getName(),def.getLabel(),def.isVisible(), null,def.getActionType(), Collections.EMPTY_MAP);
  }


  /**
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      Writer w = newCache();

      if(emphasize)
      {
        w.write("<button "); 
        if(isEnabled() && !context.isInReportMode())
        {
          w.write("onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','click')\"");
          w.write(" class=\"toolbarButton_emphasize\"> ");
        }
        else
        {
          w.write("class=\"toolbarButton_emphasize_disabled\">");
        }
        w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
        if(decoration!=ImageDecoration.NONE)
        {
          w.write("<img src=\"decorations/");
          w.write(decoration.getName());
          w.write(".png\" align=\"top\"/>");
        }
        w.write("</button>");
      }
      else
      {
        w.write("<button "); 
        if(isEnabled() && !context.isInReportMode())
        {
          w.write("onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','click')\"");
          w.write(" class=\"toolbarButton_normal\"> ");
        }
        else
        {
          w.write("class=\"toolbarButton_disabled\">");
        }
        w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
        if(decoration!=ImageDecoration.NONE)
        {
          w.write("<img src=\"decorations/");
          w.write(decoration.getName());
          w.write(".png\" align=\"top\"/>");
        }
        w.write("</button>");
      }
    }
  }
  
  public void setLabel(String l)
  {
    super.setLabel(l);
    
    // The parent of a button is a Toolbar. The Toolbar caches all the HTML from the children.
    // Enforce the redraw of the Toolbar.
    if(parent!=null)
      parent.resetCache();
  }
  
  public void setEmphasize(boolean emphasize)
  {
    this.emphasize = emphasize;
    
    resetCache();
    
    // The parent of a button is a Toolbar. The Toolbar caches all the HTML from the children.
    // Enforce the redraw of the Toolbar.
    if(parent!=null)
      parent.resetCache();
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
  }
  
  
  public boolean isVisible()
  {
    if(hasBeenConfigured==false)
    {
      SessionContext context = (SessionContext)Context.getCurrent();
	    String value=((HTTPClientSession)context.getSession()).getRuntimeProperty(getName());
	    if(value!=null)
	      super.setVisible(Boolean.valueOf(value).booleanValue());
	    hasBeenConfigured=true;
    }
    return super.isVisible();
  }
  
  /**
   * 
   * @param flag
   */
  public void setVisible(boolean flag)
  {
    super.setVisible(flag);
    // write them persistent to the database
    //
    SessionContext context = (SessionContext)Context.getCurrent();
    ((HTTPClientSession)context.getSession()).setRuntimeProperty(getName(),Boolean.toString(flag));
  }
  
  
  public void setDecoration(ImageDecoration decoration)
  {
    this.decoration = decoration;
    resetCache();
    
    // The parent of a button is a Toolbar. The Toolbar caches all the HTML from the children.
    // Enforce the redraw of the Toolbar.
    if(parent!=null)
      parent.resetCache();
    
  }

  public String getEventHandlerReference()
  {
    return null;
  }
}
