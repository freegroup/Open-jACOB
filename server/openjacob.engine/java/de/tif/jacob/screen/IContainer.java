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
import java.util.List;

/**
 * @author Andreas Herz
 *
 * @since 2.8.3
 */
public interface IContainer extends IGuiElement
{
  static public final String RCS_ID = "$Id: IContainer.java,v 1.5 2010/09/29 13:13:27 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.5 $";

  /**
   * Retrieve the tab panes of this container
   * 
   * @since 2.10
   * @return List[IPane]
   */
  public List getPanes();
  
  /**
   * Set the current active tab of the tab container
   * 
   * @param paneIndex
   * @since 2.7.2
   * @deprecated use setActivePane(IClientContext context, int paneIndex); instead
   */
  public void setActivePane(int paneIndex);
  
  /**
   * Set the current active tab of the tab container
   * 
   * @param paneIndex
   * @since 2.7.4
   * @see IPane#setActive(IClientContext)
   */
  public void setActivePane(IClientContext context, int paneIndex) throws Exception;

  
  /**
   * Returns the index of the current active tab of the tab container.
   * 
   * @return the active pane index or <code>-1</code>, if no pane is visible
   * @see #setActivePane(int)
   * @see #getPanes()
   * @since 2.7.4
   */
  public int getActivePaneIndex();


  /**
   * Sets the value of an input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new value of the input field.
   * @since 2.10
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
   * @since 2.10
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
   * @since 2.10
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
   * @since 2.10
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
   * @since 2.10
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public String getInputFieldValue(String fieldName) throws Exception;
}
