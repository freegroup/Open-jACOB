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

import java.util.Calendar;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;

import de.tif.jacob.core.data.impl.index.event.StandardLuceneEventHandler;
import de.tif.jacob.core.data.impl.qbe.QBEConstraintBuilder;
import de.tif.jacob.core.data.impl.qbe.QBEDualOperator;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBELiteral;
import de.tif.jacob.core.data.impl.qbe.QBEUnaryOperator;
import de.tif.jacob.core.data.impl.qbe.QBEWildcard;
import de.tif.jacob.core.data.impl.qbe.QBEWildcardExpression;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * Lucene search query builder.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class LuceneSearchQueryBuilder extends QBEConstraintBuilder
{
  static public transient final String RCS_ID = "$Id: LuceneSearchQueryBuilder.java,v 1.3 2010/09/22 11:22:47 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";

  private final StringBuffer filterBuffer = new StringBuffer(256);
  private final Analyzer standardAnalyzer;
  private Analyzer defaultAnalyzer;
  private PerFieldAnalyzerWrapper perFieldAnalyzer;

  protected LuceneSearchQueryBuilder(Analyzer standardAnalyzer)
  {
    this.standardAnalyzer = standardAnalyzer;
  }

  protected LuceneSearchQueryBuilder append(String str)
  {
    this.filterBuffer.append(str);
    return this;
  }

  public LuceneSearchQueryBuilder append(char c)
  {
    this.filterBuffer.append(c);
    return this;
  }
  
  public LuceneSearchQueryBuilder appendEmptyOrAnd()
  {
    if (this.filterBuffer.length() > 0)
    {
      this.filterBuffer.append(" AND ");
    }
    return this;
  }
  
  public void setTableField(ITableAlias tableAlias, ITableField tableField, boolean useKeywordAnalyzer)
  {
    super.setTableField(tableAlias, tableField);
    Analyzer fieldAnalyzer = useKeywordAnalyzer ? new KeywordAnalyzer() : this.standardAnalyzer;
    if (this.defaultAnalyzer == null)
    {
      this.defaultAnalyzer = fieldAnalyzer;
    }
    else
    {
      if (!fieldAnalyzer.getClass().equals(this.defaultAnalyzer.getClass()))
      {
        // we need a PerFieldAnalyzerWrapper!
        if (this.perFieldAnalyzer == null)
          this.perFieldAnalyzer = new PerFieldAnalyzerWrapper(this.defaultAnalyzer);
        this.perFieldAnalyzer.addAnalyzer(tableField.getDBName(), fieldAnalyzer);
      }
    }
  }

  public Analyzer getAnalyzer()
  {
    if (this.perFieldAnalyzer != null)
      return this.perFieldAnalyzer;
    return this.defaultAnalyzer != null ? this.defaultAnalyzer : this.standardAnalyzer;
  }
  
  public LuceneSearchQueryBuilder appendDBName(String dbname)
  {
    if (StandardLuceneEventHandler.CONTENTS_FIELD.equals(dbname))
      return this;
    
    this.filterBuffer.append(dbname).append(":");
    return this;
  }
  
  public String toString()
  {
    return filterBuffer.toString();
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
    // TODO
    throw new UnsupportedOperationException();
//    if (inverse)
//      noConstraintError(operator, inverse);
//    else
//      append("=");
  }

  public boolean hasEqualsOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendGreaterThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    // TODO
    throw new UnsupportedOperationException();
//    if (inverse)
//      append("<=");
//    else
//      noConstraintError(operator, inverse);
  }

  public boolean hasGreaterThanOperator(boolean inverse)
  {
    // only inverse operator existing
    return inverse;
  }

  public void appendLessThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    // TODO
    throw new UnsupportedOperationException();
//    if (inverse)
//      append(">=");
//    else
//      noConstraintError(operator, inverse);
  }

  public boolean hasLessThanOperator(boolean inverse)
  {
    // only inverse operator existing
    return inverse;
  }

  public void appendGreaterOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    // TODO
    throw new UnsupportedOperationException();
//    if (inverse)
//      noConstraintError(operator, inverse);
//    else
//      append(">=");
  }

  public boolean hasGreaterOrEqualThanOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendLessOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    // TODO
    throw new UnsupportedOperationException();
