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

import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IHtmlGroupDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;

/**
 * @author Andreas Herz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractHtmlGroupDefinition extends AbstractGuiElement implements IHtmlGroupDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractHtmlGroupDefinition.java,v 1.2 2009/03/18 11:34:57 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final String label;
  private final ITableAlias tableAlias;
  private final IBrowserDefinition activeBrowserDefinition;
  
	/**
	 * @param name
	 * @param label
	 * @param description
	 * @param hasBorder
	 * @param eventHandler
	 * @param tableAlias
	 * @param activeBrowserDefinition
	 * @param dimension
	 */
	public AbstractHtmlGroupDefinition(String name, String label, String description, String eventHandler, ITableAlias tableAlias, IBrowserDefinition activeBrowserDefinition, SelectionActionEventHandler[] selectionActions)
	{
		super(name, description, eventHandler);
    this.label = label;
    this.tableAlias = tableAlias;
    this.activeBrowserDefinition = activeBrowserDefinition; 
  }
  
  public boolean hideEmptyBrowser()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getLabel()
   */
  public final String getLabel()
  {
    return this.label;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getTableAlias()
   */
  public final ITableAlias getTableAlias()
  {
    return this.tableAlias;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGroupDefinition#getActiveBrowserDefinition()
   */
  public final IBrowserDefinition getActiveBrowserDefinition()
  {
    return this.activeBrowserDefinition;
  }


  public void toJacob(CastorGroup jacobGroup, ConvertToJacobOptions options)
  {
    jacobGroup.setName(getName());
    jacobGroup.setLabel(getLabel());
    jacobGroup.setDescription(getDescription());
    jacobGroup.setEventHandler(getEventHandler());
    jacobGroup.setAlias(getTableAlias().getName());
    jacobGroup.setBrowser(getActiveBrowserDefinition().getName());    
    
    // handle properties
    jacobGroup.setProperty(getCastorProperties());
  }
  
  protected boolean ignoreInvalidEnums()
  {
    return false;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    // perform plausibility checks
  }
}
