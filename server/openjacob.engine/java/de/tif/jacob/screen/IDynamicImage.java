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

import de.tif.jacob.screen.event.IDynamicImageEventHandler;


/**
 * Interface for dynamic image GUI elements.
 * 
 * @author Andreas Sonntag
 * @see IDynamicImageEventHandler
 * @since 2.8.7
 */
public interface IDynamicImage extends IGuiElement
{
  static public final String RCS_ID = "$Id: IDynamicImage.java,v 1.1 2009/07/02 14:18:52 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Invalidates the image. This enforces a redraw of this image.
   * 
   * @param imageId the image identifier or <code>null</code>
   * 
   * @see IDynamicImageEventHandler#writeImage(IClientContext, java.io.OutputStream, String, int, int)
   */
  public void refresh(String imageId);
  
  /**
   * Sets the tooltip for this image.
   * 
   * @param text
   *          the tooltip to set
   */
  public void setTooltip(String text);
}
