/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jan 10 11:48:56 CET 2006
 */
package jacob.event.ui.call;

import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.CallAgent;
import jacob.model.CallOwner;
import jacob.model.CallProduct;
import jacob.model.CallWorkgroup;
import jacob.model.Category;
import jacob.model.Quickcall;
import jacob.model.Solution;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the CallQuickCall generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class CallQuickCall extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CallQuickCall.java,v 1.3 2006/01/23 14:24:59 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  class QuickcallCallback implements IFormDialogCallback
  {
    final Map name2templates;
    final IDataTableRecord callRecord;

    QuickcallCallback(IDataTableRecord callRecord, Map names2template)
    {
      this.name2templates = names2template;
      this.callRecord = callRecord;
    }



    /**
     * @param context
     * @param quickcallRecord
     * @throws Exception
     */
    private void applyQuickcall(IClientContext context, IDataTableRecord quickcallRecord) throws Exception
    {
      context.getGroup().clear(context, false); // do not clear foreign field
      IDataTable call = context.getDataTable(Call.NAME);
      IDataTransaction trans = call.startNewTransaction();

      IDataTableRecord callRecord = call.newRecord(trans);
      // setlinkedrecord für Agent
      IDataTable agent = context.getDataAccessor().getTable(CallAgent.NAME);
      agent.clear();
      agent.qbeClear();
      agent.qbeSetValue(CallAgent.pkey, context.getUser().getKey());
      agent.search();
      if (agent.recordCount() == 1)
        callRecord.setLinkedRecord(trans, agent.getRecord(0));
      if (quickcallRecord.hasLinkedRecord(CallOwner.NAME))
        callRecord.setLinkedRecord(trans, quickcallRecord.getLinkedRecord(CallOwner.NAME));
      if (quickcallRecord.hasLinkedRecord(CallProduct.NAME))
        callRecord.setLinkedRecord(trans, quickcallRecord.getLinkedRecord(CallProduct.NAME));
      callRecord.setValue(trans, Call.calltype, quickcallRecord.getValue(Quickcall.calltype));
      if (quickcallRecord.hasLinkedRecord(CallWorkgroup.NAME))
        callRecord.setLinkedRecord(trans, quickcallRecord.getLinkedRecord(CallWorkgroup.NAME));
      if (quickcallRecord.hasLinkedRecord(Category.NAME))
        callRecord.setLinkedRecord(trans, quickcallRecord.getLinkedRecord(Category.NAME));
      callRecord.setValue(trans, Call.problem, quickcallRecord.getValue(Quickcall.problem));
      callRecord.setValue(trans, Call.problemtext, quickcallRecord.getValue(Quickcall.problemtext));
      if (quickcallRecord.hasLinkedRecord(Solution.NAME))
        callRecord.setLinkedRecord(trans, quickcallRecord.getLinkedRecord(Solution.NAME));
      callRecord.setValue(trans, Call.solutiontext, quickcallRecord.getValue(Quickcall.solutiontext));

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
     *      java.lang.String, java.util.Map)
     */
    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      String userSelection = (String) values.get("template");

      IDataTableRecord quickcallRecord = (IDataTableRecord) name2templates.get(userSelection);
      applyQuickcall(context, quickcallRecord);

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
    IDataTableRecord callRecord = context.getSelectedRecord();

    // search all quickcalls in the database.
    //
    IDataTable quickcall = context.getDataAccessor().newAccessor().getTable(Quickcall.NAME);
    quickcall.clear();
    quickcall.search();
    if (quickcall.recordCount() == 0 )
    {
      context.createMessageDialog(ApplicationMessage.getLocalized("CallQuickCall.2")).show();
      return;
    }

    // retrieve all names of the templates and store them in a string array
    // for
    // the select dialog.
    //
    Map names2template = new HashMap();
    String[] names = new String[quickcall.recordCount()];
    for (int i = 0; i < quickcall.recordCount(); i++)
    {
      IDataTableRecord quickcallRecord = quickcall.getRecord(i);
      names[i] = quickcallRecord.getStringValue(Quickcall.problem);
      // store the correponding template in a map
      names2template.put(names[i], quickcallRecord);
    }

    // Create a FormDialog with a list of all available quickcall templates.
    //
    FormLayout layout = new FormLayout("10dlu, grow, 10dlu", // columns
        "10dlu,p,10dlu,grow,10dlu"); // rows
    CellConstraints cc = new CellConstraints();
    IFormDialog dialog = context.createFormDialog(ApplicationMessage.getLocalized("CallQuickCall.3"), layout, new QuickcallCallback(callRecord, names2template));
    dialog.addLabel(ApplicationMessage.getLocalized("CallQuickCall.4"), cc.xy(1, 1));
    dialog.addListBox("template", names, 0, cc.xy(1, 3));
    dialog.addSubmitButton("apply",ApplicationMessage.getLocalized("CallQuickCall.6") );
    // Show the dialog with a prefered size. The dialog trys to resize to the
    // optimum size!
    dialog.show(340, 300);
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
