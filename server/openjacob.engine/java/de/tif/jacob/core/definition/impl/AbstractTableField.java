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
import java.util.List;

import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractTableField extends AbstractElement implements ITableField, ISchemaColumnDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractTableField.java,v 1.5 2010/11/12 11:57:55 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.5 $";
  
  private final String dbname;
  private final String label;
  private final boolean readonly;
  private final boolean required;
  private final FieldType type;
  private final boolean enabledForHistory;
  private AbstractKey foreignKey;
  private AbstractTableDefinition tableDefinition;
  
  public int fieldIndex;
  
  public AbstractTableField(String name, String dbname, String label, String description, FieldType type, boolean required, boolean readonly, boolean enabledForHistory)
  {
    super(name, description);
    this.dbname = StringUtil.toSaveString(dbname).length() == 0 ? name : dbname;
    this.label = StringUtil.toSaveString(label).length() == 0 ? name : label;
    this.type = type;
    this.readonly = readonly;
    this.required = required;
    this.enabledForHistory = enabledForHistory;
  }
  
  public final int hashCode()
  {
    return 31 * this.dbname.hashCode() + this.tableDefinition.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if(obj==null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
 
    AbstractTableField other = (AbstractTableField) obj;

    if(!dbname.equals(other.dbname))
      return false;
    
    if (!tableDefinition.equals(other.tableDefinition))
      return false;

    return true;
  }

  /**
   * @return Returns the dbname.
   */
  public final String getDBName()
  {
    return dbname;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.ITableField#isReadOnly()
   */
  public final boolean isReadOnly()
  {
    return this.readonly;
  }

  public final FieldType getType()
  {
    return this.type; 
  }
  
  public final boolean isRequired()
  {
    return this.required;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.ITableField#getFieldIndex()
   */
  public final int getFieldIndex()
  {
    return this.fieldIndex;
  }

  /**
   * @param fieldIndex The fieldIndex to set.
   */
  public final void setFieldIndex(int fieldIndex)
  {
    this.fieldIndex = fieldIndex;
  }

  
  
  protected final void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
  {
    tableField.setName(getName());
    tableField.setDbName(getDBName());
    tableField.setLabel(getLabel());
    tableField.setDescription(getDescription());
    tableField.setReadonly(isReadOnly());
    tableField.setRequired(isRequired());
    if (isEnabledForHistory())
      tableField.setHistory(true);
    getType().toJacob(tableField, options);
    
    // handle properties
    tableField.setProperty(getCastorProperties());
  }
    
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
		this.tableDefinition = (AbstractTableDefinition) parent;
	}

	/**
	 * @return Returns the enabledForHistory.
	 */
	public final boolean isEnabledForHistory()
	{
		return enabledForHistory;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableField#getLabel()
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * @param foreignKey The foreignKey to set.
	 */
	protected final void setForeignKey(AbstractKey foreignKey)
	{
		this.foreignKey = foreignKey;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableField#getMatchingForeignKey()
	 */
	public IKey getMatchingForeignKey()
	{
		return this.foreignKey;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableField#isPrimary()
	 */
	public final boolean isPrimary()
	{
		if (this.tableDefinition.getPrimaryKey() != null)
		{
			return this.tableDefinition.getPrimaryKey().contains(this);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ITableField#getTableDefinition()
	 */
	public ITableDefinition getTableDefinition()
	{
		return this.tableDefinition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString()
	{
		return this.tableDefinition+"."+getName();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaColumnDefinition#getDBTableName()
   */
  public String getDBTableName()
  {
    return this.tableDefinition.getDBName();
  }
  
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
    return getType().getSQLDecimalDigits(dataSource);
  }
  
  public int getSQLSize(ISQLDataSource dataSource)
  {
    return getType().getSQLSize(dataSource);
  }
  
  public int getSQLType(ISQLDataSource dataSource)
  {
    return getType().getSQLType(dataSource);
  }
  
  public String getDBDefaultValue(ISQLDataSource dataSource)
  {
    return getType().getSQLDefaultValue(dataSource);
  }
  
  public boolean isDBAutoGenerated(ISQLDataSource dataSource)
  {
    return getType().isDBAutoGenerated(dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaColumnDefinition#getEnumerationLabels()
   */
  public List getEnumerationLabels()
  {
    EnumerationFieldType enumeration = (EnumerationFieldType) getType();
    List result = new ArrayList(enumeration.enumeratedValueCount());
    for (int i=0; i < enumeration.enumeratedValueCount(); i++)
    {
      result.add(enumeration.getEnumeratedValue(i));
    }
    return result;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.schema.ISchemaColumnDefinition#isEnumeration()
   */
  public boolean isEnumeration()
  {
    return getType() instanceof EnumerationFieldType;
  }
}
