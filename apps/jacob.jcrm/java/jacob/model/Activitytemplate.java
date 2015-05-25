/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Activitytemplate
{
   private Activitytemplate(){}

   // the name of the table alias	 
   public final static String NAME = "activitytemplate";
	 
   // All field names of the table alias "activitytemplate"
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
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  activitystatus = "activitystatus";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  importance = "importance";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  d_years = "d_years";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  d_months = "d_months";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  d_days = "d_days";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  d_hours = "d_hours";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  d_minutes = "d_minutes";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  s_years = "s_years";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  s_months = "s_months";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  s_days = "s_days";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  s_hours = "s_hours";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  s_minutes = "s_minutes";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  activitytype_key = "activitytype_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
	 
                 
   public final static class activitystatus_ENUM
   {
      private activitystatus_ENUM(){};
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