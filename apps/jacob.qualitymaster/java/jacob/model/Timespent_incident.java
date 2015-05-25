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
 * wird nur in der Form incident benötift damit diese beim Backfill unabhängig von der "timespent" Form ist
 *
 * Condition: <b></b>
 * DB table: <b>timespent</b>
 *
 **/
public final class Timespent_incident
{
   private Timespent_incident(){}

   // the name of the table alias    
   public final static String NAME = "timespent_incident";
     
   // All field names of the table alias "timespent_incident"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  pkey = "pkey";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  category = "category";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  timespent = "timespent";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  timespenttype = "timespentType";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  timespentdate = "timespentDate";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  summary = "summary";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  employee_key = "employee_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  datereported = "datereported";
   
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
    * type:     <b>LONG</b><br>
    **/
   public final static String  incidentbillable_key = "incidentBillable_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  budget_key = "budget_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  cleared = "cleared";
   
     
           
   public final static class category_ENUM
   {
      private category_ENUM(){};
      public final static String _Coding = "Coding";
      public final static String _Testing = "Testing";
      public final static String _Documentation = "Documentation";
      public final static String _Training = "Training";
      public final static String _other = "other";
   }
              
   public final static class timespentType_ENUM
   {
      private timespentType_ENUM(){};
      public final static String _hours = "hours";
      public final static String _days = "days";
   }
                                
   public final static class cleared_ENUM
   {
      private cleared_ENUM(){};
      public final static String _yes = "yes";
      public final static String _no = "no";
      public final static String _n_a = "n/a";
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