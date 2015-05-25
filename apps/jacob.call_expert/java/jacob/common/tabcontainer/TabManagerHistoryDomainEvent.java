package jacob.common.tabcontainer;

import jacob.common.FormManager;
import java.util.Iterator;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;

public class TabManagerHistoryDomainEvent
{
    private static String CONTAINER_NAME = "eventContainer";

    public static void setVisible(IClientContext context, String tableAlias, boolean visibleFlag) throws Exception
    {
        ITabContainer container = getContainer(context);
        Iterator iter = container.getPanes().iterator();
        while (iter.hasNext())
        {
            ITabPane pane = (ITabPane) iter.next();
            if (pane.getPaneTableAlias().equals(tableAlias))
            {
                pane.setVisible(visibleFlag);
                if (visibleFlag)
                {
                    pane.setActive(context);
                }
            }
        }
    }

    public static ITabContainer getContainer(IClientContext context)
    {
        return (ITabContainer) FormManager.getHistory(context).findByName(CONTAINER_NAME);
    }
}
