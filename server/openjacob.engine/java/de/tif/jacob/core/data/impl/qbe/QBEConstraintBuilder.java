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

package de.tif.jacob.core.data.impl.qbe;

import java.util.Calendar;

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * QBE constraint builder
 * 
 * @author Andreas Sonntag
 */
public abstract class QBEConstraintBuilder
{
  static public transient final String RCS_ID = "$Id: QBEConstraintBuilder.java,v 1.3 2009/03/18 11:34:58 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";

  private ITableAlias tableAlias;
  private ITableField tableField;

  protected QBEConstraintBuilder()
  {
  }
  
  public final void setTableField(ITableAlias tableAlias, ITableField tableField)
  {
    this.tableAlias = tableAlias;
    this.tableField = tableField;
  }

  public final void resetTableField()
  {
    this.tableAlias = null;
    this.tableField = null;
  }

  public final ITableAlias getTableAlias()
  {
    if (this.tableAlias == null)
      throw new IllegalStateException("No table alias set");
    return tableAlias;
  }

  public final ITableField getTableField()
  {
    if (this.tableField == null)
      throw new IllegalStateException("No table field set");
    return tableField;
  }

  protected final void noConstraintError(QBEOperator operator, boolean inverse)
  {
    if (inverse)
      throw new RuntimeException("No inverse constraint defined for operator " + operator);
    else
      throw new RuntimeException("No constraint defined for operator " + operator);
  }
  
  // Unary operators
  
  public abstract void appendNotOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasNotOperator(boolean inverse);

  public abstract void appendEqualsOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasEqualsOperator(boolean inverse);

  public abstract void appendGreaterThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasGreaterThanOperator(boolean inverse);

  public abstract void appendLessThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasLessThanOperator(boolean inverse);

  public abstract void appendGreaterOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasGreaterOrEqualThanOperator(boolean inverse);

  public abstract void appendLessOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasLessOrEqualThanOperator(boolean inverse);

  // Dual operators

  public abstract void appendAndOperator(QBEDualOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasAndOperator(boolean inverse);

  public abstract void appendOrOperator(QBEDualOperator operator, boolean inverse) throws InvalidExpressionException;

  public abstract boolean hasOrOperator(boolean inverse);

  // Expressions
  
  public abstract void appendLiteral(QBELiteral literal, boolean doNot) throws InvalidExpressionException;

  public abstract void appendUnaryExpression(QBEUnaryOperator operator, QBEExpression expression, boolean doNot) throws InvalidExpressionException;

  public abstract void appendDualExpression(QBEExpression leftExpression, QBEDualOperator operator, QBEExpression rightExpression, boolean doNot)
      throws InvalidExpressionException;

  public abstract void appendTextExpression(String value, boolean doNot) throws InvalidExpressionException;

  public abstract void appendWildcardExpression(QBEWildcardExpression expression, boolean doNot) throws InvalidExpressionException;

  public abstract void appendTextLiteral(String value, boolean doNot) throws InvalidExpressionException;

  public abstract void appendBooleanLiteral(Boolean literal, boolean doNot) throws InvalidExpressionException;

  public abstract void appendNumericLiteral(Number number, boolean doNot) throws InvalidExpressionException;

  public abstract void appendDateLiteral(Calendar date, boolean doNot) throws InvalidExpressionException;

  public abstract void appendTimestampLiteral(Calendar timestamp, boolean doNot) throws InvalidExpressionException;

  public abstract void appendTimeLiteral(Calendar time, boolean doNot) throws InvalidExpressionException;

  public abstract void appendRangeExpression(QBEExpression leftExpression, QBEExpression rightExpression, boolean rightInclusive, boolean doNot)
      throws InvalidExpressionException;

  public abstract void appendNullExpression(boolean doNot) throws InvalidExpressionException;

  public abstract void appendDocumentExpression(QBEExpression embedded) throws InvalidExpressionException;
}
