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

package de.tif.jacob.core.definition.fieldtypes;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.Locale;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.impl.DataDocument;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEConstraintBuilder;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.qbe.QBELiteralExpression;
import de.tif.jacob.core.data.impl.qbe.QBETextLiteral;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.DocumentInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.DocumentField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author andreas
 *
 */
public final class DocumentFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: DocumentFieldType.java,v 1.9 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  public DocumentFieldType()
  {
    // nothing more to do
  }

  public DocumentFieldType(DocumentField type)
  {
    // nothing more to do
  }
  
  // IBIS: Prüfen ob wir dann noch diese Methode brauchen!
  public boolean isConstrainable()
  {
    return true;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#isSortable()
	 */
	public boolean isSortable()
	{
	  // IBIS: Überprüfen ob doch
		return false;
	}

  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    if (style == IDataRecord.RAW_STYLE)
      throw new UnsupportedOperationException("Not for raw style");
    
    // return document name as string
    return ((DataDocument) value).getName();
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getDocumentAdjustment().setSQLValue(this, context, statement, index, dataValue);
  }

  private static class DocumentExpression extends QBEExpression
  {
    private final QBEExpression embedded;
    
    private DocumentExpression(QBEExpression embedded)
    {
      this.embedded = embedded;
    }

    public Integer getOrGroupNumber()
    {
      return this.embedded.getOrGroupNumber();
    }

    public void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
    {
      builder.appendDocumentExpression(embedded);
    }

    public void print(PrintWriter writer)
    {
      this.embedded.print(writer);
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    // fetch string
    Object value = constraint.getValue();
    String text;
    try
    {
      text = value.toString();
    }
    catch (Exception ex)
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }

    // and build expression
    if (constraint.isExactMatch())
      return new QBELiteralExpression(new QBETextLiteral(text));

    QBEExpression embedded = QBEExpression.parseText(text);
    if (embedded == null)
      return null;
    
    return new DocumentExpression(embedded);
  }

	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
	{
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().getInitDataValue(dataSource, table, field, null);
  }

	public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    tableField.getCastorTableFieldChoice().setDocument(new DocumentField());
	}

	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale)
	{
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().convertObjectToDataValue(object, oldValue);
  }

  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    DataDocument val1 = (DataDocument) dataValue1;
    DataDocument val2 = (DataDocument) dataValue2;
    
    if (val1.isNull())
      return val2.isNull() ? 0 : -1;
    if (val2.isNull())
      return 1;

    return val1.getName().compareTo(val2.getName());
  }
  
  public Object convertDataValueToObject(Object dataValue)
  {
		return ((DataDocument) dataValue).getValue();
  }
  
	public Object convertSQLValueToDataValue(SQLDataSource dataSource, ResultSet rs, int index) throws SQLException
	{
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().convertSQLValueToDataValue(this, dataSource, rs, index);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
  {
    // delegate request
    context.getDataSource().getAdjustment().getDocumentAdjustment().setSQLValueForInsert(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
    // delegate request
    context.getDataSource().getAdjustment().getDocumentAdjustment().setSQLValueForUpdate(this, context, statement, index, dataValue);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new DocumentInputFieldDefinition(name,null, null, null, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().getSQLType(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLSize(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().getSQLSize(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLDecimalDigits(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().getSQLDecimalDigits(this, dataSource);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getDocumentAdjustment().getSQLDefaultValue(this, dataSource);
  }
}
