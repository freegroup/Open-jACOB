/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 11:40:40 CET 2009
 */
package jacob.event.ui.emailqueuedetails;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

import jacob.common.AppLogger;
import jacob.model.Emailqueue;
import org.apache.commons.logging.Log;


/**
 *
 * @author R.Spoor
 */
 public class EmailqueuedetailsGroup extends IGroupEventHandler
 {
	static public final transient String RCS_ID = "$Id: EmailqueuedetailsGroup.java,v 1.1 2009/03/19 11:00:18 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 *
	 * @param context The current client context
	 * @param status  The new status of the group.
	 * @param group   The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
	    ICheckBox replyto = (ICheckBox)group.findByName("emailqueuedetailsReplytoagentCheckbox");
        replyto.setEnable(status == IGuiElement.SEARCH || status == IGuiElement.UNDEFINED);
	    ICheckBox addressable = (ICheckBox)group.findByName("emailqueuedetailsAddressableCheckbox");
	    addressable.setEnable(status == IGuiElement.SEARCH || status == IGuiElement.UNDEFINED);
        IDataTableRecord record = context.getSelectedRecord();
        record = (record == null ? null : record.getLinkedRecord(Emailqueue.NAME));
        replyto.setChecked((record == null ? false : record.getbooleanValue(Emailqueue.emailreplytoagent)));
        addressable.setChecked((record == null ? false : record.getbooleanValue(Emailqueue.addressableflag)));
	}

	/**
	 * Will be called if the will be change the state from visible=>hidden.
	 *
	 * This happends if the user switch the Domain or Form which contains this group.
	 *
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onHide(IClientContext context, IGroup group) throws Exception
	{
	    // insert your code here
	}

	/**
	 * Will be called if the will be change the state from hidden=>visible.
	 *
	 * This happends if the user switch to a Form which contains this group.
	 *
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onShow(IClientContext context, IGroup group) throws Exception
	{
	    // insert your code here
	}
}
