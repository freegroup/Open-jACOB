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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.definition.guielements.ListBoxInputFieldDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IListBoxEventHandler;
import de.tif.jacob.screen.impl.HTTPListBox;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.screen.impl.ListBoxAction;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ListBox extends Enum implements HTTPListBox
{
  static public final transient String RCS_ID = "$Id: ListBox.java,v 1.19 2010/09/29 13:53:52 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.19 $";

  // Definiert ob im Update-Modus mehr als ein Wert selektiert werden kann
  // 
  private final boolean multiselect;
  
  private final int  positionValueId;
  private long currentScrollPosX =0;
  private long currentScrollPosY =0;
  
  private final ListBoxAction selectAllAction = new ListBoxActionSelectAll(); 
  private final ListBoxAction deselectAllAction = new ListBoxActionDeselectAll();
    
  protected ListBox(IApplication app,  ListBoxInputFieldDefinition _list)
  {
    super(app, _list, false);
    this.multiselect = _list.isMultiselect();
    positionValueId = IDProvider.next();
  }


  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==positionValueId)
    {
      String[] values = data.split(":");
      currentScrollPosX = Long.parseLong(values[0]);
      currentScrollPosY = Long.parseLong(values[1]);
      return true;
    }
    // the super-implementation trys to find the corresponding child
    // and sends the event to them
    return super.processParameter( guid, data); 
  }

  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if (!isVisible())
      return;
    String etrId = getEtrHashCode();
    // write all childs to the screen. e.g. The caption of a combo box or any
    // other decoration
    // only neccessary if
    super.calculateHTML(context);
    if (getCache() == null)
    {
      Writer w = newCache();
      boolean hasIcons = false;
      Iterator iter = this.entries.iterator();
      while (iter.hasNext() && !hasIcons)
        hasIcons = hasIcons || ((Option) iter.next()).icon != null;
      // the user can turn off the combobox with setEnabled(false);
      //
      if (isEditable() && isEnabled())
      {
        // Hinweis:
        // multiselect => Der Benutzer kann mehr wie ein Wert bei dem
        // UPDATE-Mode auswählen
        // multichoice => Der Benutzer kann mehr wie ein Wert bei dem
        // SEARCH-Mode auswählen
        //
        boolean multi = (this.multiselect || getDataStatus() == SEARCH) && isMultichoice() == true;
        boolean isChecked = false;
        w.write("<input type=\"hidden\" name=\"" + etrId + "\"  id=\"" + etrId + "\" value=\"" + StringUtil.htmlEncode(StringUtil.toSaveString(getValue())) + "\" />");
        w.write("<div class=\"listbox_normal\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" id=\"container_");
        w.write(etrId);
        w.write("\" multiselect=\"" + multi + "\" >\n");
        List selection = Arrays.asList(getSelection());
        int i = 0;
        boolean hasHandler = (getEventHandler(context) instanceof IListBoxEventHandler);
        iter = this.entries.iterator();
        while (iter.hasNext())
        {
          Option option = (Option) iter.next();
          if (option.enabled)
          {
            if (multi)
            {
              isChecked = selection.contains(option.value);
            }
            else
            {
              isChecked = getValue() != null && getValue().equals(option.value);
            }
            String className = " class=\"listboxitem\" checked=\"false\" ";
            if (isChecked)
              className = " class=\"listboxitem_selected\" checked=\"true\" ";
            w.write("<div ");
            w.write(className);
            w.write(" onclick=\"Listbox_doItemCheck(getObj('");
            w.write(etrId);
            w.write("'),getObj('container_");
            w.write(etrId);
            w.write("'),this);");
            if (hasHandler && option.hasCallback)
              w.write("FireEvent('" + Integer.toString(getId()) + "','change');");
            w.write("\" id=\"container_");
            w.write(etrId);
            w.write("_");
            w.write(Integer.toString(i));
            w.write("\" value=\"");
            w.write(StringUtil.htmlEncode(option.value));
            w.write("\">");
            if (hasIcons)
              writeIcon(w, option);
            w.write(StringUtil.htmlEncode(option.getI18NLabel(context)));
            w.write("</div>");
          }
          else
          {
            w.write("<div class=\"listboxitem_disabled\" id=\"container_");
            w.write(etrId);
            w.write("_");
            w.write(Integer.toString(i));
            w.write("\" value=\"");
            w.write(StringUtil.htmlEncode(option.value));
            w.write("\">");
            if (hasIcons)
              writeIcon(w, option);
            w.write(StringUtil.htmlEncode(option.getI18NLabel(context)));
            w.write("</div>");
          }
          i++;
        }
        w.write("</div>\n");
        // Menu/Actions for the ListBox
        //
        if (multi)
        {
          w.write("<span id=\"fade_");
          w.write(Integer.toString(getId()));
          w.write("\" style=\"position:absolute;top:" + (boundingRect.y + 1) + "px;left:" + (boundingRect.x + boundingRect.width - (2 * 20) - 1) + "px;\">");
          w.write("<table cellspacing=\"0\" cellpadding=\"0\"><tr><td>");
          selectAllAction.calculateHTML(context, this, w);
          w.write("</td><td>");
          deselectAllAction.calculateHTML(context, this, w);
          w.write("</td></tr></table></span>");
        }
      }
      else
      {
        w.write("<div class=\"listbox_selected\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" id=\"container_");
        w.write(etrId);
        w.write("\">\n");
        List selection = Arrays.asList(getSelection());
        iter = this.entries.iterator();
        while (iter.hasNext())
        {
          Option option = (Option) iter.next();
        
          if (getValue() != null && selection.contains(option.value))
          {
            w.write("<div class=\"listboxitem_selected\" >");
            if (hasIcons)
              writeIcon(w, option);
            w.write(StringUtil.htmlEncode(option.getI18NLabel(context)));
            w.write("</div>");
          }
          else
          {
            w.write("<div class=\"listboxitem_disabled\" >");
            if (hasIcons)
              writeIcon(w, option);
            w.write(StringUtil.htmlEncode(option.getI18NLabel(context)));
            w.write("</div>");
          }
        }
        w.write("</div>\n");
      }
    }
  }
  
  private void writeIcon(Writer w, Option option) throws Exception
  {
    w.write("<img style=\"vertical-align:bottom;\"");
    if(option.tooltip!=null)
    {
      w.write(" title=\"");
      w.write(option.tooltip);
      w.write("\"");
    }
    w.write("src=\"");
    if(option.icon!=null)
      w.write(option.icon.getPath(true));
    else
      w.write(Icon.blank.getPath(true));
    w.write("\"/>&nbsp;");
  }


  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
    w.write("<input type=\"hidden\" name=\""+ID_PREFIX+ positionValueId+"\"  id=\""+ID_PREFIX+positionValueId+"\" value=\""+currentScrollPosX+":"+currentScrollPosY+"\">\n");
    context.addOnLoadJavascript("initBehaviourListbox('"+getId()+"','"+ID_PREFIX+positionValueId+"');");
  }
  
	/**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid != this.getId())
      return super.processEvent(context,guid,event,value);
    return Command.processEvent(this,context,guid,event,value);
  }
  
  /**
   * Calls the eventhandler for the Button event handler
   * 
   */
  public final void onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState status) throws Exception
  {
    super.onGroupDataStatusChanged(context,status);
    Command.onGroupDataStatusChanged(this,context,status);
  }
  
}
