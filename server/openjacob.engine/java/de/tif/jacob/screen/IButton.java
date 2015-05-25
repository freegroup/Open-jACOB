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
public interface IButton extends IGuiElement
{
  static public final String RCS_ID = "$Id: IButton.java,v 1.4 2010/08/19 09:33:22 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.4 $";

  public void setIcon(Icon icon);
  
  /**
   * Set the emphasize attribute of the button.
   * 
   * @since 2.7.2
   */
  public void setEmphasize(boolean flag);
  
  /**
   * Transform the normal button to a hyperlink with the given URL. The representation of the button didn't change.<br>
   * <br>
   * This method is usefull if you want to open the native Mail client of the user. The example below opens the mail client
   * with a prefilled <b>to</b> and <b>body</b>.
   * <pre>
   * button.setLink("mailto:beispiel@example.org?body=Hallo%20Fritz,%0D%0A%0D%0Aich%20wollte%20nur%20sagen,%20dass%20");
   * </pre>
   * <b>Note:</b> The onClick will not be called if you have transform the button.
   * 
   * 
   * @param url the link or null to reset the button to normal behaviour.
   * @since 2.10
   */
  public void setLink(String url); 
}
