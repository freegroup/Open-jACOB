/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Activity
{
   private Activity(){}

   // the name of the table alias	 
   public final static String NAME = "activity";
	 
   // All field names of the table alias "activity"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  activitytype_key = "activitytype_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  importance = "importance";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateassigned = "dateassigned";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateowned = "dateowned";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  plan_start = "plan_start";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  plan_done = "plan_done";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  actual_start = "actual_start";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  actual_done = "actual_done";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  reminder = "reminder";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  incident_key = "incident_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  contact_key = "contact_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  salesproject_key = "salesproject_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  owner_key = "owner_key";
   
	 
                 
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Not_Started = "Not Started";
      public final static String _In_Progress = "In Progress";
      public final static String _Completed = "Completed";
      public final static String _Rejected = "Rejected";
      public final static String _Deferred = "Deferred";

   }
           
   public final static class importance_ENUM
   {
      private importance_ENUM(){};
      public final static String _Low = "Low";
      public final static String _Normal = "Normal";
      public final static String _High = "High";

   }
                                                   
}