/**************************************************************************
 * Project  : jacob.docMan
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
 * DB table: <b>history</b>
 *
 **/
public final class History
{
   private History(){}

   // the name of the table alias    
   public final static String NAME = "history";
     
   // All field names of the table alias "history"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  pkey = "pkey";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  create_date = "create_date";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  type = "type";
   
   /** 
    * Die Langtexthistorie des Records der verändert worden ist.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  record_history = "record_history";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  record_serialized_pkey = "record_serialized_pkey";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  related_alias = "related_alias";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  user_key = "user_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  user_fullname = "user_fullname";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  record_representative_field = "record_representative_field";
   
   /** 
    * Wenn möglich wird der Pfad des Objektes berechnet<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  record_path = "record_path";
   
     
              
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _new = "new";
      public final static String _update = "update";
      public final static String _delete = "delete";
   }
                        
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