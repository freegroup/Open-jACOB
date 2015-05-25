/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * 
 * @author mike
 */
public class PersonTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "";

    static public final transient String RCS_REV = "";

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
     *      de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        String sFirstName = tableRecord.getSaveStringValue("firstname");
        String sLastName = tableRecord.getSaveStringValue("lastname");
        String sFullName;

        // Set the fullname from first and last names

        if (sFirstName.length() > 0)
        {
            sFullName = sLastName.toUpperCase() + ", " + sFirstName.toUpperCase();
            tableRecord.setValue(transaction, "fullname", sFullName);
        }
        else
        {
            sFullName = sLastName.toUpperCase();
            tableRecord.setValue(transaction, "fullname", sFullName);
        }

        if (tableRecord.getValue("uid") != null && tableRecord.hasChangedValue("uid"))
        {
            String uid = tableRecord.getSaveStringValue("uid");
            tableRecord.setValue(transaction, "uid", uid.toUpperCase());
        }

        // CTI Phone
        if (tableRecord.hasChangedValue("phone") || tableRecord.hasChangedValue("mobile"))
        {
            String phoneNumber = tableRecord.getSaveStringValue("phone");
            StringBuffer cleanNumber = new StringBuffer();
            for (int i = 0; i < phoneNumber.length(); i++)
            {
                String c = StringUtils.mid(phoneNumber, i, 1);
                if (StringUtils.isNumeric(c))
                    cleanNumber.append(c);
            }
            cleanNumber.append("#");
            phoneNumber = tableRecord.getSaveStringValue("mobile");
            
            for (int i = 0; i < phoneNumber.length(); i++)
            {
                String c = StringUtils.mid(phoneNumber, i, 1);
                if (StringUtils.isNumeric(c))
                    cleanNumber.append(c);
            }
            cleanNumber.append("#");
            tableRecord.setValue(transaction,"phonecti",cleanNumber.toString());
        }
    }
}
