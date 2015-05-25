/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 11:39:42 CET 2009
 */
package jacob.event.ui.emailqueuedetails;

import jacob.common.AppLogger;
import jacob.model.Emailqueue;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 *
 * @author R.Spoor
 */
 public class EmailqueuedetailsPriorityText extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: EmailqueuedetailsPriorityText.java,v 1.1 2009/03/19 11:00:17 R.Spoor Exp $";
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
        text.setValue((record == null ? null : record.getStringValue(Emailqueue.priority)));
    }
}
