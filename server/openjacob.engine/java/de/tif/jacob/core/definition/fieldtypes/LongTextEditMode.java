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

package de.tif.jacob.core.definition.fieldtypes;

import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.screen.ILongText;

/**
 * Long text edit mode.
 * 
 * @see LongTextFieldType#getEditMode()
 * @see ILongText#setEditMode(LongTextEditMode)
 * @see ILongText#getEditMode()
 * @author Andreas Sonntag
 * @since 2.7.2
 */
public final class LongTextEditMode
{
  static public final transient String RCS_ID = "$Id: LongTextEditMode.java,v 1.4 2010/04/15 12:16:23 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * Mode to restrict editing a long text field by means of prepending text
   * only.
   */
  public static final LongTextEditMode PREPEND = new LongTextEditMode("PREPEND", LongTextEditModeType.PREPEND);
  
  /**
   * Mode to restrict editing a long text field by means of appending text only.
   */
  public static final LongTextEditMode APPEND = new LongTextEditMode("APPEND", LongTextEditModeType.APPEND);
  
  /**
   * Mode to allow full editing of a long text field.
   */
  public static final LongTextEditMode FULLEDIT = new LongTextEditMode("FULLEDIT", LongTextEditModeType.FULLEDIT);

  private final String name;

  private final LongTextEditModeType textInputModeType;
  
  /**
   * 
   */
  private LongTextEditMode(String name, LongTextEditModeType longTextModeType)
  {
    this.name = name;
    this.textInputModeType = longTextModeType;
  }

  public String toString()
  {
    return this.name;
  }
  
  protected static LongTextEditMode fromJacob(LongTextEditModeType jacobType)
  {
    if (jacobType == LongTextEditModeType.APPEND)
      return APPEND;
    if (jacobType == LongTextEditModeType.PREPEND)
      return PREPEND;
    if (jacobType == LongTextEditModeType.FULLEDIT)
      return FULLEDIT;
    return FULLEDIT;
//    throw new RuntimeException("Unknown castor type "+jacobType);
  }
  
  protected LongTextEditModeType toJacob()
  {
  	return this.textInputModeType;
  }
  
  /**
   * 
   * @return Returns the name of the mode.
   */
  public String getName()
  {
    return name;
  }
}
