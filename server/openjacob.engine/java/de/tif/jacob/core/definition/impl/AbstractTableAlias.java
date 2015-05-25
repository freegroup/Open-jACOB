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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.impl.aliascondition.AliasCondition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractTableAlias extends AbstractElement implements ITableAlias
{
  static public transient final String RCS_ID = "$Id: AbstractTableAlias.java,v 1.5 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";
  
  private String conditionString;
	private final String tableName;
  private AliasCondition condition;
  private ITableDefinition tableDefinition;

	/**
	 *  
	 */
	public AbstractTableAlias(String aliasName, String tableName, String condition)
	{
		super(aliasName == null ? tableName : aliasName, null);
		if (null == tableName)
			throw new NullPointerException();
		this.tableName = tableName;
		this.conditionString = condition;
	}

	public final ITableAliasCondition getCondition()
	{
    if (this.condition == null)
      return null;
    
		return this.condition.getResult(null);
	}

  public ITableAliasCondition getCondition(ITableAliasConditionAdjuster adjuster)
  {
    if (this.condition == null)
      return null;
    
    return this.condition.getResult(adjuster);
  }

  public final ITableDefinition getTableDefinition()
  {
    return this.tableDefinition;
  }
  
  public final int hashCode()
  {
    return getName().hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    AbstractTableAlias other = (AbstractTableAlias) obj;
    
    return getName().equals(other.getName());
  }

  /**
   * Internal usage because this.tableDefinition might not be set so far.
   * 
   * @param definition
   * @return
   */
  protected final ITableDefinition getTableDefinition(AbstractDefinition definition)
  {
    return definition.getTableDefinition(this.tableName);
  }
  
  public boolean isInternal()
  {
    return this.tableDefinition.isInternal(); 
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition,
	 *      de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
		this.tableDefinition = definition.getTableDefinition(this.tableName);
		try
		{
		  this.condition = AliasCondition.parseWithPostProcessing(this.conditionString, definition, this);
		}
		catch (InvalidExpressionException ex)
		{
		  if (ignoreInvalidCondition())
		  {
		    // Is an error because deployment of such an application would fail!
				System.err.println("### Error: Invalid condition '"+ex.getExpression()+"' of table alias '" + this + "': "+ex.getMessage());
				this.conditionString = null;
		  }
		  else
		  {
		    throw ex;
		  }
		}
  }
	
	protected boolean ignoreInvalidCondition()
	{
	  return false;
	}

	public void toJacob(CastorTableAlias jacobTableAlias, ConvertToJacobOptions options)
	{
		jacobTableAlias.setName(getName());
		jacobTableAlias.setDescription(getDescription());
		jacobTableAlias.setTable(getTableDefinition().getName());
		jacobTableAlias.setCondition(this.conditionString);
    
    // handle properties
    jacobTableAlias.setProperty(getCastorProperties());
  }

}
