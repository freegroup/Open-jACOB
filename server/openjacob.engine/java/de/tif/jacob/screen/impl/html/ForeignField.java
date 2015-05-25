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

import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
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
public class ForeignField extends SingleDataGUIElement implements HTTPForeignField
{
  static public final transient String RCS_ID = "$Id: ForeignField.java,v 1.40 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.40 $";

  private final static String EVENT_DELETE    = "delete";
  private final static String EVENT_DESELECT  = "deselect";
  private final static String EVENT_SHOW      = "show";
  private final static String EVENT_SEARCH    = "search";
  private final static String EVENT_MAXSEARCH = "maxsearch";
  
  static private final transient Log logger = LogFactory.getLog(Application.class);
  
  protected String  resetValue   ="";   
  private long      changeCount  = -1;
  protected final ForeignInputFieldDefinition definition;

  private List toKeysFields = new ArrayList();
  private IDataKeyValue foreignKeyValue;
  private ICaption caption;
  
  private final boolean definitionIsRequired; // wird durch die applications definition bestimmt und nicht durch die setRequired Methode

  public int getTabIndex()
  {
    return definition.getTabIndex();
  }

  /**
   * 
   * @param app
   * @param foreign
   */
  protected ForeignField(IApplication app, ForeignInputFieldDefinition foreign)
  {
    super(app, foreign.getName(), null,foreign.isVisible(),foreign.isReadOnly(), foreign.getRectangle(), foreign.getForeignTableAlias(), foreign.getForeignTableField(), foreign.getFont(), foreign.getProperties());
    this.definition = foreign;

    if (null != foreign.getCaption())
    {  
      addChild(caption=new ForeignFieldCaption(app, foreign.getCaption()));
      setLabel(foreign.getCaption().getLabel());
    }
    
    // Da um das Element eine HTML Tabelle ist, muss die Höhe korrigiert werden und an
    // der rechten Seite muss Platz für ein Button sein welcher 16 Pixel groß ist
    if(boundingRect!=null)
    {  
      boundingRect.width=boundingRect.width+16;
//      boundingRect.height+=2;
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
      ((Form)context.getForm()).setFocus(this, Integer.parseInt(value));
    }
    if(event.equals(EVENT_DELETE))
    {  
      removeBackfill(context);
      ((Form)context.getForm()).setFocus(this,0);
      resetValue="";
      if(getDataField().isDiverse())
      {
        IDataTableInternal table = (IDataTableInternal) context.getDataAccessor().getTable(getDataField().getTableAlias());
        // Mitteilen, dass die Änderung bewust vorgenommen worden ist. nicht dass diese wieder weggenommen wird.
        changeCount = table.getChangeCount();
      }
      setValue(resetValue);
    }
    else if(event.equals(EVENT_SHOW))
      tryBackfill(context,1,true, false);
    else if(event.equals(EVENT_SEARCH))
      tryBackfill(context,Property.BROWSER_COMMON_MAX_RECORDS.getIntValue(),true, false);
    else if(event.equals(EVENT_MAXSEARCH))
      tryBackfill(context,Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue(),true, true);
    else
    	logger.warn("Unknown event ["+event+"] for GuiElement ["+getClass().getName()+"]");
    return true;
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
    // Der alte Stand war "DIVERSE". Dieser wurde eben gelöscht und
    // die hinterlegten fkey's müssen entfernt werden
    //
    if(getDataField().isDiverse())
      clearInternalKey();
    
    getDataField().setValue(value); 
  }

  
  /**
   * This event occours if the ForeignField displays an record and the user delete one character in
   * input field. 
   * The ForeignField switch now form SELECTED to SEARCH mode and delesct the record in the data table.
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
    
    resetValue =getValue();

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
  public void tryBackfill(IClientContext context, int maxDisplayRecords, boolean displaySelectionDialog, boolean countRecordsAsWell) throws Exception
  {
    IForeignFieldEventHandler handler =(IForeignFieldEventHandler)getEventHandler(context);
   
    // The user has been clicked on an foreign field. Prepare the dialog and render them
    // on the client screen
    //
    IDataAccessor accessor = context.getDataAccessor();
    IDataBrowserInternal dataBrowser = (IDataBrowserInternal) accessor.getBrowser(definition.getBrowserToUse());
    
    // Falls das ForeignField bereits ein Record anzeigt, wird nur dieser in dem BrowserPopup
    // dargestellt
    //
    IDataTableRecord record =getSelectedRecord(context);
    if((getDataStatus()==UPDATE||getDataStatus()==SELECTED||getDataStatus()==NEW) && displaySelectionDialog==true && record!=null)
    {  
      dataBrowser.clear();                    // alle fremden records rausschmeissen
      dataBrowser.add(record, this.definition.getRelationSet());   // den Record den ich gerade anzeigen einfï¿½gen..
      dataBrowser.setSelectedRecordIndex(0);  // ... und selektieren
      ((Application)context.getApplication()).setForeignFieldBrowser(new ForeignFieldBrowser(getApplication(), definition.getBrowserToUse(),this,context, dataBrowser));
      return;
    }
    
    
    // abort if the 'custom code' want cancel the search action
    // or add additional QBE Expressions in the beforeSearch Hook
    accessor.qbeClearAll();
    if(handler!=null && handler.beforeSearch(context,this, dataBrowser)==false)
      return;
    
    performSearch(context,accessor, dataBrowser, maxDisplayRecords, countRecordsAsWell);
    
    switch(dataBrowser.recordCount())
    {
    case 1:
      // we found the one and only element. No ForeignFieldBrowser is neccessary.
      // Transfer the data direct to the foreignField.
      //
      dataBrowser.setSelectedRecordIndex(0);
      dataBrowser.propagateSelections();
      break;
    case 0:
      // no entry found. Show the user a feedback
      //
      if(displaySelectionDialog)
        context.createMessageDialog(new CoreMessage("NO_FOREIGNFIELD_ENTRY_FOUND")).show();
      break;
    default:
      // more than one found. Display the selection dialog.
      //
      if(displaySelectionDialog)
        ((Application)context.getApplication()).setForeignFieldBrowser(new ForeignFieldBrowser(getApplication(), definition.getBrowserToUse(),this,context, dataBrowser));
    }    
  }
  
  /**
   * 
   * @param accessor
   */
  public void setQbeValues(IClientContext context, IDataAccessor accessor) throws Exception
  {
    // 1. Feld ist zurückgefüllt
    if(isBackfilled())
    {
      accessor.getTable(getFromTable()).qbeSetPrimaryKeyValue(this.foreignKeyValue);
    }
    else
    {
      ((IDataTableInternal) accessor.getTable(getDataField().getTableAlias())).qbeSetValue(getDataField().getField(), getDataField().getValue(), context.getLocale());
    }
  }
  
