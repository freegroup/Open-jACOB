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

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractGuiElement extends AbstractElement
{
  private final String eventHandler;

  /**
   * @param name
   */
  public AbstractGuiElement(String name, String description, String eventHandler)
  {
    super(name, description);
    this.eventHandler = eventHandler;
  }

  /**
   * Returns the class name of the event handler of this gui element.
   * 
   * @return event handler class name or <code>null</code> if default method
   *         to determine the event handler should be used.
   */
  public final String getEventHandler()
  {
    return this.eventHandler;
  }

}
