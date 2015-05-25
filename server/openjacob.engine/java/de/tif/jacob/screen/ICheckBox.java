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
public interface ICheckBox extends ISingleDataGuiElement
{
  static public String RCS_ID = "$Id: ICheckBox.java,v 1.3 2008/07/11 09:18:41 freegroup Exp $";
  static public String RCS_REV = "$Revision: 1.3 $";
  
  /**
   * Returns whether the checkbox is checked or unchecked.
   * 
   * @return <code>true</code> if checked, <code>false</code> if unchecked 
   */
  public boolean isChecked();

  /**
   * Sets the state of the checkbox. Note that this method does not trigger 
   * an action event like ICheckBoxEventHandler.onCheck.
   *  
   * @since 2.7.4
   */
  public void setChecked(boolean state);

}
