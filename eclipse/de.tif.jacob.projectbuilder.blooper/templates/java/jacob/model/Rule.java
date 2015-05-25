/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Mon Dec 18 16:33:19 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Rule
{
   private Rule(){}

   // the name of the table alias	 
   public final static String NAME = "rule";
	 
   // All field names of the table alias "rule"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  tablealias = "tablealias";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  action = "action";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  rule = "rule";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  readonly = "readonly";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
	 
                     
}