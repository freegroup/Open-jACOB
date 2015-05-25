/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:55 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Reminderentry
{
   private Reminderentry(){}

   // the name of the table alias	 
   public final static String NAME = "reminderentry";
	 
   // All field names of the table alias "reminderentry"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  when = "when";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  action = "action";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroupkey = "workgroupkey";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  ownerkey = "ownerkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  tablename = "tablename";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  tablekey = "tablekey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  message = "message";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  addressee = "addressee";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
	 
                              
}