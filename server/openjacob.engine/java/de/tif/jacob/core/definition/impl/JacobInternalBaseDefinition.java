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

package de.tif.jacob.core.definition.impl;

import java.sql.Types;

import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.schema.Schema;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;

/**
 * Internal base jACOB schema definition which is part of all jACOB
 * installations. It does not matter the type of relational database and the
 * setting of the data source adjustment.
 * 
 * @author Andreas Sonntag
 */
public class JacobInternalBaseDefinition extends Schema
{
  public static final String DOCUMENT_TABLE_NAME = "jacob_document";

  public static final String DOCUMENT_ID_COLUMN_NAME = "id";
  
  public JacobInternalBaseDefinition(SQLDataSource dataSource)
  {
    add(createDocumentTableDefinition());
  }
  
  /**
   * Create document table definition.
   * <p>
   * Note: This method is called by external tools as well, e.g. datatransfer.
   * 
   * @return document table definition
   */
  public static Table createDocumentTableDefinition()
  {
    // create definition for table holding document values
    Table documentTableDefinition = new Table(DOCUMENT_TABLE_NAME, Table.USER_TABLE);
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, DOCUMENT_ID_COLUMN_NAME, Types.CHAR, UUID.STRING_LENGTH, 0, null, false));
    // Note: Use 255 as maximum length becuase MySQL before V5.0 can only handle 0..255 for varchars
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, "docname", Types.VARCHAR, 255, 0, null, false));
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, "docsize", Types.BIGINT, 0, 0, null, false));
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, "doccontent", Types.LONGVARBINARY, 0, 0, null, true));
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, "tablename", Types.VARCHAR, 30, 0, null, true));
    documentTableDefinition.add(new TableColumn(DOCUMENT_TABLE_NAME, "columnname", Types.VARCHAR, 30, 0, null, true));
    documentTableDefinition.addPrimaryKeyPart("PK_JACOB_DOCUMENT", (short) 1, DOCUMENT_ID_COLUMN_NAME);
    documentTableDefinition.addIndexPart("IDX_JACOB_DOCUMENTNAME", false, (short) 1, "docname");
    return documentTableDefinition;
  }
}
