/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Tue Dec 05 09:49:36 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Tagging
{
   private Tagging(){}

   // the name of the table alias	 
   public final static String NAME = "tagging";
	 
   // All field names of the table alias "tagging"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  tag = "tag";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  document_count = "document_count";
   
	 
         
}