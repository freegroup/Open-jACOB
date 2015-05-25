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

package de.tif.jacob.core.data.impl.index;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.model.Japplversion;
import de.tif.jacob.core.model.Useddatasource;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployMain;

/**
 * Abstract search index data source implementation
 * 
 * @author Andreas Sonntag
 * @since 2.10
 */
public abstract class IndexDataSource extends DataSource
{
  static public transient final String RCS_ID = "$Id: IndexDataSource.java,v 1.4 2010/09/22 11:22:47 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";

  /**
   * Constructor
   * 
   * @param name
   */
  public IndexDataSource(String name)
  {
    super(name);
  }

  /**
   * @param record
   * @throws Exception
   */
  public IndexDataSource(IDataTableRecord record) throws Exception
  {
    super(record);
  }

  private static final IndexEventHandler DEFAULT_EVENTHANDLER = new IndexEventHandler()
  {
    public boolean containsRecordsOfTableAlias(ITableAlias tableAlias)
    {
      return false;
    }

    private void showException(String dataSource) throws UserRuntimeException
    {
      throw new UserRuntimeException("No event handler class '" + getEventHandlerClassName(dataSource) + "' existing");
    }

    public Set getMatchingTableFields(IDataRecord indexRecord) throws Exception
    {
      showException(indexRecord.getTableAlias().getTableDefinition().getDataSourceName());
      return null;
    }

    public IDataTableRecord getReferencedTableRecord(IDataRecord indexRecord) throws RecordNotFoundException
    {
      showException(indexRecord.getTableAlias().getTableDefinition().getDataSourceName());
      return null;
    }

    public IIndexUpdateContext newIndexUpdateContext(Context context, IndexDataSource dataSource) throws Exception
    {
      showException(dataSource.getName());
      return null;
    }
  };
  
  protected static String getEventHandlerClassName(String dataSourcename)
  {
    return "jacob.event.index." + StringUtils.capitalize(dataSourcename);
  }

  public final IndexEventHandler getEventHandler(IApplicationDefinition app)
  {
    return getEventHandler(app, getName());
  }

  public static IndexEventHandler getEventHandler(IApplicationDefinition app, String dataSourcename)
  {
    IndexEventHandler handler = (IndexEventHandler) ClassProvider.getInstance(app, getEventHandlerClassName(dataSourcename));
    return handler != null ? handler : DEFAULT_EVENTHANDLER;
  }

  /**
   * Rebuilds the index.
   * 
   * @param context
   *          the application context to use for rebuild
   * @throws Exception
   *           on any problem
   */
  public abstract void rebuild(Context context) throws Exception;

  /**
   * Optimizes the index.
   * 
   * @param context
   *          the application context to use for optimization
   * @throws Exception
   *           on any problem
   */
  public abstract void optimize(Context context) throws Exception;

  private static final class ApplicationVersionRecord implements Comparable
  {
    private final String application;
    private final Version version;
    private final IDataTableRecord record;

    private ApplicationVersionRecord(IDataTableRecord record) throws Exception
    {
      this.record = record;
      this.application = record.getStringValue(Japplversion.name);
      this.version = Version.parseVersion(record.getStringValue(Japplversion.version));
    }

    public int compareTo(Object o)
    {
      ApplicationVersionRecord other = (ApplicationVersionRecord) o;
      if (application.equals(other.application))
        // higher version first!
        return -version.compareTo(other.version);
      return application.compareTo(other.application);
    }
  }

  /**
   * Get the the definition of the default application used to update the given
   * index.
   * 
   * @param indexDataSource
   * @return definition of the default application or <code>null</code>
   * @throws Exception
   */
  public IApplicationDefinition getDefaultIndexUpdateApplication() throws Exception
  {
    // Search for all active application versions which use the given index and
    // which have marked full-reconfigure flag, i.e. which are responsible to
    // update index.
    //
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable japplversTable = accessor.getTable(Japplversion.NAME);
    japplversTable.qbeSetKeyValue(Japplversion.status, Japplversion.status_ENUM._test);
    japplversTable.qbeSetKeyValue(Japplversion.status, Japplversion.status_ENUM._productive);
    IDataTable useddatasourceTable = accessor.getTable(Useddatasource.NAME);
    useddatasourceTable.qbeSetKeyValue(Useddatasource.datasourcename, getName());
    useddatasourceTable.qbeSetKeyValue(Useddatasource.datasourcetype, Useddatasource.datasourcetype_ENUM._index);
    useddatasourceTable.qbeSetKeyValue(Useddatasource.reconfiguremode, Useddatasource.reconfiguremode_ENUM._full);
    japplversTable.search(accessor.getApplication().getDefaultRelationSet());
    if (japplversTable.recordCount() == 0)
      return null;
    else if (japplversTable.recordCount() == 1)
    {
      IDataTableRecord record = japplversTable.getRecord(0);
      return DeployMain.getApplication(record.getStringValue(Japplversion.name), record.getStringValue(Japplversion.version));
    }
    else
    {
      // Sort application version records and select the application version
      // with the first alphabetical name and the highest version
      //
      TreeSet applicationVersionRecords = new TreeSet();
      for (int i = 0; i < japplversTable.recordCount(); i++)
      {
        IDataTableRecord record = japplversTable.getRecord(i);
        applicationVersionRecords.add(new ApplicationVersionRecord(record));
      }

      ApplicationVersionRecord applversrec = (ApplicationVersionRecord) applicationVersionRecords.first();
      return DeployMain.getApplication(applversrec.record.getStringValue(Japplversion.name), applversrec.record.getStringValue(Japplversion.version));
    }
  }

  protected final DataExecutionContext newExecutionContext(DataTransaction transaction) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected final void executeInternal(DataExecutionContext context, TAInsertRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected final void executeInternal(DataExecutionContext context, TAUpdateRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected final void executeInternal(DataExecutionContext context, TADeleteRecordAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  protected final void executeInternal(DataExecutionContext context, TADeleteRecordsAction action) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  public final long newJacobIds(ITableDefinition table, ITableField field, int increment) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  public final boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  public final boolean supportsAutoKeyGeneration()
  {
    return false;
  }

  public final boolean supportsSorting()
  {
    return false;
  }
}
