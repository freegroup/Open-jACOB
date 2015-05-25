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
public class AliasBinaryCondition extends AliasCondition
{
  static public transient final String RCS_ID = "$Id: AliasBinaryCondition.java,v 1.1 2007/01/19 09:50:34 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  private final AliasCondition condition1;
	private final AliasCondition condition2;
	private final String binaryOperator;

	/**
	 *  
	 */
	public AliasBinaryCondition(AliasCondition condition1, String binaryOperator, AliasCondition condition2)
	{
		this.condition1 = condition1;
		this.binaryOperator = binaryOperator;
		this.condition2 = condition2;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.aliascondition.AliasElement#print(de.tif.jacob.core.definition.impl.aliascondition.AliasConditionResult)
	 */
	protected void print(AliasConditionResult result)
	{
		boolean obsolete1 = this.condition1.isObsolete();
		boolean obsolete2 = this.condition2.isObsolete();
    
		if (!obsolete1)
			this.condition1.print(result);
		if (!obsolete1 && !obsolete2)
			result.writer.print(this.binaryOperator);
		if (!obsolete2)
			this.condition2.print(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isObsolete()
	 */
	public boolean isObsolete()
	{
		return this.condition1.isObsolete() && this.condition2.isObsolete();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#isDynamic()
	 */
	public boolean isDynamic()
	{
    return this.condition1.isDynamic() || this.condition2.isDynamic();
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.alias.AliasElement#childrenPostProcessing()
	 */
	protected void childrenPostProcessing(IDefinition definition, ITableAlias alias)
	{
    this.condition1.childrenPostProcessing(definition, alias);
    this.condition2.childrenPostProcessing(definition, alias);
  }

}
