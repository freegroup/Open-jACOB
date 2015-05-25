/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:49 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class IncidentWorkgroup
{
   private IncidentWorkgroup(){}

   // the name of the table alias	 
   public final static String NAME = "incidentWorkgroup";
	 
   // All field names of the table alias "incidentWorkgroup"
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
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  notificationaddr = "notificationaddr";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  notifymethod = "notifymethod";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  callgroup = "callgroup";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  taskgroup = "taskgroup";
   
	 
                    
   public final static class notifymethod_ENUM
   {
      private notifymethod_ENUM(){};
      public final static String _Owners = "Owners";
      public final static String _Alert = "Alert";
      public final static String _Email = "Email";
      public final static String _Fax = "Fax";
      public final static String _Pager = "Pager";
      public final static String _Printer = "Printer";

   }
         
}