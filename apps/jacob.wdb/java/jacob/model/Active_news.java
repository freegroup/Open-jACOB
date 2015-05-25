/**************************************************************************
 * Project  : jacob.wdb
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
 * DB table: <b>news</b>
 *
 **/
public final class Active_news
{
   private Active_news(){}

   // the name of the table alias    
   public final static String NAME = "active_news";
     
   // All field names of the table alias "active_news"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  pkey = "pkey";
   
   /** 
    * Kann vom Importer zur Synchronisation verwendet werden<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  external_id = "external_id";
   
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
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  headline = "headline";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  content = "content";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  intervall_start = "intervall_start";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  intervall_end = "intervall_end";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  renew = "renew";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  status = "status";
   
   /** 
    * Startzeitpunkt des veröffentlichzeitraumes in Millisekunden und dem normalisiertem Jahr 1970<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  normalized_start = "normalized_start";
   
   /** 
    * Endzeitpunkt des veröffentlichzeitraumes in Millisekunden und dem normalisiertem Jahr 1970<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  normalized_end = "normalized_end";
   
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
    * type:     <b>TEXT</b><br>
    **/
   public final static String  create_username = "create_username";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  change_date = "change_date";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  change_username = "change_username";
   
     
                             
   public final static class renew_ENUM
   {
      private renew_ENUM(){};
      public final static String _none = "none";
      public final static String _year = "year";
   }
           
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _active = "active";
      public final static String _hibernate = "hibernate";
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