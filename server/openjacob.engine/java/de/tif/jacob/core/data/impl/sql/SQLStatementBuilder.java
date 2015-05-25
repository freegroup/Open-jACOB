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
 * Created on 03.09.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import de.tif.jacob.core.data.impl.qbe.QBEBooleanLiteral;
import de.tif.jacob.core.data.impl.qbe.QBEConstraintBuilder;
import de.tif.jacob.core.data.impl.qbe.QBEDateLiteral;
import de.tif.jacob.core.data.impl.qbe.QBEDualOperator;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBELiteral;
import de.tif.jacob.core.data.impl.qbe.QBERangeExpression;
import de.tif.jacob.core.data.impl.qbe.QBETimestampLiteral;
import de.tif.jacob.core.data.impl.qbe.QBEUnaryOperator;
import de.tif.jacob.core.data.impl.qbe.QBEWildcard;
import de.tif.jacob.core.data.impl.qbe.QBEWildcardExpression;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * SQL constraint statement builder
 * 
 * @author Andreas Sonntag
 */
public final class SQLStatementBuilder extends QBEConstraintBuilder
{
	static public final transient String RCS_ID = "$Id: SQLStatementBuilder.java,v 1.3 2009/07/30 18:07:31 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final SQLDataSource datasource;
	private final StringBuffer sqlBuffer;
	private final boolean useAliasNames;
	private boolean first = true;
	
	public SQLStatementBuilder(SQLDataSource datasource, boolean useAliasNames)
	{
    this.datasource = datasource;
		this.sqlBuffer = new StringBuffer(1000);
		this.useAliasNames = useAliasNames; 
	}
	
	public SQLStatementBuilder appendDBName(SQLDataSource datasource, String dbname)
	{
	  datasource.appendDBName(this.sqlBuffer, dbname);
		return this;
	}
	
	public SQLStatementBuilder append(String str)
	{
		this.sqlBuffer.append(str);
		return this;
	}
	
	public SQLStatementBuilder append(char c)
	{
		this.sqlBuffer.append(c);
		return this;
	}
	
	public SQLStatementBuilder append(int i)
	{
		this.sqlBuffer.append(i);
		return this;
	}
	
	public SQLStatementBuilder appendWhereOrAnd()
	{
		if (this.first)
		{
			this.first = false;
			this.sqlBuffer.append(" WHERE ");
		}
		else
		{
			this.sqlBuffer.append(" AND ");
		}
		return this;
	}
	
	public boolean useAliasNames()
	{
		return this.useAliasNames;
	}

	public String toString()
	{
		return sqlBuffer.toString();
	}

