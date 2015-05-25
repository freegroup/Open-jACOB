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

import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author Andreas Herz
 *
 */
public interface IForeignField extends ISingleDataGuiElement, ICaptionProvider
{
  static public final String RCS_ID = "$Id: IForeignField.java,v 1.4 2009/03/03 08:45:16 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.4 $";

  public void setValue(IClientContext context, IDataTableRecord record) throws Exception;

  /**
   * Returns the name of the foreign table alias.
   * 
   * @return the foreign table alias name.
   * 
   * @see IGuiElement#getGroupTableAlias()
   * @since 2.7.4        
   */
  public String getForeignTableAlias();

  /**
   * Checks whether the foreign field is backfilled.
   * @return <code>true</code> if backfilled, <code>true</code> if not; 
   */
  public boolean isBackfilled();
  
  
  /**
   * Returns the selected record of the foreign field<br>
   * The record is related to the table alias of the foreign field and not the
   * the alias of the parent group.
   * 
   * @since 2.8.4
   * @param context
   * @return the selected record of the foreign field
   * @throws Exception
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception;
}
