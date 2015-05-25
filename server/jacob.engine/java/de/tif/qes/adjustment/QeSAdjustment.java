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
/*
 * Created on 23.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.qes.adjustment;

import de.tif.jacob.core.adjustment.IDataSourceAdjustment;
import de.tif.jacob.core.adjustment.IDocumentFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IEnumerationFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.IHistory;
import de.tif.jacob.core.adjustment.IIntegerFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.ILocking;
import de.tif.jacob.core.adjustment.ILongFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.ILongTextFieldTypeAdjustment;
import de.tif.jacob.core.adjustment.impl.JacobDocumentAdjustment;
import de.tif.jacob.core.definition.impl.JacobInternalBaseDefinition;
import de.tif.qes.QeSHistory;
import de.tif.qes.QeSLocking;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class QeSAdjustment implements IDataSourceAdjustment
{
	public static final transient String RCS_ID = "$Id: QeSAdjustment.java,v 1.2 2009-07-28 23:05:54 sonntag Exp $";
	public static final transient String RCS_REV = "$Revision: 1.2 $";

	private final IHistory history = new QeSHistory();
	private final ILocking locking = new QeSLocking();
	
  private final IFieldTypeAdjustment binaryAdjustment = new QeSBinaryAdjustment();
  private final IEnumerationFieldTypeAdjustment enumerationAdjustment = new QeSEnumerationAdjustment();
  private final IIntegerFieldTypeAdjustment integerAdjustment = new QeSIntegerAdjustment();
  private final ILongFieldTypeAdjustment longAdjustment = new QeSLongAdjustment();
  private final ILongTextFieldTypeAdjustment longTextAdjustment = new QeSLongTextAdjustment();
  
  // Use standard jacob adjustment since Quintus can not handle documents on scratch
  private final IDocumentFieldTypeAdjustment documentAdjustment = new JacobDocumentAdjustment();

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
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getLongAdjustment()
   */
  public ILongFieldTypeAdjustment getLongAdjustment()
  {
    return this.longAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#getLongTextAdjustment()
   */
  public ILongTextFieldTypeAdjustment getLongTextAdjustment()
  {
    return this.longTextAdjustment;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#excludeFromReconfigureAlterTable(java.lang.String)
   */
  public boolean excludeFromReconfigureAlterTable(String tableName)
  {
    // do not alter Quintus internal tables
    return tableName.toLowerCase().startsWith("qw_");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.adjustment.IDataSourceAdjustment#excludeFromReconfigureDropTable(java.lang.String)
   */
  public boolean excludeFromReconfigureDropTable(String tableName)
  {
    // do not drop Quintus internal tables and jacob_document table!
    return tableName.toLowerCase().startsWith("qw_") || tableName.equalsIgnoreCase(JacobInternalBaseDefinition.DOCUMENT_TABLE_NAME);
  }
}
