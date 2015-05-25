package jacob.common;

import jacob.common.dblayer.ContactTypeManager;
import jacob.model.Contact_type;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hecc.jacob.validation.contacts.ContactValidator;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.html.SingleDataGUIElement;

public class UIUtil
{
    public static void transferFromGuiToRecord(IClientContext context,IDataTransaction transaction, IGuiElement containerReadFrom, IDataTableRecord recordToFill) throws Exception
    {
        Iterator iter = containerReadFrom.getChildren().iterator();
        while (iter.hasNext())
        {
            IGuiElement child = (IGuiElement) iter.next();
            if (child instanceof SingleDataGUIElement)
            {
                SingleDataGUIElement dataElement = (SingleDataGUIElement) child;
                if(dataElement.getDataField().getTableAlias().getName().equals(containerReadFrom.getGroupTableAlias()))
                {
                    recordToFill.setValue(transaction, dataElement.getDataField().getField(), dataElement.getValue());
                }
                else
                {
                    ITableAlias alias = dataElement.getDataField().getTableAlias();
                    IDataTableRecord foreignRecord = context.getDataTable(alias.getName()).getSelectedRecord();
                    if(foreignRecord==null)
                    {
                        recordToFill.resetLinkedRecord(transaction, alias);
                    }
                    else
                    {
                        recordToFill.setLinkedRecord(transaction,foreignRecord);
                    }
                }
            }
        }
    }

    /**
     * Returns the possibly modified contact data.
     */
    public static String validateContact(IClientContext context, String contact, String typePkey, ITableField tableField ) throws Exception
    {
        IDataTableRecord typeRecord = ContactTypeManager.findByPkey(Context.getCurrent(), typePkey);

        String validationType = typeRecord.getStringValue(Contact_type.validation_method);
        String validationExpr = typeRecord.getStringValue(Contact_type.validation_expression);
        if(validationType!=null && validationExpr !=null)
        {
            if(Contact_type.validation_method_ENUM._regex.equals(validationType))
            {
                try
                {
                    Pattern p = Pattern.compile(validationExpr);
                    Matcher matcher = p.matcher(contact);

                    if(!matcher.matches())
                    {
                        throw new RequiredFieldException(tableField, "Contact is not well formated");
                    }

                    // no modification
                    return contact;
                }
                catch(RuntimeException exc)
                {
                    exc.printStackTrace();
                    throw new RequiredFieldException(typeRecord.getFieldDefinition(Contact_type.validation_expression),"String is not a valid regular expression");
                }
            }
            else
            {
                try
                {
                    ContactValidator validator = (ContactValidator)Class.forName(validationExpr).newInstance();
                    contact = validator.validate(contact,Context.getCurrent());
                    return contact;
                }
                catch(UserException e)
                {
                    throw e;
                }
                catch(Exception exc)
                {
                    throw new RequiredFieldException(tableField,"Contact is not well formated");
                }
            }
        }
        return contact;
    }
}
