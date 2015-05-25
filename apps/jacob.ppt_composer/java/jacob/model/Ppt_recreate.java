/**************************************************************************
 * Project  : jacob.ppt_composer
 * Date     : Fri Jul 11 12:18:36 CEST 2008
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
 * Condition: <b>ppt_recreate.changedate_thumbnail < ppt_recreate.changedate OR ppt_recreate.changedate_thumbnail is null</b>
 * DB table: <b>ppt</b>
 *
 **/
public final class Ppt_recreate
{
   private Ppt_recreate(){}

   // the name of the table alias	 
   public final static String NAME = "ppt_recreate";
	 
   // All field names of the table alias "ppt_recreate"
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
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  description = "description";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  document_en = "document_en";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  changedate_en = "changedate_en";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  thumbnail_en = "thumbnail_en";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  thumbnail_en_small = "thumbnail_en_small";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  document_de = "document_de";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  changedate_de = "changedate_de";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  thumbnail_de = "thumbnail_de";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  thumbnail_de_small = "thumbnail_de_small";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  master_language = "master_language";
   
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
   public final static String  changedate = "changedate";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  changedate_thumbnail = "changedate_thumbnail";
   
	 
                                         
   public final static class master_language_ENUM
   {
      private master_language_ENUM(){};
      public final static String _English = "English";
      public final static String _Deutsch = "Deutsch";

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