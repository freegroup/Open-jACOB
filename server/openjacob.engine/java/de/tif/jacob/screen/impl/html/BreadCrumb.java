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
import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.BreadCrumbDefinition;
import de.tif.jacob.core.definition.guielements.HeadingDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBreadCrumb;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IHeading;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.IBreadCrumbEventHandler;
import de.tif.jacob.screen.event.ICaptionEventHandler;
import de.tif.jacob.screen.event.IHotkeyEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz
 * @since 2.8.0
 */
public class BreadCrumb extends GuiHtmlElement implements IBreadCrumb
{
  static public final transient String RCS_ID = "$Id: BreadCrumb.java,v 1.9 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  private Alignment.Horizontal halign;
  private final boolean decoration;
  private final BreadCrumbDefinition definition;
  
  protected BreadCrumb(IApplication app, BreadCrumbDefinition definition)
  {
    super(app, definition.getName(), definition.getCaption().getLabel(), definition.isVisible(), definition.getRectangle(), definition.getProperties());
    this.definition = definition;
    this.halign = definition.getHorizontalAlign();
    this.setFont(definition.getCaption().getFont());
    this.setColor(definition.getCaption().getColor());
    this.decoration = definition.getProperty("avoidDecoration")==null;
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      if(event.equals("click") )
      {
        int index= Integer.parseInt(value);
        String[] segments = StringUtils.splitByWholeSeparator(getBreadCrumb(), definition.getDelimiter());
        boolean startsWith = getBreadCrumb().startsWith(definition.getDelimiter());
        boolean endsWith = getBreadCrumb().endsWith(definition.getDelimiter());
        IBreadCrumbEventHandler handler= (IBreadCrumbEventHandler)getEventHandler(context);
        String path = StringUtils.join(segments,definition.getDelimiter(),0,index);
        if(startsWith)
          path = definition.getDelimiter()+path;
        if(endsWith && path.length()>definition.getDelimiter().length())
          path = path+definition.getDelimiter();
        handler.onClick(context,path,segments[index], this);
      }
      return true;
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
      Writer w = newCache();

      
      if(decoration)
      {
        w.write("<input style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" type=\"text\"");
        w.write(" value=\"\"");
        if(isEnabled())
          w.write(" class=\"text_normal editable_inputfield\" />\n");
        else
          w.write(" readonly class=\"text_disabled\" />\n");
      }
      
      w.write("<div class=\"breadcrumb\" style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\">");
      // split the breadcrum in the different segments
      String[] segments = StringUtils.splitByWholeSeparator(getBreadCrumb(),definition.getDelimiter());
      String htmlDelimiter = "<span class=\"breadcrumb_delimiter\">"+StringUtil.htmlEncode(definition.getDelimiter())+"</span>";
      boolean hasEventhandler = this.getEventHandler(context)!=null;
      boolean startsWith = getBreadCrumb().startsWith(definition.getDelimiter());
      boolean endsWith = getBreadCrumb().endsWith(definition.getDelimiter());
      for (int i = 0; i < segments.length; i++)
      {
        String segment = segments[i];
        // kommt vor wenn der BreadCrumb mit einem Delimiter endet, Das
        // Letzte Segment ist dann ein String mit der Länge 0.
        if(segment.length()>0)
        {
          if(i>0 || (i==0 && startsWith))
            w.write(htmlDelimiter);          
          if(hasEventhandler && isEnabled())
          {
            w.write("<span class=\"breadcrumb_segment\"");
            w.write(" onclick=\"FireEventData('");
            w.write(Integer.toString(getId()));
            w.write("', 'click','");
            w.write(Integer.toString(i));
            w.write("');\">");
          }
          else
          {
            w.write("<span class=\"breadcrumb_segment_disabled\" >");
          }
          w.write(StringUtil.htmlEncode(segment));
          w.write("</span>");
        }
      }
      if(endsWith)
        w.write(htmlDelimiter);          
      w.write("</div>");
    }
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
    if(obj instanceof IBreadCrumbEventHandler)
      ((IBreadCrumbEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
  }
    
  public String getBreadCrumb()
  {
    return super.getLabel();
  }

  public void setBreadCrumb(String breadcrumb)
  {
    super.setLabel(breadcrumb);
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
  }
  /**
   * a catiopn has no data fields......
   */
  protected void addDataFields(Vector fields)
  {
  }
  
	/**
	 * @return Returns the align.
	 */
	public String getAlign()
	{
		return halign.toString();
	}

	/**
	 * @param align The align to set.
	 */
	public void setAlign(String align)
	{
	  resetCache();
	//	this.halign = Alignment.Horizontal.. halign;
	}

	public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
