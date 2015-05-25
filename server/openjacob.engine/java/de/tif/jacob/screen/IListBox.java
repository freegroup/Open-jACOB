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
public interface IListBox extends ISingleDataGuiElement
{
  static public final String RCS_ID = "$Id: IListBox.java,v 1.7 2009/04/24 14:53:52 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.7 $";

  /**
   * Returns all enabled options of this listbox.<br>
   * Elements which has been disabled via a <code>myListbox.enableOption("test",false")</code>
   * will not be returned.<br>
   * 
   * @return all enabled options of this listbox element.
   */
  public String[] getOptions();

  /**
   * Enable or disable the hands over option
   * 
   * @param entry      The option to toggle
   * @param enableFlag The state of the option (true=enabled; false=disabled)
   */
  public void enableOption(String entry, boolean enableFlag);
  
  /**
   * Enable or disable all options in the listbox.
   * 
   * @param enableFlag The state of the options (true=enabled; false=disabled)
   */
  public void enableOptions(boolean enableFlag);

  /**
   * Whether the multiselect is enabled in the SEARCH-Mode.<br>
   * Default is <code>true</code>. 
   * 
   * @param multichoice
   * @since 2.7.4
   */
  public void setMultichoice(boolean multichoice);

  /**
   * Set the options which enforce a server callback if the selection has 
   * been changed. All entries generates a server request per default.<br>
   * <br>
   * This method is usefull if the server shouldn't be called if the user select
   * dedicated entries in the combobox.<br>
   * <br>
   * @param options
   * @since 2.8.2
   */
  public void setCallbackOptions(String[] values);
}
