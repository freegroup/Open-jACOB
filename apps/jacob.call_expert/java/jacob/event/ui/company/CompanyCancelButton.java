/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 04 17:56:04 CET 2008
 */
package jacob.event.ui.company;

import jacob.common.AppLogger;
import jacob.model.Company;
import jacob.model.Event_edit_company;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the CompanyCancelButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class CompanyCancelButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CompanyCancelButton.java,v 1.5 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has clicked on the corresponding button.
     *
     * @param context The current client context
     * @param button  The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTableRecord eventRecord   = context.getDataTable(Event_edit_company.NAME).getSelectedRecord();
        IDataTableRecord companyRecord = context.getDataTable(Company.NAME).getSelectedRecord();

        if (eventRecord != null && companyRecord != null)
        {
            if (companyRecord.isNew())
            {
                context.getDataTable(Company.NAME).clear();
                context.getDataTable(Event_edit_company.NAME).clear();
            }
            else
            {
                companyRecord.getCurrentTransaction().close();
            }
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
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        button.setVisible(IGroup.UPDATE==status || IGroup.NEW==status);
    }
}

