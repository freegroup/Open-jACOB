/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 18 15:30:26 CET 2008
 */
package jacob.event.ui.project;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import jacob.event.data.ProjectTableRecord;
import jacob.model.Account;
import jacob.model.Helpdeskcategory;
import jacob.model.Project;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;


/**
 * The event handler for the ProjectToggleHelpdeskButton record selected button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 *
 * @author R.Spoor
 */
public class ProjectToggleHelpdeskButton extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: ProjectToggleHelpdeskButton.java,v 1.3 2009/02/19 12:58:32 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 *
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 */
	@Override
	public void onClick(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
		IDataTableRecord accountRecord = currentRecord.getLinkedRecord(Account.NAME);
        IDataTableRecord rootCategory = ProjectTableRecord.getRootCategory(currentRecord);
        if (rootCategory == null)
        {
            IDataAccessor accessor = currentRecord.getAccessor().newAccessor();
            IDataTable categoryTable = accessor.getTable(Helpdeskcategory.NAME);
            IDataTransaction transaction = accessor.newTransaction();
            try
            {
                rootCategory = categoryTable.newRecord(transaction);
                String name = accountRecord.getStringValue(Account.account) + " " +
                              currentRecord.getStringValue(Project.project);
                rootCategory.setValue(transaction, Helpdeskcategory.name, name);
                rootCategory.setValue(transaction, Helpdeskcategory.longname, "/" + name + "/");
                rootCategory.setValue(transaction, Helpdeskcategory.path, "/");
                rootCategory.setBooleanValue(transaction, Helpdeskcategory.leaf, false);
                rootCategory.setBooleanValue(transaction, Helpdeskcategory.deleted, false);
                Object key = currentRecord.getValue(Project.pkey);
                rootCategory.setValue(transaction, Helpdeskcategory.project_key, key);
                transaction.commit();
                String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK_TURNOFF.get(context);
                button.setLabel(label);
            }
            finally
            {
                transaction.close();
            }
        }
        else if (rootCategory.getbooleanValue(Helpdeskcategory.deleted))
        {
            IDataAccessor accessor = currentRecord.getAccessor().newAccessor();
            IDataTransaction transaction = accessor.newTransaction();
            try
            {
                setDeleted(transaction, rootCategory, false);
                transaction.commit();
                String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK_TURNOFF.get(context);
                button.setLabel(label);
            }
            finally
            {
                transaction.close();
            }
        }
        else
        {
            IDataAccessor accessor = currentRecord.getAccessor().newAccessor();
            IDataTransaction transaction = accessor.newTransaction();
            try
            {
                setDeleted(transaction, rootCategory, true);
                transaction.commit();
                String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK_TURNON.get(context);
                button.setLabel(label);
            }
            finally
            {
                transaction.close();
            }
        }
	}

	/**
	 * Sets the deleted flag for an entire tree of categories.
	 *
	 * @param transaction The transaction to use.
	 * @param categoryRecord The root of the tree.
	 * @param deleted The new value of the deleted flag.
	 * @throws InvalidExpressionException If any value is invalid. Should not happen.
	 * @throws NoSuchFieldException If any field does not exist. Should not happen.
	 * @throws RecordLockedException If any record is locked.
	 */
	private static void setDeleted(IDataTransaction transaction,
	                               IDataTableRecord categoryRecord,
	                               boolean deleted)
	        throws InvalidExpressionException, NoSuchFieldException, RecordLockedException
	{
	    categoryRecord.setBooleanValue(transaction, Helpdeskcategory.deleted, deleted);
	    IDataAccessor accessor = categoryRecord.getAccessor().newAccessor();
	    IDataTable categoryTable = accessor.getTable(Helpdeskcategory.NAME);
	    categoryTable.qbeClear();
	    Object key = categoryRecord.getValue(Helpdeskcategory.pkey);
	    categoryTable.qbeSetKeyValue(Helpdeskcategory.parentcategory_key, key);
	    int count = categoryTable.search();
	    for (int i = 0; i < count; i++)
	    {
	        IDataTableRecord childRecord = categoryTable.getRecord(i);
	        setDeleted(transaction, childRecord, deleted);
	    }
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
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	    if (status == IGuiElement.SELECTED)
	    {
	        IDataTableRecord projectRecord = context.getSelectedRecord();
	        IDataTableRecord rootCategory = ProjectTableRecord.getRootCategory(projectRecord);
	        if (rootCategory == null || rootCategory.getbooleanValue(Helpdeskcategory.deleted))
	        {
                String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK_TURNON.get(context);
	            button.setLabel(label);
	        }
	        else
	        {
                String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK_TURNOFF.get(context);
	            button.setLabel(label);
	        }
	        IUser user = context.getUser();
	        button.setEnable(user.hasRole(Role.ADMIN.getName()));
	    }
	    else
	    {
            String label = I18N.PROJECT_HELPDESK_TOGGLEHELPDESK.get(context);
	        button.setLabel(label);
	        button.setEnable(false);
	    }
	}
}

