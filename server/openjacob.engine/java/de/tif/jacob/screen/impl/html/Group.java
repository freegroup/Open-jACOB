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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.map.ListOrderedMap;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.data.internal.IDataTableRecordInternal;
import de.tif.jacob.core.definition.IGroupDefinition;
import de.tif.jacob.core.definition.ISelectionActionDefinition;
import de.tif.jacob.core.definition.impl.AbstractGuiElement;
import de.tif.jacob.core.definition.impl.AbstractSelectionActionDefinition;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IContainer;
import de.tif.jacob.screen.IContextMenu;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IGroupEventHandler;
import de.tif.jacob.screen.event.IHtmlGroupEventHandler;
import de.tif.jacob.screen.event.ITabPaneEventListener;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPGroup;
import de.tif.jacob.screen.impl.HTTPLongText;
import de.tif.jacob.screen.impl.HTTPSelectionAction;
/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class Group extends GuiHtmlElement implements HTTPGroup, ITabPane
{
  static public final transient String RCS_ID = "$Id: Group.java,v 1.57 2011/01/21 17:12:14 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.57 $";
  static protected String TAB_ACTION_EVENT ="tab_action_event";
  
  protected SearchBrowser browser;
  private DataField[] groupDataFields = null;
  private boolean autoselect = true;
  protected final IGroupDefinition definition;
  protected final ContextMenu contextMenu;
  protected final ListOrderedMap actions  = new ListOrderedMap();

  class HotdeployableISelectionAction extends HTTPSelectionAction
  {
    final AbstractSelectionActionDefinition definition;

    HotdeployableISelectionAction(ISelectionActionDefinition definition)
    {
      super(definition.getLabel());
      this.definition = (AbstractSelectionActionDefinition) definition;
    }

    public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
    {
      getAction().execute(context, emitter, selection);
    }

    public Icon getIcon(IClientContext context)
    {
      return getAction().getIcon(context);
    }

    public boolean isEnabled(IClientContext context, IGuiElement host)
    {
      return getAction().isEnabled(context, host);
    }


    public boolean isVisible(IClientContext context, IGuiElement host)
    {
      return getAction().isVisible(context, host);
    }

    private ISelectionAction getAction()
    {
      return (ISelectionAction) ClassProvider.getInstance(getApplicationDefinition(), definition.getEventHandler());
    }
  }

  protected Group(IApplication app, IGroupDefinition group, Rectangle rect)
  {
    super(app, group.getName(), group.getLabel(), true, rect, group.getProperties());
    // Element muss sich an einen container anpassen. Z.B. TabContainer.
    // Es hat somit keine eigene Dimension
    //
    if (boundingRect != null && boundingRect.height == 0 && boundingRect.width == 0)
      boundingRect = null;
    definition = group;
    browser = new SearchBrowser(app, group.getActiveBrowserDefinition(), this);
    browser.setParent(this);
    // add the context menu of this group as an child of this group
    // ...but only if it not empty.
    contextMenu = new ContextMenu(app, group.getContextMenuEntries());
    addChild(contextMenu);
    Iterator iter = definition.getSelectionActions().iterator();
    while (iter.hasNext())
    {
      AbstractSelectionActionDefinition selectionAction = (AbstractSelectionActionDefinition) iter.next();
      try
      {
        HTTPSelectionAction action = (HTTPSelectionAction) Class.forName(selectionAction.getEventHandler()).newInstance();
        action.setLabel(selectionAction.getLabel());
        browser.addSelectionAction(action);
      }
      catch (Exception exc)
      {
        // Es ist keine Action der Engine. Es ist eine der Application und muss
        // somit
        // mit dem ApplicationClassloader geladen werden. Dies muss dann auch
        // austauschbar
        // sein sobald ein Hotdeployment durchgeführt wird.
        browser.addSelectionAction(new HotdeployableISelectionAction(selectionAction));
      }
    }
  }

  public List getBrowsers()
  {
    return Collections.singletonList(getBrowser());
  }

  public IContextMenu getContextMenu()
  {
    return this.contextMenu;
  }

  public IContainer getContainer(IClientContext context) throws Exception
  {
    throw new Exception("Method Not supportet by ["+this.getClass().getName()+"]");
  }

  public boolean getHideEmptyBrowser()
  {
    return definition.hideEmptyBrowser();
  }

  
  public void onHide(IClientContext context) throws Exception
  {
    GuiEventHandler eventObj = getEventHandler(context);
    if (eventObj instanceof IGroupEventHandler)
    {
      // Dafür sorgen, dass der Eventhandler von der Gruppe
      // sich auf den korrekt gesetzten context verlassen kann
      ((ClientContext) context).setGroup(this);
      ((IGroupEventHandler) eventObj).onHide(context, this);
    }
  }

  /*
   * 
   * @see de.tif.jacob.screen.impl.HTTPGroup#onShow(de.tif.jacob.screen.IClientContext)
   */
  public void onShow(IClientContext context) throws Exception
  {
    GuiEventHandler eventObj = getEventHandler(context);
    if (eventObj instanceof IGroupEventHandler)
    {
      // Dafür sorgen, dass der Eventhandler von der Gruppe
      // sich auf den korrekt gesetzten context verlassen kann
      ((ClientContext) context).setGroup(this);
      ((IGroupEventHandler) eventObj).onShow(context, this);
    }
  }

  /*
   * 
   * @see de.tif.jacob.screen.IGroup#getSelectedRecord(de.tif.jacob.screen.IClientContext)
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception
  {
    IDataTableInternal table = (IDataTableInternal) context.getDataTable(this.definition.getTableAlias().getName());
    return table.getSelectedRecord();
  }

  public void clear(de.tif.jacob.screen.IClientContext context) throws Exception
  {
    clear(context, true);
  }

  /**
   * @since 2.8.0
   */
  public void setActive(de.tif.jacob.screen.IClientContext context) throws Exception
  {
    if (this.getParent() instanceof TabContainer)
    {
      TabContainer container = (TabContainer) this.getParent();
      container.setActivePane(context, this);
    }
    else
    {
      // Falls es eine normale Gruppe oder eine Gruppe in der Gruppe ist, dann ist diese sowieso aktive
      // und der Aufruf kann ignoriert werden.
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.impl.GuiElement#invalidate()
   */
  public void invalidate()
  {
    super.invalidate();
    if (this.browser != null)
      this.browser.invalidate();
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(de.tif.jacob.screen.IClientContext context, boolean clearSearchBrowser) throws Exception
  {
    getForm().setFocus(this.getFirstElementInTabOrder());
    // Falls in eine Script/Hook context.clearForm(..) aufgerufen wird, darf
    // sich die Gruppe/Form/Focus
    // NICHT verändern. Derzeitige Gruppe/Form merken und am Ende der Funktion
    // wieder restaurieren
    //
    ClientContext clientContext = (ClientContext) context;
    Group currentGroup = (Group) context.getGroup();
    clientContext.setGroup(this);
    Form currentForm = (Form) context.getForm();
    clientContext.setForm((HTTPForm) this.getForm());
    // FREEGROUP: Wichtig: Die Domäne kann sich ggfs. auch ändern und gesetzt
    // und wieder restauriert werden. Einerseits wenn DataScope auf Domaine
    // eingestellt ist, aber anderseits auch wenn ein clearGroup() per Script in
    // einer anderen Domaine aufruft.
    try
    {
      // the super implementation resets ALL elements.
      // A foreignField can be an element of this group. In this case
      // the foreignField will be resetted too.
      //
      super.clear(context);
      // Falls man bewusst den Browser zurücksetzten möchte ODER in dem Browser
      // keine Records
      // angezeigt werden wird dieser expliziet zurück gesetzt. dies hat die
      // Auswirkung, dass
      // auch eine eventuell vorhandene "MSG_NO_RECORDS_FOUND" Meldung in dem
      // Browser verschwindet.
      // Dies würde sonst stehen bleiben und den Benutzer ein wenig verwirren.
      //
      if (this.browser != null)
      {
        if (clearSearchBrowser || browser.getData().recordCount() == 0)
          browser.clear(context);
        else
          browser.setSelectedRecordIndex(context, -1);
      }
      // reset the data accessor with the corressponding table alias
      // of this group
      //
      context.getDataAccessor().getTable(this.definition.getTableAlias()).clear();
      setDataStatus(context, SEARCH);
    }
    finally
    {
      // Alten Zustand wieder herstellen
      clientContext.setForm(currentForm);
      clientContext.setGroup(currentGroup);
    }
  }

  /**
   * Ein TabPane leitet sich aus einer Gruppe ab. Intern werden dann die ITabPaneAction in der Gruppe
   * installiert. Werde im Fall einer richtigen Gruppe allerdings (noch) nicht rausgerendert. Werden nur angezeigt wenn
   * die Gruppe eine richtige TabPane ist und der TabStrip angezeigt wird. 
   */
  public void addAction(ITabPaneAction action) throws Exception
  {
    this.actions.put(action.getId(), action);
    // TabStrip wird vom Parent gezeichnet.
    this.parent.resetCache();
  }

  public List getActions() throws Exception
  {
    return this.actions.valueList();
  }
  
  /**
   * The input element with the lowest TabIndex will get the input focus.
   * 
   */
  public ISingleDataGuiElement getFirstElementInTabOrder()
  {
    ISingleDataGuiElement focusElement = null;
    for (int i = 0; i < getChildren().size(); i++)
    {
      Object obj = getChildren().get(i);
      if (obj instanceof SingleDataGUIElement)
      {
        ISingleDataGuiElement input = (ISingleDataGuiElement) obj;
        if (focusElement == null)
          focusElement = input;
        else if (focusElement.getTabIndex() > input.getTabIndex())
          focusElement = input;
      }
    }
    return focusElement;
  }

  /**
   * The input element with the lowest TabIndex will get the input focus.
   * 
   */
  public void resetTabOrder()
  {
    if (getParent() instanceof IForm)
      ((IForm) getParent()).setFocus(getFirstElementInTabOrder());
  }

  /**
   * 
   * @return
   * @author Andreas Herz
   */
  public IBrowser getBrowser()
  {
    return browser;
  }

  public void setBrowserAutoSelect(boolean autoSelect)
  {
    this.autoselect = autoSelect;
  }

  public boolean isBrowserAutoSelect()
  {
    return this.autoselect;
  }

  /**
   * The method collects all DataFields of it childs and return them.
   * 
   * @return All DataFields in this domain.
   */
  public DataField[] getDataFields()
  {
    if (groupDataFields == null)
    {
      Vector tmp = new Vector();
      addDataFields(tmp);
      groupDataFields = new DataField[tmp.size()];
      tmp.copyInto(groupDataFields);
    }
    return groupDataFields;
  }

  /**
   * Falls die Kinder einer Group entfernt werden (z.B. MutableGroup), dann
   * mï¿½ssen den Ihrer DataFields neu berechnet werden.
   * 
   */
  public void removeAllChildren()
  {
    groupDataFields = null;
    super.removeAllChildren();
  }

  /**
   * Falls die Kinder einer Group erweitert werden (z.B. MutableGroup), dann
   * mï¿½ssen den Ihrer DataFields neu berechnet werden.
   * 
   */
  public void addChild(IGuiElement child)
  {
    groupDataFields = null;
    super.addChild(child);
  }

  /**
   * 
   */
  public void setInputFieldValue(String fieldName, String value) throws Exception
  {
    GuiHtmlElement element = (GuiHtmlElement) findByName(fieldName);
    if (element == null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field [" + fieldName + "] in group [" + getPathName() + "]");
    ISingleDataGuiElement input = (ISingleDataGuiElement) element;
    input.setValue(value);
  }

  public void setInputFieldValue(String fieldName, java.util.Date value) throws Exception
  {
    String strValue = I18N.toFullDatetimeString(Context.getCurrent().getLocale(), value);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, BigDecimal value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, Double value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  public void setInputFieldValue(String fieldName, Float value) throws Exception
  {
    String strValue = I18N.toString(value, Context.getCurrent().getLocale(), IDataRecord.DEFAULT_STYLE);
    setInputFieldValue(fieldName, strValue);
  }

  /**
   * 
   */
  public String getInputFieldValue(String fieldName) throws Exception
  {
    GuiHtmlElement element = (GuiHtmlElement) findByName(fieldName);
    if (element == null || !(element instanceof ISingleDataGuiElement))
      throw new NoSuchFieldException("Unable to find GUI input field [" + fieldName + "] in group [" + getPathName() + "]");
    ISingleDataGuiElement input = (ISingleDataGuiElement) element;
    return input.getValue();
  }

  /**
   * 
   */
  public void addDataFields(Vector fields)
  {
    Iterator iter = getChildren().iterator();
    while (iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement) iter.next();
      element.addDataFields(fields);
    }
  }

  
  public IDataTableRecord newRecord(IClientContext context, IDataTransaction transaction) throws Exception
  {
      IDataTable table = context.getDataTable(this.getGroupTableAlias());
      IDataTableRecordInternal currentDataRecord = (IDataTableRecordInternal) table.newRecord(transaction);

      DataField[] fields=this.getDataFields();
      
      for (int i = 0; i < fields.length; i++)
      {
        DataField field = fields[i];

        // only data of the HTTPGroup (table alias) can be commited.
        // ignore foreign fields
        if (field.getFieldType() == DataField.TYPE_NORMAL && !(field.getParent() instanceof HTTPLongText) )
        {
          // IBIS: überprüfen, ob nicht die equals() Methode von Definitionelementen überschrieben
          //       werden soll, damit der Alias auch dann gleich bleibt, wenn ein Hotdeployment da
          //       war.
          if (field.getTableAlias().getName().equals(this.getGroupTableAlias()))
          {
            // only initialize fields, which have not been initialized before
            // (by data-hooks, etc.)
            if (null == currentDataRecord.getValue(field.getField().getFieldIndex()))
            {
              Object value = field.getValue();
              // empty fields == null fields in the database
              if (value != null && value instanceof String && ((String) value).length() == 0)
                continue;

              try
              {
                if (value instanceof String)
                  // Request 437: Truncate value, if too long
                  currentDataRecord.setStringValueWithTruncation(transaction, field.getField().getName(), (String) value, context.getLocale());
                else
                  currentDataRecord.setValue(transaction, field.getField().getName(), value, context.getLocale());
              }
              catch (InvalidExpressionException ex)
              {
                // ignore data values which could not be converted properly
                // e.g. when switching from search mode to new mode, there might
                // be expressions like "ENUM1|ENUM2" or "date1..date2"
              }
            }
          }
        }
      }
      this.setDataStatus(context, IGuiElement.NEW);
      return currentDataRecord;
  }

  public IDataTableRecord newRecord(IClientContext context) throws Exception
  {
    return newRecord(context, context.getDataAccessor().newTransaction());
  }
  
  
  /**
   * 
   */
  public boolean hasError()
  {
    Iterator iter = getChildren().iterator();
    while (iter.hasNext())
    {
      GuiHtmlElement element = (GuiHtmlElement) iter.next();
      if (element.hasError())
        return true;
    }
    return false;
  }

  /**
   * @param dataStatus
   *          The dataStatus to set.
   */
  public void setDataStatus(IClientContext context, GroupState dataStatus) throws Exception
  {
    super.setDataStatus(context, dataStatus);
    resetTabOrder();
    onGroupDataStatusChanged(context, dataStatus);
  }

  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, newGroupDataStatus);
    Object eventObj = getEventHandler(context);
    
    // it's a normal group
    if (eventObj instanceof IGroupEventHandler)
      ((IGroupEventHandler) eventObj).onGroupStatusChanged(context, newGroupDataStatus, this);
    // it's a tab pane
    else if(eventObj instanceof ITabPaneEventListener)
      ((ITabPaneEventListener)eventObj).onGroupStatusChanged(context, newGroupDataStatus, this);
    
    
    // Der Browser sollte dieses Event auch mitbekommen. Eventuell muss dieser
    // einen Record
    // von einem Knoten zu einem anderen Knoten umhängen wenn sich der
    // VaterRecord geändert haben
    // sollte.
    //
    if (this.browser != null)
      this.browser.onGroupDataStatusChanged(context, newGroupDataStatus);
  }

  /**
   * 
   */
  public GuiElement findChild(int guid)
  {
    // the SearchBrowser is not a child of the group.
    // This must be handle by the group.
    //
    if (this.browser != null && guid == this.browser.getId())
      return (GuiElement) this.getBrowser();
    return super.findChild(guid);
  }

  /**
   * Check if an Element is in the hands over GroupState.<br>
   * This is usefull to handle the CLearFocus function proper. The clearFocus
   * must show an 'are you shure' dialog if a group is in the NEW or UPDATE
   * mode.
   * 
   */
  public boolean hasChildInDataStatus(IClientContext context, IGuiElement.GroupState state) throws Exception
  {
    return getDataStatus() == state;
  }

  /**
   * @return Returns the dbTableName.
   */
  public String getGroupTableAlias()
  {
    return this.definition.getTableAlias().getName();
  }

  /**
   * Method mapping for the ITabPane interface.
   * 
   * Wird an dem Interface ITabPane für den Programmierer veröffentlicht. Macht im Context einer normalen Gruppe (JacobGroup)
   * keinen Sinn, da getGroupTableAlias in diesem Fall das gleiche Ergebnis liefert.
   * 
   * @since 2.8.0
   */
  public String getPaneTableAlias() throws Exception
  {
    return this.definition.getTableAlias().getName();
  }

  /**
   * Returns the parent group of this UI element. This can be a TabPane if the
   * element inside a TabPane group.
   * 
   * @see #getOuterGroup
   * @return the parent group of this UI element
   */
  public IGroup getGroup()
  {
    return this;
  }

  /**
   * Returns the outer HTTPGroup element of this UI element. This is always the
   * top most group. It is never an TabPane-Group if the UI element is a child
   * of a TabPane.
   * 
   * @see #getGroup
   * @since 2.8.1
   * @return the top most group (parent) of this UI element
   */
  public IGroup getOuterGroup()
  {
    return this;
  }

  /**
   * @return Returns the definition.
   */
  public IGroupDefinition getDefinition()
  {
    return definition;
  }

  public String getEventHandlerReference()
  {
    return ((AbstractGuiElement) definition).getEventHandler();
  }

  /**
   * Den Eventhandler der Gruppe fragen ob es fï¿½r den zugehï¿½rigen
   * SearchBrowser auch einen Eventhandler gibt. Is ein Umweg. Begrï¿½ndet sich
   * aber darin, dass man im GUI Designer keinen Eventhandler an den
   * SearchBrowder hï¿½ngen kann. Der SearchBrowser wird im Designer nicht
   * dargestellt.
   * 
   */
  public String getSearchBrowserEventHandlerReference()
  {
    Object eventObj = getEventHandler((IClientContext) Context.getCurrent());
    if (eventObj != null)
    {
      if (eventObj instanceof IGroupEventHandler)
      {
        IGroupEventHandler handler = (IGroupEventHandler) eventObj;
        Class clazz = handler.getSearchBrowserEventHandlerClass();
        return clazz != null ? clazz.getName() : null;
      }
    }
    return null;
  }
}
