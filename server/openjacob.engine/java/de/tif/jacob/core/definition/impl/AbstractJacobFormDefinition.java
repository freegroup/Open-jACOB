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
import java.util.List;
import de.tif.jacob.core.definition.IJacobFormDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractJacobFormDefinition extends AbstractGuiElement implements IJacobFormDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractJacobFormDefinition.java,v 1.4 2009/12/14 16:32:46 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  private final String label;
  private final List groups;
  private final List unmodifiableGroupList;
  private final List groupContainers;
  private final List unmodifiableGroupContainerList;
  private final boolean hideTabStrip;
  private final boolean visible;
  
	/**
	 * @param name
	 */
	public AbstractJacobFormDefinition(String name, String label, boolean hideTabStrip, String description, String eventHandler, boolean visible)
	{
		super(name, description, eventHandler);
    this.label = label;
    this.visible = visible;
    this.hideTabStrip=hideTabStrip;
    this.groups = new ArrayList();
    this.unmodifiableGroupList = Collections.unmodifiableList(this.groups);
    this.groupContainers = new ArrayList();
    this.unmodifiableGroupContainerList = Collections.unmodifiableList(this.groupContainers);
  }

  public boolean isVisible()
  {
    return this.visible;
  }
	
  protected void addGroup(AbstractGroupDefinition group)
  {
    this.groups.add(group);
  }

  /**
   * @since 2.8.0
   */
  protected void addGroupContainer(AbstractGroupContainerDefinition groupContainer)
  {
    this.groupContainers.add(groupContainer);
  }

  /**
   * @since 2.8.0
   */
	public boolean hideSearchBrowserTabStrip()
  {
    return hideTabStrip;
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IFormDefinition#getLabel()
	 */
	public final String getLabel()
	{
		return label;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IFormDefinition#getGroupDefinitions()
   */
  public final List getGroupDefinitions()
  {
    return this.unmodifiableGroupList;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IFormDefinition#getGroupDefinitions()
   */
  public List getGroupContainerDefinitions()
  {
    return this.unmodifiableGroupContainerList;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    postProcessing(definition, this.getGroupDefinitions().iterator());
  }

  public void toJacob(CastorJacobForm jacobForm, ConvertToJacobOptions options)
  {
    jacobForm.setName(getName());
    jacobForm.setLabel(getLabel());
    jacobForm.setDescription(getDescription());
    jacobForm.setEventHandler(getEventHandler());
    
    // false is default
    if (hideSearchBrowserTabStrip())
      jacobForm.setHideSearchBrowserTabStrip(true);
    
    for (int i=0; i < this.groups.size(); i++)
    {
      CastorGroup jacobGroup = new CastorGroup();
      AbstractGroupDefinition groupDef = (AbstractGroupDefinition) this.groups.get(i);
      groupDef.toJacob(jacobGroup, options);
      jacobForm.addGroup(jacobGroup);
    }
    
    for (int i=0; i < this.groupContainers.size(); i++)
    {
      CastorGroupContainer jacobGroupContainer = new CastorGroupContainer();
      AbstractGroupContainerDefinition groupContainerDef = (AbstractGroupContainerDefinition) this.groupContainers.get(i);
      groupContainerDef.toJacob(jacobGroupContainer, options);
      jacobForm.addGroupContainer(jacobGroupContainer);
    }
    
    // handle properties
    jacobForm.setProperty(getCastorProperties());
  }
}
