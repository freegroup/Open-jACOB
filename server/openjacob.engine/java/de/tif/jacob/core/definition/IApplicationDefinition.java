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

import java.util.List;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.schema.ISchemaDefinition;

/**
 * This interface represents an application definition, i.e application design. An
 * application definitions consists of: <br>
 * <li>a name, a version, a title
 * <li>table definitions, i.e. {@link ITableDefinition}
 * <li>table aliases, i.e. {@link ITableAlias}
 * <li>browser definitions, i.e. {@link IBrowserDefinition}
 * <li>relations, i.e. {@link IRelation}
 * <li>relation sets, i.e. {@link IRelationSet}
 * <li>domain definitions, i.e. {@link IDomainDefinition}
 * <li>a toolbar definition, i.e. {@link IToolbarDefinition}
 * 
 * @author Andreas Sonntag
 */
public interface IApplicationDefinition extends INamedObjectDefinition, IDefinition
{
  /**
   * Returns the application title.
   * <p>
   * Note: The title could be internationalized, if the title starts with a '%'
   * character. In this case the title is interpreted as a key to the respective
   * application resource bundles for internationalization.
   * 
   * @return the application title
   */
  public String getTitle();

  /**
   * Returns the version of this application definition.
   * 
   * @return the application version
   */
  public Version getVersion();

  /**
   * Returns the data scope of this application definition.
   * 
   * @return the data scope or <code>null</code>, if data scope definition is
   *         done by runtime by means of administration property
   *         <code>dataaccessor.scope</code>.
   * @see IDomainDefinition#getDataScope()
   * @since 2.7.4
   */
  public DataScope getDataScope();

  /**
   * Returns the toolbar definition of this application definition.
   * 
   * @return the toolbar definition
   */
  public IToolbarDefinition getToolbarDefinition();

  /**
   * Returns all browser definitions ordered by name of this application
   * definition.
   * 
   * @return List of {@link IBrowserDefinition}
   */
  public List getBrowserDefinitions();

  /**
   * Creates an ad-hoc browser definition, i.e. an modifiable browser
   * definition, for the given table alias. Initial this browser definition does
   * not contain any browser fields.
   * 
   * @param alias
   *          the table alias to use as base for the created ad-hoc browser
   *          definition
   * @return the newly created ad-hoc browser definition
   */
  public IAdhocBrowserDefinition createAdhocBrowserDefinition(ITableAlias alias);

  /**
   * Creates an ad-hoc browser definition, i.e. an modifiable browser
   * definition, for the given table alias. Initial this browser definition does
   * not contain any browser fields.
   * 
   * @param aliasName
   *          the name of the table alias to use as base for the created ad-hoc
   *          browser definition
   * @return the newly created ad-hoc browser definition
   * @since 2.10
   */
  public IAdhocBrowserDefinition createAdhocBrowserDefinition(String aliasName);

  /**
   * Creates an ad-hoc browser definition, i.e. an modifiable browser
   * definition, by means of a given browser definition as template.
   * 
   * @param browserDefinition
   *          the browser definition to use as template
   * @return the newly created ad-hoc browser definition
   */
  public IAdhocBrowserDefinition createAdhocBrowserDefinition(IBrowserDefinition browserDefinition);

  /**
   * Returns all table definitions ordered by name of this application
   * definition.
   * 
   * @return List of {@link ITableDefinition}
   */
  public List getTableDefinitions();

  /**
   * Returns all table aliases ordered by name of this application definition.
   * 
   * @return List of {@link ITableAlias}
   */
  public List getTableAliases();

  /**
   * Returns all relation sets ordered by name of this application definition.
   * 
   * @return List of {@link IRelationSet}
   */
  public List getRelationSets();

  /**
   * Returns the default relation set, i.e. the relation set which contains all
   * relations of this application definition.
   * 
   * @return the default relation set.
   */
  public IRelationSet getDefaultRelationSet();

  /**
   * Returns the local relation set, i.e. the local relation set contains no
   * relations at all. Nevertheless, the local relation set provides a specific
   * handling then used with foreign fields.
   * 
   * @return the local relation set
   * @see IRelationSet#isLocal()
   */
  public IRelationSet getLocalRelationSet();

  /**
   * Returns all relations ordered by name of this application definition.
   * 
   * @return List of {@link IRelation}
   */
  public List getRelations();

  /**
   * Returns all domain definitions of this application definition.
   * 
   * @return List of {@link IDomainDefinition}
   */
  public List getDomainDefinitions();

  /**
   * Fetches a table definition by means of the datasource name and its physical
   * name within the given datasource.
   * 
   * @param datasourceName
   *          the datasource name
   * @param dbname
   *          the physical name of the desired table definition
   * @return the desired table definition
   * @throws RuntimeException
   *           if no such table definition exists.
   * @see IDefinition#getTableDefinition(String)
   * @since 2.10
   */
  public ITableDefinition getTableDefinition(String datasourceName, String dbname) throws RuntimeException;

  /**
   * Returns the database schema definition for the given datasource as it is
   * required for this application.
   * 
   * @param datasourceName
   *          the datasource name
   * @return the schema definition or <code>null</code>, if this application
   *         is not based on the given data source.
   */
  public ISchemaDefinition getSchemaDefinition(String datasourceName);
}
