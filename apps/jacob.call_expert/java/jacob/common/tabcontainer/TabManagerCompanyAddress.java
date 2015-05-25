package jacob.common.tabcontainer;

import jacob.common.FormManager;

import java.util.Iterator;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.impl.HTTPGroup;

public class TabManagerCompanyAddress
{
    private static String CONTAINER_NAME = "company_addressContainer";

    public static void showNewTab(IClientContext context) throws Exception
    {
        getContainer(context).setActivePane(context,1);
    }

    public static void showDisplayTab(IClientContext context) throws Exception
    {
        getContainer(context).setActivePane(context,0);
    }

    public static void clearTab(IClientContext context, String tableAlias) throws Exception
    {
        ((HTTPGroup)getPane(context,tableAlias)).clear(context);
    }

    private static ITabContainer getContainer(IClientContext context)
    {
        return (ITabContainer)FormManager.getCallhandling(context).findByName(CONTAINER_NAME);
    }

    private static ITabPane getPane(IClientContext context, String tableAlias)
    {
        ITabContainer container = getContainer(context);
        Iterator iter = container.getPanes().iterator();
        while (iter.hasNext())
        {
            ITabPane pane = (ITabPane) iter.next();
            // TODO: Nicht auf Name des TabPane pr�fen sondern auf den zugeh�rigen Alias!!
            if(pane.getLabel().equals(tableAlias))
            {
                return pane;
            }
        }
        return null;
    }
}

