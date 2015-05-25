package jacob.common;

import jacob.common.tabcontainer.TabManagerRequest;
import java.awt.Color;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;

public class AbstractShowExtendedFormLabel  extends ILabelEventHandler implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        String groupAliasName = element.getGroupTableAlias();
        TabManagerRequest.setActive(context, groupAliasName);
    }

    public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
    {
        if (state == IGroup.SEARCH || state == IGroup.SELECTED )
        {
            label.setColor(Color.blue);
            label.setEnable(true);
        }
        else
        {
            label.setColor(Color.lightGray);
            label.setEnable(false);
        }
    }
}
