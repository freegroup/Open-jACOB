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
 * Condition: <b>company_bank_country.useinaddress=1</b>
 * DB table: <b>country</b>
 */
public final class Company_bank_country
{
    /**
     * Private constructor to prevent initialization.
     */
    private Company_bank_country()
    {
        // private constructor to prevent initialization
    }

    /**
     * The name of the table alias.
     */
    public final static String NAME = "company_bank_country";

   // All field names of the table alias "company_bank_country"

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>LONG</b><br>
     */
    public final static String  pkey = "pkey";

    /** 
     * Full country name<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  country = "country";

    /** 
     * ISO alpha-2 code<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  alpha2 = "alpha2";

    /** 
     * ISO alpha-3 code<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  alpha3 = "alpha3";

    /** 
     * ISO numeric code<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  numeric = "numeric";

    /** 
     * International calling code<br>
     * <br>
     * required: <b>false</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  callingcode = "callingcode";

    /** 
     * Whether or not to use in addresses<br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>BOOLEAN</b><br>
     */
    public final static String  useinaddress = "useinaddress";

    /** 
     * <br>
     * <br>
     * required: <b>true</b><br>
     * type:     <b>TEXT</b><br>
     */
    public final static String  countrysearch = "countrysearch";


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
