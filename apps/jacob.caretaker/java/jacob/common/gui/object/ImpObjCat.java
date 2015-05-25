/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 11 12:38:44 CEST 2008
 */
package jacob.common.gui.object;

import java.awt.Dialog;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.messaging.alert.AlertManager;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.sap.CsvReader;
import jacob.model.Category;
import jacob.model.Object;
import jacob.model.Sap_first_objectimp;

import org.apache.commons.logging.Log;

/**
 * The event handler for the ImpObjCat generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class ImpObjCat extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ImpObjCat.java,v 1.1 2008/06/27 14:57:06 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();
  private static final class ReadCSVCallback implements IUploadDialogCallback
  {
    public void onCancel(IClientContext arg0)
    {
      // ignore
    }

    public void onOk(IClientContext context, final String fileName, final byte[] fileData) throws Exception
    {
      
      StringBuffer buf = new StringBuffer();
      InputStream is = new ByteArrayInputStream(fileData);
      Reader r = new InputStreamReader(is);
      char delimiter = ';';
      CsvReader reader = new CsvReader(r, delimiter);// "/Users/achim/Desktop/products.csv"
      String errormes ="";
      int iInput = 0;
      int iOutput = 0;
      reader.readHeaders();
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      IDataTable object = acc.getTable(Object.NAME);
      IDataTransaction transaction = acc.newTransaction();
      try
      {
        while (reader.readRecord())
        {
          
          iInput++;
          String objextid = reader.get(reader.getHeader(0));
          String objcat = reader.get(reader.getHeader(1));
          errormes =  "Fehler in Zeile:" + iInput + " Objekt: " + objextid + "Kategorie ID: " + objcat + "Systemfehler: ";
          object.qbeClear();
          object.qbeSetKeyValue(Object.external_id, objextid);
          object.search();
          if (object.recordCount() == 1)
          {
            IDataTable cat = acc.getTable(Category.NAME);
            cat.qbeClear();
            cat.qbeSetKeyValue(Category.pkey, objcat);
            cat.qbeSetValue(Category.categorystatus, Category.categorystatus_ENUM._Gueltig);
            cat.qbeSetValue(Category.categorystatus, Category.categorystatus_ENUM._Keine_Zuordnung);
            cat.search();
            if (cat.recordCount() == 1)
            {
              object.getRecord(0).setValue(transaction, Object.objectcategory_key, objcat);
              iOutput++;
            }
            else 
            {
              buf.append("\nKategorie nicht gefuden: " + objcat);
            }

          }
          else 
          {
            buf.append("\nObjekt nicht gefuden: " + objextid);
          }
        }
        transaction.commit();
        System.out.println(iInput + "Datensätze verarbeitet, "+ iOutput + "Gewerke zugeordnet" + "\n"+buf.toString());
        
        String emes = iInput + "Datensätze verarbeitet, "+ iOutput + "Gewerke zugeordnet" + "\n"+buf.toString();
        IMessageDialog d = context.createMessageDialog(emes);
        d.show();
      }
      catch (Exception e) {
        context.createMessageDialog(errormes + e.getMessage()).show();
      }
      finally
      {
        transaction.close();
      }

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
