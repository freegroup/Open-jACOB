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
/*
 * Created on 28.09.2004
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.report.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.impl.castor.ReportDefinition;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DatabaseReport extends Report implements Comparable
{
  static public final transient String RCS_ID = "$Id: DatabaseReport.java,v 1.8 2009/12/14 09:53:40 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";
  
  public static final int OWN_MODE = 0;
  public static final int PUBLIC_MODE = 1;
  public static final int ALL_MODE = 2;

  public static List getScheduledReports() throws Exception
  {
    List reports = new ArrayList();

    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
    table.qbeSetValue(de.tif.jacob.core.model.Report.scheduled, "true");
    table.setMaxRecords(DataRecordSet.UNLIMITED_RECORDS);
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      try
      {
        reports.add(new DatabaseReport(table.getRecord(i)));
      }
      catch (Exception ex)
      {
        logger.error("Invalid Report " + table.getRecord(i).getValue(de.tif.jacob.core.model.Report.id));
      }
    }
    return reports;
  }

  /**
	 * Returns all reports for the hands over user and application definition
	 * 
	 * @param appDef
	 * @param owner
   * @param mode
	 * @return List[IReport]
	 * @throws Exception
	 */
  public static List getReports(IApplicationDefinition appDef, IUser owner, int mode) throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
    table.qbeClear();
    table.qbeSetKeyValue(de.tif.jacob.core.model.Report.applicationname, appDef.getName());
    switch (mode)
    {
      case ALL_MODE:
        table.qbeSetValue(de.tif.jacob.core.model.Report.ownerid, "|=\"" + owner.getLoginId() + "\"");
        table.qbeSetValue(de.tif.jacob.core.model.Report.accessmode, "|"+de.tif.jacob.core.model.Report.accessmode_ENUM._public);
        break;
      case PUBLIC_MODE:
        table.qbeSetValue(de.tif.jacob.core.model.Report.ownerid, "!=\"" + owner.getLoginId() + "\"");
        table.qbeSetValue(de.tif.jacob.core.model.Report.accessmode, de.tif.jacob.core.model.Report.accessmode_ENUM._public);
        break;
      case OWN_MODE:
      default:
        table.qbeSetKeyValue(de.tif.jacob.core.model.Report.ownerid, owner.getLoginId());
        break;
    }
    table.setMaxRecords(DataRecordSet.UNLIMITED_RECORDS);
    table.search();
    
    List reports = new ArrayList(table.recordCount());
    for (int i = 0; i < table.recordCount(); i++)
    {
      try
      {
        reports.add(new DatabaseReport(table.getRecord(i)));
      }
      catch (Exception ex)
      {
        logger.error("Invalid Report " + table.getRecord(i).getValue(de.tif.jacob.core.model.Report.id));
      }
    }
    
    // and sort reports by name
    //
    Collections.sort(reports);
    
    return reports;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
    DatabaseReport other = (DatabaseReport) o;
    
    int res = getName().compareTo(other.getName());
    if (res != 0)
      return res;
    
    return getGUID().compareTo(other.getGUID()) ;
  }

  /**
	 * Returns all reports for the hands over user and application definition
	 * 
	 * @param appDef
	 * @param owner
	 * @return IReport
	 * @throws Exception
	 */
  public static IReport getReport(IApplicationDefinition appDef, IUser owner, String name) throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
    table.qbeClear();
    table.qbeSetKeyValue(de.tif.jacob.core.model.Report.name, name);
    table.qbeSetKeyValue(de.tif.jacob.core.model.Report.applicationname, appDef.getName());
    table.qbeSetKeyValue(de.tif.jacob.core.model.Report.ownerid, owner.getLoginId());
    table.setUnlimitedRecords();
    table.search();
    if (table.search() > 0)
    {
      return new DatabaseReport(table.getRecord(0));
    }
    return null;
  }
  
  /**
   * For transferring file reports to database
   * 
   * @param fileReport
   * @return
   * @throws Exception
   */
  public static IReport clone(Report report) throws Exception
  {
    ReportDefinition def=(ReportDefinition)ReportDefinition.unmarshalReportDefinition(new StringReader(report.toXml()));
    def.setGuid(report.getGUID());
    return new DatabaseReport(def);
  }

  /**
	 * Returns the report definition with the hands over guid.
	 * 
   * @return the report definition or <code>null</code> if the report does not
   *         exist anymore.
	 */
  public static IReport getReport(String guid) throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
    table.qbeClear();
    table.qbeSetKeyValue(de.tif.jacob.core.model.Report.id, guid);
    if (table.search() > 0)
    {
      return new DatabaseReport(table.getRecord(0));
    }
    return null;
  }

  /**
	 * @param tableRecord
	 * @throws Exception
	 */
  public DatabaseReport(IDataTableRecord tableRecord) throws Exception
  {
    super((ReportDefinition) ReportDefinition.unmarshalReportDefinition(new StringReader(tableRecord.getStringValue(de.tif.jacob.core.model.Report.definition))));
  }

  public DatabaseReport(ReportDefinition reportDef) throws Exception
  {
    super(reportDef);
  }

  /**
	 * @param appName
	 * @param domainName
	 * @param formName
	 * @param version
	 * @param relationSet
	 * @param mainTableAlias
	 */
  public DatabaseReport(String name, String appName, String domainName, String formName, String version, String relationSet, String mainTableAlias)
  {
    super(name, appName, domainName, formName, version, relationSet, mainTableAlias);
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.report.IReport#delete()
	 */
  public void delete() throws Exception
  {
    if (this.getGUID() == null)
    {
      // report not saved so far
      return;
    }

    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(de.tif.jacob.core.model.Report.id, this.getGUID());
      if (table.search() > 0)
      {
        table.getRecord(0).delete(transaction);
      }
      transaction.commit();
     }
    finally
    {
      transaction.close();
    }
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.report.IReport#save()
	 */
  public void save() throws Exception
  {
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable table = accessor.getTable(de.tif.jacob.core.model.Report.NAME);
    IDataTransaction transaction = accessor.newTransaction();
    try
    {
      table.qbeClear();
      table.qbeSetKeyValue(de.tif.jacob.core.model.Report.name, getName());
      table.qbeSetKeyValue(de.tif.jacob.core.model.Report.applicationname, this.searchConstraint.getApplication().getName());
      table.qbeSetKeyValue(de.tif.jacob.core.model.Report.ownerid, this.searchConstraint.getOwner());
      IDataTableRecord record;
      if (table.search() > 0)
       {
        record = table.getRecord(0);
      }
      else
       {
        record = table.newRecord(transaction);
      }
      
      // set guid in any case because we might update the report with a new definition
      this.searchConstraint.setGuid(record.getStringValue(de.tif.jacob.core.model.Report.id));
      
      setHeaderData(transaction, record);
      
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
  }

  public void setHeaderData(IDataTransaction transaction, IDataTableRecord record) throws Exception
  {
    record.setValue(transaction, de.tif.jacob.core.model.Report.accessmode, isPrivate() ? de.tif.jacob.core.model.Report.accessmode_ENUM._private : de.tif.jacob.core.model.Report.accessmode_ENUM._public);
    record.setValue(transaction, de.tif.jacob.core.model.Report.applicationname, this.searchConstraint.getApplication().getName());
    try
    {
      record.setValue(transaction, de.tif.jacob.core.model.Report.applicationversion, this.searchConstraint.getApplication().getVersion());
    }
    catch (InvalidExpressionException ex)
    {
      // HACK: Just to handle the situation where automatic reconfigure of table
      // 'report' fails due to change table field 'applicationversion' from
      // Decimal(2) to Text(20)
      record.setValue(transaction, de.tif.jacob.core.model.Report.applicationversion, Version.parseVersion(this.searchConstraint.getApplication().getVersion()).toShortString());
    }
    record.setValue(transaction, de.tif.jacob.core.model.Report.name, getName());
    record.setValue(transaction, de.tif.jacob.core.model.Report.ownerid, this.searchConstraint.getOwner());
    record.setValue(transaction, de.tif.jacob.core.model.Report.scheduled, isScheduled()?de.tif.jacob.core.model.Report.scheduled_ENUM._true:de.tif.jacob.core.model.Report.scheduled_ENUM._false);
    record.setValue(transaction, de.tif.jacob.core.model.Report.definition, toXmlFormatted());
  }
}
