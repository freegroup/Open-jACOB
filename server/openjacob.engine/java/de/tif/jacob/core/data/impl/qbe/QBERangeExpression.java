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
 * QBE range expression
 * 
 * @author Andreas Sonntag
 */
public class QBERangeExpression extends QBEExpression
{
  static public transient final String RCS_ID = "$Id: QBERangeExpression.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private final QBEExpression embeddedLeftExpression;
	private final QBEExpression embeddedRightExpression;
  private final boolean rightInclusive;

	public QBERangeExpression(QBEExpression embeddedLeftExpression, QBEExpression embeddedRightExpression, boolean rightInclusive)
	{
		super();
		this.embeddedLeftExpression = embeddedLeftExpression;
		this.embeddedRightExpression = embeddedRightExpression;
    this.rightInclusive = rightInclusive;
	}

  public final void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException
  {
    builder.appendRangeExpression(this.embeddedLeftExpression, this.embeddedRightExpression, this.rightInclusive, doNot);
  }

	public void print(PrintWriter writer)
	{
		writer.print("(");
		this.embeddedLeftExpression.print(writer);
		writer.print(") .. (");
		this.embeddedRightExpression.print(writer);
		writer.print(")");
	}
}
