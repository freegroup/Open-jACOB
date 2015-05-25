/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.IJacobGroupDefinition;
import de.tif.jacob.core.definition.guielements.ResizeMode;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IContainer;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.impl.HTTPForeignField;
public class JacobGroup extends Group
{
  private boolean hasBorder = false;
  private boolean isTabPane = false;
  private long changeCount = -1;
  private String i18nLabel = null; // override the GuiElement i18nLabel!!!
  private final IJacobGroupDefinition jacobGroupDefinition;
  private TabContainer container;
  

  public JacobGroup(IApplication app, IJacobGroupDefinition group)
  {
    super(app, group, group.getRectangle());
    hasBorder = group.hasBorder();
    this.borderWidth = group.getBorderWidth();
    this.borderColor = group.getBorderColor();
    this.backgroundColor = group.getBackgroundColor();
    this.jacobGroupDefinition = group;
  }

  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if (!isTabPane())
      context.setGroup(this);
    // initial set of the data status
    if (getDataStatus() == UNDEFINED)
      setDataStatus(context, SEARCH);
    // update the element if the data has been changed
    // /e.g. an element of a table alias has been selected
    //
    IDataTableInternal table = (IDataTableInternal) context.getDataTable(this.definition.getTableAlias().getName());
    IDataTableRecord currentDataRecord = getSelectedRecord(context);
    if (table.getChangeCount() != changeCount)
    {
      // Der Record hat sich geändert -> Alle GUI Elemente nachladen
      //
      Iterator iter = getChildren().iterator();
      while (iter.hasNext())
      {
        Object obj = iter.next();
        if (obj instanceof SingleDataGUIElement && !(obj instanceof HTTPForeignField))
        {
          SingleDataGUIElement element = (SingleDataGUIElement) obj;
          // Falls das UI Element den selectedRecord anzeigen soll (index==-1)
          // wird dieser dem Element gleich übergeben.
          if (element.getDisplayRecordIndex() == -1)
          {
            element.setValue(context, currentDataRecord);
          }
          // ...ansonsten wird die ganze Tabelle übergeben und das Element
          // kann sich den Record selber raus holen. Wird z.B. bei eine MutableGroup verwendet wenn man
          // einen "InformBrowser" selbst aufbaut.
          //
          else
          {
            // Wenn das Element nicht den selectedRecord anzeigt, dann ist es
            // auch nicht  gesagt, dass sich das Element auf den Alias der Gruppe bezieht.
            element.setValue(context, context.getDataTable(element.getDataField().getTableAlias().getName()));
          }
        }
      }
      // den Status des Record an die GUI weiterleiten
      // Es kann sein, das ein Record durch einen Hook manipuliert worden ist.
      // Darauf muss nun reagiert werden.
      //
      if (currentDataRecord != null && currentDataRecord.isUpdated() && getDataStatus() != UPDATE)
        setDataStatus(context, UPDATE);
      else if (currentDataRecord != null && currentDataRecord.isNormal() && getDataStatus() != SELECTED)
        setDataStatus(context, SELECTED);
      else if (currentDataRecord != null && currentDataRecord.isNormal() && getDataStatus() == SELECTED && changeCount != table.getChangeCount())
        setDataStatus(context, SELECTED);
      else if (currentDataRecord != null && currentDataRecord.isNew() && getDataStatus() != NEW)
        setDataStatus(context, NEW);
      else if (currentDataRecord == null && getDataStatus() != SEARCH)
        setDataStatus(context, SEARCH);
      // set new change counter after all children have been informed
      // (nobody knows what they will do which has an influence on the change
      // counter as well :-)
      changeCount = table.getChangeCount();
    }
    if (getCache() == null)
    {
      Writer w = newCache();
      String eventGUID = getEtrHashCode();
      cssStyleCache = null; // hack
      if (isTabPane())
      {
        if (hasBorder)
        {
          w.write("\n<div id=\"");
          w.write(eventGUID);
          w.write("\" oncontextmenu=\"");
          w.write(contextMenu.getContextMenuFunction());
          if (this. container.definition.getResizeMode() != ResizeMode.NONE)
          {
            w.write("\" resizeMode=\"");
            w.write(this. container.definition.getResizeMode().getName());
            w.write("\" class=\"tabPane resize\" style=\"overflow:auto;");
          }
          else
          {
            w.write("\" class=\"tabPane\" style=\"");
          }
          getCSSStyle(context, w, boundingRect);
          w.write("\">\n");
        }
        else
        {
          w.write("\n<div id=\"");
          w.write(eventGUID);
          w.write("\" oncontextmenu=\"");
          w.write(contextMenu.getContextMenuFunction());
          w.write("\" style=\"");
          getCSSStyle(context, w, boundingRect);
          w.write("\">\n");
        }
      }
      else if (hasBorder)
      {
        w.write("\n<div isGroup=\"true\" id=\"");
        w.write(eventGUID);
        w.write("\" name=\"");
        w.write(eventGUID);
        w.write("\" oncontextmenu=\"");
        w.write(contextMenu.getContextMenuFunction());
        if(jacobGroupDefinition.getResizeMode()!= ResizeMode.NONE)
        {
           w.write("\" resizeMode=\"");
           w.write(jacobGroupDefinition.getResizeMode().getName());
           w.write("\" class=\"group_normal resize\" style=\"overflow:auto");
         }
        else
        {
          w.write("\" class=\"group_normal\" style=\"");
        }
        getCSSStyle(context, w, boundingRect);
        w.write("\">");
        
        w.write("<div class=\"group_legend\" ");
        w.write(">&nbsp;");
        w.write(getI18NLabel(context.getLocale()));
        w.write("</div>\n");
      }
      else
      {
        w.write("\n<div isGroup=\"true\" id=\"");
        w.write(eventGUID);
        w.write("\" oncontextmenu=\"");
        w.write(contextMenu.getContextMenuFunction());
        w.write("\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\">\n");
      }
    }
    super.calculateHTML(context);
  }

  /**
   * ATTENTION: This implementation differs from the super implementation. See
   * the sequenze of the calls FIRST write me than the children!!!!
   * 
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    context.setGroup(this);
    writeCache(w);
    super.writeHTML(context, w);
    w.write("</div>\n");
    if(this.definition.getProperty("ensure_visible")!=null)
      context.addOnLoadJavascript("new FloatingElement('"+getEtrHashCode()+"');");
  }

  /**
   * The framework call this method is an event comes from the client
   * guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // Falls die Gruppe eine TabPane ist, dann wird nicht die Gruppe gesetzt
    // (Es wird immer die Gruppe des übergeordneten TabContainers genommen)
    if (!isTabPane)
      ((ClientContext) context).setGroup(this);
    
    if(guid==this.getId() && TAB_ACTION_EVENT.equals(event))
    {
      ITabPaneAction action =(ITabPaneAction) this.actions.get(value);
      if(action!=null)
        action.execute(context, this.container, this);
      return true;
    }   
    
    return super.processEvent(context, guid, event, value);
  }

  protected boolean hasBorder()
  {
    return hasBorder;
  }

  public void setBorder(boolean hasBorder)
  {
    this.hasBorder = hasBorder;
    resetCache();
  }

  public String getI18NLabel(Locale locale)
  {
    if (i18nLabel != null)
      return i18nLabel;
    // add the state of the group in the title of the group
    //
    // Mitarbeiter bearbeiten
    // Search Employee
    String label = super.getI18NLabel(locale);
    String state = getDataStatus().toString().toUpperCase();
    i18nLabel = new CoreMessage("TITLE_GROUP_" + state, label).print(locale);
    return i18nLabel;
  }

  protected void setTabPane()
  {
    this.isTabPane = true;
    this.boundingRect = ((GuiHtmlElement) getParent()).getBoundingRect();
    this.browser = null;
    this.container = (TabContainer)this.getParent(); 
  }

  public boolean isTabPane()
  {
    return isTabPane;
  }


  public void setVisible(boolean flag)
  {
    super.setVisible(flag);

    // falls ich ein TabPane bin ..
    if (isTabPane())
    {
      IClientContext context = (IClientContext) Context.getCurrent();
      TabContainer container = (TabContainer) this.getParent();

      container.resetCache();

      // und ich aktiv bin, dann muss dafür gesorgt werden, dass
      // ein anderes sichtbares TabPane aktiv ist.
      if (container.getActivePane() == this && flag == false)
      {
        List panes = container.getPanes();
        for (int i = 0; i < panes.size(); i++)
        {
          ITabPane pane = (ITabPane) panes.get(i);
          if (pane.isVisible())
          {
            try
            {
              container.setActivePane(context, pane);
            }
            catch (Exception exc)
            {
              ExceptionHandler.handle(context, exc);
            }
            break;
          }
        }
      }
    }
  }
  
  public IContainer getContainer(IClientContext context) throws Exception
  {
    return container;
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
    if (parent == null)
      return null;
    if (isTabPane())
      return parent.getOuterGroup();
    return this;
  }

  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    // Falls ich kein TabPane bin, wird auch onOuter... gerufen. Die Elemente in
    // einem
    // TabPane haben somit die Möglichkeit auch auf Veränderungen der
    // übergeordneten Gruppe zu reagieren.
    // Erst onOuterGroup und dann onGroupStautsChanged rufen damit man mit den
    // Altanwendungen kompatible ist.
    if (!isTabPane())
      this.onOuterGroupDataStatusChanged(context, newGroupDataStatus);
    super.onGroupDataStatusChanged(context, newGroupDataStatus);
  }

  public void clear(de.tif.jacob.screen.IClientContext context, boolean clearSearchBrowser) throws Exception
  {
    super.clear(context, clearSearchBrowser);
    i18nLabel = null;
  }

  public void setDataStatus(IClientContext context, GroupState dataStatus) throws Exception
  {
    super.setDataStatus(context, dataStatus);
    i18nLabel = null;
  }
}
