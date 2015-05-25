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

package de.tif.jacob.core.definition;

/**
 * This interface represents the definition of a table alias.
 * 
 * @author Andreas Sonntag
 */
public interface ITableAlias extends INamedObjectDefinition
{
  /**
   * Interface for adjustment of alias conditions.
   * <p>
   * Note: This interface is for internal use!
   * 
   * @author Andreas Sonntag
   * @since 2.7.2
   * @see ITableAlias#getCondition(de.tif.jacob.core.definition.ITableAlias.ITableAliasConditionAdjuster)
   */
  public interface ITableAliasConditionAdjuster
  {
    /**
     * This method is used to adjust an alias name to the environment, e.g. to
     * escape alias names which are database keywords.
     * 
     * @param aliasName
     *          the alias name to adjust
     * @return the adjusted alias name
     */
    public String adjustAliasName(String aliasName);
    
    /**
     * This method is used to adjust a column name to the environment, e.g. to
     * escape column names which are database keywords.
     * 
     * @param columnName
     *          the column name to adjust
     * @return the adjusted column name
     */
    public String adjustColumnName(String columnName);
  }
  
	/**
	 * Returns the definition of the table this table alias is related to.
	 * 
	 * @return the table definition
	 */
	public ITableDefinition getTableDefinition();
  
  /**
   * Returns the condition of this table alias. A condition restricts the
   * result set of search operations performed on this table alias.
   * 
   * @return the table alias condition or <code>null</code>, if no condition
   *         exists
   */
  public ITableAliasCondition getCondition();
  
  /**
   * Returns the condition of this table alias. A condition restricts the result
   * set of search operations performed on this table alias.
   * 
   * @param adjuster
   *          adjustment implementation to adapt condition to environment, e.g.
   *          to escape alias names which are database keywords.
   * @return the table alias condition or <code>null</code>, if no condition
   *         exists
   * @since 2.7.2
   */
  public ITableAliasCondition getCondition(ITableAliasConditionAdjuster adjuster);
}
