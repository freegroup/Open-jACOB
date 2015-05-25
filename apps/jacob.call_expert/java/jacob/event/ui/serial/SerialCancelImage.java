/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Nov 23 11:28:07 CET 2009
 */
package jacob.event.ui.serial;


import jacob.common.tabcontainer.TabManagerSerial;
import jacob.model.Request;
import jacob.model.Serial;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author R.Spoor
 */
public class SerialCancelImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    /**
     * The event handler if the group status has been changed.<br>
     *
     * @param context The current work context of the jACOB application.
     * @param status  The new state of the group.
     * @param emitter The emitter of the event.
     */
    public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
    {
        // Your code here.....
    }

    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        try
        {
            IDataTableRecord serialRecord = context.getDataTable(Serial.NAME).getSelectedRecord();
            IDataTransaction trans = serialRecord.getCurrentTransaction();
            IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
            if (request.hasLinkedRecord(Serial.NAME))
            {
                request.resetLinkedRecord(trans, Serial.NAME);
            }
            trans.close();
            context.getDataTable(Serial.NAME).clear();
        }
        catch (Exception exc)
        {
            // ignore silently
        }
        TabManagerSerial.showDisplayTab(context);
    }
}
