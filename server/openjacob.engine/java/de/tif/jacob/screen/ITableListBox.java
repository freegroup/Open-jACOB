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

import de.tif.jacob.screen.event.ILinkEventListener;


/**
 * @author Andreas Herz
 *
 */
public interface ITableListBox extends IBrowser
{
  static public final String RCS_ID = "$Id: ITableListBox.java,v 1.14 2010/12/10 14:32:40 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.14 $";
 
  /**
   * Install an action which is related to each row in the browser.
   * 
   * TODO: move the method to IBrowser if required by app-programmer. Need additional effort of implementation.
   * @param action
   * @throws Exception 
   * @since 2.10
   */
  public void addAction( IBrowserRecordAction action) throws Exception;

  /**
   * Sets the parser to use when parsing links. We need to parse links to transform a link 
   * reference passed as a raw string by the long text element into a clickable link object.
   * <br>
   * You can use {@link RegExprLinkParser} as a simple regular expression link parser.
   * 
   * @since 2.10
   * @param parser the link parser to use
   * @param listener the event handler to use
   * @throws Exception 
   * @see RegExprLinkParser
   */
  public void setLinkHandling(ILinkParser parser, ILinkEventListener listener) throws Exception;
}
