/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Tue Dec 05 09:49:36 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Configuration
{
   private Configuration(){}

   // the name of the table alias	 
   public final static String NAME = "configuration";
	 
   // All field names of the table alias "configuration"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pop3_server = "pop3_server";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pop3_user = "pop3_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pop3_password = "pop3_password";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_server = "smtp_server";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_user = "smtp_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_password = "smtp_password";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  from_email = "from_email";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  error_email_text = "error_email_text";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notify_about_deletemark = "notify_about_deletemark";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  notify_about_delete = "notify_about_delete";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  notify_about_deletesubject = "notify_about_deletesubject";
   
	 
                                    
}