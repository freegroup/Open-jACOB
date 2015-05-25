/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Nov 07 14:13:27 CET 2008
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.common.dblayer.ContactTypeManager;
import jacob.model.Contact_type;
import jacob.model.Customer_contact;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hecc.jacob.validation.contacts.ContactValidator;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author achim
 */
public class Customer_contactTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: Customer_contactTableRecord.java,v 1.6 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.6 $";

    @Override
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.afterNewAction(tableRecord, transaction);
        tableRecord.setValue(transaction, Customer_contact.contact, "<empty>");
        tableRecord.setValue(transaction, Customer_contact.sort_id, tableRecord.getValue(Customer_contact.pkey));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction
     * (de.tif.jacob.core.data.IDataTableRecord,
     * de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);

        // Be in mind: It is not possible to modify the 'tableRecord', if we want
        // delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        String contact = tableRecord.getSaveStringValue(Customer_contact.contact);
        String typePkey = tableRecord.getSaveStringValue(Customer_contact.customer_contact_type_key);
        IDataTableRecord typeRecord = ContactTypeManager.findByPkey(Context.getCurrent(), typePkey);

        String validationType = typeRecord.getStringValue(Contact_type.validation_method);
        String validationExpr = typeRecord.getStringValue(Contact_type.validation_expression);
        if (validationType != null && validationExpr != null)
        {
            if (Contact_type.validation_method_ENUM._regex.equals(validationType))
            {
                try
                {
                    Pattern p = Pattern.compile(validationExpr);
                    Matcher matcher = p.matcher(contact);

                    if (!matcher.matches())
                    {
                        throw new RequiredFieldException(tableRecord.getFieldDefinition(Customer_contact.contact),"Contact is not well formated");
                    }
                }
                catch (Exception exc)
                {
                    throw new RequiredFieldException(typeRecord.getFieldDefinition(Contact_type.validation_expression),"String is not a valid regular expression");
                }
            }
            else
            {
                try
                {
                    ContactValidator validator = (ContactValidator)Class.forName(validationExpr).newInstance();
                    contact = validator.validate(contact,Context.getCurrent());
                }
                catch (UserException e)
                {
                    throw e;
                }
                catch (Exception exc)
                {
                    throw new RequiredFieldException(tableRecord.getFieldDefinition(Customer_contact.contact),"Contact is not well formated");
                }
            }
        }
        tableRecord.setValue(transaction, Customer_contact.contact, contact);
        String displaycontact = typeRecord.getSaveStringValue(Contact_type.type) + " - " + contact;
        tableRecord.setValue(transaction, Customer_contact.displaycontact, displaycontact);
    }
}

