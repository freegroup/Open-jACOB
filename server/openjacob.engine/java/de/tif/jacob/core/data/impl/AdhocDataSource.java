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

package de.tif.jacob.core.data.impl;

import java.io.PrintWriter;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.sql.InMemorySQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TARecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.schema.ISchemaDefinition;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public abstract class AdhocDataSource extends DataSource
{
  static public transient final String RCS_ID = "$Id: AdhocDataSource.java,v 1.6 2010/07/13 17:55:23 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.6 $";
  
	private final static ThreadLocal threadLocal = new ThreadLocal();
	
  /**
   * 
   */
  public AdhocDataSource(String name)
  {
    super(name);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#count(de.tif.jacob.core.data.impl.qbe.QBESpecification)
   */
  protected final long count(DataRecordSet recordSet, QBESpecification spec) throws InvalidExpressionException
  {
    DataSource internal = getInternal(recordSet.getAccessorInternal(), true);
    
    // delegate
    return internal.count(recordSet, spec);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#executeInternal(de.tif.jacob.core.data.impl.DataExecutionContext, de.tif.jacob.core.data.impl.ta.TADeleteRecordAction)
   */
  protected final void executeInternal(DataExecutionContext context, TADeleteRecordAction action) throws Exception
  {
    DataExecutionContext internalContext = ((MyExecutionContext) context).getInternal(action);
    internalContext.getDataSource().executeInternal(internalContext, action);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#executeInternal(de.tif.jacob.core.data.impl.DataExecutionContext, de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction)
   */
  protected final void executeInternal(DataExecutionContext context, TADeleteRecordsAction action) throws Exception
  {
    // should not make much sense
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#executeInternal(de.tif.jacob.core.data.impl.DataExecutionContext, de.tif.jacob.core.data.impl.ta.TAInsertRecordAction)
   */
  protected final void executeInternal(DataExecutionContext context, TAInsertRecordAction action) throws Exception
  {
    DataExecutionContext internalContext = ((MyExecutionContext) context).getInternal(action);
    internalContext.getDataSource().executeInternal(internalContext, action);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#executeInternal(de.tif.jacob.core.data.impl.DataExecutionContext, de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction)
   */
  protected final void executeInternal(DataExecutionContext context, TAUpdateRecordAction action) throws Exception
  {
    DataExecutionContext internalContext = ((MyExecutionContext) context).getInternal(action);
    internalContext.getDataSource().executeInternal(internalContext, action);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#newExecutionContext(de.tif.jacob.core.data.impl.DataTransaction)
   */
  protected final DataExecutionContext newExecutionContext(DataTransaction transaction) throws Exception
  {
    return new MyExecutionContext(this, transaction);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#setNextJacobId(de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField, long)
   */
  public final boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws Exception
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#newId(de.tif.jacob.core.definition.ITableDefinition, de.tif.jacob.core.definition.ITableField)
   */
  public final long newJacobIds(ITableDefinition table, ITableField field, int increment) throws Exception
  {
    throw new UnsupportedOperationException();
  }
  
  public final boolean supportsAutoKeyGeneration()
  {
    return false;
  }
  
  public boolean supportsSorting()
  {
    return true;
  }

  public final void destroy()
  {
    super.destroy();
  }
  
  public final IDataSourceAdjustment getAdjustment()
  {
    return super.getAdjustment();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#printInfo(java.io.PrintWriter)
   */
  public final void printInfo(PrintWriter writer)
  {
    super.printInfo(writer);
  }
  
  private DataSource getInternal(DataAccessor accessor, boolean create)
  {
    String key = "datasource.resource." + getName();
    AdhocResource adhocResource = (AdhocResource) accessor.getProperty(key);
    if (null == adhocResource)
    {
      if (create)
      {
        // avoid recursive calls of this method
        if (threadLocal.get() != null)
          throw new RuntimeException("Recursive call");
        
        threadLocal.set("FLAG");
        try
        {
          // create new internal InMemorySQLDataSource and wrapping resource
          //
          // Note: The datasource name must be unique because for each accessor an own
          // temporary database must be used!
          //
          SQLDataSource internalDataSource = new InMemorySQLDataSource(getName()+accessor.getId());
          adhocResource = new AdhocResource(internalDataSource);

          // glue data source, i.e resource, to accessor
          // Note: Do this immediately to allow accessor to "clear" inproper initialized
          // resource.
          accessor.setProperty(key, adhocResource);
          
          // get reconfigure implementation
          Reconfigure reconfigure = internalDataSource.getReconfigureImpl();

          // fetch current schema
          ISchemaDefinition currentSchema = reconfigure.fetchSchemaInformation();

          // setup datasource
          internalDataSource.setup(currentSchema);

          // and create schema
          CommandList commandList = reconfigure.reconfigure(accessor.getApplication().getSchemaDefinition(getName()), currentSchema, true);
          commandList.execute(internalDataSource, true);
          
          // and create adhoc content
          // note: use temporary data accessor to keep cache of original intact!
          //
          DataAccessor tempAccessor = (DataAccessor) accessor.newAccessor();
          tempAccessor.setProperty(key, adhocResource);
          try
          {
            initialize(tempAccessor);
          }
          finally 
          {
            // reset just to be on the safe side
            tempAccessor.setProperty(key, null);
          }
        }
        catch (RuntimeException ex)
        {
          throw ex;
        }
        catch (Exception ex)
        {
          throw new RuntimeException(ex);
        }
        finally
        {
          threadLocal.set(null);
        }
      }
      else
      {
        throw new IllegalStateException();
      }
    }
    return adhocResource.get();
  }
  
  protected abstract void initialize(IDataAccessor accessor) throws Exception;
  
  protected IDataSearchResult search(DataRecordSet recordSet, QBESpecification spec, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws InvalidExpressionException
  {
    DataSource internal = getInternal(recordSet.getAccessorInternal(), true);
    
    // delegate
    return internal.search(recordSet, spec, callback, eventHandler);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#test()
   */
  public final String test() throws Exception
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * This class is needed to treat the contained datasource as a resource, which
   * has to be destroyed at latest after the main application window is closed.
   * Otherwise in case of contained InMemorySQLDataSources there would be a
   * memory leak.
   * 
   * @author Andreas Sonntag
   */
  private static class AdhocResource extends ManagedResource
  {
    private SQLDataSource internalDataSource;
    
    private AdhocResource(SQLDataSource internalDataSource)
    {
      this.internalDataSource = internalDataSource;
      
  		// register the resource for the life cycle management of the application window.
  		Context.getCurrent().registerForWindow(this);
    }
    
    public SQLDataSource get()
    {
      if (this.internalDataSource == null)
      {
        // should never occur
        throw new IllegalStateException("Datasource already destroyed");
      }
      
      return this.internalDataSource;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.ManagedResource#release()
     */
    public void release()
    {
      if (this.internalDataSource != null)
      {
        this.internalDataSource.destroy();
        this.internalDataSource = null;
      }
    }
  }
  
  private class MyExecutionContext extends DataExecutionContext
  {
    private DataExecutionContext internalContext;

    /**
     * @param dataSource
     */
    private MyExecutionContext(DataSource dataSource, DataTransaction transaction)
    {
      super(dataSource, transaction);
    }
    
    private DataExecutionContext getInternal(TARecordAction action) throws Exception
    {
      DataSource internalDataSource = AdhocDataSource.this.getInternal(action.getRecord().getAccessorInternal(), true);
      if (this.internalContext == null)
      {
        this.internalContext = internalDataSource.newExecutionContext(getTransaction());
      }
      else
      {
        if (this.internalContext.getDataSource() != internalDataSource)
          throw new UnsupportedOperationException("Modifications of records from different accessors is not supported");
      }
      return this.internalContext;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.DataExecutionContext#close()
     */
    public void close()
    {
      if (internalContext != null)
        internalContext.close();
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.DataExecutionContext#commit()
     */
    public void commit() throws Exception
    {
      // internalContext should never be null
      internalContext.commit();
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.DataExecutionContext#rollback()
     */
    public void rollback()
    {
      if (internalContext != null)
        internalContext.rollback();
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.DataSource#isTransient()
   */
  public final boolean isTransient()
  {
    return true;
  }
}
