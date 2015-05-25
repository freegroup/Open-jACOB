/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:27 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Audit
{
   private Audit(){}

   // the name of the table alias	 
   public final static String NAME = "audit";
	 
   // All field names of the table alias "audit"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     LONGTEXT<br>
   */
   public final static String  comment = "comment";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  date = "date";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  date_note = "date_note";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  audit_co_moderator_key = "audit_co_moderator_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  audit_moderator_key = "audit_moderator_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  state = "state";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  protocol = "protocol";
   
	 
                             
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _Geplant = "Geplant";
      public final static String _Abgesagt = "Abgesagt";
      public final static String _Abgeschlossen = "Abgeschlossen";

   }
         
}