/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 22 16:24:52 CEST 2007
 */
package jacob.common.gui.call;

import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Callworkgroup;
import jacob.model.Category;
import jacob.model.Customerint;
import jacob.model.Location;
import jacob.model.Process;
import jacob.model.Quickcalls;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the Quickcall generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Quickcall extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Quickcall.java,v 1.3 2007/12/18 14:17:18 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();
  class DialogCallback implements IFormDialogCallback
  {
    final Map name2templates;

    DialogCallback(Map names2template)
    {
      this.name2templates = names2template;

    }

    /*
     * Generate Activity from Quickcall
     * 
     */
    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      String userSelection = (String) values.get("template");
      IDataTableRecord quickrecord = (IDataTableRecord) name2templates.get(userSelection);
      if (quickrecord == null)
      {
        context.createMessageDialog("Vorlage nicht gefunden").show();
        return;
      }

      // Create new activity record
      IDataTable callTable = context.getDataTable(Call.NAME);
      IDataTable custTable = context.getDataTable("customerint");
      IDataTableRecord custint = custTable.getSelectedRecord();
      IDataTransaction transaction = callTable.startNewTransaction();
      IDataTableRecord callrecord = callTable.newRecord(transaction);

      // Set Values from Quickcall
      callrecord.setValue(transaction, Call.problem, quickrecord.getSaveStringValue(Quickcalls.problem));
      callrecord.setValue(transaction, Call.problemtext, quickrecord.getSaveStringValue(Quickcalls.problemtext));
      callrecord.setValue(transaction, Call.origin, quickrecord.getValue(Quickcalls.origin));
      callrecord.setValue(transaction, Call.callbackmethod, quickrecord.getValue(Quickcalls.callbackmethod));

      // set linked records
      if (custint != null)
      {
        callrecord.setLinkedRecord(transaction, custint);
        // Betroffene Person setzen
        String aperson = custint.getSaveStringValue(Customerint.fullname) + " Tel.: " + custint.getSaveStringValue(Customerint.phonecorr);
        callrecord.setValue(transaction, Call.affectedperson, aperson);
      }

      if (quickrecord.hasLinkedRecord(Callworkgroup.NAME))
      {
        // System.out.println("Linked type");
        IDataTableRecord ak = quickrecord.getLinkedRecord(Callworkgroup.NAME);
        callrecord.setLinkedRecord(transaction, ak);
      }
      if (quickrecord.hasLinkedRecord(Process.NAME))
      {
        // System.out.println("Linked type");
        IDataTableRecord process = quickrecord.getLinkedRecord(Process.NAME);
        callrecord.setLinkedRecord(transaction, process);
      }
      if (quickrecord.hasLinkedRecord(Category.NAME))
      {

        IDataTableRecord Cat = quickrecord.getLinkedRecord(Category.NAME);
        callrecord.setLinkedRecord(transaction, Cat);
      }
      if (quickrecord.hasLinkedRecord(jacob.model.Object.NAME))
      {

        IDataTableRecord obj = quickrecord.getLinkedRecord(jacob.model.Object.NAME);
        callrecord.setLinkedRecord(transaction, obj);
      }

      if (quickrecord.hasLinkedRecord(Location.NAME))
      {

        IDataTableRecord qloc = quickrecord.getLinkedRecord(Location.NAME);
        IDataAccessor acc = context.getDataAccessor();
        IDataTransaction tr2 = acc.newTransaction();
        try
        {

          IDataTableRecord loc = acc.cloneRecord(tr2, qloc);
          tr2.commit();
          callrecord.setLinkedRecord(transaction, loc);

          acc.propagateRecord(loc, Filldirection.BACKWARD);
        }
        finally
        {
          tr2.close();
        }

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

    // search all document templates in the database. It is possible to render
    // the
    // call with different type of xsl-stylesheets.
    //
    IDataTable table = context.getDataTable(Quickcalls.NAME);
    table.clear();
    table.search();

    if (table.recordCount() > 0)
    {
      // retrieve all names of the templates and store them in a string array
      // for
      // the select dialog.
      //
      Map names2template = new HashMap();
      String[] names = new String[table.recordCount()];
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord docRecord = table.getRecord(i);
        names[i] = docRecord.getStringValue(Quickcalls.name);
        // store the correponding template in a map
        names2template.put(docRecord.getStringValue(Quickcalls.name), docRecord);
      }

      // Create a FormDialog with a list of all available doc templates.
      //
      FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns
          // //$NON-NLS-1$
          "10dlu,p,10dlu,grow,10dlu"); // rows //$NON-NLS-1$
      CellConstraints cc = new CellConstraints();
      IFormDialog dialog = context.createFormDialog("Vorlagenauswahl", layout, new DialogCallback(names2template));
      dialog.addLabel("Vorlagenauswahl", cc.xy(1, 1));
      dialog.addListBox("template", names, 0, cc.xy(1, 3)); //$NON-NLS-1$
      dialog.addSubmitButton("show", "Anwenden");
      // Show the dialog with a prefered size. The dialog trys to resize to the
      // optimum size!
      dialog.show(300, 250);
    }
    else
    {
      // no document template in the system found. make a user notification.
      //
      IMessageDialog dialog = context.createMessageDialog(ApplicationMessage.getLocalized("ActivityQuickActivity.42"));
      dialog.show();
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
