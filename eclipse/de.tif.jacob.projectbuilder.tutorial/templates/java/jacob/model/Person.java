/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Jan 11 19:59:47 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Person
{
   private Person(){}

   // the name of the table alias	 
   public final static String NAME = "person";
	 
   // All field names of the table alias "person"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  firstname = "firstname";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  lastname = "lastname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  title = "title";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
	 
                  
}