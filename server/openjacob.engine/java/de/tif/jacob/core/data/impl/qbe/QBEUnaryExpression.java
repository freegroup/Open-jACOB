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

import java.io.PrintWriter;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBEUnaryExpression extends QBEExpression
{
  static public transient final String RCS_ID = "$Id: QBEUnaryExpression.java,v 1.2 2010/01/29 00:47:09 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final QBEUnaryOperator operator;
  private final QBEExpression embeddedExpression;

  public QBEUnaryExpression(QBEUnaryOperator operator, QBEExpression embeddedExpression)
  {
    super();
    this. operator = operator;
    this. embeddedExpression =embeddedExpression;
  }
  
  public boolean isNullExpression()
  {
    return this.embeddedExpression.isNullExpression() && this.operator == QBEUnaryOperator.NOT;
  }

  public void print(PrintWriter writer)
	{
    writer.print(this.operator);
    writer.print(" (");
    this.embeddedExpression.print(writer);
    writer.print(")");
  }

  public void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendUnaryExpression(operator, this.embeddedExpression, doNot);
  }
}
