/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:26 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Process_responsible
{
   private Process_responsible(){}

   // the name of the table alias	 
   public final static String NAME = "process_responsible";
	 
   // All field names of the table alias "process_responsible"
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