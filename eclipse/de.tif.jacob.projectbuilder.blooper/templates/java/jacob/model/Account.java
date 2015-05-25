/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Mon Dec 18 16:33:19 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Account
{
   private Account(){}

   // the name of the table alias	 
   public final static String NAME = "account";
	 
   // All field names of the table alias "account"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  loginname = "loginname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  password = "password";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  photo = "photo";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
	 
                  
}