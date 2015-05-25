/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Nov 17 18:40:23 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;
import jacob.common.BreadCrumbController_RequestCategory;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableListBoxEventHandler;

/**
 *
 * @author andherz
 */
public class RequestCategoryListbox extends IMutableListBoxEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestCategoryListbox.java,v 1.2 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * Called, if the user changed the selection during the NEW or UPDATE state of
     * the related table record.
     *
     * @param context
     *          The current work context of the jACOB application.
     * @param emitter
     *          The emitter of the event.
     */
    public void onSelect(IClientContext context, IMutableListBox emitter) throws Exception
    {
        BreadCrumbController_RequestCategory.onSelect(context, emitter);
    }

    public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableListBox listBox) throws Exception
    {
    }

}
