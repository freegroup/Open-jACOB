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

import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;

/**
 * @author Andreas Herz
 * 
 */
public interface ILongText extends IText
{
  static public final String RCS_ID = "$Id: ILongText.java,v 1.8 2010/01/28 10:37:10 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.8 $";

  /**
   * Returns the edit mode of this long text GUI element.
   * 
   * @return the edit mode
   */
  public LongTextEditMode getEditMode();

  /**
   * Sets the edit mode of this long text GUI element.
   * 
   * @param editMode
   *          the desired edit mode or <code>null</code> to set the default
   *          mode, which is defined by the edit mode of the underlying long
   *          text field
   * @see LongTextFieldType#getEditMode()
   * @since 2.9
   */
  public void setEditMode(LongTextEditMode editMode);

  public boolean hasWordwrap();
}
