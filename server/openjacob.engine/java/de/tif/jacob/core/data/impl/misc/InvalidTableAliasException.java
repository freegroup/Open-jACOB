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
package de.tif.jacob.core.data.impl.misc;

import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;

public final class InvalidTableAliasException extends RuntimeException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: InvalidTableAliasException.java,v 1.1 2007/01/19 09:50:50 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  private final ITableAlias tableAlias;
  private final IRelationSet relationSet;
  private ITableAliasCondition aliasCondition;
  private ITableAlias conditionAlias;

  /**
   * Constructs the <code>InvalidTableAliasException</code>.
   * 
   * @param tableField
   *          the table field causing the invalid expression
   * @param cause
   *          the cause
   * @param cause
   *          a further cause
   */
  public InvalidTableAliasException(ITableAlias tableAlias, IRelationSet relationSet)
  {
    super("Alias '"+tableAlias+"' not found in graph of relation set '"+relationSet+"'");
    this.tableAlias = tableAlias;
    this.relationSet = relationSet;
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#getLocalizedMessage()
   */
  public String getLocalizedMessage()
  {
    return getMessage();
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#getMessage()
   */
  public String getMessage()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Alias '").append(tableAlias).append("'");
    if (this.aliasCondition != null)
    {
      buffer.append(" which is part of condition \"").append(this.aliasCondition).append("\"");
      if (this.conditionAlias != null)
      {
        buffer.append(" of alias '").append(this.conditionAlias).append("'");
      }
    }
    buffer.append(" not found in graph of relation set '").append(relationSet).append("'");
    return buffer.toString();
  }

  /**
   * Returns the invalid table alias.
   * 
   * @return the invalid table alias
   */
  public final ITableAlias getTableAlias()
  {
    return tableAlias;
  }

  /**
   * Returns the relation set.
   * 
   * @return the relation set
   */
  public final IRelationSet getRelationSet()
  {
    return this.relationSet;
  }

  /**
   * @return Returns the aliasCondition.
   */
  public ITableAliasCondition getAliasCondition()
  {
    return aliasCondition;
  }

  /**
   * @param aliasCondition The aliasCondition to set.
   */
  public void setAliasCondition(ITableAliasCondition aliasCondition)
  {
    this.aliasCondition = aliasCondition;
  }

  /**
   * @return Returns the conditionAlias.
   */
  public ITableAlias getConditionAlias()
  {
    return conditionAlias;
  }

  /**
   * @param conditionAlias The conditionAlias to set.
   */
  public void setConditionAlias(ITableAlias conditionAlias)
  {
    this.conditionAlias = conditionAlias;
  }
}
