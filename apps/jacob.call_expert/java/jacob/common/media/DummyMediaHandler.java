package jacob.common.media;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

public class DummyMediaHandler implements IMediaChannelHandler
{
    public void createNew(IClientContext context,IDataTableRecord customerContactRecord) throws Exception
    {
        context.showTransparentMessage("createNew called");
    }

    public void replyTo(IClientContext context,IDataTableRecord eventHistoryRecord) throws Exception
    {
        context.showTransparentMessage("replyTo called");
    }
}
