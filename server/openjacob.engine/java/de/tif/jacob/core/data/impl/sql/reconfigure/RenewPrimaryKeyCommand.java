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
 * Created on 12.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class RenewPrimaryKeyCommand extends AlterTableSubCommand
{
  protected final ISchemaKeyDefinition desiredPrimaryKeyDefinition;
  protected final ISchemaKeyDefinition currentPrimaryKeyDefinition;
  
  /**
   * @param desiredPrimaryKeyDefinition
   */
  public RenewPrimaryKeyCommand(ISchemaKeyDefinition currentPrimaryKeyDefinition, ISchemaKeyDefinition desiredPrimaryKeyDefinition)
  {
    this.currentPrimaryKeyDefinition = currentPrimaryKeyDefinition;
    this.desiredPrimaryKeyDefinition = desiredPrimaryKeyDefinition;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    List result = new ArrayList();

    result.add(new ReconfigureWarning("Can not renew primary key of table " + this.table.getDBName()));

    return result;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("RENEW PRIMARY KEY ").append(this.desiredPrimaryKeyDefinition.getDBName());
    return buffer.toString();
  }

}
