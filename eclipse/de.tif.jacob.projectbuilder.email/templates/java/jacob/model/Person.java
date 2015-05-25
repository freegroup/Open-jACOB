/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:06 CET 2007
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
   public final static String  loginname = "loginname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  password = "password";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  mandator_id = "mandator_id";
   
	 
               
}