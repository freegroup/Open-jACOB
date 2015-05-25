/**************************************************************************
 * Project  : jacob.bailee
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
 * DB table: <b>document</b>
 *
 **/
public final class Document
{
   private Document(){}

   // the name of the table alias    
   public final static String NAME = "document";
     
   // All field names of the table alias "document"
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
    * type:     <b>TEXT</b><br>
    **/
   public final static String  tag = "tag";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  file = "file";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  create_date = "create_date";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  description = "description";
   
   /** 
    * Wird fr die Browser benýigt. Man muss so nicht das gesamte Document in den Speicher laden um den Namen des Dokumentes zu erhalten<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  file_name = "file_name";
   
   /** 
    * Das Datum an dem das Dokument als "zu lýchend" markiert wurde<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  request_for_delete_date = "request_for_delete_date";
   
   /** 
    * Warum der Benutzer dieses Dokument lýchen mýhte<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  request_for_delete_comment = "request_for_delete_comment";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  owner_email = "owner_email";
   
   /** 
    * optimale größe, maximal 500x500.
Kann somit auch auch kleiner sein.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  thumbnail = "thumbnail";
   
   /** 
    * Thumbnail mit der normgröße 500x500<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  normalized_thumbnail = "normalized_thumbnail";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  thumbnail_width = "thumbnail_width";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  thumbnail_height = "thumbnail_height";
   
     
                                       
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