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
 * DB table: <b>event</b>
 */
public final class Event_edit_company
{
    /**
     * Private constructor to prevent initialization.
     */
    private Event_edit_company()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "event_edit_company";

   // All field names of the table alias "event_edit_company"

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  pkey = "pkey";

    /** 
     * Defines the kind of event. Avoid spaces in the enum. The value will be used in the internal I18N resource bundle<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  type = "type";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  detail = "detail";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONGTEXT</b><br>
     */
    public final static String  answer = "answer";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  description = "description";

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
     * type:     <b>TEXT</b><br>
     */
    public final static String  created_by = "created_by";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TIMESTAMP</b><br>
     */
    public final static String  create_date = "create_date";

    /** 
     * must be in synch with the table field "contact_type.media_type"<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  media_type = "media_type";

    /** 
     * The direction of the communication channel (media_type)<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  direction = "direction";

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
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  agent_key = "agent_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  event_status_key = "event_status_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  event_company_key = "event_company_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  customer_key = "customer_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  question_answer_key = "question_answer_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  followup_key = "followup_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  storage_phone_key = "storage_phone_key";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  storage_email_key = "storage_email_key";

    /**
     * The <b>type</b> enumeration values.
     */
    public final static class type_ENUM
    {
        /**
         * Private constructor to prevent initialization.
         */
        private type_ENUM()
        {
            // private constructor to prevent initialization
        }

        /**
         * The <b>NewRequest</b> value.
         */
        public final static String _NewRequest = "NewRequest";

        /**
         * The <b>ChangeRequest</b> value.
         */
        public final static String _ChangeRequest = "ChangeRequest";

        /**
         * The <b>NewCustomer</b> value.
         */
        public final static String _NewCustomer = "NewCustomer";

        /**
         * The <b>ChangeCustomer</b> value.
         */
        public final static String _ChangeCustomer = "ChangeCustomer";

        /**
         * The <b>ChangeCompany</b> value.
         */
        public final static String _ChangeCompany = "ChangeCompany";

        /**
         * The <b>System</b> value.
         */
        public final static String _System = "System";

        /**
         * The <b>NewCompany</b> value.
         */
        public final static String _NewCompany = "NewCompany";

        /**
         * The <b>NewQA</b> value.
         */
        public final static String _NewQA = "NewQA";

        /**
         * The <b>InboundEmail</b> value.
         */
        public final static String _InboundEmail = "InboundEmail";

        /**
         * The <b>OutboundEmail</b> value.
         */
        public final static String _OutboundEmail = "OutboundEmail";

        /**
         * The <b>NewFollowUp</b> value.
         */
        public final static String _NewFollowUp = "NewFollowUp";
    }
    /**
     * The <b>media_type</b> enumeration values.
     */
    public final static class media_type_ENUM
    {
        /**
         * Private constructor to prevent initialization.
         */
        private media_type_ENUM()
        {
            // private constructor to prevent initialization
        }

        /**
         * The <b>voice</b> value.
         */
        public final static String _voice = "voice";

        /**
         * The <b>chat</b> value.
         */
        public final static String _chat = "chat";

        /**
         * The <b>email</b> value.
         */
        public final static String _email = "email";
    }
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
