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
import java.util.Vector;

import de.tif.jacob.core.definition.guielements.StaticImageDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StaticImage extends GuiHtmlElement implements IStaticImage
{
  static public final transient String RCS_ID = "$Id: StaticImage.java,v 1.9 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  final StaticImageDefinition definition;
  final String url ;
  private String i18nTooltip=null;
  private String tooltip;
  protected StaticImage(IApplication app, StaticImageDefinition def)
  {
  	super(app, def.getName(), "",def.isVisible(),def.getRectangle(), def.getProperties());
    this.definition =def;
    this.tooltip = definition.getTooltip();
    
    this.url = "./application/"+app.getName()+"/"+app.getApplicationDefinition().getVersion().toShortString()+"/"+definition.getSource();
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
    	String tip=getI18NTooltip(context);
      // Nur wenn das Image clickbar und enabled ist wird dies als Link dargestellt
      //
      if(getEventHandler(context) instanceof IOnClickEventHandler && this.isEnabled())
      {
        w.write("<a href=\"#\" onClick=\"FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("', 'click')\"><img style=\"");
        getCSSStyle(context, w, boundingRect);
        if(tip!=null)
        {
          w.write("\" title=\"");
          w.write(tip);
        }
        w.write("\" name=\"");
        w.write(getEtrHashCode());
        w.write("\" id=\"");
        w.write(getEtrHashCode());
        w.write("\" border=0 src=\"");
        w.write(url);
        w.write("\"></a>\n");
      }
      else
      {
        w.write("<img style=\"");
        getCSSStyle(context, w, boundingRect);
        if(!this.isEnabled())
        {
          w.write("filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity: 0.3;");
          // disabled image hat kein Tooltip
        }
        else if(tip!=null)
        {
          w.write("\" title=\"");
          w.write(tip);
        }
        w.write("\" name=\"");
        w.write(getEtrHashCode());
        w.write("\" id=\"");
        w.write(getEtrHashCode());
        w.write("\" border=0");
        w.write(" src=\"");
        w.write(url);
        w.write("\"");
        w.write(" >\n");
      }
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
// FREEGROUP: Performanceumstellung - StaticImage hat keine Kindelemente. Aufruf kann entfallen    
//    super.writeHTML(w);
    writeCache(w);
  }

  protected final void addDataFields(Vector fields)
  {
  }

  public final String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }


  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid!=this.getId())
      return false;
    
    // Event an den Eventhandler weiter leiten
    //
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if(obj instanceof IOnClickEventHandler)
       ((IOnClickEventHandler)obj).onClick(context, this);
      
    return true;
  }

  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if(obj instanceof IStaticImageEventHandler)
      ((IStaticImageEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
  }


  public void setTooltip(String text)
  {
    this.tooltip=text;
    this.i18nTooltip=null;
    resetCache();
}


  private String getI18NTooltip(IClientContext context)
  {
    if (i18nTooltip == null)
    {
      if (this.tooltip == null)
        return null;
      
      i18nTooltip = I18N.localizeLabel(this.tooltip, this.getApplicationDefinition(), context.getLocale());
    }
    return i18nTooltip;
  }
}
