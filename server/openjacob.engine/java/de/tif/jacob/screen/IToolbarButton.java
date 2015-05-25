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
public interface IToolbarButton extends IGuiElement
{
  static public final String RCS_ID = "$Id: IToolbarButton.java,v 1.1 2007/01/19 09:50:30 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";
  
  /**
   * Set the flag to emphasize the toolbar button
   * 
   * @param emphasize <b>true</b> to emphasize/highlight the button
   */
  public void setEmphasize(boolean emphasize);
  
  /**
   * Add an image decoration to button.
   * 
   * @param decoration
   */
  public void setDecoration(ImageDecoration decoration);
}
