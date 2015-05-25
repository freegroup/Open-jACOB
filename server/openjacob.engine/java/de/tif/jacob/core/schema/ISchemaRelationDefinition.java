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
 * Created on 14.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.schema;

import java.util.Iterator;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ISchemaRelationDefinition
{
  /**
   * Returns the name of the foreign key constraints associated with this relation.
   * 
   * @return foreign key name
   */
  public String getSchemaForeignKeyName();
  
  /**
   * Returns the column names of the foreign key
   * 
   * @return <code>Iterator</code> of <code>String</code>
   */
  public Iterator getSchemaForeignColumnNames();

  
  /**
   * Returns the name of the table this foreign key belongs to.
   * 
   * @return primary table name
   */
  public String getSchemaTableName();
  
  /**
   * Returns the name of the primary table this foreign key is associated with.
   * 
   * @return primary table name
   */
  public String getSchemaPrimaryTableName();
}
