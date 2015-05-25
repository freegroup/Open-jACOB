/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Apr 28 16:46:17 CEST 2009
 */
package jacob.event.ui.project;

import jacob.common.AppLogger;
import jacob.model.Sqldatasource;
import jacob.resources.I18N;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;


/**
 * The event handler for the ProjectDatabaseUpdateButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 *
 * @author R.Spoor
 */
public class ProjectDatabaseUpdateButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: ProjectDatabaseUpdateButton.java,v 1.3 2009/05/05 07:57:15 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The username field name.
     */
    private static final String USERNAME_FIELD = "username";

    /**
     * The password field name.
     */
    private static final String PASSWORD_FIELD = "password";

    /**
     * The submit button name.
     */
    private static final String SUBMIT_BUTTON = "submit";

    /**
     * The user has clicked on the corresponding button.<br>
     * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
     *             if the button has not the [selected] flag.<br>
     *             The selected flag assures that the event can only be fired,<br>
     *             if <code>selectedRecord!=null</code>.<br>
     *
     * @param context The current client context
     * @param emitter The corresponding button to this event handler
     */
    @Override
    public void onClick(final IClientContext context, IGuiElement emitter) throws Exception
    {
        FormLayout layout = new FormLayout(
            "10dlu, p, 10dlu, 300dlu, 10dlu",
            "10dlu, p, 2dlu, p, 10dlu"
        );
        CellConstraints cc = new CellConstraints();
        String title = I18N.PROJECT_DATABASE_UPDATE_TITLE.get(context);
        IFormDialogCallback callback = new IFormDialogCallback()
        {
            @SuppressWarnings("unchecked")
            public void onSubmit(IClientContext context, String buttonId,
                                 Map formValues) throws Exception
            {
                if (SUBMIT_BUTTON.equals(buttonId))
                {
                    String username = (String)formValues.get(USERNAME_FIELD);
                    String password = (String)formValues.get(PASSWORD_FIELD);
                    updateDatabases(context, username, password);
                }
            }
        };
        IFormDialog dialog = context.createFormDialog(title, layout, callback);
        dialog.addLabel(I18N.PROJECT_DATABASE_UPDATE_USERNAME.get(context), cc.xy(1, 1));
        dialog.addTextField(USERNAME_FIELD, "sa", cc.xy(3, 1));
        dialog.addLabel(I18N.PROJECT_DATABASE_UPDATE_PASSWORD.get(context), cc.xy(1, 3));
        dialog.addPasswordField(PASSWORD_FIELD, "", cc.xy(3, 3));
        dialog.addSubmitButton(SUBMIT_BUTTON, I18N.PROJECT_DATABASE_UPDATE_SUBMIT.get(context));
        dialog.setCancelButton(I18N.PROJECT_DATABASE_UPDATE_CANCEL.get(context));
        dialog.show();
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
     * @param emitter The corresponding button to this event handler
     */
    @Override
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        IUser user = context.getUser();
        if (!user.hasRole(Role.ADMIN.getName()))
        {
            emitter.setVisible(false);
        }
    }

    private void updateDatabases(IClientContext context, String username,
                                 String password)
            throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        IDataTable table = accessor.getTable(Sqldatasource.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Sqldatasource.name, "viewDataSource");
        table.setMaxRecords(1);
        int count = table.search(IRelationSet.LOCAL_NAME);
        if (count == 0)
        {
            String message = I18N.PROJECT_DATABASE_UPDATE_DATASOURCE_MISSING.get(context);
            context.createMessageDialog(message).show();
            return;
        }
        IDataRecord record = table.getRecord(0);
        String connectString = record.getSaveStringValue(Sqldatasource.connectstring);
        String driverClass = record.getSaveStringValue(Sqldatasource.jdbcdriverclass);
        // Class.forName should not fail
        Class.forName(driverClass);
        try
        {
            Connection connection = DriverManager.getConnection(connectString, username, password);
            try
            {
                Statement stmt = connection.createStatement();
                try
                {
                    // must loop through all results of the statement
                    boolean b = stmt.execute("exec sp_update_databases");
                    b = b || stmt.getUpdateCount() != -1;
                    // !b means execute returned no result set and the update
                    // count is -1
                    while (b)
                    {
                        b = stmt.getMoreResults() || stmt.getUpdateCount() != -1;
                        // !b means no more result sets and no more update counts
                    }
                    String message = I18N.PROJECT_DATABASE_UPDATE_SUCCESS.get(context);
                    context.createMessageDialog(message).show();
                }
                finally
                {
                    stmt.close();
                }
            }
            finally
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            String message = e.getMessage();
            context.createMessageDialog(message).show();
        }
    }
}
