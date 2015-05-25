/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Jan 11 19:59:47 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Recipe
{
   private Recipe(){}

   // the name of the table alias	 
   public final static String NAME = "recipe";
	 
   // All field names of the table alias "recipe"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  image = "image";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  template = "template";
   
	 
               
}