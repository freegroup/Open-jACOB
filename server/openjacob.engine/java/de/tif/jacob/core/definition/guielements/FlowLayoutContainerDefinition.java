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

import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer;
import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class FlowLayoutContainerDefinition extends GUIElementDefinition
{
	static public final transient String RCS_ID = "$Id: FlowLayoutContainerDefinition.java,v 1.3 2009/07/27 15:06:11 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final List unmodifiableElements;
  private boolean horizontal;
  
	public FlowLayoutContainerDefinition(String name, String description, String eventHandler, boolean visible, Dimension position,boolean horizontal, List elements, int borderWith,  String borderColor, String backgroundColor) throws Exception
	{
    super(name, description, eventHandler, position, visible, -1, 0, null, borderWith, borderColor, backgroundColor);

    this.unmodifiableElements = Collections.unmodifiableList(elements);
    this.horizontal = horizontal;
	}

  public final IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
   return factory.createFlowLayoutContainer(app, parent,this); 
  }
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    FlowLayoutContainer jacobContainer = new FlowLayoutContainer();
    jacobContainer.setOrientation(FlowLayoutContainerOrientationType.HORIZONTAL);
    jacobContainer.setDimension(getDimension().toJacob());
    jacobGuiElement.getCastorGuiElementChoice().setFlowLayoutContainer(jacobContainer);
  }

  /**
   * Returns the pane definitions of this container.
   * 
   * @return <code>List</code> of {@link IGuiElementDefinition}
   */
  public final List getGuiElementDefinitions()
  {
    return this.unmodifiableElements;
  }

  public boolean isHorizontal()
  {
    return horizontal;
  }
}
