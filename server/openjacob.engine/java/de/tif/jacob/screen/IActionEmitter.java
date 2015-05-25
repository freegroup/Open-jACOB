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
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IActionEmitter extends IGuiElement
{
  /**
   * This method allows to set the mode for search actions dynamically.
   * 
   * @param safeSearch if <code>true</code> unconstrained search actions
   * will not be allowed and lead to an error message, otherwise if 
   * <code>false</code> unconstrained searches will be accepted.
   * @deprecated
   * TODO: hat nichts an eine ActionEmitter zu suchen. Bei einem 'Cancel' Button macht so eine Funktion keinen Sinn.
   */
  public void setSearchMode(boolean safeSearch);
  
  /**
   * Checks whether safe search is enabled.
   * 
   * @return <code>true</code> if safe search is active, otherwise <code>false</code>
   * @deprecated
   * TODO: hat nichts an eine ActionEmitter zu suchen. Bei einem 'Cancel' Button macht so eine Funktion keinen Sinn.
   */
  public boolean isSafeSearch();
  
}
