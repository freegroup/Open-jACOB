/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jun 18 17:48:48 CEST 2007
 */
package jacob.common.gui.object;

import jacob.common.AppLogger;
import jacob.common.sap.CsvReader;
import jacob.model.Accountcode;
import jacob.model.Object;
import jacob.model.Sapadmin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the ObjectGenericButton2 generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class ObjectGenericButton2 extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ObjectGenericButton2.java,v 1.3 2007/08/13 14:54:04 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  static class ReadCSVCallback implements IUploadDialogCallback
  {

    IDataTableRecord record;
    String attType;

    public ReadCSVCallback()
    {

    }

    /*
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext arg0)
    {
      // ignore
    }

    /*
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onOk(de.tif.jacob.screen.IClientContext,
     *      java.lang.String, byte[])
     */
    public void onOk(IClientContext context, final String fileName, final byte[] fileData) throws Exception
    {

      // attachment.setValue(trans, "document",fileData);
      InputStream is = new ByteArrayInputStream(fileData);
      Reader r = new InputStreamReader(is);
      char delimiter = ';';
      CsvReader reader = new CsvReader(r, delimiter);// "/Users/achim/Desktop/products.csv"
      reader.readHeaders();
      IDataTable object = context.getDataTable(Object.NAME);
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      IDataTransaction transaction = acc.newTransaction();
      IDataTable accountcode = acc.getTable(Accountcode.NAME);

      try
      {
        IDataTable sapadmin = object.getAccessor().newAccessor().getTable(Sapadmin.NAME);
        String extsystemkey = "";
        sapadmin.qbeClear();
        sapadmin.qbeSetKeyValue(Sapadmin.active, "1");
        sapadmin.search();
        int prevcount = 25;
        String sDefHeader = "";
        if (sapadmin.recordCount() == 1)

        {
          IDataTableRecord saprec = sapadmin.getRecord(0);
          extsystemkey = saprec.getSaveStringValue(Sapadmin.ext_system_key);
          prevcount = saprec.getintValue(Sapadmin.obj_preview_records);
          sDefHeader = saprec.getSaveStringValue(Sapadmin.objectheader);
          // System.out.println("col:"+reader.getColumnCount());
          // System.out.println("Head:"+reader.getHeaderCount());
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
          throw new UserException("Kein eindeutiger aktiver Verbindungsdatensatz, Import nicht möglich");
        }
        while (reader.readRecord())

        {

          // Object;Typ;Werk;Beschreibung;HWG;Kostenstelle

          // buf.append("\\r\n");

          IDataTableRecord objrec = object.newRecord(transaction);

          objrec.setValue(transaction, Object.external_id, reader.get("Object"));
          objrec.setValue(transaction, Object.ext_system_key, extsystemkey);
          objrec.setValue(transaction, Object.name, reader.get("Beschreibung"));
          objrec.setValue(transaction, Object.objstatus, Object.objstatus_ENUM._in_Betrieb);
          // Kostenstelle setzen
          accountcode.qbeClear();
          accountcode.qbeSetKeyValue(Accountcode.code, reader.get("Kostenstelle"));
          accountcode.search();
          if (accountcode.recordCount() == 1)
          {
            objrec.setValue(transaction, Object.accountingcode_key, reader.get("Kostenstelle"));
          }

        }
        // TODO In Endversion jeden Datensat in eine transaktion
        transaction.commit();
      }
      catch (Exception e)
      {
        throw new UserException(e.getMessage());
      }
      finally
      {
        transaction.close();
      }

      reader.close();
    }
  }

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDialog dialog;
    dialog = context.createUploadDialog(new ReadCSVCallback());
    dialog.show();

  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
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
    // You can enable/disable the button in relation to your conditions.
    //
    // button.setEnable(true/false);
  }
}
