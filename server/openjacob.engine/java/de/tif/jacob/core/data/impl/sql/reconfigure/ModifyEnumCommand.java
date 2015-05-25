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

package de.tif.jacob.core.data.impl.sql.reconfigure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.i18n.CoreMessage;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Andreas Sonntag
 */
public class ModifyEnumCommand extends AlterTableSubCommand
{
  private final List statements;
  private final ISchemaColumnDefinition desiredColumn;
  
  protected ModifyEnumCommand(ISchemaColumnDefinition desiredColumn, List statements)
  {
    this.statements = statements;
    this.desiredColumn = desiredColumn;
  }
  
  /**
   * This method compares the enum values of desired and current column. <br>
   * In case differences have been detected, it is tried to fix them by means
   * of:
   * 
   * <pre>
   * 
   *  UPDATE activity SET activitystatus = -1 WHERE activitystatus = 0
   *  UPDATE activity SET activitystatus = -2 WHERE activitystatus = 1
   *  UPDATE activity SET activitystatus = -3 WHERE activitystatus = 2
   *  UPDATE activity SET activitystatus = 1 WHERE activitystatus = -1
   *  UPDATE activity SET activitystatus = 2 WHERE activitystatus = -2
   *  UPDATE activity SET activitystatus = 0 WHERE activitystatus = -3
   *  
   * </pre>
   * 
   * @param dataSource
   * @param desiredColumn
   * @param currentColumn
   * @param statements
   * @return <code>true</code> if there is a change in the enum labels, otherwise <code>false</code> 
   * @throws UserRuntimeException if there are still entries of old enumeration values 
   */
  public static boolean checkQuintusEnumLabels(SQLDataSource dataSource, ISchemaColumnDefinition desiredColumn, ISchemaColumnDefinition currentColumn, List statements) throws UserRuntimeException
  {
    List currentLabels = currentColumn.getEnumerationLabels();
    List desiredLabels = desiredColumn.getEnumerationLabels();
    
    // there is of course always a change, if the sizes are different
    boolean result = currentLabels.size() != desiredLabels.size();

    // Map{label->Integer}
    Map desiredLabelMap = new HashMap();
    for (int j = 0; j < desiredLabels.size(); j++)
    {
      desiredLabelMap.put(desiredLabels.get(j), new Integer(j));
    }

    // check labels whether they have been moved or deleted
    //
    int movedLabels = 0;
    int statementInsertIndex = statements.size();
    for (int i = 0; i < currentLabels.size(); i++)
    {
      Object currentLabel = currentLabels.get(i);

      Integer newPosition = (Integer) desiredLabelMap.get(currentLabel);
      if (newPosition != null)
      {
        if (newPosition.intValue() != i)
        {
          result = true;
          
          // enum label has been moved
          movedLabels++;
          statements.add(statementInsertIndex++, "UPDATE " + desiredColumn.getDBTableName() + " SET " + desiredColumn.getDBName() + "=-"
              + movedLabels + " WHERE " + desiredColumn.getDBName() + "=" + i);
          statements.add("UPDATE " + desiredColumn.getDBTableName() + " SET " + desiredColumn.getDBName() + "=" + newPosition + " WHERE "
              + desiredColumn.getDBName() + "=-" + movedLabels);
        }
      }
      else
      {
        result = true;
        
        // enum label has been deleted
        // -> check whether there are no entries existing anymore
        //
        try
        {
          Connection connection = dataSource.getConnection();
          try
          {
            Statement statement = connection.createStatement();
            try
            {
              ResultSet rs = statement.executeQuery("SELECT count(*) FROM " + currentColumn.getDBTableName() + " WHERE "
                  + currentColumn.getDBName() + "=" + i);
              try
              {
                rs.next();
                long existingEntries = rs.getLong(1);
                if (existingEntries != 0)
                {
                  // throw exception, because old enum values still exist!
                  //
                  Object[] arguments = new Object[]
                  { currentLabel, currentColumn.getDBTableName(), currentColumn.getDBName(), Long.toString(existingEntries) };
                  throw new UserRuntimeException(new CoreMessage("MSG_QW_FIELDINFO_ENUM_STILL_EXISTING", arguments));
                }
              }
              finally
              {
                rs.close();
              }
            }
            finally
            {
              statement.close();
            }
          }
          finally
          {
            connection.close();
          }
        }
        catch (SQLException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    }
    return result;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.sql.reconfigure.Command#getSQLStatements(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public List getSQLStatements(SQLDataSource dataSource)
  {
    return this.statements;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("MODIFY ENUMS OF COLUMN ").append(this.desiredColumn.getDBName());
    return buffer.toString();
  }
}
