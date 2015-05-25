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

import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDate;
import de.tif.jacob.screen.event.IDateEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Date extends SingleDataGUIElement implements IDate
{
  static public final transient String RCS_ID = "$Id: Date.java,v 1.11 2010/10/15 11:40:30 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.11 $";

  private final DateInputFieldDefinition definition;
  private ICaption caption;
  
  /**
   * Constructor with an CASTOR object from the adf file
   * 
   * @param parent
   * @param dateTime
   */
  protected Date(IApplication app, DateInputFieldDefinition date)
  {
    super(app, date.getName(), null, date.isVisible(),date.isReadOnly(), date.getRectangle(), date.getLocalTableAlias(), date.getLocalTableField(), date.getFont(), date.getProperties());

    if (null != date.getCaption())
      addChild(caption=new Caption(app, date.getCaption()));
    
    // Da um das Element eine HTML Tabelle ist, muss die Höhe korrigiert werden und an
    // der rechten Seite muss Platz für ein Button sein welcher 16 Pixel groß ist
    if(boundingRect!=null)
    {
      boundingRect.width += 16;

      // TODO: Hack, weil scheinbar Tabelle Pixels klaut
      boundingRect.height += 2;
    }
    definition = date;
  }
  
  
  public ICaption getCaption()
  {
    return caption;
  }

  public int getTabIndex()
  {
    return definition.getTabIndex();
  }
  
  /**
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      // Die Kinder eines Eingabefeldes (bis jetzt immer 'Caption') müssen nur dann
      // neu berechnet werden, wenn sich das Textelement selbst verändert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      
      String eventGUID = getEtrHashCode();
      Writer w = newCache();
      w.write("<table cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;position:absolute;");
      getCSSStyle(context,w, boundingRect);
      w.write("\" id=\"container_");
      w.write(eventGUID);
      w.write("\"><tr><td><input onFocus=\"onFocusElement(this,'");
      w.write(getName());
      w.write("')\" style=\"width:100%;height:100%\" type=\"text\"");
      if(getTabIndex()>=0)
      {
      	w.write(" TABINDEX=\"");
      	w.write(Integer.toString(definition.getTabIndex()));
      	w.write("\"");
      }
      w.write(" id=\"");
      w.write(eventGUID);
      w.write("\"");
      w.write(" name=\"");
      w.write(eventGUID);
      w.write("\" value=\"");
      w.write(StringUtil.htmlEncode(getValue()));
      w.write("\"");
      if(isEnabled() && isEditable())
      {
        w.write(" class=\"datetime_normal editable_inputfield\"></td><td width=\"16\">");
        
        w.write("<a href=\"javascript:showDateSelector('");
        w.write(eventGUID);
        w.write("','");
        w.write(I18N.getHttpCalendarDateFormat(context.getLocale()));
        w.write("');\"><img class=\"datetime_normal\" id=\"img_"+Integer.toString(getId())+"\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("date.png"));
        w.write("\" border=\"0\" alt=\"");
        w.write(I18N.getCoreLocalized("TOOLTIP_SELECT_DATE", context));
        w.write("\"></a>\n");
      }
      else
      {
        w.write(" readonly class=\"datetime_disabled\"></td><td width=\"16\">");
        w.write("<img class=\"datetime_disabled\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("date_disabled.png"));
        w.write("\" border=\"0\">");
      }
      w.write("</td></tr></table>");
    }
  }
  
  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);

    if(getDataField().isDiverse())
      context.addOnLoadJavascript("initBehaviourDiverse('"+getEtrHashCode()+"');");
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
    if(obj!=null)
    {
      if(obj instanceof IDateEventHandler)
        ((IDateEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IDateEventHandler.class.getName()+"]");
    }
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
