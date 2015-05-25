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
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.HTTPReportBrowser;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReportBrowser extends Browser implements HTTPReportBrowser
{
  static public final transient String RCS_ID = "$Id: ReportBrowser.java,v 1.12 2010/08/13 20:14:28 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.12 $";

  public static final transient String VAR_SUFFIX = "report_";
  public static final transient String VAR_PRIVATE = "isprivate";
  
  private static final String REPORT_NAME_TEXT_INPUT_ID = "report_name_text";
  private static final String REPORT_SAVE_BUTTON_ID = "report_save_button";
  private static final String REPORT_SAVE_AND_SHOW_BUTTON_ID = "report_save_and_show_button";
  
  private final AdhocBrowserDefinition browserDefinition;
  
  private String reportName = "";
  private boolean isPrivate = true;
  
  private final IRelationSet relationSet;
  private final String       anchorTable;
  
  private final IDataFieldConstraints startConstraints;
  
  /**
	 * @param name
	 * @param label
	 * @param isInvisibleString
	 */
	protected ReportBrowser(IApplication app, IClientContext context, String anchorTable, IRelationSet relationset, IDataFieldConstraints startConstraints) throws Exception
	{
		super(app, context.getApplicationDefinition().createAdhocBrowserDefinition(context.getApplicationDefinition().getTableAlias(anchorTable)), null);
		
    this.browserDefinition  = (AdhocBrowserDefinition)definition;
		this.data               = (IDataBrowserInternal) context.getDataAccessor().createBrowser(browserDefinition);

		if(context.getGUIBrowser()==null)
      throw new Exception("Unable to determine required GUI-SearchBrowser.");
    
    this.relationSet = relationset;
    if(this.relationSet==null)
      throw new Exception("Unable to determine required relation set.");
    
    this.anchorTable      = anchorTable;
    this.startConstraints = startConstraints;
    
		addHiddenAction(BrowserAction.ACTION_CANCEL_REPORT); 
    addHiddenAction(BrowserAction.ACTION_SAVE_REPORT);
    addHiddenAction(BrowserAction.ACTION_SAVE_SHOW_REPORT);
		addHiddenAction(BrowserAction.ACTION_DELETE_REPORT_COLUMN);
		addHiddenAction(BrowserAction.ACTION_SHIFT_COLUMN_LEFT);
		addHiddenAction(BrowserAction.ACTION_SHIFT_COLUMN_RIGHT);

    addHiddenAction(BrowserAction.ACTION_SORT_DESC);
    addHiddenAction(BrowserAction.ACTION_SORT_ASC);
    addHiddenAction(BrowserAction.ACTION_SORT_NONE);
	}

  protected int getEmptyRowCount()
  {
    return 0;
  }
	
	public void writeTabHTML(ClientContext context) throws IOException
	{
	  context.out.write("<div class=\"reporting_input_bar\"><table height=\"30\" border=\"0\"><tr><td valign=\"center\" width=\"1\"><span class=\"caption_normal_search\" style=\"white-space:nowrap;\">");
	  context.out.write(I18N.getCoreLocalized("LABEL_REPORTING_REPORTNAME", context));
    context.out.write("</span></td><td valign=\"center\" width=\"1\"><input id=\"" + REPORT_NAME_TEXT_INPUT_ID + "\" style=\"width:250;\" type=\"text\" class=\"text_normal editable_inputfield\" name='");
    context.out.write(VAR_SUFFIX+Integer.toString(reportName.hashCode()));
    context.out.write("' value='");
    context.out.write(reportName);
    context.out.write("'></td><td valign=\"center\">");
    
    context.out.write("<input type=\"checkbox\" name='");
    context.out.write(VAR_PRIVATE);
    context.out.write("' value='1'");
    if (this.isPrivate)
      context.out.write(" checked");
    context.out.write("><span class=\"caption_normal_search\">");
    context.out.write(I18N.getCoreLocalized("LABEL_REPORTING_PRIVATE", context));
    context.out.write("</span>&nbsp;");
    
    context.out.write("<button id=\"" + REPORT_SAVE_BUTTON_ID + "\" class=\"button_emphasize_normal button_default\" isDefault=\"true\" onClick=\"FireEvent('");
    context.out.write(Integer.toString(getId()));
    context.out.write("','"+BrowserAction.ACTION_SAVE_REPORT.getId()+"')\">");
    context.out.write(BrowserAction.ACTION_SAVE_REPORT.getI18NLabel(context));
    context.out.write("</button>&nbsp;");
    context.out.write("<button id=\"" + REPORT_SAVE_AND_SHOW_BUTTON_ID + "\" class=\"button_emphasize_normal button_default\" isDefault=\"false\" onClick=\"FireEvent('");
    context.out.write(Integer.toString(getId()));
    context.out.write("','"+BrowserAction.ACTION_SAVE_SHOW_REPORT.getId()+"')\">");
    context.out.write(BrowserAction.ACTION_SAVE_SHOW_REPORT.getI18NLabel(context));
    context.out.write("</button>&nbsp;");
    context.out.write("<button class=\"button_normal\" onClick=\"FireEvent('");
    context.out.write(Integer.toString(getId()));
    context.out.write("','"+BrowserAction.ACTION_CANCEL_REPORT.getId()+"')\">");
    context.out.write(BrowserAction.ACTION_CANCEL_REPORT.getI18NLabel(context));
    context.out.write("</button></td></tr>");
    // Einen Hinweistext anzeigne, wie man neue Felder in den Report mit aufnimmt
    
    //
    context.out.write("<tr><td class=\"reporting_message\" colspan=\"3\" >");
    // Replace \n\r with <br> => htmlEncode
    context.out.write(StringUtil.replace(I18N.getCoreLocalized("MSG_REPORTING_START",context),"\\r\\n","<br>"));
    context.out.write("</td></tr></table></div>");
	}
	
  public boolean processParameter(int guid, String data, boolean isPrivate) throws IOException, NoSuchFieldException
  {
    if (guid == this.reportName.hashCode())
    {
      this.reportName = data;
      this.isPrivate = isPrivate;
    }
    return true;
  }
  /**
   * 
   */
//  public void calculateHTML(ClientContext context) throws Exception
//  {
//    if(!context.getApplication().isSearchBrowserVisible(context))
//      return;
//    
//    super.calculateHTML(context);
//  }
//  
  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    writeCache(w);
    super.writeHTML(context, w);

    context.addOnLoadJavascript( //
        "Event.observe(\"" + REPORT_NAME_TEXT_INPUT_ID + "\",\"keydown\",function(event)\n" + //
            "{\n" + //
            "   if(event.keyCode === 13)\n" + //
            "   {\n" + //
            "      event.stop();\n" + //
            "      $(\"" + REPORT_SAVE_BUTTON_ID + "\").click();\n" + //
            "   }\n" + //
            "});\n");
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
      return parent.getI18NLabel(locale);
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
    IBrowserTableField field = (IBrowserTableField) definition.getBrowserField(column);
    
    String arrow  = "";
    String action = BrowserAction.ACTION_SORT_ASC.getId();

    SortOrder sortOrder = field.getSortorder();

    if (sortOrder == SortOrder.ASCENDING)
    {
      arrow = "<span class=\"sortarrow\">&uarr;</span>";
      action =BrowserAction.ACTION_SORT_DESC.getId();
    }
    else if (sortOrder == SortOrder.DESCENDING)
    {
      arrow = "<span class=\"sortarrow\">&darr;</span>";
      action = BrowserAction.ACTION_SORT_NONE.getId();
    }
    else if(sortOrder == SortOrder.NONE)
    {
      arrow = "";
      action = BrowserAction.ACTION_SORT_ASC.getId();
    }
    
    String c = Integer.toString(column);
    Writer w = new StringWriter(512);
    w.write("<div style=\"width:100%\"");
    w.write(" onClick=\"FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(action);
    w.write("','");
    w.write(c);
    w.write("')\"");
    w.write(">");
    w.write(((IBrowserField) getColumns().get(column)).getLabel());
    w.write(arrow);
    w.write("</div>");
    
    writeColumnAction(context,w,BrowserAction.ACTION_DELETE_REPORT_COLUMN,c);
    if(column>0)
      writeColumnAction(context,w,BrowserAction.ACTION_SHIFT_COLUMN_LEFT,c);
    if((column+1)<getBrowserTableFields().size())
      writeColumnAction(context,w,BrowserAction.ACTION_SHIFT_COLUMN_RIGHT,c);
    
    return w.toString();
  }
 

  private void writeColumnAction(IClientContext context, Writer w, BrowserAction action, String c) throws IOException
  {
    w.write("<a href='#' ><img src=\"");
    Icon icon = action.getIcon(context);
    if(icon!=null)
      w.write(icon.getPath(true));
    else
      w.write(((ClientSession)context.getSession()).getTheme().getImageURL(action.getIcon()));
    w.write("\" ");
    w.write(" onClick=\"FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(action.getId());
    w.write("','");
    w.write(c);
    w.write("')\"");
    w.write(" border=\"0\" ></a>");
  }
  
  public void addColumn(String alias, String column, String label) throws NoSuchFieldException
  {
    addColumn(alias,column,label, SortOrder.NONE);
  }
  
  public void addColumn(String alias, String column, String label, SortOrder order) throws NoSuchFieldException
  {
    browserDefinition.addBrowserField(alias,column,label, order);
    resetCache();
  }
  
  public void addColumn(int index, IBrowserTableField field) throws NoSuchFieldException
  {
    browserDefinition.addBrowserField(index, field);
    resetCache();
  }
  

  public IBrowserTableField removeColumn(int index) throws NoSuchFieldException
  {
    resetCache();
    return browserDefinition.removeBrowserField(index);
  }
  

  public void sort(int index , SortOrder order) throws NoSuchFieldException
  {
    IBrowserTableField field = (IBrowserTableField)browserDefinition.getBrowserFields().get(index);
    String label  = field.getLabel();
    String column = field.getTableField().getName();
    String alias  = field.getTableAlias().getName();

    
    browserDefinition.removeBrowserField(index);
    browserDefinition.addBrowserField(index,alias,column,label,order);
    resetCache();
  }
  
  public void addSelectionAction(ISelectionAction action)
  {
  }


  public void removeSelectionAction(ISelectionAction action)
  {
  }
  
  /**
   * 
   * @return List[IBrowserTableField]
   */
  public List getBrowserTableFields()
  {
    return browserDefinition.getBrowserFields();
  }
  
  public String getReportName()
  {
    return reportName;
  }
  
  public String getAnchorTable()
  {
    return anchorTable;
  }
  
  public IRelationSet getRelationSet()
  {
    return relationSet;
  }
  
  public Filldirection getFilldirection()
  {
    return Filldirection.BOTH;
  }
  
  public boolean isPrivate()
  {
    return isPrivate;
  }

  protected void setPrivate(boolean isPrivate)
  {
    this.isPrivate = isPrivate;
  }

  protected void setReportName(String reportName)
  {
    this.reportName = reportName;
  }
  
  public IDataFieldConstraints getStartConstraints()
  {
    return startConstraints;
  }
  
  public String getEventHandlerReference()
  {
    return null;
  }

  public boolean isColumnConfigureable(int columnIndex)
  {
    return true;
  }

  public boolean isColumnVisible(int columnIndex)
  {
    return true;
  }
}
