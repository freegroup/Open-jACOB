/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Dec 14 11:25:13 CET 2007
 */
package jacob.common.gui.workgroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Hwgtasktype;
import jacob.model.Sapadmin;
import jacob.model.Tasktype;
import jacob.model.Workgroup;
import jacob.model.Tasktype.tasktypestatus_ENUM;

import org.apache.commons.logging.Log;

/**
 * The event handler for the Mandanten generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Mandanten extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Mandanten.java,v 1.1 2007/12/18 14:17:18 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();
  private class GridCallback implements IGridTableDialogCallback
  {
    private final IDataBrowser hwg_browser;
    private final IDataTable tasktype;
    private final String extsystem;

    private GridCallback(IDataBrowser hwg_browser, IDataTable tasktype, String extsystem)
    {
      this.hwg_browser = hwg_browser;
      this.tasktype = tasktype;
      this.extsystem = extsystem;

    }

    /*
     * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext,
     *      int, java.util.Properties)
     */
    public void onSelect(IClientContext context, int rowIndex, Properties columns) throws Exception
    {

      
      
      
      final IDataTableRecord tasktypeRecord = this.tasktype.getRecord(rowIndex);
      String OKmes = "Für die Datensätze im Suchbrowser \\r\\n" +
      "werden ManantenInfo Datensätze mit: \\r\\n" +
      "Auftragsart: " + tasktypeRecord.getSaveStringValue(Tasktype.description) +
      "\\r\\nund externem System: " + extsystem +
      "\\r\\nerstellt.  " +
      " \\r\\nFortfahren?  ";
  context.createOkCancelDialog(OKmes, new IOkCancelDialogCallback()
  {
  
    public void onOk(IClientContext context) throws Exception
    {
      String sTsakType = tasktypeRecord.getSaveStringValue(Tasktype.taskcode);
      IDataTransaction transaction = context.getDataAccessor().newTransaction();
      IDataTable mandant = context.getDataAccessor().getTable(Hwgtasktype.NAME);
      
      try
      {
        // Erstellen der MandantenInfo Sätze
        //
        int j=0;
        for (int i = 0; i < hwg_browser.recordCount(); i++) 
        {
          IDataTableRecord hwgrec = hwg_browser.getRecord(i).getTableRecord();
          //Prüfen, ob HWG, sonst nichts tun
          if (hwgrec.getSaveStringValue(Workgroup.wrkgrptype).equals(Workgroup.wrkgrptype_ENUM._HWG) && hwgrec.getSaveStringValue(Workgroup.groupstatus).equals(Workgroup.groupstatus_ENUM._gueltig))
          {
            //jetzt noch püfen ob schon Info besteht
            mandant.qbeClear();
            mandant.qbeSetValue(Hwgtasktype.ext_system_key, extsystem);
            mandant.qbeSetValue(Hwgtasktype.tasktype_key, sTsakType);
            mandant.qbeSetValue(Hwgtasktype.taskworkgroup_key, hwgrec.getSaveStringValue(Workgroup.pkey));
            mandant.search();
            if (mandant.recordCount()==0)
            {
              IDataTableRecord mandantrec = mandant.newRecord(transaction);
              mandantrec.setValue(transaction, Hwgtasktype.ext_system_key, extsystem);
              mandantrec.setValue(transaction, Hwgtasktype.tasktype_key, sTsakType);
              mandantrec.setValue(transaction, Hwgtasktype.taskworkgroup_key, hwgrec.getSaveStringValue(Workgroup.pkey));
              j++;
            }

          }
        }

        // commit the transaction
        //
        transaction.commit();

        alert("Es wurden " + j + " ManantenInfo Datensätze erzeugt." );
        // show created document to the user
        // context.createDocumentDialog(document).show();
      }
      finally
      {
        transaction.close();
      }
  
    }
  
    public void onCancel(IClientContext context) throws Exception
    {
      // TODO Auto-generated method stub
  
    }
  
  }).show();


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
    // zuerst gültige Auftragsarten suchen

    // Note: Create new accessor because this ensures, that the resulting
    // records are not cleared by
    // the main window, if the user keeps the dialog open for a while.
    IDataTable taskType = context.getDataAccessor().newAccessor().getTable(Tasktype.NAME);
    taskType.qbeClear();
    taskType.qbeSetValue(Tasktype.tasktypestatus, tasktypestatus_ENUM._gueltig);
    taskType.search();
    // Import braucht ext.system
    final IDataTable oSapAdm = context.getDataAccessor().newAccessor().getTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    String extSystemObj = "";
    if (oSapAdm.recordCount() == 1)
    {
      extSystemObj = oSapAdm.getRecord(0).getSaveStringValue(Sapadmin.ext_system_key);
      
    }
    // Wenn wir ds Externe System und Auftragsarten haben
    if (taskType.recordCount() > 0 && oSapAdm.recordCount() == 1)
    {
      IGridTableDialog dialog = context.createGridTableDialog(button, new GridCallback(context.getDataBrowser("workgroupBrowser"), taskType, extSystemObj));

      // create the header for the selection grid dialog
      //
      String[] header = new String[]
        { "Code", "Auftragsart" };
      dialog.setHeader(header);

      String[][] daten = new String[taskType.recordCount()][2];
      for (int j = 0; j < taskType.recordCount(); j++)
      {
        IDataTableRecord taskTypeRecord = taskType.getRecord(j);
        daten[j] = new String[]
          { taskTypeRecord.getSaveStringValue(Tasktype.taskcode), taskTypeRecord.getSaveStringValue(Tasktype.description) };
      }
      dialog.setData(daten);
      dialog.show(300, 100);
    }
    else
    {
      String sMessage = "";
      if (taskType.recordCount() == 0)
      {
        sMessage = "Keine Gültigen Auftragsarten vorhanden. MandatenInfo kann nicht erstellt werden. \\r\\nUnter Stammdaten - Auftragsart können Auftragsarten bearbeitet werden.";

      }
      else
      {
        sMessage = "Kein Externes System vorhanden. MandatenInfo kann nicht erstellt werden. \\r\\nUnter SAP-Admin - SAP Admin kann das externe System zugewiesen werden.";

      }

      alert(sMessage);
    }

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
