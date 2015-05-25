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
public interface ILabel extends IGuiElement
{
  static public final String RCS_ID = "$Id: ILabel.java,v 1.2 2008/07/01 11:04:16 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.2 $";

  public void   setLabel(String caption);
  public String getLabel();

  /**
   * Transform the normal label to a hyperlink with the given URL.
   * 
   * @param url
   */
  public void   setLink(URL url);

  /**
   * Transform the normal label to a hyperlink with the given URL.
   * 
   * @param url
   * @since 2.7.4
   */
  public void   setLink(String url);
}
