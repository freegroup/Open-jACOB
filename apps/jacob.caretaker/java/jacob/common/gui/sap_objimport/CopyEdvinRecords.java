/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Nov 19 12:23:39 CET 2007
 */
package jacob.common.gui.sap_objimport;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;
import jacob.common.AppLogger;
import jacob.common.gui.ext_system.ObjectImportSAP;
import jacob.event.screen.f_administration.wgroupmember.ReplaceGroupButton;
import jacob.model.Ext_system;
import jacob.model.Sapadmin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;


/**
 * The event handler for the CopyEdvinRecords generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class CopyEdvinRecords extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CopyEdvinRecords.java,v 1.1 2007/12/18 14:17:17 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
  private static int procedureLoadedSQLDataSourceHashCode = 0;
  
  private static String getResourceContent(String fileName) throws Exception
  {
    InputStream inputStream = CopyEdvinRecords.class.getResourceAsStream(fileName);
    if (null == inputStream)
    {
      throw new Exception("Could not open file '" + fileName + "' as stream");
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
  private static void loadProcedure(Connection connection) throws Exception
  {
    String procedure = getResourceContent("CopyEdvinRecords.sql");

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
  
  
  

  private static int performObjectImport( IDataTableRecord sapExtSystemRecord) throws Exception
  {
    String datasourceName = sapExtSystemRecord.getTable().getTableAlias().getTableDefinition().getDataSourceName();
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
      
      CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "sap_object_import_cp", 3);
      try
      {
        
        statement.setInt(1, 1);
        statement.setInt(2, sapExtSystemRecord.getintValue("pkey"));
        statement.registerOutParameter(3, java.sql.Types.INTEGER);
        statement.executeUpdate();
        int res = statement.getInt(3);
        connection.commit();
        return res;
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
	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    //Neuen Systemrec holen
    IDataTableRecord sapExtSystemRecord;
    IDataAccessor acc = context.getDataAccessor().newAccessor();

    IDataTable sapadmin = acc.newAccessor().getTable(Sapadmin.NAME);
    sapadmin.qbeClear();
    sapadmin.qbeSetKeyValue(Sapadmin.active, "1");
    sapadmin.search();
    if (sapadmin.recordCount() == 1)
    {
      IDataTableRecord saprec = sapadmin.getRecord(0);
      sapExtSystemRecord = saprec.getLinkedRecord(Ext_system.NAME);

    }
    else {
      throw new UserException("Aktiver SAP-Admindatensatz nicht gefunden");
    }
    
    int csvEntryCount = 0;
    
    
    
    System.out.println("*** Job Objectimport Start Cloning the Objects at: " + (new Date()) + " ***");

    // und eigentliche Aktion ausführen
    //
    String message;
    String sapSystemName = sapExtSystemRecord.getStringValue(Ext_system.name);
    int newObjectCount = performObjectImport(sapExtSystemRecord);
    if (csvEntryCount == newObjectCount)
      message = "Es wurden " + newObjectCount + " Objektdatensätze für SAP-System '" + sapSystemName + "' angelegt";
    else
      message = "Es wurden " + newObjectCount + " Objektdatensätze für SAP-System '" + sapSystemName + "' angelegt, aber " + csvEntryCount
              + " Einträge sind in der CVS-Liste vorhanden";
    
    logger.info(message);
    context.createMessageDialog(message).show();
    System.out.println("*** Job Objectimport endet at: " + (new Date()) + " ***");
		
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}
