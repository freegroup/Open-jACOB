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

package de.tif.jacob.core.definition;

import java.awt.Color;
import java.awt.Rectangle;

import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * This interface represents GUI elements on an abstract level.
 * 
 * @author Andreas Sonntag
 */
public interface IGUIElementDefinition extends INamedObjectDefinition
{
  /**
   * Returns the caption of this GUI element.
   * 
   * @return the caption of the GUI element or <code>null</code> if no caption
   *         is existing or supported for this type of GUI element.
   */
  public Caption getCaption();

  /**
   * Returns the graphical extension of this gui element.
   * 
   * @return the rectangle of the GUI element on the screen or <code>null</code>,
   *         if managed by container.
   */
  public Rectangle getRectangle();

  /**
   * Returns the default visibility of this GUI element.
   * 
   * @return the default visibility
   */
  public boolean isVisible();

  /**
   * Returns the tabulator index of this GUI element.
   * 
   * @return the tabulator index of this GUI element within the corresponding
   *         group or -1 if the element does not participate within the groups
   *         tab group.
   */
  public int getTabIndex();

  /**
   * Returns the pane index of this GUI element.
   * 
   * @return the pane index of this GUI element within the corresponding
   *         group.
   */
  public int getPaneIndex();

  /**
   * Method to create a concret GUI element out of this GUI element definition.
   * <p>
   * Note that this method is currently for internal use only!
   * 
   * @param factory
   *          the application factory
   * @param app
   *          the concret application object
   * @param parent
   *          the parent GUI element
   * @return the created GUI element
   */
  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent);

  /**
   * @since 2.8.6
   */
  public int getBorderWith();
  
  /**
   * @since 2.8.6
   */
  public Color getBorderColor();
  
  /**
   * @since 2.8.6
   */
  public Color getBackgroundColor();
}
