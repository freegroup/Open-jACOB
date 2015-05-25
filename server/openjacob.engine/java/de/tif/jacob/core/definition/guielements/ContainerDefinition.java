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

package de.tif.jacob.core.definition.guielements;

import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.definition.IGroupDefinition;
import de.tif.jacob.core.definition.impl.AbstractGroupDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.Container;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType;

/**
 * @author Andreas
 * 
 * @since 2.8.3
 */
public abstract class ContainerDefinition extends GUIElementDefinition
{
	static public final transient String RCS_ID = "$Id: ContainerDefinition.java,v 1.4 2010/08/12 07:52:16 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

  private final List unmodifiablePanes;
  private final ResizeMode resizeMode;
  
	public ContainerDefinition(String name, String description, String eventHandler, boolean visible, Dimension position, List panes, int borderWidth,  String borderColor, String backgroundColor, CastorResizeMode castorResizeMode) throws Exception
	{
    super(name, description, eventHandler, position, visible, -1, 0, null, borderWidth, borderColor, backgroundColor);

    this.resizeMode = ResizeMode.fromJacob(castorResizeMode);
    this.unmodifiablePanes = Collections.unmodifiableList(panes);
	}

  public ResizeMode getResizeMode()
  {
    return this.resizeMode;
  }
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    Container jacobContainer = new Container();
    jacobContainer.setLayout(ContainerLayoutType.TAB_STRIP);
    jacobContainer.setDimension(getDimension().toJacob());
    jacobGuiElement.getCastorGuiElementChoice().setContainer(jacobContainer);
    
    // fetch panes
    for (int i=0; i < this.unmodifiablePanes.size(); i++)
    {
      CastorGroup jacobGroup = new CastorGroup();
      AbstractGroupDefinition groupDef = (AbstractGroupDefinition) this.unmodifiablePanes.get(i);
      groupDef.toJacob(jacobGroup, options);
      jacobContainer.addPane(jacobGroup);
    }
  }

  /**
   * Returns the pane definitions of this container.
   * 
   * @return <code>List</code> of {@link IGroupDefinition}
   */
  public final List getPaneDefinitions()
  {
    return this.unmodifiablePanes;
  }
}
