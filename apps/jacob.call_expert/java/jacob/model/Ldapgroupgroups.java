/**************************************************************************
 * Project  : jacob.CallExpert
 * Date     : Mon Nov 23 11:09:29 CET 2009
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * 
 *
 * Condition: <b></b>
 * DB table: <b>ldapgroupgroups</b>
 */
public final class Ldapgroupgroups
{
    /**
     * Private constructor to prevent initialization.
     */
    private Ldapgroupgroups()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "ldapgroupgroups";

   // All field names of the table alias "ldapgroupgroups"

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  groupname = "groupname";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  parentgroupname = "parentgroupname";


    /**
     * Create a new Record within the current DataAccessor of the Context with a new transaction
     */
    public static IDataTableRecord newRecord(Context context) throws Exception
    {
        return newRecord(context,context.getDataAccessor().newTransaction());
    }

    /**
     * Create a new Record within the current DataAccessor of the Context and the handsover
     * transaction.
     */
    public static IDataTableRecord newRecord(Context context, IDataTransaction trans) throws Exception
    {
        return newRecord(context.getDataAccessor(),trans);
    }

    /**
     * Create a new Record within the hands over DataAccessor and a new transaction.
     */
    public static IDataTableRecord newRecord(IDataAccessor acc) throws Exception
    {
        return acc.getTable(NAME).newRecord(acc.newTransaction());
    }

    /**
     * Create a new Record within the hands over DataAccessor and transaction.
     */
    public static IDataTableRecord newRecord(IDataAccessor acc, IDataTransaction trans) throws Exception
    {
        return acc.getTable(NAME).newRecord(trans);
    }
}
