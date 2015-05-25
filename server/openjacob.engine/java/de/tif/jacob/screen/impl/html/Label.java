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
import java.net.URL;
import java.util.Locale;
import java.util.Vector;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Label extends GuiHtmlElement implements ILabel
{
  static public final transient String RCS_ID = "$Id: Label.java,v 1.14 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.14 $";

  private Alignment.Horizontal halign;
  private Alignment.Vertical valign;

  private final LabelDefinition definition;
  private String dbLabelValue = null;
  private String link;

  protected Label(IApplication app, LabelDefinition definition)
  {
    super(app, definition.getName(), definition.getCaption().getLabel(), definition.isVisible(), definition.getRectangle(), definition.getProperties());
    this.definition = definition;
    this.halign = definition.getHorizontalAlign();
    this.valign = definition.getVerticalAlign();
    this.setFont(definition.getCaption().getFont());
    this.foregroundColor = definition.getCaption().getColor();
  }

  /**
   * The framework call this method is an event comes from the client
   * guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid == this.getId())
    {
      if (event.equals("click"))
      {
        Object handler = getEventHandler(context);
        if (handler instanceof IOnClickEventHandler)
          ((IOnClickEventHandler) handler).onClick(context, this);
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
    if (!isVisible())
      return;

    if (getCache() == null)
    {
      Writer w = newCache();
      String clickAction = "";

      // Der Benutzer hat einen Hook auf das Label gesetzt.
      //
      ILabelEventHandler handler = (ILabelEventHandler) getEventHandler(context);
      // onClickEventhandler hat priorität vor dem URL link
      //
      if (this.isEnabled())
      {
        if (handler instanceof IOnClickEventHandler)
          clickAction = " onClick=\"FireEvent('" + Integer.toString(getId()) + "', 'click')\" ";
        else if (this.link != null)
          clickAction = " href=\"" + this.link + "\" ";
      }
      if (clickAction.length() > 0)
      {
        w.write("\t<a ");
        w.write(clickAction);
        w.write(" class=\"caption_linked_"+getDataStatus().toString()+"\" style=\"text-align:");
      }
      else
      {
        w.write("\t<a class=\"caption_normal_"+getDataStatus().toString()+"\" style=\"text-align:");
      }

      w.write(halign.toString());
      w.write(";white-space:nowrap;");
      getCSSStyle(context, w, boundingRect);
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\">");
      w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
      if (parent instanceof SingleDataGUIElement && ((SingleDataGUIElement) parent).isRequired())
        w.write("*");
      w.write("</a>\n");
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    writeCache(w);
    if(definition.getCaption().getEllipsis())
      context.addOnLoadJavascript("ellipsis('"+getEtrHashCode()+"');");
  }

  /**
   * a catiopn has no searchable data fields......
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
   * @param align
   *          The align to set.
   */
  public void setAlign(String align)
  {
    resetCache();
    // TODO: API ändern
    // this.halign = Alignment.Horizontal.. halign;
  }

  public String getEventHandlerReference()
  {
    return definition.getCaption().getEventHandler();
  }

  public void resetCache()
  {
    this.dbLabelValue = null;
    super.resetCache();
  }

  public String getLabel()
  {
    if (definition.getLocalTableField() != null)
      return super.getI18NLabel(null);
    return super.getLabel();
  }

  /**
   * Falls das Teil an ein Datenbankfeld gebunden ist, dann wird der Wert aus
   * dem aktuellen Record geholt. Ansonsten wird der normale Weg gegangen
   */
  public String getI18NLabel(Locale locale)
  {
    if (definition.getLocalTableField() != null)
    {
      try
      {
        if (dbLabelValue != null)
          return dbLabelValue;
        IClientContext context = (IClientContext) Context.getCurrent();
        IDataTableRecord record = context.getDataTable(definition.getLocalTableAlias().getName()).getSelectedRecord();
        if (record != null)
          dbLabelValue = record.getStringValue(definition.getLocalTableField().getName(), context.getLocale());
        
        if(dbLabelValue ==null)
          dbLabelValue = definition.getNullDefaultValue();
        
        return dbLabelValue;
      }
      catch (Exception e)
      {
        // ist nur ein Label. IGNORE
      }
    }
    return super.getI18NLabel(locale);
  }

  /*
   * @see
   * de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de
   * .tif.jacob.screen.IClientContext,
   * de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context, GroupState groupStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the
    // textfield
    // or it can calculate the new value if the element a non DB bounded
    // element.
    //
    Object obj = getEventHandler(context);
    if (obj instanceof ILabelEventHandler)
      ((ILabelEventHandler) obj).onGroupStatusChanged(context, groupStatus, this);
  }

  public void setLink(URL url)
  {
    this.link = url == null ? null : url.toExternalForm();
    resetCache();
  }

  public void setLink(String url)
  {
    this.link = url;
    resetCache();
  }
}
