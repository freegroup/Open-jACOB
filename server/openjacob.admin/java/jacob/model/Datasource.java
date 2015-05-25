/**************************************************************************
 * Project  : jacob.admin
 * Date     : Fri Jun 25 02:27:16 CEST 2010
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
 * DB table: <b>datasource</b>
 *
 **/
public final class Datasource
{
   private Datasource(){}

   // the name of the table alias	 
   public final static String NAME = "datasource";
	 
   // All field names of the table alias "datasource"
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
    * type:     <b>TEXT</b><br>
    **/
   public final static String  jndiname = "jndiName";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  location = "location";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  rdbtype = "rdbType";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  connectstring = "connectString";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  configurepool = "configurePool";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  jdbcdriverclass = "jdbcDriverClass";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  username = "userName";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  password = "password";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  maxactivecons = "maxActiveCons";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  maxidlecons = "maxIdleCons";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  maxconnectionwait = "maxConnectionWait";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  validationquery = "validationQuery";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  adjustment = "adjustment";
   
	 
              
   public final static class location_ENUM
   {
      private location_ENUM(){};
      public final static String _jACOB = "jACOB";
      public final static String _Webserver = "Webserver";

   }
           
   public final static class rdbType_ENUM
   {
      private rdbType_ENUM(){};
      public final static String _AutoDetect = "AutoDetect";
      public final static String _Oracle = "Oracle";
      public final static String _Oracle9 = "Oracle9";
      public final static String _MSSQL = "MSSQL";
      public final static String _MySQL = "MySQL";
      public final static String _HSQL = "HSQL";
      public final static String _Lucene = "Lucene";

   }
              
   public final static class configurePool_ENUM
   {
      private configurePool_ENUM(){};
      public final static String _No = "No";
      public final static String _Yes = "Yes";

   }
                                
   public final static class adjustment_ENUM
   {
      private adjustment_ENUM(){};
      public final static String _jACOB = "jACOB";
      public final static String _Quintus = "Quintus";

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