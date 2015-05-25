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
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.collections.map.ListOrderedMap;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IBrowserRecordList;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.IDataBrowserSortStrategy;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserCellRenderer;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IDragDropListener;
import de.tif.jacob.screen.event.ISelectionListener;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.HTTPBrowser;
import de.tif.jacob.screen.impl.TwoPhaseSelectionAction;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz
 *
 */
public abstract class Browser extends GuiHtmlElement implements HTTPBrowser
{
  static public final transient String RCS_ID = "$Id: Browser.java,v 1.104 2011/02/15 08:13:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.104 $";

  IDataBrowserInternal data = null;
  protected long changeCount  = -1;
  protected final IBrowserDefinition definition;
  protected long currentScrollPosX = 0;
  protected long currentScrollPosY = 0;
  protected boolean sortable = true;
  protected boolean enableTreeView = false;
  
  protected boolean[] isColumnConfigurable;
  protected boolean[] isColumnVisible;
  
  protected String width="100%";
  
  private String caption = "";
  
  private final Map<String, BrowserAction> headerActions     = new HashMap<String, BrowserAction>();
  private final Map<String, BrowserAction> singleRowActions  = new HashMap<String, BrowserAction>();
  private final Map<String, BrowserAction> hiddenActions     = new HashMap<String, BrowserAction>();
  private final ListOrderedMap selectionActions  = new ListOrderedMap();

  private  Map<IDataBrowserRecord, List> record2ChildrenArray = new HashMap<IDataBrowserRecord, List>(); // Map<IDataBrowserRecord,ArrayList<IDataBrowserRecord>> 
  private  Map<IDataBrowserRecord, IDataTableRecord> browserRecord2tableRecord = new HashMap<IDataBrowserRecord, IDataTableRecord>();
  protected Map<IDataRecord, Marker> record2marker = new HashMap<IDataRecord, Marker>();
  protected Map<String, IBrowserCellRenderer> column2cellRenderer = new HashMap<String, IBrowserCellRenderer>(); // Map<String, IBrowserCellRenderer>
  
  protected ISelectionAction lastSelectionEvent;
  protected TwoPhaseSelectionAction runningSelectionEvent;
  
  // Default Hook falls der Browser ein connectByKey hat aber kein Hook definiert hat.
  private final IBrowserEventHandler DEFAULT_HOOK = new DefaultConnectByKeyDragDropBrowserEventHandler();
  
  public static class DefaultConnectByKeyDragDropBrowserEventHandler extends IBrowserEventHandler implements IDragDropListener 
  {
    public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
    {
    }

    public void drop(IClientContext context, IGuiElement dropElement, Object dragObject, Object dropObject) throws Exception
    {
      Browser browser = (Browser)dropElement;
      
      IKey connectByKey = browser.getConnectByKey();
      if(connectByKey==null)
        return;
      
      // Eine Suche mit allen Key-Parameter auf der betroffenen Tabelle absetzen
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      IDataTransaction trans = acc.newTransaction();
      try
      {
        IDataBrowserRecord dragBrowserRecord = (IDataBrowserRecord)dragObject;
        IDataBrowserRecord dropBrowserRecord = (IDataBrowserRecord)dropObject;
  
        IDataTableRecord dragTableRecord = dragBrowserRecord.getTableRecord();
        
        // Zeile wurde auf eine leere Stelle im Browser fallen gelassen
        //
        if(dropBrowserRecord==null)
        {
          for (ITableField foreignField : connectByKey.getTableFields())
          {
            dragTableRecord.setValue(trans,foreignField, null);
          }
        }
        else
        {
          IDataBrowserRecord parent = browser.getParent( dropBrowserRecord);
          while(parent!=null)
          {
            if(parent.getPrimaryKeyValue().equals(dragBrowserRecord.getPrimaryKeyValue()))
              return;
            parent = browser.getParent(parent);
          }
        
          IDataTableRecord dropTableRecord = dropBrowserRecord.getTableRecord();
          IDataTable table = acc.getTable(dragTableRecord.getTableAlias().getName());
          IKey primaryKey = table.getTableAlias().getTableDefinition().getPrimaryKey();
          Iterator foreignIter = connectByKey.getTableFields().iterator();
          Iterator primaryIter = primaryKey.getTableFields().iterator();
          while (foreignIter.hasNext())
          {
            ITableField foreignField = (ITableField) foreignIter.next();
            ITableField primaryField = (ITableField) primaryIter.next();
      
            dragTableRecord.setValue(trans,foreignField, dropTableRecord.getValue(primaryField.getFieldIndex()));
          }
        }
        trans.commit();
        browser.refresh(context,dragTableRecord);
      }
      finally
      {
        trans.close();
      }
    }

    public Icon getFeedback(Context context, IGuiElement dropElement,boolean canDrop, Object dragObject, Object hoverObject) throws Exception
    {
      if(canDrop)
        return Icon.accept;
      return Icon.delete;
    }

    public boolean validateDrop(Context context, IGuiElement dropElement, Object dragObject, Object targetObject) throws Exception
    {
      return true;
    }
  };
  
  // damit der Browser nicht so leer aussieht
  protected abstract int getEmptyRowCount();
  
