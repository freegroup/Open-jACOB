/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Tue Dec 05 09:49:36 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Document
{
   private Document(){}

   // the name of the table alias	 
   public final static String NAME = "document";
	 
   // All field names of the table alias "document"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  tag = "tag";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  file = "file";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  file_name = "file_name";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  request_for_delete_date = "request_for_delete_date";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  request_for_delete_comment = "request_for_delete_comment";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  owner_email = "owner_email";
   
	 
                           
}