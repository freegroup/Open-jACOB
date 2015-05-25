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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractRelationSet;
import de.tif.qes.QeSFocusDefinition;
import de.tif.qes.QeSLocking;
import de.tif.qes.adf.ADFDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLDefinition extends AbstractDefinition
{
  static public final transient String RCS_ID = "$Id: ADLDefinition.java,v 1.3 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
	private final Map tablesets = new HashMap();
	private final Map modules = new HashMap();
	private Map datasourceMapping;
  private String defaultDatasourceName;
	private List tempFormorderNames;
	private List tempFormNames;
	private List tempBrowserNames;
	private Version adlVersion;
	
	private static ADLTable createQwLocksTable(String dataSource) throws Exception
  {
	  //
	  // create fields
	  List fields = new ArrayList();
    fields.add(new ADLField(
        "tablename", // name
        "tablename", // dbname
				new ADLVString( // type
				    new Integer(30), // length
				    null, // desc
						null, // def
						Boolean.FALSE, // anchorleft 
						Boolean.FALSE, // casesensitive
						Boolean.TRUE), // required
				"tablename", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    fields.add(new ADLField(
        "keyvalue", // name
        "keyvalue", // dbname
				new ADLVString( // type
				    new Integer(128), // length
				    null, // desc
						null, // def
						Boolean.FALSE, // anchorleft 
						Boolean.FALSE, // casesensitive
						Boolean.TRUE), // required
				"keyvalue", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    fields.add(new ADLField(
        "username", // name
        "username", // dbname
				new ADLVString( // type
				    new Integer(30), // length
				    null, // desc
						null, // def
						Boolean.FALSE, // anchorleft 
						Boolean.FALSE, // casesensitive
						Boolean.FALSE), // required
				"username", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    fields.add(new ADLField(
        "hostname", // name
        "hostname", // dbname
				new ADLVString( // type
				    new Integer(128), // length
				    null, // desc
						null, // def
						Boolean.FALSE, // anchorleft 
						Boolean.FALSE, // casesensitive
						Boolean.FALSE), // required
				"hostname", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    fields.add(new ADLField(
        "pid", // name
        "pid", // dbname
				new ADLInteger( // type
				    null, // desc
				    null, // def
				    null, // max
				    null, // min
						Boolean.FALSE), // required
				"pid", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    fields.add(new ADLField(
        "locktime", // name
        "locktime", // dbname
				new ADLDatetime( // type
				    "NOW", // desc
						Boolean.FALSE), // required
				"locktime", // label
				Boolean.TRUE, // readonly 
				Boolean.FALSE, // not_null
				Boolean.TRUE, // systemfield
				Boolean.FALSE, // history
				Boolean.FALSE)); // historyfield
    
    //
    // create keys
    List primaryKeyFieldNames = new ArrayList();
    primaryKeyFieldNames.add("tablename");
    primaryKeyFieldNames.add("keyvalue");
    ADLKey primaryKey = new ADLKey("pk_qw_locks", KeyType.PRIMARY, primaryKeyFieldNames, Boolean.TRUE);
    
    //
    // create table itself
    return new ADLTable(
        QeSLocking.LOCK_TABLENAME, // name  
        QeSLocking.LOCK_TABLENAME, // dbname
				"Quintus internal table for locking", // description
				dataSource, // dataSource
				primaryKey,  // primaryKey
				Collections.EMPTY_LIST, // otherKeys
				fields, // fields
				null, // infoField
				Boolean.TRUE); // systemTable
  }
	
	public void setAdlVersionString(String versionStr)
  {
    String prefix = "ADL-";
    if (versionStr != null && versionStr.startsWith(prefix))
    {
      try
      {
        this.adlVersion = Version.parseVersion(versionStr.substring(prefix.length()));
      }
      catch (Exception ex)
      {
        // ignore
      }
    }
  }

  /**
   * @return Returns the adlVersion or <code>null<code>.
   */
  public Version getAdlVersion()
  {
    return adlVersion;
  }
  
	/**
	 * @param tableset
	 */
	public void add(ADLTableSet tableset)
	{
		Iterator iter = tableset.getAliases().iterator();
		while (iter.hasNext())
		{
			ADLTableAlias alias = (ADLTableAlias) iter.next();
      add(alias);
		}
		this.tablesets.put(tableset.getName(), tableset);
	}
	
	/**
	 * @param module
	 */
	public void add(ADLModule module)
	{
		this.modules.put(module.getName(), module);
  }

	public ADLModule getModule(String name)
	{
		ADLModule result = (ADLModule) this.modules.get(name);
		if (null == result)
		{
			throw new RuntimeException("No module " + name + " found!");
		}
		return result;
	}

	/**
	 * Main reason for post processing is to perform semantical checks and to
	 * link definition elements for faster access.
	 */
  public void postProcessing(String defaultDatasourceName, Map datasourceMapping) throws Exception
  {
    this.datasourceMapping = datasourceMapping;
    this.defaultDatasourceName = defaultDatasourceName;
    
    //
    // add additional internal QW tables which are not defined in the ADL file
    // Note: Definition of QW_LOCKS table is needed to access entries by means
    // of data layer functionality as used for application caretaker.
    add(createQwLocksTable(defaultDatasourceName));
    
    // create table aliases from all table definitions which have no alias with
    // the same name so far
    // Reason: jACOB Designer does not come along with table definitions being
    // table alias of itself!
    //
    for (Iterator iter = getTables(); iter.hasNext();)
    {
      ITableDefinition tableDef = (ITableDefinition) iter.next();
      if (!hasRealTableAliasDefinition(tableDef.getName()))
      {
        add(new ADLTableAlias(tableDef.getName(), tableDef.getName(), "implicit table alias created from table definition", null, Collections.EMPTY_SET));
      }
    }
    
    postProcessing(getTables());
    postProcessing(getTableAliases());
    postProcessing(getRelationSets());
    postProcessing(getRelations());
    
    // and initialize default relation set which contains all relations
    initDefaultRelationsSet();
    
    // initialise browsers, which depend on complete relationset initialisation (i.e. needed for foreign fields)
    postProcessing(getBrowsers());
    
    // merge all table rules for aliases
    // Note: Quintus ADL-Definition contains redundant and inconsistent definitions concerning 
    // table rules for table aliases. Therefore merge all table rules for each alias stored!
    Iterator iter = this.tablesets.values().iterator();
    while (iter.hasNext())
    {
    	ADLTableSet tableSet = (ADLTableSet) iter.next();
    	for (int i=0; i<tableSet.getAliases().size();i++)
    	{
    		ADLTableAlias alias = (ADLTableAlias) tableSet.getAliases().get(i);
    		Iterator ruleIter = alias.getTableRules().iterator();
    		ADLTableAlias storedAlias = (ADLTableAlias) this.getTableAlias(alias.getName());
    		
    		// merge table rules
    		while (ruleIter.hasNext())
    		{
    			storedAlias.add((ADLTableRule) ruleIter.next());
    		}
    	}
    }
  }
  
  public void postProcessing(ADFDefinition adfDefinition) throws Exception
  {
    // create domains
    Iterator iter = this.modules.values().iterator();
    while (iter.hasNext())
    {
      ADLModule module = (ADLModule) iter.next();
      if (module.isFocus())
      {
        add(new QeSFocusDefinition(module, adfDefinition));
      }
    }    
    postProcessing(adfDefinition.getForms());
    postProcessing(getDomains());
  }
  
	public void clearTemp()
	{
		this.tempBrowserNames = null;
		this.tempFormNames = null;
		this.tempFormorderNames = null;
	}

	public void setTempBrowserNames(List names)
	{
		this.tempBrowserNames = names;
	}

	public void setTempFormNames(List names)
	{
		this.tempFormNames = names;
	}

	public void setTempFormorderNames(List names)
	{
		this.tempFormorderNames = names;
	}

	/**
	 * @return Returns the tempBrowserNames.
	 */
	public List getTempBrowserNames()
	{
		return tempBrowserNames;
	}

	/**
	 * @return Returns the tempFormNames.
	 */
	public List getTempFormNames()
	{
		return tempFormNames;
	}

	/**
	 * @return Returns the tempFormorderNames.
	 */
	public List getTempFormorderNames()
	{
		return tempFormorderNames;
	}

	public String mapDatasource(String name)
	{
		if (this.datasourceMapping != null)
		{
			String newName = (String) this.datasourceMapping.get(name);
			if (null != newName)
				return newName;
		}
		return name;
	}
  
  public static final String CQDEFAULT = "cqdefault";
  public static final String CQLOCAL = "cqlocal";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractDefinition#getRelationSet(java.lang.String)
	 */
	public AbstractRelationSet getAbstractRelationSet(String name)
	{
    // regard predefined Quintus relation sets
    if (CQDEFAULT.equals(name))
      return getDefaultRelationSet();
    if (CQLOCAL.equals(name))
      return getLocalRelationSet();

    return super.getAbstractRelationSet(name);
	}

	/**
	 * @return Returns the defaultDatasourceName.
	 */
	protected final String getDefaultDatasourceName()
	{
		return defaultDatasourceName;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractDefinition#ignoreUnknownRelationsSets()
   */
  protected boolean ignoreUnknownRelationsSets()
  {
    // ADL designer does not perform reliable plausibility checks -> ignore, i.e. release a warning and return default relation set
    return true;
  }
}
