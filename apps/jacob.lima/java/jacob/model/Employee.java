/**************************************************************************
 * Project  : jacob.lima
 * Date     : Tue Mar 07 14:04:45 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Employee
{
   private Employee(){}

   // the name of the table alias	 
   public final static String NAME = "employee";
	 
   // All field names of the table alias "employee"
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
     required: true<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: false<br>
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
   public final static String  email = "email";
   
	 
                        
}