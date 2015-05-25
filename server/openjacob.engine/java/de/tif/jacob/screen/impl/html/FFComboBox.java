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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractBrowserTableField;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPForeignField;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FFComboBox extends SingleDataGUIElement implements HTTPForeignField
{
  static public final transient String RCS_ID = "$Id: FFComboBox.java,v 1.18 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.18 $";

  private final static String EVENT_DESELECT  = "deselect";
  private final static String EVENT_OPEN      = "open";
  private final static String EVENT_SELECT    = "select";
  
  static private final transient Log logger = LogFactory.getLog(Application.class);
  
  private long      changeCount  = -1;
  protected final ForeignInputFieldDefinition definition;

  private List toKeysFields = new ArrayList();
  private IDataKeyValue foreignKeyValue;
  private ICaption caption;
  final AdhocBrowserDefinition browserDefinition; 
  IDataBrowserInternal browserData;
  private String displayFieldName ;
  private final boolean definitionIsRequired; // wird durch die applications definition bestimmt und nicht durch die setRequired Methode

  
  /**
   * 
   * @param app
   * @param foreign
   */
  protected FFComboBox(IApplication app, ForeignInputFieldDefinition foreign)
  {
    super(app, foreign.getName(), null,foreign.isVisible(),foreign.isReadOnly(), foreign.getRectangle(), foreign.getForeignTableAlias(), foreign.getForeignTableField(), foreign.getFont(), foreign.getProperties());
    this.definition = foreign;

    if (null != foreign.getCaption())
    {  
      addChild(caption=new ForeignFieldCaption(app, foreign.getCaption()));
      setLabel(foreign.getCaption().getLabel());
    }
    
    getDataField().setFieldType(DataField.TYPE_FOREIGN);
    
    definitionIsRequired=((ITableField)definition.getRelationToUse().getToForeignKey().getTableFields().get(0)).isRequired();
    
    // fromTable <-- toTable
    // 
    Iterator iter = definition.getRelationToUse().getToForeignKey().getTableFields().iterator();
    ITableAlias toTable = definition.getRelationToUse().getToTableAlias();    
    while(iter.hasNext())
    {
      ITableField field = (ITableField)iter.next();
      DataField d = new DataField(this,toTable,field,"");
      toKeysFields.add(d);
    }
    
    browserDefinition = new AdhocBrowserDefinition(app.getApplicationDefinition(),foreign.getBrowserToUse());
    try
    {
      // prï¿½fen ob das Feld welches anzuzeigen ist bereits im Browser vorhanden ist.
      //- wenn ja wird dann einfach dieses genommen und kein weiteres Feld hinzugefï¿½gt.
      //
      Iterator fieldIter = browserDefinition.getBrowserFields().iterator();
      while (fieldIter.hasNext())
      {
        IBrowserField field = (IBrowserField)fieldIter.next();
        if(field instanceof AbstractBrowserTableField)
        {
          AbstractBrowserTableField tableField = (AbstractBrowserTableField)field;
          if(tableField.getTableField().equals(foreign.getForeignTableField()))
          {
            displayFieldName = browserDefinition.getBrowserFieldName(field);
          }
        }
      }
      if(displayFieldName==null)
        displayFieldName = browserDefinition.addBrowserField(foreign.getForeignTableAlias().getName(), foreign.getForeignTableField().getName(), SortOrder.NONE, "<unset>");
    }
    catch (NoSuchFieldException e)
    {
      ExceptionHandler.handle(e);
    }
  }

  public void tryBackfill(IClientContext context, int maxDisplayRecords, boolean displaySelectionDialog, boolean countRecordsAsWell) throws Exception
  {
  }


  public int getTabIndex()
  {
    return definition.getTabIndex();
  }

  
  public ICaption getCaption()
  {
    return caption;
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * The GuiElement must check if the event has 'this' as target. If the target
   * not the object itself, send the event to all childs.
   *  
   */
  public final boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // ..or the object itself has send the event
    //
    if(guid != this.getId())
      return super.processEvent(context,guid,event,value);
    
    // this event will be fired if the user switch to the update mode
    // and changed the element in the input field
    //
    if(event.equals(EVENT_DESELECT))
    {
      removeBackfill(context);
    }
    else if(event.equals(EVENT_OPEN))
    {
      search(context);
    }
    else if(event.equals(EVENT_SELECT))
    {
      IDataTableRecord selectedRecord =browserData.getRecord(Integer.parseInt(value)).getTableRecord(); 
      context.getDataAccessor().propagateRecord(selectedRecord,Filldirection.NONE);
    }
    else
    	logger.warn("Unknown event ["+event+"] for GuiElement ["+getClass().getName()+"]");
    return true;
  }
  

  public IDataBrowser search(IClientContext context) throws Exception
  {
    // Aus Rücksicht zu den alten jACOB Applikationen kann nicht der DatenAccessor der aktuellen
    // GUI genommen werden. Es wird ein neuer DatenAccessor genommen, damit die zurückgefüllte
    // GUI nicht die Suche der Combobox beeinflusst.
    // Warum nicht die GUI als QBE?!
    // Das aufklappen der Combobox erfolgt asynchron via AJAX. Somit hat in diesem Moment der Server
    // nicht die aktuellen GUI Eingaben. Wenn dieses Verhalten nicht gewünscht ist, dann sollte ein normales FF
    // genommen werden
    if(browserData==null)
      browserData = (IDataBrowserInternal) context.getDataAccessor().newAccessor().createBrowser(browserDefinition);
    browserData.getAccessor().qbeClearAll();
    browserData.clear();
    
    IForeignFieldEventHandler eh = (IForeignFieldEventHandler) this.getEventHandler(context);
    if (null != eh)
    {
      // Hier hat man noch die Möglichkeit via Script seine QBE zu setzten
      // Wichtig: context enthält den Accessor der GUI
      //          browserData enthält den DataAccessro mit dem gesucht wird.
      eh.beforeSearch(context, this, browserData);
    }
    
    browserData.search(this.definition.getRelationSet());
    return browserData;
  }

  /**
   * This event occours if the FFComboBox displays an record and the user select the <empty> entry
   * 
   * @param context
   * @param value
   * @throws Exception
   */
  protected void removeBackfill(IClientContext context) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    IDataTable table = accessor.getTable(getDataField().getTableAlias());
    table.clear();
    setDataStatus(context, SEARCH);
    
    clearInternalKey();
    
    IForeignFieldEventHandler eh = (IForeignFieldEventHandler) this.getEventHandler(context);
    if (null != eh)
      eh.onDeselect(context, this);
  }
  
  /**
   * The user clicks on the search icon of the foreign field.
   * 
   * @param context
   * @param value
   * @throws Exception
   */
  public void tryBackfill(IClientContext context) throws Exception
  {
  }
  
  /**
   * 
   * @param accessor
   */
  public void setQbeValues(IClientContext context, IDataAccessor accessor) throws Exception
  {
    if(this.foreignKeyValue!=null)
      accessor.getTable(getFromTable()).qbeSetPrimaryKeyValue(this.foreignKeyValue);
  }
  
  
  /**
   * render the HTML for this object to the hands over Writer
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
  	// update the element if the data has been changed
  	// /e.g. an element of an table alias has been selected
  	// -----------------------------------------------------------------------------
  	// ATTENTION: The status of >> invisible <<  fields has to be updated as well, because
  	// they might be needed for scripting (Mike Special :-) and they are also
  	// used for commiting data on update.
  	// -----------------------------------------------------------------------------
  	//
    // Wenn der DisplayRecord sich ändert (z.B. beim Umschalten von Selected=>Update)
    // wird intern ein resetCache() ausgelöst da sich das Element neu zeichnen muss. Dummerweise löscht dies immer die errorDecoration.
    // Diese muß leider hier "rübergerettet" werden.
    FastStringWriter helper = this.lastErrorMessage;
  	IDataTableRecord currentDataRecord = getSelectedRecord(context);
  	this.lastErrorMessage = helper;
    
  	if(!isVisible())
      return;
    
    super.calculateHTML(context);

    if(getCache()==null)
    {
    	Writer w = newCache();
      // the text field
      //
      String etrId = getEtrHashCode();
      if(isEditable() && isEnabled())
      {
      	w.write("<table id='"+etrId+"' class='combobox' border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;position:absolute;");
      	getCSSStyle(context, w, boundingRect);
      	w.write("\" ");
        w.write("onclick='openFFComboBox(\""+Integer.toString(getId())+"\");'");
        w.write("><tr><td class='combobox_value' style=\"overflow:hidden;white-space:nowrap;margin:0px\">");
        w.write(StringUtil.htmlEncode(getValue()));
        w.write("</td><td  class='combobox_trigger' width=\"16\">&nbsp;</td></tr></table>\n");
      }
      else
      {
        w.write("<table id='"+etrId+"' class='combobox_readonly' border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;position:absolute;");
        getCSSStyle(context, w, boundingRect);
        w.write("\"><tr><td class='combobox_value_readonly' style=\"overflow:hidden;white-space:nowrap;margin:0px\">");
        w.write(StringUtil.htmlEncode(getValue()));
        w.write("</td><td  class='combobox_trigger_readonly' width=\"16\">&nbsp;</td></tr></table>\n");
      }
    }
  }

  /**
   * Returns the record to display for this foreign field
   * 
   * @deprecated use {@link #getSelectedRecord(IClientContext)} instead
   * @param context
   * @return
   * @throws Exception
   */
  public IDataTableRecord getDisplayRecord(IClientContext context) throws Exception
  {
    return getSelectedRecord(context);
  }
  
  /**
   * {@inheritDoc}
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception
  {
    // update the element if the data has been changed
    // /e.g. an element of an table alias has been selected
    //
    IDataTableInternal table = (IDataTableInternal) context.getDataAccessor().getTable(getDataField().getTableAlias());
    IDataTableRecord currentDataRecord = table.getSelectedRecord();
    
    // a new record in the ForeignField has been selected. Fetch them and display it.
    //
    if(currentDataRecord!=null  && table.getChangeCount()!= changeCount)
    {
      changeCount = table.getChangeCount();
      // Falls das ForeignField den selectedRecord anzeigen soll (index==0) wird dieser
      // dem Element gleich ï¿½bergeben.
      if(getDisplayRecordIndex()==-1)
      {
        setValue(context, currentDataRecord);
      }
      // ...ansonsten wird die ganze Tabelle ï¿½bergeben und das Element kann sich den Record selber
      // raus holen. Wird z.B. bei eine MutableGroup verwendet wenn man einen "InformBrowser" selbst aufbaut.
      //
      else
      {
        setValue(context, table);
      }
      
      if(parent.getDataStatus()== UPDATE)
        setDataStatus(context, UPDATE);
      else if(parent.getDataStatus()== NEW)
        setDataStatus(context, NEW);
      else
        setDataStatus(context, SELECTED);
      resetCache();
    }
    // The datarecord of the ForeignField has been deleted. Reset the gui element and display nothing.
    //
    else if(currentDataRecord==null  && table.getChangeCount()!= changeCount)
    {
      changeCount = table.getChangeCount();
      
      setValue(context, (IDataTableRecord) null);
      
      // prüfen ob eventuell ein DIVERSE gesetzt ist. Dies kommt beim MultipleUpdate vor
      // wenn alle betroffenen Rekords nicht auf die gleiche ForeignRecord verweisen.
      //
      DataField field = ((DataField) toKeysFields.get(0));
      IDataTableRecord selectedRecord = context.getDataTable(field.getTableAlias().getName()).getSelectedRecord();
      if(selectedRecord!=null)
      {
        Object value = selectedRecord.getValue(field.getField());
        if(value == IDataMultiUpdateTableRecord.DIVERSE)
        {
          setValue(IDataMultiUpdateTableRecord.DIVERSE); 
          for(int i=0;i<toKeysFields.size();i++)
            ((DataField)toKeysFields.get(i)).setValue(IDataMultiUpdateTableRecord.DIVERSE);
        }
      }
      setDataStatus(context, SEARCH);
      resetCache();
    }
    return currentDataRecord;
  }
  
  
  /** 
   * 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
  }
  
  
  /**
   * 
   */
  protected void addDataFields(Vector fields)
  {
    if(fields!=null)
    {
      fields.addAll(toKeysFields);
      super.addDataFields(fields);
    }
  }
  

  /**
   * 
   * @param value
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    super.setValue(context, record);
    // Enthï¿½lt die Felder welche an der Gruppe zu setzten sind.
    // Die Reihenfolge von getprimarykeyvalue ist mit der toforeignkey identisch
    // und zu setzen.
    //
    IDataKeyValue key = null;
    if (record != null && (key = record.getPrimaryKeyValue()) != null)
    {
      // Nur wenn der selektierte Record sich wirklich geändert hat ein onSelect auslösen und
      // den Key übernehmen. Ansonsten werden ünnötige Events ausgelöst.
      if (!key.equals(this.foreignKeyValue))
      {
        for (int i = 0; i < key.numberOfFieldValues(); i++)
        {
          ((DataField) toKeysFields.get(i)).setValue(key.getFieldValue(i).toString());
        }
        this.foreignKeyValue = key;
        IForeignFieldEventHandler eh = (IForeignFieldEventHandler) this.getEventHandler(context);
        if (null != eh)
          eh.onSelect(context, record, this);
      }
    }
    else
    {
      clearInternalKey();
      IForeignFieldEventHandler eh = (IForeignFieldEventHandler) this.getEventHandler(context);
      if (null != eh)
        eh.onDeselect(context, this);
    }
  }

  
  public boolean isEnabled()
  {
    GroupState dataStatus = parent.getDataStatus(); 
    if(    dataStatus==NEW 
        || dataStatus==SELECTED  
        || dataStatus==UPDATE)
    {     
      return (!isDefinedAsReadOnlyInJAD) && isNotUserDisabled();       
    }
      
    return isNotUserDisabled();
  }

  /**
   * @return Returns the isEditable.
   */
  public boolean isEditable()
  {
    GroupState dataStatus = parent.getDataStatus(); 

    // if the group in SELECTED modus the foreign field
    // is not editable
    //
    if(dataStatus==SELECTED)
      return false;
    
    // Falls der Record zurückgefüllt ist und die Grupp nicht im
    // selected Status ist, dann ist das Feld auf jedenfall editierbar.
    //
    else if(dataStatus==SEARCH)
      return true;

    else /*if(dataStatus==NEW || dataStatus==SELECTED|| dataStatus==UPDATE)*/
    {
      return (!isDefinedAsReadOnlyInJAD) && isEditable;
    }
  }


  /**
   * Clears/Reset the corresponding data element in this GUI element
   * 
   * The can be fired from the clear button in a group. The group calls
   * the clear method in all childs.
   * 
   */
  public void clear(IClientContext context) throws Exception
	{
  	// the super implementation calls all childs to clear them
  	super.clear(context);
  	
  	context.getDataAccessor().getTable(getDataField().getTableAlias()).clear();
  	clearInternalKey();
  	setDataStatus(context, SEARCH);
  }

  /**
   * Reset the toTable and fromTable field values
   * 
   *
   */
  protected void clearInternalKey()
  {
  	// clear the toRelation from the group to the foreign element
  	//
  	for (int i=0; i < toKeysFields.size(); i++)
  	{
  		((DataField) toKeysFields.get(i)).setValue("");
  	}
  	this.foreignKeyValue = null;
  }
  
  /**
   * The foreign field is required if one of the to-relation of the group
   * is required.
   * 
	 * @return Returns the definitionIsRequired.
	 */
	public boolean isRequired()
	{
		return definitionIsRequired || isRequired;
	}

	/*
	 *  
	 * @see de.tif.jacob.screen.IForeignField#isBackfilled()
	 */
	public boolean isBackfilled()
	{
		return this.foreignKeyValue != null;
	}

  public String getForeignTableAlias()
  {
    return this.definition.getForeignTableAlias().getName();
  }

  /**
   * @return Returns the fromTable.
   */
  public ITableAlias getFromTable()
  {
    return definition.getRelationToUse().getFromTableAlias();
  }

  /**
   * @return Returns the toTable.
   */
  public ITableAlias getToTable()
  {
    return definition.getRelationToUse().getToTableAlias();
  }

  
  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IForeignFieldEventHandler)
        ((IForeignFieldEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IForeignFieldEventHandler.class.getName()+"]");
    }
  }

  public IKey getToKey()
  {
    return definition.getRelationToUse().getToForeignKey();
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
  /**
   * A foreign field caption should fetch the data status of the
   * group it belongs to and not from the foreign field itself,
   * since this delivers the data status of the foreign table
   * alias.
   * 
   * @author Andreas Sonntag
   */
  private class ForeignFieldCaption extends Caption
  {
    private ForeignFieldCaption(IApplication app, de.tif.jacob.core.definition.guielements.Caption caption)
    {
      super(app, caption);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.IGuiElement#getDataStatus()
     */
    public GroupState getDataStatus()
    {
      // fetch data status from parent of foreign field, i.e. the group
      return FFComboBox.this.getParent().getDataStatus();
    }
  }

  /** 
   * Used from externa JSP page. Don't remove them!!!!!
   * @return
   */
  public String getDisplayFieldName()
  {
    return displayFieldName;
  }
}
