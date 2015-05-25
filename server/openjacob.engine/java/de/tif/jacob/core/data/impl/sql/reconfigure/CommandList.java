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

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class CommandList
{
  static private final transient Log logger = LogFactory.getLog(CommandList.class);

  private final List commands = new ArrayList();
  private int nextCommandToExecute = 0;
  private int nextStatementToExecute = 0;
  private String lastErrorStatement = null;
  private String lastErrorMessage = null;
  private boolean executionError = false;
  private StringBuffer executionSummary = new StringBuffer();
  
  private int numberOfDropRelationCommands = 0;
  private int numberOfCreateRelationCommands = 0;

  public void add(Command command)
  {
    if (command instanceof DropRelationCommand)
    {
      // add new drop relation command at the end of all existing drop relation commands
      this.commands.add(numberOfDropRelationCommands++, command);
    }
    else if (command instanceof CreateRelationCommand)
    {
      // add new create relation command at the end of all existing commands
      this.commands.add(command);
      this.numberOfCreateRelationCommands++;
    }
    else
    {
      // add regular command before the first create relation command
      this.commands.add(this.commands.size()-this.numberOfCreateRelationCommands, command);
    }
  }

  public int size()
  {
    return this.commands.size();
  }

  public Command get(int index)
  {
    return (Command) this.commands.get(index);
  }

  public String getSQLStatementScript(SQLDataSource dataSource)
  {
    StringBuffer buffer = new StringBuffer(1024);
    for (int i = 0; i < size(); i++)
    {
      List statements = get(i).getSQLStatements(dataSource);
      for (int j = 0; j < statements.size(); j++)
      {
        Object statement = statements.get(j);
        if (statement instanceof ReconfigureComment)
        {
          // statement is a comment
          dataSource.appendDBComment(buffer, statement.toString());
          buffer.append("\n\n");
        }
        else
        {
          buffer.append(statement);
          buffer.append(dataSource.getSQLStatementSeparator());
        }
      }
    }
    return buffer.toString();
  }

  private void execute(Connection con, String sqlStatement) throws SQLException
  {
    Statement statement = con.createStatement();
    try
    {
      logger.info("Executing: " + sqlStatement);
      statement.execute(sqlStatement);
    }
    finally
    {
      statement.close();
    }
  }
  
  public boolean executionCompleted()
  {
    return this.nextCommandToExecute >= size();
  }

  /**
   * @return Returns the lastErrorMessage.
   */
  public String getLastErrorMessage()
  {
    return lastErrorMessage;
  }
  
  /**
   * @return Returns the lastErrorStatement.
   */
  public String getLastErrorStatement()
  {
    return lastErrorStatement;
  }
  
  /**
   * @return Returns the executionError.
   */
  public boolean hasExecutionError()
  {
    return executionError;
  }
  
  /**
   * @return Returns the executionSummary.
   */
  public String getExecutionSummary()
  {
    return executionSummary.toString();
  }
  
  public void execute(SQLDataSource dataSource) throws Exception
  {
    execute(dataSource, true, true);
  }

  public boolean execute(SQLDataSource dataSource, boolean abortOnError) throws Exception
  {
    return execute(dataSource, abortOnError, false);
  }
  
  private boolean execute(SQLDataSource dataSource, boolean abortOnError, boolean throwError) throws Exception
  {
    Connection connection = dataSource.getConnection();
    try
    {
      connection.setAutoCommit(true);

      while (this.nextCommandToExecute < size())
      {
        List statements = get(this.nextCommandToExecute).getSQLStatements(dataSource);
        while (this.nextStatementToExecute < statements.size())
        {
          this.lastErrorMessage = null;
          this.lastErrorStatement = null;
          Object nextItem = statements.get(this.nextStatementToExecute++);
          if (nextItem instanceof ReconfigureComment)
          {
            // next item is a comment
            
            if (nextItem instanceof ReconfigureWarning)
            {
              this.executionSummary.append(nextItem).append("\r\n\r\n");
              this.executionError = true;
            }
          }
          else
          {
            // next item is a statement to execute
            
            String statement = (String) nextItem;
            try
            {
              this.executionSummary.append("SQL: ").append(statement).append("\r\n");
              execute(connection, statement);
              this.executionSummary.append("RES: OK\r\n\r\n");
            }
            catch (SQLException ex)
            {
              this.executionError = true;
              this.lastErrorMessage = ex.getMessage();
              this.lastErrorStatement = statement;

              this.executionSummary.append("RES: " + this.lastErrorMessage + "\r\n\r\n");

              if (throwError)
              {
                // rethrow
                throw ex;
              }

              if (abortOnError)
              {
                return false;
              }
            }
          }
        }
                
        this.nextStatementToExecute = 0;
        this.nextCommandToExecute++;
      }
    }
    finally
    {
      try
      {
        connection.setAutoCommit(false);
      }
      catch (Exception ex)
      {
      }
      connection.close();
    }
    
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < this.commands.size(); i++)
    {
      buffer.append(this.commands.get(i).toString());
      buffer.append("\r\n");
    }
    return buffer.toString();
  }

  public void print(PrintStream ps)
  {
    for (int i = 0; i < this.commands.size(); i++)
    {
      ps.println(this.commands.get(i).toString());
    }
  }
}
