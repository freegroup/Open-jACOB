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
import de.tif.jacob.core.definition.impl.jad.castor.Heading;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HeadingDefinition extends GUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: HeadingDefinition.java,v 1.2 2009/07/27 15:06:11 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final Alignment.Horizontal halign;

  /**
	 * @param name
	 * @param eventHandler
	 * @param visible
	 * @param tabIndex
	 * @param caption
	 */
	public HeadingDefinition(String name, String eventHandler, boolean visible, int tabIndex, int paneIndex, Caption caption)
  {
    super(name, null, eventHandler, caption.getDimension(), visible, tabIndex, paneIndex, caption, -1,null,null);

    this.halign = caption.getHorizontalAlign();
  }


  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createHeading(app, parent,this); 
  }
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    Heading jacobHeading = new Heading();
    jacobHeading.setCaption(getCaption().toJacob());
    jacobGuiElement.getCastorGuiElementChoice().setHeading(jacobHeading);
  }

  /**
   * @return Returns the halign.
   */
  public Alignment.Horizontal getHorizontalAlign()
  {
    return halign;
  }
}