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

/**
 * @author Andreas Herz
 *
 */
public interface IStaticImage extends IGuiElement
{
  static public final String RCS_ID = "$Id: IStaticImage.java,v 1.3 2009/01/30 14:12:15 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.3 $";

  /**
   * Set the tooltip for this image.
   * 
   * @since 2.8.2
   * @param text the tooltip to set
   */
  public void setTooltip(String text);
}
