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

package de.tif.jacob.core.data.impl.qbe;

import java.util.Locale;

import de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBEFieldConstraint implements IDataFieldConstraintPrivate
{
  static public transient final String RCS_ID = "$Id: QBEFieldConstraint.java,v 1.3 2010/06/29 12:39:05 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  private final ITableAlias alias;
  private final ITableField field;
  private final Object value;
  private final boolean exactMatch;
  private final String guiElementName;
  private final Locale locale;
  private QBEFieldConstraint linkedConstraint;
  
  /**
   * Optional flag indicating that the constraint should only be considered, if
   * a respective (linked) record exists.
   */
  private boolean isOptional = false;
  
	/**
   * Constructor of field constraint
   * 
   * @param alias
   *          the alias of the constrained field
   * @param field
   *          the constrained field
   * @param value
   *          the QBE constraint value
   * @param exactMatch
   *          <code>true</code> means <code>value</code> should be treated
   *          as exact match value, otherwise <code>false</code>
   * @param locale
   *          the locale used to parse the QBE constraint value or
   *          <code>null</code>
   * @param guiElementName
   *          the name of the constrained gui element or <code>null</code>
   */
	public QBEFieldConstraint(ITableAlias alias, ITableField field, Object value, boolean exactMatch, Locale locale, String guiElementName)
	{
		if (null == alias)
			throw new NullPointerException("alias is null");
		if (null == field)
			throw new NullPointerException("field is null");
    if (null == value)
      throw new NullPointerException("value is null");
    
    this.value = value;
    this.exactMatch = exactMatch;
    this.guiElementName = guiElementName;
    this.locale = locale;
    
    this.alias = alias;
    this.field = field;
	}
  
  public final ITableAlias getTableAlias()
  {
    return this.alias;
  }

	/**
	 * @return Returns the field.
	 */
	public final ITableField getTableField()
	{
		return this.field;
	}

	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("QBEFieldConstraint[");
		buffer.append("alias = ").append(alias.getName());
		buffer.append(", field = ").append(field.getName());
    buffer.append(", value = ").append(value);
    buffer.append(", exactMatch = ").append(exactMatch);
    buffer.append(", guiElementName = ").append(guiElementName);
    buffer.append(", linkedConstraint = ").append(linkedConstraint);
    buffer.append("]");
		return buffer.toString();
	}
  
  public Object getValue()
  {
    if (this.linkedConstraint == null)
      return this.value;
    
    StringBuffer buffer = new StringBuffer(); 
    getValue(buffer);
    return buffer.toString();
  }
  
  private void getValue(StringBuffer buffer)
  {
    // Enforce exact match for text fields by means of '='. All others are exact match per default.
    if (this.exactMatch && field.getType() instanceof TextFieldType)
      buffer.append("=");
    
    buffer.append(this.value.toString());
    if (this.linkedConstraint != null)
    {  
      // make logical OR operation
      buffer.append("|");
      this.linkedConstraint.getValue(buffer);
    }
  }
  
  public boolean isExactMatch()
  {
    return this.linkedConstraint == null && this.exactMatch;
  }
  
  public boolean isExactMatchRecursive()
  {
    return this.exactMatch && (this.linkedConstraint == null || this.linkedConstraint.isExactMatchRecursive());
  }
  
  /**
   * Returns the locale used to parse the QBE constraint value.
   * 
   * @return The locale or <code>null</code>
   */
  public Locale getLocale()
  {
    return locale;
  }
  
  /**
   * Checks whether this constraint equals to an null check, i.e. the
   * respective field value should be <code>null</code>.
   * 
   * @return <code>true</code> if null check, otherwise <code>false</code> 
   */
  public boolean isNullCheck()
  {
    if (!exactMatch && linkedConstraint == null && value instanceof String)
    {
      return ((String) value).trim().toUpperCase().equals("NULL");
    }
    return false;
  }
  
  /**
   * Checks whether this constraint belongs to an OR-group.
   * 
   * @return <code>true</code> if constraint belongs to an OR-group, otherwise <code>false</code> 
   */
  public boolean isOrGroupCheck()
  {
    if (!exactMatch && linkedConstraint == null && value instanceof String)
    {
      return ((String) value).trim().startsWith("|");
    }
    return false;
  }
  
  public void add(QBEFieldConstraint otherConstraint)
  {
    if (this.exactMatch == otherConstraint.exactMatch && this.value.equals(otherConstraint.value))
    {
      // same constraint -> ignore
      return;
    }
    
    if (this.linkedConstraint == null)
      this.linkedConstraint = otherConstraint;
    else
      this.linkedConstraint.add(otherConstraint);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataFieldConstraint#getQbeValue()
	 */
	public String getQbeValue()
	{
		return this.value.toString();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataFieldConstraint#isQbeKeyValue()
   */
  public boolean isQbeKeyValue()
  {
    return this.exactMatch;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.internal.IDataFieldConstraintPrivate#guiElementName()
	 */
	public String guiElementName()
	{
		return this.guiElementName;
	}

	/**
	 * @return Returns the linkedConstraint.
	 */
	protected QBEFieldConstraint getLinkedConstraint()
	{
		return linkedConstraint;
	}

  /**
   * @return Returns the isOptional.
   */
  public boolean isOptional()
  {
    return isOptional;
  }
  
  /**
   * @param isOptional The isOptional to set.
   */
  public void setOptional()
  {
    this.isOptional = true;
  }
}
