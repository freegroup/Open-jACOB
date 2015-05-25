/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:04 CET 2007
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
   public final static String  subject = "subject";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  body = "body";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  from = "from";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  folder = "folder";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  msgid = "msgId";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  mandator_id = "mandator_id";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  senddate = "senddate";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  to = "to";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  answered = "answered";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  deleted = "deleted";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  draft = "draft";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  flagged = "flagged";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  recent = "recent";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  seen = "seen";
   
	 
                                             
}