/**************************************************************************
 * Project  : jacob.CallExpert
 * Date     : Mon Nov 23 11:09:27 CET 2009
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
 * DB table: <b>contact_type</b>
 */
public final class Contact_type
{
    /**
     * Private constructor to prevent initialization.
     */
    private Contact_type()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "contact_type";

   // All field names of the table alias "contact_type"

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
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  changed_by = "changed_by";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TIMESTAMP</b><br>
     */
    public final static String  change_date = "change_date";

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
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  media_type = "media_type";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>ENUM</b><br>
     */
    public final static String  validation_method = "validation_method";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  validation_expression = "validation_expression";

    /** 
     * Callback class for handling outbound messages of the corresponding media type.
The handler class can open a dialog window, chat window or call the Avaya foundation for phone call.
<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  java_outbound_handler_class = "java_outbound_handler_class";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  type = "type";

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
     * The <b>validation_method</b> enumeration values.
     */
    public final static class validation_method_ENUM
    {
        /**
         * Private constructor to prevent initialization.
         */
        private validation_method_ENUM()
        {
            // private constructor to prevent initialization
        }

        /**
         * The <b>class</b> value.
         */
        public final static String _class = "class";

        /**
         * The <b>regex</b> value.
         */
        public final static String _regex = "regex";
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
