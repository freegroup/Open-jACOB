/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Mon Oct 16 16:16:38 CEST 2006
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
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
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
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  photo = "photo";
   
	 
               
}