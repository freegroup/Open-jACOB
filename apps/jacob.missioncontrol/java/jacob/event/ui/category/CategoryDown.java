package jacob.event.ui.category;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Aug 02 10:41:56 CEST 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CategoryDown-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author mike
 * 
 */
public class CategoryDown extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CategoryDown.java,v 1.4 2005/11/02 12:47:03 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.4 $";

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The user has been click on the corresponding button.
     * 
     * @param context
     *            The current client context
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataBrowser browser = context.getDataBrowser(); // the current
                                                            // browser
        IDataTable categoryTable = context.getDataTable(); // the table in
                                                            // which the actions
                                                            // performes
        boolean isAdmin = "administration".equals(context.getDomain().getName());

        IDataTableRecord category = categoryTable.getSelectedRecord();

        if (category == null)
        {
            alert("Kein Gewerk selektiert!");
            return;
        }
        String parentCategoryKey = category.getStringValue("pkey");

        if (isAdmin)
        {
            context.clearDomain();
        }
        else
        {
            categoryTable.qbeClear();
        }

        // set search criteria
        categoryTable.qbeSetValue("parent_category_key", parentCategoryKey);
        // do the search itself
        //
        if (isAdmin)
        {
            browser.search("category_admin", Filldirection.BOTH);
        }
        else
        {
            browser.search(IRelationSet.LOCAL_NAME);
        }
        if (browser.recordCount() == 0)
        {
            // no child found, select old one
            if (isAdmin)
            {
                context.clearDomain();
            }
            else
            {
                categoryTable.qbeClear();
            }
            categoryTable.qbeSetValue("pkey", parentCategoryKey);
            if (isAdmin)
            {
                browser.search("category_admin", Filldirection.BOTH);
            }
            else
            {
                browser.search(IRelationSet.LOCAL_NAME);
            }
        }

        // display the result set
        //
        context.getGUIBrowser().setData(context, browser);

    }

    /**
     * The status of the parent group (TableAlias) has been changed. <br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record. <br>
     * <br>
     * Possible values for the state is defined in IGuiElement <br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     * 
     * @param context
     *            The current client context
     * @param status
     *            The new group state. The group is the parent of the
     *            corresponding event button.
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        // button.setEnable(true/false);
    }
}
