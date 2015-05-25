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
public class AliasTableColumn extends AliasExpression
{
  static public transient final String RCS_ID = "$Id: AliasTableColumn.java,v 1.4 2009/07/28 22:31:57 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";

  private final String aliasName;
  private final boolean optional;
	private final String column;
	private ITableAlias foreignAlias;
	private boolean isFunction;

	/**
	 *  
	 */
	public AliasTableColumn(String aliasName, String column, boolean optional)
	{
		this.aliasName = aliasName;
		this.column = column;
    this.optional = optional;
	}

	public AliasTableColumn(String column)
	{
		this.aliasName = null;
		this.column = column;
    this.optional = false;
	}

	protected void print(AliasConditionResult result)
	{
		if (this.aliasName != null)
		{
			if (this.foreignAlias != null)
				result.addForeignAlias(this.foreignAlias, this.optional);
			
      if (result.adjuster == null)
        result.writer.print(this.aliasName);
      else
        result.writer.print(result.adjuster.adjustAliasName(this.aliasName));
			result.writer.print(".");
		}
    if (result.adjuster == null || this.isFunction)
      result.writer.print(this.column);
    else
      result.writer.print(result.adjuster.adjustColumnName(this.column));
	}

	public boolean isObsolete()
	{
		return false;
	}

	public boolean isDynamic()
	{
		return false;
	}

	protected void childrenPostProcessing(IDefinition definition, ITableAlias alias)
	{
    if (aliasName != null)
    {
    	// only determine alias for foreign aliases
    	if (!aliasName.equals(alias.getName()))
    		this.foreignAlias = definition.getTableAlias(this.aliasName);
    }
    else
    {
      // Check whether the column name (without alias) really specifies a column
      // or a function without brackets and arguments (e.g. Oracle's SYSDATE)
      this.isFunction = !alias.getTableDefinition().hasTableFieldByDBName(this.column);
    }
	}

}
