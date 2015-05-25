/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Jan 11 19:59:47 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Image_store
{
   private Image_store(){}

   // the name of the table alias	 
   public final static String NAME = "image_store";
	 
   // All field names of the table alias "image_store"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: true<br>
     type:     DOCUMENT<br>
   */
   public final static String  image = "image";
   
	 
            
}