package jacob.common.media;

import jacob.common.FormManager;
import jacob.model.Customer;
import jacob.model.Customer_contact;
import jacob.model.Customer_gender;
import jacob.model.Customer_history;
import jacob.model.Request;
import jacob.model.Storage_email;
import jacob.model.Storage_email_outbound;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.util.StringUtil;

public class EmailMediaChannel implements IMediaChannelHandler
{
    private final static int CLIENT_PORT = 47900;

    public void createNew(IClientContext context, IDataTableRecord contactRecord) throws Exception
    {
        String email = contactRecord.getStringValue(Customer_contact.contact);
        // Collect Data
        IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
        String subject = "Request ID " + request.getValue(Request.pkey);
        subject += " " + request.getValue(Request.description);
        IDataTableRecord customer = contactRecord.getLinkedRecord(Customer.NAME);
        String salutation = customer.getLinkedRecord(Customer_gender.NAME).getSaveStringValue(Customer_gender.gender);
        salutation += " ";
        salutation += customer.getSaveStringValue(Customer.name);

        prepareEmailForm(context, request, customer, email, subject, salutation);
    }

    public void replyTo(IClientContext context, IDataTableRecord eventHistoryRecord) throws Exception
    {
        IDataTableRecord request = eventHistoryRecord.getLinkedRecord(Request.NAME);
        IDataTableRecord customer = eventHistoryRecord.getLinkedRecord(Customer_history.NAME);
        customer = context.getDataAccessor().getTable(Customer.NAME).loadRecord(customer.getPrimaryKeyValue());
        IDataTableRecord storage_email = eventHistoryRecord.getLinkedRecord(Storage_email.NAME);
        String email = storage_email.getSaveStringValue(Storage_email.reply_to);

        String subject = "Re: " + storage_email.getSaveStringValue(Storage_email.subject);
        String body = storage_email.getSaveStringValue(Storage_email.text_body);
        body = "\n\n\n>" + StringUtil.replace(body, "\n", "\n>");
        String salutation = customer.getLinkedRecord(Customer_gender.NAME).getSaveStringValue(Customer_gender.gender);
        salutation += " ";
        salutation += customer.getSaveStringValue(Customer.name);

        IDataTableRecord emailRec = prepareEmailForm(context, request, customer, email, subject, body);

        emailRec.setLinkedRecord(emailRec.getCurrentTransaction(), storage_email);
    }

    private IDataTableRecord prepareEmailForm(IClientContext context, IDataTableRecord request, IDataTableRecord customer,
                                              String email, String subject, String body) throws Exception
    {
        FormManager.showEmailOutbound(context);

        context.getDomain().setVisible(true);

        // Create new EmailRecord//Set Form Values

        // provide the request record in the eMail form/data accessor
        //
        context.getDataAccessor().propagateRecord(request, IRelationSet.LOCAL_NAME, Filldirection.BOTH);
        context.getDataAccessor().propagateRecord(customer, IRelationSet.LOCAL_NAME, Filldirection.BOTH);

        IDataTransaction emailTransaction = context.getDataAccessor().newTransaction();
        IDataTableRecord emailRec = context.getDataTable(Storage_email_outbound.NAME).newRecord(emailTransaction);
        emailRec.setValue(emailTransaction, Storage_email_outbound.to, email);
        emailRec.setValue(emailTransaction, Storage_email_outbound.subject, subject);
        emailRec.setValue(emailTransaction, Storage_email_outbound.text_body, body);
        emailRec.setValue(emailTransaction, Storage_email_outbound.from, context.getUser().getEMail());
        emailRec.setValue(emailTransaction, Storage_email_outbound.direction, Storage_email_outbound.direction_ENUM._out);

        return emailRec;
    }
}
