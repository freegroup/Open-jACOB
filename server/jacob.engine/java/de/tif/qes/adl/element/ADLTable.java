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

package de.tif.qes.adl.element;

import java.util.List;

import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractTableDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLTable extends AbstractTableDefinition
{
  static public final transient String RCS_ID = "$Id: ADLTable.java,v 1.2 2010-02-23 12:31:22 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private final boolean systemTable;

	public ADLTable(String name, String dbname, String description, String dataSource, ADLKey primaryKey, List otherKeys, List fields, String infoField, Boolean systemTable) throws Exception
	{
    super(name, dbname, dataSource, description, false, false, false);
    
    this.systemTable = systemTable.booleanValue();

		// init fields
		for (int i = 0; i < fields.size(); i++)
		{
      ADLField field = (ADLField) fields.get(i);
      field.setFieldIndex(i);
      addField(field);
      
      if (field.isHistoryField())
        this.setHistoryFieldName(field.getName()); 
    }

    // init keys
    if (null != primaryKey)
      setPrimaryKey(primaryKey);
    
    for (int i = 0; i < otherKeys.size(); i++)
		{
			addKey((ADLKey) otherKeys.get(i));
		}
    
    if (infoField != null)
      setRepresentativeFieldName(infoField);
	}

	/**
	 * @return Returns the systemTable.
	 */
	public boolean isInternal()
	{
		return systemTable;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    ADLDefinition adlDefinition = (ADLDefinition) definition;
    
    // QES system tables have no datasource assigned -> assign default datasource
    if (null == getDataSourceName())
      setDataSourceName(adlDefinition.getDefaultDatasourceName());

    // map datasource if necessary
    setDataSourceName(adlDefinition.mapDatasource(getDataSourceName()));
    
    super.postProcessing(definition, parent);
  }
  
}
