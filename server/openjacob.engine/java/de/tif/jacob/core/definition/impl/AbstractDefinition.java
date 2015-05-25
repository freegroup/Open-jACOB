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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IDefinition;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.Applications;
import de.tif.jacob.core.definition.impl.jad.castor.Browsers;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRole;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.DataSources;
import de.tif.jacob.core.definition.impl.jad.castor.Domains;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.definition.impl.jad.castor.Relations;
import de.tif.jacob.core.definition.impl.jad.castor.Relationsets;
import de.tif.jacob.core.definition.impl.jad.castor.Roles;
import de.tif.jacob.core.definition.impl.jad.castor.TableAliases;
import de.tif.jacob.core.definition.impl.jad.castor.Tables;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractDefinition implements IDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractDefinition.java,v 1.5 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";
  
  /**
   * Map used to check unique db names.
   * 
   * Map{datasourceName -> Map{tableDBName.toLowerCase()->AbstractTableDefinition}}
   */
  private final Map datasources = new HashMap();
  
  /**
   * Map{tableName -> AbstractTableDefinition}
   */
  private final Map tables = new HashMap();
  
	private final Map tableAliases = new HashMap();
	private final Map browsers = new HashMap();
	private final Map relations = new HashMap();
	private final Map relationsets = new HashMap();
  private final Map domains = new HashMap();
  private final Map applications = new HashMap();
  
	private DefaultRelationSet defaultRelationSet;

	/**
	 *  
	 */
	public AbstractDefinition()
	{
    // nothing more to do
	}
	
	public void add(AbstractTableDefinition table)
	{
	  // maintain datasource map
	  //
	  Map dataSourceTableMap = (Map) this.datasources.get(table.getDataSourceName());
	  if (dataSourceTableMap == null)
	  {
	    dataSourceTableMap = new HashMap();
	    this.datasources.put(table.getDataSourceName(), dataSourceTableMap);
	  }
	  
	  // check for unique db names
	  //
    if (dataSourceTableMap.put(table.getDBName().toLowerCase(), table) != null)
    {
      System.err.println("### Error: DB name '" + table.getDBName() + "' of table definition '" + table.getName() + "' is not unique!");
    }
	  
	  // and add the table definition
	  //
		this.tables.put(table.getName(), table);
	}
	
  /**
   * @return
   * @since 2.10
   */
  protected Iterator getDatasourceNames()
  {
    return this.datasources.keySet().iterator();
  }

  protected Iterator getTables()
  {
    return this.tables.values().iterator();
  }

	public void add(AbstractTableAlias tableAlias)
	{
		this.tableAliases.put(tableAlias.getName(), tableAlias);
	}
	protected Iterator getTableAliases()
	{
		return this.tableAliases.values().iterator();
	}

	public void add(AbstractBrowserDefinition browser)
	{
		this.browsers.put(browser.getName(), browser);
	}
	protected Iterator getBrowsers()
	{
		return this.browsers.values().iterator();
	}

	public void add(AbstractRelation relation)
	{
		this.relations.put(relation.getName(), relation);
	}
	protected Iterator getRelations()
	{
		return this.relations.values().iterator();
	}

	public void add(AbstractRelationSet relationset)
	{
		if (null != this.relationsets.put(relationset.getName(), relationset))
    {
		  // Check used for module-character of QUINTUS QDesigner which allows different relationsets
		  // with the same name in different modules.
			System.err.println("### Warning: Duplicate relationsets with name '"+relationset.getName()+"' found!");
    }
	}
	protected Iterator getRelationSets()
	{
		return this.relationsets.values().iterator();
	}

  public void add(AbstractDomainDefinition domain)
  {
    this.domains.put(domain.getName(), domain);
  }
  protected Iterator getDomains()
  {
    return this.domains.values().iterator();
  }

  public void add(AbstractApplicationInfo application)
	{
		this.applications.put(application.getName(), application);
	}
	protected Iterator getApplications()
	{
		return this.domains.values().iterator();
	}

	public final IBrowserDefinition getBrowserDefinition(String name)
	{
	  return getAbstractBrowserDefinition(name);
	}
	
	public AbstractBrowserDefinition getAbstractBrowserDefinition(String name)
	{
		AbstractBrowserDefinition result = (AbstractBrowserDefinition) this.browsers.get(name);
		if (null == result)
		{
			throw new RuntimeException("No browser '" + name + "' found!");
		}
		return result;
	}

  public final ITableAlias getTableAlias(String name)
  {
    AbstractTableAlias result = (AbstractTableAlias) this.tableAliases.get(name);
    if (null == result)
    {
      // tables might be accessed as alias as well!
      AbstractTableDefinition result2 = (AbstractTableDefinition) this.tables.get(name);
      if (result2 != null)
      {
        return result2;
      }

      throw new RuntimeException("No table alias '" + name + "' found!");
    }
    return result;
  }

  protected final boolean hasRealTableAliasDefinition(String name)
  {
    return this.tableAliases.containsKey(name);
  }

	public final ITableDefinition getTableDefinition(String name)
	{
	  return getAbstractTableDefinition(name);
	}
	
  public final ITableDefinition getTableDefinition(String datasourceName, String dbname) throws RuntimeException
  {
    Map dataSourceTableMap = (Map) this.datasources.get(datasourceName);
    if (dataSourceTableMap != null)
    {
      AbstractTableDefinition result = (AbstractTableDefinition) dataSourceTableMap.get(dbname.toLowerCase());
      if (null != result)
      {
        return result;
      }
    }
    throw new RuntimeException("No table definition '" + datasourceName + "." + dbname + "' found!");
  }
  
	public AbstractTableDefinition getAbstractTableDefinition(String name)
	{
		AbstractTableDefinition result = (AbstractTableDefinition) this.tables.get(name);
		if (null == result)
		{
			throw new RuntimeException("No table definition '" + name + "' found!");
		}
		return result;
	}

	protected final void initDefaultRelationsSet()
	{
		this.defaultRelationSet = new DefaultRelationSet(this.relations.values().iterator());
	}

	protected final AbstractRelationSet getDefaultRelationSet()
	{
    if (null == this.defaultRelationSet)
      throw new IllegalStateException("defaultRelationSet has not been initialised so far");
		return this.defaultRelationSet;
	}

	protected final AbstractRelationSet getLocalRelationSet()
	{
		return LocalRelationSet.INSTANCE;
	}

	public final IRelationSet getRelationSet(String name)
	{
	  return getAbstractRelationSet(name);
	}
	
	public AbstractRelationSet getAbstractRelationSet(String name)
	{
		// regard predefined relation sets
		if (null == name || IRelationSet.DEFAULT_NAME.equals(name))
			return getDefaultRelationSet();

		if (IRelationSet.LOCAL_NAME.equals(name))
			return getLocalRelationSet();

		AbstractRelationSet result = (AbstractRelationSet) this.relationsets.get(name);
		if (null == result)
		{
		  if (ignoreUnknownRelationsSets())
		  {
				System.err.println("### Warning: Unknown relationset '"+name+"' (using default relationset)!");
				return getDefaultRelationSet();
		  }
		  
  		throw new RuntimeException("No relation set '" + name + "' found!");
		}
		return result;
	}
	
	protected boolean ignoreUnknownRelationsSets()
	{
	  return false;
	}

	public final IRelation getRelation(String name)
	{
	  return getAbstractRelation(name);
	}
	
	public AbstractRelation getAbstractRelation(String name)
	{
		AbstractRelation result = (AbstractRelation) this.relations.get(name);
		if (null == result)
		{
			throw new RuntimeException("No relation '" + name + "' found!");
		}
		return result;
	}

  public final IDomainDefinition getDomainDefinition(String name)
  {
    return getAbstractDomainDefinition(name);
  }
  
  public AbstractDomainDefinition getAbstractDomainDefinition(String name)
  {
    AbstractDomainDefinition result = (AbstractDomainDefinition) this.domains.get(name);
    if (null == result)
    {
      throw new RuntimeException("No domain '" + name + "' found!");
    }
    return result;
  }

  public AbstractApplicationInfo getApplicationInfo(String name)
	{
		AbstractApplicationInfo result = (AbstractApplicationInfo) this.applications.get(name);
		if (null == result)
		{
			throw new RuntimeException("No application '" + name + "' found!");
		}
		return result;
	}

	protected void postProcessing(Iterator iter) throws Exception
	{
		while (iter.hasNext())
		{
			((AbstractElement) iter.next()).postProcessing(this, (AbstractElement) null);
		}
	}

	public void toJacob(Jacob jacob, ConvertToJacobOptions options)
	{
		// initialize data sources (attention: use TreeSet for sorted output!)
	  Set dataSourceNames = new TreeSet();
	  DataSources dataSources = new DataSources();
		jacob.setDataSources(dataSources);
		
		// initialize data sources (attention: use TreeSet for sorted output!)
	  Set roleNames = new TreeSet();
	  Roles roles = new Roles();
		jacob.setRoles(roles);
		
		// fetch table data (attention: use TreeSet for sorted output!)
		//
		Tables jacobTables = new Tables();
		jacob.setTables(jacobTables);
		Iterator iter = new TreeSet(this.tables.values()).iterator();
		while (iter.hasNext())
		{
			AbstractTableDefinition table = (AbstractTableDefinition) iter.next();
			if (!options.isSuppressSystemTables() || !table.isInternal())
			{
				CastorTable jacobTable = new CastorTable();
				table.toJacob(jacobTable, options);
				jacobTables.addTable(jacobTable);
				
				// and add data source, if not already done
				dataSourceNames.add(table.getDataSourceName());
			}
		}
		
		// fetch data source names and add them
		iter = dataSourceNames.iterator();
		while (iter.hasNext())
		{
			CastorDataSource jacobDataSource = new CastorDataSource();
      String datasourceName = (String) iter.next();
      jacobDataSource.setName(datasourceName);
      jacobDataSource.setReconfigure(getReconfigureMode(datasourceName));
			dataSources.addDataSource(jacobDataSource);
		}

		// fetch alias data (attention: use TreeSet for sorted output!)
		TableAliases jacobTableAliases = new TableAliases();
		jacob.setTableAliases(jacobTableAliases);
		iter = new TreeSet(this.tableAliases.values()).iterator();
		while (iter.hasNext())
		{
			AbstractTableAlias tableAlias = (AbstractTableAlias) iter.next();
      if (!options.isSuppressSystemTables() || !tableAlias.isInternal())
      {
				CastorTableAlias jacobTableAlias = new CastorTableAlias();
				tableAlias.toJacob(jacobTableAlias, options);
				jacobTableAliases.addTableAlias(jacobTableAlias);
			}
		}

		// fetch browser data (attention: use TreeSet for sorted output!)
		Browsers jacobBrowsers = new Browsers();
		jacob.setBrowsers(jacobBrowsers);
		iter = new TreeSet(this.browsers.values()).iterator();
		while (iter.hasNext())
		{
      AbstractBrowserDefinition browser = (AbstractBrowserDefinition) iter.next();
      if (!options.isSuppressSystemTables() || !browser.isInternal())
      {
        CastorBrowser jacobBrowser = new CastorBrowser();
        browser.toJacob(jacobBrowser);
        jacobBrowsers.addBrowser(jacobBrowser);
      }
		}

		// fetch relation data (attention: use TreeSet for sorted output!)
		Relations jacobRelations = new Relations();
		jacob.setRelations(jacobRelations);
		iter = new TreeSet(this.relations.values()).iterator();
		while (iter.hasNext())
		{
			CastorRelation jacobRelation = new CastorRelation();
      jacobRelation.setCastorRelationChoice(new CastorRelationChoice());
			AbstractRelation relation = (AbstractRelation) iter.next();
			relation.toJacob(jacobRelation);
			jacobRelations.addRelation(jacobRelation);
		}

		// fetch relationset data (attention: use TreeSet for sorted output!)
		Relationsets jacobRelationsets = new Relationsets();
		jacob.setRelationsets(jacobRelationsets);
		iter = new TreeSet(this.relationsets.values()).iterator();
		while (iter.hasNext())
		{
			CastorRelationset jacobRelationset = new CastorRelationset();
			AbstractRelationSet relationSet = (AbstractRelationSet) iter.next();
			relationSet.toJacob(jacobRelationset);
			jacobRelationsets.addRelationset(jacobRelationset);
		}

		// fetch domains (attention: use TreeSet for sorted output!)
		Domains jacobDomains = new Domains();
		jacob.setDomains(jacobDomains);
		iter = new TreeSet(this.domains.values()).iterator();
		while (iter.hasNext())
		{
			CastorDomain jacobDomain = new CastorDomain();
			AbstractDomainDefinition domain = (AbstractDomainDefinition) iter.next();
			domain.toJacob(jacobDomain);
			jacobDomains.addDomain(jacobDomain);
			
			// and add role names, if not already done
			roleNames.addAll(domain.getRoleNames());
		}

		// fetch applications (attention: use TreeSet for sorted output!)
		Applications jacobApplications = new Applications();
		jacob.setApplications(jacobApplications);
		iter = new TreeSet(this.applications.values()).iterator();
		while (iter.hasNext())
		{
			CastorApplication jacobApplication = new CastorApplication();
			AbstractApplicationInfo application = (AbstractApplicationInfo) iter.next();
			application.toJacob(jacobApplication, this);
			if (options.isLookupByReference())
			{
		    jacobApplication.setEventHandlerLookupMethod(CastorApplicationEventHandlerLookupMethodType.REFERENCE);
			}
			jacobApplications.addApplication(jacobApplication);
		}
		
		// add collected role names
		//
		iter = roleNames.iterator();
		while (iter.hasNext())
		{
			CastorRole jacobRole = new CastorRole();
			jacobRole.setName((String) iter.next());
			roles.addRole(jacobRole);
		}

		// adl has no external module feature
//		jacob.setExternalModules(new ExternalModules());
	}
  
  public CastorDataSourceReconfigureType getReconfigureMode(String datasourceName)
  {
    // by default full -> might be overwritten
    return CastorDataSourceReconfigureType.FULL;
  }

  public CastorDataSourceTypeType getDataSourceType(String datasourceName)
  {
    // by default SQL -> might be overwritten
    return CastorDataSourceTypeType.SQL;
  }
  
	private static class LocalRelationSet extends AbstractRelationSet
	{
		final static LocalRelationSet INSTANCE = new LocalRelationSet();

		/**
		 * @param name
		 */
		private LocalRelationSet()
		{
			super(IRelationSet.LOCAL_NAME);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.IRelationSet#isLocal()
		 */
		public boolean isLocal()
		{
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.AbstractRelationSet#toJacob(de.tif.jacob.core.jad.castor.CastorRelationset)
		 */
		public void toJacob(CastorRelationset jacobRelationset)
		{
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition,
		 *      de.tif.jacob.core.definition.impl.AbstractElement)
		 */
		public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
		{
			throw new UnsupportedOperationException();
		}

	}

	static class DefaultRelationSet extends AbstractRelationSet
	{
		/**
		 * @param name
		 */
		DefaultRelationSet(Iterator relations)
		{
			super(IRelationSet.DEFAULT_NAME);

			// add all given relations
			while (relations.hasNext())
			{
				this.addRelation((AbstractRelation) relations.next());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.IRelationSet#isLocal()
		 */
		public boolean isLocal()
		{
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.AbstractRelationSet#toJacob(de.tif.jacob.core.jad.castor.CastorRelationset)
		 */
		public void toJacob(CastorRelationset jacobRelationset)
		{
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition,
		 *      de.tif.jacob.core.definition.impl.AbstractElement)
		 */
		public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
		{
			throw new UnsupportedOperationException();
		}
	}

}
