package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Jun 20 16:59:35 CEST 2005
 *
 */
import java.util.Map;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.event.ui.call.CallGMZtask.AskCallback;

import org.apache.commons.logging.Log;



 /**
  * The Event handler for the CallBMStask-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallBMStask extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallBMStask.java,v 1.2 2005/09/01 11:59:20 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
    private static final String BMSKEY = "1";

    private static final String TASKCODE = "BMS";
    private static final String HWGKEY = "100544";
  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  public static class DialogCallback implements IFormDialogCallback
  {
      IDataTableRecord callRec = null;
      DialogCallback(IDataTableRecord selectedRecord)
      {
          this.callRec=selectedRecord;
      }
    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, java.lang.String, java.util.Map)
     */
    public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
    {

          IDataTableRecord callRec = this.callRec;
          String bmsCode = (String)formValues.get("bmsCode");
          if (bmsCode.length()== 0)
          {
              context.createMessageDialog("Es ist keine BMS-Aufragsnummer angegeben").show();
              return;
          }

          String description = (String)formValues.get("description");
          IDataTable taskTable = context.getDataTable("task");
          IDataTransaction trans = taskTable.startNewTransaction();
          try
          {
              IDataTableRecord taskRec = taskTable.newRecord(trans);
              taskRec.setValue(trans, "extsystem_id", (String)formValues.get("bmsCode"));
              taskRec.setValue(trans, "taskstatus", "In Arbeit");
              taskRec.setStringValueWithTruncation(trans, "summary", " BMS Auftragsnr. " + bmsCode+" : "+ description);
              taskRec.setValue(trans, "taskstatus", "In Arbeit");
              taskRec.setValue(trans, "ext_system_key", BMSKEY);
              taskRec.setValue(trans, "tasktype_key", TASKCODE);
              taskRec.setValue(trans, "calltask", callRec.getValue("pkey"));
              taskRec.setValue(trans, "workgrouptask", HWGKEY);
              trans.commit();
              // den datensatz in der GUI refreshen!
              IRelationSet myRelation = context.getApplicationDefinition().getRelationSet("r_task");

              context.getDataAccessor().propagateRecord(taskRec, myRelation, Filldirection.BOTH);
              context.setCurrentForm("UTtask");
          }
          finally
          {
              trans.close();
          }
      }
  }

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
//      IAskDialog dialog = context.createAskDialog(
//       "BMS-Aufträge werden nicht mehr automatisch erzeugt.\\nLegen Sie ihne bitte selbst an, und tragen Sie bitte hier die BMS-Auftragsnr. ein:", "0", new AskCallback());
      
      IDataTableRecord callRec = context.getSelectedRecord();
      
      // Constraints für den Dialog festlegen
      //
      CellConstraints cc= new CellConstraints();
      FormLayout  layout = new FormLayout("10dlu,10dlu,p,3dlu,300dlu,10dlu",  // columns
                                          "10dlu,p,3dlu,p,10dlu,p,20dlu");      // rows
      // Dialog samt Callback-Klasse anlegen
      //
      IFormDialog dialog=context.createFormDialog("BMS-Auftrag übernehmen",layout,new DialogCallback(callRec));
      dialog.addHeader("BMS-Auftrag für Meldung "+callRec.getSaveStringValue("pkey"),cc.xywh(1,1,4,1));
      dialog.addLabel("BMS-Auftrag:",cc.xy(2,3));
      dialog.addTextField("bmsCode","",cc.xy(4,3));
      dialog.addLabel("Beschreibung:",cc.xy(2,5));
      dialog.addTextField("description",callRec.getSaveStringValue("problem"),cc.xy(4,5));

      dialog.addSubmitButton("summit","Anlegen");

      dialog.show(200,100);
  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
           if (status == IGuiElement.SELECTED)
            {
                IDataTableRecord currentRecord = context.getSelectedRecord();
                String callstatus = currentRecord.getStringValue("callstatus");
                button.setEnable(callstatus.equals("Angenommen"));
            }	}
}

