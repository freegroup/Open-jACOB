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

package de.tif.jacob.core.data.impl.ldap;

import java.util.Calendar;
import java.util.Iterator;

import de.tif.jacob.core.data.impl.qbe.QBEConstraintBuilder;
import de.tif.jacob.core.data.impl.qbe.QBEDualOperator;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBELiteral;
import de.tif.jacob.core.data.impl.qbe.QBEUnaryOperator;
import de.tif.jacob.core.data.impl.qbe.QBEWildcard;
import de.tif.jacob.core.data.impl.qbe.QBEWildcardExpression;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * LDAP search filter builder.
 * 
 * @author Andreas Sonntag
 */
public final class LdapSearchFilterBuilder extends QBEConstraintBuilder
{
  static public transient final String RCS_ID = "$Id: LdapSearchFilterBuilder.java,v 1.2 2008-01-24 14:39:21 sonntag Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  private final StringBuffer filterBuffer = new StringBuffer(256);

  protected LdapSearchFilterBuilder()
  {
  }

  protected LdapSearchFilterBuilder append(String str)
  {
    this.filterBuffer.append(str);
    return this;
  }

  public String toString()
  {
    return filterBuffer.toString();
  }
  
  public void appendNotOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    noConstraintError(operator, inverse);
  }

  public boolean hasNotOperator(boolean inverse)
  {
    return false;
  }

  public void appendEqualsOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append("=");
  }

  public boolean hasEqualsOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendGreaterThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append("<=");
    else
      noConstraintError(operator, inverse);
  }

  public boolean hasGreaterThanOperator(boolean inverse)
  {
    // only inverse operator existing
    return inverse;
  }

  public void appendLessThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      append(">=");
    else
      noConstraintError(operator, inverse);
  }

  public boolean hasLessThanOperator(boolean inverse)
  {
    // only inverse operator existing
    return inverse;
  }

  public void appendGreaterOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append(">=");
  }

  public boolean hasGreaterOrEqualThanOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendLessOrEqualThanOperator(QBEUnaryOperator operator, boolean inverse) throws InvalidExpressionException
  {
    if (inverse)
      noConstraintError(operator, inverse);
    else
      append("<=");
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
      append("&");
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
      append("|");
  }

  public boolean hasOrOperator(boolean inverse)
  {
    // no inverse operator existing
    return !inverse;
  }

  public void appendNullExpression(boolean doNot) throws InvalidExpressionException
  {
    append("(");
    if (!doNot)
      append("!(");
    append(getTableField().getDBName());
    append("=*");
    if (!doNot)
      append(")");
    append(")");
  }

  public void appendBooleanLiteral(Boolean literal, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }

  public void appendNumericLiteral(Number number, boolean doNot) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
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
    append(LdapDataSource.convertToLdap(value));
  }

  public void appendTextExpression(String value, boolean doNot) throws InvalidExpressionException
  {
    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    append("(");
    // IBIS: LDAP: How to do case sensitive?
    if (doNot)
      append("!(");
    append(getTableField().getDBName());
    append("=");
    if (!textFieldType.isLeftAnchored())
      append("*");
    append(LdapDataSource.convertToLdap(value));
    if (!textFieldType.isRightAnchored())
      append("*");
    if (doNot)
      append(")");
    append(")");
  }

  private void appendWildcard(QBEWildcard wildcard) throws InvalidExpressionException
  {
    if (wildcard.isMultipleTimes())
    {
      append("*");
    }
    else
    {
      // IBIS: LDAP: Wie macht man einfache Wildcards?
      append("*");
      
      for (int i = 0; i < wildcard.getTimes(); i++)
      {
//        builder.append(SQL.SWILDCARD);
      }
    }
  }

  public void appendWildcardExpression(QBEWildcardExpression expression, boolean doNot) throws InvalidExpressionException
  {
    // IBIS: LDAP: How to do case sensitive?

    TextFieldType textFieldType = (TextFieldType) getTableField().getType();

    append("(");
    if (doNot)
      append("!(");
    append(getTableField().getDBName());
    append("=");
    if (!textFieldType.isLeftAnchored() && expression.isUnbound())
      append("*");
    Iterator iter = expression.getParts().iterator();
    while (iter.hasNext())
    {
      Object element = iter.next();
      if (element instanceof String)
      {
        String text = (String) element;
        append(LdapDataSource.convertToLdap(text));
      }
      else
      {
        appendWildcard((QBEWildcard) element);
      }
    }
    if (!textFieldType.isRightAnchored() && expression.isUnbound())
      append("*");
    if (doNot)
      append(")");
    append(")");
  }

  public void appendLiteral(QBELiteral literal, boolean doNot) throws InvalidExpressionException
  {
    append("(");
    // IBIS: LDAP: How to do case sensitive?
    if (doNot)
      append("!(");
    append(getTableField().getDBName());
    append("=");
    literal.makeConstraint(this, false);
    if (doNot)
      append(")");
    append(")");
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
      boolean inverse = false;
      if (doNot)
      {
        if (operator.hasConstraint(this, false))
        {
          // suppose there is an inverse operator
          inverse = true;
          doNot = false;
        }
      }
      else
      {
        if (operator.hasConstraint(this, false))
        {
          // suppose there is an inverse operator
          inverse = true;
          doNot = true;
        }
      }
      append("(");
      if (doNot)
        append("!(");
      append(getTableField().getDBName());
      operator.toConstraint(this, inverse);
      expression.makeConstraint(this, false);
      if (doNot)
        append(")");
      append(")");
    }
  }

  public void appendDualExpression(QBEExpression leftExpression, QBEDualOperator operator, QBEExpression rightExpression, boolean doNot) throws InvalidExpressionException
  {
    append("(");
    operator.toConstraint(this, doNot);
    leftExpression.makeConstraint(this, false);
    rightExpression.makeConstraint(this, false);
    append(")");
  }

  public void appendRangeExpression(QBEExpression leftExpression, QBEExpression rightExpression, boolean rightInclusive, boolean doNot) throws InvalidExpressionException
  {
    // IBIS: LDAP: Prüfen, ob das mit dem < und > ohne = funktioniert
    append("(");
    append(doNot ? "|" : "&");

    append("(");
    append(getTableField().getDBName());
    append(doNot ? "<" : ">=");
    leftExpression.makeConstraint(this, false);
    append(")");

    append("(");
    append(getTableField().getDBName());
    if (rightInclusive)
      append(doNot ? ">" : "<=");
    else
      append(doNot ? ">=" : "<");
    rightExpression.makeConstraint(this, false);
    append(")");

    append(")");
  }

  public void appendDocumentExpression(QBEExpression embedded) throws InvalidExpressionException
  {
    throw new UnsupportedOperationException();
  }
}
