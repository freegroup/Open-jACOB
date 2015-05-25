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

package de.tif.jacob.screen.event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IOwnDrawElement;

/**
 * Abstract event handler class for own draw areas. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to own draw areas.
 * 
 * @author Andreas Herz
 */
public abstract class IOwnDrawElementEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IOwnDrawElementEventHandler.java,v 1.4 2008/02/13 14:57:06 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * This hook method will be called, if the own draw area has to be refreshed.
   * Therefore, the image within the own draw area has to be painted completely
   * by means of this method.
   * 
   * @deprecated use paint(IClientContext context,IOwnDrawElement image,  Graphics graphics, Dimension dimension) instead
   * @param context
   *          The current client context
   * @param graphics
   *          The graphics object to be used for all draw operations
   * @param dimension
   *          The dimension of the own draw area
   */
  public void paint(IClientContext context, Graphics graphics, Dimension dimension) throws Exception
  {
    
  }
  
  public void paint(IClientContext context,IOwnDrawElement image,  Graphics graphics, Dimension dimension) throws Exception
  {
    paint(context,graphics,dimension);
  }

  /**
   * Returns the color which should be used as transparent color for the image.
   * 
   * @return The transparent color for the image or <code>null</code>, if the
   *         image does not contain a transparent color.
   */
  public abstract Color getTransparentColor() throws Exception;

  /**
   * This hook method will be called, if the user has clicked on a entry of the image map which has
   * been added with IOwnDrawElement.addClickArea(String id, Rectangle area)
   * 
   * 
   * @param context
   *          The current client context
   * @param image
   *          The chart itself
   * @param barIndex
   *          The index of the bar which has been clicked
   */
  public void onClick(IClientContext context, IOwnDrawElement image, String barIndex) throws Exception
  {
  }
}
