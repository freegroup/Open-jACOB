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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.NumberUtils;
import org.apache.xml.utils.FastStringBuffer;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataTableRecordInternal;
import de.tif.jacob.core.definition.ISelectionActionDefinition;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.InFormBrowserDefinition;
import de.tif.jacob.core.definition.impl.AbstractSelectionActionDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IInformBrowserEventHandler;
import de.tif.jacob.screen.event.ISelectionListener;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.HTTPInFormBrowser;
import de.tif.jacob.screen.impl.HTTPSelectionAction;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;
/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InFormBrowser extends Browser implements HTTPInFormBrowser
{
  static public final transient String RCS_ID = "$Id: InFormBrowser.java,v 1.41 2011/01/21 17:12:14 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.41 $";
  private final InFormBrowserDefinition definition;
  private final List inlineFields = new ArrayList();
  private BrowserAction lazyBackfill = null;
  private Rectangle boundingRectWithAction = new Rectangle();
  private final String comboEtrHashCode = getEtrHashCode(IDProvider.next());
  private final String buttonEtrHashCode = getEtrHashCode(IDProvider.next());
  private FastStringBuffer externalHtml = new FastStringBuffer();
  class HotdeployableISelectionAction extends HTTPSelectionAction
  {
    /**
     */
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

    public String getId()
    {
      return getAction().getId();
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

  /**
   * @param name
   * @param label
   * @param isInvisibleString
   */
  protected InFormBrowser(IApplication app, InFormBrowserDefinition browser)
  {
    super(app, browser.getName(), browser.getCaption() == null ? null : browser.getCaption().getLabel(), browser.getBrowserToUse(), browser.getRectangle());
    width = boundingRect.width + "px";
    // Falls eine Action im Browser ist muß seine Höhe um 20px reduziert werden.
    // Dieser Platz wird dann von der Combobox eingenommen
    //
    boundingRectWithAction = new Rectangle(boundingRect);
    boundingRectWithAction.height = boundingRectWithAction.height - 25;
    addHiddenAction(BrowserAction.ACTION_ADD_COLUMN);
    this.definition = browser;
    // pass thru visible information!
    setVisible(browser.isVisible());
  }

  public void setParent(GuiElement newParent)
  {
    super.setParent(newParent);
    // Muss dummerweise hier gemacht werden, da die Hotdeplyo.... Klasse die
    // ApplicationsDefinition
    // des "parent" benötigt. Im normalen Konstruktur ist jedoch der "parent"
    // noch nicht gesetzt.
    // TODO: Wird hier wirklich eine HotDeplyomentAction benötigt oder reicht
    // auch ein ein/aus loggen des Anwenders?
    boolean first = true;
    Iterator iter = definition.getSelectionActions().iterator();
    while (iter.hasNext())
    {
      ISelectionActionDefinition selectionAction = (ISelectionActionDefinition) iter.next();
      ISelectionAction action;
      addSelectionAction(action = new HotdeployableISelectionAction(selectionAction));
      if (first)
      {
        this.lastSelectionEvent = action;
        first = false;
      }
    }
  }

  protected int getEmptyRowCount()
  {
    return Math.max(0, 10 - this.getData().recordCount());
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception
  {
    lazyBackfill = null;
    // the parent group has select a record
    // -> update the data of the InFormBrowser
    //
    if (newGroupDataStatus == SELECTED)
    {
      // lazy backfill of the record if the browser realy display the content
      lazyBackfill = BrowserAction.ACTION_LAZY_BACKFILL;
    }
    else if (newGroupDataStatus == UPDATE)
    {
      setSelectedRecordIndex(context, -1);
      removeHiddenAction(BrowserAction.ACTION_SORT_ASC);
      removeHiddenAction(BrowserAction.ACTION_SORT_DESC);
      removeHiddenAction(BrowserAction.ACTION_SORT_NONE);
      removeHiddenAction(BrowserAction.ACTION_CLICK);
      if (definition.isDeletingEnabled())
        addRowAction(BrowserAction.ACTION_DELETE);
      if (definition.isUpdatingEnabled())
        addHiddenAction(BrowserAction.ACTION_UPDATE);
    }
    else if (newGroupDataStatus == SEARCH)
    {
      for (int i = 0; i < inlineFields.size(); i++)
      {
        SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
        if (element != null)
        {
          element.setValue((String) null);
          element.onGroupDataStatusChanged(context, SEARCH);
        }
      }
      removeHiddenAction(BrowserAction.ACTION_SORT_ASC);
      removeHiddenAction(BrowserAction.ACTION_SORT_DESC);
      removeHiddenAction(BrowserAction.ACTION_SORT_NONE);
      removeRowAction(BrowserAction.ACTION_DELETE);
      removeRowAction(BrowserAction.ACTION_UPDATE);
      addHiddenAction(BrowserAction.ACTION_CLICK);
      IDataAccessor accessor = context.getDataAccessor();
      IDataBrowserInternal browser = (IDataBrowserInternal) accessor.getBrowser(this.definition.getBrowserToUse());
      // IBIS: HACK: Diese Abfrage wird für VE 3.0 benötigt. Weiss mir nicht
      // anders zu helfen ;-(
      // Anmerkung: mit diesem Flag möchte ich das Aktualisieren des IF-Browsers
      // selbst übernehmen und mir das nicht immer kaputt machen lassen.
      if (!browser.isDisableSearchIFB())
        browser.clear();
    }
    else
    {
      removeRowAction(BrowserAction.ACTION_DELETE);
      removeRowAction(BrowserAction.ACTION_UPDATE);
      removeHiddenAction(BrowserAction.ACTION_SORT_ASC);
      removeHiddenAction(BrowserAction.ACTION_SORT_DESC);
      removeHiddenAction(BrowserAction.ACTION_SORT_NONE);
      addHiddenAction(BrowserAction.ACTION_CLICK);
    }
    super.onGroupDataStatusChanged(context, newGroupDataStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the
    // textfield
    // or it can calculate the new value if the element a non DB bounded
    // element.
    // Anmerkung:
    // Hier muß expliziert mit "instanceof" geprüft werdne, da es noch alte
    // Applikationen gibt
    // welche nur IBrowserEventhandelr implmentieren.
    //
    Object obj = getEventHandler(context);
    if (obj instanceof IInformBrowserEventHandler)
      ((IInformBrowserEventHandler) obj).onGroupStatusChanged(context, newGroupDataStatus, this);
  }

  /**
   * {@inheritDoc}
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
    return super.getData();
  }

  /*
   * (non-Javadoc)
   * 
   * @seede.tif.jacob.screen.impl.html.Browser#setData(de.tif.jacob.screen.
   * IClientContext, de.tif.jacob.core.data.IDataBrowser)
   */
  public void setData(IClientContext context, IDataBrowser newData)
  {
    super.setData(context, newData);
    // Datenwurden expliziet vom Benutzer gesetzt. Die letzte lazyBackfill
    // (welche vom System selber kommt) ist somit nicht mehr relevant.
    lazyBackfill = null;
  }

  /**
   * Proceed the action. An action can be added with
   * <code>addAction(BrowserAction action)</code><br>
   * <br>
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    for (int i = 0; i < inlineFields.size(); i++)
    {
      SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
      if (element != null && element.processEvent(context, guid, event, value))
      {
        // FREEGROUP: Es muss nicht jedesmal der Cache gelöscht werden wenn ein
        // InlineElement
        // den Parameter konsumiert hat. Es kann ja sein, das sich das Element
        // nicht verändert hat.!!!!
        // ***Dies ist besonders böse, wenn der InformBrowser sehr viele
        // Elemente enthält***
        // FREEGROUP: Methode 'SingleDateGuiElement.hasChanged' implementieren.
        return true;
      }
    }
    if (guid == this.getId() && event.equals(BrowserAction.ACTION_ADD.getId()))
    {
      BrowserAction.ACTION_ADD.execute(context, this, value);
      int column = NumberUtils.stringToInt(value, 0);
      SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(column);
      if (element != null)
        context.getForm().setFocus(element);
      return true;
    }
    return super.processEvent(context, guid, event, value);
  }

  public void onEndEditMode(IClientContext context) throws Exception
  {
    super.onEndEditMode(context);
    setSelectedRecordIndex(context, -1);
  }

  /**
   * The default implementation transfer the data from the event to the GUI
   * guid. (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter(int guid, String data) throws IOException, NoSuchFieldException
  {
    if (getDataStatus() == SEARCH || getDataStatus() == UPDATE || getDataStatus() == NEW)
    {
      for (int i = 0; i < inlineFields.size(); i++)
      {
        SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
        if (element != null && element.processParameter(guid, data))
        {
          // FREEGROUP: Es muss nicht jedesmal der Cache gelöscht werden wenn
          // ein InlineElement
          // den Parameter konsumiert hat. Es kann ja sein, das sich das Element
          // nicht verändert hat.!!!!
          // ***Dies ist besonders böse, wenn der InformBrowser sehr viele
          // Elemente enthält***
          // FREEGROUP: Methode 'SingleDateGuiElement.hasChanged'
          // implementieren.
          resetCache();
          return true;
        }
      }
    }
    // the super-implementation trys to find the corresponding child
    // and sends the event to them
    return super.processParameter(guid, data);
  }

  /**
   * Clear/Reset the GUI Element to the default behaviour.
   * 
   * The default implementation calls all child to clear it.
   */
  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
    for (int i = 0; i < inlineFields.size(); i++)
    {
      SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
      if (element != null)
        element.clear(context);
    }
  }

  /**
   * Set the Caption of the Informbrowser. This Method must reset the cache
   * manually because the base class didn't do that for performance reasons.
   * 
   */
  public void setLabel(String l)
  {
    super.setCaption(l);
    resetCache();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.impl.GuiElement#invalidate()
   */
  public void invalidate()
  {
    super.invalidate();
    for (int i = 0; i < this.inlineFields.size(); i++)
    {
      SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
      if (element != null)
        element.invalidate();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer)
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if (!isVisible())
      return;
    // Falls die Form welche gerade berechnet wird nicht diese ist welche gerade
    // sichtbar ist, interessiert uns dies
    // bei einem InformBrowser nicht.
    // Man kann sich so den teuren Browserbackfill sparen
    //
    IForm activeForm = ((Application) context.getApplication()).getActiveDomain().getCurrentForm(context);
    if (activeForm != context.getForm())
      return;
    if (lazyBackfill != null)
    {
      lazyBackfill.execute(context, this, null);
      lazyBackfill = null;
    }
    // Falls sich der DataBrowser geändert hat muss der Browser neu berechent
    // werden.
    // Dies kann passieren falls sich der Anwendungsprogrammierer den
    // DataBrowser holt
    // und via API-Calls verändert.
    if (getDataInternal().getChangeCount() != changeCount)
      resetCache();
    if (getCache() == null)
    {
      // The user can change the language and the caption must be in sync too.
      //
      if (getCaption() != null)
        this.setCaption(I18N.localizeLabel(getCaption(), context));
      super.calculateHTML(context);
      FastStringWriter oldWriter = getCache();
      Writer w = newCache();
      boolean atLeastOnIsVisible = false;
      Iterator actionsIter = getSelectionActions().iterator();
      while (actionsIter.hasNext())
      {
        ISelectionAction action = (ISelectionAction) actionsIter.next();
        if (action.isEnabled(context, this))
        {
          atLeastOnIsVisible = true;
          break;
        }
      }
      // Den Browser in ein DIV packen damit dieser einfach positioniert werden
      // kann.
      //
      this.cssStyleCache = null;
      if (atLeastOnIsVisible)
      {
        w.write("<div  style=\"overflow:hidden;border: 1px outset #cccccc;");
        getCSSStyle(context, w, boundingRectWithAction);
        w.write("\">");
        oldWriter.writeTo(w);
        w.write("</div>");
        // Combobox für die Action rausschreiben
        //
        writeActionHTML(context, w);
      }
      else
      {
        w.write("<div style=\"overflow:hidden;border: 1px outset #cccccc;");
        getCSSStyle(context, w, boundingRect);
        w.write("\">");
        oldWriter.writeTo(w);
        w.write("</div>");
      }
    }
  }

  /**
   * Writes the HTML content to the stream
   * 
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    writeCache(w);
    super.writeHTML(context, w);
    context.addOnLoadJavascript("var browser_" + this.getId() + " = new jACOBTable('" + this.getId() + "');");
    // the selectable list of the action combo box
    //
    context.addComboboxAdditionalHTML(externalHtml.toString());
    // Absolute Ausnahmebehandlung von Comboboxen.
    // Das Div der Liste einer Combobox muss am Ende des HTML stehen (z-index)
    // 
    if ((definition.isUpdatingEnabled() && (getDataStatus() == UPDATE || getDataStatus() == NEW) && getDataInternal().getGuiSortStrategy().getSelectedRecordIndex() >= 0) || (getDataStatus() == SEARCH))
    {
      for (int i = 0; i < inlineFields.size(); i++)
      {
        SingleDataGUIElement inline = (SingleDataGUIElement) inlineFields.get(i);
        if (inline instanceof ComboBox)
        {
          ComboBox combobox = (ComboBox) inline;
          context.addComboboxAdditionalHTML(new String(combobox.getExternalHtml()));
        }
        else if (inline instanceof Document)
        {
          // FREEGROUP: Jetzt haben wir schon die 2. Ausnahme, da für inline
          // Document field writeHTML() nicht gerufen wird.
          // Bitte generisch implementieren, damit wir in Zukunft nicht wieder
          // in dieselbe Falle tappen.
          //
          context.addOnLoadJavascript("initBehaviourDocument('" + inline.getId() + "');");
        }
      }
    }
  }

  /**
   * Das Anzeigen der Checkboxen macht nur Sinn wenn mind. 1 Action sichtbar ist
   * oder der Browser einen Eventhandler vom Typ SelectionListener implementiert
   * hat.
   * 
   * @return
   */
  public boolean hasMultiselect(IClientContext context)
  {
    boolean atLeastOnIsVisible = false;
    Iterator actionsIter = getSelectionActions().iterator();
    while (actionsIter.hasNext())
    {
      ISelectionAction action = (ISelectionAction) actionsIter.next();
      if (action.isEnabled(context, this))
      {
        atLeastOnIsVisible = true;
        break;
      }
    }
    return (atLeastOnIsVisible == true || getEventHandler(context) instanceof ISelectionListener) && getDataInternal().recordCount() > 0;
  }

  /**
   * Return HTML representation of this object
   * 
   */
  private void writeActionHTML(ClientContext context, Writer sw) throws Exception
  {
    String hashCode = Integer.toString(getId());
    if ("Button Bar".equals(definition.getProperty("action_representation")))
    {
      sw.write("<div class=\"informbrowser_buttonbar\" style=\"position:absolute;top:");
      sw.write(Integer.toString((int) (boundingRectWithAction.getY() + boundingRectWithAction.getHeight())));
      sw.write("px;left:");
      sw.write(Integer.toString((int) (boundingRectWithAction.getX())));
      sw.write("px;width:");
      sw.write(Integer.toString((int) (boundingRectWithAction.getWidth() + 2)));
      sw.write("px\">");
      Iterator actionsIter = getSelectionActions().iterator();
      int i = 0;
      while (actionsIter.hasNext())
      {
        ISelectionAction action = (ISelectionAction) actionsIter.next();
        if (action.isEnabled(context, this))
        {
          sw.write("<img title=\"" + StringUtil.htmlEncode(action.getLabel(context)) + "\" style=\"cursor:pointer;margin:3px;float:left\" ");
          sw.write(" onclick=\"FireEvent('" + hashCode + "','" + action.getId() + "');\"");
          sw.write(" src=\"" + action.getIcon(context).getPath(true) + "\"");
          sw.write(" >");
        }
      }
      sw.write("</div>\n");
    }
    else
    {
      // eine Combobox rausschreiben welche die Action anzeigt.
      sw.write("<div style=\"position:absolute;top:");
      sw.write(Integer.toString(boundingRectWithAction.y + boundingRectWithAction.height + 4));
      sw.write("px;left:");
      sw.write(Integer.toString(boundingRectWithAction.x));
      sw.write("px;width:");
      sw.write(Integer.toString(boundingRectWithAction.width));
      sw.write("px\"><img style=\"float:left\" src=\"");
      sw.write(((ClientSession) context.getSession()).getTheme().getImageURL("row_action_link.png"));
      sw.write("\"><table class=\"Multiselection\" id=\"container_");
      sw.write(comboEtrHashCode);
      sw.write("\" name=\"container_");
      sw.write(comboEtrHashCode);
      sw.write("\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;");
      sw.write("width:150px;height:20px;float:left\" border=\"0\"><tr><td class=\"Multiselection_Title\" >");
      sw.write("<input style=\"width:100%;\" readonly value=\"");
      if (this.lastSelectionEvent != null)
        sw.write(StringUtil.htmlEncode(this.lastSelectionEvent.getLabel(context)));
      sw.write("\"  type=\"text\" class=\"Multiselection_Title\" onclick=\"Multiselection_doMenuClick(this);\" onmouseout=\"Multiselection_doMenuMouseOut(this);\" ");
      sw.write(" multiselect=\"false\" id=\"");
      sw.write(comboEtrHashCode);
      sw.write("\" name=\"");
      sw.write(comboEtrHashCode);
      sw.write("\" submenu=\"combolist_");
      sw.write(comboEtrHashCode);
      sw.write("\" container=\"container_");
      sw.write(comboEtrHashCode);
      sw.write("\" ></td><td ");
      sw.write("onclick=\"Multiselection_doMenuClick(getObj('");
      sw.write(comboEtrHashCode);
      sw.write("'));\" onmouseout=\"Multiselection_doMenuMouseOut(getObj('");
      sw.write(comboEtrHashCode);
      sw.write("'));\" ");
      sw.write("class=\"combobox_trigger\" >&nbsp;</td></tr></table>\n");
      // ActionButton für die ausgewaehlte Aktion rausschreiben
      sw.write("<button id='" + buttonEtrHashCode + "' onclick=\"");
      if (this.lastSelectionEvent != null)
        sw.write("FireEvent('" + hashCode + "','" + this.lastSelectionEvent.getId() + "');return false;\"");
      else
        sw.write("return false;\"");
      sw.write(" class=\"button_normal\" style=\"height:20px;\" >");
      sw.write(I18N.getCoreLocalized("BUTTON_COMMON_EXECUTE", context));
      sw.write("</button></div>\n");
      externalHtml = new FastStringBuffer();
      externalHtml.append("\n<div style='width:120px;position:absolute;display:none;' nowrap='nowrap' onmouseover='Multiselection_doClearMenuTimer(this);' onmouseout='Multiselection_doStartMenuTimer(this);' id='combolist_" + comboEtrHashCode
          + "' parent='" + comboEtrHashCode + "' >\n");
      externalHtml.append("<table class='combobox_pane' width='120' cellspacing='0' style='position:static;' cellpadding='0' >\n");
      Iterator actionsIter = getSelectionActions().iterator();
      int i = 0;
      while (actionsIter.hasNext())
      {
        ISelectionAction action = (ISelectionAction) actionsIter.next();
        externalHtml.append("<tr>");
        String fireEvent = "$(\"" + buttonEtrHashCode + "\").onclick= function(){FireEvent(\"" + hashCode + "\",\"" + action.getId() + "\");return false;}";
        externalHtml.append("<td style='white-space:nowrap;' checked='false'  displayItem=");
        externalHtml.append(comboEtrHashCode);
        externalHtml.append(" onselectstart='return false;' id=\"item_" + i++);
        externalHtml.append("_");
        externalHtml.append(comboEtrHashCode);
        externalHtml.append("\" ");
        externalHtml.append("onmouseover=\"$(this).addClassName('combobox_option_over');\" onmouseout=\"$(this).removeClassName('combobox_option_over');\"' class='combobox_option' ");
        externalHtml.append(" value='");
        externalHtml.append(action.getLabel(context));
        externalHtml.append("' onclick='Multiselection_doItemCheck(this);");
        externalHtml.append(fireEvent);
        externalHtml.append("' >");
        externalHtml.append(StringUtil.htmlEncode(action.getLabel(context)));
        externalHtml.append("&nbsp;</td>");
        externalHtml.append("</tr>\n");
      }
      externalHtml.append("</table>");
      externalHtml.append("</div>\n");
    }
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
    if (getDataStatus() == UPDATE || getDataStatus() == NEW)
    {
      if (definition.isAddingEnabled())
      {
        w.write("<div class=\"sbc_add\" title=\"");
        w.write(I18N.getCoreLocalized("TOOLTIP_ADD_NEW_ROW", context));
        w.write("\" onClick=\"FireEventData('");
        w.write(Integer.toString(getId()));
        w.write("','");
        w.write(BrowserAction.ACTION_ADD.getId());
        w.write("','" + column + "');\" >&nbsp;</div>");
      }
    }
    // Der InformBrowser ist im NORMAL Zustand. Der Browser richtet eine
    // Such-Zeile ein
    //
    else if (getDataStatus() == SEARCH && definition.isSearchEnabled())
    {
      SingleDataGUIElement inline = (SingleDataGUIElement) inlineFields.get(column);
      if (inline != null)
      {
        inline.calculateHTML((ClientContext) context);
        inline.writeCache(w);
      }
    }
    else
    {
      super.writeFooterCellContent(context, w, column);
    }
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
    if (sortable == false)
      return super.getHeaderCellContent(context, column);
    Writer w = new StringWriter(512);
    // Die Sortier-Aktionen machen nur Sinn, wenn mehr als ein Eintrag vorhanden
    // ist und
    // wenn sich der Browser im selected Modus befindet.
    if (getDataStatus() == SELECTED && getData().recordCount() > 1 && !((ClientContext) context).isInReportMode())
    {
      String arrow = "";
      String action = BrowserAction.ACTION_SORT_ASC.getId();
      SortOrder sortOrder = getDataInternal().getGuiSortStrategy().isColumnInvolved(column);
      if (sortOrder == SortOrder.ASCENDING)
      {
        arrow = "<span class=\"sortarrow\">&darr;</span>";
        action = BrowserAction.ACTION_SORT_DESC.getId();
      }
      else if (sortOrder == SortOrder.DESCENDING)
      {
        arrow = "<span class=\"sortarrow\">&uarr;</span>";
        action = BrowserAction.ACTION_SORT_NONE.getId();
      }
      else if (sortOrder == SortOrder.NONE)
      {
        arrow = "";
        action = BrowserAction.ACTION_SORT_ASC.getId();
      }
      w.write("<div  style=\"width:100%\" onClick=\"FireEventData('");
      w.write(Integer.toString(getId()));
      w.write("','");
      w.write(action);
      w.write("','");
      w.write(Integer.toString(column));
      w.write("')\">");
      w.write(super.getHeaderCellContent(context, column));
      w.write(arrow);
      w.write("</div>");
    }
    else if (((ClientContext) context).isInReportMode())
    {
      w.write("<div  style=\"width:100%\" onClick=\"FireEventData('");
      w.write(Integer.toString(getId()));
      w.write("','");
      w.write(BrowserAction.ACTION_ADD_COLUMN.getId());
      w.write("','");
      w.write(Integer.toString(column));
      w.write("')\">");
      w.write(super.getHeaderCellContent(context, column));
      w.write("</div>");
    }
    else
    {
      w.write(super.getHeaderCellContent(context, column));
    }
    return w.toString();
  }

  /**
   * If the group in the search (SEARCH) modus the InformBrowser must insert
   * input fields in the columns. Required for the search.
   */
  protected boolean writeCellContent(IClientContext context, Writer w, IBrowserEventHandler browserFilter, IDataBrowserRecord record, boolean isFirstVisibleColumn, int row, int column, boolean handleExceptions) throws Exception
  {
    // Der Informbrowser ist im Update Modus und eine Zeile muß in den
    // Edit-State geschaltet
    // und angezeigt werden.
    //
    if (definition.isUpdatingEnabled() && (getDataStatus() == UPDATE || getDataStatus() == NEW) && getDataInternal().getGuiSortStrategy().getSelectedRecordIndex() == row)
    {
      SingleDataGUIElement inline = (SingleDataGUIElement) inlineFields.get(column);
      if (inline != null)
      {
        inline.calculateHTML((ClientContext) context);
        inline.writeCache(w);
      }
      return true;
    }
    else
    {
      return super.writeCellContent(context, w, browserFilter, record, isFirstVisibleColumn, row, column, handleExceptions);
    }
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
    if ((row == getDataInternal().getGuiSortStrategy().getSelectedRecordIndex() && (getDataStatus() == UPDATE || getDataStatus() == NEW)) || getDataStatus() == SEARCH)
      return "sbr_u";
    return super.getRowCssClass(record, row);
  }

  /**
   * 
   * @param index
   *          The zero based index to select or -1 to unselect.
   */
  public void setSelectedRecordIndex(IClientContext context, int index) throws Exception
  {
    if (index != getDataInternal().getGuiSortStrategy().getSelectedRecordIndex() && (getDataStatus() == UPDATE || getDataStatus() == NEW))
    {
      resetCache();
      IDataTransaction trans = context.getDataTable().getTableTransaction();
      // es zuvor schon ein Record im Update modus. Diese Werte müssen jetzt in
      // den DataTableRecord
      // eingetragen werden da der zu editierende Record sich jetzt ändert.
      //
      if (getDataInternal().getGuiSortStrategy().getSelectedRecordIndex() >= 0)
      {
        IDataTableRecordInternal record = getDataInternal().getRecordToUpdate(trans, getDataInternal().getGuiSortStrategy().getSelectedRecordIndex());
        // do not set values for a record which has been just deleted
        if (!record.isDeleted())
        {
          for (int i = 0; i < inlineFields.size(); i++)
          {
            SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
            if (element instanceof InFormBrowserForeignField)
            {
              /*
               * FromTable ToTable --------- ----------- | | 1 0-n | | | |
               * -------------> | | | | RelationToUse | | --------- -----------
               */
              InFormBrowserForeignField ffInline = (InFormBrowserForeignField) element;
              IDataTableRecord dataTableRecord = getData().getRecord(getDataInternal().getGuiSortStrategy().getSelectedRecordIndex()).getTableRecord();
              if (ffInline.getDisplayRecord(context) == null)
                dataTableRecord.resetLinkedRecord(trans, ffInline.getFromTable());
              else
                dataTableRecord.setLinkedRecord(trans, ffInline.getDisplayRecord(context));
            }
            else if (element != null)
            {
              DataField field = element.getDataField();
              // es können im Informbrowser auf readonly Fremdfelder enthalten
              // sein
              if (record.getTableAlias().equals(field.getTableAlias()))
              {
                Object value = field.getValue();
                // empty fields == null fields in the database
                if (value != null && value instanceof String && ((String) value).length() == 0)
                  value = null;
                record.setValue(trans, field.getField(), value, context.getLocale());
              }
            }
          }
        }
      }
      getData().clearSelections();
      // Den neuen Record holen und in die inline felder füllen
      //
      if (index >= 0)
      {
        getDataInternal().getGuiSortStrategy().setSelectedRecordIndex(index);
        IDataTableRecord record = getDataInternal().getGuiSortStrategy().getRecordToUpdate(trans, index);
        for (int i = 0; i < inlineFields.size(); i++)
        {
          SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
          if (element instanceof InFormBrowserForeignField)
          {
            InFormBrowserForeignField ffInline = (InFormBrowserForeignField) element;
            IDataTableRecord informRecord = getDataInternal().getGuiSortStrategy().getRecord(index).getTableRecord();
            IDataTableRecord foreignRecord = informRecord.getLinkedRecord(ffInline.getFromTable());
            ffInline.setValue(context, foreignRecord);
          }
          else if (element != null)
          {
            element.onGroupDataStatusChanged(context, UPDATE);
            element.setValue(context, record);
          }
        }
      }
    }
    else
      super.setSelectedRecordIndex(context, index);
  }

  /**
   * @param inlineFields
   *          The inlineFields to set.
   */
  public void addInlineField(IGuiElement element)
  {
    inlineFields.add(element);
  }

  /**
   * 
   */
  protected void addDataFields(Vector fields)
  {
    GroupState dataStatus = getDataStatus();
    if (((dataStatus == UPDATE || dataStatus == NEW) && (this.definition.isUpdatingEnabled() || this.definition.isAddingEnabled())) //
        || (dataStatus == SEARCH && this.definition.isSearchEnabled()))
    {
      for (int i = 0; i < inlineFields.size(); i++)
      {
        SingleDataGUIElement element = (SingleDataGUIElement) inlineFields.get(i);
        if (element != null)
          element.addDataFields(fields);
      }
    }
  }

  public InFormBrowserDefinition getInFormBrowserDefinition()
  {
    return definition;
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
