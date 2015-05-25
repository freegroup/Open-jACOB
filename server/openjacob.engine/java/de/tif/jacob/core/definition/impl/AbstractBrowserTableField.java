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

import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractBrowserTableField extends AbstractBrowserField implements IBrowserTableField
{
  static public transient final String RCS_ID = "$Id: AbstractBrowserTableField.java,v 1.2 2007/10/26 11:21:46 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final String tablefieldName;
  private final SortOrder sortorder;
  private ITableField tableField;
  private AbstractBrowserDefinition browserDefinition;
  
  protected AbstractBrowserTableField(String name, String tablefieldName, String label, SortOrder sortorder, boolean visible, boolean readonly, boolean configureable)
  {
    super(name, label, visible, readonly, configureable);
    this.tablefieldName = tablefieldName;
    this.sortorder = sortorder;
  }

  public final ITableField getTableField()
  {
   return this.tableField; 
  }
  
  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    this.browserDefinition = (AbstractBrowserDefinition) parent;
    this.tableField = getTableAlias().getTableDefinition().getTableField(this.tablefieldName);
    
    super.postProcessing(definition, parent);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IBrowserTableField#getSortorder()
	 */
	public final SortOrder getSortorder()
	{
		return sortorder;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IBrowserField#getTableAlias()
	 */
	public ITableAlias getTableAlias()
	{
		return this.browserDefinition.getTableAlias();
	}

  protected void toJacob(CastorBrowserField jacobBrowserField)
  {
    super.toJacob(jacobBrowserField);
    CastorBrowserTableFieldType jacobBrowserTableField = new CastorBrowserTableFieldType(); 
    jacobBrowserTableField.setTableField(getTableField().getName());
    jacobBrowserTableField.setSortOrder(getSortorder().toJad());
    jacobBrowserField.getCastorBrowserFieldChoice().setTableField(jacobBrowserTableField);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowserField#createInputFieldDefinition(java.lang.String)
	 */
	protected InputFieldDefinition createInputFieldDefinition(String inputFieldName)
	{
    return this.tableField.getType().createDefaultInputField(inputFieldName, null, true, isReadonly(), -1, 0, null, getTableAlias(), this.tableField);
  }

}
