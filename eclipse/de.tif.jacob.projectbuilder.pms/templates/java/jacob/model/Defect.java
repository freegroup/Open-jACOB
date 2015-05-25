/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:27 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Defect
{
   private Defect(){}

   // the name of the table alias	 
   public final static String NAME = "defect";
	 
   // All field names of the table alias "defect"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  subject = "subject";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  state = "state";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  resolve_date = "resolve_date";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  audit_key = "audit_key";
   
	 
                    
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _new = "new";
      public final static String _open = "open";
      public final static String _closed = "closed";

   }
               
}