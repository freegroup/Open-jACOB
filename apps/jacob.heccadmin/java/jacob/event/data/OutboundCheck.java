package jacob.event.data;

import jacob.resources.I18N;
import com.hecc.jacob.util.exception.InvalidFieldValueException;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;

public class OutboundCheck
{
    private OutboundCheck()
    {
        // private constructor to prevent initialization
    }

    /**
     * Checks an outbound code.
     * 
     * @param record The record for which to check the outbound code.
     * @param field The name of the field of the outbound code.
     * @param code The outbound code to check.
     * @throws InvalidFieldValueException If the outbound code is invalid.
     * @throws NoSuchFieldException If the field does not exist for the record.
     * @throws NullPointerException If the record is <code>null</code>.
     */
    public static void checkOutboundCode(IDataTableRecord record, String field,
                                         String code)
           throws InvalidFieldValueException, NoSuchFieldException
    {
        boolean validOutbound = true;
        try
        {
            if (Integer.parseInt(code) < 0)
            {
                validOutbound = false;
            }
        }
        catch (NumberFormatException e)
        {
            validOutbound = false;
        }
        validOutbound = validOutbound && code.length() == 4;
        if (!validOutbound)
        {
            Context context = Context.getCurrent();
            // reuse message
            String message = I18N.INVALID_OUTBOUNDCODE.get(context);
            throw new InvalidFieldValueException(record, field, message);
        }
    }
}