	/**
   * This constructor will be called from the initial castor call.
   * 
   * @param browser the browser definition
   * @param boundingRect the bounding rectangle
	 */
	public Browser(IApplication app, IBrowserDefinition browser, Rectangle boundingRect)
	{
    this(app, browser.getName(), "", browser, boundingRect);
    
    this.enableTreeView = getConnectByKey()!=null;
    // nur ein Browser welcher kein Baum ist ist sortierbar
    //
    sortable = getConnectByKey()==null || this.enableTreeView==false;
    
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.IBrowser#getDefinition()
   */
  public final IBrowserDefinition getDefinition()
  {
    return this.definition;
  }

  public IKey getConnectByKey()
  {
    return definition.getConnectByKey();
  }
  
  /**
   * 
   * @param name the GUI element name
   * @param browser the browser definition
   * @param boundingRect the bounding rectangle
   */
  public Browser(IApplication app, String name, String caption, IBrowserDefinition browser, Rectangle boundingRect)
	{
		super(app, name, browser.getName(), true, boundingRect, browser.getProperties());

    this.definition= browser;
    this.caption = caption;

    this.enableTreeView = getConnectByKey()!=null;
    // nur ein Browser welcher kein Baum ist ist sortierbar
    //
    sortable = getConnectByKey()==null || this.enableTreeView==false;
    
    initHeaderActions();
	}
	
  protected void initHeaderActions()
  {
    addHiddenAction(BrowserAction.ACTION_EXPAND_ROW);
    addHiddenAction(BrowserAction.ACTION_COLLAPSE_ROW);
    addHiddenAction(BrowserAction.ACTION_DRAG_DROP);        // DragDrop Event handler for the TreeBrowser
    addHiddenAction(BrowserAction.ACTION_SELECT_ALL); // The user clicks in a row. This is not a action with a icon
    addHiddenAction(BrowserAction.ACTION_TOGGLE_SELECTION); // The user clicks in a row. This is not a action with a icon
    addHiddenAction(BrowserAction.ACTION_CLICK);           // The user clicks in a row. This is not a action with a icon
    addHiddenAction(BrowserAction.ACTION_COLUMN_PICKER);    // show/Hide columns f the browser
    addHeaderAction(BrowserAction.ACTION_EXCEL);           // The user wants an excel export. Action with icon -> is in the top of the browser
	}
	
	/**
	 * Adds a action object to the browser which will display in the header bar of the browser.<br>
	 * <br>
	 * Possible implementations for Actions are Refresh, UnlimitedSearch, ExportToExcel,RowClick....<br>
	 * 
	 * @param action The action to add to the browser.
	 */
	protected final void addHeaderAction(BrowserAction action)
	{
	  headerActions.put(action.getId(),action);
	}
	
	protected final void removeHeaderAction(BrowserAction action)
	{
	  headerActions.remove(action.getId());
	}
	
  protected ISelectionAction getSelectionAction(String id)
  {
    return (ISelectionAction)selectionActions.get(id);
  }

  /**
   * Returns the SelectionActions of the Browser.<br>
   * Used from JSP files and UI-Plugins too.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public Collection<ISelectionAction> getSelectionActions()
  {
    return selectionActions.valueList();
  }
  
	public void addSelectionAction(ISelectionAction action)
  {
    if (this.lastSelectionEvent==null)
    {
      this.lastSelectionEvent = action;
    }
    selectionActions.put(action.getId(), action);
    
    // Combobox fï¿½r die Action kann Teil von dem Cache sein => Cache lï¿½schen
    //
    resetCache();
  }

  
  public void removeSelectionAction(ISelectionAction action)
  {
    selectionActions.remove(action.getId());

    // Die default action wurde aus dem Browser entfernt.
    //
    if(this.lastSelectionEvent == action)
       this.lastSelectionEvent = null;
    
    if(this.runningSelectionEvent == action)
      this.runningSelectionEvent = null;

    // Combobox fï¿½r die Action kann Teil von dem Cache sein => Cache lï¿½schen
    //
    resetCache();
  }

  
  public void removeAllSelectionActions()
  {
    this.lastSelectionEvent = null;

    selectionActions.clear();
    resetCache();
  }
  
  /**
	 * 
	 * @param action The action to add to the browser.
	 */
	public final void addHiddenAction(BrowserAction action)
	{
	  hiddenActions.put(action.getId(),action);
	}
	
	public final void removeHiddenAction(BrowserAction action)
	{
	  hiddenActions.remove(action.getId());
	}

	/**
	 * Adds a action object to the browser which will display at the left hand side of a row. The exceute
	 * method of the action will be get the clicked index of the row.<br>
	 * <br>
	 * Possible implementations for Actions are Delete, new , update....<br>
	 * 
	 * @param action The action to add to the browser.
	 */
	public final void addRowAction(BrowserAction action)
	{
	  singleRowActions.put(action.getId(),action);
	}

	public final void removeRowAction(BrowserAction action)
	{
	  singleRowActions.remove(action.getId());
	}

  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId())
    {
      String[] values = data.split(":");
      scrollTo(Long.parseLong(values[0]), Long.parseLong(values[1]));
      return true;
    }
    // the super-implementation trys to find the corresponding child
    // and sends the event to them
    return super.processParameter( guid, data); 
  }
	
	/**
	 * Proceed the action. An action can be added with <code>addAction(BrowserAction action)</code><br>
	 * <br>
	 * 
	 */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // This is not an event for this guiBrowser
    // and a generic guiBrowser has no childs -> return with no action
    //
    if(guid!=this.getId())
      return false;
    
    // The browser has fired an hidden event
    //
    BrowserAction action=hiddenActions.get(event);
    if(action!=null)
    {  
      action.execute(context,this,value);
      return true;
    }

    // The user has clicked in the header
    //
    action=headerActions.get(event);
    if(action!=null)
    {  
      action.execute(context,this,value);
      return true;
    }
    
    // The user has clicked in the row
    //
    action=singleRowActions.get(event);
    if(action!=null)
    {  
      action.execute(context,this,value);
      return true;
    }    

    // Selektion Event wurde gefeuert.
    //
    ISelectionAction sa=(ISelectionAction)selectionActions.get(event);
    if(sa!=null)
    {
      // reset the old error decorations if the user triggers a new action
      //
      resetErrorDecoration(context);
      resetWarningDecoration(context);
      
      if(sa instanceof TwoPhaseSelectionAction)
      {
        TwoPhaseSelectionAction tp = (TwoPhaseSelectionAction)sa;
        if(tp == this.runningSelectionEvent)
        {
          if(TwoPhaseSelectionAction.SAVE.equals(value))
            tp.execute(context,this,this.getSelection());
          else
            tp.cancel(context,this);
          if(this.runningSelectionEvent.isDone())
            this.runningSelectionEvent=null;
        }
        else
        {
          try
          {
          if(this.runningSelectionEvent!=null)
            this.runningSelectionEvent.cancel(context, this);
          }
          catch(Exception exc)
          {
            // ignore silently.
          }
          
          try
          {
            tp.prepare(context, this,this.getSelection());
            this.runningSelectionEvent = tp;
          }
          catch(Exception exc)
          {
            ExceptionHandler.handleSmart(context, exc);
          }
        }
      }
      else
      {
        try
        {
          sa.execute(context,this,this.getSelection());
        }
        catch(Exception exc)
        {
          ExceptionHandler.handleSmart(context, exc);
        }
      }
      this.lastSelectionEvent = sa;
      return true;
    }    

    return true;
  }

  /**
   * Select the hands over index in the current dataBrowser.<br>
   * This enforce a <code>propagateSelection</code> on the DataBrowser.<br>
   * <br> 
   * The selection will be reseted if you hands over <code>-1>/code><br>
   * 
   * @param index The zero based index to select or -1 to unselect.
   */
  public void setSelectedRecordIndex(IClientContext context, int index) throws Exception
  {
    if((index!=getDataInternal().getGuiSortStrategy().getSelectedRecordIndex()))
    {
      resetCache();
      if(index==-1)
      {  
        getData().clearSelections();
      }
      else
      {  
        getDataInternal().getGuiSortStrategy().setSelectedRecordIndex(index);
        getDataInternal().propagateSelections();
      }

      // Notify the eventhandler about the new selection.
      //
      IBrowserEventHandler browserFilter=(IBrowserEventHandler)getEventHandler(context);
      if(browserFilter!=null && index!=-1)
        browserFilter.onRecordSelect(context,this,getDataRecord(index).getTableRecord());
    }
  }
  
  
  public void setSelectedRecord(IClientContext context, IDataTableRecord record) throws Exception
  {
    setSelectedRecordIndex(context,getRecordIndex(context,record));
  }
  

  public void setSelectedRecord(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    setSelectedRecordIndex(context,getRecordIndex(context,record));
  }

  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception
  {
    int index = getDataInternal().getGuiSortStrategy().getSelectedRecordIndex();
    if(index<0)
      return null;
    IDataBrowserRecord record = getDataRecord(index);
    return record!=null?record.getTableRecord():null;
  }
  
  
  public IDataTableRecord newRecord(IClientContext context, IDataTransaction trans) throws Exception
  {
    IDataTableRecord newRecord = context.getDataTable(getDefinition().getTableAlias().getName()).newRecord(trans);
    this.add(context,newRecord);
    return newRecord;
  }

  public void refresh(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    if(record==null)
      return;
    refresh(context,getTableRecord(record));
  }

  /**
   * Prï¿½fen ob sich die Vater-Kind Beziehung eines Records geÃ¤nder hat und diesen dann notfalls
   * in dem Browser neu einsortieren.
   */
  public void refresh(IClientContext context, IDataTableRecord record) throws Exception
  {
    if(record==null)
      return;
 
    int currentIndex = getRecordIndex(context,record);
    if(currentIndex == -1)
      return;

    // get the old error decoration entry
    IDataBrowserRecord browserRecord = getDataRecord(currentIndex);
     Marker oldMarker = record2marker.remove(browserRecord);

     // Den Record erstmal frisch aus der Datenbank holen
     //
    boolean remove = getDataInternal().removeRecord(browserRecord);
    record = context.getDataAccessor().newAccessor().getTable(record.getTableAlias().getName()).loadRecord(record.getPrimaryKeyValue());
    ((DataBrowser)getDataInternal()).add(currentIndex,record);
    
    // Restore the error Decoration with the reloaded record
    if(oldMarker!=null)
      record2marker.put(getDataRecord(currentIndex), oldMarker);
      
    GuiEventHandler obj = getEventHandler(context);
    if(obj instanceof IBrowserEventHandler)
    {
      browserRecord = getDataRecord(currentIndex);
      IDataBrowserRecord oldParent     = getParent(browserRecord);
      IBrowserEventHandler handler = (IBrowserEventHandler)obj;
      IDataTableRecord parent = handler.getParent(context,this,record);
      
      // Der Record hatte vorher keinen Vater und hat nachher keinen Vater.
      //
      if(oldParent==null && parent==null)
        return;
      
      // der Vater hat sich nicht geÃ¤ndert
      //
      if(oldParent!=null && parent!=null && oldParent.getPrimaryKeyValue().equals(parent.getPrimaryKeyValue()))
        return;
      
      // Der Vater eines Rekords hat sich geÃ¤ndert. Record aus dem Browser entfernen 
      // und unterhalb des neuen Vaters einsortieren.
      //
      this.remove(context,record);
      this.add(context,record);
    }
  }

  public IDataBrowserRecord getSelectedBrowserRecord(IClientContext context) throws Exception
  {
    int index = getDataInternal().getGuiSortStrategy().getSelectedRecordIndex();
    if(index<0)
      return null;
    return getDataRecord(index);
  }

  /**
   * Returns the Record index. This method considered the current user sort order.
   * 
   * @param context
   * @param record
   * @return
   */
  public int getRecordIndex(IClientContext context, IDataRecord record)
  {
    int recordCount = getData().recordCount();
    for(int i=0;i<recordCount;i++)
    {
      IDataRecord browserRecord = getDataInternal().getGuiSortStrategy().getRecord(i);
      if(browserRecord.getId().equals(record.getId()))
        return i;
    }
    return -1;
  }
  
  public void resetSelectedRecord(IClientContext context) throws Exception
  {
    setSelectedRecordIndex(context, -1);
  }
  
  public final IDataBrowserRecord getDataRecord(int rowIndex) throws IndexOutOfBoundsException
  {
    return getDataInternal().getGuiSortStrategy().getRecord(rowIndex);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer)
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    super.calculateHTML(context);

    // the last search result has been changed -> update the view
    //
    IDataBrowserInternal dataBrowser = getDataInternal();
    if(dataBrowser.getChangeCount()!= changeCount)
    {
      resetCache();
    	changeCount=dataBrowser.getChangeCount();
      
    	// if there is only one record in the browser and the record already propagated..
    	if (dataBrowser.recordCount()==1 && dataBrowser.isAlreadyPropagated(0))
    	{
    		// mark the record as selected (i.e. propagated)
        scrollTo(0,0);
    	}
    	
    }
    
    if(getCache()==null )
    {
      if(isColumnConfigurable==null)
        calculateVisibility(context);
      
      IBrowserEventHandler handler = (IBrowserEventHandler)getEventHandler(context);
      // application callback for modified data
      try
      {
        if(handler!=null)
          handler.onDataChanged(context,this, dataBrowser);
      }
      catch(Exception exc)
      {
        ExceptionHandler.handleSmart(context,exc);
      }
      
      int recordCount = dataBrowser.recordCount();
      
      int columnssize = definition.getFieldNumber();

      // estimate the prospective data size for the output buffer an allocate them
      //
      Writer w = newCache((columnssize*128 + 128)*recordCount+512);

      int selectedRecordIndex = dataBrowser.getGuiSortStrategy().getSelectedRecordIndex();
      String hashCode = Integer.toString(getId());

      boolean dragDropSupport = (getEventHandler(context) instanceof IDragDropListener);
      boolean multiselect = hasMultiselect(context);
      w.write("<table dragdropsupport=\""+dragDropSupport+"\" multiselect=\""+multiselect+"\" cellspacing=\"0\" cellpadding=\"0\" id='"+hashCode+"'>");
      w.write("<caption class=\"sbh_counter\"><table class=\"sbh_counter\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" ><tr><td style=\"text-align:left\">");
      writeCaptionContent(context,w);
      w.write("</td><td>");
      if (dataBrowser.isSearchPerformed())
      {
        // Note: Only show record count, if a search has really been performed. This makes it easy for the user
        // to distinct between no search or a search with 0 hits.
        w.write(I18N.getCoreLocalized("LABEL_COMMON_HIT", context));
        w.write("<b>"+recordCount);
        if (dataBrowser.hasMoreRecords())
        { 
          long realCount = dataBrowser.getRealRecordCount();
          if (realCount == -1)
            w.write("/?");
          else
            w.write("/"+realCount);
        }
        
        w.write("</b>");
      }
      w.write("&nbsp;");
      
      Iterator<BrowserAction> actionIter =headerActions.values().iterator();
      while(actionIter.hasNext())
      {
        BrowserAction action =actionIter.next();
        if(action.isEnabled(context, this))
        {
          renderAction(context,w,action.getId(), "",action.getIcon(),action.getIcon(context), action.getTooltip(context),action.reloadPage());
        }
      }
      w.write("</td></tr></table></caption>");

      /////////////////////////////////////////
      // HEADER der Tabelle rausschreiben
      /////////////////////////////////////////
      w.write("<thead>");
      w.write("<tr>");
      // Column Platzhalter fuer die SelectionCheckbox mit rausschreiben
      if(multiselect)
      {
        w.write("<th class=\"sbh_checkbox\">");

        Iterator<Marker> iter = record2marker.values().iterator();
        boolean allChecked=record2marker.size()==recordCount;
        while(allChecked && iter.hasNext())
          allChecked = allChecked && iter.next().checked;
        
        if(this.runningSelectionEvent!=null)
        {
          w.write("<img checked='false' style='filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity:0.3;' src='");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
          w.write("'>");
        }
        else if(allChecked && recordCount>0)
        {
          w.write("<img checked='true' src='");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("checked.png"));
          if(handler instanceof ISelectionListener)
            w.write("' onclick='FireEventData(\""+Integer.toString(getId())+"\",\""+BrowserAction.ACTION_SELECT_ALL.getId()+"\",\"false\")' ");
          else
            w.write("' onclick='return checkAll(event,\"");
          w.write(hashCode);
          w.write("\");'>");
        }
        else
        {
          w.write("<img checked='false' src='");
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
          if(handler instanceof ISelectionListener)
            w.write("' onclick='FireEventData(\""+Integer.toString(getId())+"\",\""+BrowserAction.ACTION_SELECT_ALL.getId()+"\",\"true\")' ");
          else
            w.write("' onclick='return checkAll(event,\"");
          w.write(hashCode);
          w.write("\");'>");
        }
        w.write("</th>");
      }

      // Column Platzhalter fuer die Zeilen ActionItems mit rausschreiben
      if(singleRowActions.size()>0)
        w.write("<th class=\"sbh\">&nbsp;</th>");
      
      for (int i = 0; i<columnssize;i++)
      {
        if(isColumnVisible(i))
        {
          IBrowserCellRenderer renderer = column2cellRenderer.get(definition.getBrowserField(i).getName());
          if(renderer==null)
            w.write("\t<th valign=\"middle\" class=\"sbh\" nowrap >");
          else
          {
            int width = renderer.getCellWidth(context);
            if(width>0)
              w.write("\t<th style=\"width:"+width+"px\" valign=\"middle\" class=\"sbh\" nowrap >");
          }
          w.write(getHeaderCellContent(context,i));
          w.write("</th>\n");
        }
      }
      
      w.write("<th class=\"sbh\">&nbsp;</th>");
      w.write("</tr>");
      w.write("</thead>");

      /////////////////////////////////////////
      // BODY der Tabelle rausschreiben
      /////////////////////////////////////////
      w.write("<tbody>");

      String rowClass = "sbr_e";
      boolean handleExceptions = true;
      int row=0;
      for(;row<recordCount;row++)
      {
        IDataBrowserRecord record = getDataRecord(row);
        Marker marker = record2marker.get(record);
        rowClass=getRowCssClass(record, row);
        w.write("<tr class=\"");
        w.write(rowClass);
        w.write("\" ");
        
        // Tooltip an die Zeile hï¿½ngen
        if(marker!=null)
        {
          if(marker.error!=null)
          {
            w.write("title=\"");
            w.write(StringUtil.htmlEncode(marker.error));
            w.write("\" ");
          }
          else if(marker.warning!=null)
          {
            w.write("title=\"");
            w.write(StringUtil.htmlEncode(marker.warning));
            w.write("\" ");
          }
        }
        
        if(row != selectedRecordIndex)
        {
          if(this.runningSelectionEvent==null)
          {
            w.write(" onMouseOver=\"this.className='sbr_h'\" onMouseOut=\"this.className='");
            w.write(rowClass);
            w.write("'\"");
            w.write(" onClick=\"FireEventData('");
            w.write(hashCode);
            w.write("','"+BrowserAction.ACTION_CLICK.getId()+"','");
            w.write(Integer.toString(row));
            w.write("')\"");
          }
          else
          {
            w.write(" style=\"cursor:default;");
            if(marker==null || (marker.checked==false && marker.warning==null && marker.warning==null) )
              w.write("filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity: 0.3;");
            w.write("\"");
          }
        }
        else
        {
          String contextmenu = getContextMenuFunction(context);
          if(contextmenu!=null && contextmenu.length()>0)
            w.write(" selected=\"true\" oncontextmenu=\""+contextmenu+"\"");
        }
        w.write(">");

        // Checkbox fuer das Multiselect rausschreiben.
        if(multiselect)
        {
          String action = "return check(event,\""+hashCode+"\")";
          // Falls der Browser das Interface ISelectionListener implementiert, dann muss
          // bei jedem Klick auf eine Checkbox ein Serverequest ausgelï¿½st werden und der 
          // Eventhandler muss dann gerufen werden.
          if(handler instanceof ISelectionListener)
            action = "FireEventData(\""+Integer.toString(getId())+"\",\""+BrowserAction.ACTION_TOGGLE_SELECTION.getId()+"\",\""+Integer.toString(row)+"\")";
                
          w.write("<td class=\"sbc_checkbox\">");
          if(marker!=null && marker.checked)
          {
            if(this.runningSelectionEvent==null)
            {
              w.write("<img selectionbox='true' checked='true' src='");
              w.write(((ClientSession)context.getSession()).getTheme().getImageURL("checked.png"));
              w.write("' onclick='");
              w.write(action);
              w.write("'>");
            }
            else
            {
              w.write("<img selectionbox='true' checked='true' style='filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity:0.3;' src='");
              w.write(((ClientSession)context.getSession()).getTheme().getImageURL("checked.png"));
              w.write("'>");
            }
          }
          else
          {
            if(this.runningSelectionEvent==null)
            {
              w.write("<img selectionbox='true' checked='false' src='");
              w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
              w.write("' onclick='");
              w.write(action);
              w.write("'>");
            }
            else
            {
              w.write("<img selectionbox='true' checked='false' style='filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity:0.3;' src='");
              w.write(((ClientSession)context.getSession()).getTheme().getImageURL("unchecked.png"));
              w.write("'>");
            }
          }
          w.write("</td>");
        }

        // Column Platzhalter fuer die Zeilen ActionItems mit rausschreiben
        if(singleRowActions.size()>0)
        {  
          w.write("<td class=\"sbc\">");
          w.write(getRowDecoration(context,row));
          w.write("</td>");
        }

        boolean firstVisibleColumn=true;
        for(int column=0; column<columnssize;column++)
        {
          if(isColumnVisible(column))
          {
            w.write("<td nowrap class=\"sbc\">");
            if (writeCellContent(context, w,handler, record,firstVisibleColumn, row, column, handleExceptions) == false)
            {
              // to avoid handling recurring errors being caused by application programmer errors in user hooks
              handleExceptions = false;
            }
            w.write("</td>");
            firstVisibleColumn=false;
          }
        }
        w.write("<td  class=\"sbc\" >&nbsp;</td>");
        w.write("</tr>\n");
      }

      w.write("<tr filler=\"true\" class=\"");
      w.write(getRowCssClass(null,row));
      w.write("\"  style=\"cursor:default;\" >\n");

      // Column Platzhalter fuer die SelectionCheckbox mit rausschreiben
      if(multiselect)
        w.write("<th class=\"sbh_checkbox\">&nbsp;</td>");

      // placeholder for single row action
      if(singleRowActions.size()>0)
        w.write("<td  class=\"sbc\">&nbsp;</td>\n");
      
      // write the data column
      // Falls eine Suche abgesetzt wurde und keine Records gefunden wurde, wird dem benutzter
      // ein Hinweistext dargestellt.
      //
      for(int column=0; column<columnssize;column++)
      {
        if(isColumnVisible(column))
        {
          w.write("<td class=\"sbc\">");
          writeFooterCellContent(context,w, column);
          w.write("</td>\n");
        }
      }
      // write the filler column
      w.write("<td  class=\"sbc\">&nbsp;</td>");
      w.write("</tr>\n");

      // Falls der Benutzer gesucht hat und keine Records gefunden
      // wurden, wird dem Benutzer dies mit einer Status Meldung mitgeteilt.
      //
      if(dataBrowser.isSearchPerformed() && recordCount==0 && showStatusMessage())
      {
        w.write("<td colspan=\""+(columnssize+1)+"\" class=\"sb_no_records\">");
        w.write(I18N.getCoreLocalized("MSG_NO_RECORDS_FOUND",context));
        w.write("</td>\n");
      }
      // ein paar Leerzeile gibt es immer....sieht einfach besser aus.
      //
      row++;
      for(;row<(recordCount+getEmptyRowCount());row++)
      {
        rowClass=getRowCssClass(null,row);
        w.write("<tr filler=\"true\" class=\"");
        w.write(rowClass);
        w.write("\" style=\"cursor:default;\" oncontextmenu=\"return false;\">");

        // Column Platzhalter fuer die SelectionCheckbox mit rausschreiben
        if(multiselect)
          w.write("<th class=\"sbh_checkbox\">&nbsp;</td>");

        // Column Platzhalter fuer die Zeilen ActionItems mit rausschreiben
        if(singleRowActions.size()>0)
          w.write("<td class=\"sbc\">&nbsp;</td>");
        
        for(int column=0; column<columnssize;column++)
        {
          if(isColumnVisible(column))
            w.write("<td nowrap class=\"sbc\">&nbsp;</td>");
        }
        w.write("<td  class=\"sbc\">&nbsp;</td>");
        w.write("</tr>\n");
      }
      w.write("</tbody>");
      w.write("</table>");
     }
  }

  
  public void ensureVisible(IClientContext context, IDataRecord record)
  {
    int rowIndex = getRecordIndex(context,record);
    if(rowIndex<0)
      return;
    String hashCode = Integer.toString(getId());
    ((ClientContext)context).addOnLoadJavascript("jACOBTable_scrollToRow('"+hashCode+"',"+rowIndex+")");
  }

  public void highlightRecord(IClientContext context, int rowIndex)
  {
    if(rowIndex<0 || rowIndex>=getData().recordCount())
      return;
    
    String hashCode = Integer.toString(getId());
//    ((ClientContext)context).addOnLoadJavascript(
//      "var t=$('"+hashCode+"');\n"+
//      "if(t!=null)\n{\n glow(t.rows["+rowIndex+"]);\n"+
//      "jACOBTable_scrollToRow('"+hashCode+"',"+rowIndex+");\n}\n");
    
    // as 7.10.2009: Problembehebung Aufgabe 623: if a new record is stored under IE, the browser shows an empty entry.
    // JavaScript jACOBTable_scrollToRow ist unter IE fehlerhaft und auch unnötig für das Einfügen eines neuen Records.
    ((ClientContext)context).addOnLoadJavascript(
      "var t=$('"+hashCode+"');\n"+
      "if(t!=null)\n{\n glow(t.rows["+rowIndex+"]);\n}\n");
  }

  public void highlightRecord(IClientContext context, IDataTableRecord record)
  {
    highlightRecord(context, getRecordIndex(context,record));
  }

  public void highlightRecord(IClientContext context, IDataBrowserRecord record)
  {
    highlightRecord(context, getRecordIndex(context,record));
  }


  /**
   * Gibt an ob einen Hinweistext angezeigt werden soll, wenn keine Records gefunden wurden?
   *
   */
  protected boolean showStatusMessage()
  {
    return false;
  }

  /**
   * Override this function and return the Javascript method for the selected Row contextmenu
   * 
   * @param context
   * @return
   */
  protected String getContextMenuFunction(ClientContext context)
  {
    return null;
  }

  /** 
   * Writes the HTML content to the stream
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    // Die Scrollposition eines Browsers kann sich unabhaengig von seinem Inhalt aendern.
    // Man kann somit den String fuer das setzten der HiddenVarialbe nicht in den Cache des
    // BrowserContent schreiben. Dieser wird ja nur neu berechnet, wenn sich der Inhalt des Browsers
    // wirklich ändert.
    //
    w.write("<input type=\"hidden\" name=\""+getEtrHashCode()+"\"  id=\""+getEtrHashCode()+"\" value=\""+currentScrollPosX+":"+currentScrollPosY+"\">\n");
  }
  
  /**
   * Render a action icon with the FireEvent
   * @param context
   * @param w
   * @param actionName
   * @param icon
   * @param tooltip
   * @throws Exception
   */
  private final void renderAction(IClientContext context, Writer w, String actionName, String value,String oldStyleIcon, Icon newStyleIcon, String tooltip, boolean reloadPage) throws IOException
  {
    if(reloadPage)
      w.write("<a href=\"#\" onClick=\"FireEventData('");
    else
      w.write("<a href=\"#\" onClick=\"reloadPage=false;FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(actionName);
    w.write("','");
    w.write(value);
    w.write("');\"><img align=\"top\" border=\"0\" src=\"");
    
    if(newStyleIcon!=null)
      w.write(newStyleIcon.getPath(true));
    else
      w.write(((ClientSession)context.getSession()).getTheme().getImageURL(oldStyleIcon));
    
    w.write("\" title=\"");
    w.write(tooltip);
    w.write("\"></a>&nbsp;");
  }
  
  /**
   * Returns the left hand side action, decorations for the hands over row index
   * 
   * @param context
   * @param row
   * @return
   * @throws Exception
   */
  protected String getRowDecoration(IClientContext context, int row) throws Exception
  {
    Writer w=new StringWriter(200);
    Iterator<BrowserAction> iter =singleRowActions.values().iterator();
    while(iter.hasNext())
    {
      BrowserAction action =iter.next();
      renderAction(context,w,action.getId(), ""+row,action.getIcon(),action.getIcon(context), action.getTooltip(context),action.reloadPage());
    }
    return w.toString();
  }
  
  /**
   * Returns the CSSstyle class name for the hands over row index. 
   * 
   * @param row
   * @return
   * @throws Exception
   */
  protected String getRowCssClass(IDataBrowserRecord record, int row) throws Exception
  {
    String s="";
    if(record!=null)
    {
      Marker marker = record2marker.get(record);
      if(marker!=null)
      {
        // "error" hat vorrang gegenï¿½ber "warning". Gleichzeitige Darstellung 
        // macht hier keinen Sinn.
        if(marker.error!=null)
           s=s+" error";
        else if(marker.warning!=null)
          s=s+" warning";
        
        s=marker.emphasize?s+" emphasize":s;
      }
    }
    if (row == getDataInternal().getGuiSortStrategy().getSelectedRecordIndex())
      return "sbr_s"+s;

    return (row & 0x1) == 0x1 ? "sbr_o"+s : "sbr_e"+s;
  }

 private Boolean isTreeBrowserFlag = null;
 
  /**
   * This method determines, whether this browser is a tree browser.
   *  
   * @param context
   * @param browserFilter
   * @param record
   * @return
   * @throws Exception
   */
  private boolean isTreeBrowser(IClientContext context, IBrowserEventHandler browserFilter, IDataBrowserRecord record) throws Exception
  {
    if (this.isTreeBrowserFlag == null)
    {
      IBrowserRecordList recordList = browserFilter.getChildren(context, this, record);
      this.isTreeBrowserFlag = recordList == HTTPBrowser.NO_TREE_BROWSER ? Boolean.FALSE : Boolean.TRUE;
    }
    return this.isTreeBrowserFlag.booleanValue();
  }

  /**
   * Writes the HTML content of the cell[row,column]. <br>
   * The column with the index 0 ist <b>not</b> a data column. The column is the container for
   * click headerActions <code>add, delete, modify</code>.<br>
   * If your browser dosn't support this headerActions you can return an empty string or the index of the row.
   * <br>
   * It is <b>not</b> allowed to return <code>null</code><br>
   * 
   * @param context The current context of the request
   * @param row The 0 based index of the row
   * @param column The 0 based index of the column
   * @return <code>true</code> on success, <code>false</code> if an error occurred writing cell content 
   */
  protected boolean writeCellContent(IClientContext context, Writer w,IBrowserEventHandler browserFilter, IDataBrowserRecord record, boolean firstVisibleColumn, int row, int column, boolean handleExceptions) throws Exception
  {
    // if we have no data we return an empty string
    //
    if (getData().recordCount() == 0)
      return true;
    
    // Es kann sein, dass der Browser einene eigneen CellRenderer installiert hat.
    // Die Anwendung kann so in der BrowserCell darstellen was sie will
    IBrowserCellRenderer cellRenderer = column2cellRenderer.get(definition.getBrowserField(column).getName());
    
    // if we have browser filter we return the filterd content of the cell
    // (Browser welcher als Baumdargestellt wird (connectBy) hat immer(!) einen BrowserFilter)
    //
    if (browserFilter != null)
    {
      boolean writeDiv = false;
      if(firstVisibleColumn && isTreeBrowser(context, browserFilter, record) && this.enableTreeView==true)
      {
        List children = getRecordChildrenMap().get(record);
        // Es wurde noch nicht die Kinder ermittelt
        if(children==null)
        {
          if(browserFilter.hasChildren(context,this,record)==false)
            getRecordChildrenMap().put(record,children=HTTPBrowser.NO_CHILDREN);
          else
            getRecordChildrenMap().put(record,children=HTTPBrowser.UNKNOWN_CHILDREN);
        }
        boolean hasChildren = children!=HTTPBrowser.NO_CHILDREN;
        
        BrowserAction action =  (children.size()==0)?BrowserAction.ACTION_EXPAND_ROW:BrowserAction.ACTION_COLLAPSE_ROW;
        
  
        w.write("<div>");
        // die Einruecktiefe des Baumes fuer diesen Rekord rausschreiben
        //
        Marker marker = record2marker.get(record);
        if(marker!=null)
        {
          for(int i=0;i<marker.deep;i++)
          {
            w.write("<img style=\"vertical-align:text-bottom\" src=\"");
            w.write(Icon.blank.getPath(true));
            w.write("\"/>");       
          }
        }
        if(hasChildren)
        {
          writeDiv = true;
          w.write("<a href=\"#\" onClick=\"FireEventData('");
          w.write(Integer.toString(getId()));
          w.write("','");
          w.write(action.getId());
          w.write("','"+row+"');\">");
          w.write("<img border=\"0\" style=\"vertical-align:middle\" src=\"");
          Icon icon = action.getIcon(context);
          if(icon!=null)
            w.write(icon.getPath(true));
          else
            w.write(((ClientSession)context.getSession()).getTheme().getImageURL(action.getIcon()));
          w.write("\"/></a>");
        }
        else
        {
          writeDiv = true;
          w.write("<img style=\"vertical-align:middle\" src=\"");
          w.write(Icon.blank.getPath(true));
          w.write("\"/>");       
        }
      }
      
      String value = record.getSaveStringValue(column, context.getLocale());

      
      Exception errorInHook = null;
      Icon icon;
      try
      {
        icon = browserFilter.decorateCell(context, this, row, column, record, value);
      }
      catch (Exception ex)
      {
        // error in user hook
        icon = Icon.NONE;
        errorInHook = ex;
      }
      if(icon != Icon.NONE)
      {
        // nur wenn das div noch nicht geschrieben wurde, wird dies jetzt getan
        //
        if(writeDiv==false)
          w.write("<div>");
        writeDiv = true;
        w.write("<img class=\"sbc_icon\" style=\"vertical-align:middle\" src=\"");
        w.write(icon.getPath(true));
        w.write("\"/>");       
        try
        {
          value = browserFilter.filterCell(context, this, row, column, record, value);
        }
        catch (Exception ex)
        {
          // error in user hook
          errorInHook = ex;
        }
        w.write(StringUtil.htmlEncode(value));
      }
      else
      {
        try
        {
          value = browserFilter.filterCell(context, this, row, column, record, value);
        }
        catch (Exception ex)
        {
          // error in user hook
          errorInHook = ex;
        }
        if(cellRenderer==null)
        {
          if(value == null || value.length()==0)
            w.write("&nbsp;");
          else
            w.write(StringUtil.htmlEncode(value));
        }
        else
        {
          // Es liegt in der verantwortung des CellRenderes den Inhalt raus zu schreiben.
          // auf KEINEN Fall das Ergebniss mit "htmlEncode" verändern. Es kann ja sein, dass der
          // CellRender HTML Fragmente rausschreibt. Diese wären dann dahin.....
           w.write(cellRenderer.renderCell(context,this, record, row, value)); 
        }
      }
      if(writeDiv)
        w.write("</div>");
      
      // any problem in user hooks?
      if (errorInHook !=null)
      {
        if (handleExceptions)
          ExceptionHandler.handle(context, errorInHook);
        return false;
      }
    }
    else
    {
      String value = record.getSaveStringValue(column, context.getLocale());
      if(cellRenderer==null)
      {
        if(value.length()==0)
          w.write("&nbsp;");
        else
          w.write(StringUtil.htmlEncode(value));
      }
      else
      {
        // Es liegt in der verantwortung des CellRenderes den Inhalt raus zu schreiben.
        // auf KEINEN Fall das Ergebniss mit "htmlEncode" verändern. Es kann ja sein, dass der
        // CellRender HTML Fragmente rausschreibt. Diese wären dann dahin.....
         w.write(cellRenderer.renderCell(context,this, record, row, value)); 
      }
    }
    return true;
  }
  
  /**
   * 
   * @param context
   * @param row
   * @param column
   * @return
   * @throws Exception
   */
  protected void writeFooterCellContent(IClientContext context, Writer w, int column) throws Exception
  {
    w.write("&nbsp;");
  }

  protected void writeCaptionContent(IClientContext context, Writer w) throws Exception
  {
    if(this.caption==null)
      return;
    w.write(this.caption);
  }
  
  /**
   * 
   * @param context
   * @param row
   * @param column
   * @return
   * @throws Exception
   */
  protected String getHeaderCellContent(IClientContext context, int column) throws Exception
  {
    return I18N.localizeLabel(definition.getBrowserField(column).getLabel(), context);
  }
  
 /*
  * Returns the definitions of all browser fields of this browser.
  * 
  * @return List[IBrowserField]
  */
  public List getColumns()
  {
    return definition.getBrowserFields();
  }
  
  private void calculateVisibility(IClientContext context)
  {
    isColumnConfigurable = new boolean[definition.getFieldNumber()];
    isColumnVisible      = new boolean[isColumnConfigurable.length];
    
    // Cache the visible and configurable flag for faster access during HTML rendering
    //
    ClientSession session = (ClientSession)context.getSession();
    for (int i = 0; i < definition.getFieldNumber(); i++)
    {
      IBrowserField field = definition.getBrowserField(i);
      isColumnConfigurable[i] = field.isConfigureable();
      isColumnVisible[i] = field.isVisible();
      // override the default flag with the user settings....if possible
      //
      if(isColumnConfigureable(i))
      {
        String flag = session.getRuntimeProperty(this.getPathName()+"."+this.getName()+"."+i);
        if(flag!=null)
        {
          boolean userFlag = new Boolean(flag).booleanValue();
          isColumnVisible[i] = userFlag;
        }
      }
    }
  }
  
  public void setEnableTreeView(IClientContext context, boolean flag) throws Exception
  {
    if(this.getConnectByKey()==null)
      throw new Exception("Browser didn't support this feature. Set the 'connectBy' property in the jacob desinger browser defintion");
    
    this.enableTreeView = flag;
    
    // nur ein Browser welcher kein Baum ist ist sortierbar
    //
    this.sortable = getConnectByKey()==null || this.enableTreeView==false;

    // reinit/reset the view with the current data
    //
    resetCache();
    
    scrollTo(0,0);
    
    // reset the check tick of the rows.
    record2marker = new HashMap<IDataRecord, Marker>();
    record2ChildrenArray = new HashMap<IDataBrowserRecord, List>();
    browserRecord2tableRecord = new HashMap<IDataBrowserRecord, IDataTableRecord>();

    setSelectedRecordIndex(context, -1);
  }

  public void setColumnVisible(int columnIndex, boolean flag)
  {
    if(isColumnConfigureable(columnIndex))
      isColumnVisible[columnIndex]=flag;
    // Den Zustand in den UserProperties ablegen
    //
    IClientContext context=(IClientContext)Context.getCurrent();
    ClientSession session = (ClientSession)context.getSession();
    session.setRuntimeProperty(this.getPathName()+"."+this.getName()+"."+columnIndex,flag);
    
    resetCache();
    
    // Falls die Column sichtbar gemacht wurde wird dies dann
    // mittels einem glow-Effect hervorgehoben.
    //
    if(flag==true)
    {
      int htmlIndex=0;
      String hashCode = Integer.toString(getId());
      // Anzahl der sichtbaren Column vor dieser Column berechnen
      //
      for(int i=0;i<columnIndex;i++)
      {
        if(isColumnVisible[i]==true)
          htmlIndex++;
      }
      if(hasMultiselect(context))
        htmlIndex++;
      
      // Es muss eine Column zu leuchten gebracht werden. Dies geht dummerweise nur indem
      // man jede einzelne Zelle anspricht.
      ((ClientContext)context).addOnLoadJavascript("var glowE=$('"+hashCode+"');");
      int browserRowCount = Math.max(getEmptyRowCount(),getData().recordCount());
      int glowCount = Math.min(20,browserRowCount);
      for(int i=0;i<glowCount;i++)
      {
        ((ClientContext)context).addOnLoadJavascript("glow(glowE.rows["+i+"].cells["+htmlIndex+"]);");
      }
      scrollTo(0,0);
    }
    
  }

  public boolean isColumnVisible(int columnIndex)
  {
    return isColumnVisible[columnIndex];
  }
  
  public boolean isColumnConfigureable(int columnIndex)
  {
    return isColumnConfigurable[columnIndex];
  }

  public boolean isInMultipleUpdate(IClientContext context)
  {
    return this.runningSelectionEvent!=null;
  }
  
  /**
   * Set the current data of the guiBrowser.
   * 
   * @param data
   */
  public void setData(de.tif.jacob.screen.IClientContext context, IDataBrowser newData)
  {
    resetCache();
    
    scrollTo(0,0);
    
    // reset the check tick of the rows.
    record2marker = new HashMap<IDataRecord, Marker>();
    record2ChildrenArray = new HashMap<IDataBrowserRecord, List>();
    browserRecord2tableRecord = new HashMap<IDataBrowserRecord, IDataTableRecord>();
    
    // remember the change counter of the dataBrowser element
    //
    changeCount = ((IDataBrowserInternal) newData).getChangeCount();
    if(this.getConnectByKey()==null || this.enableTreeView==false)
    {
      this.data = (IDataBrowserInternal) newData;
    }
    else
    {
      IBrowserEventHandler handler= (IBrowserEventHandler)getEventHandler(context);
      int recordCount = newData.recordCount();
      List<IDataBrowserRecord> records = new ArrayList<IDataBrowserRecord>();
      for(int i=0;i<recordCount;i++)
      {
        records.add(newData.getRecord(i));
      }
      this.data.clear();
      for(int i=0;i<recordCount;i++)
      {
        IDataBrowserRecord record = records.get(i);
        try
        {
          IDataTableRecord initialChild = getTableRecord(record);
          addParent(context,handler,new ArrayList<IDataKeyValue>(), initialChild);
        }
        catch(Exception exc)
        {
          exc.printStackTrace();
        }
      }
    }
  }

  
  /**
   * {@inheritDoc}
   */
  public IDataBrowser getData()
  {
    return getDataInternal();
  }

  /**
   * Ist gleich wie getData() liefert allerdings das erweiterte Interface zurueck. Ist somit
   * nur fuer die eigentliche interne Implementierungen gedacht.
   *  
   * Ist nicht Teil des oeffentlichen Interfaces!!
   * 
   */
  public IDataBrowserInternal getDataInternal()
  {
    if (data == null)
      data = (IDataBrowserInternal) Context.getCurrent().getDataAccessor().getBrowser(this.definition);
    return data;
  }

  public int append(IClientContext context,IDataTableRecord record)
  {
    return append(context,record, true);
  }
  
  public int append(IClientContext context,IDataTableRecord record, boolean setAsSelectedRecord)
  {
    int index =getDataInternal().append(record);
    
    // and simply set it as selected
    if(setAsSelectedRecord)
    {
      getData().setSelectedRecordIndex(index);
      highlightRecord(context,index);
    }

    return index;
  }

  public void add(IClientContext context, IDataTableRecord record) throws Exception
  {
     add(context,record,true);
  }
  
  public void add(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception
  {
    // pruefen ob the browser einen hook hat und ob der record eventuell
    // an einen bestehenden record angehï¿½ngt werden kann (Baumdarstellung)
    Object objh= getEventHandler(context);
    if(objh==null)
    {
      int index = getDataInternal().add(record);
      if(setAsSelectedRecord)
      {
        getData().setSelectedRecordIndex(index);
        scrollTo(0,0);
        highlightRecord(context,index);
      }
    }
    else
    {
      if(!(objh instanceof IBrowserEventHandler))
        throw new UserException("Class ["+objh.getClass().getName()+"] does not implement required interface/class ["+IBrowserEventHandler.class.getName()+"]");
     
      IBrowserEventHandler handler = (IBrowserEventHandler)objh;
      addParent(context,handler,new ArrayList<IDataKeyValue>(), record);
      if(setAsSelectedRecord)
      {
        int index = getRecordIndex(context,record);
        setSelectedRecordIndex(context,index);
        highlightRecord(context,index);
      }
    }
  }

  public void remove(IClientContext context, IDataTableRecord record) throws Exception
  {
    IDataBrowserRecord browserRecord= getDataRecord(getRecordIndex(context,record));
    remove(context,browserRecord);
  }
  
  public void remove(IClientContext context, IDataBrowserRecord browserRecord) throws Exception
  {
    if(getDataInternal().removeRecord(browserRecord)==false)
      return;
    
    record2marker.remove(browserRecord);
    
    Map<IDataBrowserRecord, List> record2children = this.getRecordChildrenMap();
    
    // Alle Kinder dieses Knotens entfernen
    //
    List orig = record2children.get(browserRecord);
    if(orig!=null)
    {
      List children = new ArrayList(orig);
      Iterator iter = children.iterator();
      while (iter.hasNext())
      {
        IDataBrowserRecord child = (IDataBrowserRecord) iter.next();
        remove(context,child);
      }
    }
    
    // Den Record bei dem Vater austragen.
    //
    Iterator iter =record2children.entrySet().iterator();
    while (iter.hasNext())
    {
      Entry entry = (Entry) iter.next();
      List  children = (List)entry.getValue();
      // children kann vom typ NO_CHILDREN oder UNKNOWN_CHILDREN sein
      if (children.size() > 0 && children.remove(browserRecord))
      {
        if(children.size()==0)
        {
          entry.setValue(HTTPBrowser.NO_CHILDREN);
        }
      }
    }
    record2children.remove(browserRecord);
  }
  
  /**
   * Add recursiv all parent records to the browser
   * 
   * @param context
   * @param handler
   * @param record
   * @throws Exception
   */
  private void addParent(IClientContext context,IBrowserEventHandler handler,List<IDataKeyValue> visitedBrowserRecords, IDataTableRecord record) throws Exception
  {
    visitedBrowserRecords.add(record.getPrimaryKeyValue());
    IDataTableRecord parent = handler.getParent(context,this,record);
    
    
    // Der Record hat keinen Vater. Wir sind an der Wurzel angekommen.
    if(parent==null || visitedBrowserRecords.contains(parent.getPrimaryKeyValue()))
    {
      // jetzt fuegen wir den Record ein wenn dieser noch nicht vorhanden ist
      // und expandieren diesen gleich.
      boolean parentIsAllreadyInBrowser=false;
      int recordCount = getData().recordCount();
      for(int i=0;i<recordCount;i++)
      {
        IDataBrowserRecord browserRecord = getDataRecord(i);
        if(browserRecord.getPrimaryKeyValue().equals(record.getPrimaryKeyValue()))
        {
          parentIsAllreadyInBrowser=true;
          break;
        }
      }
      if(!parentIsAllreadyInBrowser)
        getDataInternal().add(record);
      expand(context,record);
    }
    else
    {
      addParent(context,handler,visitedBrowserRecords, parent);
      expand(context,record);
    }
  }

  public IDataBrowserRecord getParent( IDataBrowserRecord record)
  {
    Iterator iter = this.record2ChildrenArray.entrySet().iterator();
    while(iter.hasNext())
    {
      Entry entry = (Entry)iter.next();
      if(((List)entry.getValue()).contains(record))
        return (IDataBrowserRecord)entry.getKey();
    }
    return null;
  }
  
  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(de.tif.jacob.screen.IClientContext context) throws Exception
  {
    super.clear(context);
    scrollTo(0,0);
    if(data!=null)
    {
    	data.clear();
      data=null;
    }
    if(this.runningSelectionEvent!=null)
    {
      try {this.runningSelectionEvent.cancel(context,this);}catch(Exception exc){/*ignore*/}
      this.runningSelectionEvent=null;
    }
  }

  public void scrollTo(long x, long y)
  {
    currentScrollPosX=x;
    currentScrollPosY=0; 
  }
  
  /**
   * A browser has no DataFields at the moment
   * 
   */
  protected void addDataFields(Vector fields)
  {
  }
  
  public String getEventHandlerReference()
  {
    return null;
  }

  public GuiEventHandler getEventHandler(IClientContext context)
  {
    GuiEventHandler handler = super.getEventHandler(context);
    // Falls der Anwendungentwickler ein ConnectBy definiert hat und kein EventHandler vorhanden ist, wird ein
    // Default EventHandler als Plugin eingehï¿½ngt. Dieser ï¿½bernimmt dann die Aufgabe
    // die Methoden fï¿½r hasChildren,getChildren und getParent ordentlich auszufï¿½hren.
    //
    // (Ansonsten liegt es in der Verantwortung des Anwendungsentwickler)
    if(handler==null && getConnectByKey()!=null && this.enableTreeView==true)
      return DEFAULT_HOOK;
    
    return handler;
  }
  

  public void setCaption(String caption)
  {
    // NICHT den Cache zuruecksetzten!!! ist bei einem Browser zu teuer.
    // Wird im Moment auch nicht benoetigt, da die Methode nicht nach aussen gegeben wird.
    // Wird die Methode nach aussen gereicht, dann muss der Cache auf eine effiziente Art wieder
    // aufgebaut werden
    //
    this.caption = caption;
  }

  protected String getCaption()
  {
    return caption;
  }

  public void expand(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    int index = getRecordIndex(context,record);

    // not part of the browser
    if(index <0)
      return;
    
    // Falls der Record bereits aufgeklappt ist wird der erst zusammengeklappt
    // und danach wieder aufgeklappt. Dies entspricht einen "refresh".
    //
    if(getRecordChildrenMap().get(record)!=null)
      BrowserAction.ACTION_COLLAPSE_ROW.execute(context,this,""+index);

    BrowserAction.ACTION_EXPAND_ROW.execute(context,this,""+index);
  }

  public void expandAll(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    int index = getRecordIndex(context,record);

    // not part of the browser
    if(index <0)
      return;
    
    // Falls der Record bereits aufgeklappt ist wird der erst zusammengeklappt
    // und danach wieder aufgeklappt. Dies entspricht einen "refresh".
    //
    if(getRecordChildrenMap().get(record)!=null)
      BrowserAction.ACTION_COLLAPSE_ROW.execute(context,this,""+index);

    BrowserAction.ACTION_EXPAND_ROW.execute(context,this,""+index);
    List l = getRecordChildrenMap().get(record);
    if(l!=null)
    {
      Iterator iter = l.iterator();
      while (iter.hasNext())
      {
        IDataBrowserRecord child = (IDataBrowserRecord)iter.next();
        expandAll(context,child);
      }
    }
  }
  
  public void expand(IClientContext context, IDataTableRecord record) throws Exception
  {
    int index = getRecordIndex(context,record);

    // not part of the browser
    if(index<0)
      return;
    
    IDataBrowserRecord browserRecord = getDataRecord(index);
    expand(context,browserRecord);
  }
  
  public boolean hasChildren(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    // Keine Baumdarstellung => keine Kinder
    if(this.enableTreeView==false)
      return false;
    
    int index = getRecordIndex(context, record);

    // not part of the browser
    if (index < 0)
      return false;
    
    List children = getRecordChildrenMap().get(record);
    // Es wurde noch nicht die Kinder ermittelt
    if (children == null)
    {
      IBrowserEventHandler handler = (IBrowserEventHandler) getEventHandler(context);
      if (handler.hasChildren(context, this, record) == false)
        getRecordChildrenMap().put(record, children = HTTPBrowser.NO_CHILDREN);
      else
        getRecordChildrenMap().put(record, children = HTTPBrowser.UNKNOWN_CHILDREN);
    }
    return children != HTTPBrowser.NO_CHILDREN;
  }
  
  public void checkRow(int index)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker==null)
      record2marker.put(record,new Marker(0,null,null,false,true));
    else
      marker.checked=true;
  }

  public void uncheckRow(int index)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker!=null)
    {
      marker.checked=false;
      if(marker.allFalse())
        record2marker.remove(record);
    }
  }

  public void toggleRow(int index)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker==null)
      record2marker.put(record,marker=new Marker(0,null,null,false,true));
    else
      marker.checked=!marker.checked;
    
    if(marker.allFalse())
      record2marker.remove(record);
  }


  public void resetErrorDecoration(IClientContext context, int index) 
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker!=null)
      marker.error=null;
    
    if(marker.allFalse())
      record2marker.remove(record);
  }
  

  public void resetErrorDecoration(IClientContext context) 
  {
    resetCache();
    // Markierungen anpassen
    //
    Iterator iter = new ArrayList(record2marker.entrySet()).iterator();
    while(iter.hasNext())
    {
      Entry entry = (Entry) iter.next();
      Marker marker = (Marker)entry.getValue();
      marker.error=null;
      if(marker.allFalse())
        record2marker.remove(entry.getKey());
    }
  }
  
  public void setEmphasize(IClientContext context, int index, boolean flag)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    setEmphasize(context,record,flag);
  }
  
  public void setEmphasize(IClientContext context, IDataRecord record, boolean flag)
  {
    resetCache();
    Marker marker = record2marker.get(record);
    if(marker==null)
      record2marker.put(record,marker=new Marker(0,null,null,flag,false));
    else
      marker.emphasize=flag;

    if(marker.allFalse())
      record2marker.remove(record);
  }


  public void resetEmphasize(IClientContext context) 
  {
    resetCache();
    // Markierungen anpassen
    //
    Iterator iter = new ArrayList(record2marker.entrySet()).iterator();
    while(iter.hasNext())
    {
      Entry entry = (Entry) iter.next();
      Marker marker = (Marker)entry.getValue();
      marker.emphasize=false;
      if(marker.allFalse())
        record2marker.remove(entry.getKey());
    }
  }
  

  public void installCellRenderer(IClientContext context, String browserColumnName, IBrowserCellRenderer renderer) throws Exception
  {
    // sicherstellen, dass es die BrowserColumn gibt
    // wift eine Exception falls es die Column nicht gibt.
    //
    definition.getBrowserField(browserColumnName);
    
    column2cellRenderer.put(browserColumnName,renderer);
  }

  public void setWarningDecoration(IClientContext context, int index, String message)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker==null)
      record2marker.put(record,new Marker(0,null,message!=null?message:"",false,false));
    else
      marker.warning=message!=null?message:"";
  }

  public void setWarningDecoration(IClientContext context, IDataRecord record, String message)
  {
    setWarningDecoration(context,this.getRecordIndex(context,record), message);
  }

  public void resetWarningDecoration(IClientContext context, int index) 
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker!=null)
      marker.warning=null;
    
    if(marker.allFalse())
      record2marker.remove(record);
  }
  

  public void resetWarningDecoration(IClientContext context) 
  {
    resetCache();
    Iterator iter = new ArrayList(record2marker.entrySet()).iterator();
    while(iter.hasNext())
    {
      Entry entry = (Entry) iter.next();
      Marker marker = (Marker)entry.getValue();
      marker.warning=null;
      if(marker.allFalse())
        record2marker.remove(entry.getKey());
    }
  }
  
  public void setErrorDecoration(IClientContext context, int index, String message)
  {
    resetCache();
    IDataBrowserRecord record = getDataRecord(index);
    Marker marker = record2marker.get(record);
    if(marker==null)
      record2marker.put(record,new Marker(0,message!=null?message:"",null,false,false));
    else
      marker.error=message!=null?message:"";
  }
  

  public void resetErrorDecoration(IClientContext context, IDataRecord record)
  {
    resetErrorDecoration(context,this.getRecordIndex(context,record));
  }

  public void setErrorDecoration(IClientContext context, IDataRecord record, String message)
  {
    setErrorDecoration(context,this.getRecordIndex(context,record), message);
  }

  public ISelection getSelection()
  {
    IDataBrowserInternal dataBrowser = getDataInternal();

    BrowserSelection selection = new BrowserSelection(this);
    IDataBrowserSortStrategy sort =dataBrowser.getGuiSortStrategy();
    // Es ist genau einer im Browser angeklickt und zurueckgefuellt
    // eine PrimarySelection erzeugen
    if(dataBrowser.getSelectedRecordIndex()!=-1)
    {
      selection= new BrowserSelection(this,sort.getRecord( dataBrowser.getSelectedRecordIndex()));
    }
    
    // Alle Records mit einem "tick" in die Selektion einfuegen
    //
    int count = getData().recordCount();
    for(int i=0;i<count;i++)
    {
      IDataBrowserRecord record = getDataRecord(i);
      Marker marker = record2marker.get(record);
      if(marker!=null && marker.checked==true)
        selection.add(record);
    }
      
    return selection;
  }

  
  public Map<IDataBrowserRecord, List> getRecordChildrenMap()
  {
    return record2ChildrenArray;
  }

  public Map<IDataRecord, Marker> getRecordMarkerMap()
  {
    return record2marker;
  }

  /**
   * Es ist multiselect mï¿½glich wenn der user dies via API so eingestellt hat
   * oder wenn mindestens eine SelectionAction vorhanden ist und mindestens ein
   * Record im Browser vorhanden ist.
   * 
   * @return
   */
  public boolean hasMultiselect(IClientContext context)
  {
    return (selectionActions.size()>0 || getEventHandler(context) instanceof ISelectionListener) && getDataInternal().recordCount()>0;
  }

  private IDataTableRecord getTableRecord(IDataBrowserRecord browserRecord) throws Exception
  {
    IDataTableRecord tableRecord =browserRecord2tableRecord.get(browserRecord);
    if(tableRecord!=null)
      return tableRecord;
    
    browserRecord2tableRecord.put(browserRecord, tableRecord=browserRecord.getTableRecord());
    return tableRecord;
  }

}

