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
 * DB table: <b>person</b>
 */
public final class Agent
{
    /**
     * Private constructor to prevent initialization.
     */
    private Agent()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "agent";

   // All field names of the table alias "agent"

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
    public final static String  fullname = "fullname";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  loginname = "loginname";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  password = "password";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>BOOLEAN</b><br>
     */
    public final static String  has_administration = "has_administration";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>BOOLEAN</b><br>
     */
    public final static String  has_debugging = "has_debugging";

    /** 
     * <br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>BOOLEAN</b><br>
     */
    public final static String  has_configuration = "has_configuration";

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
     * type:     <b>TEXT</b><br>
     */
    public final static String  email = "email";


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
