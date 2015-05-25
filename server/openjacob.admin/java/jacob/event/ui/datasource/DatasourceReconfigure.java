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

package jacob.event.ui.datasource;

import jacob.model.Useddatasource;

import java.util.Iterator;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.definition.impl.JacobInternalDefinition;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DatasourceReconfigure generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class DatasourceReconfigure extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DatasourceReconfigure.java,v 1.7 2010/07/13 17:57:09 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.7 $";

  private static class ExecuteCallback implements IFormDialogCallback
  {
    private final CommandList commands;
    private final SQLDataSource sqlDataSource;

    private ExecuteCallback(SQLDataSource sqlDataSource, CommandList commands)
    {
      this.sqlDataSource = sqlDataSource;
      this.commands = commands;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("execute".equals(buttonId))
      {
        if (this.commands.execute(this.sqlDataSource, !"on".equals(values.get("ignoreerrors"))))
        {
          // execution completed
          if (this.commands.hasExecutionError())
          {
            // Create a FormDialog to show execution summary
            //
            FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns
                                      "10dlu,p,10dlu,grow,20dlu"); // rows
            CellConstraints cc = new CellConstraints();
            IFormDialog dialog = context.createFormDialog("Reconfigure completed with errors", layout, this);
            dialog.addLabel("Execution summary:", cc.xy(1, 1));
            dialog.addTextArea("summary", this.commands.getExecutionSummary(), true, cc.xy(1, 3));
            dialog.setCancelButton("Close");

            // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
            dialog.show(500, 400);    
          }
          else
          {
            context.createMessageDialog("The data source has been successfully reconfigured").show();
          }
        }
        else
        {
          // execution aborted
          
          // Create a FormDialog with a list of all application versions.
          //
          FormLayout layout = new FormLayout("10dlu, grow, p, p, 10dlu", // columns
                                             "10dlu, p, 10dlu, grow, 10dlu, p, 20dlu"); // rows
          CellConstraints cc = new CellConstraints();
          IFormDialog dialog = context.createFormDialog("Reconfigure data source", layout, this);
          dialog.addLabel("Execution error:", cc.xy(1, 1));
          dialog.addTextArea("statements", "Statement: "+this.commands.getLastErrorStatement()+"\r\n\r\nError: "+this.commands.getLastErrorMessage(), cc.xywh(1, 3, 3, 1));
          dialog.addLabel("Ignore errors", cc.xy(2, 5));
          dialog.addCheckBox("ignoreerrors", false, cc.xy(3, 5));
          dialog.addSubmitButton("execute", "Continue");

          // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
          dialog.show(400, 250);    
        }
      }
    }
  }

  /**
   * @param context
   * @param sqlDataSource
   * @param desiredSchemaDefinition
   * @param currentSchemaDefinition
   */
  private static void startSetupReconfigure(IClientContext context, SQLDataSource sqlDataSource, ISchemaDefinition desiredSchemaDefinition, ISchemaDefinition currentSchemaDefinition, boolean compareOnly) throws Exception
  {
    Reconfigure reconfigure = sqlDataSource.getReconfigureImpl();
    
    // setup datasource first
    //
    if (compareOnly == false)
      sqlDataSource.setup(currentSchemaDefinition);
    
    // execute reconfigure check then
    //
    CommandList commands = reconfigure.reconfigure(desiredSchemaDefinition, currentSchemaDefinition, true);
    
    if (commands.size()==0)
    {
      context.createMessageDialog("Data source is already up to date").show();
    }
    else
    {
      // Create a FormDialog which lists all statements to execute.
      //
      IFormDialog dialog;
      if (compareOnly)
      {
        FormLayout layout = new FormLayout("10dlu, grow, p, p, 10dlu", // columns
            "10dlu, p, 10dlu, grow, 20dlu"); // rows
        CellConstraints cc = new CellConstraints();
        dialog = context.createFormDialog("Compare data source", layout, new ExecuteCallback(sqlDataSource, commands));
        dialog.addLabel("Compare statements:", cc.xy(1, 1));
        dialog.addTextArea("statements", commands.getSQLStatementScript(sqlDataSource), "sql", cc.xywh(1, 3, 3, 1));
      }
      else
      {
        FormLayout layout = new FormLayout("10dlu, grow, p, p, 10dlu", // columns
            "10dlu, p, 10dlu, grow, 10dlu, p, 20dlu"); // rows
        CellConstraints cc = new CellConstraints();
        dialog = context.createFormDialog("Reconfigure data source", layout, new ExecuteCallback(sqlDataSource, commands));
        dialog.addLabel("Reconfigure statements to be executed:", cc.xy(1, 1));
        dialog.addTextArea("statements", commands.getSQLStatementScript(sqlDataSource), "sql", cc.xywh(1, 3, 3, 1));
        dialog.addLabel("Ignore errors", cc.xy(2, 5));
        dialog.addCheckBox("ignoreerrors", false, cc.xy(3, 5));
        dialog.addSubmitButton("execute", "Execute");
      }

      // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
      dialog.show(500, 400);
    }
  }   
  
  /**
   * Callback for additional approval of reconfigure.
   */
  private static class ReconfigureApprovedCallback implements IOkCancelDialogCallback
  {
    private final SQLDataSource sqlDataSource;
    private final ISchemaDefinition desiredSchemaDefinition;
    private final ISchemaDefinition currentSchemaDefinition;
    
    private ReconfigureApprovedCallback(SQLDataSource sqlDataSource, ISchemaDefinition desiredSchemaDefinition, ISchemaDefinition currentSchemaDefinition)
    {
      this.sqlDataSource = sqlDataSource;
      this.desiredSchemaDefinition = desiredSchemaDefinition;
      this.currentSchemaDefinition = currentSchemaDefinition;
    }
    
    public void onCancel(IClientContext context)
    {
    }
    
    public void onOk(IClientContext context) throws Exception
    {
      startSetupReconfigure(context, sqlDataSource, desiredSchemaDefinition, currentSchemaDefinition, false);
    }   
  }
  
  private static class ReconfigureCallback implements IFormDialogCallback
  {
    private final IDataTable applicationSchemaTable;
    private final SQLDataSource sqlDataSource;
    private final boolean compareOnly;

    private ReconfigureCallback(SQLDataSource sqlDataSource, IDataTable lockTable, boolean compareOnly)
    {
      this.sqlDataSource = sqlDataSource;
      this.applicationSchemaTable = lockTable;
      this.compareOnly = compareOnly;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("reconfigure".equals(buttonId))
      {
        int index = -1;
        try
        {
          index = Integer.parseInt((String) values.get("schemaIndex"));
        }
        catch (Exception ex)
        {
          // ignore
        }
        
        if (index == -1)
        {
          context.createMessageDialog("No application schema selected").show();
        }
        else
        {
          // Extract schema definition from application
          IDataTableRecord schemaRecord = applicationSchemaTable.getRecord(index);
          ISchemaDefinition desiredSchemaDefinition = DeployMain.getApplication(schemaRecord.getStringValue(Useddatasource.applicationname), //
                                                                                schemaRecord.getStringValue(Useddatasource.applicationversion)).getSchemaDefinition(this.sqlDataSource.getName());
          
          Reconfigure reconfigure = this.sqlDataSource.getReconfigureImpl();
          
          ISchemaDefinition currentSchemaDefinition = reconfigure.fetchSchemaInformation();
          
          if (this.compareOnly == false)
          {
            // Security check for adjustment
            if (this.sqlDataSource.isQuintusAdjustment())
            {
              // database already setup with Quintus internal tables?
              if (!isQuintusSchema(currentSchemaDefinition))
              {
                // No
                context.createMessageDialog("Attention:\r\n" + "The underlying database has not been setup as Quintus database.\r\n" + "Use QDesigner tool to setup the database first!").show();
                return;
              }
            }
            else
            {
              // jACOB adjustment

              if (!isJacobSchema(currentSchemaDefinition))
              {
                if (isQuintusSchema(currentSchemaDefinition))
                {
                  context.createOkCancelDialog(
                    "Attention:\r\n" + "The underlying database contains internal Quintus tables, but the adjustment has been set to jACOB conformity.\r\n" + "A wrong data source adjustment could destroy the data structure!\r\n"
                        + "Do you want to proceed?", new ReconfigureApprovedCallback(sqlDataSource, desiredSchemaDefinition, currentSchemaDefinition)).show();
                  return;
                }
              }
            }
          }
                    
          // everything ok so far -> start setup and reconfigure
          startSetupReconfigure(context, sqlDataSource, desiredSchemaDefinition, currentSchemaDefinition, this.compareOnly);
        }
      }
    }
  }
  
  /**
   * Checks whether the given schema is a Quintus schema.
   * 
   * @param schemaDefinition
   * @return
   */
  private static boolean isQuintusSchema(ISchemaDefinition schemaDefinition)
  {
    // Simply check whether there is a QW_KEYS table 
    Iterator iter = schemaDefinition.getSchemaTableDefinitions();
    while (iter.hasNext())
    {
      if ("QW_KEYS".equalsIgnoreCase(((ISchemaTableDefinition) iter.next()).getDBName()))
        return true;
    }
    return false;
  }

  /**
   * Checks whether the given schema is a jACOB schema.
   * 
   * @param schemaDefinition
   * @return
   */
  private static boolean isJacobSchema(ISchemaDefinition schemaDefinition)
  {
    // Simply check whether there is a JACOB_IDS table 
    Iterator iter = schemaDefinition.getSchemaTableDefinitions();
    while (iter.hasNext())
    {
      if (JacobInternalDefinition.IDS_TABLE_NAME.equalsIgnoreCase(((ISchemaTableDefinition) iter.next()).getDBName()))
        return true;
    }
    return false;
  }

  protected static void reconfigure(IClientContext context, boolean compareOnly) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();
    
    DataSource dataSource = DataSource.get(record.getStringValue("name"));
    if (!(dataSource instanceof SQLDataSource))
    {
      context.createMessageDialog("Only SQL data sources could be reconfigured").show();
      return;
    }
    
    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;

    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable table = accessor.getTable(Useddatasource.NAME);
    table.qbeSetKeyValue(Useddatasource.datasourcename, sqlDataSource.getName());
    if (!compareOnly)
      table.qbeSetValue(Useddatasource.reconfiguremode, "!" + Useddatasource.reconfiguremode_ENUM._none);
    table.search();
    if (table.recordCount() == 0)
    {
      context.createMessageDialog(compareOnly ? //
          "There are no appropriate application versions existing to compare this data source"
          : //
          "There are no appropriate application versions existing to reconfigure this data source").show();
      return;
    }
    
    // Retrieve all appropriate application versions.
    //
    String[] entries = new String[table.recordCount()];
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord schemaRecord = table.getRecord(i);
      
      // FREEGROUP: Achtung: Es duerfen nicht mehrere Leerzeichen hintereinander in einem Entrystring
      // vorhanden sein, da FormLayout diese durch einfache Leerzeichen zu ersetzen scheint und dann
      // der Eintrag ueber den HashMap-Eintrag nicht mehr gefunden werden kann.
      entries[i] = schemaRecord.getStringValue(Useddatasource.applicationname) + " " + schemaRecord.getStringValue(Useddatasource.applicationversion);
    }
    
    // Create a FormDialog with a list of all application versions.
    //
    FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns
                                       "10dlu,p,10dlu,grow,10dlu"); // rows
    CellConstraints cc = new CellConstraints();
    IFormDialog dialog = context.createFormDialog(compareOnly ? "Compare data source" : "Reconfigure data source", layout, new ReconfigureCallback(sqlDataSource, table, compareOnly));
    dialog.addLabel("Select application schema:", cc.xy(1, 1));
    dialog.addListBox("schema", entries, entries.length == 1 ? 0 : -1, cc.xy(1, 3));
    dialog.addSubmitButton("reconfigure", compareOnly ? "Compare" : "Reconfigure");

    // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
    dialog.show(250, 300);    
  }
  
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    reconfigure(context, false);
  }

  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED && //
        !Datasource.rdbType_ENUM._Lucene.equals(context.getSelectedRecord().getStringValue(Datasource.rdbtype)));
  }
}
