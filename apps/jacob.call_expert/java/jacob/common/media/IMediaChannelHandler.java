package jacob.common.media;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

public interface IMediaChannelHandler
{
    /**
     * Initiate a new communication channel with the hands over connection parameter
     * (contactRecord)
     *
     * @param context
     * @param contactRecord a record based on the alias Customer_contact
     * @throws Exception
     */
    public void createNew(IClientContext context, IDataTableRecord customerContactRecord) throws Exception;

    /**
     * Initiate a replyTo. The implementation of IMediaChannelHandler must
     * retrieve the reply data from the hands over eventHistroyRecord.
     *
     * @param context
     * @param eventHistoryRecord
     */
    public void replyTo(IClientContext context, IDataTableRecord eventHistoryRecord ) throws Exception;
}
