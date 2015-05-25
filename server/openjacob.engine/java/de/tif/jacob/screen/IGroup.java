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

import java.math.BigDecimal;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * @author Andreas Herz
 *
 */
public interface IGroup extends IGuiElement
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IGroup.java,v 1.8 2010/08/19 09:33:22 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.8 $";

  /**
   * Returns the associated search browser of this group.
   * 
   * @return the associated search browser
   * 
   * @see IForm#getCurrentBrowser()
   * @see IForm#setCurrentBrowser(IBrowser)
   */
  public IBrowser getBrowser();
  
  /**
   * @param autoSelect
   * @deprecated nur temporär bitte entfernen wenn Aufgabe 504 erledigt
   */
  public void setBrowserAutoSelect(boolean autoSelect);

  /**
   * @return
   * @deprecated nur temporär bitte entfernen wenn Aufgabe 504 erledigt
   */
  public boolean isBrowserAutoSelect();

  /**
   * Clears the group.
   * <p>
   * Note: This method is equivalent to {@link #clear(IClientContext, boolean)},
   * if the second argument is set to <code>true</code>.
   * 
   * @param context
   *          The current client context
   * @throws Exception
   *           on any problem
   */
  public void clear(IClientContext context) throws Exception;

  /**
   * Clears the group.
   * 
   * @param context
   *          The current client context
   * @param clearSearchBrowser
   *          if <code>true</code> the associated search browser will be
   *          cleared as well.
   * @throws Exception
   *           on any problem
   */
  public void clear(IClientContext context, boolean clearSearchBrowser) throws Exception;

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
   * Sets the value given as decimal of an input field specified by name.
   * <p>
   * The decimal value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new decimal value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, BigDecimal value) throws Exception;

  /**
   * Sets the value given as double of an input field specified by name.
   * <p>
   * The double value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new double value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, Double value) throws Exception;

  /**
   * Sets the value given as float of an input field specified by name.
   * <p>
   * The float value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new float value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, Float value) throws Exception;

  /**
   * Sets the value given as date of an input field specified by name.
   * <p>
   * The date value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new date value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, java.util.Date value) throws Exception;

  /**
   * Returns the value of the input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field
   * @return the visible value of the input field
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public String getInputFieldValue(String fieldName) throws Exception;

  /**
   * Returns the element with the lowest tab index, i.e. the first element in
   * tab order.
   */
  public ISingleDataGuiElement getFirstElementInTabOrder();

  /**
   * Enable or disable the border of this group.
   * 
   * @param flag
   *          The border visibility.
   * @deprecated This method will be removed in future releases.
   * @since 2.6
   */
  public void setBorder(boolean flag);
  
  /**
   * Returns the related context menu of the group.
   * 
   * @return
   * @since 2.8.0
   */
  public IContextMenu getContextMenu();

  /**
   * Returns the selected data table record of the group.<br>
   * <br>
   * 
   * @return the selected data table record or <code>null</code> if no
   *         selected record exists
   * @since 2.10
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception;
  

  /**
   * Create new table record an fill them with the current input field values of the
   * group children (InputFields). Starts a new transaction.
   * 
   * @param context
   * @return
   * @since 2.9.1
   */
  public IDataTableRecord newRecord(IClientContext context) throws Exception;
  
  /**
   * Create new table record an fill them with the current input field values of the
   * group children (InputFields). 
   * 
   * @param context
   * @param trans the transaction to use
   * @return
   * @since 2.9.1
   */
  public IDataTableRecord newRecord(IClientContext context, IDataTransaction trans) throws Exception;

  /**
   * Flag which indicates that an empty search browser should be hidden.
   * 
   * @since 2.10
   * @return
   */
  public boolean getHideEmptyBrowser();
}
