/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 04 14:00:55 CET 2008
 */
package jacob.event.ui.company;

import jacob.common.AppLogger;
import jacob.common.GroupManagerCompanyHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import jacob.model.Company_contact;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;


/**
 * The event handler for the CompanySearchButton2 search button.<br>
 *
 * @author achim
 */
public class CompanySearchButton2 extends ISearchActionEventHandler
{
    static public final transient String RCS_ID = "$Id: CompanySearchButton2.java,v 1.5 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

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
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        String value = context.getGroup().getInputFieldValue("companyFilterText");

        if(GroupManagerCompanyHeader.isSelected(context))
        {
            // A Record is backfilled in the Group and the corresponding TabPane.
            // => 1. Clear the group and the TabPane.
            //    2. Restore the search criteria from the unbound text field
            //
            context.clearGroup();
            TabManagerRequest.clearTab(context, context.getGroup().getGroupTableAlias());
            context.getGroup().setInputFieldValue("companyFilterText", value);
        }

        if(value!=null && value.length()>0)
        {
            context.getDataTable().qbeSetValue(Company.name, "|"+value);
            context.getDataTable().qbeSetValue(Company.branchoffice, "|"+value);
            context.getDataTable().qbeSetValue(Company.external_reference, "|"+value);
            if (StringUtils.isNumeric(value))
            {
                context.getDataTable().qbeSetValue(Company.pkey, "|"+value);
            }

            context.getDataTable(Company_contact.NAME).qbeSetValue(Company_contact.contact, "|" + value);
        }
        return true;
    }

    /**
     * This event method will be called, if the SEARCH action has been successfully executed.<br>
     *
     * @param context The current context of the application
     * @param button  The action button (the emitter of the event)
     */
    public void onSuccess(IClientContext context, IGuiElement button)
    {
        try
        {
            if(context.getSelectedRecord()!=null)
                TabManagerRequest.setActive(context, Company.NAME);
        }
        catch (Exception e)
        {
            // TODO: handle exception
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
     * @param context The current client context
     * @param status  The new group state. The group is the parent of the corresponding event button.
     * @param button  The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement button) throws Exception
    {
        button.setEnable(state == IGroup.SEARCH || state == IGroup.SELECTED);
    }
}
