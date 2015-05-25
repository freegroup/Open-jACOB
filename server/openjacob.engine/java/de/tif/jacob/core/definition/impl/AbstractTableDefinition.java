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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.CastorKey;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractTableDefinition extends AbstractElement implements ITableDefinition, ITableAlias
{
  static public transient final String RCS_ID = "$Id: AbstractTableDefinition.java,v 1.11 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.11 $";
  
  // the following length is ok for Oracle and MSSQL as well
  public static final int MAX_TABLE_DBNAME_LENGTH = 30;
  
  private final String dbname;
  private final boolean isOmitLocking;
  
  // Ist nur ein Hinweis!!!! Die Engine interpretiert diese Werte (noch) nicht.
  // Der AppProgrammierer kann auf diese Werte in einem generischen TableHook zugreifen
  // und die verbunden Rekords bei Bedarf löschen oder den entsprechnedne Record austragen.
  //
  private final boolean isMtoNTable;
  private final boolean isRecordsAlwaysDeleteable;
  
	private String dataSourceName;
	
	// key counter to ensure unique key db names
	private int keycounter = 0;

	private final List fieldList;
	private final List unmodifiableFieldList;
	private final Map fieldNameToFieldMap;
	
  private final List sortedTableAliases;
  private final List unmodifiableTableAliases;
  
  /**
   * Map used to access and check unique db column names.
   * 
   * <code>Map</code> of <code>fieldDBName.toLowerCase()</code> to {@link AbstractTableField}.
   */
	private final Map dbFieldNameToFieldMap;
	private final Map keyNameToKeyMap;

	private AbstractKey primaryKey;
  private AbstractTableField representativeField;
  private AbstractTableField historyField;
  
	/**
	 *  
	 */
	public AbstractTableDefinition(String name, String dbname, String dataSourceName, String description, boolean isOmitLocking, boolean isMtoNTable, boolean isAlwaysDeleteable)
	{
		super(name, description);
    this.dbname = StringUtil.toSaveString(dbname).length() == 0 ? name : dbname;
    this.isOmitLocking = isOmitLocking;
    this.isMtoNTable = isMtoNTable;
    this.isRecordsAlwaysDeleteable = isAlwaysDeleteable;
		this.dataSourceName = dataSourceName;
		this.fieldList = new ArrayList();
		this.unmodifiableFieldList = Collections.unmodifiableList(this.fieldList);
		this.fieldNameToFieldMap = new HashMap();
		this.dbFieldNameToFieldMap = new HashMap();
		this.keyNameToKeyMap = new HashMap();
		
    this.sortedTableAliases = new ArrayList();
    this.unmodifiableTableAliases = Collections.unmodifiableList(this.sortedTableAliases);
	}
  
  public final int hashCode()
  {
    return 31 * this.dataSourceName.hashCode() + this.dbname.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    AbstractTableDefinition other = (AbstractTableDefinition) obj;
    
    if (!dataSourceName.equals(other.dataSourceName))
      return false;
    
    if (!dbname.equals(other.dbname))
      return false;
    
    return true;
  }

  protected void setRepresentativeFieldName(String name) throws NoSuchFieldException, Exception
  {
    // plausibility check
    if (this.representativeField != null)
    {
      throw new Exception("Duplicated representative fields of table: "+this); 
    }
    
    this.representativeField = (AbstractTableField) getTableField(name);
  }

  protected void setHistoryFieldName(String name) throws NoSuchFieldException, Exception
  {
    // plausibility check
    if (this.historyField != null)
    {
      throw new Exception("Duplicated history fields of table: "+this); 
    }
    
    this.historyField = (AbstractTableField) getTableField(name);
    
    // plausibility check
    if (!(this.historyField.getType() instanceof LongTextFieldType))
    {
      throw new Exception("Invalid history field: "+name); 
    }
  }

  protected final void addField(AbstractTableField field)
	{
		this.fieldList.add(field);
		this.fieldNameToFieldMap.put(field.getName(), field);
		if (null != this.dbFieldNameToFieldMap.put(field.getDBName().toLowerCase(), field))
		{
      System.err.println("### Error: Table '" + this.getName() + "' contains ambiguous '" + field.getDBName() + "' columns!");
		}
	}

	protected final void addKey(AbstractKey key)
	{
		registerKey(key);
	}

	protected final void setPrimaryKey(AbstractKey key)
	{
		this.primaryKey = key;
		registerKey(key);
	}
	
	private void registerKey(AbstractKey key)
	{
		if (this.keyNameToKeyMap.containsKey(key.getName()))
		{
			throw new RuntimeException("Duplicate key name '"+key.getName()+"'");
		}
		this.keyNameToKeyMap.put(key.getName(), key);
	}

	public Iterator getKeys()
	{
		return this.keyNameToKeyMap.values().iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getDBName()
	 */
	public final String getDBName()
	{
		return this.dbname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getDataSourceName()
	 */
	public final String getDataSourceName()
	{
		return this.dataSourceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getPrimaryKey()
	 */
	public final IKey getPrimaryKey()
	{
		return this.primaryKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getKey(java.lang.String)
	 */
	public final IKey getKey(String name)
	{
		AbstractKey key = (AbstractKey) this.keyNameToKeyMap.get(name);
		if (null == key)
			throw new RuntimeException(name + " is no key of table " + getName());
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getTableField(java.lang.String)
	 */
	public final ITableField getTableField(String tableFieldName) throws NoSuchFieldException
	{
		AbstractTableField field = (AbstractTableField) this.fieldNameToFieldMap.get(tableFieldName);
		if (null == field)
			throw new NoSuchFieldException("No field '" + tableFieldName + "' existing in table '" + getName() + "'");
		return field;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.ITableDefinition#getTableFieldByDBName(java.lang.String)
   */
  public final ITableField getTableFieldByDBName(String dbTableFieldName) throws NoSuchFieldException
  {
		AbstractTableField field = (AbstractTableField) this.dbFieldNameToFieldMap.get(dbTableFieldName.toLowerCase());
		if (null == field)
			throw new NoSuchFieldException("No database field '" + dbTableFieldName + "' existing in table '" + getName() + "'");
		return field;
  }
  
  public final boolean hasTableFieldByDBName(String dbTableFieldName)
  {
    return this.dbFieldNameToFieldMap.containsKey(dbTableFieldName.toLowerCase());
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#getTableFields()
	 */
	public final List getTableFields()
	{
		return this.unmodifiableFieldList;
	}

	/**
	 * @param dataSource
	 *          The dataSource to set.
	 */
	protected final void setDataSourceName(String dataSourceName)
	{
		this.dataSourceName = dataSourceName;
	}

	/**
	 * @param table
	 */
	public final void toJacob(CastorTable jacobTable, ConvertToJacobOptions options)
	{
		jacobTable.setName(getName());
    jacobTable.setDbName(getDBName());
    jacobTable.setOmitLocking(isOmitLocking());
		jacobTable.setDatasource(getDataSourceName());
		jacobTable.setDescription(getDescription());

		// fields
		for (int i = 0; i < this.fieldList.size(); i++)
		{
			CastorTableField jacobTableField = new CastorTableField();
      jacobTableField.setCastorTableFieldChoice(new CastorTableFieldChoice());
      ((AbstractTableField) this.fieldList.get(i)).toJacob(jacobTableField, options);
			jacobTable.addField(jacobTableField);
		}

		// primary key
		if (null != this.primaryKey)
		{
      CastorKey jacobPrimaryKey = new CastorKey();
			this.primaryKey.toJacob(jacobPrimaryKey);
			jacobTable.setPrimaryKey(jacobPrimaryKey);
		}

		// other keys (attention: use TreeSet for sorted output!)
		Iterator iter = new TreeSet(this.keyNameToKeyMap.values()).iterator();
		while (iter.hasNext())
		{
			AbstractKey key = (AbstractKey) iter.next();
			
			if (key.getType().equals(KeyType.PRIMARY))
			{
				// we already have this one
				continue;
			}
			
			CastorKey jacobKey = new CastorKey();
			key.toJacob(jacobKey);
			if (key.getType().equals(KeyType.FOREIGN))
			{
				jacobTable.addForeignKey(jacobKey);
			}
			else if (key.getType().equals(KeyType.UNIQUE))
			{
				jacobTable.addUniqueIndex(jacobKey);
			}
			else if (key.getType().equals(KeyType.INDEX))
			{
				jacobTable.addIndex(jacobKey);
			}
			else
			{
				// should never occure!
				throw new RuntimeException("Unknown key: "+key);
			}
		}
    
    if (getRepresentativeField() != null)
    {
      jacobTable.setRepresentativeField(getRepresentativeField().getName());
    }
    if (getHistoryField() != null)
    {
      jacobTable.setHistoryField(getHistoryField().getName());
    }
    
    // handle properties
    jacobTable.setProperty(getCastorProperties());
  }

	public final ITableAliasCondition getCondition()
	{
		// nothing more to do
		return null;
	}

  public ITableAliasCondition getCondition(ITableAliasConditionAdjuster adjuster)
  {
    // nothing more to do
    return null;
  }

	public final ITableDefinition getTableDefinition()
	{
		return this;
	}

	public final ITableField getRepresentativeField()
	{
		return this.representativeField;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableDefinition#getHistoryField()
	 */
	public ITableField getHistoryField()
	{
		return this.historyField;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    postProcessing(definition, this.fieldList.iterator());
    postProcessing(definition, getKeys());

    // build sorted table aliases
    //
    Iterator iter = definition.getTableAliases();
    while (iter.hasNext())
    {
      AbstractTableAlias alias = (AbstractTableAlias) iter.next();
      if (alias.getTableDefinition(definition).equals(this))
        this.sortedTableAliases.add(alias);
    }
    Collections.sort(this.sortedTableAliases);
	}

  public final List getTableAliases()
  {
    return this.unmodifiableTableAliases;
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableDefinition#hasTableField(de.tif.jacob.core.definition.ITableField)
	 */
	public boolean hasTableField(ITableField tableField)
	{
		int fieldIndex = tableField.getFieldIndex();
		return fieldIndex < this.unmodifiableFieldList.size() && tableField.equals(this.unmodifiableFieldList.get(fieldIndex));
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableDefinition#hasTableField(java.lang.String)
	 */
	public boolean hasTableField(String tableFieldName)
	{
		return this.fieldNameToFieldMap.containsKey(tableFieldName);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableDefinition#hasKey(de.tif.jacob.core.definition.IKey)
	 */
	public boolean hasKey(IKey key)
	{
		return this.equals(((AbstractKey) key).getTableDefinition());
	}
	
	/**
	 * Increments internal key counter and returns result.
	 * 
	 * @return next key counter
	 */
	int getNextKeyCounter()
	{
	  return ++this.keycounter;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableDefinition#hasKey(java.lang.String)
	 */
	public boolean hasKey(String keyName)
	{
		return this.keyNameToKeyMap.containsKey(keyName);
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaTableDefinition#getSchemaPrimaryKeyDefinition()
   */
  public ISchemaKeyDefinition getSchemaPrimaryKeyDefinition()
  {
    return this.primaryKey;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.ITableDefinition#isOmitLocking()
   */
  public final boolean isOmitLocking()
  {
    return this.isOmitLocking;
  }

  public boolean isRecordsAlwaysDeletable()
  {
    return isRecordsAlwaysDeleteable;
  }

  public boolean isMtoNTable()
  {
    return isMtoNTable;
  }
}
