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

package de.tif.jacob.core.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.IAdhocBrowserDefinition;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.InputFieldDefinition;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class AdhocBrowserDefinition implements IAdhocBrowserDefinition
{
  static public final String RCS_ID = "$Id: AdhocBrowserDefinition.java,v 1.11 2010/10/13 14:21:23 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.11 $";
  
  static private final transient Log logger = LogFactory.getLog(AdhocBrowserDefinition.class);

  private final IApplicationDefinition applicationDefinition;
  private final ITableAlias tableAlias;
  private final List fields;
  private final List unmodifiableFields;
  private final Map fieldNameToFieldMap;
  
	/**
	 * 
	 */
  public AdhocBrowserDefinition(IApplicationDefinition applicationDefinition, String tableAliasName)
  {
    if (logger.isTraceEnabled())
      logger.trace("AdhocBrowserDefinition(): tableAliasName=" + tableAliasName);
    
    this.applicationDefinition = applicationDefinition;
    this.tableAlias = applicationDefinition.getTableAlias(tableAliasName);
    this.fields = new ArrayList();
    this.unmodifiableFields = Collections.unmodifiableList(this.fields);
    this.fieldNameToFieldMap = new HashMap();
  }

  public AdhocBrowserDefinition(IApplicationDefinition applicationDefinition, IBrowserDefinition browserDefinition)
  {
    this(applicationDefinition, browserDefinition.getTableAlias().getName());
    
    List browserFields = browserDefinition.getBrowserFields();
    for (int i = 0; i < browserFields.size(); i++)
    {
      // only add table browser fields
      if (browserFields.get(i) instanceof IBrowserTableField)
      {
        BrowserField browserField = new BrowserField((IBrowserTableField) browserFields.get(i), this.fields.size());
        this.fields.add(browserField);
        this.fieldNameToFieldMap.put(browserField.getName(), browserField);
      }
    }
  }

	public Map getProperties()
  {
    return Collections.EMPTY_MAP;
  }

  public IKey getConnectByKey()
  {
    return null;
  }

  public String getName()
	{
		return this.tableAlias.getName()+"AdhocBrowser";
	}

  public String getDescription()
  {
    return null;
  }
  
	public ITableAlias getTableAlias()
	{
		return this.tableAlias;
	}

	/**
   * Returns the list of all browser fields.
   * <p>
   * Note: Browser fields are always of type <code> IBrowserTableField </code>
   * 
   * @see de.tif.jacob.core.definition.IBrowserDefinition#getBrowserFields()
   */
  public List getBrowserFields()
  {
    return this.unmodifiableFields;
  }
  
  public IBrowserField getBrowserField(int index) throws IndexOutOfBoundsException
  {
    return (IBrowserField) this.fields.get(index);
  }

  public IBrowserTableField removeBrowserField(int index) throws IndexOutOfBoundsException
  {
    if (logger.isTraceEnabled())
      logger.trace("removeBrowserField(): index=" + index);

    IBrowserTableField browserField = (IBrowserTableField) this.fields.remove(index);
    this.fieldNameToFieldMap.remove(browserField.getName());

    // Index der nachfolgenden Browserfields korrigieren
    for (int i = index; i < fields.size(); i++)
      ((BrowserField) this.fields.get(i)).index = i;

    return browserField;
  }

  public int getFieldNumber()
  {
    return this.fields.size();
  }
  
  public void addBrowserField(int index, String tableAliasName, String tableFieldName, String label, SortOrder sortOrder) throws NoSuchFieldException, IndexOutOfBoundsException
  {
    addBrowserField(index, tableAliasName, tableFieldName, sortOrder, label);
  }

  public void addBrowserField(String tableAliasName, String tableFieldName, String label, SortOrder sortOrder) throws NoSuchFieldException
  {
    addBrowserField(tableAliasName, tableFieldName, sortOrder, label);
  }

  public String addBrowserField(String tableAliasName, String tableFieldName, SortOrder sortOrder, String label) throws NoSuchFieldException
  {
    return addBrowserField(this.fields.size(), tableAliasName, tableFieldName, sortOrder, label);
  }

  public String getBrowserFieldName(IBrowserField field)
  {
    if (field != null)
    {
      Iterator entrieIter =this.fieldNameToFieldMap.entrySet().iterator();
      while(entrieIter.hasNext())
      {
        Entry entry = (Entry)entrieIter.next();
        if(entry.getValue().equals(field))
          return (String)entry.getKey();
      }
    }
    return null;
  }

  
  public String addBrowserField(int index, String tableAliasName, String tableFieldName, SortOrder sortOrder, String label) throws NoSuchFieldException, IndexOutOfBoundsException
  {
    ITableAlias tableAlias = this.applicationDefinition.getTableAlias(tableAliasName);    
    ITableField tableField = tableAlias.getTableDefinition().getTableField(tableFieldName);
    
    return addBrowserField( index,  tableAlias,  tableField,  sortOrder,  label);
  }
  
  public void addBrowserField(int index, IBrowserTableField browserField) throws IndexOutOfBoundsException
  {
    addBrowserField(index, browserField.getTableAlias(), browserField.getTableField(), browserField.getSortorder(), browserField.getLabel());
  }
  
  private String addBrowserField(int index, ITableAlias tableAlias, ITableField tableField, SortOrder sortOrder, String label) throws IndexOutOfBoundsException
  {
    // do not sort unsortable fields
    if (!tableField.getType().isSortable())
    {
      sortOrder = SortOrder.NONE;
    }

    if (sortOrder != SortOrder.NONE)
    {
      if (containsBrowserFieldOf(tableField))
      {
        // the field has already been added to the browser

        // Note: Since some databases (e.g. MSSQL Server) are making problems,
        // if a table field is added more
        // than once to order by clause, reset sort order!
        sortOrder = SortOrder.NONE;
      }
    }

    BrowserField browserField = new BrowserField(tableAlias, tableField, label, sortOrder,true, index);
    this.fields.add(index, browserField);
    this.fieldNameToFieldMap.put(browserField.getName(), browserField);
    
    // Index der nachfolgenden Browserfields korrigieren
    for (int i = index + 1; i < fields.size(); i++)
      ((BrowserField) this.fields.get(i)).index = i;
    
    return browserField.getName();
  }
  
  private boolean containsBrowserFieldOf(ITableField tableField)
  {
    for (int i=0; i < this.fields.size(); i++)
    {
      IBrowserTableField browserField = (IBrowserTableField) this.fields.get(i);
      if (browserField.getTableField().equals(tableField))
        return true;
    }
    return false;
  }
  
  private static final class BrowserField implements IBrowserTableField
  {
    private final ITableAlias tableAlias;
    private final ITableField tableField;
    private final String label;
    private final SortOrder sortOrder;
    private final String name;
    private boolean visible;
    private int index;
    
    private BrowserField(ITableAlias tableAlias, ITableField tableField, String label, SortOrder sortOrder,boolean visible, int index)
    {
      this.tableAlias = tableAlias;
      this.tableField = tableField;
      this.label = label;
      this.sortOrder = sortOrder;
      this.index = index;
      this.visible = visible;
      // create a unique name
      this.name = "AdhocBrowserField" + this.hashCode();
    }
    
    private BrowserField(ITableAlias tableAlias, ITableField tableField, String label, String name, SortOrder sortOrder,boolean visible, int index)
    {
      this.tableAlias = tableAlias;
      this.tableField = tableField;
      this.label = label;
      this.sortOrder = sortOrder;
      this.index = index;
      this.visible = visible;
      this.name =name;
    }

    public Map getProperties()
    {
      return Collections.EMPTY_MAP;
    }

    private BrowserField(IBrowserTableField toClone, int index)
    {
      this(toClone.getTableAlias(), toClone.getTableField(), toClone.getLabel(),toClone.getName(), toClone.getSortorder(),toClone.isVisible(), index);
   }
    
		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserField#getLabel()
		 */
		public String getLabel()
		{
			return this.label;
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.INamedObjectDefinition#getDescription()
     */
    public String getDescription()
    {
      return null;
    }
    
		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserField#getSortorder()
		 */
		public SortOrder getSortorder()
		{
			return this.sortOrder;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserField#getTableAlias()
		 */
		public ITableAlias getTableAlias()
		{
			return this.tableAlias;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserField#getTableField()
		 */
		public ITableField getTableField()
		{
			return this.tableField;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserField#isVisible()
		 */
		public boolean isVisible()
		{
			return this.visible;
		}
    
    public boolean isConfigureable()
    {
      return false;
    }
    
    /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
      StringBuffer buffer = new StringBuffer();
      buffer.append("BrowserField[");
      buffer.append("tableAlias = ").append(tableAlias.getName());
      buffer.append(", tableField = ").append(tableField.getName());
      buffer.append(", name = ").append(name);
      buffer.append(", label = ").append(label);
      buffer.append(", sortOrder = ").append(sortOrder);
      buffer.append("]");
      return buffer.toString();
    }

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IBrowserTableField#getInputFieldDefinition()
		 */
		public InputFieldDefinition getInputFieldDefinition()
		{
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IDefinition#getName()
		 */
		public String getName()
		{
      return this.name;
    }

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IDefinition#getProperty(java.lang.String)
		 */
		public String getProperty(String name)
		{
			return null;
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.IBrowserField#getFieldIndex()
     */
    public int getFieldIndex()
    {
      return this.index;
    }
  }
  
	public String getProperty(String name)
	{
		// no properties supported
		return null;
	}

  public IBrowserField getBrowserField(String browserFieldName) throws NoSuchFieldException
  {
    IBrowserField field = (IBrowserField) this.fieldNameToFieldMap.get(browserFieldName);
    if (null == field)
      throw new NoSuchFieldException("'"+browserFieldName + "' is not a field of browser '" + getName()+"'");
    return field;
  }
}
