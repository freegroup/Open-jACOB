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
public interface IMutableRadioButtonGroup extends IRadioButtonGroup
{
  static public final String RCS_ID = "$Id: IMutableRadioButtonGroup.java,v 1.1 2007/05/23 11:20:55 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Add the handsover entry to the radio button group element
   * 
   * @param entry      The option to add
   * @param label       The label to show
   */
  public void addOption(String entry, String label);

  /**
   * Removes the handsover entry from the listbox element
   * 
   * @param entry      The option to remove
   * @return returns [true] if the options has been remove or [false] if the 
   *           handsover options was not a member of the listbox
   */
  public boolean removeOption(String entry);

  
  /**
   * Remove all options of the listbox
   *
   * @return [false] if the listbox already be empty
   */
  public boolean removeOptions();
  
  /**
   * 
   * @param optionToTest test if the handsover option member of the optionlist
   * @return [true] if the handover option part of the listbox.
   */
  public boolean hasOption(String optionToTest);
   
  /**
   * Select the handsover option in the listbox if the entry exists.
   * 
   * @param entry The option to be select
   */
  public void selectOption(String entry);
  
  /**
   * Removes the current selection.
   *
   */
  public void clearSelection();
  
  /**
   * 
   * @return the selected Strings or new String[0] of non element selected
   */
  public String[] getSelection();
}  

