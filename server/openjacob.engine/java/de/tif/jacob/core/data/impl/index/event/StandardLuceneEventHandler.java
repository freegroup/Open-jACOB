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

package de.tif.jacob.core.data.impl.index.event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.InMemoryLuceneDataSource;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.index.Lucene;
import de.tif.jacob.core.data.impl.index.LuceneDataSource;
import de.tif.jacob.core.data.impl.index.LuceneTika;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.data.internal.IDataRecordInternal;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.DocumentFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;

/**
 * Standard Lucene event handler implementation.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public class StandardLuceneEventHandler extends LuceneEventHandler
{
  static public transient final String RCS_ID = "$Id: StandardLuceneEventHandler.java,v 1.9 2010/09/25 20:57:59 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.9 $";

  private static final transient Log logger = Lucene.logger;

  public static final String ID_FIELD = "id";
  public static final String SCORE_FIELD = "score";
  public static final String TABLEALIAS_FIELD = "tablealias";
  public static final String TABLENAME_FIELD = "tablename";
  public static final String TABLEFIELD_FIELD = "tablefield";
  public static final String PRIMARYKEY_FIELD = "primarykey";
  public static final String SUBJECT_FIELD = "subject";
  public static final String CATEGORY_FIELD = "category";
  public static final String CONTENTS_FIELD = "contents";

  /**
   * {@inheritDoc}
   */
  public boolean containsRecordsOfTableAlias(ITableAlias tableAlias)
  {
    ITableDefinition tableDef = tableAlias.getTableDefinition();

    if (!tableDef.getName().equals(tableAlias.getName()))
      return false;

    if (tableDef.getRepresentativeField() == null)
      return false;

    if (tableDef.getPrimaryKey() == null)
      return false;

    return true;
  }

  private LuceneDataSource getLuceneDataSource(DataSource dataSource) throws UserException
  {
    if (dataSource instanceof LuceneDataSource)
      return (LuceneDataSource) dataSource;
    throw new UserException("Data source '" + dataSource.getName() + "' is not a Lucene data source");
  }

  private final class IndexUpdateContext implements IIndexUpdateContext
  {
    private final IndexWriter writer;
    private final Context context;
    private final boolean fieldLevel;

    private IndexUpdateContext(LuceneDataSource dataSource, Context context, boolean fieldLevel, boolean create) throws Exception
    {
      this.writer = dataSource.getIndexWriter(context, create);
      this.context = context;
      this.fieldLevel = fieldLevel;
    }

    public void addToIndex(IDataTableRecord tableRecord) throws Exception
    {
      writeRecord(this.context, this.writer, tableRecord, this.fieldLevel);
    }

    public void removeFromIndex(ITableAlias alias, IDataKeyValue primaryKeyValue) throws Exception
    {
      ITableDefinition tableDef = alias.getTableDefinition();
      TermQuery tableQuery = new TermQuery(new Term(TABLENAME_FIELD, tableDef.getName()));
      TermQuery pkeyQuery = new TermQuery(new Term(PRIMARYKEY_FIELD, tableDef.getPrimaryKey().convertKeyValueToString(primaryKeyValue)));
      BooleanQuery query = new BooleanQuery();
      query.add(tableQuery, Occur.MUST);
      query.add(pkeyQuery, Occur.MUST);
      writer.deleteDocuments(query);
    }

    public void updateWithinIndex(IDataTableRecord tableRecord) throws Exception
    {
      // simply remove and add again
      removeFromIndex(tableRecord.getTableAlias(), tableRecord.getPrimaryKeyValue());
      addToIndex(tableRecord);
    }

    public void flush() throws Exception
    {
      this.writer.commit();
    }

    public void close()
    {
      Lucene.close(this.writer);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final IIndexUpdateContext newIndexUpdateContext(Context context, IndexDataSource dataSource) throws Exception
  {
    return new IndexUpdateContext(getLuceneDataSource(dataSource), context, false, false);
  }

  /**
   * Checks whether values of the given table field should be added as content
   * (see {@link #CONTENTS_FIELD}) for data table records of the given alias to
   * the index.
   * <p>
   * The default implementation of this method returns <code>true</code> for
   * every text, long text (excluding history fields) and document field. In all
   * other cases <code>false</code> is returned.
   * 
   * @param context
   *          the current application context
   * @param alias
   *          the table alias
   * @param field
   *          the table field
   * @return <code>true</code> if values of the given table field should be
   *         added to the index, otherwise <code>false</code>
   * @throws Exception
   *           on any problem
   * @see #CONTENTS_FIELD           
   */
  public boolean addToContentsField(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    FieldType type = field.getType();
    if (type instanceof TextFieldType)
    {
      // do not index history fields!
      ITableField historyField = alias.getTableDefinition().getHistoryField();
      return historyField == null || !historyField.equals(field);
    }
    return type instanceof DocumentFieldType;
  }

  /**
   * Returns the boost factor for values of the given table field when added as
   * content (see {@link #CONTENTS_FIELD}) for data table records of the given
   * alias to the index.
   * <p>
   * The default implementation of this method returns <code>1.0f</code> for
   * each table field. To boost certain table field, a factor of greater
   * <code>1</code> should be returned.
   * 
   * @param context
   *          the current application context
   * @param alias
   *          the table alias
   * @param field
   *          the table field
   * @return the boost factor of the given table field
   * @throws Exception
   *           on any problem
   * @see #CONTENTS_FIELD
   */
  public float getContentsFieldBoost(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    return 1.0f;
  }

  /**
   * Returns the content type of the given table field.
   * <p>
   * Example: If this method returns <code>text/html</code>, the table field
   * values will be HTML parsed to only added the real content to the index,
   * i.e. skipping any HTML tags.
   * <p>
   * The default implementation of this method always returns
   * <code>text/plain</code>. Which means that the content is not parsed, i.e.
   * the content will be completely indexed.
   * <p>
   * Note: This method will only be invoked for text fields, i.e. fields of type
   * {@link TextFieldType}!
   * 
   * @param context
   *          the current application context
   * @param alias
   *          the table alias
   * @param field
   *          the table field
   * @return the content type or <code>null</code> to try to automatically
   *         detect content type
   * @throws Exception
   *           on any problem
   */
  public String getContentType(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    return "text/plain";
  }

  /**
   * Returns the subject string of the given data table record, which will be
   * added as {@link #SUBJECT_FIELD} field to the index.
   * <p>
   * The default implementation of this method returns the string presentation
   * of the representative field of the corresponding table definition (see
   * {@link ITableDefinition#getRepresentativeField()}) or <code>null</code>, if
   * no representative field exists for that table.
   * <p>
   * The subject string should give the user an idea about which data entity has
   * been retrieved from an index in a human understandable manner.
   * 
   * @param context
   *          the current application context
   * @param record
   *          the data table record
   * @return the subject string of the given data table record or
   *         <code>null</code>
   * @throws Exception
   *           on any problem
   */
  public String getSubjectFieldValue(Context context, IDataTableRecord record) throws Exception
  {
    ITableField repField = record.getTableAlias().getTableDefinition().getRepresentativeField();
    if (repField != null)
      return record.getStringValue(repField.getName(), context.getApplicationLocale());
    return null;
  }

  /**
   * Returns the category string of the given data table record, which will be
   * added as {@link #CATEGORY_FIELD} field to the index.
   * <p>
   * The default implementation of this method always returns <code>null</code>,
   * i.e. category feature not available by default.
   * 
   * @param context
   *          the current application context
   * @param record
   *          the data table record
   * @return the category string of the given data table record or
   *         <code>null</code>
   * @throws Exception
   *           on any problem
   */
  public String getCategoryFieldValue(Context context, IDataTableRecord record) throws Exception
  {
    return null;
  }

  private void writeRecord(Context context, IndexWriter writer, IDataTableRecord record, boolean fieldLevel) throws Exception
  {
    // make a new, empty document
    Document doc = new Document();

    ITableAlias alias = record.getTableAlias();
    ITableDefinition tableDef = alias.getTableDefinition();

    doc.add(new Field(TABLEALIAS_FIELD, alias.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
    doc.add(new Field(TABLENAME_FIELD, tableDef.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));

    IKey pkey = tableDef.getPrimaryKey();
    if (pkey != null)
    {
      IDataKeyValue pkeyvalue = record.getPrimaryKeyValue();
      doc.add(new Field(PRIMARYKEY_FIELD, pkey.convertKeyValueToString(pkeyvalue), Field.Store.YES, Field.Index.NOT_ANALYZED));
    }

    {
      String subject = getSubjectFieldValue(context, record);
      if (subject != null)
        doc.add(new Field(SUBJECT_FIELD, subject, Field.Store.YES, Field.Index.ANALYZED));
    }

    {
      String category = getCategoryFieldValue(context, record);
      if (category != null)
        doc.add(new Field(CATEGORY_FIELD, category, Field.Store.YES, Field.Index.NOT_ANALYZED));
    }

    List fields = tableDef.getTableFields();
    for (int i = 0; i < fields.size(); i++)
    {
      ITableField field = (ITableField) fields.get(i);
      if (addToContentsField(context, alias, field))
      {
        float boost = getContentsFieldBoost(context, alias, field);
        
        String value = record.getStringValue(i, context.getApplicationLocale());
        if (value != null)
        {
          // IBIS: Not all should be analyzed
          // IBIS: How to handle Date, Timestamp, Time values?

          if (field.getType() instanceof DocumentFieldType)
          {
            DataDocumentValue docvalue = record.getDocumentValue(i);
            try
            {
              LuceneTika.addToLuceneDocument(doc, CONTENTS_FIELD, docvalue, boost);
            }
            catch (RuntimeException ex)
            {
              throw ex;
            }
            catch (Exception ex)
            {
              if (logger.isWarnEnabled())
                logger.warn("Can not add document '" + docvalue.getName() + "' to index: " + ex.toString());
            }
          }
          else
          {
            String contentType;
            if ((field.getType() instanceof TextFieldType) && !"text/plain".equals(contentType = getContentType(context, alias, field)))
            {
              LuceneTika.addToLuceneDocument(doc, CONTENTS_FIELD, value, contentType, boost);
            }
            else
            {
              Field docfield = new Field(CONTENTS_FIELD, value, Field.Store.NO, Field.Index.ANALYZED);
              docfield.setBoost(boost);
              doc.add(docfield);
            }
          }

          if (fieldLevel)
          {
            doc.add(new Field(TABLEFIELD_FIELD, field.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            writer.addDocument(doc);
            doc.removeField(TABLEFIELD_FIELD);
            doc.removeFields(CONTENTS_FIELD);
          }
        }
      }
    }

    // return the document
    if (!fieldLevel)
      writer.addDocument(doc);
  }

  /**
   * {@inheritDoc}
   */
  public final Set getMatchingTableFields(IDataRecord indexRecord) throws Exception
  {
    LuceneDataSource luceneDatasource = getLuceneDataSource(DataSource.get(indexRecord.getTableAlias().getTableDefinition().getDataSourceName()));

    IDataTableRecord referencedTableRecord = getReferencedTableRecord(indexRecord);
    
    // Create temporary Lucene data source
    InMemoryLuceneDataSource tempDataSource = new InMemoryLuceneDataSource("TempInMemoryLuceneDataSource");
    try
    {
      // add the original record to the temporary index on field level
      //
      IIndexUpdateContext indexUpdateContext = new IndexUpdateContext(tempDataSource, Context.getCurrent(), true, true);
      try
      {
        indexUpdateContext.addToIndex(referencedTableRecord);
        indexUpdateContext.flush();
      }
      finally
      {
        indexUpdateContext.close();
      }

      // execute the original query again on the temporary index and determine which table fields are matching
      //
      Set tableFields = new HashSet();
      ITableDefinition referencedTableDef = referencedTableRecord.getTableAlias().getTableDefinition();
      Document[] documents = tempDataSource.executeQuery(luceneDatasource.getStickyRetrievalRecord(indexRecord));
      for (int i = 0; i < documents.length; i++)
      {
        String fieldName = documents[i].getField(TABLEFIELD_FIELD).stringValue();
        tableFields.add(referencedTableDef.getTableField(fieldName));
      }
      return tableFields;
    }
    finally
    {
      tempDataSource.destroy();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public final IDataTableRecord getReferencedTableRecord(IDataRecord indexRecord) throws RecordNotFoundException
  {
    try
    {
      IApplicationDefinition applDef = indexRecord.getAccessor().getApplication();
      IDataTableRecord tableIndexRecord = ((IDataRecordInternal)indexRecord).getTableRecord();
      ITableAlias alias = applDef.getTableAlias(tableIndexRecord.getStringValue(TABLEALIAS_FIELD));
      IDataKeyValue pkeyvalue = alias.getTableDefinition().getPrimaryKey().convertStringToKeyValue(tableIndexRecord.getStringValue(PRIMARYKEY_FIELD));
      return indexRecord.getAccessor().getTable(alias).getRecord(pkeyvalue);
    }
    catch (NoSuchFieldException ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
