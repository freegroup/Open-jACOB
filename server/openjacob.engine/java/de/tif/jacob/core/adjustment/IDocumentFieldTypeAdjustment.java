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
package de.tif.jacob.core.adjustment;

import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLStatementBuilder;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author andreas
 *
 */
public interface IDocumentFieldTypeAdjustment extends IFieldTypeAdjustment
{
	public static final String RCS_ID = "$Id: IDocumentFieldTypeAdjustment.java,v 1.1 2007/01/19 09:50:39 freegroup Exp $";
	public static final String RCS_REV = "$Revision: 1.1 $";

  public void makeSQL(SQLDataSource datasource, ITableAlias alias, ITableField field, SQLStatementBuilder builder, QBEExpression embedded) throws InvalidExpressionException;
}
