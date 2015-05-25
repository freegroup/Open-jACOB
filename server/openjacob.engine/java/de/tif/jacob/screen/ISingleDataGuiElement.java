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

import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas Herz
 *
 */
public interface ISingleDataGuiElement extends IGuiElement
{
  static public final String RCS_ID = "$Id: ISingleDataGuiElement.java,v 1.9 2009/06/22 10:46:38 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.9 $";

  public int    getTabIndex();
  public String getValue();
  public void   setValue(String value);

  /**
   * Set the editable flag for this element.<br>
   * <br>
   * You can define the editable/readonly flag also in the application definition (*.jad).<br>
   * You can't reset the flag if you define it in the *.jad file as readonly. 
   * The definition in the *.jad is a database constraint and can't be ignored.
   *
   * @param isEditable The isEditable to set.
   * @since 2.7.2
   */
  public void setEditable(boolean isEditable);
  
  /**
   * @since 2.8.0
   * @return
   */
  public boolean isEditable();
  
  /**
   * Returns if the Data element is required. You can override the flag with <code>setRequired(..)</code>.<br>
   * If you define the element required in the application definition (*.jad) you <b>can not</b> reset
   * the required flag with <code>setRequired(false)</code>.<br>
   * The required attribute in the *.jad file is related to the DB constraint and can't be ignored!
   * <br>
   * <br>
   * @return <code>true</code> if required, otherwise <code>false</code>
   */
  public boolean isRequired();
  
  /**
   * Set the required flag for this element.<br>
   * <br>
   * You can define the required flag also in the application definition (*.jad).<br>
   * You can't reset the required flag if you define it in the *.jad file as required. The definition in the *.jad is a database constraint and can't
   * be ignored.
   * <br>
   * @param required
   */
  public void setRequired(boolean required);
  
  // FreeGroup: Kommt eine Ebene tiefer
  public void clear(IClientContext context) throws Exception;
  
  /**
   * Return the underlying talbe field of the UI element or null if the
   * UI element not related to a database field.
   * 
   * @since 2.8.5
   * @return the related table field or null.
   */
  public ITableField getTableField();
}