  public void appendNotOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append("NOT");
  }

  public boolean hasNotOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendEqualsOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append("<>");
    else
      append("=");
  }

  public boolean hasEqualsOperator(boolean inverse)
  {
    return true;
  }

  public void appendGreaterThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append("<=");
    else
      append(">");
  }

  public boolean hasGreaterThanOperator(boolean inverse)
  {
    return true;
  }

  public void appendLessThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append(">=");
    else
      append("<");
  }

  public boolean hasLessThanOperator(boolean inverse)
  {
    return true;
  }

  public void appendGreaterOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append("<");
    else
      append(">=");
  }

  public boolean hasGreaterOrEqualThanOperator(boolean inverse)
  {
    return true;
  }

  public void appendLessOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append(">");
    else
      append("<=");
  }

  public boolean hasLessOrEqualThanOperator(boolean inverse)
  {
    return true;
  }

  public void appendAndOperator(QBEDualOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append("AND");
  }

  public boolean hasAndOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendOrOperator(QBEDualOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append("OR");
  }

  public boolean hasOrOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendBooleanLiteral(Boolean literal, boolean doNot) throws InvalidExpressionException
  {
    // Attention: do not use this method, since TRUE means <>0 !!!
    throw new IllegalStateException();
  }

  public void appendDateLiteral(Calendar date, boolean doNot) throws InvalidExpressionException
  {
    if (getTableField().getType() instanceof TimestampFieldType)
      append(datasource.toQueryString(new Timestamp(date.getTimeInMillis())));
    else
      append(datasource.toQueryString(new Date(date.getTimeInMillis())));
  }

  public void appendDocumentExpression(QBEExpression embedded) throws InvalidExpressionException
  {
    // delegate request
    datasource.getAdjustment().getDocumentAdjustment().makeSQL(datasource, getTableAlias(), getTableField(), this, embedded);
  }

  public void appendDualExpression(QBEExpression leftExpression, QBEDualOperator operator, QBEExpression rightExpression, boolean doNot)
      throws InvalidExpressionException
  {
    leftExpression.makeConstraint(this, false);
    append(" ");
    operator.toConstraint(this, doNot);
    append(" ");
    rightExpression.makeConstraint(this, false);
  }

  public void appendLiteral(QBELiteral literal, boolean doNot) throws InvalidExpressionException
  {
    if ((literal instanceof QBEDateLiteral) && (getTableField().getType() instanceof TimestampFieldType))
    {
      // Interpret date literal as range for timestamp fields!
      QBEDateLiteral dateLiteral = (QBEDateLiteral) literal;

      // calculate next day of original date
      Calendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(dateLiteral.getCalendar().getTimeInMillis());
      calendar.setLenient(true);
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
      
      // and print out range SQL expression
      QBERangeExpression rangeExpression = new QBERangeExpression(new QBETimestampLiteral(dateLiteral), new QBETimestampLiteral(calendar), false);
      rangeExpression.makeConstraint(this, doNot);
    }
    else if (literal instanceof QBEBooleanLiteral)
    {
      if (this.useAliasNames)
        appendDBName(datasource, getTableAlias().getName()).append(".");
      appendDBName(datasource, getTableField().getDBName());
      if (((QBEBooleanLiteral) literal).isTrue() ^ doNot)
        append("<>0");
      else
        append("=0");
    }
    else
    {
      if (this.useAliasNames)
        appendDBName(datasource, getTableAlias().getName()).append(".");
      appendDBName(datasource, getTableField().getDBName());
      if (doNot)
        append("<>");
      else
        append("=");
      literal.makeConstraint(this, false);
    }
  }

  public void appendNullExpression(boolean doNot) throws InvalidExpressionException
  {
    // append("(");
    if (this.useAliasNames)
      appendDBName(datasource, getTableAlias().getName()).append(".");
    appendDBName(datasource, getTableField().getDBName());
    if (doNot)
      append(" IS NOT NULL");
    else
      append(" IS NULL");
    // append(")");
  }

  public void appendNumericLiteral(Number number, boolean doNot) throws InvalidExpressionException
  {
    append(number.toString());
  }

  public void appendRangeExpression(QBEExpression leftExpression, QBEExpression rightExpression, boolean rightInclusive, boolean doNot) throws InvalidExpressionException
  {
    append("(");
    if (this.useAliasNames)
      appendDBName(datasource, getTableAlias().getName()).append(".");
    appendDBName(datasource, getTableField().getDBName());
    append(doNot ? "<" : ">=");
    leftExpression.makeConstraint(this, false);
    append(doNot ? " OR " : " AND ");
    if (this.useAliasNames)
      appendDBName(datasource, getTableAlias().getName()).append(".");
    appendDBName(datasource, getTableField().getDBName());
    if (rightInclusive)
      append(doNot ? ">" : "<=");
    else
      append(doNot ? ">=" : "<");
    rightExpression.makeConstraint(this, false);
    append(")");
  }
  
  private void appendWildcard(QBEWildcard wildcard)
  {
    if (wildcard.isMultipleTimes())
    {
      append(SQL.WILDCARD);
    }
    for (int i = 0; i < wildcard.getTimes(); i++)
    {
      append(SQL.SWILDCARD);
    }
  }

  public void appendTextExpression(String value, boolean doNot) throws InvalidExpressionException
  {
    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    boolean toLower = useLowerFunction(textFieldType);
    
    // append("(");
    if (toLower)
      append(datasource.getLowerFunctionName()).append("(");
    if (this.useAliasNames)
      appendDBName(datasource, getTableAlias().getName()).append(".");
    appendDBName(datasource, getTableField().getDBName());
    if (toLower)
      append(")");
    if (doNot)
      append(" NOT LIKE ");
    else
      append(" LIKE ");
    append(SQL.QUOTE);
    if (!textFieldType.isLeftAnchored())
      appendWildcard(QBEWildcard.MULTIPLE);
    append(datasource.convertToSQL(toLower ? value.toLowerCase() : value, false));
    if (!textFieldType.isRightAnchored())
      appendWildcard(QBEWildcard.MULTIPLE);
    append(SQL.QUOTE);
    //  append(")");
  }

  public void appendTextLiteral(String value, boolean doNot) throws InvalidExpressionException
  {
    if (getTableField().getType() instanceof EnumerationFieldType)
    {
      EnumerationFieldType enumFieldType = (EnumerationFieldType) getTableField().getType();
      enumFieldType.appendSqlEnumValue(datasource, this, value);
    }
    else
    {
      append(datasource.convertToSQL(value, true));
    }
  }

  public void appendTimeLiteral(Calendar time, boolean doNot) throws InvalidExpressionException
  {
    append(datasource.toQueryString(new Time(time.getTimeInMillis())));
  }

  public void appendTimestampLiteral(Calendar timestamp, boolean doNot) throws InvalidExpressionException
  {
    append(datasource.toQueryString(new Timestamp(timestamp.getTimeInMillis())));
  }

  public void appendUnaryExpression(QBEUnaryOperator operator, QBEExpression expression, boolean doNot) throws InvalidExpressionException
  {
    if (operator.equals(QBEUnaryOperator.NOT))
    {
      doNot = !doNot;
      expression.makeConstraint(this, doNot);
    }
    else
    {
      // append("(");
      if (this.useAliasNames)
        appendDBName(datasource, getTableAlias().getName()).append(".");
      appendDBName(datasource, getTableField().getDBName());
      operator.toConstraint(this, doNot);
      expression.makeConstraint(this, false);
      // append(")");
    }
  }
  
  private boolean useLowerFunction(TextFieldType textFieldType)
  {
    if (textFieldType instanceof LongTextFieldType)
    {
      return !textFieldType.isCaseSensitive() && datasource.supportsLowerFunctionForLongText();
    }
    return !textFieldType.isCaseSensitive();
  }

  public void appendWildcardExpression(QBEWildcardExpression expression, boolean doNot) throws InvalidExpressionException
  {
    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    boolean toLower = useLowerFunction(textFieldType);
    
    append("(");
    if (toLower)
      append(datasource.getLowerFunctionName()).append("(");
    if (this.useAliasNames)
      appendDBName(datasource, getTableAlias().getName()).append(".");
    appendDBName(datasource, getTableField().getDBName());
    if (toLower)
      append(")");
    if (doNot)
      append(" NOT LIKE ");
    else
      append(" LIKE ");
    append(SQL.QUOTE);
    if (!textFieldType.isLeftAnchored() && expression.isUnbound())
      appendWildcard(QBEWildcard.MULTIPLE);
    Iterator iter = expression.getParts().iterator();
    while (iter.hasNext())
    {
      Object element = iter.next();
      if (element instanceof String)
      {
        String text = (String) element;
        append(datasource.convertToSQL(toLower ? text.toLowerCase() : text, false));
      }
      else
      {
        appendWildcard((QBEWildcard) element);
      }
    }
    if (!textFieldType.isRightAnchored() && expression.isUnbound())
      appendWildcard(QBEWildcard.MULTIPLE);
    append(SQL.QUOTE);
    append(")");
  }
}
