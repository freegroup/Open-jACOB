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
/*
 * Created on 22.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.definition.impl.aliascondition;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableAlias.ITableAliasConditionAdjuster;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class AliasConditionResult implements ITableAliasCondition
{
	static public transient final String RCS_ID = "$Id: AliasConditionResult.java,v 1.2 2007/10/16 00:24:14 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

	private final StringWriter string;
  
	final ITableAliasConditionAdjuster adjuster;
	final PrintWriter writer;
  private final Set foreignAliases;
  private final Set mandatoryForeignAliases;
	private final Set unmodifiableForeignAliases;
	
	/**
	 * 
	 */
	public AliasConditionResult(ITableAliasConditionAdjuster adjuster)
	{
		this.string = new StringWriter();
		this.writer = new PrintWriter(string);
    this.adjuster = adjuster;
		this.foreignAliases = new HashSet();
    this.mandatoryForeignAliases = new HashSet();
		this.unmodifiableForeignAliases = Collections.unmodifiableSet(this.foreignAliases);
	}
	
	protected boolean isEmpty()
	{
		String temp = toString();
		return null == temp || "".equals(temp);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return string.toString();
	}
	
	protected void addForeignAlias(ITableAlias alias, boolean optional)
	{
    if (!optional)
      this.mandatoryForeignAliases.add(alias);
		this.foreignAliases.add(alias);
	}

	/* (non-Javadoc)
   * @see de.tif.jacob.core.definition.ITableAliasCondition#isOptionalForeignAlias(de.tif.jacob.core.definition.ITableAlias)
   */
  public boolean isOptionalForeignAlias(ITableAlias alias)
  {
    return !this.mandatoryForeignAliases.contains(alias);
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableAliasCondition#getForeignAliases()
	 */
	public Set getForeignAliases()
	{
		return this.unmodifiableForeignAliases;
	}
}
