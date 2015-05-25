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
 * Condition: <b>menutree.lifecycle ='alive'</b>
 * DB table: <b>menutree</b>
 *
 **/
public final class Menutree
{
   private Menutree(){}

   // the name of the table alias    
   public final static String NAME = "menutree";
     
   // All field names of the table alias "menutree"
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
    * External reference name which might be used for misc purposes<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  external_ref_name = "external_ref_name";
   
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
    * type:     <b>TEXT</b><br>
    **/
   public final static String  title = "title";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  state = "state";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  menutree_parent_key = "menutree_parent_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  create_date = "create_date";
   
   /** 
    * Name des Benutzers der die Menüstruktur angelegt hat<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  create_username = "create_username";
   
   /** 
    * PKEY des Benutzers der die Menustruktur angelegt hat. KEIN f_key! Macht das Löschen eines Benutzers unmöglich wenn dieser eine Menüstruktur angelegt hat. <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  create_userid = "create_userid";
   
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
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  article_key = "article_key";
   
   /** 
    * Used to restore Menutree->Artikel relation, if an article is restored

No foreign key constrained needed!<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  recyclebin_article_key = "recyclebin_article_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  lifecycle = "lifecycle";
   
     
                       
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _active = "active";
      public final static String _locked = "locked";
   }
                                   
   public final static class lifecycle_ENUM
   {
      private lifecycle_ENUM(){};
      public final static String _alive = "alive";
      public final static String _recyclebin = "recyclebin";
      public final static String _recyclebin2 = "recyclebin2";
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