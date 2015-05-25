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

package de.tif.jacob.core.adjustment;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface IDataSourceAdjustment
{
	public static final String RCS_ID = "$Id: IDataSourceAdjustment.java,v 1.4 2011/07/02 09:11:32 freegroup Exp $";
	public static final String RCS_REV = "$Revision: 1.4 $";
	
	/**
   * Gets the implementation how record changes should be historicized.
   * 
   * @return the history implementation
   */
  public IHistory getHistoryImplementation();

  /**
   * Gets the implementation how record should be locked when changes have to be
   * performed.
   * 
   * @return
   */
  public ILocking getLockingImplementation();
	

  /**
   * @return
   */
  public IFieldTypeAdjustment getBinaryAdjustment();

  /**
   * @return
   */
  public ILongTextFieldTypeAdjustment getLongTextAdjustment();

  /**
   * @return
   */
  public IDocumentFieldTypeAdjustment getDocumentAdjustment();


  /**
   * @return
   */
  public IBooleanFieldTypeAdjustment getBooleanAdjustment();
  public void setBooleanAdjustment(IBooleanFieldTypeAdjustment adjustment);

  /**
   * @return
   */
  public IEnumerationFieldTypeAdjustment getEnumerationAdjustment();

  /**
   * @return
   */
  public IIntegerFieldTypeAdjustment getIntegerAdjustment();

  /**
   * @return
   */
  public ILongFieldTypeAdjustment getLongAdjustment();
  
  /**
   * Checks whether the table with the given name should never be dropped during
   * reconfigure process.
   * 
   * @param tableName
   *          the name of the table to check for exclusion
   * @return <code>true</code> the table should be excluded, otherwise
   *         <code>false</code>
   */
  public boolean excludeFromReconfigureDropTable(String tableName);
  
  /**
   * Checks whether the table with the given name should never be altered during
   * reconfigure process.
   * 
   * @param tableName
   *          the name of the table to check for exclusion
   * @return <code>true</code> the table should be excluded, otherwise
   *         <code>false</code>
   */
  public boolean excludeFromReconfigureAlterTable(String tableName);
}
