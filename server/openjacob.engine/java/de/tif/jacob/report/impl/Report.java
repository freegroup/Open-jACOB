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
package de.tif.jacob.report.impl;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.castor.CastorCaption;
import de.tif.jacob.report.impl.castor.CastorLayout;
import de.tif.jacob.report.impl.castor.CastorLayoutColumn;
import de.tif.jacob.report.impl.castor.CastorLayoutColumns;
import de.tif.jacob.report.impl.castor.CastorLayoutPart;
import de.tif.jacob.report.impl.castor.CronRule;
import de.tif.jacob.report.impl.castor.Layouts;
import de.tif.jacob.report.impl.castor.Notifyee;
import de.tif.jacob.report.impl.castor.ReportDefinition;
import de.tif.jacob.report.impl.castor.SearchCriteria;
import de.tif.jacob.report.impl.castor.types.SortOrderType;
import de.tif.jacob.report.impl.transformer.IReportDataIterator;
import de.tif.jacob.report.impl.transformer.IReportDataRecord;
import de.tif.jacob.report.impl.transformer.Transformer;
import de.tif.jacob.report.impl.transformer.base.FormattedTextTransformer;
import de.tif.jacob.scheduler.iterators.CronEntry;
import de.tif.jacob.searchbookmark.AbstractSearchConstraint;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 * 
 */
public abstract class Report extends AbstractSearchConstraint implements ILayoutReport
{
  static public final transient String RCS_ID = "$Id: Report.java,v 1.18 2010/04/27 12:08:18 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.18 $";
  static public final transient Log logger = LogFactory.getLog(Report.class);
  private int lastRecordCount = -1;
  private long realRecordCount = -1;

  protected Report(ReportDefinition def) throws Exception
  {
    super(def);
  }

  /**
   * 
   * 
   * @author Andreas Herz
   */
  protected Report(String name, String appName, String domainName, String formName, String version, String relationSet, String mainTableAlias)
  {
    super(name, appName, domainName, formName, version, relationSet, mainTableAlias);
  }

  public void addColumn(String tableAlias, String field, String label)
  {
    addColumn(tableAlias, field, label, SortOrder.NONE);
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#addColumn(java.lang.String,
   * java.lang.String)
   */
  public void addColumn(String tableAlias, String field, String label, SortOrder order)
  {
    de.tif.jacob.report.impl.castor.Column column = new de.tif.jacob.report.impl.castor.Column();
    column.setTableAlias(tableAlias);
    column.setField(field);
    column.setLabel(label);
    column.setSortOrder(SortOrderType.valueOf(order.toString()));
    searchConstraint.getOutput().addColumn(column);
  }

