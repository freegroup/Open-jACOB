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

package de.tif.qes.adl.element;

import java.util.Collections;
import java.util.Set;

import de.tif.jacob.core.definition.impl.AbstractTableAlias;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.qes.IQeScriptContainer;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADLTableAlias extends AbstractTableAlias implements IQeScriptContainer
{
  static public final transient String RCS_ID = "$Id: ADLTableAlias.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final String desc;
  private final Set tableRules;
  private final Set unmodifiableTableRules;
  
	/**
	 * 
	 */
  public ADLTableAlias(String tablename, String alias, String desc, String condition, Set tableRules)
  {
    super(alias,tablename, condition);
    this.desc = desc;
    this.tableRules = tableRules;
    this.unmodifiableTableRules = Collections.unmodifiableSet(tableRules);
  }
  
  public void add(ADLTableRule rule)
  {
  	this.tableRules.add(rule);
  }

	/**
	 * @return Returns the desc.
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @return Set{ADLTableRule}
	 */
	public Set getTableRules()
	{
		return this.unmodifiableTableRules;
	}

	/* (non-Javadoc)
	 * @see de.tif.qes.IQeScriptContainer#getScripts()
	 */
	public Set getScripts()
	{
		// tables rules also implement IQeScript!
		return getTableRules();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractTableAlias#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias)
	 */
	public void toJacob(CastorTableAlias jacobTableAlias, ConvertToJacobOptions options)
	{
		ADLTableRule.putScriptsToProperties(getScripts(), options, this);
		super.toJacob(jacobTableAlias, options);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractTableAlias#ignoreInvalidCondition()
   */
  protected boolean ignoreInvalidCondition()
  {
    // ADL designer does not perform condition checking -> ignore, i.e. release a warning only
    return true;
  }
}
