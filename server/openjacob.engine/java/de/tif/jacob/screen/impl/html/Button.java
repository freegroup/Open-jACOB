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

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.actiontypes.ActionTypeChangeUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearchUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Button  extends ActionEmitter implements IButton
{
	static public final transient String RCS_ID = "$Id: Button.java,v 1.16 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.16 $";

  private final ButtonDefinition definition;
 
  // The HTML fragment for the shortcut key definition
  private String accesskey="";
  private Alignment.Horizontal halign;
    
  private Icon icon = Icon.NONE;
  private boolean emphasize = false;
  private String link = null;
  
  protected Button(IApplication app, ButtonDefinition button)
  {
    super(app, button.getName(),button.getLabel(),button.isVisible(), button.getRectangle(), button.getActionType(), button.getProperties());

    this.halign = button.getHorizontalAlign();
    // add the short cut accessor for this button
    int index=getLabel().indexOf("&");
    if(index!=-1)
    {
      String key= getLabel().substring(index+1,index+2);
      accesskey=" accesskey=\""+key+"\" ";
      setLabel(StringUtil.replace(getLabel(),"&"+key,key));
    }
    definition   = button;
    emphasize = definition.isEmphasize();
  }
 
  public void setIcon(Icon icon)
  {
    this.icon=icon;
    resetCache();
  }

  
  public void setEmphasize(boolean emphasize)
  {
    this.emphasize = emphasize;
    resetCache();
  }
  

  /**
   * Buttons können Ihre Position dadurch verändern, dass diese in einer ButtonBar
   * enthalten sind. aus diesem Grund müssen diese zusätzlich Ihr CSS-Style resetten.
   * Dies ist bei "normalen" Elementen noch nicht notwendig.
   */
  public void resetCache()
  {
    super.resetCache();
    cssStyleCache=null;
  }
  
  public void setLink(String url)
  {
    this.link = url;
    this.resetCache();
  }

  /**
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
  
// FREEGROUP: Performanceumstellung - Ein Button kann im Moment keine Kinder habe. Aufruf kann somit entfallen.
//    super.calculateHTML(context);
    
    if(getCache()==null)
    {
      Writer w = newCache();
      
      w.write("\t<button style=\"");
      getCSSStyle(context, w, boundingRect);
      // Falls der Button 'nur' ein Icon hat aber keinen Text, dann
      // wird versucht das Icon zentrisch im Button darzustellen
      // Da stören eventuell Ränder welche im Style definiert wurden (margin/padding)
      // Diese werden jetzt zurückgesetzt
      //
      if(icon != Icon.NONE && getLabel().length()==0)
      {
        w.write(";margin:0px;padding:0px");
      }
      if(halign!=null)
      {
        w.write(";text-align:");
        w.write(halign.toString());
        w.write("");
      }
      w.write("\"");
      if(definition.getTabIndex()>=0)
      {
      	w.write(" TABINDEX=\"");
      	w.write(Integer.toString(definition.getTabIndex()));
        w.write("\" ");
      }
      w.write(" name=\"");
      w.write(getEtrHashCode());
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\" ");
      // the access key HTMLfragment must have a space as first character
      w.write(accesskey);
      String classAttribute= "";
      if(isEnabled() && !context.isInReportMode() &&!((Browser)getOuterGroup().getBrowser()).isInMultipleUpdate(context))
      {
        if(link==null)
        {
          w.write(" onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("', 'click')\" ");
        }
        else
        {
          w.write(" onClick=\"location.href='"+link+"';return false;\" ");
        }
        
        if(emphasize)
          classAttribute = " class=\"button_emphasize_normal";
        else
          classAttribute = " class=\"button_normal";
        
        if(isDefaultKey(context))
        {
          w.write(" isDefault=\"true\" ");
          classAttribute=classAttribute + " button_default\" ";
        }
        else
        {
          classAttribute = classAttribute+"\" ";
        }
      }
      else
      {
        w.write(" onClick=\"return false;\" ");
        if(emphasize)
          classAttribute = " class=\"button_emphasize_disabled\" ";
        else
          classAttribute = " class=\"button_disabled\" ";
      }
      
      w.write(classAttribute);
      w.write(">");
       if(icon!=Icon.NONE)
      {
        w.write("<img class=\"buttonicon\" src=\"");
        w.write(icon.getPath(isEnabled()));
        w.write("\"/>");
      }
      w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
      w.write("</button>\n");
    }
  }
  
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
  
    writeCache(w);
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
  private boolean isDefaultKey(ClientContext context)
  {
    // Default flag has been already in JAD?
    if (this.definition.hasDefault())
      return this.definition.isDefault();

    // No -> Determine automatically by means of button type and application
    // state
    //

    ActionType action = getAction(context);

    // wenn die Gruppe im 'NEW' modus ist und die Button-Aktion vom Type 'new'
    // ist, dann ist der Button
    // der default Button.
    //
    if (getDataStatus() == NEW && (action instanceof ActionTypeUpdateRecord || action instanceof ActionTypeChangeUpdateRecord))
      return true;

    // Falls der Modus 'normal' ist, dann ist der SearchButton der default
    // button
    //
    if (getDataStatus() == SEARCH && action instanceof ActionTypeSearch)
      return true;

    // Falls der Modus 'edit' ist, dann ist der SpeicherButton der default
    // button
    //
    if (getDataStatus() == SEARCH && action instanceof ActionTypeSearchUpdateRecord)
      return true;
    if (getDataStatus() == UPDATE && action instanceof ActionTypeSearchUpdateRecord)
      return true;
    if (getDataStatus() == NEW && action instanceof ActionTypeSearchUpdateRecord)
      return true;

    // Falls man im Updatemodus eines Record ist, dann ist der Speicher-Button
    // der default Button
    //
    if (getDataStatus() == UPDATE && (action instanceof ActionTypeUpdateRecord || action instanceof ActionTypeChangeUpdateRecord))
      return true;

    return false;
  }
  
}
