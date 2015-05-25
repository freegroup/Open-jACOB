/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer.base.rte;

/**
 * Report text expression function resolver.
 * 
 * @since 2.9
 * @author Andreas Sonntag
 */
public interface IRTEFunctionResolver
{
  /**
   * Return an ignorable character, which might act as keyword separator
   * @return
   */
  public String ignore();

  public String tilde();

  /**
   * Return a tab of default space number.
   * 
   * @return
   */
  public String tab();

  public String tab(String num);

  /**
   * Returns the actual date.
   * <p>
   * Multiple calls will return the same date string!
   * 
   * @return
   */
  public String date();

  /**
   * Returns the actual time.
   * <p>
   * Multiple calls will return the same time string!
   * 
   * @return
   */
  public String time();

  /**
   * Returns the report name.
   * 
   * @return the report name
   */
  public String name();

  /**
   * Count the number of records.
   * 
   * @return
   * @throws Exception
   */
  public String count() throws Exception;

  /**
   * Count the number of records there the given field is not <code>null</code>.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String count(String field) throws Exception;

  /**
   * The average of all values of the given field.
   * <p>
   * Note: Field values which are <code>null</code> are not considered.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String average(String field) throws Exception;

  /**
   * The summation of all values of the given field.
   * <p>
   * Note: Field values which are <code>null</code> are not considered.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String sum(String field) throws Exception;

  /**
   * The minimum of all values of the given field.
   * <p>
   * Note: Field values which are <code>null</code> are not considered.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String min(String field) throws Exception;

  /**
   * The maximum of all values of the given field.
   * <p>
   * Note: Field values which are <code>null</code> are not considered.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String max(String field) throws Exception;

  /**
   * The value of the given field, i.e. the value of the first record found.
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public String value(String field) throws Exception;
}
