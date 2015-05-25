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
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPSingleDataGuiElement;
import de.tif.jacob.util.StringUtil;
;

/**
 * @author Andreas Herz
 *
 */
public abstract class SingleDataGUIElement extends GuiHtmlElement implements HTTPSingleDataGuiElement
{
  static public final transient String RCS_ID = "$Id: SingleDataGUIElement.java,v 1.17 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.17 $";

  static private final transient Log logger = LogFactory.getLog(SingleDataGUIElement .class);
  
  protected DataField dataField;
  
  protected final boolean isDefinedAsReadOnlyInJAD;              // depends on the schema definition. 
  protected boolean       isEditable   = true;     // depends on the state of the group or field
  protected boolean       isRequired   = false;    // can be changed via API. The Required flag in the *.jad is dominant.
  protected int         displayRecordIndex = -1;  // -1 = selected Record
  public abstract int getTabIndex();

  /**
   * 
   * @param name
   * @param label
   * @param isVisible
   * @param alias
   * @param tableField
   */
  protected SingleDataGUIElement(IApplication app,  String name, String label, boolean isVisible,boolean isReadOnly, Rectangle boundingRect,ITableAlias alias, ITableField tableField, FontDefinition font, Map properties)
  {
    super(app, name, label, isVisible, boundingRect, properties);
    
    this.setFont(font);
    this.isDefinedAsReadOnlyInJAD=isReadOnly;
    dataField = new DataField(this, alias, tableField,"");
  }

  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId() &&  isEditable())
    {
      if(logger.isDebugEnabled())logger.debug("Setting data ["+data+"] for"+getDataField());
      setValue(data);
      return true;
    }
    
    // Ein SingleDataGuiElement sollte 'eigentlich' keine Daten haltende Kinder haben.
    return false;    
  }
  
 
  /**
   * 
   */
  public void onGroupDataStatusChanged(IClientContext context, GroupState groupStatus) throws Exception
  {
    // the status of the parent has changed
    //
// Wechsel von SELECTED => SELECTED wird so nicht beachtet.
// Dies kann vorkommen wenn man im Browser einen anderen Record selektiert
// Dies erzeugt dann ein onGroupStatusChanged mit SELECTED => SELECTED    
//    if(groupStatus!= getDataStatus())
    {
      setDataStatus(context,groupStatus);
      if(groupStatus==SELECTED)
          setEditable(false);
      else
          setEditable(true);
      super.onGroupDataStatusChanged(context, groupStatus);
    }
  }

  
  public String getEventHandlerReference()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 
   */
  protected void addDataFields(Vector fields)
  {
    if(getDataField()!=null && fields!=null && getDataField().getField()!=null && getDataField().getTableAlias()!=null)
      fields.add(getDataField());
  }
  
  /**
   * 
   * @param value
   * 
   */
  public void setValue(String value)
  {
    // Don't invalidate the cache if the new data the same
    // SEHR Wichtig für MultipleUpdate.
    // Grund: Der Wert im DataField kann das Singleton (IDataMultiUpdateTableRecord#DIVERSE) 
    //        sein. Wenn wir dies ungesehen austauschen, dann kann nicht mehr erkannt werden
    //        ob der Anwender den Wert "wirklich" verändert hat.
    //  
    if(StringUtil.saveEquals(value,(String)getDataField().getValue()))
        return;
    
    resetCache();
    getDataField().setValue(value); 
  }

  
  /**
   * 
   * @param context 
   * @param value
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    // This element is not bounded to a database field
    //
    if (getDataField().getField() == null)
    {
      // Es darf nur der Wert zurueck gesetzt werden wenn das Feld keinen
      // Eventhandler definiert hat. Dies ist sonst aufgabe des Eventhandler.
      //
      if (getEventHandler(context) == null)
        setValue((String) null);
    }
    else
    {
      if(record==null)
        setValue((String) null);
      else
        setValue(record.getStringValue(getDataField().getField().getFieldIndex(), context.getLocale()));
    }
  }

  /**
   * It is possible to hand over a DataTable. The UI Elements select the record
   * which should be display.
   * Required for the MutableGroup. 
   * 
   * @param context
   * @param table
   * @throws Exception
   */
  public void setValue(IClientContext context, IDataTable table) throws Exception
  {
    if(table.recordCount()>displayRecordIndex)
      setValue(context, table.getRecord(displayRecordIndex));
    else
      setValue(context, (IDataTableRecord)null);
  }
  
  /**
   * 
   * @return
   */
  public String getValue()
  {
    return (String)getDataField().getValue(); 
  }
  

  /**
   * Clears/Reset the corresponding data element in this GUI element
   * 
   * The can be fired from the clear button in a group. The group calls
   * the clear method in all childs.
   * 
   * @author Andreas Herz
   */
  public void clear(IClientContext context) throws Exception
  {
    // the super implementation calls all children to clear them
    super.clear(context);
    
    // and now clear the data element itself
    getDataField().setValue("");
    
    // the element has been resettet - now we can edit it
    //
    isEditable = true;
  }
  
  /**
   * The required flag from the application definition is high prio.
   * 
   * @return
   */
  public boolean isRequired()
  {
    return isRequired || getDataField().isRequired(); 
  }

  /**
   * 
   * @param isRequired
   */
  public void setRequired(boolean isRequired)
  {
    if(this.isRequired!=isRequired)
    {  
      resetCache();
      // Die Caption zeichnet sich in Abhï¿½ngigkeit des required Flags des Parent.
      // -> alle Kinder mï¿½ssen sich neu berechnen
      //
      for (int i=0; i<getChildren().size();i++)
      {
        ((GuiHtmlElement)getChildren().get(i)).resetCache();
      }
    }
    this.isRequired =isRequired;
  }
  
  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("SingleDataGUIElement[");
    buffer.append("hashCode  = ").append(""+getId());
    buffer.append("dataField = ").append(getDataField());
    buffer.append("]");
    return buffer.toString();
  }
  
  /**
   * @return Returns the isEditable.
   */
  public boolean isEditable()
  {
    // Normal controls which have been explicitly enabled by application programmer
    // must be by editable!
    // See request 270 in QM
    //
    if (this.isUserEnabled != null && getDataField().getField() == null)
      return this.isUserEnabled.booleanValue();
      
    GroupState dataStatus = getDataStatus(); 
    if(    dataStatus==NEW 
        || dataStatus==SELECTED  
        || dataStatus==UPDATE)
    {
      return (!isDefinedAsReadOnlyInJAD) && isEditable;
    }
   return isEditable;
  }

  
  public boolean isEnabled()
  {
    GroupState dataStatus = getDataStatus(); 
    if(    dataStatus==NEW 
        || dataStatus==SELECTED  
        || dataStatus==UPDATE)
    {     
      return (!isDefinedAsReadOnlyInJAD) && isNotUserDisabled();        
    }
      
    return isNotUserDisabled();
  }
  
  /**
   * @param isEditable The isEditable to set.
   */
  public void setEditable(boolean isEditable)
  {
    this.isEditable = isEditable;
    // reset cache for child (e.g. captions) elements as well 
    invalidate();
  }


  public DataField getDataField()
  {
    return dataField;
  }

  public ITableField getTableField()
  {
    return getDataField().getField();
  }

  public int getDisplayRecordIndex()
  {
    return displayRecordIndex;
  }

  public void setDisplayRecordIndex(int displayRecordIndex)
  {
    this.displayRecordIndex = displayRecordIndex;
  }

}
