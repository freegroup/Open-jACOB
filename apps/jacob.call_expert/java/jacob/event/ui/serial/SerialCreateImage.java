/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Nov 23 11:26:26 CET 2009
 */
package jacob.event.ui.serial;


import jacob.common.tabcontainer.TabManagerSerial;
import jacob.model.Serial;
import jacob.resources.I18N;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;
import de.tif.jacob.util.DatetimeUtil;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author R.Spoor
 */
public class SerialCreateImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
    public static void createSerial(IClientContext context) throws Exception
    {
        String value = context.getGroup().getInputFieldValue("serialSerailno");
        if (StringUtils.isEmpty(value))
        {
            String message = I18N.SERIAL_SERIALNO_MISSING.get(context);
            throw new UserException(message);
        }

        String purchaseDate = context.getGroup().getInputFieldValue("serialPurchase_date");
        if (StringUtils.isNotEmpty(purchaseDate))
        {
            // Check Date must be <= today
            // use DatetimeUtil instead of SimpleDateFormat
            Date serialPurchasedate = DatetimeUtil.convertToDate(purchaseDate);
            Date now = new Date();
            if (serialPurchasedate.compareTo(now) > 0)
            {
                throw new UserException("Purchase date is in the future");
            }
        }
        IDataTableRecord serialRecord = context.getDataTable(Serial.NAME).getSelectedRecord();

        // database uniqueness should solve this as well but it disables the fields
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        IDataTable serialTable = accessor.getTable(Serial.NAME);
        serialTable.qbeClear();
        serialTable.qbeSetKeyValue(Serial.model_key, serialRecord.getValue(Serial.model_key));
        serialTable.qbeSetKeyValue(Serial.serialno, value);
        serialTable.setMaxRecords(1);
        int count = serialTable.search(IRelationSet.LOCAL_NAME);
        if (count > 0)
        {
            // already exists
            String message = I18N.SERIAL_SERIAL_MODEL_NOT_UNIQUE.get(context);
            throw new UserException(message);
        }

        IDataTransaction trans = serialRecord.getCurrentTransaction();
        try
        {
            serialRecord.setStringValue(trans, Serial.serialno, value);
            serialRecord.setValue(trans, Serial.purchase_date, purchaseDate);
            trans.commit();
        }
        finally
        {
            trans.close();
        }
        TabManagerSerial.showDisplayTab(context);
    }

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
        createSerial(context);
    }
}

