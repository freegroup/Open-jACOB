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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.ResizeMode;
import de.tif.jacob.core.definition.guielements.TableListBoxDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserCellRenderer;
import de.tif.jacob.screen.IBrowserRecordAction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.ILinkParser;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.ILinkEventListener;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;
import de.tif.jacob.screen.impl.AbstractAction;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.BrowserActionLazyBackfill;
import de.tif.jacob.screen.impl.HTTPTableListBox;
import de.tif.jacob.util.StringUtil;
/*
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TableListBox extends GuiHtmlElement implements HTTPTableListBox, IBrowser
{
  static public final transient String RCS_ID = "$Id: TableListBox.java,v 1.55 2010/12/08 17:01:14 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.55 $";
  private final TableListBoxDefinition definition;
  private AdhocBrowserDefinition browserDefinition;
  private BrowserAction lazyBackfill = null;
  protected long changeCount = -1;
  IDataBrowserInternal data = null;
  protected Map column2cellRenderer = new HashMap(); // Map<String,IBrowserCellRenderer>
  protected Map id2action = new HashMap(); // Map<String, IBrowserRecordAction>
  private ILinkParser linkParser = null;
  private ILinkEventListener linkListener=null;

  protected TableListBox(IApplication app, TableListBoxDefinition def)
  {
    super(app, def.getName(), null, def.isVisible(), def.getRectangle(), def.getProperties());
    definition = def;
    if (null != def.getCaption())
      addChild(new Caption(app, def.getCaption()));
    try
    {
      if (definition.getBrowserToUse() == null)
      {
        browserDefinition = (AdhocBrowserDefinition) app.getApplicationDefinition().createAdhocBrowserDefinition(def.getTableAlias());
        browserDefinition.addBrowserField(0, def.getTableAlias().getName(), def.getDisplayField().getName(), SortOrder.ASCENDING, "");
      }
      else
      {
        browserDefinition = new AdhocBrowserDefinition(app.getApplicationDefinition(), definition.getBrowserToUse());
      }
    }
    catch (NoSuchFieldException e)
    {
      ExceptionHandler.handle(e);
    }
  }

  public void addAction(IBrowserRecordAction action) throws Exception
  {
    id2action.put(action.getId(), action);
    resetCache();
  }

  public void installCellRenderer(IClientContext context, String browserColumnName, IBrowserCellRenderer renderer) throws Exception
  {
    // sicherstellen, dass es die BrowserColumn gibt
    // wift eine Exception falls es die Column nicht gibt.
    //
    if (definition.getBrowserToUse() != null)
    {
      // sicherstellen, dass es die BrowserColumn gibt
      // wift eine Exception falls es die Column nicht gibt.
      this.browserDefinition.getBrowserField(browserColumnName);
    }
    else
    {
      // Es wurde kein Browsre im JAD hinterlegt. In diesem Fall sind wir ein
      // bischen toleranter und
      // verwenden die einzigste Spalte welche im Browser ist.
      browserColumnName = this.browserDefinition.getBrowserField(0).getName();
    }
    column2cellRenderer.put(browserColumnName, renderer);
  }

  /*
   * 
   * @see
   * de.tif.jacob.screen.IBrowser#newRecord(de.tif.jacob.screen.IClientContext,
   * de.tif.jacob.core.data.IDataTransaction)
   */
  public IDataTableRecord newRecord(IClientContext context, IDataTransaction trans) throws Exception
  {
    IDataTableRecord newRecord = context.getDataTable(getDefinition().getTableAlias().getName()).newRecord(trans);
    this.add(context, newRecord);
    return newRecord;
  }

  public void highlightRecord(IClientContext context, int rowIndex)
  {
    throw new RuntimeException("Method not implemented at the moment");
  }

  public void highlightRecord(IClientContext context, IDataTableRecord record)
  {
    throw new RuntimeException("Method not implemented at the moment");
  }

  public void highlightRecord(IClientContext context, IDataBrowserRecord record)
  {
    throw new RuntimeException("Method not implemented at the moment");
  }

  public void ensureVisible(IClientContext context, IDataRecord record)
  {
    throw new RuntimeException("Method not implemented at the moment");
  }

  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if (!isVisible())
      return;
    // Falls die Form welche gerade berechnet wird nicht diese ist welche gerade
    // sichtbar ist, interessiert uns dies
    // bei einer TableListBox nicht. Man kann sich so den teuren Browserbackfill
    // sparen
    //
    IForm activeForm = ((Application) context.getApplication()).getActiveDomain().getCurrentForm(context);
    if (activeForm != context.getForm())
      return;
    if (lazyBackfill != null)
    {
      lazyBackfill.execute(context, this, null);
      lazyBackfill = null;
    }
    IDataBrowserInternal dataBrowser = getDataInternal();
    if (dataBrowser.getChangeCount() != changeCount)
    {
      resetCache();
      changeCount = dataBrowser.getChangeCount();
    }
 
    if (getCache() == null)
    {
      // write all children to the screen. e.g. The caption of a combo box or
      // any other decoration
      // only neccessary if
      super.calculateHTML(context);
      Writer w = newCache();
 
      ITableListBoxEventHandler handler = (ITableListBoxEventHandler)getEventHandler(context);
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
      
      w.write("<div class=\"listbox_selected");
      if (this.definition.getResizeMode() != ResizeMode.NONE)
      {
        w.write(" resize\" resizeMode=\"");
        w.write(this.definition.getResizeMode().getName());
        w.write("\" style=\"overflow:auto;");
      }
      else
      {
        w.write("\" style=\"");
      }
      getCSSStyle(context, w, boundingRect);
      w.write(" id=\"container_");
      w.write(getEtrHashCode());
      w.write("\">\n");
      w.write("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" >");
      boolean withEvent = (getEventHandler(context) != null);
      int selectedIndex = data.getGuiSortStrategy().getSelectedRecordIndex();
      int counter = 0;
      for (int i = 0; i < data.recordCount(); i++)
      {
        counter = 0;
        IDataBrowserRecord record = data.getRecord(i);
        String clickEvent = withEvent ? "onclick=\"FireEventData('" + Integer.toString(getId()) + "','click','" + Integer.toString(i) + "');\"" : "";
        if (selectedIndex == i)
          w.write("<tr " + clickEvent + " class=\"listboxitem_selected\" >");
        else
          w.write("<tr " + clickEvent + " class=\"listboxitem\" >");
        // Der Benutzer kann Einträge löschen wenn dies der Modus im JAD zuläst
        // 
        if ((this.definition.getCanDeleteSelected()) || (this.definition.getCanDeleteUpdateNew() && (getDataStatus() == UPDATE || getDataStatus() == NEW)))
        {
          w.write("<td width=\"1\">");
          renderAction(context, w, BrowserAction.ACTION_DELETE, i);
          w.write("</td>");
          counter++;
        }
        Iterator fieldIter = browserDefinition.getBrowserFields().iterator();
        while (fieldIter.hasNext())
        {
          IBrowserTableField field = (IBrowserTableField) fieldIter.next();
          // Feld überspringen falls dieses nicht sichtbar ist.
          if (!field.isVisible())
            continue;
          IBrowserCellRenderer renderer = (IBrowserCellRenderer) column2cellRenderer.get(field.getName());
          if (renderer == null)
          {
            w.write("<td width=\"1\"><pre>\n");
          }
          else
          {
            int width = renderer.getCellWidth(context);
            if (width > 0)
              w.write("<td style=\"width:" + width + "px\" width=\"" + width + "\" >\n");
            else
              w.write("<td width=\"1\">\n");
          }
          String value = record.getSaveStringValue(field.getFieldIndex());
          // als Erstes geht der Inhalt durch die filterCell Methode des
          // GUI-Element hook
          //
          if (handler != null)
            value = handler.filterCell(context, this, i, field.getTableField().getName(), record, value);
          // Danach wird eventuell noch der BrowserCellRenderer gerufen welcher
          // den Inhalt gesondert aufbereiten und
          // Darstellen kann
          if (renderer != null)
          {
            if(this.linkParser!=null)
              w.write(this.linkParser.parse(context, this, renderer.renderCell(context, this, record, i, value)));
            else
              w.write(renderer.renderCell(context, this, record, i, value));
            w.write("</td>\n");
          }
          else
          {
            if(this.linkParser!=null)
               w.write(this.linkParser.parse(context,this,StringUtil.htmlEncode(value)));
            else
               w.write(StringUtil.htmlEncode(value));
            w.write("</pre></td>\n");
          }
          counter++;
        }
        w.write("<td> </td>\n");
        w.write("</tr>");
      }
      w.write("<tr>\n");
      for (int i = 0; i <= counter; i++)
        w.write("<td></td>\n");
      w.write("</tr></table>\n");
      w.write("</div>\n");
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
  }

  /**
   * The framework call this method is an event comes from the client
   * guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid != this.getId())
      return super.processEvent(context, guid, event, value);
    
    try
    {
      if (HtmlLinkRenderStrategy.EVENT_CLICK.equals(event))
      {
        if(this.linkListener!=null)
          this.linkListener.onClick(context, this, value);
      }
      else
      {
        // als erstes die user definerten Aktionen überprüfen
        //
        IBrowserRecordAction action = (IBrowserRecordAction) id2action.get(event);
        if (action != null)
        {
          action.execute(context, this, this.getDataRecord(Integer.parseInt(value)));
          return true;
        }
        if (BrowserAction.ACTION_DELETE.getId().equals(event))
        {
          BrowserAction.ACTION_DELETE.execute(context, this, value);
          return true;
        }
        this.setSelectedRecordIndex(context, Integer.parseInt(value));
      }
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return true;
  }

  public ISelection getSelection()
  {
    IDataBrowserInternal dataBrowser = getDataInternal();
    // Falls kein zurueckgefuellter Record vorhanden ist
    //
    if (dataBrowser.getSelectedRecordIndex() != -1)
    {
      return new TableListBoxSelection(this);
    }
    // es ist genau einer im Browser angeklickt und zurueckgefuellt
    //
    else if (dataBrowser.getSelectedRecordIndex() != -1)
    {
      return new TableListBoxSelection(this, dataBrowser.getRecord(dataBrowser.getSelectedRecordIndex()));
    }
    // Empty selection
    return new TableListBoxSelection(this);
  }

  /**
   * Calls the eventhandler for the Button event handler
   * 
   */
  public final void onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState status) throws Exception
  {
    lazyBackfill = null;
    // the parent group has select a record
    // -> update the data of the InFormBrowser
    //
    if (status == SELECTED)
    {
      // lazy backfill of the record if the browser realy display the content
      lazyBackfill = new BrowserActionLazyBackfill();
    }
    else if (status == UPDATE)
    {
      // eine TableListBox kann nicht mit dem MultipleUpdate umgehen.
      if (context.getSelectedRecord() instanceof IDataMultiUpdateTableRecord)
        getData().clear();
    }
    else if (status == SEARCH)
    {
      getData().clear();
    }
    else
    {
    }
    super.onGroupDataStatusChanged(context, status);
    GuiEventHandler handler = getEventHandler(context);
    if (handler instanceof ITableListBoxEventHandler)
      ((ITableListBoxEventHandler) handler).onGroupStatusChanged(context, status, this);
  }

  protected void addDataFields(Vector fields)
  {
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(de.tif.jacob.screen.IClientContext context) throws Exception
  {
    super.clear(context);
    // currentScrollPosX = 0;
    if (data != null)
    {
      data.clear();
      data = null;
    }
  }

  private final void renderAction(IClientContext context, Writer w, AbstractAction action, int index) throws IOException
  {
    w.write("<a href=\"#\" onClick=\"FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(action.getId());
    w.write("','" + index + "');\"><img align=\"top\" border=\"0\" src=\"");
    Icon icon = action.getIcon(context);
    if (icon != null)
      w.write(icon.getPath(true));
    else
      w.write(((ClientSession) context.getSession()).getTheme().getImageURL(action.getIcon()));
    w.write("\" title=\"");
    action.getTooltip(context);
    w.write("\"></a>&nbsp;");
  }

  public List getColumns()
  {
    return this.browserDefinition.getBrowserFields();
  }

  /**
   * lazy instanciation
   * 
   * @return Returns the data.
   */
  public final IDataBrowser getData()
  {
    // Der Applicationprogrammierer hat die Daten eventuell angefordert bevor
    // diese von der Datenbank geholt worden sind. Tja - nachladen.
    if (lazyBackfill != null)
    {
      try
      {
        lazyBackfill.execute((IClientContext) Context.getCurrent(), this, null);
        lazyBackfill = null;
      }
      catch (Exception exc)
      {
        ExceptionHandler.handle(exc);
      }
    }
    return getDataInternal();
  }

  public final IDataBrowserRecord getDataRecord(int rowIndex) throws IndexOutOfBoundsException
  {
    return getDataInternal().getGuiSortStrategy().getRecord(rowIndex);
  }

  /**
   * Ist gleich wie getDate() liefert allerdings das erweiterte Interface
   * zurueck. Ist somit nur fuer die eigentliche interne Implementierungen
   * gedacht.
   * 
   * Ist nicht Teil des oeffentlichen Interfaces!!
   * 
   */
  public final IDataBrowserInternal getDataInternal()
  {
    if (data == null)
      data = (IDataBrowserInternal) Context.getCurrent().getDataAccessor().createBrowser(this.browserDefinition);
    return data;
  }

  public IBrowserDefinition getDefinition()
  {
    return this.browserDefinition;
  }

  /**
   * Set the current data of the guiBrowser.
   * 
   * @param data
   */
  public void setData(de.tif.jacob.screen.IClientContext context, IDataBrowser data)
  {
    resetCache();
    this.data = (IDataBrowserInternal) data;
    // reset the scroll position
    //
    // currentScrollPosX=0;
  }

  public int append(IClientContext context, IDataTableRecord record) throws Exception
  {
    return append(context, record, true);
  }

  public void add(IClientContext context, IDataTableRecord record) throws Exception
  {
    add(context, record, true);
  }

  public void add(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception
  {
    resetCache();
    getDataInternal().add(record);
  }

  public int append(IClientContext context, IDataTableRecord record, boolean setAsSelectedRecord) throws Exception
  {
    resetCache();
    return getDataInternal().append(record);
  }

  public void resetSelectedRecord(IClientContext context) throws Exception
  {
    setSelectedRecordIndex(context, -1);
  }

  public void addSelectionAction(ISelectionAction action)
  {
  }

  public void removeSelectionAction(ISelectionAction action)
  {
  }

  public void removeAllSelectionActions()
  {
  }

  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception
  {
    int index = data.getSelectedRecordIndex();
    if (index < 0)
      return null;
    return data.getRecord(index).getTableRecord();
  }

  public IDataBrowserRecord getSelectedBrowserRecord(IClientContext context) throws Exception
  {
    int index = data.getSelectedRecordIndex();
    if (index < 0)
      return null;
    return data.getRecord(data.getSelectedRecordIndex());
  }

  public int getRecordIndex(IClientContext context, IDataRecord record)
  {
    int recordCount = getData().recordCount();
    for (int i = 0; i < recordCount; i++)
    {
      IDataRecord browserRecord = getDataRecord(i);
      if (browserRecord.getId().equals(record.getId()))
        return i;
    }
    return -1;
  }

  /*
   * 
   * @see
   * de.tif.jacob.screen.IBrowser#remove(de.tif.jacob.screen.IClientContext,
   * de.tif.jacob.core.data.IDataBrowserRecord)
   */
  public void remove(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    getDataInternal().removeRecord(record);
    resetCache();
  }

  /*
   * 
   * @see
   * de.tif.jacob.screen.IBrowser#remove(de.tif.jacob.screen.IClientContext,
   * de.tif.jacob.core.data.IDataTableRecord)
   */
  public void remove(IClientContext context, IDataTableRecord record) throws Exception
  {
    int index = getRecordIndex(context, record);
    getDataInternal().remove(index);
    resetCache();
  }

  public void refresh(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    resetCache();
  }

  public void refresh(IClientContext context, IDataTableRecord record) throws Exception
  {
    resetCache();
  }

  public void setSelectedRecordIndex(IClientContext context, int index) throws Exception
  {
    if ((index != getDataInternal().getGuiSortStrategy().getSelectedRecordIndex()))
    {
      // Notify the eventhandler about the new selection.
      //
      ITableListBoxEventHandler listboxHook = (ITableListBoxEventHandler) getEventHandler(context);
      resetCache();
      if (index == -1)
      {
        getData().clearSelections();
      }
      else
      {
        getDataInternal().getGuiSortStrategy().setSelectedRecordIndex(index);
        IDataBrowserRecord recordToSelect = getDataInternal().getGuiSortStrategy().getRecord(index);
        if (listboxHook != null && !listboxHook.beforeAction(context, this, recordToSelect))
          return;
        IDataTableRecord tableRecordToSelect = recordToSelect.getTableRecord();
        if (tableRecordToSelect.isNewOrUpdated())
        {
          IDataTableInternal table = (IDataTableInternal) tableRecordToSelect.getTable();
          table.setSelectedRecord(tableRecordToSelect);
        }
        else
        {
          getDataInternal().propagateSelections();
        }
        if (listboxHook != null)
          listboxHook.onSelect(context, this, tableRecordToSelect);
      }
    }
  }

  public void setSelectedRecord(IClientContext context, IDataTableRecord record) throws Exception
  {
    setSelectedRecordIndex(context, getRecordIndex(context, record));
  }

  public void setSelectedRecord(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    setSelectedRecordIndex(context, getRecordIndex(context, record));
  }

  public void setLinkHandling(ILinkParser parser, ILinkEventListener listener) throws Exception
  {
    this.linkParser = parser;
    this.linkListener = listener;
    
    this.resetCache();
  }
  
  public void addHiddenAction(BrowserAction clickAction)
  {
  }

  public void removeHiddenAction(BrowserAction updateAction)
  {
  }

  public void removeRowAction(BrowserAction action)
  {
  }

  public void uncheckRow(int index)
  {
  }

  public void checkRow(int index)
  {
  }

  public void expandAll(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    // TODO Auto-generated method stub
  }

  public void expand(IClientContext context, IDataBrowserRecord record) throws Exception
  {
  }

  public void expand(IClientContext context, IDataTableRecord record) throws Exception
  {
  }

  public boolean hasChildren(IClientContext context, IDataBrowserRecord record) throws Exception
  {
    return false;
  }

  public void resetErrorDecoration(IClientContext context, IDataRecord record)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void resetErrorDecoration(IClientContext context, int i)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void setErrorDecoration(IClientContext context, IDataRecord record, String message)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void setErrorDecoration(IClientContext context, int index, String message)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void setEmphasize(IClientContext context, IDataRecord record, boolean flag)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void setEmphasize(IClientContext context, int rowIndex, boolean flag)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void resetEmphasize(IClientContext context)
  {
    // TODO: Implementierung bei Bedarf nachziehen
  }

  public void setWarningDecoration(IClientContext context, int index, String message)
  {
    // TODO Auto-generated method stub
  }

  public void resetWarningDecoration(IClientContext context, int rowIndex)
  {
    // TODO Auto-generated method stub
  }

  public void resetWarningDecoration(IClientContext context)
  {
    // TODO Auto-generated method stub
  }

  public void setWarningDecoration(IClientContext context, IDataRecord record, String message)
  {
    // TODO Auto-generated method stub
  }
  
  public void setEnableTreeView(IClientContext context, boolean flag) throws Exception
  {
      throw new Exception("TableListBox didn't support this feature at the moment.");
  }
}
