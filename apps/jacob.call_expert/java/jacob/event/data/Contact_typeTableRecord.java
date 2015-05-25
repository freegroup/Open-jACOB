/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 03 11:39:48 CET 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Contact_type;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import com.hecc.jacob.validation.contacts.ContactValidator;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;

/**
 *
 * @author andherz
 */
public class Contact_typeTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Contact_typeTableRecord.java,v 1.3 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {

        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        // check the validation method of the contyctType and validate them
        //
        String validationMethod     = tableRecord.getStringValue(Contact_type.validation_method);
        String validationExpression = tableRecord.getStringValue(Contact_type.validation_expression);
        if (validationMethod == null ^ validationExpression == null)
        {
            TableFieldExceptionCollection exc = new TableFieldExceptionCollection("Either both of these fields are filled or none");
            exc.add(new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_method)));
            exc.add(new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_expression)));
            throw exc;
        }

        if (Contact_type.validation_method_ENUM._regex.equals(validationMethod))
        {
            try
            {
                Pattern.compile(validationExpression);
            }
            catch (Exception exc)
            {
                throw new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_expression),"String is not a valid regular expression");
            }
        }
        else if (Contact_type.validation_method_ENUM._class.equals(validationMethod))
        {
            try
            {
                Object obj = Class.forName(validationExpression).newInstance();
                if (!(obj instanceof ContactValidator))
                {
                    throw new Exception("Class [" + validationExpression + "] is not type of [" + ContactValidator.class.getName() + "]");
                }
            }
            catch (Exception exc)
            {
                throw new RequiredFieldException(tableRecord.getFieldDefinition(Contact_type.validation_expression),"Unable to load java class ["+validationExpression+"]");
            }
        }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
