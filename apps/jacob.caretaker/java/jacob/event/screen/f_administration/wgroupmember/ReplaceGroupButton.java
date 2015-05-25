/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 10 20:50:09 CEST 2006
 */
package jacob.event.screen.f_administration.wgroupmember;

import jacob.common.AppLogger;
import jacob.exception.BusinessException;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 * The event handler for the ReplaceGroupButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ReplaceGroupButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ReplaceGroupButton.java,v 1.1 2006/08/11 22:45:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
  
  private static int procedureLoadedSQLDataSourceHashCode = 0;
  
  private class ConfirmCallback implements IOkCancelDialogCallback
  {
    private final IDataTableRecord workgroupRecord;
    private final String newName;

    private ConfirmCallback(IDataTableRecord workgroupRecord, String newName)
    {
      this.workgroupRecord = workgroupRecord;
      this.newName = newName;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      // Workgroup ersetzen durch neue Kopie
      //
      int newWorkgroupId = replaceWorkgroup(workgroupRecord, newName);
      logger.info("Workgroup '" + workgroupRecord.getValue("name") + "' with ID " + workgroupRecord.getValue("pkey") + " has been replaced by workgroup '"
          + newName + "' with ID " + newWorkgroupId);

      // Neue Workgroup zurückfüllen
      //
      IDataTable workgroupTable = context.getDataTable();
      workgroupTable.qbeClear();
      workgroupTable.qbeSetKeyValue("pkey", Integer.toString(newWorkgroupId));
      workgroupTable.search();
      context.getDataAccessor().propagateRecord(workgroupTable.getSelectedRecord(), "r_wkgrpemp", Filldirection.BOTH);
    }
  }
  
  private class EnterNameCallback implements IAskDialogCallback
  {
    private final IDataTableRecord workgroupRecord;

    private EnterNameCallback(IDataTableRecord workgroupRecord)
    {
      this.workgroupRecord = workgroupRecord;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context, String newName) throws Exception
    {
      newName = StringUtil.toSaveString(newName).trim();

      if (newName.length() == 0)
        throw new BusinessException("Name darf nicht leer sein");

      if (newName.equals(workgroupRecord.getStringValue("name")))
        throw new BusinessException("Neuer Name darf nicht identisch mit dem alten Namen sein");

      StringBuffer message = new StringBuffer();
      message.append("Wollen sie wirklich ");
      message.append(isAK(workgroupRecord) ? "den AK" : "die HWG");
      message.append(" '").append(this.workgroupRecord.getStringValue("name")).append("' durch ");
      message.append(isAK(workgroupRecord) ? "den neuen AK" : "die neue HWG");
      message.append(" '").append(newName).append("' ersetzen?");

      context.createOkCancelDialog(message.toString(), new ConfirmCallback(this.workgroupRecord, newName)).show();
    }
  }
  
  private static boolean isAK(IDataTableRecord workgroupRecord) throws Exception
  {
    return "AK".equals(workgroupRecord.getStringValue("wrkgrptype"));
  }

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord workgroupRecord = context.getSelectedRecord();

    StringBuffer message = new StringBuffer();
    message.append("Geben sie einen Namen für ");
    message.append(isAK(workgroupRecord) ? "den neuen AK" : "die neue HWG");
    message.append(" ein:");

    context.createAskDialog(message.toString(), "NEW " + workgroupRecord.getValue("name"), new EnterNameCallback(workgroupRecord)).show();
  }
  
  private String getResourceContent(String fileName) throws Exception
  {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    if (null == inputStream)
    {  
      throw new Exception("Could not open file '"+fileName+"' as stream");
    }
    
    try
    {
      return IOUtils.toString(inputStream, "ISO-8859-1");
    }
    finally
    {
      IOUtils.closeQuietly(inputStream);
    }
  }
  
  /**
   * Diese Procedure kann durch
   * OracleDatasource.createOrReplaceStoredProcedure() ersetzt werden sobald
   * sicher gestellt ist, daß der Caretaker nur auf jACOB 2.6 oder höher
   * deployed wird.
   * 
   * @param connection
   * @throws Exception
   */
  private void loadProcedure(Connection connection) throws Exception
  {
    String procedure = getResourceContent("ReplaceWorkgroup.sql");

    // Note: Oracle does not like carriage returns in statement, therefore drop
    // these!
    procedure = StringUtil.replace(procedure, "\r", "");

    Statement statement = connection.createStatement();
    try
    {
      statement.execute(procedure);
    }
    catch (SQLException ex)
    {
      // procedure already exists?
      if (ex.getErrorCode() != 955)
      {
        // a real problem -> rethrow
        throw ex;
      }
    }
    finally
    {
      connection.clearWarnings();
      statement.close();
    }
  }
  
  private int replaceWorkgroup(IDataTableRecord workgroupRecord, String newName) throws Exception
  {
    String datasourceName = workgroupRecord.getTable().getTableAlias().getTableDefinition().getDataSourceName();
    SQLDataSource sqlDataSource = (SQLDataSource) DataSource.get(datasourceName);
    Connection connection = sqlDataSource.getConnection();
    try
    {
      // prüfen, ob die Stored-Procedure schon in die Datenbank geladen wurde.
      // Achtung: Wird die Datasource zurückgesetzt bekommen wir auch eine
      //          neue Instanz von SQLDataSource zurück und damit erkennen
      //          wir das sich die Konfiguration geändert hat und wir möglicherweise
      //          auf eine andere physikalische Datenbank verweisen.
      //
      synchronized (ReplaceGroupButton.class)
      {
        if (procedureLoadedSQLDataSourceHashCode != sqlDataSource.hashCode())
        {
          loadProcedure(connection);

          procedureLoadedSQLDataSourceHashCode = sqlDataSource.hashCode();
        }
      }
      
      CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "replace_workgroup", 3);
      try
      {
        statement.setInt(1, workgroupRecord.getintValue("pkey"));
        statement.setString(2, newName);
        statement.registerOutParameter(3, java.sql.Types.INTEGER);
        statement.executeUpdate();
        return statement.getInt(3);
      }
      finally
      {
        statement.close();
      }
    }
    catch (SQLException ex)
    {
      connection.rollback();
      throw ex;
    }
    finally
    {
      connection.close();
    }
  }
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // Button darf nur für gültige AKs oder HWGs gedrückt werden
    //
    boolean enable = false;
    if (IGuiElement.SELECTED == status)
    {
      String groupstatus = context.getSelectedRecord().getStringValue("groupstatus");
      String wrkgrptype = context.getSelectedRecord().getStringValue("wrkgrptype");
      enable = "gültig".equals(groupstatus) && ("AK".equals(wrkgrptype) || "HWG".equals(wrkgrptype));
    }
    button.setEnable(enable);
  }
}

