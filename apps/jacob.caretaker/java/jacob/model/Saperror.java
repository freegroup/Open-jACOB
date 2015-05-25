/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Mar 11 16:13:27 CET 2008
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Saperror
{
   private Saperror(){}

   // the name of the table alias	 
   public final static String NAME = "saperror";
	 
   // All field names of the table alias "saperror"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  errortext = "errortext";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  aktiv = "aktiv";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  action = "action";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  exc_table = "exc_table";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  outputmessage = "outputmessage";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  actiontype = "actiontype";
   
	 
                       
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _import = "import";
      public final static String _export = "export";

   }
           
   public final static class action_ENUM
   {
      private action_ENUM(){};
      public final static String _create = "create";
      public final static String _update = "update";
      public final static String _delete = "delete";
      public final static String _import = "import";
      public final static String _close = "close";
      public final static String _writeback = "writeback";

   }
           
   public final static class exc_table_ENUM
   {
      private exc_table_ENUM(){};
      public final static String _call = "call";
      public final static String _task = "task";

   }
              
   public final static class actiontype_ENUM
   {
      private actiontype_ENUM(){};
      public final static String _loeschen = "löschen";
      public final static String _Fehler_zuruecksetzen = "Fehler zurücksetzen";

   }
   
}