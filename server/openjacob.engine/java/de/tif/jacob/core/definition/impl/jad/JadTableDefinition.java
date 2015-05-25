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

package de.tif.jacob.core.definition.impl.jad;

import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.impl.AbstractTableDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadTableDefinition extends AbstractTableDefinition
{
  static public transient final String RCS_ID = "$Id: JadTableDefinition.java,v 1.2 2010/02/23 12:22:26 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
	/**
	 *  
	 */
	public JadTableDefinition(CastorTable table) throws Exception
	{
		super(table.getName(), table.getDbName(), table.getDatasource(), table.getDescription(), table.getOmitLocking(), table.getNtoMTable(), table.getRecordsAlwaysDeletable());

    // handle properties
    if (table.getPropertyCount() > 0)
      putCastorProperties(table.getProperty());
    
		// init fields
		for (int i = 0; i < table.getFieldCount(); i++)
		{
      addField(new JadTableField(table.getField(i), i));
		}

		// init keys
    if (null != table.getPrimaryKey())
      setPrimaryKey(new JadKey(table.getPrimaryKey(), KeyType.PRIMARY));
          
    for (int i = 0; i < table.getForeignKeyCount(); i++)
    {
    	addKey(new JadKey(table.getForeignKey(i), KeyType.FOREIGN));
    }
    for (int i = 0; i < table.getUniqueIndexCount(); i++)
    {
    	addKey(new JadKey(table.getUniqueIndex(i), KeyType.UNIQUE));
    }
    for (int i = 0; i < table.getIndexCount(); i++)
    {
    	addKey(new JadKey(table.getIndex(i), KeyType.INDEX));
    }
    
    if (table.getRepresentativeField() != null)
    	setRepresentativeFieldName(table.getRepresentativeField());
    
    if (table.getHistoryField() != null)
      setHistoryFieldName(table.getHistoryField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.ITableDefinition#isInternal()
	 */
	public boolean isInternal()
	{
		return false;
	}

}
