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

package de.tif.jacob.core.definition.impl.aliascondition;

import de.tif.jacob.core.definition.IDefinition;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AliasSimpleExpression extends AliasExpression
{
  static public transient final String RCS_ID = "$Id: AliasSimpleExpression.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final String expression;
  
	/**
	 * 
	 */
	public AliasSimpleExpression(String expression)
	{
    this.expression = expression;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
	 */
	protected void print(AliasConditionResult result)
	{
    result.writer.print(this.expression);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasExpression#isObsolete()
	 */
	public boolean isObsolete()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasExpression#isDynamic()
	 */
	public boolean isDynamic()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#childrenPostProcessing(de.tif.jacob.core.definition.IDefinition, de.tif.jacob.core.definition.ITableAlias)
	 */
	protected void childrenPostProcessing(IDefinition definition, ITableAlias alias)
	{
		// nothing more to do
	}

}