  /**
   * 
   * @param context
   */
  protected void performSearch(IClientContext context, IDataAccessor accessor, IDataBrowser dataBrowser, int maxDisplayRecords, boolean countRecordsAsWell) throws Exception
  {
    // Falls es eine 'locale' suche ist, dann interessieren andere Felder bei der suche 
    // nicht. Es wird bei dem ForeignField nur das eigentliche Eingabefeld beachtet.
    //
    if(this.definition.getRelationSet().isLocal())
    {  
      ((IDataTableInternal) accessor.getTable(getDataField().getTableAlias())).qbeSetValue(getDataField().getField(), getDataField().getValue(), context.getLocale());
    }
    else
    {  
      // Es müssen jetzt die Datafields der gesamten Application, der Domain oder nur
      // der aktuellen Form ermittelt werden. Dies ist in Abhängigkeit der DataAccessor Scope
      // [application, domain, form]
      DataField[] fields;
      DataScope dataScope = getDataScope();
      if (dataScope == DataScope.APPLICATION)
        fields = ((Application) context.getApplication()).getDataFields();
      else if (dataScope == DataScope.DOMAIN)
        fields = ((Domain) context.getDomain()).getDataFields();
      else if (dataScope == DataScope.FORM)
        fields = ((Form) context.getForm()).getDataFields();
      else
        // default behaviour in the case of corrupt Property configuration
        fields = ((Domain) context.getDomain()).getDataFields();
      
      for (int i = 0; i < fields.length; i++)
      {
        DataField field = fields[i];
        if(field.getParent() instanceof HTTPForeignField)
        {
          HTTPForeignField ffield = (HTTPForeignField)field.getParent(); 
          ffield.setQbeValues(context, accessor);
        }
        else
        {
          if(field.getField() != null && field.getField().getType().isConstrainable())
          {
            ((IDataTableInternal) accessor.getTable(field.getTableAlias())).qbeSetValue(field.getField(), field.getValue(), context.getLocale());
          }
        }
      }
    }
    if (countRecordsAsWell)
    	((IDataBrowserInternal) dataBrowser).performRecordCount();
    dataBrowser.setMaxRecords(maxDisplayRecords);
    dataBrowser.search(this.definition.getRelationSet());
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
    // wird intern ein resetCache() ausgelässt da sich das Element neu zeichnen muss. Dummerweise löscht dies immer die errorDecoration.
    // Diese muß leider hier "rübergerettet" werden.
    FastStringWriter helper = this.lastErrorMessage;
  	IDataTableRecord currentDataRecord = getSelectedRecord(context);
  	this.lastErrorMessage = helper;
    
  	if(!isVisible())
      return;
    
    super.calculateHTML(context);

    if(getCache()==null)
    {
    	String eventGUID = getEtrHashCode();
    	Writer w = newCache();
      // the text field
      //
    	w.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;margin:0px;");
    	getCSSStyle(context, w, boundingRect);
    	w.write("\" id=container_");
      w.write(eventGUID);
      w.write("><tr><td><input onFocus=\"onFocusElement(this,'");
      w.write(getName());
      w.write("');\" style=\"");
      // nur bei absoluten Angaben soll sich das GUI-Feld auf die maximale Größe ziehen
      if (isAbsolute())
        w.write("width:100%;");
      w.write("height:97%\" type=\"text\"");
      w.write(" id=\"");
      w.write(eventGUID);
      w.write("\" ");
      if(getTabIndex()>=0)
      {
      	w.write(" TABINDEX=\"");
      	w.write(Integer.toString(getTabIndex()));
      	w.write("\"");
      }
       
      if (currentDataRecord != null)
      {
        // Note: Das Flag backfilled darf nur gesetzt werden, wenn das FF
        // auch editierbar ist, da ansonsten in der JS function setFocus() ein
        // JavaScript Fehler unter dem IE ausgelöst werden kann.
        // w.write(" state=\"backfilled\"");
        if (isEditable())
        {
          w.write(" state=\"backfilled\"");
          w.write(" onKeyDown=\"this.valueBeforeKeyDown=this.value\" onKeyUp=\"if(this.valueBeforeKeyDown && this.valueBeforeKeyDown!=this.value)FireEventData('");
          w.write(Integer.toString(getId()));
          w.write("', '" + EVENT_DESELECT + "',getCaretPosition(this))\"");
        }
      }
      else
      {
        w.write(" state=\"empty\"");
        if (parent.getDataStatus() != SELECTED)
        {
          w.write(" onKeyDown=\"if(((window.event)?window.event.keyCode:event.which)==");
          w.write(Property.SHORTCUT_FOREIGNFIELD.getValue());
          w.write(")FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','" + EVENT_SEARCH + "')\" ");
        }
      }
      w.write(" name=\"");
      w.write(eventGUID);
      w.write("\"");

      if(isEditable() && isEnabled())
        w.write(" class=\"foreign_normal editable_inputfield\"");
      else if((!isEditable() && isEnabled()))
        w.write(" readonly class=\"foreign_normal\"");
      else if(!isEnabled()) 
        w.write(" readonly class=\"foreign_disabled\"");
      else
        w.write(" class=\"text_normal\" ");
      w.write(" value=\"");
      w.write(StringUtil.htmlEncode(getValue()));
      w.write("\"></td><td style=\"width: 16px\" >");

      // Es wird nur ein klickbares Icon rausgeschrieben wenn das Element auch enabled ist
      //
      if(isEnabled())
      {
        // UPDATE-Modus mit einem zurückgefüllten Record im FF => DELETE Action
        //
        if(getDataField().isDiverse() || (currentDataRecord!=null && parent.getDataStatus()==UPDATE  && isEditable() ))
        {
          w.write("<a name=\"");
          w.write(getName());
          w.write("\" id=\"");
          w.write(getName());
          w.write("\" class=\"foreign_normal\" ");
          w.write(" href=\"#\" onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','"+EVENT_DELETE+"')\" ");
          w.write("><img title=\"");
          w.write(I18N.getCoreLocalized("BUTTON_COMMON_DELETE", context));
          w.write("\" name=\"img_");
          w.write(Integer.toString(getId()));
          w.write("\" border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_delete.png"));
          w.write("\"></a>\n");
        }
        // NEW-Modus mit einem zurückgefüllten Record im FF => DELETE Action
        //
        else if(currentDataRecord!=null && parent.getDataStatus()==NEW  && isEditable())
        {
          w.write("<a name=\"");
          w.write(getName());
          w.write("\" id=\"");
          w.write(getName());
          w.write("\" class=\"foreign_normal\" ");
          w.write(" href=\"#\" onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','"+EVENT_DELETE+"')\" ");
          w.write("><img title=\"");
          w.write(I18N.getCoreLocalized("BUTTON_COMMON_DELETE", context));
          w.write("\" name=\"img_");
          w.write(Integer.toString(getId()));
          w.write("\" border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_delete.png"));
          w.write("\"></a>\n");
        }
        // SEARCH-Modus mit einem zurückgefüllten Record im FF => DELETE Action
        //
        else if(currentDataRecord!=null && parent.getDataStatus()==SEARCH  && isEditable())
        {
          w.write("<a name=\"");
          w.write(getName());
          w.write("\" id=\"");
          w.write(getName());
          w.write("\" class=\"foreign_normal\" ");
          w.write(" href=\"#\" onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','"+EVENT_DELETE+"')\" ");
          w.write("><img title=\"");
          w.write(I18N.getCoreLocalized("BUTTON_COMMON_DELETE", context));
          w.write("\" name=\"img_");
          w.write(Integer.toString(getId()));
          w.write("\" border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_delete.png"));
          w.write("\"></a>\n");
        }
        // Ansonsten ist SUCHEN erlaubt wenn das Element "enabled" ist.
        //
        else if(!(parent.getDataStatus()==SELECTED && currentDataRecord==null) && isEditable()==true)
        {
          w.write("<a name=\"");
          w.write(getName());
          w.write("\" id=\"");
          w.write(getName());
          w.write("\" class=\"foreign_normal\" ");
  	      w.write(" href=\"#\" onClick=\"if(isCtrlKeyPressed(event))");
  	      w.write(" FireEvent('");
  	      w.write(Integer.toString(getId()));
  	      w.write("','"+EVENT_MAXSEARCH+"'); ");
  	      w.write(" else FireEvent('");
  	      w.write(Integer.toString(getId()));
  	      w.write("','"+EVENT_SEARCH+"')\" ");
          w.write("><img title=\"");
          w.write(I18N.getCoreLocalized("TOOLTIP_SEARCH_DATARECORDS", context));
          w.write("\" name=\"img_");
          w.write(Integer.toString(getId()));
          w.write("\" border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_search.png"));
          w.write("\"></a>\n");
        }
        // ansonsten hat man die Möglichkeit sich den die Details des Records anzuzeigen.
        //
        else if(currentDataRecord!=null)
        {
          w.write("<a name=\"");
          w.write(getName());
          w.write("\" id=\"");
          w.write(getName());
          w.write("\" class=\"foreign_normal\" ");
          w.write(" href=\"#\" onClick=\"FireEvent('");
          w.write(Integer.toString(getId()));
          w.write("','"+EVENT_SHOW+"')\" ");
          w.write("><img title=\"");
          w.write(I18N.getCoreLocalized("TOOLTIP_SHOW_DETAILS", context));
          w.write("\" name=\"img_");
          w.write(Integer.toString(getId()));
          w.write("\" border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_selected.png"));
          w.write("\"></a>\n");
        }
        // Kein Record ==> keine Details
        else
        {
          w.write("<img border=\"0\" src=\"");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_disabled.png"));
          w.write("\">\n");
        }
      }
      // disabled
      else
      {
        w.write("<img border=\"0\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("foreingField_disabled.png"));
        w.write("\">\n");
      }
      w.write("</td></tr></table>\n");
      
    }
  }

  /**
   * Returns the record to display for this foreign field
   * 
   * @deprecated use {@link #getSelectedRecord(IClientContext)} instead
   * @param context
   * @return
   * @throws Exception
   * 
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
      // dem Element gleich übergeben.
      if(getDisplayRecordIndex()==-1)
      {
        setValue(context, currentDataRecord);
      }
      // ...ansonsten wird die ganze Tabelle übergeben und das Element kann sich den Record selber
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
      setValue(resetValue); 
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
      resetValue="";
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
    if(getDataField().isDiverse())
      context.addOnLoadJavascript("initBehaviourDiverse('"+getEtrHashCode()+"');");
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
    // Enthaelt die Felder welche an der Gruppe zu setzten sind.
    // Die Reihenfolge von getprimarykeyvalue ist mit der toforeignkey identisch
    // und zu setzen.
    //
    IDataKeyValue key = null;
    if (record != null && (key = record.getPrimaryKeyValue()) != null)
    {
      // Nur wenn der selektierte Record sich wirklich geändert hat ein onSelect
      // auslösen und
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
      IForeignFieldEventHandler eh = (IForeignFieldEventHandler) this.getEventHandler(context);
      if (null != eh && isBackfilled() == true)
        eh.onDeselect(context, this);
      clearInternalKey();
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
    // if the group in SELECTED modus the foreign field
    // is not editable
    //
    if(parent.getDataStatus()==SELECTED)
      return false;
    
    // Falls der Record zurückgefüllt ist und die Grupp nicht im
    // selected Status ist, dann ist das Feld auf jedenfall editierbar.
    //
    if(parent.getDataStatus()==SEARCH)
      return true;

    return super.isEditable();
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
      return ForeignField.this.getParent().getDataStatus();
    }
  }

}
