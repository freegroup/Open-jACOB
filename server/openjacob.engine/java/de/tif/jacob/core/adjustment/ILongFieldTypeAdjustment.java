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

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ILongFieldTypeAdjustment
{
	public static final String RCS_ID = "$Id: ILongFieldTypeAdjustment.java,v 1.3 2009/01/13 15:31:57 ibissw Exp $";
	public static final String RCS_REV = "$Revision: 1.3 $";

	public Object newKey(DataSource dataSource, ITableDefinition table, ITableField field, boolean dbIncrement, IDataNewKeysCache newKeysCache) throws Exception;
  
  public boolean setNextKey(DataSource dataSource, ITableDefinition table, ITableField field, boolean dbIncrement, long nextKey) throws Exception;
}
