/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:06 CET 2007
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
     type:     TEXT<br>
   */
   public final static String  mandator_id = "mandator_id";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imap_server = "imap_server";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  user = "user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  password = "password";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smtp_server = "smtp_server";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  folder_out = "folder_out";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  folder_trash = "folder_trash";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  folder_spam = "folder_spam";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  save_incoming_emailaddress = "save_incoming_emailaddress";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  save_outgoing_emailaddress = "save_outgoing_emailaddress";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  signature = "signature";
   
	 
                                 
}