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

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTableSearchIterateCallback;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.event.IDataBrowserModifiableRecord;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.DataRecordSet;
import de.tif.jacob.core.data.impl.DataSearchResult;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataSearchIterateCallback;
import de.tif.jacob.core.data.impl.IDataSearchResult;
import de.tif.jacob.core.data.impl.index.event.IndexEventHandler;
import de.tif.jacob.core.data.impl.index.event.LuceneEventHandler;
import de.tif.jacob.core.data.impl.index.event.StandardLuceneEventHandler;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.data.impl.misc.InvalidFieldExpressionException;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEField;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBESpecification;
import de.tif.jacob.core.data.impl.qbe.QBEUserConstraint;
import de.tif.jacob.core.data.impl.sql.SQLMonitor;
import de.tif.jacob.core.data.internal.IDataRecordInternal;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.i18n.CoreMessage;

/**
 * Lucene data source implementation.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public class LuceneDataSource extends IndexDataSource
{
  static public transient final String RCS_ID = "$Id: LuceneDataSource.java,v 1.8 2010/09/22 11:22:47 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.8 $";

  protected static final transient Log logger = Lucene.logger;

  private final String indexDirectory;

  private Connection connection;

  private final class Connection
  {
    private static final long REOPEN_CHECK_INTERVAL = 3 * 1000;
    
    private final IndexReader reader;
    private final Searcher searcher;
    private int regCount;
    private long lastReopenTime = System.currentTimeMillis();

    private Connection(Directory directory) throws Exception
    {
      // only searching, so read-only=true
      this.reader = IndexReader.open(directory, true);
      try
      {
        this.searcher = new IndexSearcher(reader);
      }
      catch (Exception ex)
      {
        this.reader.close();
        throw ex;
      }
      this.regCount = 0;
    }

    private Connection(IndexReader reader) throws Exception
    {
      this.reader = reader;
      try
      {
        this.searcher = new IndexSearcher(reader);
      }
      catch (Exception ex)
      {
        this.reader.close();
        throw ex;
      }
      this.regCount = 1;
    }

    public Connection register() throws Exception
    {
      if (Math.abs(System.currentTimeMillis() - this.lastReopenTime) > REOPEN_CHECK_INTERVAL)
      {
        IndexReader newreader = this.reader.reopen(true);
        if (this.reader != newreader)
          return new Connection(newreader);
        this.lastReopenTime = System.currentTimeMillis();
      }
      this.regCount++;
      return this;
    }

    public boolean unregister()
    {
      if (--this.regCount < 0)
        throw new IllegalStateException("regCount is " + this.regCount);
      return this.regCount == 0;
    }

    public TopDocs search(String searchQuery, Analyzer analyzer, int n) throws Exception
    {
      // Note: QueryParser is not
      // threadsafe(http://wiki.apache.org/lucene-java/LuceneFAQ#Is_the_QueryParser_thread-safe.3F),
      // so construct one all the time
      QueryParser parser = new QueryParser(Version.LUCENE_29, StandardLuceneEventHandler.CONTENTS_FIELD, analyzer);
      parser.setAllowLeadingWildcard(true);

      Query query = parser.parse(searchQuery);
      return this.searcher.search(query, null, n);
    }

    public Document getSearchDoc(int docid) throws Exception
    {
      return connection.searcher.doc(docid);
    }

    public void close()
    {
      try
      {
        this.searcher.close();
      }
      catch (Exception ex)
      {
        logger.warn("Closing searcher failed", ex);
      }

      try
      {
        this.reader.close();
      }
      catch (Exception ex)
      {
        logger.warn("Closing reader failed", ex);
      }
    }
  }

  /**
   * Constructor
   * 
   * @param name
   */
  public LuceneDataSource(String name)
  {
    super(name);

    this.indexDirectory = filterIndexDirectory(conf.getProperty("datasource." + name + ".fsDirectory"));

    if (null == this.indexDirectory)
      throw new RuntimeException("No connect information found - index directory is missing!");
  }

  protected LuceneDataSource(String name, String indexDirectory)
  {
    super(name);
    
    this.indexDirectory = filterIndexDirectory(indexDirectory);
  }

  /**
   * @param record
   * @throws Exception
   */
  public LuceneDataSource(IDataTableRecord record) throws Exception
  {
    super(record);

    String connectString;
    if (Datasource.location_ENUM._Webserver.equals(record.getStringValue(Datasource.location)))
    {
      String jndiName = record.getStringValue(Datasource.jndiname);

      if (null == jndiName)
        throw new RuntimeException("No connect information found - JNDI name is missing!");

      // try to resolve name space binding via JNDI
      //
      Object jndiObject;
      try
      {
        jndiObject = jndiLookup(jndiName);
      }
      catch (Exception ex)
      {
        throw new Exception("Accessing JNDI object '" + jndiName + "' failed", ex);
      }

      if (!(jndiObject instanceof String))
      {
        throw new Exception("JNDI object '" + jndiName + "' is not a String: " + (jndiObject == null ? "null" : jndiObject.getClass().getName()));
      }

      connectString = (String) jndiObject;
    }
    else
    {
      connectString = record.getStringValue(Datasource.connectstring);

      if (null == connectString)
        throw new RuntimeException("No connect information found - connectString is missing!");
    }

    this.indexDirectory = filterIndexDirectory(extractFromConnectString(connectString, "index:lucene:fsDirectory:"));

    if (null == this.indexDirectory)
      throw new RuntimeException("No connect information found - index directory is missing - expecting: \"index:lucene:fsDirectory:<mydirectory>\"!");
  }

  private static String filterIndexDirectory(String s)
  {
    if (s == null)
      return null;
    return StringUtils.replace(s, WEB_APPDIR_REPLACEMENT, Bootstrap.getApplicationRootPath());
  }
  
  private static String extractFromConnectString(String connectString, String parameter)
  {
    int pos = connectString.indexOf(parameter);
    if (-1 != pos)
    {
      int startPos = pos + parameter.length();
      int endPos = connectString.indexOf(";", startPos);
      if (-1 == endPos)
        return connectString.substring(startPos);
      return connectString.substring(startPos, endPos);
    }

    return null;
  }

  protected Directory getDirectory() throws Exception
  {
    return FSDirectory.open(new File(this.indexDirectory));
  }

  private synchronized Connection getConnection() throws Exception
  {
    // already done?
    if (this.connection == null)
    {
      this.connection = new Connection(getDirectory());
    }

    // will return new connection, if index has been changed
    this.connection = this.connection.register();

    return this.connection;
  }

  private synchronized void returnConnection(Connection connection) throws Exception
  {
    if (connection.unregister() && this.connection != connection)
      connection.close();
  }

  public void destroy()
  {
    if (this.connection != null)
    {
      // IBIS: really close? is done by returnConnection
      this.connection.close();
      this.connection = null;
    }

    super.destroy();
  }

  public String test() throws Exception
  {
    StringBuffer buffer = new StringBuffer();

    Connection connection = getConnection();
    try
    {
      // // simply try to get a context
      // DirContext context = getContext();
      // try
      // {
      // buffer.append("Environment:\r\n");
      // Hashtable table = context.getEnvironment();
      // Enumeration enumeration = table.keys();
      // while (enumeration.hasMoreElements())
      // {
      // Object key = enumeration.nextElement();
      // Object value = table.get(key);
      // buffer.append("\t").append(key).append(": ").append(value).append("\r\n");
      // }
      //	    
      // // IBIS: LDAP: What else to test?
      // }
      // finally
      // {
      // context.close();
      // }
    }
    finally
    {
      returnConnection(connection);
    }

    return buffer.toString();
  }

  private LuceneSearchQueryBuilder buildQuery(QBESpecification spec) throws InvalidExpressionException
  {
    boolean constraintExists = false;
    Context context = Context.getCurrent();
    LuceneSearchQueryBuilder queryBuilder = new LuceneSearchQueryBuilder(getLuceneEventHandler(context.getApplicationDefinition()).getAnalyzer(context.getApplicationLocale()));

    // -------------------------------------
    // process relation constraints (joins)
    // -------------------------------------
    // Lucene: No joins supported

    // -------------------------------------
    // process field constraints
    // -------------------------------------
    Iterator iter = spec.getFieldConstraints();
    while (iter.hasNext())
    {
      QBEFieldConstraint constraint = (QBEFieldConstraint) iter.next();

      ITableAlias alias = constraint.getTableAlias();
      ITableField field = constraint.getTableField();

      queryBuilder.setTableField(alias, field, constraint.isExactMatchRecursive());

      try
      {
        QBEExpression expr = field.getType().createQBEExpression(this, constraint);
        if (expr == null)
          continue;

        constraintExists = true;

        // Lucene: No constraint.isOptional()

        // Lucene: No aliasOr Handling

        queryBuilder.appendEmptyOrAnd();
        expr.makeConstraint(queryBuilder, false);
      }
      catch (UnsupportedOperationException ex)
      {
        throw new InvalidFieldExpressionException(field, new InvalidExpressionException(constraint.getQbeValue(), ex));
      }
      catch (InvalidExpressionException ex)
      {
        // exchange exception to add field info
        throw new InvalidFieldExpressionException(field, ex);
      }
      finally
      {
        queryBuilder.resetTableField();
      }
    }

    // -------------------------------------
    // process user where clauses
    // -------------------------------------
    //
    List userConstraints = spec.getUserConstraints();
    for (int i = 0; i < userConstraints.size(); i++)
    {
      constraintExists = true;
      QBEUserConstraint userConstraint = (QBEUserConstraint) userConstraints.get(i);
      queryBuilder.appendEmptyOrAnd().append("(").append(userConstraint.toString()).append(")");
    }

    // -------------------------------------
    // process inverse relations
    // -------------------------------------
    // Lucene: No inverse relations supported

    // -------------------------------------
    // process orderby clause
    // -------------------------------------
    // IBIS : Lucene: How to handle order by?

    // we do not allow unconstrained Lucene queries
    //
    if (!constraintExists)
      throw new UserRuntimeException(new CoreMessage(CoreMessage.UNCONSTRAINED_SEARCH));

    return queryBuilder;
  }
  
  private IDataSearchResult executeQuery(DataRecordSet recordSet, QBESpecification spec, LuceneSearchQueryBuilder searchQuery, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws Exception
  {
    DataSearchResult result = new DataSearchResult(callback == null);

    int maxRecords = recordSet.getMaxRecords();
    if (maxRecords == DataRecordSet.DEFAULT_MAX_RECORDS)
      maxRecords = Property.BROWSER_SYSTEM_MAX_RECORDS.getIntValue();

    // do not set count limit if filtering is activated!
    // if (maxRecords != DataRecordSet.UNLIMITED_RECORDS && !(eventHandler !=
    // null && eventHandler.isFilterSearchAction()))
    // constraints.setCountLimit(maxRecords + 1);
    // IBIS: TODO HACK
    if (maxRecords == DataRecordSet.UNLIMITED_RECORDS || callback != null)
      maxRecords = 1000;

    Connection connection = getConnection();
    try
    {
      long start = System.currentTimeMillis();
      Analyzer analyzer = searchQuery.getAnalyzer();
      String query = searchQuery.toString();
      TopDocs topDocs = connection.search(query, analyzer, maxRecords + 1);
      SQLMonitor.log(SQLMonitor.QUERY_LOG_TYPE, start, this, "Lucene: query=" + query + " analyzer=" + analyzer + " filter=" + null);

      List resultFields = spec.getFieldsToQuery();
      int[] primaryKeyIndices = spec.getPrimaryKeyIndices();
      boolean hasMore = true;
      int n = 0;
      while (result.getRecordCount() < maxRecords)
      {
        if (n >= topDocs.scoreDocs.length)
        {
          hasMore = false;
          break;
        }
        ScoreDoc scoreDoc = topDocs.scoreDocs[n++];
        Document document = connection.getSearchDoc(scoreDoc.doc);

        Object[] values = new Object[resultFields.size()];
        boolean[] isScoreValue = null;
        for (int i = 0; i < values.length; i++)
        {
          QBEField field = (QBEField) resultFields.get(i);
          if (field.isKeepEmpty())
          {
            values[i] = null;
          }
          else
          {
            try
            {
              String fieldDBName = field.getTableField().getDBName();
              Object fieldValue;
              Field docfield = document.getField(fieldDBName);
              if (docfield != null)
              {
                fieldValue = docfield.stringValue();
              }
              else
              {
                if (StandardLuceneEventHandler.ID_FIELD.equals(fieldDBName))
                  fieldValue = new Long(scoreDoc.doc);
                else if (StandardLuceneEventHandler.SCORE_FIELD.equals(fieldDBName))
                {
                  fieldValue = new Float(scoreDoc.score);
                  
                  if (isScoreValue == null)
                    isScoreValue = new boolean[resultFields.size()];
                  isScoreValue[i] = true;
                }
                else
                  fieldValue = null;
              }
              values[i] = field.getTableField().getType().convertObjectToDataValue(this, fieldValue, null, null);
            }
            catch (Exception ex)
            {
              logger.error("convertObjectToDataValue(): Problem with field " + field, ex);
              throw ex;
            }
          }
        }

        DataRecord record = instantiateRecord(recordSet, eventHandler, primaryKeyIndices, values, result.getRecordCount());

        // IBIS: HACK: Hier wollen wir verhindern, dass der Score-Value beim
        // Selektieren eines Browserrecords wieder durch den Tablerecord
        // überschrieben wird.
        //
        if (isScoreValue != null && record instanceof IDataBrowserModifiableRecord)
        {
          IDataBrowserModifiableRecord modifiableRec = (IDataBrowserModifiableRecord) record;
          for (int i = 0; i < isScoreValue.length; i++)
          {
            if (isScoreValue[i])
            {
              modifiableRec.setValue(i, modifiableRec.getValue(i));
            }
          }
        }

        // record filtered or not?
        if (record != null)
        {
          record.addStickyRetrievalObject(searchQuery);
          
          if (callback != null && !callback.onNextRecord(record))
            maxRecords = 0;
          else
            result.add(record);
        }
      }
      result.setHasMore(hasMore);
      return result;
    }
    finally
    {
      returnConnection(connection);
    }
  }
  
  public final Document[] executeQuery(LuceneSearchQueryBuilder searchQuery) throws Exception
  {
    Connection connection = getConnection();
    try
    {
      long start = System.currentTimeMillis();
      Analyzer analyzer = searchQuery.getAnalyzer();
      String query = searchQuery.toString();
      // IBIS: TODO HACK
      int maxRecords = 1000;
      TopDocs topDocs = connection.search(query, analyzer, maxRecords + 1);
      SQLMonitor.log(SQLMonitor.QUERY_LOG_TYPE, start, this, "Lucene: query=" + query + " analyzer=" + analyzer + " filter=" + null);

      Document[] result = new Document[topDocs.scoreDocs.length];
      for (int i = 0; i < result.length; i++)
      {
        result[i] = connection.getSearchDoc(topDocs.scoreDocs[i].doc);
      }
      return result;
    }
    finally
    {
      returnConnection(connection);
    }
  }
  
  /**
   * Internal method to fetch the query a given index record has been retrieved.
   * 
   * @param indexRecord
   * @return
   * @throws RecordNotFoundException
   */
  public LuceneSearchQueryBuilder getStickyRetrievalRecord(IDataRecord indexRecord) throws RecordNotFoundException
  {
    // some plausibility checks
    //
    if (indexRecord == null)
      throw new NullPointerException("record is null");

    String recordDatasourceName = indexRecord.getTableAlias().getTableDefinition().getDataSourceName();
    if (!getName().equals(recordDatasourceName))
      throw new RuntimeException("Record of datasource '" + recordDatasourceName + "' is incompatible with datasource '" + getName() + "'");

    IDataRecordInternal internalRec = (IDataRecordInternal) indexRecord;

    Object stickyRetrievalObject = internalRec.getStickyRetrievalObject();
    if (stickyRetrievalObject == null)
      throw new NullPointerException("Sticky retrieval object is null");
    if (!(stickyRetrievalObject instanceof LuceneSearchQueryBuilder))
      throw new RuntimeException("Class of sticky retrieval object '" + stickyRetrievalObject.getClass().getName() + "' is incompatible with class '" + LuceneSearchQueryBuilder.class.getName() + "'");

    return (LuceneSearchQueryBuilder) stickyRetrievalObject;
  }

  protected IDataSearchResult search(DataRecordSet recordSet, QBESpecification spec, IDataSearchIterateCallback callback, DataTableRecordEventHandler eventHandler) throws InvalidExpressionException
  {
    LuceneSearchQueryBuilder searchQuery = buildQuery(spec);

    try
    {
      return executeQuery(recordSet, spec, searchQuery, callback, eventHandler);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      // convert exception
      throw new RuntimeException(e);
    }
  }

  protected long count(DataRecordSet recordSet, QBESpecification spec) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }
  
  public IndexWriter getIndexWriter(Context context, boolean create) throws Exception
  {
    Analyzer analyzer = getLuceneEventHandler(context.getApplicationDefinition()).getAnalyzer(context.getApplicationLocale());
    return new IndexWriter(getDirectory(), analyzer, create, IndexWriter.MaxFieldLength.UNLIMITED);
  }

  public final LuceneEventHandler getLuceneEventHandler(IApplicationDefinition app)
  {
    IndexEventHandler handler = getEventHandler(app, getName());
    if (handler instanceof LuceneEventHandler)
      return (LuceneEventHandler) handler;
    throw new UserRuntimeException("Event handler class '" + getEventHandlerClassName(getName()) + "' is not a LuceneEventHandler or not existing");
  }
  
  private static final class Callback implements IDataTableSearchIterateCallback
  {
    private final IIndexUpdateContext indexUpdateContext;

    private Callback(IIndexUpdateContext indexUpdateContext)
    {
      this.indexUpdateContext = indexUpdateContext;
    }

    public boolean onNextRecord(IDataTableRecord record) throws Exception
    {
      this.indexUpdateContext.addToIndex(record);

      return true;
    }
  }

  public void rebuild(Context context) throws Exception
  {
    long start = System.currentTimeMillis();

    if (logger.isInfoEnabled())
      logger.info("Rebuilding index of data source '" + getName() + "' ...");
    
    // Delete index first
    //
    IndexWriter writer = getIndexWriter(context, true);
    try
    {
      writer.deleteAll();
      writer.commit();
    }
    finally
    {
      Lucene.close(writer);
    }

    // and rebuild index afterwards
    //
    IndexEventHandler handler = getEventHandler(context.getApplicationDefinition());
    IIndexUpdateContext indexUpdateContext = handler.newIndexUpdateContext(context, this);
    try
    {
      IApplicationDefinition applDef = context.getApplicationDefinition();
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      List aliases = applDef.getTableAliases();
      for (int i = 0; i < aliases.size(); i++)
      {
        ITableAlias alias = (ITableAlias) aliases.get(i);

        // Do not handle data from index data sources ;-)
        if (DataSource.get(alias.getTableDefinition().getDataSourceName()) instanceof IndexDataSource)
          continue;

        if (!handler.containsRecordsOfTableAlias(alias))
          continue;

        IDataTable dataTable = acc.getTable(alias);
        dataTable.searchAndIterate(new Callback(indexUpdateContext), applDef.getLocalRelationSet());
      }
      indexUpdateContext.flush();
    }
    finally
    {
      indexUpdateContext.close();
    }
    
    if (logger.isInfoEnabled())
      logger.info("Index of datasource '" + getName() + "' rebuild in " + (System.currentTimeMillis() - start) + " msecs");
  }

  public void optimize(Context context) throws Exception
  {
    long start = System.currentTimeMillis();

    if (logger.isInfoEnabled())
      logger.info("Optimizing index of data source '" + getName() + "' ...");
    
    IndexWriter writer = getIndexWriter(context, false);
    try
    {
      writer.optimize();
    }
    finally
    {
      Lucene.close(writer);
    }
    
    if (logger.isInfoEnabled())
      logger.info("Index of datasource '" + getName() + "' optimized in " + (System.currentTimeMillis() - start) + " msecs");
  }

}
