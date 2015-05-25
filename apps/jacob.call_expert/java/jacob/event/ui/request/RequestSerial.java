/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 15:15:37 CET 2009
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;
import jacob.model.Request;
import jacob.model.Serial;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;
import de.tif.jacob.util.DatetimeUtil;

/**
 *
 * @author achim
 */
public class RequestSerial extends IForeignFieldEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestSerial.java,v 1.6 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.6 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * This hook method will be called, if the search icon of the foreign field
     * has been pressed. <br>
     * You can avoid the search action, if you return <code>false</code> or you
     * can add QBE search constraints to the respective data tables to constraint
     * the search result. <br>
     *
     * @param context
     *          The current client context
     * @param foreignField
     *          The foreign field itself
     * @return <code>false</code>, if you want to avoid the execution of the
     *         search action, otherwise <code>true</code>
     */
    public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
    {
        return true;
    }

    /**
     * This hook method will be called, if a record has been filled back
     * (selected) in the foreign field.
     *
     * @param context
     *          The current client context
     * @param foreignRecord
     *          The record which has been filled in the foreign field.
     * @param foreignField
     *          The foreign field itself
     */
    public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreignField) throws Exception
    {
        ISingleDataGuiElement datefield = (ISingleDataGuiElement) context.getForm().findByName("requestPurchase_date");
        String sPdate = datefield.getValue();
        Date dPdate = foreignRecord.getDateValue(Serial.purchase_date);
        if (dPdate != null && StringUtils.isBlank(sPdate))
        {
            datefield.setValue(DatetimeUtil.convertDateToString(dPdate, context.getLocale()));
        }
        // Tooltip Steuern
        if (dPdate != null && StringUtils.isNotBlank(sPdate))
        {

            String compDate = DatetimeUtil.convertDateToString(dPdate, context.getApplicationLocale());
            System.out.println(DatetimeUtil.convertDateToString(dPdate, context.getApplicationLocale()));
            System.out.println("compPdate" + compDate);
            System.out.println("csPdate" + sPdate);
            if (compDate.equals(sPdate))
            {
                context.getForm().findByName("requestPurchaseDateStyledText").setVisible(false);
            }
            else
            {
                context.getForm().findByName("requestPurchaseDateStyledText").setVisible(true);

            }
        }
        else
        {
            context.getForm().findByName("requestPurchaseDateStyledText").setVisible(false);
        }

    }

    /**
     * This hook method will be called, if the foreign field has been cleared
     * (deselected).
     *
     * @param context
     *          The current client context
     * @param foreignField
     *          The foreign field itself
     */
    public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
    {

        IDataTableRecord requestrec =context.getDataTable(Request.NAME).getSelectedRecord();
        if (requestrec != null)
        {
            IDataTransaction transaction = requestrec.getCurrentTransaction();
            if (transaction!=null)
            {

                requestrec.setValue(transaction, Request.serial_key, null);
            }
        }
    }

    @Override
    public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
    {
        context.getForm().findByName("requestPurchaseDateStyledText").setVisible(false);
    }
}
