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
import java.net.URL;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.ICaptionEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Caption extends ActionEmitter implements ICaption
{
  static public final transient String RCS_ID = "$Id: Caption.java,v 1.16 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.16 $";

  
  private Alignment.Horizontal halign;
  private Alignment.Vertical valign;
  private String link;
  private String tooltip;
  
  private final de.tif.jacob.core.definition.guielements.Caption definition;
  
  protected Caption(IApplication app, de.tif.jacob.core.definition.guielements.Caption caption)
  {
  	super(app, "undef",caption.getLabel(),true, caption.getRectangle(),caption.getActionType(), caption.getProperties());
  	this.halign = caption.getHorizontalAlign();
  	this.valign = caption.getVerticalAlign();
    this.setFont(caption.getFont());
    this.definition = caption;
    this.foregroundColor = caption.getColor(); 
  }
  
  protected Caption(IApplication app, de.tif.jacob.core.definition.guielements.Caption caption, Rectangle rect)
  {
  	super(app, "undef",caption.getLabel(),true, rect,caption.getActionType(), caption.getProperties());
  	this.halign = caption.getHorizontalAlign();
  	this.valign = caption.getVerticalAlign();
    this.setFont(caption.getFont());
    this.definition = caption;
  }
  
  /**
   * Checks whether this caption belongs to a required GUI input field.
   * 
   * @return
   */
  private boolean isRequired()
  {
    if (parent instanceof SingleDataGUIElement)
    {
      SingleDataGUIElement sparent = (SingleDataGUIElement) parent;
      
      // the field must also be enabled and editable, otherwise the required field should
      // be filled by scripts.
      return sparent.isRequired() && sparent.isEnabled() && sparent.isEditable();
    }
    return false;
  }

  /**
   * return the HTML representation of this object
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
  
    if(getCache()==null)
    {
      String clickAction ="";
      boolean required = isRequired();
      
      Writer w = newCache();
    	String hoverClass ="";
      w.write("\t<a");
      
      // 1.) Falls die Action eine URL hat wird diese rausgeschrieben  
      //
      if(this.link!=null)
      {
        if(this.link.startsWith("javascript:"))
          clickAction =" onclick=\""+StringUtil.replace(this.link,"javascript:","")+"\" ";
        else
          clickAction =" href=\""+this.link+"\" ";
      }
        
      // 2.) "Klickbar" für den Reportdesigner, machen wenn es ein Datenfeld ist UND es eine Relation zur
      //     Datenbank hat. Berechnete Felder können nicht in den Reportdesigner aufgenommen werden.
      //
      if(context.isInReportMode() && parent instanceof SingleDataGUIElement && ((SingleDataGUIElement)parent).getDataField().getTableAlias()!=null)
      {
        clickAction = " onClick=\"this.style.color='red';FireEvent('"+Integer.toString(getId())+"', 'report')\" ";
        hoverClass=" reporting_clickable";
      }

      // 3.) Falls die Caption eine Aktion hat, dann hat diese priorität. Kann somit nicht mehr in den Report mit aufgenommen
      //     werden, da die onClick-Methode überschrieben worden ist.
      //
      // @deprecated Wird nur noch im Caretaker oder bei anderen Daimler Anwendungen verwendet
      ActionType action = getAction(context);
      if(action!=null)
      {
        clickAction = " onClick=\"FireEvent('" + Integer.toString(getId()) + "', 'navigate')\"";
        hoverClass="";
      }
      
      // 4.) Der Benutzer hat einen Hook auf die Caption gesetzt. Dies hat absolute Priorität
      //     und die anderen Events werden ignoriert!!
      ICaptionEventHandler handler= (ICaptionEventHandler)getEventHandler(context);
      if(handler instanceof IOnClickEventHandler && this.isEnabled())
      {
        clickAction = " onClick=\"FireEvent('" + Integer.toString(getId()) + "', 'click')\"";
        hoverClass="";
      }
      
      w.write(clickAction);

      
      // Festlegen des style für die Caption. Linked, Required, Normal.
      // in Abhängigkeit des Status der Gruppe
      //
      if(action!=null || clickAction.length()>0)
      {
        if (required)
          w.write(" class=\"caption_linked_required_");
        else
          w.write(" class=\"caption_linked_");
      }
      else if(required)
      {
        w.write(" class=\"caption_required_");
      }
      else
      {
        w.write(" class=\"caption_normal_");
      }
      w.write(getDataStatus().toString());
      w.write(hoverClass);
      // Eine Caption muss eine Id habe, da diese als Anchor für GridDialoge, Tooltips,...
      // verwendet werden kann. Die ID ist dann die JavaScript Referenz zu dem Element.
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\" ");
      if(this.tooltip!=null)
      {
         w.write(" title=\"");
         w.write(StringUtil.htmlEncode(this.tooltip));
         w.write("\" ");
      }
      w.write(" style=\"white-space:nowrap;");
      getCSSStyle(context, w,boundingRect);
      w.write(";text-align:");
      w.write(halign.toString());
      w.write("\">");
      w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
      if(required)
        w.write("<span class=\"caption_requiredflag\" >*</span>");
      w.write("</a>\n");
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
    if(definition.getEllipsis())
      context.addOnLoadJavascript("ellipsis('"+getEtrHashCode()+"');");
  }
  
	/**
	 * @return Returns the horizontal align.
	 */
	public Alignment.Horizontal getHorizontalAlign()
	{
		return this.halign;
	}

	/**
	 * @param align The align to set.
	 */
	public void setHorizontalAlign(Alignment.Horizontal align)
	{
	  resetCache();
		this.halign = align;
	}
	
	public GroupState getDataStatus()
	{
	  if(parent!=null)
	    return parent.getDataStatus();
	  return super.getDataStatus(); 
	}
	
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      if(event.equals("report") )
      {
        SingleDataGUIElement e =(SingleDataGUIElement)parent;
        String table =e.getDataField().getTableAlias().getName();
        String field =e.getDataField().getField().getName();
  
        Application app = (Application)context.getApplication();
        app.addReportColumn(table, field, getI18NLabel(context.getLocale()));
        return true;
      }
      else if(event.equals("click") )
      {
        IOnClickEventHandler handler= (IOnClickEventHandler)getEventHandler(context);
        if(handler !=null)
          handler.onClick(context,this);
        return true;
      }
    }
    
    return super.processEvent(context, guid,event,value);
  }

  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the textfield
    // or it can calculate the new value if the element a non DB bounded element.
    //
    Object obj = getEventHandler(context);
    if(obj instanceof ICaptionEventHandler)
      ((ICaptionEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
  }
  
  
  public void setLink(URL url)
  {
    this.link = url==null?null:url.toExternalForm();
    resetCache();
  }

  
  public void setLink(String url)
  {
    this.link = url;
    resetCache();
  }

  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
    resetCache();
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
