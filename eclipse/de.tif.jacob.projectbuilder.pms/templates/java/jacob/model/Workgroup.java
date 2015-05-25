/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:25 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Workgroup
{
   private Workgroup(){}

   // the name of the table alias	 
   public final static String NAME = "workgroup";
	 
   // All field names of the table alias "workgroup"
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
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  comment = "comment";
   
	 
               
}