package jacob.common.gui.ext_system;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Aug 13 15:20:52 CEST 2007
 *
 */
import jacob.common.AppLogger;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CsvReader;
import jacob.event.screen.f_administration.wgroupmember.ReplaceGroupButton;
import jacob.model.Ext_system;
import jacob.model.Sap_first_objectimp;
import jacob.model.Sapadmin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * The Event handler for the ObjectImportSAP-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author achim
 * 
 */
public class ObjectImportSAP extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ObjectImportSAP.java,v 1.3 2007/08/20 17:47:11 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  
  private static int procedureLoadedSQLDataSourceHashCode = 0;
  
  private static String getResourceContent(String fileName) throws Exception
  {
    InputStream inputStream = ObjectImportSAP.class.getResourceAsStream(fileName);
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
    String procedure = getResourceContent("ObjectImportSAP.sql");

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
  
  /**
   * Stored procedure aufrufen, welche Objekt-Kopien für alle in der temporären Tabelle enthaltenen Objekt-IDs erzeugt und die alten EDVIN-Objekte auf gelöscht setzt.
   * 
   * @param edvinExtSystemRecord
   * @param sapExtSystemRecord
   * @return
   * @throws Exception
   */
  private static int performObjectImport(IDataTableRecord edvinExtSystemRecord, IDataTableRecord sapExtSystemRecord) throws Exception
  {
    String datasourceName = edvinExtSystemRecord.getTable().getTableAlias().getTableDefinition().getDataSourceName();
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
      
      CallableStatement statement = sqlDataSource.buildStoredProcedureStatement(connection, "sap_object_import", 3);
      try
      {
        statement.setInt(1, edvinExtSystemRecord.getintValue("pkey"));
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
   
  private static final class ReadCSVCallback implements IUploadDialogCallback
  {
    private final IDataTableRecord edvinExtSystemRecord;
    
    private ReadCSVCallback(IDataTableRecord edvinExtSystemRecord)
    {
      this.edvinExtSystemRecord = edvinExtSystemRecord;
    }

    public void onCancel(IClientContext arg0)
    {
      // ignore
    }

    public void onOk(IClientContext context, final String fileName, final byte[] fileData) throws Exception
    {
      // Daten von der CVS-Liste in die Austauschtabelle laden
      System.out.println("*** Job Objectimport Start CSV Isport at: " + (new Date()) + " ***");
      //
      IDataTableRecord sapExtSystemRecord;
      int csvEntryCount = 0;
      
      // attachment.setValue(trans, "document",fileData);
      InputStream is = new ByteArrayInputStream(fileData);
      Reader r = new InputStreamReader(is);
      char delimiter = ';';
      CsvReader reader = new CsvReader(r, delimiter);// "/Users/achim/Desktop/products.csv"
      try
      {
        reader.readHeaders();
        IDataAccessor acc = context.getDataAccessor().newAccessor();

        IDataTable sapadmin = acc.newAccessor().getTable(Sapadmin.NAME);
        sapadmin.qbeClear();
        sapadmin.qbeSetKeyValue(Sapadmin.active, "1");
        sapadmin.search();
        if (sapadmin.recordCount() == 1)
        {
          IDataTableRecord saprec = sapadmin.getRecord(0);
          //int prevcount = saprec.getintValue(Sapadmin.obj_preview_records);
          String sDefHeader = saprec.getSaveStringValue(Sapadmin.objectheader);
          sapExtSystemRecord = saprec.getLinkedRecord(Ext_system.NAME);
          StringBuffer checkheader = new StringBuffer();
          
          for (int i = 0; i < reader.getHeaderCount(); i++)
          {
            checkheader.append(reader.getHeader(i));
            if (i < reader.getHeaderCount() - 1)
            {
              checkheader.append(";");
            }
          }
          if (!checkheader.toString().equals(sDefHeader))
          {
            throw new UserException("Die Definition des Objektimports stimmt nicht mit der Importquelle überein");
          }
        }
        else
        {
          throw new UserException("Kein eindeutiger aktiver SAP-Verbindungsdatensatz, Import nicht möglich");
        }

        IDataTransaction transaction = acc.newTransaction();
        try
        {
          // Temporäre Tabelle löschen
          //
          IDataTable tmp_object = context.getDataTable(Sap_first_objectimp.NAME);
          tmp_object.qbeClear();
          tmp_object.fastDelete(transaction);

          while (reader.readRecord())
          {
            // SAP Bezeichnung;Edvin Objekt-Ident-Nr

            // buf.append("\\r\n");
            csvEntryCount++;
            IDataTableRecord tmp = tmp_object.newRecord(transaction);
            //TODO Header variabel machen
            tmp.setValue(transaction, Sap_first_objectimp.sap_object, reader.get(reader.getHeader(0)));
            tmp.setValue(transaction, Sap_first_objectimp.edvin_object, reader.get(reader.getHeader(1)));


          }
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }
        
        if (csvEntryCount == 0)
        {
          context.createMessageDialog("Die CVS-Liste enthält keine Einträge").show();
          return;
        }
      }
      finally
      {
        reader.close();
      }
      System.out.println("*** Job Objectimport Start Cloning the Objects at: " + (new Date()) + " ***");

      // und eigentliche Aktion ausführen
      //
      String message;
      String sapSystemName = sapExtSystemRecord.getStringValue(Ext_system.name);
      int newObjectCount = performObjectImport(edvinExtSystemRecord, sapExtSystemRecord);
      if (csvEntryCount == newObjectCount)
        message = "Es wurden " + newObjectCount + " Objektdatensätze für SAP-System '" + sapSystemName + "' angelegt";
      else
        message = "Es wurden " + newObjectCount + " Objektdatensätze für SAP-System '" + sapSystemName + "' angelegt, aber " + csvEntryCount
                + " Einträge sind in der CVS-Liste vorhanden";
      
      logger.info(message);
      context.createMessageDialog(message).show();
      System.out.println("*** Job Objectimport endet at: " + (new Date()) + " ***");
    }
  }

  /**
   * The user has been click on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord edvinExtSystemRecord = context.getSelectedRecord();

    IDialog dialog;
    dialog = context.createUploadDialog(new ReadCSVCallback(edvinExtSystemRecord));
    dialog.show();
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    if (!status.equals(IGuiElement.SEARCH))
    {
      IDataTableRecord rec = context.getSelectedRecord();
      if (rec.getSaveStringValue(Ext_system.systemtype).equals(Ext_system.systemtype_ENUM._EDVIN))
      {
        button.setEnable(true);
      }
      else
      {
        button.setEnable(false);
      }
    }
    else
    {
      button.setEnable(false);
    }
  }
}