//    if (inverse)
//      noConstraintError(operator, inverse);
//    else
//      append("<=");
  }

  public boolean hasLessOrEqualThanOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
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

  public void appendNullExpression(boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendBooleanLiteral(Boolean literal, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendNumericLiteral(Number number, boolean doNot) throws InvalidExpressionException
  {
    append(number.toString());
  }

  public void appendDateLiteral(Calendar date, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendTimestampLiteral(Calendar timestamp, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendTimeLiteral(Calendar time, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendTextLiteral(String value, boolean doNot) throws InvalidExpressionException
  {
    append(Lucene.convertToLucene(value, false));// true
  }
  
  private static boolean isLeftAnchored(TextFieldType textFieldType)
  {
    // Lucene QueryParser does not support wildcards at the beginning!
    return true;
  }

  private static boolean isRightAnchored(TextFieldType textFieldType)
  {
    // On Longtextfields we search without bounding
    if (textFieldType instanceof LongTextFieldType)
      return true;
    
    return textFieldType.isRightAnchored();
  }

  public void appendTextExpression(String value, boolean doNot) throws InvalidExpressionException
  {
    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    append("(");
    if (doNot)
      append("NOT ");
    appendDBName(getTableField().getDBName());
//    append(Lucene.QUOTE);
    if (!isLeftAnchored(textFieldType))
      appendWildcard(QBEWildcard.MULTIPLE);
    append(Lucene.convertToLucene(value, false));
    if (!isRightAnchored(textFieldType))
      appendWildcard(QBEWildcard.MULTIPLE);
//    append(Lucene.QUOTE);
    append(")");
  }

  private void appendWildcard(QBEWildcard wildcard) throws InvalidExpressionException
  {
    if (wildcard.isMultipleTimes())
    {
      append(Lucene.WILDCARD);
    }
    for (int i = 0; i < wildcard.getTimes(); i++)
    {
      append(Lucene.SWILDCARD);
    }
  }

  public void appendWildcardExpression(QBEWildcardExpression expression, boolean doNot) throws InvalidExpressionException
  {
    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    append("(");
    if (doNot)
      append("NOT ");
    appendDBName(getTableField().getDBName());
//    append(Lucene.QUOTE);
    if (!isLeftAnchored(textFieldType) && expression.isUnbound())
      appendWildcard(QBEWildcard.MULTIPLE);
    Iterator iter = expression.getParts().iterator();
    while (iter.hasNext())
    {
      Object element = iter.next();
      if (element instanceof String)
      {
        String text = (String) element;
        append(Lucene.convertToLucene(text, false));
      }
      else
      {
        appendWildcard((QBEWildcard) element);
      }
    }
    if (!isRightAnchored(textFieldType) && expression.isUnbound())
      appendWildcard(QBEWildcard.MULTIPLE);
//    append(Lucene.QUOTE);
    append(")");
  }

  public void appendLiteral(QBELiteral literal, boolean doNot) throws InvalidExpressionException
  {
    // TODO check
    if (doNot)
      append("NOT ");
    appendDBName(getTableField().getDBName());
    literal.makeConstraint(this, false);
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
      appendDBName(getTableField().getDBName());
      operator.toConstraint(this, doNot);
      expression.makeConstraint(this, false);
      // append(")");
    }
  }

  public void appendDualExpression(QBEExpression leftExpression, QBEDualOperator operator, QBEExpression rightExpression, boolean doNot) throws InvalidExpressionException
  {
    leftExpression.makeConstraint(this, false);
    append(" ");
    operator.toConstraint(this, doNot);
    append(" ");
    rightExpression.makeConstraint(this, false);
  }

  public void appendRangeExpression(QBEExpression leftExpression, QBEExpression rightExpression, boolean rightInclusive, boolean doNot) throws InvalidExpressionException
  {
    if (doNot)
      throw new UnsupportedOperationException();

    append("(");
    appendDBName(getTableField().getDBName());
    append("[");
    leftExpression.makeConstraint(this, false);
    append(" To ");
    rightExpression.makeConstraint(this, false);
    if (rightInclusive)
      append("]");
    else
      append("}");
    append(")");
  }

  public void appendDocumentExpression(QBEExpression embedded) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }
}
