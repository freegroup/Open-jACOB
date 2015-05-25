/**************************************************************************
 * Project  : jacob.docPool
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
 * Condition: <b>favorite.favorite_index <> 0</b>
 * DB table: <b>bo</b>
 *
 **/
public final class Favorite
{
   private Favorite(){}

   // the name of the table alias    
   public final static String NAME = "favorite";
     
   // All field names of the table alias "favorite"
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
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  folder_key = "folder_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  parent_bo_key = "parent_bo_key";
   
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
    * type:     <b>LONG</b><br>
    **/
   public final static String  document_key = "document_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  change_date = "change_date";
   
   /** 
    * 0=kein favorite
ansonsten ist es die Sortierreihenfolge wie es bei den Favoriten angezeigt werden soll.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  favorite_index = "favorite_index";
   
   /** 
    * normalerweise kann man anhand von document_key oder folder_key unterscheiden welcher typ von BO dies ist. Type wird aber benötigt um die browser ordentlich zu sortieren.<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  type = "type";
   
     
                             
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _folder = "folder";
      public final static String _document = "document";
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