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
 * Condition: <b>report.applicationname='{APPLICATIONNAME}' and (report.ownerid='{LOGINID}' or report.accessmode='shared')</b>
 * DB table: <b>report</b>
 *
 **/
public final class Report
{
   private Report(){}

   // the name of the table alias    
   public final static String NAME = "report";
     
   // All field names of the table alias "report"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  id = "id";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  applicationname = "applicationname";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  definition = "definition";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  accessmode = "accessmode";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  ownerid = "ownerid";
   
   /** 
    * Should be required but because of compatibility to old databases containing entries with dbname applicationversion DECIMAL this has not been done.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  applicationversion = "applicationversion";
   
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
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  scheduled = "scheduled";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  lastusage = "lastUsage";
   
     
                 
   public final static class accessmode_ENUM
   {
      private accessmode_ENUM(){};
      public final static String _private = "private";
      public final static String _public = "public";
      public final static String _shared = "shared";
   }
                    
   public final static class scheduled_ENUM
   {
      private scheduled_ENUM(){};
      public final static String _false = "false";
      public final static String _true = "true";
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