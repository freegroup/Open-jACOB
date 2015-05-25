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
 * DB table: <b>followup</b>
 */
public final class Followup
{
    /**
     * Private constructor to prevent initialization.
     */
    private Followup()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "followup";

   // All field names of the table alias "followup"

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  pkey = "pkey";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  history = "history";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TIMESTAMP</b><br>
     */
    public final static String  duedate = "duedate";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  status = "status";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  notes = "notes";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  request_key = "request_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  agent_followup_key = "agent_followup_key";

    /**
     * The <b>status</b> enumeration values.
     */
    public final static class status_ENUM
    {
        /**
         * Private constructor to prevent initialization.
         */
        private status_ENUM()
        {
            // private constructor to prevent initialization
        }

        /**
         * The <b>open</b> value.
         */
        public final static String _open = "open";

        /**
         * The <b>in Progress</b> value.
         */
        public final static String _in_Progress = "in Progress";

        /**
         * The <b>done</b> value.
         */
        public final static String _done = "done";
    }

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