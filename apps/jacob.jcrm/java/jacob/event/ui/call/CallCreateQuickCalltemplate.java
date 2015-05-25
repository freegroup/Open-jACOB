/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 23 15:11:26 CET 2006
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Quickcall;

import org.apache.commons.logging.Log;


/**
 * The event handler for the CallCreateQuickCalltemplate record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class CallCreateQuickCalltemplate extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallCreateQuickCalltemplate.java,v 1.1 2006/01/23 14:25:00 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
  /**
   * @param context
   * @param callRecord
   * @throws Exception
   */
  private void createQuickcall(IClientContext context, IDataTableRecord callRecord) throws Exception
  {
    IDataTable quickcall = context.getDataTable(Quickcall.NAME);
    IDataTransaction trans = quickcall.startNewTransaction();
    try
    {
      IDataTableRecord qcRecord = quickcall.newRecord(trans);
      qcRecord.setValue(trans, Quickcall.callowner_key, callRecord.getValue(Call.callowner_key));
      qcRecord.setValue(trans, Quickcall.callproduct_key, callRecord.getValue(Call.callproduct_key));
      qcRecord.setValue(trans, Quickcall.calltype, callRecord.getValue(Call.calltype));
      qcRecord.setValue(trans, Quickcall.callworkgroup_key, callRecord.getValue(Call.callworkgroup_key));
      qcRecord.setValue(trans, Quickcall.category_key, callRecord.getValue(Call.category_key));
      qcRecord.setValue(trans, Quickcall.problem, callRecord.getValue(Call.problem));
      qcRecord.setValue(trans, Quickcall.problemtext, callRecord.getValue(Call.problemtext));
      qcRecord.setValue(trans, Quickcall.solution_key, callRecord.getValue(Call.solution_key));
      qcRecord.setValue(trans, Quickcall.solutiontext, callRecord.getValue(Call.solutiontext));
      trans.commit();
    }
    finally
    {
      trans.close();
    }
    context.createMessageDialog(ApplicationMessage.getLocalized("CallCreateQuickCalltemplate.1")).show();
  }
	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
    createQuickcall(context,currentRecord);

	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}

