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

import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.DynamicImage;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * 
 * @author Andreas Sonntag
 * @since 2.8.7
 */
public final class DynamicImageDefinition extends GUIElementDefinition
{
	static public final transient String RCS_ID = "$Id: DynamicImageDefinition.java,v 1.2 2009/07/27 15:06:11 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	private final String tooltip;
  
	/**
	 * @param name
	 * @param eventHandler
	 * @param visible
	 * @param position
	 */
	public DynamicImageDefinition(String name, String description, String eventHandler, boolean visible, Dimension position, String tooltip)
  {
    super(name, description, eventHandler, position, visible, -1, 0, null,-1,null,null);
    this.tooltip = tooltip;
  }

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
   return factory.createDynamicImage(app, parent, this); 
  }
  
  /**
   * Returns the tooltip of the image
   * 
   * @return the image source.
   */
  public String getTooltip()
  {
    return tooltip;
  }
  
  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    DynamicImage jacobDynamicImage = new DynamicImage();
    jacobDynamicImage.setDimension(getDimension().toJacob());
    jacobGuiElement.getCastorGuiElementChoice().setDynamicImage(jacobDynamicImage);
  }
}
