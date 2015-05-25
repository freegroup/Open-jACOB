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
 * DB table: <b>globalcontent</b>
 *
 **/
public final class Globalcontent
{
   private Globalcontent(){}

   // the name of the table alias    
   public final static String NAME = "globalcontent";
     
   // All field names of the table alias "globalcontent"
   /** 
    * Lucene document id of the index entry.

Note: Do not used this id for QBE or persistent purposes since this id is highly volatile.<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  id = "id";
   
   /** 
    * Score value of index entry after a query has been performed.<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>FLOAT</b><br>
    **/
   public final static String  score = "score";
   
   /** 
    * Table alias of the matching data table record<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  tablealias = "tablealias";
   
   /** 
    * Table name of the matching data table record<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  tablename = "tablename";
   
   /** 
    * Primary key of the matching data table record<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  primarykey = "primarykey";
   
   /** 
    * Subject of the index entry, which is used to identify the entry in a human readable form. E.g. "Jack White", "Project AB12", "IBM Corp.", etc.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  subject = "subject";
   
   /** 
    * Category, which can be used to categorize the index entry other than tablealias or tablename<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  category = "category";
   
   /** 
    * Entire contents of the entry, which is indexed but not stored.

Note: Use this field for specifiying your search expression (QBE) only.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  contents = "contents";
   
   /** 
    * Will be empty and filled up on the fly on data level<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  article = "article";
   
   /** 
    * Will be empty and filled up on the fly on data level<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  article_key = "article_key";
   
     
                              
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