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

import java.net.URL;

/**
 * @author Andreas Herz
 * 
 */
public interface ICaption extends IGuiElement
{
  static public final String RCS_ID = "$Id: ICaption.java,v 1.5 2010/10/06 07:49:06 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.5 $";

  /**
   * {@inheritDoc}
   */
  public void setLabel(String label);

  /**
   * {@inheritDoc}
   */
  public String getLabel();

  /**
   * Sets an URL which will be called, if the label of this caption is clicked.
   * 
   * @param url
   *          the url to be called or <code>null</code> to reset
   */
  public void setLink(URL url);

  /**
   * Sets an URL which will be called, if the label of this caption is clicked.
   * 
   * @param url
   *          the url to be called or <code>null</code> to reset
   */
  public void setLink(String url);

  /**
   * Set the optional Tooltip of the Caption.
   * 
   * @since 2.10
   */
  public void setTooltip(String tooltip);
}
