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

package de.tif.jacob.core.data.impl;

import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IDataSource
{
	/**
	 * Test this datasource.
	 * <p>
	 * The test has been successfully, if no exception has been thrown.
	 * 
	 * @return additional data source information
	 * @throws Exception
	 *           Test of datasource has failed.
	 */
  public String test() throws Exception;

  /**
   * Returns the adjustment of the data source.
   * 
   * @return the adjustment.
   */
  public IDataSourceAdjustment getAdjustment();

	/**
   * Returns the data source name.
   * 
   * @return the data source name.
   */
  public String getName();
  
  /**
	 * Checks whether this datasource is a jACOB predefined datasource.
	 * <p>
	 * Predefined datasource may not be administrated by means of the admin application.
	 * 
	 * @return <code>true</code> datasource is predefined, otherwise <code>false</code>
	 */
  public boolean isPredefined();

  /**
   * Checks whether this datasource is transient datasource.
   * 
   * @return <code>true</code> datasource is transient, otherwise <code>false</code>
   * @since 2.6
   */
  public boolean isTransient();
  
  /**
   * Checks whether the datasource supports auto key generation, i.e. the keys are
   * not created by means of {@link #newJacobIds(ITableDefinition, ITableField, int)}.
   * 
   * @return <code>true</code> if the datasource supports auto id generation,
   *         otherwise <code>false</code>
   */
  public boolean supportsAutoKeyGeneration();
}
