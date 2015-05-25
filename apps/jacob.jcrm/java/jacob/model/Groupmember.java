/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:49 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Groupmember
{
   private Groupmember(){}

   // the name of the table alias	 
   public final static String NAME = "groupmember";
	 
   // All field names of the table alias "groupmember"
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  notifymethod = "notifymethod";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  tier = "tier";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  employee_key = "employee_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
	 
        
   public final static class notifymethod_ENUM
   {
      private notifymethod_ENUM(){};
      public final static String _Alert = "Alert";
      public final static String _Email = "Email";
      public final static String _Pager = "Pager";
      public final static String _Fax = "Fax";

   }
            
}