/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:25 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Workgroup_process
{
   private Workgroup_process(){}

   // the name of the table alias	 
   public final static String NAME = "workgroup_process";
	 
   // All field names of the table alias "workgroup_process"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  workgroup_name = "workgroup_name";
   
	 
            
}