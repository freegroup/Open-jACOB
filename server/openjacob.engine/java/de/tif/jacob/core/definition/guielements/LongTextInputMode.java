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

package de.tif.jacob.core.definition.guielements;

import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;

/**
 * @author Andreas Sonntag
 *
 * @deprecated Replaced by means of {@link LongTextEditMode}
 */
public final class LongTextInputMode
{
  static public final transient String RCS_ID = "$Id: LongTextInputMode.java,v 1.4 2010/01/28 10:37:10 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  public static final LongTextInputMode READONLY = new LongTextInputMode("READONLY", TextInputModeType.READONLY, null);
  public static final LongTextInputMode PREPEND = new LongTextInputMode("PREPEND", TextInputModeType.PREPEND, LongTextEditMode.PREPEND);
  public static final LongTextInputMode APPEND = new LongTextInputMode("APPEND", TextInputModeType.APPEND, LongTextEditMode.APPEND);
  public static final LongTextInputMode FULLEDIT = new LongTextInputMode("FULLEDIT", TextInputModeType.FULLEDIT, LongTextEditMode.FULLEDIT);
  
  private final String name;
  
  private final TextInputModeType textInputModeType;
  private final LongTextEditMode editMode;
  
  /**
   * 
   */
  private LongTextInputMode(String name, TextInputModeType longTextModeType, LongTextEditMode editMode)
  {
    this.name = name;
    this.textInputModeType = longTextModeType;
    this.editMode = editMode;
  }

  public String toString()
  {
    return this.name;
  }
  
  public static LongTextInputMode fromJacob(TextInputModeType jacobType)
  {
    if (jacobType == TextInputModeType.READONLY)
      return READONLY;
    if (jacobType == TextInputModeType.APPEND)
      return APPEND;
    if (jacobType == TextInputModeType.PREPEND)
      return PREPEND;
    if (jacobType == TextInputModeType.FULLEDIT)
      return FULLEDIT;
    
    return null;
  }
  
  /**
   * Returns the corresponding edit mode.
   * 
   * @return the corresponding edit mode or <code>null</code>
   * @since 2.9
   */
  public LongTextEditMode getEditMode()
  {
    return this.editMode;
  }

  protected TextInputModeType toJacob()
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
