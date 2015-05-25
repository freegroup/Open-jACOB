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
package de.tif.jacob.core.adjustment.impl;

import de.tif.jacob.core.adjustment.IBooleanFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.adjustment.IDocumentFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IEnumerationFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IHistory;
import de.tif.jacob.core.adjustment.IIntegerFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.ILocking;
import de.tif.jacob.core.adjustment.ILongFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.ILongTextFieldTypeAdjustment;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class JacobAdjustment implements IDataSourceAdjustment
{
	public static final transient String RCS_ID = "$Id: JacobAdjustment.java,v 1.4 2011/07/02 09:11:32 freegroup Exp $";
	public static final transient String RCS_REV = "$Revision: 1.4 $";

	private final IHistory history = new JacobHistory();
	
	private final ILocking locking = new JacobLocking();
	
  private IBooleanFieldTypeAdjustment booleanAdjustment = new BooleanAdjustment();
  private IFieldTypeAdjustment binaryAdjustment = new JacobBinaryAdjustment();
  private IDocumentFieldTypeAdjustment documentAdjustment = new JacobDocumentAdjustment();
  private IEnumerationFieldTypeAdjustment enumerationAdjustment = new EnumerationAdjustment();
  private IIntegerFieldTypeAdjustment integerAdjustment = new IntegerAdjustment();
  private ILongFieldTypeAdjustment longAdjustment = new LongAdjustment();
  private ILongTextFieldTypeAdjustment longTextAdjustment = new JacobLongTextAdjustment();

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getHistoryImplementation()
   */
  public IHistory getHistoryImplementation()
  {
    return this.history;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getLockingImplementation()
   */
  public ILocking getLockingImplementation()
  {
    return this.locking;
  }
  
  @Override
  public IBooleanFieldTypeAdjustment getBooleanAdjustment() 
  {
    return booleanAdjustment;
  }

  @Override
  public void setBooleanAdjustment(IBooleanFieldTypeAdjustment adjustment)
  {
      this.binaryAdjustment = adjustment;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getBinaryAdjustment()
   */
  public IFieldTypeAdjustment getBinaryAdjustment()
  {
    return this.binaryAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getDocumentAdjustment()
   */
  public IDocumentFieldTypeAdjustment getDocumentAdjustment()
  {
    return this.documentAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getEnumerationAdjustment()
   */
  public IEnumerationFieldTypeAdjustment getEnumerationAdjustment()
  {
    return this.enumerationAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getIntegerAdjustment()
   */
  public IIntegerFieldTypeAdjustment getIntegerAdjustment()
  {
    return this.integerAdjustment;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getLongTextAdjustment()
   */
  public ILongTextFieldTypeAdjustment getLongTextAdjustment()
  {
    return this.longTextAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getLongAdjustment()
   */
  public ILongFieldTypeAdjustment getLongAdjustment()
  {
    return this.longAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#excludeFromReconfigureAlterTable(java.lang.String)
   */
  public boolean excludeFromReconfigureAlterTable(String tableName)
  {
    // all tables should be altered
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#excludeFromReconfigureDropTable(java.lang.String)
   */
  public boolean excludeFromReconfigureDropTable(String tableName)
  {
    // do not drop jACOB internal tables
    return tableName.toLowerCase().startsWith("jacob_");
  }
}
