/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Oct 11 10:13:59 CEST 2006
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
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  password = "password";
   
	 
            
}