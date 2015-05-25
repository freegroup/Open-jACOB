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

import java.util.List;

/**
 * @author Andreas Herz
 *
 */
public interface IGroupContainer extends IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IGroupContainer.java,v 1.1 2008/11/22 12:05:29 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Retrieve the groups of this container
   * 
   * @since 2.8.0
   * @return List[IGroup]
   */
  public List getGroups();
  
  /**
   * Retrieve the group with the given index
   * 
   * @since 2.8.0
   * @return the Group with the given index
   */
  public IGroup getGroup(int index);

  /**
   * Retrieve the first group with the given name. Multiple
   * groups with the same name can be stored in the GroupContainer
   * 
   * @since 2.8.0
   * @return the first group with the given name.
   */
  public IGroup getGroup(String paneName);
}
