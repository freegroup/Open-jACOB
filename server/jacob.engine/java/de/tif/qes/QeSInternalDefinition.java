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
 * Created on 12.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.qes;

import java.sql.Types;

import de.tif.jacob.core.data.impl.UUID;
import de.tif.jacob.core.data.impl.schema.Table;
import de.tif.jacob.core.data.impl.schema.TableColumn;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.definition.impl.JacobInternalBaseDefinition;

/**
 * Internal jACOB schema definition for data source adjustment setting <b>jACOB</b>.
 * 
 * @author Andreas Sonntag
 */
public class QeSInternalDefinition extends JacobInternalBaseDefinition
{
	public static final String LONGTEXT_TABLE_NAME = "qw_text";
	public static final String BINARY_TABLE_NAME = "qw_byte";
	public static final String IDS_TABLE_NAME = "qw_keys";
  public static final String LOCKS_TABLE_NAME = "qw_locks";
  
  public static final String LONGTEXT_VALUE_COLUMN_NAME = "text";
  public static final String BINARY_VALUE_COLUMN_NAME = "text";
  
  public static final String LONGTEXT_ID_COLUMN_NAME = "qwkey";
  public static final String BINARY_ID_COLUMN_NAME = "qwkey";
  
	/**
   * Do not instantiate now, since we do not want to create Quintus database schema.
   * 
	 * @param dataSource
	 */
	private QeSInternalDefinition(SQLDataSource dataSource)
  {
	  super(dataSource);
	  
    if (dataSource.needsMappingOfLongText())
    {
      add(createLongTextTableDefinition());
    }

    if (dataSource.needsMappingOfBinary())
    {
      add(createBinaryTableDefinition());
    }

    // create definition for table holding next ids
    Table idsTableDefinition = new Table(IDS_TABLE_NAME, Table.USER_TABLE);
    idsTableDefinition.add(new TableColumn(IDS_TABLE_NAME, "tablename", Types.VARCHAR, 240, 0, null, false));
    idsTableDefinition.add(new TableColumn(IDS_TABLE_NAME, "keyvalue", Types.BIGINT, 0, 0, null, false));
    idsTableDefinition.addPrimaryKeyPart("PK_QW_KEYS_TABLENAME", (short) 1, "tablename");
    add(idsTableDefinition);

    // create definition for table holding locks
    Table locksTableDefinition = new Table(LOCKS_TABLE_NAME, Table.USER_TABLE);
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "tablename", Types.VARCHAR, 30, 0, null, false));
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "keyvalue", Types.VARCHAR, 128, 0, null, false));
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "locktime", Types.TIMESTAMP, 0, 0, null, true));
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "pid", Types.BIGINT, 0, 0, null, true));
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "username", Types.VARCHAR, 30, 0, null, true));
    locksTableDefinition.add(new TableColumn(LOCKS_TABLE_NAME, "hostname", Types.VARCHAR, 128, 0, null, true));
    locksTableDefinition.addPrimaryKeyPart("PK_QW_LOCKS_PKEY", (short) 1, "tablename");
    locksTableDefinition.addPrimaryKeyPart("PK_QW_LOCKS_PKEY", (short) 2, "keyvalue");
    add(locksTableDefinition);
  }
  
  /**
   * Create long text table definition.
   * <p>
   * Note: This method is called by external tools as well, e.g. datatransfer.
   * 
   * @return long text table definition
   */
  public static Table createLongTextTableDefinition()
  {
    // create definition for table holding long text values
    Table longTextTableDefinition = new Table(LONGTEXT_TABLE_NAME, Table.USER_TABLE);
    longTextTableDefinition.add(new TableColumn(LONGTEXT_TABLE_NAME, LONGTEXT_ID_COLUMN_NAME, Types.BIGINT, 0, 0, null, false));
    longTextTableDefinition.add(new TableColumn(LONGTEXT_TABLE_NAME, LONGTEXT_VALUE_COLUMN_NAME, Types.LONGVARCHAR, 0, 0, null, true));
    longTextTableDefinition.add(new TableColumn(LONGTEXT_TABLE_NAME, "tablename", Types.VARCHAR, 30, 0, null, true));
    longTextTableDefinition.add(new TableColumn(LONGTEXT_TABLE_NAME, "fieldname", Types.VARCHAR, 30, 0, null, true));
    longTextTableDefinition.add(new TableColumn(LONGTEXT_TABLE_NAME, "textkey", Types.BIGINT, 0, 0, null, true));
    longTextTableDefinition.addPrimaryKeyPart("PK_QW_TEXT_QWKEY", (short) 1, LONGTEXT_ID_COLUMN_NAME);
    return longTextTableDefinition;
  }
  
  /**
   * Create binary table definition.
   * <p>
   * Note: This method is called by external tools as well, e.g. datatransfer.
   * 
   * @return binary table definition
   */
  public static Table createBinaryTableDefinition()
  {
    // create definition for table holding binary values
    Table binaryTableDefinition = new Table(BINARY_TABLE_NAME, Table.USER_TABLE);
    binaryTableDefinition.add(new TableColumn(BINARY_TABLE_NAME, BINARY_ID_COLUMN_NAME, Types.BIGINT, 0, 0, null, false));
    binaryTableDefinition.add(new TableColumn(BINARY_TABLE_NAME, BINARY_VALUE_COLUMN_NAME, Types.LONGVARBINARY, 0, 0, null, true));
    binaryTableDefinition.add(new TableColumn(BINARY_TABLE_NAME, "tablename", Types.VARCHAR, 30, 0, null, true));
    binaryTableDefinition.add(new TableColumn(BINARY_TABLE_NAME, "columnname", Types.VARCHAR, 30, 0, null, true));
    binaryTableDefinition.add(new TableColumn(BINARY_TABLE_NAME, "textkey", Types.BIGINT, 0, 0, null, true));
    binaryTableDefinition.addPrimaryKeyPart("PK_QW_BYTE_QWKEY", (short) 1, BINARY_ID_COLUMN_NAME);
    return binaryTableDefinition;
  }
}
