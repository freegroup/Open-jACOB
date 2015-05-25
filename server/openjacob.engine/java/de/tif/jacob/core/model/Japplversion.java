/**************************************************************************
 * Project  : jacob.admin
 * Date     : Fri Oct 30 00:06:41 CET 2009
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package de.tif.jacob.core.model;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * 
 *
 * Condition: <b></b>
 * DB table: <b>japplversion</b>
 *
 **/
public final class Japplversion
{
   private Japplversion(){}

   // the name of the table alias	 
   public final static String NAME = "japplversion";
	 
   // All field names of the table alias "japplversion"
   /** 
    * major and minor version number without fix number<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  version = "version";
   
   /** 
    * fix number<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  fix = "fix";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  buildby = "buildby";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  builddatetime = "builddatetime";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  installedsince = "installedsince";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  buildmachine = "buildmachine";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  status = "status";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>BINARY</b><br>
    **/
   public final static String  content = "content";
   
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
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  contentsize = "contentsize";
   
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
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  installseqnbr = "installseqnbr";
   
   /** 
    * the complete version number of the API version (the engine version of the jacobBase.jar the application has been compiled)<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  api_version = "api_version";
   
   /** 
    * the complete version number of the JAD version (the engine version of the JAD file, i.e. the jacob designer's jacobBase.jar used to write the JAD file)<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  jad_version = "jad_version";
   
	 
                          
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _inactive = "inactive";
      public final static String _productive = "productive";
      public final static String _test = "test";

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