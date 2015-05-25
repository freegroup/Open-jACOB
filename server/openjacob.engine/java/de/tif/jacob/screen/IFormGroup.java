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
public interface IFormGroup extends IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IFormGroup.java,v 1.3 2008/12/01 21:06:27 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.3 $";

  /**
   * Clears the form elements which are related to this form group.
   * 
   * @param context
   *          The current client context
   * @throws Exception
   *           on any problem
   */
  public void clear(IClientContext context) throws Exception;
}