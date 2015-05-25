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

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class QBEUnaryOperator extends QBEOperator
{
  static public transient final String RCS_ID = "$Id: QBEUnaryOperator.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  public static final QBEUnaryOperator NOT = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendNotOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasNotOperator(inverse);
    }

    public String toString()
    {
      return "NOT";
    }
  };
  
  public static final QBEUnaryOperator EQUALS = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendEqualsOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasEqualsOperator(inverse);
    }

    public String toString()
    {
      return "=";
    }
  };
  
  public static final QBEUnaryOperator GREATER_THAN = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendGreaterThanOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasGreaterThanOperator(inverse);
    }

    public String toString()
    {
      return ">";
    }
  };
  
  public static final QBEUnaryOperator LESS_THAN = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendLessThanOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasLessThanOperator(inverse);
    }

    public String toString()
    {
      return "<";
    }
  };
  
  public static final QBEUnaryOperator GREATER_OR_EQUAL_THAN = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendGreaterOrEqualThanOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasGreaterOrEqualThanOperator(inverse);
    }

    public String toString()
    {
      return ">=";
    }
  };
  
  public static final QBEUnaryOperator LESS_OR_EQUAL_THAN = new QBEUnaryOperator()
  {
    public void toConstraint(QBEConstraintBuilder builder, boolean inverse) throws InvalidExpressionException
    {
      builder.appendLessOrEqualThanOperator(this, inverse);
    }

    public boolean hasConstraint(QBEConstraintBuilder builder, boolean inverse)
    {
      return builder.hasLessOrEqualThanOperator(inverse);
    }

    public String toString()
    {
      return "<=";
    }
  };
  
  private QBEUnaryOperator()
  {
    // protect
  }
}
