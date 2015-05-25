/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 13:00:32 CET 2009
 */
package jacob.event.ui.request;


import jacob.common.tabcontainer.TabManagerSerial;
import jacob.model.Model;
import jacob.model.Product;
import jacob.model.Serial;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author achim
 */
public class RequestAddSerialImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        IDataTableRecord productRecord = context.getDataTable(Product.NAME).getSelectedRecord();
        IDataTableRecord modelRecord = context.getDataTable(Model.NAME).getSelectedRecord();
        if(productRecord == null || modelRecord == null)
        {
            String message = I18N.SERIAL_PRODUCT_OR_SERIAL_MISSING.get(context);
            throw new UserException(message);
        }
        IDataTransaction transaction = context.getDataAccessor().newTransaction();
        IDataTableRecord serialRecord = Serial.newRecord(context, transaction);

        serialRecord.setValue(transaction, Serial.model_key,modelRecord.getValue(Model.pkey));
        TabManagerSerial.showNewTab(context);
        context.getGroup().findByName("serialSerailno").requestFocus();
    }

    /**
     *
     * The event handler if the group status has been changed.<br>
     *
     * @param context The current work context of the jACOB application.
     * @param status  The new state of the group.
     * @param emitter The emitter of the event.
     */
    public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
    {
        image.setVisible(state==IGroup.UPDATE || state == IGroup.NEW);
    }
}
