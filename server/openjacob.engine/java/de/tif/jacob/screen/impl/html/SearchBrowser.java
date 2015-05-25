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
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.screen.impl.TwoPhaseSelectionAction;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

/**
 * 
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SearchBrowser extends Browser
{
  static public final transient String RCS_ID = "$Id: SearchBrowser.java,v 1.40 2011/07/01 21:24:06 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.40 $";

  private final Group relatedGroup;
  private final String comboEtrHashCode  = getEtrHashCode(IDProvider.next());
  private final String buttonEtrHashCode = getEtrHashCode(IDProvider.next());
  
  private boolean isEmitterOfLastEvent=false;
  
  /**
	 * @param name
	 * @param label
	 * @param isInvisibleString
	 */
	protected SearchBrowser(IApplication app,  IBrowserDefinition browser, Group group)
	{
		super(app,  browser, null);
		relatedGroup = group;
	}
  
  protected void initHeaderActions()
  {
    // Inherit common actions
    super.initHeaderActions();

    // Die Aktionen machen bei einer Baumdarstellung keinen Sinn.
    //
    if(this.definition.getConnectByKey()==null)
    {
      // and add search browser specific actions
      addHeaderAction(BrowserAction.ACTION_REFRESH);         // refresh the SearchBrowser with the last SearchCriteria
      addHeaderAction(BrowserAction.ACTION_UNLIMITED_SEARCH); // make a search with an unlimited RowCount 
      addHeaderAction(BrowserAction.ACTION_ADD_CONSTRAINTS); // the last search constraint can be saved as bookmark 
      addHiddenAction(BrowserAction.ACTION_SORT_DESC);
      addHiddenAction(BrowserAction.ACTION_SORT_ASC);
      addHiddenAction(BrowserAction.ACTION_SORT_NONE);
    }
    
    addHiddenAction(BrowserAction.ACTION_SHOW_BROWSER);            // make the SearchBrowser visible
    addHiddenAction(BrowserAction.ACTION_CLICK_AND_CONFIRM); // The user clicks in a row. This is not a action with a icon
  }

  protected int getEmptyRowCount()
  {
    return Math.max(0,20-this.getData().recordCount());
  }

  
  /**
	 * @return Returns the relatedGroup.
	 * @deprecated getGroup liefert das gleiche Ergebnis
	 */
	public IGroup getRelatedGroup()
	{
	  return relatedGroup;
	}

	
  /**
   * @return Returns the anchor table of the search browser
   */
  public String getGroupTableAlias()
  {
    return relatedGroup.getGroupTableAlias();
  }

  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception {
		// Der SearchBrowser muss sich merken ob das Event von Ihm kamm
  	// Wenn ja, dann wird die Keyboardnavigation aktiviert
  	//
  	isEmitterOfLastEvent= super.processEvent(context, guid, event, value);

    return isEmitterOfLastEvent;
	}

  public boolean isVisible()
  {
    if(getRelatedGroup().getHideEmptyBrowser())
      return getData().recordCount()>0 && super.isVisible();

      return super.isVisible();
  }

  /**
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!context.getApplication().isSearchBrowserVisible(context))
      return;
   
    boolean calc = getCache()==null;
    IDataBrowserInternal data = getDataInternal();
    super.calculateHTML(context);
    if(calc && getCache()!=null)
    {
      String hashCode = Integer.toString(getId());
      FastStringWriter writer = new FastStringWriter();
      boolean hasErrorOrWarning = false;
      writer.write("<table id=\"error_bar\" cellspacing=\"0\" cellpadding=\"0\" >");
      for(int row=0;row<data.recordCount();row++)
      {
        IDataBrowserRecord record = getDataRecord(row);
        Marker marker = (Marker)record2marker.get(record);
        if(marker!=null)
        {
          hasErrorOrWarning = hasErrorOrWarning|| marker.error!=null || marker.warning!=null;
          if(marker.error!=null)
          {
            writer.write("<tr><td><div onclick=\"jACOBTable_scrollRowToTop('");
            writer.write(hashCode);
            writer.write("',");
            writer.write(Integer.toString(row));
            writer.write(")\" title=\"");
            writer.write(marker.error);
            writer.write("\" class='error_cell'> </div></td></tr>");
          }
          else if(marker.warning!=null)
          {
            writer.write("<tr><td><div onclick=\"jACOBTable_scrollRowToTop('");
            writer.write(hashCode);
            writer.write("',");
            writer.write(Integer.toString(row));
            writer.write(")\" title=\"");
            writer.write(marker.warning);
            writer.write("\" class='warning_cell'> </div></td></tr>");
          }
          else
          {
            writer.write("<tr><td></td></tr>");
          }
        }
        else
        {
          writer.write("<tr><td></td></tr>");
        }
      }
      writer.write("</table>");
      if(hasErrorOrWarning)
        writer.writeTo(getCache());
    }
  }
  
  protected void writeFooterCellContent(IClientContext context, Writer w, int column) throws Exception
  {
    w.write("&nbsp;");
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);

    super.writeHTML(context,w);
    context.addAdditionalHTML("<script>var searchBrowserId = '"+this.getId()+"';</script>");
    
    // Falls der User auf den Browser geklickt hat, dann kann man im Browser mit 
    // der Tastatur navigieren.
    //
    if(isEmitterOfLastEvent==true)
    {
      context.addAdditionalHTML("\n<script  type=\"text/javascript\">");
      context.addAdditionalHTML("addKeyHandler(document.body);");
      context.addAdditionalHTML("document.body.addKeyDown("+KEYCODE_DOWN+",function(){onCursorDownKey('"+getId()+"')});");
      context.addAdditionalHTML("document.body.addKeyDown("+KEYCODE_UP+",function(){onCursorUpKey('"+getId()+"')});");
      context.addAdditionalHTML("document.body.addKeyDown("+KEYCODE_ENTER+",function(){onEnter('"+getId()+"')});");
      context.addAdditionalHTML("</script>\n");
    }
    isEmitterOfLastEvent=false;
  }
  
  /**
   * Return HTML representation of this object
   * 
   */
  public void writeActionHTML(ClientContext context) throws Exception
  {
    if(!hasMultiselect(context))
      return;
    
    FastStringWriter sw = new FastStringWriter();

    String hashCode = Integer.toString(getId());

    if(this.runningSelectionEvent==null)
    {
      if(this.lastSelectionEvent!=null && this.lastSelectionEvent.isVisible(context, this)==false)
        this.lastSelectionEvent = null;
      
      // eine Combobox rausschreiben welche die Action anzeigt.
      sw.write("<div style=\"position:relative;top:3px;width:100%\"><img style=\"float:left\" src=\"");
      sw.write(((ClientSession)context.getSession()).getTheme().getImageURL("row_action_link.png"));
      sw.write("\"><table class=\"Multiselection\" id=\"container_");
      sw.write(comboEtrHashCode);
      sw.write("\" name=\"container_");
      sw.write(comboEtrHashCode);
      sw.write("\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;");
      sw.write("width:200px;height:20px;float:left\" border=\"0\"><tr><td class=\"Multiselection_Title\" >");
      sw.write("<input style=\"width:100%;\" readonly value=\"");
      if(this.lastSelectionEvent!=null)
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
      sw.write("<button id='"+buttonEtrHashCode+"' onclick=\"");
      if(this.lastSelectionEvent!=null)
        sw.write("FireEvent('"+hashCode+"','"+this.lastSelectionEvent.getId()+"');return false;\"");
      else
        sw.write("return false;\"");
      sw.write(" class=\"button_normal\" style=\"height:20px;\" >");
      sw.write(I18N.getCoreLocalized("BUTTON_COMMON_EXECUTE", context));
      sw.write("</button>");
      
      int selectionCount = this.getSelection().size();
      if(selectionCount>0)
        sw.write("<span class=\"selection_action_counter\"> ( "+selectionCount+" / "+this.getData().recordCount()+" )</span></div>\n");
      else
        sw.write("<span class=\"selection_action_counter\"></span></div>\n");
      
      sw.write("</div>\n");
    
      // Die Comboboxeinträge raus schreiben
      //
      StringBuffer externalHtml = new StringBuffer();
      String width="200";
      
      externalHtml.append("\n<div style='z-index:1000;min-width:"+width+"px;position:absolute;display:none;' nowrap='nowrap' onmouseover='Multiselection_doClearMenuTimer(this);' onmouseout='Multiselection_doStartMenuTimer(this);' ");
      externalHtml.append(" id='combolist_"+comboEtrHashCode+"' parent='"+comboEtrHashCode+"' >\n");
      externalHtml.append("<table class='combobox_pane' width='"+width+"' cellspacing='0' style='position:static;min-width:"+width+"px;width:"+width+"px' cellpadding='0' >\n");
  
      int i=0;
      for (ISelectionAction action : getSelectionActions())
      {
        externalHtml.append("<tr id=\""+action.getId());
        externalHtml.append("\" ");
        if(!action.isVisible(context, this))
          externalHtml.append("style=\"display: none;\"");
        externalHtml.append(">");
        
        String fireEvent="$(\""+buttonEtrHashCode+"\").onclick= function(){FireEvent(\""+hashCode+"\",\""+action.getId()+"\");return false;}";
  
        externalHtml.append("<td style='white-space:nowrap;' checked='false'  displayItem=");
        externalHtml.append(comboEtrHashCode);
        externalHtml.append(" onselectstart='return false;' id=\"item_"+i);
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
        i++;
      }
        
      externalHtml.append("</table>");
      externalHtml.append("</div>\n");
      context.addAdditionalHTML(externalHtml.toString());
    }
    else
    {
      sw.write("<div style=\"position:relative;top:3px;width:100%\"><img style=\"float:left\" src=\"");
      sw.write(((ClientSession)context.getSession()).getTheme().getImageURL("row_action_link.png"));
      sw.write("\">");
      sw.write("<input style='width:"+width+"px;' type='text' disabled class='text_disabled' value='"+this.runningSelectionEvent.getLabel(context)+"'>");
      sw.write("&nbsp;<button onclick=\"");
      sw.write("FireEventData('"+hashCode+"','"+this.runningSelectionEvent.getId()+"','"+TwoPhaseSelectionAction.SAVE+"');return false;\"");
      sw.write(" class=\"button_emphasize_normal button_default\" isDefault=\"true\" style=\"height:20px;\" >");
      sw.write(I18N.getCoreLocalized("BUTTON_COMMON_SAVE", context));
      sw.write("</button>&nbsp;<span class=\"caption_normal_update\">");
      sw.write(I18N.getCoreLocalized("LABEL_OR",context));
      sw.write("</span>&nbsp;");
      sw.write("<button onclick=\"");
      sw.write("FireEventData('"+hashCode+"','"+this.runningSelectionEvent.getId()+"','"+TwoPhaseSelectionAction.CANCEL+"');return false;\"");
      sw.write(" class=\"button_normal\" style=\"height:20px;\" >");
      sw.write(I18N.getCoreLocalized("BUTTON_COMMON_CANCEL", context));
      sw.write("</button>");
  
      int selectionCount = this.getSelection().size();
      if(selectionCount>0)
        sw.write("<span class=\"selection_action_counter\"> ( "+selectionCount+" / "+this.getData().recordCount()+" )</span></div>\n");
      else
        sw.write("<span class=\"selection_action_counter\"></span></div>\n");
      }
    sw.writeTo(context.out);
  }  

  protected String getContextMenuFunction(ClientContext context)
  {
    return relatedGroup.contextMenu.getContextMenuFunction();
  }

  /**
   * Zeige einen Hinweistext wenn nach einer Suche keine Records gefunden wurden
   *
   */
  protected boolean showStatusMessage()
  {
    return true;
  }
  
  /**
   * 
   */
  public String getLabel()
  {
    if(parent!=null)
      return parent.getLabel();
    return super.getLabel();
  }
  
  public String getI18NLabel(Locale locale)
  {
    if(parent!=null)
    {
      // do not get <"TITLE_GROUP_"+state> label from group
      return I18N.localizeLabel(getLabel(),this.getApplicationDefinition(), locale);
    }
    return super.getI18NLabel(locale);
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
    if(sortable==false)
      return super.getHeaderCellContent(context,column);
    
    String arrow="";
    String action=BrowserAction.ACTION_SORT_ASC.getId();
    
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
    else if(sortOrder == SortOrder.NONE)
    {
      arrow = "";
      action = BrowserAction.ACTION_SORT_ASC.getId();
    }
    
    Writer w = new StringWriter(512);
    w.write("<div onClick=\"FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(action);
    w.write("','");
    w.write(Integer.toString(column));
    w.write("')\">");
    w.write(super.getHeaderCellContent(context,column));
    w.write(arrow);
    w.write("</div>");
    return w.toString();
  }
 
  /**
   * Set the current data of the guiBrowser.
   * 
   * @param data
   */
  public void setData(IClientContext context, IDataBrowser data)
  {
    try
    {
    	super.setData(context, data);
    	
      // select the one and only record immediatly
      //
      if(this.getData().recordCount()==1 && this.relatedGroup.isBrowserAutoSelect())
      	setSelectedRecordIndex(context, 0);
      // show the tab of the search browser
      //
      context.getForm().setCurrentBrowser(this);
      
      // Falls es vermieden werden soll einen leeren SearchBrowser anzzeigen, dann
      // wird der Hinweis, dass keine Records gefunden wurden als Dialog angezeigt.
      //
      if(this.getData().recordCount()==0 && getRelatedGroup().getHideEmptyBrowser())
        this.alert(context,"MSG_NO_RECORDS_FOUND");
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context,e);
    }
  }


  /**
   * Der Searchbrowser leitet die Anfrage nach einem Filter/Eventhandler an die entsprechende Gruppe
   * weiter. Man kann im Moment den SearchBrowser im GUI Designer nicht darstellen. Somit ist das
   * direkte anhängen eines Eventhandler nicht möglich.
   * Vorgehen:
   *  - Man erstellt im Designer an der Gruppe einen Eventhandler.
   *  - An dem Eventhandler implementiert die Methode zum erfragen den Browser Eventhandler
   *  
   */
  public String getEventHandlerReference()
  {
    return ((Group)getRelatedGroup()).getSearchBrowserEventHandlerReference();
  }

  /**
   * Falls ein Record aus dem SearchBrowser entfernt wird, dann sollte dieser
   * auch nicht mehr in der zugehörigen Gruppe sichtbar sein.
   */
  public void remove(IClientContext context, IDataBrowserRecord browserRecord) throws Exception
  {
    super.remove(context, browserRecord);
    String tableAlias = relatedGroup.getGroupTableAlias();
    IDataTable alias = context.getDataTable(tableAlias);
    IDataTableRecord currentRecord = alias.getSelectedRecord();
    if(currentRecord!=null && currentRecord.getPrimaryKeyValue().equals(browserRecord.getPrimaryKeyValue()))
      relatedGroup.clear(context,false);
  }  
  
  
}
