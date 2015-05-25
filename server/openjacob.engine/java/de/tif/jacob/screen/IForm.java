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
 */
public interface IForm extends IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IForm.java,v 1.8 2010/11/18 11:25:36 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.8 $";

  /** 
   * Returns <code>true</code> whether the form is in update or new mode.<br>
   * This means that the user has unsaved content.
   * @throws Exception 
   *
   **/
  public boolean isDirty(IClientContext context) throws Exception;
  
  /**
   * Sets the current search browser of the form, i.e. the active browser being
   * displayed on top (of other search browsers, if existing) then the form
   * becomes the current one.
   * 
   * @param browser
   *          the browser to become the current browser
   *          
   * @see #getCurrentBrowser()
   * @see IClientContext#setCurrentForm(String)
   * @see IClientContext#setCurrentForm(String, String)
   */
  public void setCurrentBrowser(IBrowser browser);

  /**
   * Returns all UI Representation elements of the table and field.
   * 
   * @param tableAlias
   * @param fieldName
   * @return
   */
  public List getGuiRepresentations(String tableAlias, String fieldName);

  /**
   * Returns the current search browser of the form, i.e. the active browser
   * being displayed on top (of other search browsers, if existing) then the
   * form becomes the current one.
   * 
   * @return the current search browser of the form
   * 
   * @see #setCurrentBrowser(IBrowser)
   * @see IClientContext#setCurrentForm(String)
   * @see IClientContext#setCurrentForm(String, String)
   */
  public IBrowser getCurrentBrowser();

  /**
   * Clears the form.
   * 
   * @param context
   *          The current client context
   * @throws Exception
   *           on any problem
   */
  public void clear(IClientContext context) throws Exception;

  /**
   * Sets the value of an input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, String value) throws Exception;

  /**
   * Returns the value of the input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @return the visible value of the input field
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public String getInputFieldValue(String fieldName) throws Exception;

  /**
   * Resets the current input focus and sets the element with the lowest tab index as
   * the current input element.
   */
  public void resetTabOrder();
 
  /**
   * Set the element with the input focus.
   * 
   * @param element
   */
  public void setFocus(IGuiElement element);

  /**
   * Set the element with the input focus.
   * 
   * @param element the element for the focus
   * @param caretPosition the caret position within the element
   * @since 2.8.5
   * @see IInFormLongText#setCaretPosition(int)
   */
  public void setFocus(IInFormLongText element, int caretPosition);

  /**
   * Set the element with the input focus.
   * 
   * @param element the element for the focus
   * @param caretPosition the caret position within the element
   * @since 2.8.5
   */
  public void setFocus(IText element, int caretPosition);

  /**
   * Hides the TabStrip of the  SearchBrowser container.
   * 
   * @since 2.8.0
   */
  public void hideTabStrip(boolean flag);
}
