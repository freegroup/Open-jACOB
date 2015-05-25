/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:31:00 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Servicelevel
{
   private Servicelevel(){}

   // the name of the table alias	 
   public final static String NAME = "servicelevel";
	 
   // All field names of the table alias "servicelevel"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  delay = "delay";
   
	 
            
}