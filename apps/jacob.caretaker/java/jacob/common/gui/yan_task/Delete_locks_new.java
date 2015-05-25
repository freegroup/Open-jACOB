/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 08 14:24:46 CEST 2008
 */
package jacob.common.gui.yan_task;

import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;


import org.apache.commons.logging.Log;


/**
 * The event handler for the Delete_locks_new generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Delete_locks_new extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: Delete_locks_new.java,v 1.2 2008/07/24 13:16:31 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private static class LockSearchCallback implements IFormDialogCallback
  {
    private static class LockDeleteCallback implements IFormDialogCallback
    {
      private final IDataTable lockTable;

      private LockDeleteCallback(IDataTable lockTable)
      {
        this.lockTable = lockTable;
      }

      /*
       * Selektierte Vorlage an einen EntryPoint übergeben welcher dann das RTF Dokument generiert.
       *  
       */
      public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
      {
        if ("delete".equals(buttonId))
        {
          int index = -1;
          try
          {
            index = Integer.parseInt((String) values.get("locksIndex"));
          }
          catch (Exception ex)
          {
            // ignore
          }
          
          if (index == -1)
          {
            context.createMessageDialog("Kein Lock ausgewählt. Löschen ist somit nicht möglich").show();
          }
          else
          {
            // and delete the lock record
            IDataTableRecord lockRecord = lockTable.getRecord(index);
            IDataTransaction transaction = context.getDataAccessor().newTransaction();
            try
            {
              lockRecord.delete(transaction);
              transaction.commit();
            }
            finally
            {
              transaction.close();
            }
          }
        }
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
     *      java.lang.String, java.util.Map)
     */
    public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
    {
      String pkey = (String) formValues.get("pkey");
      String user = (String) formValues.get("user");

      // Achtung: Wir benötigen hier unseren eigenen Datenaccessor, damit uns niemand dazwischenfunkt, bzw.
      // damit die Sache auch funktioniert sofern der Benutzer auf die Idee kommt mehrere solche Dialogfenster
      // zu öffnen.
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      IDataTable table = accessor.getTable("qw_locks");
      if (pkey != null && pkey.trim().length() > 0)
        table.qbeSetValue("keyvalue", pkey.trim());
      if (user != null && user.trim().length() > 0)
        table.qbeSetValue("username", user.trim());
      table.search();

      if (table.recordCount() > 0)
      {
        // retrieve all locks.
        //
        String[] entries = new String[table.recordCount()];
        for (int i = 0; i < table.recordCount(); i++)
        {
          IDataTableRecord lockRecord = table.getRecord(i);
          // FREEGROUP: Achtung: Es dürfen nicht mehrere Leerzeichen hintereinander in einem Entrystring
          // vorhanden sein, da FormLayout diese durch einfache Leerzeichen zu ersetzen scheint und dann
          // der Eintrag über den HashMap-Eintrag nicht mehr gefunden werden kann.
          entries[i] = lockRecord.getStringValue("tablename") + " ID:" + lockRecord.getStringValue("keyvalue") + " gelockt von " + lockRecord.getStringValue("username");
        }

        // Create a FormDialog with a list of all available locks.
        //
        FormLayout layout = new FormLayout("10dlu, 300dlu,10dlu", // columns
                                           "10dlu,p,10dlu,250dlu,10dlu"); // rows
        CellConstraints cc = new CellConstraints();
        IFormDialog dialog = context.createFormDialog("Locks löschen", layout, new LockDeleteCallback(table));
        dialog.addLabel("Lock auswählen", cc.xy(1, 1));
        dialog.addListBox("locks", entries, 0, cc.xy(1, 3));
        dialog.addSubmitButton("delete", "Löschen");

        // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
        dialog.show(400, 500);
      }
      else
      {
        context.createMessageDialog("nicht möglich - keine Locks gefunden").show();
      }
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    alert("da sind wir");
/*    FormLayout layout = new FormLayout("10dlu,p,10dlu,200dlu,10dlu", // 6 columns
                                       "20dlu,p,2dlu,p,20dlu"); // 7 rows

    IFormDialog dialog = context.createFormDialog("Locks suchen", layout, new LockSearchCallback());

    CellConstraints c = new CellConstraints();
    dialog.addLabel("PKey:", c.xy(1, 1));
    dialog.addTextField("pkey", "", c.xy(3, 1));
    dialog.addLabel("User:", c.xy(1, 3));
    dialog.addTextField("user", "", c.xy(3, 3));

    dialog.addSubmitButton("ok", "Suchen");
    dialog.setCancelButton("Abbrechen");
    dialog.show(300, 120);*/
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {
    // nothing to do here
  }
}
