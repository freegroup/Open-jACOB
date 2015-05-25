/**************************************************************************
 * Project  : jacob.docMan
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
 * DB table: <b>recyclebin</b>
 *
 **/
public final class Recyclebin
{
   private Recyclebin(){}

   // the name of the table alias    
   public final static String NAME = "recyclebin";
     
   // All field names of the table alias "recyclebin"
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
   public final static String  original_path = "original_path";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  name = "name";
   
   /** 
    * Enthält für alle Synonyme der Dokumente welche hier abgelegt wurden (es kann ein ein Ordner sein der gelöscht wurde). Dies werden für die Wiederherstellung benötigt.<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  synonyme = "synonyme";
   
   /** 
    * Die Datei welche gelöscht wurde oder der geZIPte Ordner der gelöscht wurde. Falls es ein Ordner ist, welcher gelöscht wurde, dann enthalten die Dateien darin den absoluten Pfad innerhalb des DMS.<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>DOCUMENT</b><br>
    **/
   public final static String  content = "content";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  delete_date = "delete_date";
   
   /** 
    * Der Name des Benutzers der das Objekt gelöscht hat. NICHT den PKEY. Man kann sonst keine Benutzer löschen welche mal Dateien entfernt haben.<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  delete_by = "delete_by";
   
   /** 
    * <br>
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