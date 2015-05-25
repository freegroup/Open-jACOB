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

package de.tif.jacob.screen;

import java.awt.Rectangle;


/**
 * @author Andreas Herz
 *
 */
public interface IOwnDrawElement extends IGuiElement , IImageProvider
{
  static public final String RCS_ID = "$Id: IOwnDrawElement.java,v 1.2 2007/11/05 21:23:58 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Invalidate the image. This enforce a redraw of the image.<br>
   * The jACOB engine calls the event handler 'paint(...) method to do that.
   *
   */
  public void refresh();
  
  /**
   * Add a clickable rectangle in the own draw image. 
   * The callback "onClick(...)" will be called if the user clicks onto this area
   * 
   * @param id
   * @param area
   */
  public void addClickArea(String id, String tooltip, Rectangle area);
}
