/**************************************************************************
 * Project  : jacob.qualitymaster
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
 * DB table: <b>requestbranch</b>
 *
 **/
public final class Requestbranch
{
   private Requestbranch(){}

   // the name of the table alias    
   public final static String NAME = "requestbranch";
     
   // All field names of the table alias "requestbranch"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  rcs_branch_key = "rcs_branch_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  request_key = "request_key";
   
     
      
  /**
   * Create a new Record within the current DataAccessor of the Context with a new transaction
   **/
  public static IDataTableRecord newRecord(Context context) throws Exception
  {
    return newRecord(context,context.getDataAccessor().newTransaction());
  }

  /**
   * Create a new Record within the current DataAccessor of the Context and the handsover
   * transaction.
   **/
  public static IDataTableRecord newRecord(Context context, IDataTransaction trans) throws Exception
  {
    return newRecord(context.getDataAccessor(),trans);
  }

  /**
   * Create a new Record within the hands over DataAccessor and a new transaction.
   **/
  public static IDataTableRecord newRecord(IDataAccessor acc) throws Exception
  {
    return acc.getTable(NAME).newRecord(acc.newTransaction());
  }

  /**
   * Create a new Record within the hands over DataAccessor and transaction.
   **/
  public static IDataTableRecord newRecord(IDataAccessor acc, IDataTransaction trans) throws Exception
  {
    return acc.getTable(NAME).newRecord(trans);
  }
}