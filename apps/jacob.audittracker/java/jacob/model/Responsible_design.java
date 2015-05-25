/**************************************************************************
 * Project  : jacob.audittracker
 * Date     : Tue Apr 10 21:35:36 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Responsible_design
{
   private Responsible_design(){}

   // the name of the table alias	 
   public final static String NAME = "responsible_design";
	 
   // All field names of the table alias "responsible_design"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
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
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  photo = "photo";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
	 
                  
}