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
public interface IDomain extends IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IDomain.java,v 1.2 2011/01/03 17:10:42 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Clears this domain, i.e. the domains data accessor and input fields will be
   * reset to their default values.
   * 
   * @param context
   *          The current client context
   * @throws Exception
   */
  public void clear(IClientContext context) throws Exception;

  /**
   * Returns the current form of this domain, i.e. the visible form if this
   * domain becomes the active domain.
   * 
   * @param context
   *          The current client context
   * @return the current form of this domain
   */
  public IForm getCurrentForm(IClientContext context);

  /**
   * Sets the current visible form of this domain.
   * <p>
   * 
   * Note: This domain becomes the active domain, if this is not already the
   * case!
   * 
   * @param context
   *          The current client context
   * @param form
   *          The form to become the current visible form.
   */
  public void setCurrentForm(IClientContext context, IForm form) throws Exception;

  /**
   * Sets the value of a GUI field in the group.<br>
   * 
   * @param formName
   *          The name of the form (the parent) of the field element.
   * @param fieldName
   *          The GUI input field name. <b>Not</b> the database field name.
   * @param value
   *          The value of the input field.
   */
  public abstract void setInputFieldValue(String formName, String fieldName, String value) throws Exception;

  /**
   * Returns the GUI value of the hands over GUI element.
   * 
   * @param formName
   *          The name of the form (the parent) of the field element.
   * @param fieldName
   *          the name of the input field
   * @return the visible value of the input field
   * @throws Exception
   *           throws an Exception if the field does not exist or is not an
   *           input field.
   */
  public abstract String getInputFieldValue(String formName, String fieldName) throws Exception;

  /**
   * Returns the GUI elements which are related to the given table alias and
   * field name.<br>
   * 
   * @param tableAlias
   *          The table alias for that you want the GUI input element
   * @param fieldName
   *          The field name for that you want the GUI input element.
   * @return <code>List</code> of {@link ISingleDataGuiElement} which are
   *         related to the database field.
   */
  public abstract List getGuiRepresentations(String tableAlias, String fieldName);

  public abstract IForm getForm(String form);
}
