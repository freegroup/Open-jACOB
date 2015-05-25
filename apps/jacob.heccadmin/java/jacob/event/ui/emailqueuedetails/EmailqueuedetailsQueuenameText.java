/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 11:38:58 CET 2009
 */
package jacob.event.ui.emailqueuedetails;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import jacob.model.Emailqueue;
import org.apache.commons.logging.Log;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 *
 * @author R.Spoor
 */
 public class EmailqueuedetailsQueuenameText extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: EmailqueuedetailsQueuenameText.java,v 1.1 2009/03/19 11:00:17 R.Spoor Exp $";
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
     * @param state   The new group status
     * @param text    The corresponding GUI element of this event handler
     */
    @Override
    public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
    {
        text.setEnable(state == IGuiElement.SEARCH || state == IGuiElement.UNDEFINED);
        IDataTableRecord record = context.getSelectedRecord();
        record = (record == null ? null : record.getLinkedRecord(Emailqueue.NAME));
        text.setValue((record == null ? null : record.getStringValue(Emailqueue.queuename)));
    }
}
