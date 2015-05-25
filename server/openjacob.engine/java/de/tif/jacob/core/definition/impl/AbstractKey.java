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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.impl.jad.castor.CastorKey;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractKey extends AbstractElement implements IKey, ISchemaKeyDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractKey.java,v 1.4 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  // the following length is ok for Oracle and MSSQL as well
  private static final int MAX_KEY_DBNAME_LENGTH = 30;
  
  private final KeyType type;
  private final List fieldNames;
  private List tableFields;
  private ITableDefinition foreignTable;
  private Set foreignTableAliases;
  private AbstractTableDefinition tableDefinition;
  private String dbName;
  
  protected AbstractKey(String name, KeyType type)
  {
    super(name, null);
    this.type = type;
    this.fieldNames = new ArrayList();
  }
  
  public final int hashCode()
  {
    return 31* getName().hashCode() + this.tableDefinition.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    AbstractKey other = (AbstractKey) obj;
    
    if (!tableDefinition.equals(other.tableDefinition))
      return false;

    return getName().equals(other.getName());
  }

  protected void addFieldName(String name)
  {
  	this.fieldNames.add(name); 
  }
  
  public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    this.tableDefinition = (AbstractTableDefinition) parent;
    List tableFields = new ArrayList(this.fieldNames.size());
    for (int i=0; i < this.fieldNames.size(); i++)
    {
      String fieldName = (String) this.fieldNames.get(i);
      tableFields.add(tableDefinition.getTableField(fieldName));
    }
    this.tableFields = Collections.unmodifiableList(tableFields);
    
    // link table field with key
    if (this.tableFields.size() == 1 && this.type == KeyType.FOREIGN)
    {
      ((AbstractTableField) this.tableFields.get(0)).setForeignKey(this);
    }
    
    // build key name as follows
    // Example: FK12_CALLS_EMP_PKEY
    this.dbName = this.getType().getPrefix() + this.tableDefinition.getNextKeyCounter() + "_" + this.tableDefinition.getDBName().toUpperCase() + "_" + this.getName().toUpperCase();
    if (this.dbName.length() > MAX_KEY_DBNAME_LENGTH)
    {
      // truncate dbname
      this.dbName = this.dbName.substring(0, MAX_KEY_DBNAME_LENGTH);
    }
  }
  
  public final List getTableFields()
  {
    return this.tableFields;
  }
  
  protected final void toJacob(CastorKey jacobKey)
  {
    jacobKey.setName(getName());
    for (int i=0; i < this.fieldNames.size(); i++)
    {
      jacobKey.addField((String) this.fieldNames.get(i));
    }
  }
  
  
	/**
	 * @param foreignKeyRelation The foreignKeyRelation to set.
	 */
	protected final void setForeignKeyRelation(IOneToManyRelation foreignKeyRelation)
	{
    if (this.foreignTable != null && this.foreignTable != foreignKeyRelation.getFromTableAlias().getTableDefinition())
    {
      // IBIS: Prüfen, wie damit umgegangen werden soll
      System.err.println("### Warning: Foreign key "+this+" already linked to table: "+this.foreignTable+", conflict with: "+foreignKeyRelation.getFromTableAlias().getTableDefinition());
//     throw new RuntimeException("Foreign key "+this+" already linked to table: "+this.foreignTable+", conflict with: "+foreignKeyRelation.getFromTableAlias().getTableDefinition()); 
    }
		this.foreignTable = foreignKeyRelation.getFromTableAlias().getTableDefinition();
		
		if (this.foreignTableAliases == null)
		{
		  // lazy instantiation
		  this.foreignTableAliases = new HashSet();
		}
		this.foreignTableAliases.add(foreignKeyRelation.getFromTableAlias());
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IKey#getType()
	 */
	public final KeyType getType()
	{
		return this.type;
	}

	public ITableDefinition getLinkedForeignTable()
	{
		return this.foreignTable;
	}

	public Iterator getLinkedForeignTableAliases()
	{
	  if (this.foreignTableAliases == null)
	    return Collections.EMPTY_LIST.iterator();
		return this.foreignTableAliases.iterator();
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IKey#contains(de.tif.jacob.core.definition.ITableField)
	 */
	public boolean contains(ITableField field)
	{
		return this.tableFields.contains(field);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IKey#getDBName()
	 */
	public String getDBName()
	{
		return this.dbName;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IKey#getTableDefinition()
	 */
	public ITableDefinition getTableDefinition()
	{
		return tableDefinition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString()
	{
    return this.tableDefinition+"."+getName();
  }
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#getSchemaColumnNames()
   */
  public Iterator getSchemaColumnNames()
  {
    return new SchemaColumnNameIterator();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#hasRequiredColumns()
   */
  public boolean hasRequiredColumns()
  {
    for (int i = 0; i < this.tableFields.size(); i++)
    {
      if (false == ((ITableField) this.tableFields.get(i)).isRequired())
      {
        return false;
      }
    }

    // all fields are required
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaKeyDefinition#isUnique()
   */
  public boolean isUnique()
  {
    return this.type.isUnique();
  }

  private static final char DELIMITER = '|';
  private static final char ESCAPE = '\\';
  
  private static String adjustForRestore(String value)
  {
    if (value == null)
      return "";
    
    // fast track
    if (value.indexOf(DELIMITER) < 0 && value.indexOf(ESCAPE) < 0)
      return value;
    
    StringBuffer buffer = new StringBuffer();
    for (int i=0; i<value.length();i++)
    {
      char c = value.charAt(i);
      switch (c)
      {
        case ESCAPE:
        case DELIMITER:
          buffer.append(ESCAPE);
          break;
      }
      buffer.append(c);
    }
    return buffer.toString();
  }
  
  private Object convertStringToDataValue(ITableField field, String value, Object argument)
  {
    try
    {
      return field.getType().convertObjectToDataValue(null, value, null, null);
    }
    catch (Exception ex)
    {
      raiseError(argument, "field '" + field + "': " + ex.toString());
      
      // will be never reached
      return null;
    }
  }
  
  public final IDataKeyValue convertStringToKeyValue(String keyvalueStr) throws IllegalArgumentException
  {
    List fields = getTableFields();

    if (fields.size() == 1)
      return new IDataKeyValue(convertStringToDataValue((ITableField) fields.get(0), keyvalueStr, keyvalueStr));

    Object[] fieldValues = new Object[fields.size()];
    StringBuffer buffer = new StringBuffer();
    boolean escape = false;
    int n = 0;
    for (int i = 0; i < keyvalueStr.length(); i++)
    {
      char c = keyvalueStr.charAt(i);
      switch (c)
      {
        case ESCAPE:
          if (escape)
          {
            escape = false;
            buffer.append(ESCAPE);
          }
          else
          {
            escape = true;
          }
          continue;
        case DELIMITER:
          if (escape)
          {
            escape = false;
            buffer.append(DELIMITER);
          }
          else
          {
            fieldValues[n] = convertStringToDataValue((ITableField) fields.get(n), buffer.toString(), keyvalueStr);

            if (++n == fieldValues.length)
              raiseError(keyvalueStr, "Too many delimiters");

            buffer = new StringBuffer();
          }
          continue;
      }
      buffer.append(c);
    }

    if (n != fieldValues.length - 1)
      raiseError(keyvalueStr, "Too less delimiters");

    fieldValues[n] = convertStringToDataValue((ITableField) fields.get(n), buffer.toString(), keyvalueStr);

    return new IDataKeyValue(fieldValues);
  }
  
  private void raiseError(Object argument, String reason) throws IllegalArgumentException
  {
    throw new IllegalArgumentException("Key '" + this + "' is not compatible with argument \"" + argument + "\": " + reason);
  }
  
  private String convertDataValueToString(ITableField field, Object dataValue, Object argument)
  {
    try
    {
      return field.getType().convertDataValueToString(dataValue, null, IDataRecord.RAW_STYLE);
    }
    catch (Exception ex)
    {
      raiseError(argument, "field '" + field + "': " + ex.toString());
      
      // will never be reached
      return null;
    }
  }
  
  public final String convertKeyValueToString(IDataKeyValue keyvalue) throws IllegalArgumentException
  {
    List fields = getTableFields();
    if (fields.size() == keyvalue.numberOfFieldValues())
    {
      if (fields.size() == 1)
      {
        return convertDataValueToString((ITableField) fields.get(0), keyvalue.getFieldValue(0), keyvalue);
      }
      else if (fields.size() > 1)
      {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields.size(); i++)
        {
          if (i != 0)
            result.append(DELIMITER);
          result.append(adjustForRestore(convertDataValueToString((ITableField) fields.get(i), keyvalue.getFieldValue(i), keyvalue)));
        }
        return result.toString();
      }
    }

    raiseError(keyvalue, "Different number of key fields");
    // will never be reached
    return null;
  }
  
  /**
   * @author Andreas
   *
   * Iterator which iterates over all fields belonging to a given key.
   */
  private class SchemaColumnNameIterator implements Iterator
  {
    private Iterator fieldIter;
    private String next;
    
    private SchemaColumnNameIterator()
    {
      this.fieldIter = AbstractKey.this.tableFields.iterator();
      iterate();
    }
    
    private void iterate()
    {
      if (this.fieldIter.hasNext())
      {
        ITableField key = (ITableField) this.fieldIter.next();
        this.next = key.getDBName();
        return;
      }
      
      this.next = null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
      return this.next != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next()
    {
      Object result = this.next;
      iterate();
      return result;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
