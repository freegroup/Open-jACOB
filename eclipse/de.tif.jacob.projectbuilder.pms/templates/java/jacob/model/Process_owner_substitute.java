/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:26 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Process_owner_substitute
{
   private Process_owner_substitute(){}

   // the name of the table alias	 
   public final static String NAME = "process_owner_substitute";
	 
   // All field names of the table alias "process_owner_substitute"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  comment = "comment";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
	 
                  
}