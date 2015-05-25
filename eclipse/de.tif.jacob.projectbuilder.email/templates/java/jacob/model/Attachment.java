/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:06 CET 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Attachment
{
   private Attachment(){}

   // the name of the table alias	 
   public final static String NAME = "attachment";
	 
   // All field names of the table alias "attachment"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  docfile = "docfile";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  email_key = "email_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  mandator_id = "mandator_id";
   
	 
            
}