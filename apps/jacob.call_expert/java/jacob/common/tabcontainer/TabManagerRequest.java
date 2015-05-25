package jacob.common.tabcontainer;

import jacob.common.FormManager;
import java.util.Iterator;
import java.util.List;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.impl.HTTPGroup;
import de.tif.jacob.screen.impl.html.Group;

public class TabManagerRequest
{
    private static String CONTAINER_NAME = "requestContainer";


    public static void setActive(IClientContext context, String tableAlias) throws Exception
    {
        ITabContainer container = getContainer(context);

        List panes = container.getPanes();
        for (int i = 0; i < panes.size(); i++)
        {
            ITabPane pane = (ITabPane) panes.get(i);
            if (pane.getPaneTableAlias().equals(tableAlias))
            {
                container.setActivePane(context, i);
            }
        }
        ((Group) container.getParent()).resetCache();
    }

    public static void clearTab(IClientContext context, String tableAlias) throws Exception
    {
        ((HTTPGroup) getPane(context, tableAlias)).clear(context);
    }

    /**
     * Clear the handsover table alias and hide the correesponding TabPane if it
     * on top.
     *
     * @param context
     * @param tableAlias
     * @throws Exception
     */
    public static void clear(IClientContext context, String tableAlias) throws Exception
    {
        if (getPane(context, tableAlias)==null)
        {
            context.clearGroup();
        }
        else
        {
            ((HTTPGroup) getPane(context, tableAlias)).clear(context);
        }
    }


    public static ITabContainer getContainer(IClientContext context)
    {
        return (ITabContainer) FormManager.getCallhandling(context).findByName(CONTAINER_NAME);
    }

    public static ITabPane getPane(IClientContext context, String tableAlias) throws Exception
    {
        ITabContainer container = getContainer(context);
        Iterator iter = container.getPanes().iterator();
        while (iter.hasNext())
        {
            ITabPane pane = (ITabPane) iter.next();
            if (pane.getPaneTableAlias().equals(tableAlias))
            {
                return pane;
            }
        }
        return null;
    }
}
