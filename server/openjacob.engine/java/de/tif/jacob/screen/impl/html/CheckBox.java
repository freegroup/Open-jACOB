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

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;

import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.BooleanFieldType;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.HTTPCheckBox;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class CheckBox extends SingleDataGUIElement implements HTTPCheckBox
{
  static public final transient String RCS_ID = "$Id: CheckBox.java,v 1.12 2010/10/18 10:42:39 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.12 $";

  private final CheckBoxInputFieldDefinition definition;
  private final Caption caption;
  protected CheckBox(IApplication app,  CheckBoxInputFieldDefinition _checkBox)
  {
    super(app, _checkBox.getName(), null, _checkBox.isVisible(),_checkBox.isReadOnly(),_checkBox.getRectangle(),_checkBox.getLocalTableAlias(), _checkBox.getLocalTableField(), null, _checkBox.getProperties());
    
    definition = _checkBox;
    if (null != _checkBox.getCaption())
    	addChild(caption=new Caption(app, _checkBox.getCaption()));
    else
      caption = null;
  }

  private boolean hasUnderlyingBoolean()
  {
    ITableField field = this.definition.getLocalTableField();
    return field != null && field.getType() instanceof BooleanFieldType;
  }

  
  public boolean isChecked()
  {
    if(hasUnderlyingBoolean())
      return StringUtil.toSaveString(getValue()).equals("true");
    else
      return StringUtil.toSaveString(getValue()).equals("1");
  }

  public void setChecked(boolean flag)
  {
    if(flag)
    {
      if(hasUnderlyingBoolean())
        setValue("true");
      else
        setValue("1");
    }
    else
    {
      if(hasUnderlyingBoolean())
        setValue("false");
      else
        setValue("0");
    }
  }

  public int getTabIndex()
  {
    return definition.getTabIndex();
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid!=this.getId())
      return super.processEvent(context,guid,event,value);
    
    return Command.processEvent(this, context, guid,event,value);
  }
   
  /**
   * The state of the checkbox will handled in the processEvent method.
   * 
   */
  public boolean processParameter(int guid, String data) throws IOException, NoSuchFieldException
  {
    return Command.processParameter(this, guid,data);
  }
  
  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    Command.onGroupDataStatusChanged(this, context,groupStatus);
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
      // render all childs e.g The label/caption of a text field.
      // FREEGROUP: PERFORMANCE UMSTELLUNG
      // Die Kinder eines Eingabefeldes (bis jetzt immer 'Caption') müssen nur dann
      // neu berechnet werden, wenn sich das Textelement selbst verändert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      
      String value = StringUtil.toSaveString(getValue());
      Writer w = newCache();
      w.write("<input type=\"hidden\" id=\"");
      w.write(getEtrHashCode());
      w.write("\" name=\"");
      w.write(getEtrHashCode());
      w.write("\" value=\"");
      w.write(value);
      w.write("\" >");
     	w.write("<input id=\"container_");
      w.write(getEtrHashCode());
      w.write("\" style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\" onClick=\"getObj('");
      w.write(getEtrHashCode());
      if (SEARCH == getDataStatus())
      {
        // Return empty string if unchecked because we do not
        // want to search for unchecked entries
        if (hasUnderlyingBoolean())
          w.write("').value=this.checked?'true':'';");
        else
          w.write("').value=this.checked?'1':'';");
      }
      else
      {
        if (hasUnderlyingBoolean())
          w.write("').value=this.checked?'true':'false';");
        else
          w.write("').value=this.checked?'1':'0';");
      }
      if(getEventHandler(context)!=null)
      {  
        w.write("FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("', 'click');");
      }
      w.write("\" ");
      if(getTabIndex()>=0)
      {
      	w.write("TABINDEX=\"");
      	w.write(Integer.toString(getTabIndex()));
      	w.write("\" ");
      }
      if(context.isDebug())
      {  
        w.write(" title=\"");
        w.write(getDataField().toString());
        w.write("\"");
      }
      if(!isEditable() || !isEnabled()) 
        w.write(" DISABLED ");
     
      if(!StringUtil.saveEquals("",value) && !value.equals("0") && !value.equalsIgnoreCase("false") && !getDataField().isDiverse())
      	w.write(" checked ");
      w.write(" type=\"checkbox\">\n");
    }
  }
  
  public void setBackgroundColor(Color color)
  {
    if(caption!=null)
      caption.setBackgroundColor(color);
    resetCache();
  }

  public void setColor(Color color)
  {
    if(caption!=null)
      caption.setColor(color);
    resetCache();
  }

  /**
   * 
   * @param context 
   * @param value
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    // The record is empty or the element has not a binding to the database
    // (calculated element)
    if (record == null || getDataField().getField() == null)
    {
      // Es darf nur der Wert zurück gesetzt werden wenn das Feld keinen
      // Eventhandler definiert hat.
      // Ist ein Eventhandler definiert, ist dieser bei einem Nicht-DB-Feld für
      // den Inhalt verantwortlich
      //
      if (getEventHandler(context) == null)
        setValue((String) null);
    }
    else
    {
      String rawValue = record.getStringValue(getDataField().getField().getFieldIndex());
      if(hasUnderlyingBoolean()==true && rawValue!=IDataMultiUpdateTableRecord.DIVERSE)
      {
        Boolean b =record.getBooleanValue(getDataField().getField().getFieldIndex());        
        setValue(b!=null ?b.toString():Boolean.FALSE.toString());
      }
      else
      {
        setValue(rawValue);
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
      context.addOnLoadJavascript("initBehaviourDiverse('checkbox_"+getEtrHashCode()+"');");
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
