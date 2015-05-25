/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Apr 28 17:30:44 CEST 2009
 */
package jacob.event.ui.agent;

import jacob.common.AppLogger;
import jacob.model.Sqldatasource;
import jacob.resources.I18N;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;


/**
 * The event handler for the AgentLdapUpdateButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author R.Spoor
 */
public class AgentLdapUpdateButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: AgentLdapUpdateButton.java,v 1.2 2009/05/05 07:57:15 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

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
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        IDataTable table = accessor.getTable(Sqldatasource.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Sqldatasource.name, "ldapDataSource");
        table.setMaxRecords(1);
        int count = table.search(IRelationSet.LOCAL_NAME);
        if (count == 0)
        {
            String message = I18N.LDAP_UPDATE_DATASOURCE_MISSING.get(context);
            context.createMessageDialog(message).show();
            return;
        }
        IDataRecord record = table.getRecord(0);
        String connectString = record.getSaveStringValue(Sqldatasource.connectstring);
        String driverClass = record.getSaveStringValue(Sqldatasource.jdbcdriverclass);
        String userName = record.getSaveStringValue(Sqldatasource.username);
        String password = record.getStringValue(Sqldatasource.password);
        // Class.forName should not fail
        Class.forName(driverClass);
        try
        {
            Connection connection = DriverManager.getConnection(connectString, userName, password);
            try
            {
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();
                try
                {
                    // must loop through all results of the statement
                    boolean b = stmt.execute("exec sp_update_ldapcontents");
                    b = b || stmt.getUpdateCount() != -1;
                    // !b means execute returned no result set and the update
                    // count is -1
                    while (b)
                    {
                        b = stmt.getMoreResults() || stmt.getUpdateCount() != -1;
                        // !b means no more result sets and no more update counts
                    }
                    b = stmt.execute("exec sp_update_ldaplinks");
                    b = b || stmt.getUpdateCount() != -1;
                    while (b)
                    {
                        b = stmt.getMoreResults() || stmt.getUpdateCount() != -1;
                    }
                    connection.commit();
                    String message = I18N.LDAP_UPDATE_SUCCESS.get(context);
                    context.createMessageDialog(message).show();
                }
                catch (SQLException e)
                {
                    connection.rollback();
                    throw e;
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
}
