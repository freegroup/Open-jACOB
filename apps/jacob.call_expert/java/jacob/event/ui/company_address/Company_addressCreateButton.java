/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jan 21 12:09:01 CET 2009
 */
package jacob.event.ui.company_address;

import jacob.common.AppLogger;
import jacob.common.tabcontainer.TabManagerCompanyAddress;
import jacob.model.Company_address;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.html.SingleDataGUIElement;


/**
 * The event handler for the Company_addressCreateButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class Company_addressCreateButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Company_addressCreateButton.java,v 1.3 2009/11/23 11:33:46 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

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
     * @param button  The corresponding button to this event handler
     * @throws Exception
     */
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        IDataTableRecord addressRecord = context.getDataTable(Company_address.NAME).getSelectedRecord();
        IDataTransaction trans = addressRecord.getCurrentTransaction();
        GuiElement button = (GuiElement) emitter;
        IGroup pane = button.getGroup();
        Iterator iter = pane.getChildren().iterator();
        while (iter.hasNext())
        {
            IGuiElement child = (IGuiElement) iter.next();
            if (child instanceof SingleDataGUIElement)
            {
                SingleDataGUIElement dataElement = (SingleDataGUIElement) child;
                if(dataElement.getDataField().getTableAlias().getName().equals(pane.getGroupTableAlias()))
                {
                    addressRecord.setValue(trans, dataElement.getDataField().getField(), dataElement.getValue());
                }
                else
                {
                    ITableAlias alias = dataElement.getDataField().getTableAlias();
                    IDataTableRecord foreignRecord = context.getDataTable(alias.getName()).getSelectedRecord();
                    if(foreignRecord==null)
                        addressRecord.resetLinkedRecord(trans, alias);
                    else
                        addressRecord.setLinkedRecord(trans,foreignRecord);
                }
            }
        }
        IBrowser browser = (IBrowser)context.getGroup().findByName("companyCompany_addressBrowser");
        browser.add(context, addressRecord);
        TabManagerCompanyAddress.showDisplayTab(context);
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
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        //emitter.setEnable(true/false);
    }
}
