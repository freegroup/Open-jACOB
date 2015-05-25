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
 * DB table: <b>storage_email</b>
 */
public final class Storage_email
{
    /**
     * Private constructor to prevent initialization.
     */
    private Storage_email()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "storage_email";

   // All field names of the table alias "storage_email"

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
     * type:     <b>TEXT</b><br>
     */
    public final static String  eduid = "eduid";

    /** 
     * qem_message table pkey<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  external_id = "external_id";

    /** 
     * The originator of the outbound email<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  storage_email_key = "storage_email_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TIMESTAMP</b><br>
     */
    public final static String  date = "date";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  from = "from";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  to = "to";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  cc = "cc";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  reply_to = "reply_to";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  subject = "subject";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  text_body = "text_body";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  original_mime_message = "original_mime_message";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  emailmimetype = "emailmimetype";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  direction = "direction";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  status = "status";

    /** 
     * Set if a outbound message can't be sent.
(status==Error)<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  error_message = "error_message";

    /**
     * The <b>direction</b> enumeration values.
     */
    public final static class direction_ENUM
    {
        /**
         * Private constructor to prevent initialization.
         */
        private direction_ENUM()
        {
            // private constructor to prevent initialization
        }

        /**
         * The <b>in</b> value.
         */
        public final static String _in = "in";

        /**
         * The <b>out</b> value.
         */
        public final static String _out = "out";
    }
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
         * The <b>Queued</b> value.
         */
        public final static String _Queued = "Queued";

        /**
         * The <b>Sent</b> value.
         */
        public final static String _Sent = "Sent";

        /**
         * The <b>Error</b> value.
         */
        public final static String _Error = "Error";
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
