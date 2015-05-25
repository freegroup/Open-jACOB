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
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractBrowserDefinition extends AbstractElement implements IBrowserDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractBrowserDefinition.java,v 1.7 2010/11/12 20:53:54 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.7 $";
  
  private final String tableAliasName;
  private final List fieldList;
	private final List unmodifiableFieldList;
	private final Map fieldNameToFieldMap;
	private ITableAlias tableAlias;
  
	protected AbstractBrowserDefinition(String name, String description, String tableAliasName)
	{
		super(name, description);
    this.tableAliasName = tableAliasName;
    this.fieldList = new ArrayList();
		this.unmodifiableFieldList = Collections.unmodifiableList(this.fieldList);
		this.fieldNameToFieldMap = new HashMap();
  }

  public final int hashCode()
  {
    return 31 * getName().hashCode() + this.tableAliasName.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
 
    AbstractBrowserDefinition other = (AbstractBrowserDefinition) obj;

    if(!getName().equals(other.getName()))
      return false;
    
    if (!this.tableAliasName.equals(other.tableAliasName))
      return false;

    return true;
  }

	public IKey getConnectByKey()
  {
    // no connectBy key for a default browser.
    return null;
  }

  protected void addField(AbstractBrowserField field)
	{
	  field.setFieldIndex(this.fieldList.size());
		this.fieldList.add(field);
		this.fieldNameToFieldMap.put(field.getName(), field);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IBrowserDefinition#getTableAlias()
   */
  public final ITableAlias getTableAlias()
  {
    return this.tableAlias; 
  }
  
  public boolean isInternal()
  {
    return this.tableAlias.getTableDefinition().isInternal();
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IBrowserDefinition#getBrowserFields()
	 */
	public final List getBrowserFields()
	{
		return this.unmodifiableFieldList;
	}

  public IBrowserField getBrowserField(int index) throws IndexOutOfBoundsException
  {
    return (IBrowserField) this.fieldList.get(index);
  }

  public int getFieldNumber()
  {
    return this.fieldList.size();
  }

  protected abstract BrowserType getType();
  
  /* (non-Javadoc)
   * @see de.tif.qes.adl.element.ADLElement#postProcessing(de.tif.qes.adl.element.ADLDefinition, de.tif.qes.adl.element.ADLElement)
   */
  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    this.tableAlias = definition.getTableAlias(this.tableAliasName);
    postProcessing(definition, this.getBrowserFields().iterator());
  }
  
  protected final void toJacob(CastorBrowser jacobBrowser)
	{
		jacobBrowser.setName(getName());
		jacobBrowser.setDescription(getDescription());
		jacobBrowser.setType(getType());
		jacobBrowser.setAlias(getTableAlias().getName());
		for (int i = 0; i < this.fieldList.size(); i++)
		{
      AbstractBrowserField browserField = (AbstractBrowserField) this.fieldList.get(i);
			jacobBrowser.addField(browserField.toJacob());
		}
    
    // handle properties
    jacobBrowser.setProperty(getCastorProperties());
  }

  public IBrowserField getBrowserField(String browserFieldName) throws NoSuchFieldException
  {
    AbstractBrowserField field = (AbstractBrowserField) this.fieldNameToFieldMap.get(browserFieldName);
    if (null == field)
      throw new NoSuchFieldException("'"+browserFieldName + "' is not a field of browser '" + getName()+"'");
    return field;
  }
}
