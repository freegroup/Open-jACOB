/*
 * Created on 25.10.2004 by mike
 *  
 */
package jacob.event.ui.yan_task;

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

/**
 * Locks in qw_locks suchen und löschen.
 * 
 * @author Andreas Sonntag
 *  
 */
public class Yan_taskDeleteLock extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Yan_taskDeleteLock.java,v 1.1 2005/06/02 16:29:46 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

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
    FormLayout layout = new FormLayout("10dlu,p,10dlu,200dlu,10dlu", // 6 columns
                                       "20dlu,p,2dlu,p,20dlu"); // 7 rows

    IFormDialog dialog = context.createFormDialog("Locks suchen", layout, new LockSearchCallback());

    CellConstraints c = new CellConstraints();
    dialog.addLabel("PKey:", c.xy(1, 1));
    dialog.addTextField("pkey", "", c.xy(3, 1));
    dialog.addLabel("User:", c.xy(1, 3));
    dialog.addTextField("user", "", c.xy(3, 3));

    dialog.addSubmitButton("ok", "Suchen");
    dialog.setCancelButton("Abbrechen");
    dialog.show(300, 120);
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
