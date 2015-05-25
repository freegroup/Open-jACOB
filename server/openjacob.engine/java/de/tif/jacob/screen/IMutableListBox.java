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
public interface IMutableListBox extends IListBox
{
  static public final String RCS_ID = "$Id: IMutableListBox.java,v 1.6 2008/12/01 21:06:27 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.6 $";

  /**
   * Add the handsover entry to the listbox element
   * 
   * @param value      The option to add
   */
  public void addOption(String value);

  /**
   * Add the handsover entry to the listbox element
   * 
   * @param label      
   *         The label of the listbox entry
   * @param value      
   *         The value of the listbox entry
   *         
   * @since 2.8.0
   */
  public void addOption(String label, String value);

  /**
   * Add the handsover entry to the listbox element and additionally assigns an
   * icon and an optional tooltip to it.
   * 
   * @param value      
   *         The value of the listbox entry
   * @param icon
   *          the icon to assign to the entry
   * @param tooltip
   *          the tooltip to assign to the icon or <code>null</code>
   *          
   * @since 2.7.2
   */
  public void addOption(String value, Icon icon, String tooltip);

  /**
   * Add the handsover entry to the listbox element and additionally assigns an
   * icon and an optional tooltip to it.
   * 
   * @param label      
   *         The label of the listbox entry
   * @param value      
   *         The value of the listbox entry
   * @param icon
   *          the icon to assign to the entry
   * @param tooltip
   *          the tooltip to assign to the icon or <code>null</code>
   *          
   * @since 2.8.0
   */
  public void addOption(String label, String value, Icon icon, String tooltip);

  /**
   * Enable/disable the handsover option in the listbox.<br>
   * The option is visible but not selectable.<br>
   * 
   * @param value
   *          The option to enable/disable
   * @param enableFlag
   *          The enable flag
   *          
   * @since 2.8.0
   */
  public void enableOption(String value, boolean enableFlag);
  
  /**
   * Removes the handsover entry from the listbox element
   * 
   * @param value      The option to remove
   * @return returns [true] if the options has been remove or [false] if the 
   *           handsover options was not a member of the listbox
   */
  public boolean removeOption(String value);

  /**
   * Set the entries of the ListBox element
   * 
   * @param values      The options to add
   */
  public void setOptions(String[] values);
  
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
   * @param selection The option to be select
   */
  public void selectOption(String selection);
  
  /**
   * Select the handsover options in the listbox if the entries exist in the list box.
   * 
   * @param selections The options to be select
   */
  public void selectOptions(String[] selections);
 
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

