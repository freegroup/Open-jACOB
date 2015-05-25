/**************************************************************************
 * Project  : jacob.digisim
 * Date     : Thu Jul 31 09:46:01 CEST 2008
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
 * Condition: <b>queued_part.state='request_for_release'</b>
 * DB table: <b>part</b>
 *
 **/
public final class Queued_part
{
   private Queued_part(){}

   // the name of the table alias	 
   public final static String NAME = "queued_part";
	 
   // All field names of the table alias "queued_part"
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
    * type:     <b>TEXT</b><br>
    **/
   public final static String  name = "name";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  state = "state";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  code = "code";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  comment = "comment";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  tool_image = "tool_image";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  resource_image = "resource_image";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  owner_key = "owner_key";
   
   /** 
    * Zeigt von Public_part auf sein Ursprungselement<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  part_key = "part_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  history = "history";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  last_release = "last_release";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  assignee_key = "assignee_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  mandator_id = "mandator_id";
   
   /** 
    * Zeigt von Queued_part auf sein Ursprungselement<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  part_key2 = "part_key2";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  tooltip = "tooltip";
   
   /** 
    * z.B. Gatter, Schnittstelle,....<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  type = "type";
   
	 
              
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _development = "development";
      public final static String _lock = "lock";
      public final static String _request_for_release = "request_for_release";
      public final static String _released = "released";

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