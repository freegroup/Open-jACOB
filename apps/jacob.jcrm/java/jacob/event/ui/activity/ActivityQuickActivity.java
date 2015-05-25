package jacob.event.ui.activity;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 06 14:50:16 CEST 2005
 *
 */
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
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
 * The Event handler for the ActivityQuickActivity-Button.<br>
 * The onAction will be called, if the user clicks on this button.<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class ActivityQuickActivity extends IButtonEventHandler
{
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
        context.createMessageDialog(ApplicationMessage.getLocalized("ActivityQuickActivity.3")).show();
        return;
      }

      //    Create new activity record      
      IDataTable activityTable = context.getDataTable("activity");
      IDataTransaction transaction = activityTable.startNewTransaction();
      IDataTableRecord activityrecord = activityTable.newRecord(transaction);
      
      //    Set Values from Quickcall
      activityrecord.setValue(transaction, "description", quickrecord.getSaveStringValue("description"));
      activityrecord.setValue(transaction, "importance", quickrecord.getSaveStringValue("importance"));
      activityrecord.setValue(transaction, "status", quickrecord.getSaveStringValue("activitystatus"));

      //    set linked records

      if (quickrecord.hasLinkedRecord("activitytype"))
      {
        //          System.out.println("Linked type");
        IDataTableRecord activitytype = quickrecord.getLinkedRecord("activitytype");
        activityrecord.setLinkedRecord(transaction, activitytype);
      }
      if (quickrecord.hasLinkedRecord("workgroup"))
      {
        IDataTableRecord workgroup = quickrecord.getLinkedRecord("workgroup");
        activityrecord.setLinkedRecord(transaction, context.getDataTable("activityWorkgroup").loadRecord(workgroup.getPrimaryKeyValue()));
      }
      //    Set Longtext Fields
      if (!quickrecord.hasNullValue("notes"))
      {
        activityrecord.setValue(transaction, "notes", quickrecord.getSaveStringValue("notes"));

      }

      IDataTableRecord activityContact = context.getDataTable("activityContact").getSelectedRecord();
      if (activityContact != null)
      {
        activityrecord.setLinkedRecord(transaction, activityContact);
      }
      IDataTableRecord salesproject = context.getDataTable("salesproject").getSelectedRecord();
      if (salesproject != null)
      {
        activityrecord.setLinkedRecord(transaction, salesproject);
      }
      //    And now the Dates

      Calendar planstart = Calendar.getInstance();
      planstart.add(Calendar.YEAR, quickrecord.getintValue("s_years"));
      planstart.add(Calendar.MONTH, quickrecord.getintValue("s_months"));
      planstart.add(Calendar.DATE, quickrecord.getintValue("s_days"));
      planstart.add(Calendar.HOUR, quickrecord.getintValue("s_hours"));
      planstart.add(Calendar.MINUTE, quickrecord.getintValue("s_minutes"));
      activityrecord.setDateValue(transaction, "plan_start", planstart.getTime());

      Calendar plandone = planstart;
      plandone.add(Calendar.YEAR, quickrecord.getintValue("d_years"));
      plandone.add(Calendar.MONTH, quickrecord.getintValue("d_months"));
      plandone.add(Calendar.DATE, quickrecord.getintValue("d_days"));
      plandone.add(Calendar.HOUR, quickrecord.getintValue("d_hours"));
      plandone.add(Calendar.MINUTE, quickrecord.getintValue("d_minutes"));
      activityrecord.setDateValue(transaction, "plan_done", plandone.getTime());
    }
  }

  /**
   * The user has been click on the corresponding button.<br>
   * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
   *             The selected flag warranted that the event can only be fired if the<br>
   *             selectedRecord!=null.<br>
   *
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // search all document templates in the database. It is possible to render the 
    // call with different type of xsl-stylesheets.
    //
    IDataTable table = context.getDataTable("activitytemplate");
    table.clear();
    table.search();

    if (table.recordCount() > 0)
    {
      // retrieve all names of the templates and store them in a string array for
      // the select dialog.
      //
      Map names2template = new HashMap();
      String[] names = new String[table.recordCount()];
      for (int i = 0; i < table.recordCount(); i++)
      {
        IDataTableRecord docRecord = table.getRecord(i);
        names[i] = docRecord.getStringValue("name");
        // store the correponding template in a map 
        names2template.put(docRecord.getStringValue("name"), docRecord);
      }

      // Create a FormDialog with a list of all available doc templates.
      //
      FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns //$NON-NLS-1$
          "10dlu,p,10dlu,grow,10dlu"); // rows //$NON-NLS-1$
      CellConstraints cc = new CellConstraints();
      IFormDialog dialog = context.createFormDialog(ApplicationMessage.getLocalized("ActivityQuickActivity.37"), layout, new DialogCallback(names2template));
      dialog.addLabel(ApplicationMessage.getLocalized("ActivityQuickActivity.38"), cc.xy(1, 1));
      dialog.addListBox("template", names, 0, cc.xy(1, 3)); //$NON-NLS-1$
      dialog.addSubmitButton("show", ApplicationMessage.getLocalized("ActivityQuickActivity.41"));
      // Show the dialog with a prefered size. The dialog trys to resize to the optimum size!
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

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
