/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 11:03:35 CET 2009
 */
package jacob.event.ui.emailqueuedetails;

import java.util.HashSet;
import java.util.Set;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import jacob.common.AppLogger;
import jacob.model.Emailqueue;
import jacob.model.Emailqueuedetails;
import org.apache.commons.logging.Log;


/**
 * The event handler for the EmailqueuedetailsSearchButton search button.<br>
 *
 * @author R.Spoor
 */
public class EmailqueuedetailsSearchButton extends ISearchActionEventHandler
{
	static public final transient String RCS_ID = "$Id: EmailqueuedetailsSearchButton.java,v 1.1 2009/03/19 11:00:17 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
	 *
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
    @Override
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        Set<Long> queueKeys = getKeys(accessor, Emailqueue.NAME, Emailqueue.pkey);
        Set<Long> detailKeys = getKeys(accessor, Emailqueuedetails.NAME, Emailqueuedetails.queue_key);
        queueKeys.removeAll(detailKeys);
        if (!queueKeys.isEmpty())
        {
            IDataTable table = accessor.getTable(Emailqueuedetails.NAME);
            IDataTransaction transaction = accessor.newTransaction();
            try
            {
                for (Long key: queueKeys)
                {
                    IDataTableRecord record = table.newRecord(transaction);
                    record.setLongValue(transaction, Emailqueuedetails.queue_key, key.longValue());
                }
                transaction.commit();
            }
            finally
            {
                transaction.close();
            }
        }
        accessor = context.getDataAccessor();
        IGroup group = context.getGroup();
        IDataTable queueTable = accessor.getTable(Emailqueue.NAME);
        ISingleDataGuiElement queueid = (ISingleDataGuiElement)group.findByName("emailqueuedetailsQueueidText");
        queueTable.qbeSetValue(Emailqueue.queueid, queueid.getValue());
        ISingleDataGuiElement queuename = (ISingleDataGuiElement)group.findByName("emailqueuedetailsQueuenameText");
        queueTable.qbeSetValue(Emailqueue.queuename, queuename.getValue());
        ICheckBox replyto = (ICheckBox)group.findByName("emailqueuedetailsReplytoagentCheckbox");
        if (replyto.isChecked())
        {
            queueTable.qbeSetValue(Emailqueue.emailreplytoagent, replyto.getValue());
        }
        ICheckBox addressable = (ICheckBox)group.findByName("emailqueuedetailsAddressableCheckbox");
        if (addressable.isChecked())
        {
            queueTable.qbeSetValue(Emailqueue.addressableflag, addressable.getValue());
        }
        ISingleDataGuiElement servicelevel = (ISingleDataGuiElement)group.findByName("emailqueuedetailsServicelevelText");
        queueTable.qbeSetValue(Emailqueue.servicelevel, servicelevel.getValue());
        ISingleDataGuiElement priority = (ISingleDataGuiElement)group.findByName("emailqueuedetailsPriorityText");
        queueTable.qbeSetValue(Emailqueue.priority, priority.getValue());
        ISingleDataGuiElement minagents = (ISingleDataGuiElement)group.findByName("emailqueuedetailsMinimumText");
        queueTable.qbeSetValue(Emailqueue.minimumagents, minagents.getValue());
	    return true;
	}

    private Set<Long> getKeys(IDataAccessor accessor, String tableName, String keyName) throws Exception
    {
        Set<Long> keys = new HashSet<Long>();
        IDataTable table = accessor.getTable(tableName);
        table.qbeClear();
        int count = table.search(IRelationSet.LOCAL_NAME);
        for (int i = 0; i < count; i++)
        {
            IDataRecord record = table.getRecord(i);
            Long key = record.getLongValue(keyName);
            keys.add(key);
        }
        return keys;
    }

	/**
	 * This event method will be called, if the SEARCH action has been successfully executed.<br>
	 *
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
    @Override
	public void onSuccess(IClientContext context, IGuiElement button)
	{
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
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}
