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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.tif.jacob.core.definition.IGroupContainerDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractGroupContainerDefinition extends AbstractGuiElement implements IGroupContainerDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractGroupContainerDefinition.java,v 1.2 2008/11/25 18:16:35 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  private final List groups;
  private final List unmodifiableGroupList;
  private final Dimension dimension;
  
	/**
	 * @param name
	 */
	public AbstractGroupContainerDefinition(String name, String description, String eventHandler, Dimension dimension)
	{
		super(name, description, eventHandler);
    this.groups = new ArrayList();
    this.unmodifiableGroupList = Collections.unmodifiableList(this.groups);
    this.dimension = dimension;
  }

  protected void addGroup(AbstractGroupDefinition group)
  {
    this.groups.add(group);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.definition.IGUIElementDefinition#getRectangle()
   */
  public final Rectangle getRectangle()
  {
    return new Rectangle(this.dimension.getLeft(), this.dimension.getTop(), this.dimension.getWidth(), this.dimension.getHeight());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IFormDefinition#getGroupDefinitions()
   */
  public final List getGroupDefinitions()
  {
    return this.unmodifiableGroupList;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    postProcessing(definition, this.getGroupDefinitions().iterator());
  }

  public void toJacob(CastorGroupContainer jacobGroupContainer, ConvertToJacobOptions options)
  {
    jacobGroupContainer.setName(getName());
    jacobGroupContainer.setDescription(getDescription());
    jacobGroupContainer.setEventHandler(getEventHandler());
    jacobGroupContainer.setDimension(dimension.toJacob());
    
    for (int i=0; i < this.groups.size(); i++)
    {
      CastorGroup jacobGroup = new CastorGroup();
      AbstractGroupDefinition groupDef = (AbstractGroupDefinition) this.groups.get(i);
      groupDef.toJacob(jacobGroup, options);
      jacobGroupContainer.addGroup(jacobGroup);
    }
    
    // handle properties
    jacobGroupContainer.setProperty(getCastorProperties());
  }
}
