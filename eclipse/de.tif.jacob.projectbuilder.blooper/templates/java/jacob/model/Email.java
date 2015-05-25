/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Mon Dec 18 16:33:19 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Email
{
   private Email(){}

   // the name of the table alias	 
   public final static String NAME = "email";
	 
   // All field names of the table alias "email"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_user = "smtp_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_server = "smtp_server";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_password = "smtp_password";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_from_address = "smtp_from_address";
   
	 
               
}