  public void addColumn(IReport.Column column)
  {
      addColumn(column.table, column.field, column.label, column.order);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#getRecordCount()
   */
  public final int getRecordCount()
  {
    return lastRecordCount;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#getRealRecordCount()
   */
  public long getRealRecordCount()
  {
    return this.realRecordCount;
  }

  /*
   * 
   * @see de.tif.jacob.report.IReport#isPrivate()
   */
  public boolean isPrivate()
  {
    return searchConstraint.getPrivate();
  }

  public boolean isScheduled()
  {
    return searchConstraint.getNotifyeeCount() > 0;
  }

  public void setPrivate(boolean flag)
  {
    searchConstraint.setPrivate(flag);
  }

  /**
   * Render the report to the default mimeType
   */
  public void render(OutputStream out) throws Exception
  {
    render(out, getDefaultMimeType(), null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#render(java.io.OutputStream,
   * java.lang.String)
   */
  public void render(OutputStream out, String mimeType) throws Exception
  {
    render(out, mimeType, null);
  }

  public IDataBrowserInternal fetchData(int maxRecords) throws Exception
  {
    IApplicationDefinition app = getApplication();
    
    DataAccessor accessor = new DataAccessor(app);
    // create a customizable DataBrowser
    //
    IAdhocBrowserDefinition browserDef = app.createAdhocBrowserDefinition(app.getTableAlias(getAnchorTable()));
    IDataBrowserInternal browser = (IDataBrowserInternal) accessor.createBrowser(browserDef);
    // enter search criteria for the browser
    //
    for (int i = 0; i < searchConstraint.getInput().getSearchCriteriaCount(); i++)
    {
      SearchCriteria criteria = searchConstraint.getInput().getSearchCriteria(i);
      accessor.getTable(criteria.getTableAlias()).qbeSetValue(criteria.getField(), criteria.getValue());
    }
    // define related table and fields
    for (int i = 0; i < searchConstraint.getOutput().getColumnCount(); i++)
    {
      de.tif.jacob.report.impl.castor.Column c = searchConstraint.getOutput().getColumn(i);
      SortOrder order = SortOrder.ASCENDING;
      if (c.getSortOrder() != null)
      {
        switch (c.getSortOrder().getType())
        {
        case SortOrderType.ASCENDING_TYPE:
          order = SortOrder.ASCENDING;
          break;
        case SortOrderType.DESCENDING_TYPE:
          order = SortOrder.DESCENDING;
          break;
        case SortOrderType.NONE_TYPE:
          order = SortOrder.NONE;
          break;
        }
      }
      browserDef.addBrowserField(c.getTableAlias(), c.getField(), order, c.getField());
    }
    // start the search itself
    //
    browser.setMaxRecords(maxRecords);
    browser.performRecordCount();
    browser.search(searchConstraint.getInput().getRelationSet(), Filldirection.BACKWARD);
    return browser;
  }

  private CastorLayout getLayout(String mimeType)
  {
    Layouts layouts = searchConstraint.getLayouts();
    if (layouts != null)
    {
      CastorLayout defaultLayout = null;
      for (int i = 0; i < layouts.getLayoutCount(); i++)
      {
        CastorLayout layout = layouts.getLayout(i);
        if (layout.getMimeType().equals(mimeType))
          return layout;
        if (defaultLayout == null && layout.getMimeType().equals(searchConstraint.getDefaultMimeType()))
          defaultLayout = layout;
      }
      return defaultLayout;
    }
    return null;
  }

  public void render(OutputStream out, String mimeType, Locale locale) throws Exception
  {
    render(out, mimeType, Property.REPORT_MAX_RECORDS.getIntValue(), locale);
  }
  
  public void render(OutputStream out, String mimeType, int maxRecords, Locale locale) throws Exception
  {
    // use default locale of owner?
    if (locale == null)
      locale = getOwner().getLocale();
    CastorLayout layout = getLayout(mimeType);
    if (layout == null)
    {
      layout = createDefaultLayout();
    }

    // fetch data from data source
    IDataBrowserInternal browser = fetchData(maxRecords);
    
    // render report
    Transformer trans = Transformer.get(mimeType);
    trans.transform(out, this, layout, new ReportDataIterator(browser), locale);

    // store the last record counts after success
    //
    this.lastRecordCount = browser.recordCount();
    this.realRecordCount = browser.getRealRecordCount();
  }
  
  /**
   * Create default layout to show old reports with no layout as formatted text.
   * 
   * @return the default layout
   */
  private CastorLayout createDefaultLayout()
  {
    de.tif.jacob.report.impl.castor.Column[] columns = searchConstraint.getOutput().getColumn();
    
    CastorLayout layout = new CastorLayout();
    layout.setMimeType(FormattedTextTransformer.TEXT_FORMATTED_MIME_TYPE);
    
    CastorLayoutPart part = new CastorLayoutPart();
    layout.setPart(part);
    
    CastorLayoutColumns layoutColumns = new CastorLayoutColumns();
    part.setColumns(layoutColumns);
    
    for (int i=0; i<columns.length;i++)
    {
      de.tif.jacob.report.impl.castor.Column column = columns[i];
      
      CastorLayoutColumn layoutColumn = new CastorLayoutColumn();
      layoutColumns.addColumn(layoutColumn);
      
      layoutColumn.setTableAlias(column.getTableAlias());
      layoutColumn.setField(column.getField());
      layoutColumn.setLabel(column.getLabel());
    }
    
    return layout;
  }

  /**
   * Implementation of report data iterator for new layout renderer.
   * 
   * @author Andreas Sonntag
   * @since 2.9
   */
  private static final class ReportDataIterator implements IReportDataIterator
  {
    private final IDataBrowser browser;
    private int index = 0;
    private final Map aliasToFieldRecordIndexMap;
    private final class Record implements IReportDataRecord
    {
      private final IDataBrowserRecord record;

      private Record(IDataBrowserRecord record)
      {
        this.record = record;
      }

      public Object getValue(String alias, String field)
      {
        return this.record.getValue(getIndex(alias, field));
      }

      public String getStringValue(String alias, String field, Locale locale)
      {
        return this.record.getStringValue(getIndex(alias, field), locale);
      }
    }

    private ReportDataIterator(IDataBrowser browser)
    {
      this.browser = browser;
      IBrowserDefinition definition = browser.getBrowserDefinition();
      this.aliasToFieldRecordIndexMap = new HashMap(definition.getFieldNumber());
      for (int i = 0; i < definition.getFieldNumber(); i++)
      {
        // we just have table fields
        IBrowserTableField field = (IBrowserTableField) definition.getBrowserField(i);
        Map fieldToRecordIndexMap = (Map) this.aliasToFieldRecordIndexMap.get(field.getTableAlias().getName());
        if (fieldToRecordIndexMap == null)
          this.aliasToFieldRecordIndexMap.put(field.getTableAlias().getName(), fieldToRecordIndexMap = new HashMap());
        fieldToRecordIndexMap.put(field.getTableField().getName(), new Integer(i));
      }
    }

    private int getIndex(String alias, String field)
    {
      Map fieldToRecordIndexMap = (Map) this.aliasToFieldRecordIndexMap.get(alias);
      if (fieldToRecordIndexMap != null)
      {
        Integer index = (Integer) fieldToRecordIndexMap.get(field);
        if (index != null)
          return index.intValue();
      }
      // report definition is semantically wrong
      throw new RuntimeException(alias + "." + field + " is not a valid report field");
    }

    public IApplicationDefinition getApplication()
    {
      return this.browser.getAccessor().getApplication();
    }

    public boolean hasNext()
    {
      return this.index < browser.recordCount();
    }

    public IReportDataRecord next()
    {
      return new Record(this.browser.getRecord(this.index++));
    }
  }

  public void removeAllColumns()
  {
    searchConstraint.getOutput().removeAllColumn();
  }
  
  /*
   * @see de.tif.jacob.report.IReport#getColumns()
   */
  public Column[] getColumns()
  {
    Column[] result = new Column[searchConstraint.getOutput().getColumnCount()];
    for (int i = 0; i < searchConstraint.getOutput().getColumnCount(); i++)
    {
      Column c = new Column(searchConstraint.getOutput().getColumn(i));
      result[i] = c;
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.tif.jacob.report.IReport#getReportNotifyee(de.tif.jacob.security.IUser)
   */
  public ReportNotifyee getReportNotifyee(IUser user)
  {
    String currentUserId = user.getKey();
    for (int i = 0; i < searchConstraint.getNotifyeeCount(); i++)
    {
      Notifyee current = searchConstraint.getNotifyee(i);
      if (currentUserId.equals(current.getUserId()))
      {
        CronRule rule = current.getCronRule();
        if (rule.getRule() != null)
          return new ReportNotifyee(user, current.getAddress(), current.getProtocol().toString(), current.getMimeType(), new CronEntry(rule.getRule()), current.getOmitEmpty());
        
        // for backward compatibility of old report definitions
        return new ReportNotifyee(user, current.getAddress(), current.getProtocol().toString(), current.getMimeType(), rule.getMinute(), rule.getHour(), rule.getDays(), current.getOmitEmpty());
      }
    }
    return null;
  }

  public ReportNotifyee getReportNotifyee()
  {
    return getReportNotifyee(Context.getCurrent().getUser());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.report.IReport#getReportNotifyees()
   */
  public ReportNotifyee[] getReportNotifyees()
  {
    ReportNotifyee[] nots = new ReportNotifyee[searchConstraint.getNotifyeeCount()];
    for (int i = 0; i < searchConstraint.getNotifyeeCount(); i++)
    {
      Notifyee current = searchConstraint.getNotifyee(i);
      // Achtung: Bei alten Reports liefert getUserLoginId() null
      // zurück. Bei diesen ist der Notifyee immer der Owner, da
      // es nur private Reports gab.
      String userLoginId = current.getUserLoginId();
      if (userLoginId == null)
        userLoginId = searchConstraint.getOwner();
      CronRule rule = current.getCronRule();
      if (rule.getRule() != null)
        nots[i] = new ReportNotifyee(current.getUserId(), userLoginId, current.getAddress(), current.getProtocol().toString(), current.getMimeType(), //
            new CronEntry(rule.getRule()), current.getOmitEmpty());
      else
        // for backward compatibility of old report definitions
        nots[i] = new ReportNotifyee(current.getUserId(), userLoginId, current.getAddress(), current.getProtocol().toString(), current.getMimeType(), //
            rule.getMinute(), rule.getHour(), rule.getDays(), current.getOmitEmpty());
    }
    return nots;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.tif.jacob.report.IReport#deleteReportNotifyee(de.tif.jacob.security.
   * IUser)
   */
  public void deleteReportNotifyee(IUser notifyee)
  {
    String currentUserId = notifyee.getKey();
    for (int i = 0; i < searchConstraint.getNotifyeeCount(); i++)
    {
      Notifyee current = searchConstraint.getNotifyee(i);
      if (currentUserId.equals(current.getUserId()))
      {
        searchConstraint.removeNotifyee(i);
        return;
      }
    }
    // should not occur
    if (logger.isWarnEnabled())
      logger.warn("Could not remove notifyee " + notifyee.getKey() + " of report " + searchConstraint.getGuid());
  }

  public void setReportNotifyee(ReportNotifyee notifyee)
  {
    // for backward compatibility
    //
    if (notifyee == null)
    {
      deleteReportNotifyee(Context.getCurrent().getUser());
      return;
    }
    String currentUserId = notifyee.getUserId();
    // search notifyee in report
    //
    for (int i = 0; i < searchConstraint.getNotifyeeCount(); i++)
    {
      Notifyee current = searchConstraint.getNotifyee(i);
      if (currentUserId.equals(current.getUserId()))
      {
        // found
        setCronRule(current.getCronRule(), notifyee);
        current.setUserId(notifyee.getUserId());
        current.setUserLoginId(notifyee.getUserLoginId());
        current.setAddress(notifyee.getAddress());
        current.setMimeType(notifyee.getMimeType());
        current.setProtocol(notifyee.getProtocol());
        current.setOmitEmpty(notifyee.isOmitEmpty());
        return;
      }
    }
    // not found -> add notifyee to report
    //
    Notifyee n = new Notifyee();
    CronRule rule = new CronRule();
    setCronRule(rule, notifyee);
    n.setCronRule(rule);
    n.setUserId(notifyee.getUserId());
    n.setUserLoginId(notifyee.getUserLoginId());
    n.setAddress(notifyee.getAddress());
    n.setMimeType(notifyee.getMimeType());
    n.setProtocol(notifyee.getProtocol());
    n.setOmitEmpty(notifyee.isOmitEmpty());
    searchConstraint.addNotifyee(n);
  }
  
  private static void setCronRule(CronRule rule, ReportNotifyee notifyee)
  {
    if (notifyee.getMinute() == -1)
      rule.deleteMinute();
    else
      rule.setMinute(notifyee.getMinute());

    if (notifyee.getHour() == -1)
      rule.deleteHour();
    else
      rule.setHour(notifyee.getHour());

    if (notifyee.getDays() == null)
      rule.removeAllDays();
    else
      rule.setDays(notifyee.getDays());

    CronEntry cronEntry = notifyee.getCronEntry();
    if (cronEntry == null)
      rule.setRule(null);
    else
      rule.setRule(cronEntry.toString());
  }

  /*
   * @see de.tif.jacob.report.IReport#getMimeType()
   */
  public void setDefaultMimeType(String mimetype)
  {
    searchConstraint.setDefaultMimeType(mimetype);
  }
  
  /*
   * @see de.tif.jacob.report.IReport#getMimeType()
   */
  public String getDefaultMimeType()
  {
    return searchConstraint.getDefaultMimeType();
  }
  
  public Layouts getLayouts()
  {
    if(searchConstraint.getLayouts()==null)
      searchConstraint.setLayouts(new Layouts());
    
    return searchConstraint.getLayouts();
  }
  
  public CastorLayout getLayout_01()
  {
    Layouts l = getLayouts();
    if(l.getLayoutCount()==0)
      return null;
    return l.getLayout(0);
  }

  public CastorLayout createLayout_01()
  {
    CastorLayout layout = new CastorLayout();
    layout.setMimeType("text/plain");
    
    layout.setPrologue(createCaption());
    layout.setPageHeader(createCaption());

    CastorLayoutPart part = new CastorLayoutPart();
    CastorLayoutColumns columns =new CastorLayoutColumns();
    part.setIdent(0);
    part.setColumns(columns);
    layout.setPart(part);
    layout.setPageFooter(createCaption());
    layout.setEpilogue(createCaption());
    
    return layout;
  }
  
  private CastorCaption createCaption()
  {
    CastorCaption c = new CastorCaption();
    
    c.setExpanded(true);
    c.setVisible(true);
    c.setText("");
    return c;
  }
}
