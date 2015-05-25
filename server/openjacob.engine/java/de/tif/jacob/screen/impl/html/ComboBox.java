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

import org.apache.xml.utils.FastStringBuffer;

import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IComboBoxEventHandler;
import de.tif.jacob.screen.impl.HTTPComboBox;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComboBox extends Enum implements HTTPComboBox
{
  static public final transient String RCS_ID = "$Id: ComboBox.java,v 1.26 2010/10/15 12:15:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.26 $";

  private FastStringBuffer externalHtml = new FastStringBuffer();
  
  // überblenden der unterliegnede "definition"
  protected final ComboBoxInputFieldDefinition definition;

  protected ComboBox(IApplication app,  ComboBoxInputFieldDefinition _combo)
  {
    super(app, _combo, true);
    
    this.definition = _combo;
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
      
      Writer w = newCache();
      // the user can turn off the combobox with setEnabled(false);
      //
      if(isEditable() && isEnabled())
      {
        	boolean multiselect=getDataStatus()==SEARCH && isMultichoice()==true;
        	
          w.write("<table class=\"Multiselection\" id=\"container_"+etrId+"\" name=\"container_"+etrId+"\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;");
          getCSSStyle(context, w, boundingRect);
          w.write("\" border=\"0\"><tr><td class=\"Multiselection_Title\" >");
          w.write("<input style=\"width:100%;\" readonly value=\"");
          w.write(StringUtil.htmlEncode(getValue()));
          w.write("\" ");
          if(getTabIndex()>=0)
          {
          	w.write(" TABINDEX=\"");
          	w.write(Integer.toString(getTabIndex()));
          	w.write("\" ");
          }
          w.write(" type=\"text\" class=\"Multiselection_Title\" onclick=\"Multiselection_doMenuClick(this);\" onmouseout=\"Multiselection_doMenuMouseOut(this);\" ");
          
          w.write(" multiselect=\"");
          w.write(Boolean.toString(multiselect));
          w.write("\" id=\""+etrId+"\" name=\""+etrId+"\" submenu=\"combolist_"+etrId+"\" container=\"container_"+etrId+"\" ></td><td ");
          w.write("onclick=\"Multiselection_doMenuClick(getObj('"+etrId+"'));\" onmouseout=\"Multiselection_doMenuMouseOut(getObj('"+etrId+"'));\" ");
          w.write("class=\"combobox_trigger\" >&nbsp;</td></tr></table>\n");
          
          
          // external HTML
          //
          externalHtml = new FastStringBuffer();
        	String width=boundingRect==null?"auto":""+(boundingRect.width-3);
        	
        	externalHtml.append("\n<div style='width:"+width+"px;position:absolute;display:none;' nowrap='nowrap' onmouseover='Multiselection_doClearMenuTimer(this);' onmouseout='Multiselection_doStartMenuTimer(this);' id='combolist_"+etrId+"' parent='"+etrId+"' >\n");
        	externalHtml.append("<table class='combobox_pane' width='"+width+"' cellspacing='0' style='position:static;' cellpadding='0' >\n");
      
	  	    
        	// Leerer Eintrag für das Zurücksetzten aller Selectionen
        	// (Ist an Erster Stelle)
          if(multiselect)
          {
            String clear = StringUtil.htmlEncode(new CoreMessage("COMBOBOX_CLEAR").print());
            if(clear==null || clear.length()==0)
              clear="&nbsp;";
            externalHtml.append("<tr><td style='white-space:nowrap;' checked='false' class='combobox_option_icon' onclick=\"Multiselection_doDeselectAll(getObj('"+etrId+"'));\">"+clear+"</td></tr>\n");
          }
          int i=0;
          List selections = Arrays.asList(getSelection());
          
          // Gesonderte Einträge für "leer" und "nicht leer" bei der Suche
          // Bei Feldern welche pflicht snd macht eine "NULL" oder "NOT NULL" Suche keinen Sinn. Hier können diese
          // Einträge entfallen.
          //
          if(multiselect && getDataStatus() == SEARCH && !isRequired())
          {
            if(definition.getAllowNotNullSearch())
            {
              String searchNotNull = StringUtil.htmlEncode(new CoreMessage("COMBOBOX_SEARCH_NOTNULL").print());
              if(searchNotNull==null || searchNotNull.length()==0)
                searchNotNull="&nbsp;";
              if(selections.contains("!null"))
                externalHtml.append("<tr><td value=\"!null\" id=\"item_"+(i++)+"_"+etrId+"\" displayItem='"+etrId+"' onselectstart='return false;' style='white-space:nowrap;' onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon checked' onclick=\"Multiselection_doItemCheck(this);\">"+searchNotNull+"</td></tr>\n");
              else
                externalHtml.append("<tr><td value=\"!null\" id=\"item_"+(i++)+"_"+etrId+"\" displayItem='"+etrId+"' onselectstart='return false;' style='white-space:nowrap;' onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon unchecked' onclick=\"Multiselection_doItemCheck(this);\">"+searchNotNull+"</td></tr>\n");
              
              externalHtml.append("<tr><td><hr></td></tr>\n");
            }
              
            if(definition.getAllowNullSearch())
            {
              String searchNull = StringUtil.htmlEncode(new CoreMessage("COMBOBOX_SEARCH_NULL").print());
              if(searchNull==null || searchNull.length()==0)
                searchNull="&nbsp;";
              if(selections.contains("null"))
                externalHtml.append("<tr><td value=\"null\" id=\"item_"+(i++)+"_"+etrId+"\" displayItem='"+etrId+"' onselectstart='return false;' style='white-space:nowrap;' onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon checked' onclick=\"Multiselection_doItemCheck(this);\">"+searchNull+"</td></tr>\n");
              else
                externalHtml.append("<tr><td value=\"null\" id=\"item_"+(i++)+"_"+etrId+"\" displayItem='"+etrId+"' onselectstart='return false;' style='white-space:nowrap;' onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon unchecked' onclick=\"Multiselection_doItemCheck(this);\">"+searchNull+"</td></tr>\n");
            }
          }
        	
	  	    boolean isChecked=false;
          IComboBoxEventHandler handler = (IComboBoxEventHandler)getEventHandler(context);

          Iterator iter = this.entries.iterator();
	  	    while(iter.hasNext())
	  	    {
            Option option = (Option)iter.next();
            // Eine Combobox zeigt keine disabled Einträge an
            //
            if(option.enabled==false)
              continue;
            
	  	    	externalHtml.append("<tr>");
	  	      String fireEvent="";
	  	      if(multiselect)
	  	      {
	  	        isChecked=selections.contains(option.value);
	  	      }
	  	      else
	  	      {
	  	        isChecked=getValue()!=null && getValue().equals(option.value);
              // Nur wenn ein Eventhandler implementiert ist und dieser auch über diesen Eintrag
              // informiert werden will, wird ein onClick Event gesendet.
              //
              if(handler!=null && option.hasCallback)
              {
                fireEvent=";FireEvent(\""+Integer.toString(getId())+"\",\"change\");";
              }
	  	      }

	  	      if(isChecked)
	  	      {
	  	      	externalHtml.append("<td style='white-space:nowrap;' checked='true' displayItem='");
              externalHtml.append(etrId);
              externalHtml.append("' onselectstart='return false;' id=\"item_"+i);
              externalHtml.append("_");
              externalHtml.append(etrId);
              externalHtml.append("\" ");
              if(multiselect)
                 externalHtml.append("onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon checked' ");
              else
                externalHtml.append("onmouseover=\"$(this).addClassName('combobox_option_over');\" onmouseout=\"$(this).removeClassName('combobox_option_over');\"' class='combobox_option' ");
              externalHtml.append(" value='");
              externalHtml.append(StringUtil.htmlEncode(option.value));
              externalHtml.append("' onclick='Multiselection_doItemCheck(this);'>");
              if(handler!=null)
              {
                String filterd = handler.filterEntry(context,this,option.value);
                // check with == and not equals! Default implementation returns the option.value object. Easy detection if the 
                // application programmer has changed the returnd value object.
                if(filterd==option.value)
                  externalHtml.append(StringUtil.htmlEncode(option.getI18NLabel(context)));
                else
                  externalHtml.append(StringUtil.htmlEncode(filterd));
              }
              else
              {
                externalHtml.append(StringUtil.htmlEncode(option.getI18NLabel(context)));
              }
              externalHtml.append("&nbsp;</td>");
	  	      }
	  	      else
	  	      {
	  	      	externalHtml.append("<td style='white-space:nowrap;' checked='false' displayItem='");
              externalHtml.append(etrId);
              externalHtml.append("' onselectstart='return false;' id=\"item_"+i);
              externalHtml.append("_");
              externalHtml.append(etrId);
              externalHtml.append("\" ");
              if(multiselect)
                 externalHtml.append("onmouseover=\"$(this).addClassName('combobox_option_icon_over');\" onmouseout=\"$(this).removeClassName('combobox_option_icon_over');\"' class='combobox_option_icon unchecked' "); 
              else
                externalHtml.append("onmouseover=\"$(this).addClassName('combobox_option_over');\" onmouseout=\"$(this).removeClassName('combobox_option_over');\"' class='combobox_option' ");
              externalHtml.append(" value='");
              externalHtml.append(StringUtil.htmlEncode(option.value));
              externalHtml.append("' onclick='Multiselection_doItemCheck(this);");
              externalHtml.append(fireEvent);
              externalHtml.append("' >");
              if(handler!=null)
              {
                String filterd = handler.filterEntry(context,this,option.value);
                // check with == and not equals()! Default implementation returns the option.value object. Easy detection if the 
                // application programmer has changed the returnd value object.
                if(filterd==option.value)
                  externalHtml.append(StringUtil.htmlEncode(option.getI18NLabel(context)));
                else
                  externalHtml.append(StringUtil.htmlEncode(filterd));
              }
              else
              {
                externalHtml.append(StringUtil.htmlEncode(option.getI18NLabel(context)));
              }
              externalHtml.append("&nbsp;</td>");
	  	      }
	  	      externalHtml.append("</tr>\n");
            i++;
	  	    }
          // User defined value
          //
          if(definition.getAllowUserDefinedValue())
          {
            externalHtml.append("<tr><td><hr></td><tr>");
            externalHtml.append("<tr><td style='white-space:nowrap;' checked='false'  displayItem=");
            externalHtml.append(etrId);
            externalHtml.append(" ");
            externalHtml.append(" class='combobox_option' ");
            externalHtml.append(" onclick='Multiselection_doUserDefinedValue(this);");
            externalHtml.append("' >");
            externalHtml.append(StringUtil.htmlEncode(new CoreMessage("COMBOBOX_USER_DEFINED").print()));
            externalHtml.append("</td></tr>\n");
          }

          externalHtml.append("</table>");
	  	    externalHtml.append("</div>\n");
      }
      else
      {
        externalHtml = new FastStringBuffer();
        w.write("<table class='combobox_readonly' border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;position:absolute;");
        getCSSStyle(context, w, boundingRect);
        w.write("\"><tr><td class='combobox_value_readonly' style=\"overflow:hidden;white-space:nowrap;margin:0px\">");
        Option op = getOption(getValue());
        if(op!=null)
          w.write(StringUtil.htmlEncode(op.getI18NLabel(context)));
        else
          w.write(StringUtil.htmlEncode(getValue()));
        w.write("</td><td  class='combobox_trigger_readonly' width=\"16\">&nbsp;</td></tr></table>\n");
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
    
  	// the selectable list of the combo box
    //
    context.addComboboxAdditionalHTML(externalHtml.toString());

    if(getDataField().isDiverse())
      context.addOnLoadJavascript("initBehaviourDiverse('"+getEtrHashCode()+"');");
  }
  
	/**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid!=this.getId())
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
  
  public void enableNullOption(boolean enableFlag)
  {
    Iterator iter = entries.iterator();
    while (iter.hasNext())
    {
      Option option = (Option) iter.next();
      if (option.value.equals(""))
      {
        option.enabled = enableFlag;
        option.deleted = !enableFlag;
        return;
      }
    }
  }

	/**
	 * @return Returns the externalHtml.
	 */
	protected String getExternalHtml() 
	{
		return externalHtml.toString();
	}
}
