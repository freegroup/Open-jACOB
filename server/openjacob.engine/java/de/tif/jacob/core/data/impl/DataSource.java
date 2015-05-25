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

import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Service;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.adjustment.impl.JacobAdjustment;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.index.LuceneDataSource;
import de.tif.jacob.core.data.impl.index.update.AsynchronousIndexUpdateContext;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.data.impl.misc.Nullable;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.ta.TAAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordsAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.ITableAlias.ITableAliasConditionAdjuster;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.definition.impl.AbstractKey;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import de.tif.jacob.core.exception.UnavailableDatasourceException;
import de.tif.jacob.core.exception.UncommittedLinkedRecordException;
import de.tif.jacob.core.exception.UndefinedDatasourceException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.util.config.Config;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DataSource implements IDataSource, ITableAliasConditionAdjuster
{
  static public transient final String RCS_ID = "$Id: DataSource.java,v 1.19 2011/07/02 09:11:32 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.19 $";
  
  /**
   * Placeholder used in connection string for HSQL and Lucene data sources to
   * be replaced by application root path.
   * 
   * @see Bootstrap#getApplicationRootPath()
   */
  public static final String WEB_APPDIR_REPLACEMENT = "${webapp.dir}";
  
  protected static final Config conf = Config.getCommonConfig();
  private final static transient Log logger = LogFactory.getLog(DataSource.class);
  
  private final static Class DATASOURCE_BY_NAME_ARGS[] = { String.class };
  private final static Class DATASOURCE_BY_RECORD_ARGS[] = { IDataTableRecord.class };
  
  protected final static String QUINTUS_ADJUSTMENT_CLASS = "de.tif.qes.adjustment.QeSAdjustment";
  
  private static final Map dataSources = new HashMap();
  
	private final String name;
	private final IDataSourceAdjustment adjustment;
  private final boolean predefined;
  private boolean destroyed = false;
  
	/**
   * Gets the data source instance specified by name.
   * 
   * @param dataSourceName
   *          the name of the data source
   * @return the requested data source instance
   * @throws UndefinedDatasourceException
   *           the requested data source name is not known
   * @throws UnavailableDatasourceException
   *           the requested data source is not available
   */
  public static DataSource get(String dataSourceName) throws UndefinedDatasourceException, UnavailableDatasourceException
	{
		synchronized (dataSources)
		{
			DataSource dataSource = (DataSource) dataSources.get(dataSourceName);
			if (null == dataSource)
			{
				try
				{
					dataSource = instantiate(dataSourceName);
				}
        catch (UnavailableDatasourceException ex)
        {
          // just rethrow
          throw ex;
        }
				catch (UndefinedDatasourceException ex)
				{
				  // just rethrow
				  throw ex;
				}
				catch (InvocationTargetException ex)
				{
					// IBIS: throw specific exception
					throw new RuntimeException("Getting datasource '" + dataSourceName + "' failed!", ex.getCause());
				}
				catch (Exception ex)
				{
          // IBIS: throw specific exception
          throw new RuntimeException("Getting datasource '" + dataSourceName + "' failed!", ex);
        }
        dataSources.put(dataSourceName, dataSource);
			}
			return dataSource;
		}
	}
  
  /**
   * Checks whether the given data source is a datasource implementation of the
   * given type.
   * <p>
   * Note: This works also for "wrapped" data sources, i.e.
   * {@link de.tif.jacob.core.data.impl.sql.AutoDetectSQLDataSource}.
   * <p>
   * Examples:
   * <li><code>myDatasource.isTypeOf(OracleDataSource.class)</code>
   * <li><code>myDatasource.isTypeOf(SQLDataSource.class)</code>
   * 
   * @param databaseClassImplementation
   *          the data source implementation to test for
   * @return <code>true</code> if this datasource is of the given
   *         implementation, <code>false</code> otherwise.
   * 
   * @since 2.6
   */
  public boolean isTypeOf(Class databaseClassImplementation)
  {
    Class clazz = this.getClass();
    while (!clazz.equals(databaseClassImplementation))
    {
      clazz = clazz.getSuperclass();
      if (clazz == null)
        return false;
    }
    return true;
  }
	
  /**
   * Prints internal information about datasources
   * 
   * @param writer
   *          the writer to print information to.
   */
  public static void printAllInfo(PrintWriter writer)
  {
    // get a sorted list of all datasources
    //
    List tempList = new ArrayList();
    synchronized (dataSources)
    {
      Set tempSet = new TreeSet(dataSources.keySet());
      for (Iterator it = tempSet.iterator(); it.hasNext();)
      {
        tempList.add(dataSources.get(it.next()));
      }
    }

    // print info about all datasources in a 
    // sorted manner
    //
    for (int i = 0; i < tempList.size(); i++)
    {
      DataSource ds = (DataSource) tempList.get(i);
      ds.printInfo(writer);
    }
  }
  
  /**
   * Prints internal information about this datasource
   * 
   * @param writer
   *          the writer to print information to.
   */
  public void printInfo(PrintWriter writer)
  {
    writer.println(getName());
  }
  
	/**
	 * Destroys this datasource.
	 */
	public void destroy()
	{
	  this.destroyed = true;
	}

  protected static Object jndiLookup(String jndiName) throws NamingException
  {
    if (jndiName == null)
      throw new NullPointerException("jndiName is null");
    
    Context context;
    
    // use root context?
    if (jndiName.startsWith("/"))
    {
      // yes
      Context initCtx = new InitialContext();
      context = (Context) initCtx.lookup("");
      jndiName = jndiName.substring(1);
    }
    else
    {
      // no -> use environment context
      context = getEnvironmentContext();
    }
    
    return context.lookup(jndiName);
  }
  
  protected static Context getEnvironmentContext() throws NamingException
  {
    // instantiate initial context
    Context initCtx = new InitialContext();

    String environmentContextName = conf.getProperty("datasource.environment.context");
    if (environmentContextName == null || environmentContextName.trim().length() == 0)
    {
      if (logger.isInfoEnabled())
        logger.info("Using environment context: initial");

      return initCtx;
    }
    
    if (logger.isInfoEnabled())
      logger.info("Using environment context: " + environmentContextName);

    Object environmentContext = initCtx.lookup(environmentContextName);
    if (environmentContext instanceof Context)
      return (Context) environmentContext;
    throw new RuntimeException("Can not access environment context: " + environmentContextName);
  }

  /**
   * Checks whether a given database is predefined, i.e. defined within
   * <code>config.properties</code>.
   * 
   * @param dataSourceName
   *          the name of the data source
   * @return <code>true</code> data source is predefined, otherwise
   *         <code>false</code>
   */
  public static boolean isPredefined(String dataSourceName)
  {
    return null != conf.getProperty("datasource." + dataSourceName + ".className");
  }

	private static DataSource instantiate(String dataSourceName) throws UndefinedDatasourceException, UnavailableDatasourceException, Exception
  {
    logger.info("Loading datasource " + dataSourceName + " ..");

    //
    // First check whether the desired datasource is defined in property file
    String dataSourceClassName = conf.getProperty("datasource." + dataSourceName + ".className");
    if (dataSourceClassName != null)
    {
      logger.info("Instantiating datasource class " + dataSourceClassName + " ..");
      Class clazz = Class.forName(dataSourceClassName);
      Constructor constructor = clazz.getConstructor(DATASOURCE_BY_NAME_ARGS);
      return (DataSource) constructor.newInstance(new Object[] { dataSourceName });
    }

    //
    // Second check for datasources defined in jACOB configuration database
    IDataAccessor accessor = new DataAccessor(AdminApplicationProvider.getApplication());
    IDataTable table = accessor.getTable("datasource");
    table.qbeClear();
    table.qbeSetKeyValue("name", dataSourceName);
    if (table.search() > 0)
    {
      IDataTableRecord datasourceRecord = table.getRecord(0);
      Class clazz = getDatasourceClass(datasourceRecord);
      logger.info("Instantiating datasource class " + clazz.getName() + " ..");
      Constructor constructor = clazz.getConstructor(DATASOURCE_BY_RECORD_ARGS);
      return (DataSource) constructor.newInstance(new Object[] { datasourceRecord });
    }

    throw new UndefinedDatasourceException(dataSourceName);
  }
  
  private static Class getLdapDatasourceClass(IDataTableRecord datasourceRecord) throws UnavailableDatasourceException, Exception
  {
    Iterator iter = Service.providers(DataSourceProvider.class);
    while (iter.hasNext())
    {
      DataSourceProvider provider = (DataSourceProvider) iter.next();
      Class clazz = provider.getLdapDataSourceClass();
      if (clazz != null)
        return clazz;
    }
    throw new UnavailableDatasourceException(datasourceRecord.getStringValue(Datasource.name), "LDAP");
  }

  private static Class getDatasourceClass(IDataTableRecord datasourceRecord) throws UnavailableDatasourceException, Exception
  {
    String rdbType = datasourceRecord.getStringValue(Datasource.rdbtype);

    try
    {
      if (Datasource.rdbType_ENUM._AutoDetect.equals(rdbType))
      {
        String location = datasourceRecord.getStringValue(Datasource.location);

        if (Datasource.location_ENUM._Webserver.equals(location))
        {
          String jndiName = datasourceRecord.getStringValue(Datasource.jndiname);
          try
          {
            Object jndiObject = jndiLookup(jndiName);
            if (jndiObject instanceof javax.sql.DataSource)
              return de.tif.jacob.core.data.impl.sql.AutoDetectSQLDataSource.class;

            if (jndiObject instanceof String)
            {
              String connectString = (String) jndiObject;
              if (connectString.startsWith("index:lucene"))
                return LuceneDataSource.class;
              return getLdapDatasourceClass(datasourceRecord);
            }

            throw new Exception("Jndi object '" + jndiName + "' has invalid type: " + jndiObject.getClass().getName());
          }
          catch (NamingException ex)
          {
            throw new Exception("Could not access jndi object: " + jndiName, ex);
          }
        }
        else
        {
          // IBIS: Hack: Check for LDAP and Lucene datasource
          //
          String connectString = datasourceRecord.getStringValue(Datasource.connectstring);
          if (null != connectString)
          {
            if (connectString.startsWith("ldap"))
              return getLdapDatasourceClass(datasourceRecord);
            if (connectString.startsWith("index:lucene"))
              return LuceneDataSource.class;
          }

          return de.tif.jacob.core.data.impl.sql.AutoDetectSQLDataSource.class;
        }
      }
      else
      {
        Iterator iter = Service.providers(DataSourceProvider.class);
        while (iter.hasNext())
        {
          DataSourceProvider provider = (DataSourceProvider) iter.next();
          Class clazz = provider.getDataSourceClassByName(rdbType);
          if (clazz != null)
            return clazz;
        }
        
        if (logger.isWarnEnabled())
          logger.warn("No provider for data source type '" + rdbType + "' found");
      }
    }
    catch (NoClassDefFoundError e)
    {
      // ignore
    }

    throw new UnavailableDatasourceException(datasourceRecord.getStringValue(Datasource.name), rdbType);
  }

	/**
   * Destroys the datasource given by name. Destroying means that all datasource
   * resources (e.g. connection pool) are released. If {@link #get(String)} is
   * called afterwards, the datasource configuration (if existing) will be
   * retrieved and the datasource will be initialised accordingly.
   * 
   * @param dataSourceName
   *          datasource name
   * @return the destroyed datasource or <code>null</code> if not existing
   * @throws Exception
   *           on any problem
   */
	public static DataSource destroy(String dataSourceName) throws Exception
  {
	  if (logger.isInfoEnabled())
	    logger.info("Destroying datasource "+dataSourceName+" ..");
	  
	  synchronized (dataSources)
    {
      DataSource dataSource = (DataSource) dataSources.get(dataSourceName);
      if (null != dataSource)
      {
        dataSource.destroy();
        dataSources.remove(dataSourceName);
      }
      return dataSource;
    }
  }
	
	/**
	 * Destroyes all datasources.
	 */
	protected static void destroyAll()
  {
    logger.info("Destroying all datasources ..");

    synchronized (dataSources)
    {
      Iterator iter = dataSources.values().iterator();
      while (iter.hasNext())
      {
        DataSource dataSource = (DataSource) iter.next();
        try
        {
          dataSource.destroy();
        }
        catch (Exception ex)
        {
          logger.warn("Destroying datasource " + dataSource.name + " failed", ex);
        }
      }
      dataSources.clear();
    }
  }
	
	/**
	 * Test this datasource.
	 * <p>
	 * The test has been successfully, if no exception has been thrown.
	 * 
	 * @return additional data source information
	 * @throws Exception
	 *           Test of datasource has failed.
	 */
  public abstract String test() throws Exception;

  protected abstract IDataSearchResult search(DataRecordSet recordSet, QBESpecification spec, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws InvalidExpressionException;
  
  protected abstract long count(DataRecordSet recordSet, QBESpecification spec) throws InvalidExpressionException;
  
  protected final DataRecord instantiateRecord(DataRecordSet recordSet, DataTableRecordEventHandler eventHandler, int[] primaryKeyIndices, Object[] values, int seqNbr)
  {
    return recordSet.instantiateRecord(this, eventHandler, primaryKeyIndices, values, seqNbr);
  }
  
  protected abstract DataExecutionContext newExecutionContext(DataTransaction transaction) throws Exception;
  
  public final void execute(DataExecutionContext context, TAInsertRecordAction action) throws Exception, UserException
  {
    context.setCurrentRecord(action.getRecord());
    
    // check whether all required fields are set
    checkRequiredFields(action.getRecord());
    
    // check for uncommitted linked records
    checkForeignKeyFields(action.getRecord());

    // write history
    this.adjustment.getHistoryImplementation().build(action);
    
    executeInternal(context, action);
  }
  
  public final void execute(DataExecutionContext context, TAUpdateRecordAction action) throws Exception, UserException
  {
    context.setCurrentRecord(action.getRecord());
    
    // check whether all required fields are set
    checkRequiredFields(action.getRecord());
    
    // check for uncommitted linked records
    checkForeignKeyFields(action.getRecord());

    // write history
    this.adjustment.getHistoryImplementation().build(action);
    
    executeInternal(context, action);
  }
  
  public final void execute(DataExecutionContext context, TADeleteRecordAction action) throws Exception
  {
  	context.setCurrentRecord(action.getRecord());
  	
  	// write history
    this.adjustment.getHistoryImplementation().build(action);
  	
  	executeInternal(context, action);
  }
  
  public final void execute(DataExecutionContext context, TADeleteRecordsAction action) throws Exception
  {
  	executeInternal(context, action);
  }
  
  private void checkRequiredFields(DataTableRecord record) throws RequiredFieldException, TableFieldExceptionCollection
  {
    TableFieldExceptionCollection exs = null;
    for (int i = 0; i < record.getFieldNumber(); i++)
    {
      ITableField field = record.getFieldDefinition(i);
      if (field.isRequired())
      {
        // Call getValueInternal() to avoid expensive data load for long text
        // and binary
        Object value = record.getValueInternal(i);
        if (value == null || (value instanceof Nullable && ((Nullable) value).isNull()))
        {
          if (exs == null)
            exs = new TableFieldExceptionCollection(new CoreMessage("REQUIRED_FIELDS_MISSING"));
          exs.add(new RequiredFieldException(field));
        }
      }
    }
    if (exs != null)
      throw exs;
  }
  
  private void checkForeignKeyFields(DataTableRecord record) throws UncommittedLinkedRecordException
  {
    // loop over all foreign keys
    //
    IDataAccessor accessor = record.getAccessor();
    Iterator keyIter = record.getTableAlias().getTableDefinition().getKeys();
    while (keyIter.hasNext())
    {
      AbstractKey key = (AbstractKey) keyIter.next();
      
      // foreign key value has been changed?
      if (key.getType() == KeyType.FOREIGN && record.hasChangedKeyValue(key))
      {
        // a new (not null) key has been set
        IDataKeyValue keyValue = record.getKeyValue(key);
        if (null != keyValue)
        {
          // search for uncommitted record 
          Iterator aliasIter = key.getLinkedForeignTableAliases();
          while (aliasIter.hasNext())
          {
            ITableAlias alias = (ITableAlias) aliasIter.next();
            IDataTableRecord foreignRecord = accessor.getTable(alias.getName()).getSelectedRecord();
            if (foreignRecord != null)
            {
              // linked record is new and has not been committed in it's (other!) transaction?
              DataTransaction recordTrans = (DataTransaction) record.getCurrentTransaction();
              DataTransaction foreignTrans = (DataTransaction) foreignRecord.getCurrentTransaction();
              if (foreignTrans != null)
              {
                recordTrans = recordTrans != null ? recordTrans.getRootTransaction() : null;
                foreignTrans = foreignTrans.getRootTransaction();
                if (recordTrans != foreignTrans && //
                    foreignRecord.isNew() && //
                    keyValue.equals(foreignRecord.getPrimaryKeyValue()))
                {
                  throw new UncommittedLinkedRecordException(foreignRecord);
                }
              }
            }
          }
        }
      }
    }
  }
  
  protected abstract void executeInternal(DataExecutionContext context, TAInsertRecordAction action) throws Exception;
  protected abstract void executeInternal(DataExecutionContext context, TAUpdateRecordAction action) throws Exception;
  protected abstract void executeInternal(DataExecutionContext context, TADeleteRecordAction action) throws Exception;
  protected abstract void executeInternal(DataExecutionContext context, TADeleteRecordsAction action) throws Exception;
  
  /**
   * @param transaction
   * @throws UserException
   * @throws Exception
   */
  protected final void execute(DataTransaction transaction) throws UserException, Exception
  {
    if (logger.isDebugEnabled())
      logger.debug("Starting execution of datasource " + this);

    try
    {
      // execute actions (which might have been changed since executing beforeCommit-Hook!)
      List executeActions = transaction.getActionsToExecute(getName());
      DataExecutionContext executionContext = newExecutionContext(transaction);
      try
      {
        for (int i=0; i < executeActions.size(); i++)
        {
          TAAction action = (TAAction) executeActions.get(i);
          action.execute(this, executionContext);
        }
        executionContext.commit();

        if (logger.isDebugEnabled())
          logger.debug("Execution completed successfully");
      }
      catch (Exception ex)
      {
        executionContext.rollback();
        throw ex;
      }
      finally
      {
        executionContext.close();
      }
      
      // actualize record cache (Remark: this must be done after commit was
      // successful!), etc.
      for (int i = 0; i < executeActions.size(); i++)
      {
        TAAction action = (TAAction) executeActions.get(i);
        action.executeAfterCommit();
      }

      // handling to update search indices (if existing)
      //
      {
        AbstractApplicationDefinition applDef = (AbstractApplicationDefinition) transaction.getApplication();
        List datasourceIndices = applDef.getDataSourceIndexNames();
        for (int j = 0; j < datasourceIndices.size(); j++)
        {
          String datasourceIndexName = (String) datasourceIndices.get(j);
          try
          {
            IIndexUpdateContext indexUpdateContext = new AsynchronousIndexUpdateContext(applDef, datasourceIndexName);
            try
            {
              for (int i = 0; i < executeActions.size(); i++)
              {
                TAAction action = (TAAction) executeActions.get(i);
                action.executeUpdateIndex(indexUpdateContext);
              }
              indexUpdateContext.flush();
            }
            finally
            {
              indexUpdateContext.close();
            }
          }
          catch (Exception ex)
          {
            if (logger.isWarnEnabled())
              logger.warn("Updating index '" + datasourceIndexName + "' failed", ex);
          }
        }
      }
    }
    catch (UserRuntimeException ex)
    {
      if (logger.isDebugEnabled())
        logger.debug(ex.toString());
      
      // rethrow
      throw ex;
    }
    catch (UserException ex)
    {
      if (logger.isDebugEnabled())
        logger.debug(ex.toString());
      
      // rethrow
      throw ex;
    }
    catch (Exception ex)
    {
      // release a warning because we do not know the context (i.e. severity)
      if (logger.isWarnEnabled())
        logger.warn(ex.toString());
      
      // rethrow
      throw ex;
    }
  }

  protected DataSource(String name)
  {
    this.name = name == null ? "testDatasource" : name;
    this.predefined = true;
    this.adjustment = getAdjustment(name == null ? null : conf.getProperty("datasource." + name + ".adjustment"), true);
  }

  protected DataSource(IDataTableRecord record) throws Exception
  {
    this.name = record.getStringValue(Datasource.name);
    this.predefined = false;
    this.adjustment = getAdjustment(record.getStringValue(Datasource.adjustment), false);
  }

  protected  IDataSourceAdjustment getAdjustment(String adjustment, boolean doDefault)
  {
    if (Datasource.adjustment_ENUM._jACOB.equalsIgnoreCase(adjustment))
    {
      return new JacobAdjustment();
    }
    else if (Datasource.adjustment_ENUM._Quintus.equalsIgnoreCase(adjustment))
    {
      // check whether module Quintus exists
      //
      try
      {
        return (IDataSourceAdjustment) Class.forName(QUINTUS_ADJUSTMENT_CLASS).newInstance();
      }
      catch (ClassNotFoundException ex)
      {
        throw new UserRuntimeException(new CoreMessage(CoreMessage.FUNCTION_NOT_AVAILABLE_IN_DEMO_VERSION, "Quintus"));
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    }
    else if (null == adjustment && doDefault)
    {
      return new JacobAdjustment();
    }

    throw new RuntimeException("Unknown data source adjustment: " + adjustment + ".");
  }

  public String adjustAliasName(String aliasName)
  {
    // by default no adjustment
    return aliasName;
  }

  public String adjustColumnName(String columnName)
  {
    // by default use same method as for alias names
    return adjustAliasName(columnName);
  }

  public IDataSourceAdjustment getAdjustment()
  {
    return this.adjustment;
  }

	/**
	 * @return Returns the name.
	 */
	public final String getName()
	{
		return name;
	}
  
	public abstract long newJacobIds(ITableDefinition table, ITableField field, int increment) throws Exception;
	
  /**
   * Method used to adjust internal keys.
   * 
   * @param table
   * @param field
   * @param nextId
   * @return
   * @throws Exception
   */
  public abstract boolean setNextJacobId(ITableDefinition table, ITableField field, long nextId) throws Exception;
  
  /**
   * Checks whether the datasource supports auto key generation, i.e. the keys are
   * not created by means of {@link #newJacobIds(ITableDefinition, ITableField, int)}.
   * 
   * @return <code>true</code> if the datasource supports auto id generation,
   *         otherwise <code>false</code>
   */
  public abstract boolean supportsAutoKeyGeneration();
  
  /**
   * Checks whether the datasource supports sorting.
   * 
   * @return <code>true</code> if the datasource supports sorting,
   *         otherwise <code>false</code>
   * @since 2.10
   */
  public abstract boolean supportsSorting();
  
	protected final boolean isLockingEnabled(IDataRecordId recordId)
	{
		return this.adjustment.getLockingImplementation().doLocking(this, recordId);
	}
  
  protected final void lock(IDataTransaction transaction, IDataRecordId record) throws RecordLockedException
  {
    this.adjustment.getLockingImplementation().lock(transaction, this, record); 
  }

  protected final void checkLocked(IDataTransaction transaction, IDataRecordId record) throws RecordLockedException
  {
    this.adjustment.getLockingImplementation().checkLocked(transaction, this, record); 
  }

  protected final void unlock(IDataTransaction transaction, IDataRecordId record) throws Exception
  {
    this.adjustment.getLockingImplementation().unlock(transaction, this, record); 
  }

  /**
   * @return Returns the destroyed.
   */
  public final boolean isDestroyed()
  {
    return destroyed;
  }

  public final String toString()
	{
		return this.name;
	}
  
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object obj)
	{
    // make sure default implementation is not overwritten
    return super.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode()
	{
    // make sure default implementation is not overwritten
    return super.hashCode();
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	protected final String getResourceContent(String fileName) throws Exception
	{
	  InputStream inputStream = getClass().getResourceAsStream(fileName);
	  if (null == inputStream)
	  {  
	    throw new Exception("Could not open file '"+fileName+"' as stream");
	  }
	  
	  try
    {
      // IBIS: Avoid fixed encoding
      return IOUtils.toString(inputStream, "ISO-8859-1");
    }
    finally
    {
      IOUtils.closeQuietly(inputStream);
    }
	}
	
  /**
	 * Checks whether this datasource is a jACOB predefined datasource.
	 * <p>
	 * Predefined datasource may not be administrated by means of the admin application.
	 * 
	 * @return <code>true</code> datasource is predefined, otherwise <code>false</code>
	 */
  public final boolean isPredefined()
  {
    return predefined;
  }

  /**
   * Checks whether this datasource is transient datasource.
   * 
   * @return <code>true</code> datasource is transient, otherwise <code>false</code>
   * @since 2.6
   */
  public boolean isTransient()
  {
    return false;
  }
}
