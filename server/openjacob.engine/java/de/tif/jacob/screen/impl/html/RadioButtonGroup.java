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

import de.tif.jacob.core.definition.guielements.RadioButtonGroupInputFieldDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IRadioButtonGroupEventHandler;
import de.tif.jacob.screen.impl.HTTPRadioButtonGroup;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RadioButtonGroup extends Enum implements HTTPRadioButtonGroup
{
  static public final transient String RCS_ID = "$Id: RadioButtonGroup.java,v 1.14 2010/10/18 08:51:01 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.14 $";

  protected RadioButtonGroup(IApplication app,  RadioButtonGroupInputFieldDefinition _radio)
  {
    super(app, _radio, false);
  }

  /**
   * Add the handsover entry to the listbox element
   * 
   * @param entry      The option to add
   */
  public void addOption(String entry, String label)
  {
    super.addOption(entry);
    // TODO: label muss sich noch gemerkt werden.
  }
  

  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    
  	String etrId = getEtrHashCode();

    
    if(getCache()==null)
    {
      // write all childs to the screen. e.g. The caption of a combo box or any other decoration
      // only neccessary if 
      super.calculateHTML(context);
      
      IRadioButtonGroupEventHandler handler = (IRadioButtonGroupEventHandler)getEventHandler(context);

      Writer w = newCache();
      // the user can turn off the combobox with setEnabled(false);
      //
      if(isEditable() && isEnabled())
      {
        	boolean multiselect=getDataStatus()==SEARCH && isMultichoice()==true;
          w.write("\t<div style=\"");
          getCSSStyle(context, w, boundingRect);
          w.write("\" id=\"container_");
          w.write(etrId);
          w.write("\" class=\"radio_normal\">");
          w.write("<input type=\"hidden\" name=\"");
          w.write(etrId);
          w.write("\" id=\"");
          w.write(etrId);
          w.write("\" value=\"");
          w.write(StringUtil.htmlEncode( getValue()));
          w.write("\">\n");
          // immer mit einem Offset von 25px das nächste Element zeichnen
          //
          Iterator iter = this.entries.iterator();
          int i=0;
          while(iter.hasNext())
          {
            Option option = (Option)iter.next();
            if (!option.enabled)
              continue;
            
            String checked = "";
            w.write("<input onclick='updateRadioButtonGroup(\"");
            w.write(etrId);
            w.write("\")");
            // Nur wenn ein Eventhandler implementiert ist und dieser auch über diesen Eintrag
            // informiert werden will, wird ein onClick Event gesendet.
            //
            if(handler!=null && option.hasCallback)
            {
              w.write(";FireEvent(\""+Integer.toString(getId())+"\",\"change\");");
            }
            w.write("'");
            
            w.write(" id=\"radio_");
            w.write(Integer.toString(i));
            w.write("_");
            w.write(etrId);
            w.write("\" style=\"position:absolute;top:");
            w.write(Integer.toString(i*25));
            if(multiselect)
            {
              w.write("px;left:0px\" type=\"checkbox\" ");
              checked = (Arrays.asList(getSelection()).contains(option.value))?"checked":"";
            }
            else
            {
              w.write("px;left:0px\" name=\"");
              w.write(etrId);
              w.write("\" type=\"radio\" ");
              checked = (option.value.equals(getValue()))?"checked":"";
            }
            w.write(checked);
            w.write(" value=\"");
            w.write(StringUtil.htmlEncode( option.value));
            w.write("\">\n");
            w.write("<div class=\"caption_normal_search\" style=\"position:absolute;top:");
            w.write(Integer.toString(i*25));
            w.write("px;left:25px\">");
            w.write(StringUtil.htmlEncode( option.getI18NLabel(context)));
            w.write("</div>\n");
            i++;
          }
          w.write("\t</div>\n");
      }
      else
      {
        w.write("\t<div style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" class=\"radio_normal\">");
        // immer mit einem Offset von 25px das nächste Element zeichnen
        //
        Iterator iter = this.entries.iterator();
        int i=0;
        while(iter.hasNext())
        {
          Option option = (Option)iter.next();
          if (!option.enabled)
            continue;
          
          String checked = (option.value.equals(getValue()))?"CHECKED":"";
          w.write("<input disabled style=\"position:absolute;top:");
          w.write(Integer.toString(i*25));
          w.write("px;left:0px\" type=\"radio\" ");
          w.write(checked);
          w.write(" name=\"");
          w.write(etrId);
          w.write("\"");
          w.write(" value=\"");
          w.write(StringUtil.htmlEncode( option.value));
          w.write("\">");
          w.write("<div class=\"caption_normal_search\" style=\"position:absolute;top:");
          w.write(Integer.toString(i*25));
          w.write("px;left:25px\">");
          w.write(StringUtil.htmlEncode( option.getI18NLabel(context)));
          w.write("</div>");
          i++;
        }
        w.write("\t</div>\n");
      }
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
      context.addOnLoadJavascript("initBehaviourDiverse('radio_0_"+getEtrHashCode()+"');");
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
