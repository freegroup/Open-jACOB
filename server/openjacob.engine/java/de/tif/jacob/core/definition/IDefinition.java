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
 * Created on 25.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.definition;

/**
 * This interface represents an abstract definition used to fetch <br>
 * <li> table definitions
 * <li> table aliases
 * <li> browser definitions
 * <li> relations
 * <li> relation sets
 * <li> domain definitions
 * by name.
 * 
 * @author Andreas Sonntag
 */
public interface IDefinition
{
  /**
   * Fetches a browser definition by means of a given name.
   * 
   * @param name
   *          the name of the desired browser definition
   * @return the desired browser definition
   * @throws RuntimeException if no such browser definition exists.
   */
  public IBrowserDefinition getBrowserDefinition(String name) throws RuntimeException;

  /**
   * Fetches a table definition by means of a given name.
   * 
   * @param name
   *          the name of the desired table definition
   * @return the desired table definition
   * @throws RuntimeException if no such table definition exists.
   */
  public ITableDefinition getTableDefinition(String name) throws RuntimeException;

  /**
   * Fetches a table alias by means of a given name.
   * 
   * @param name
   *          the name of the desired table alias
   * @return the desired table alias
   * @throws RuntimeException if no such table alias exists.
   */
  public ITableAlias getTableAlias(String name) throws RuntimeException;

  /**
   * Fetches a relation set by means of a given name.
   * 
   * @param name
   *          the name of the desired relation set
   * @return the desired relation set
   * @throws RuntimeException if no such relation set exists.
   */
  public IRelationSet getRelationSet(String name) throws RuntimeException;

  /**
   * Fetches a relation by means of a given name.
   * 
   * @param name
   *          the name of the desired relation
   * @return the desired relation
   * @throws RuntimeException if no such relation exists.
   */
  public IRelation getRelation(String name) throws RuntimeException;

  /**
   * Fetches a domain definition by means of a given name.
   * 
   * @param name
   *          the name of the desired domain definition
   * @return the desired domain definition
   * @throws RuntimeException if no such domain definition exists.
   */
  public IDomainDefinition getDomainDefinition(String name) throws RuntimeException;
}
