/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Feb 19 15:12:46 CET 2009
 */
package jacob.event.data;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import jacob.common.AppLogger;
import jacob.model.Contact_type;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;
import com.hecc.jacob.validation.contacts.ContactValidator;
import com.hecc.jacob.validation.contacts.ContactValidators;

/**
 *
 * @author R.Spoor
 */
public class Contact_typeTableRecord extends CreateChangeRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Contact_typeTableRecord.java,v 1.2 2009/02/24 15:55:38 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    public Contact_typeTableRecord()
    {
        super(
            Contact_type.created_by, Contact_type.create_date,
            Contact_type.changed_by, Contact_type.change_date
        );
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);
        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if(tableRecord.isDeleted())
            return;

        String method = tableRecord.getStringValue(Contact_type.validation_method);
        String expression = tableRecord.getStringValue(Contact_type.validation_expression);
        if (method == null ^ expression == null)
        {
            Context context = Context.getCurrent();
            String message = I18N.CONTACTTYPE_METHOD_EXPRESSION_MISMATCH.get(context);
            TableFieldExceptionCollection collection = new TableFieldExceptionCollection(message);
            collection.add(new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_method)));
            collection.add(new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_expression)));
            throw collection;
        }
        if (Contact_type.validation_method_ENUM._regex.equals(method))
        {
            try
            {
                Pattern.compile(expression);
            }
            catch (PatternSyntaxException e)
            {
                Context context = Context.getCurrent();
                String message = I18N.CONTACTTYPE_INVALID_REGEX.get(context);
                throw new InvalidFieldValueException(tableRecord, Contact_type.validation_expression, message);
            }
        }
        else if (Contact_type.validation_method_ENUM._class.equals(method))
        {
            ClassLoader loader = getClass().getClassLoader();
            ContactValidator validator = ContactValidators.getValidatorSafe(expression, loader);
            if (validator == null)
            {
                // was not loaded before, could not load now
                Context context = Context.getCurrent();
                String message = I18N.CONTACTTYPE_INVALID_CLASS.get(context);
                throw new InvalidFieldValueException(tableRecord, Contact_type.validation_expression, message);
            }
        }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    @Override
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
