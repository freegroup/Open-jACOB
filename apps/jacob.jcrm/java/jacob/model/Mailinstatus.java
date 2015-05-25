/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Mailinstatus
{
   private Mailinstatus(){}

   // the name of the table alias	 
   public final static String NAME = "mailinstatus";
	 
   // All field names of the table alias "mailinstatus"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  error = "error";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  lastaccess = "lastaccess";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  fetchedmails = "fetchedmails";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  nextaccess = "nextaccess";
   
	 
               
}