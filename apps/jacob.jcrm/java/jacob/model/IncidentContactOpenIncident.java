/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class IncidentContactOpenIncident
{
   private IncidentContactOpenIncident(){}

   // the name of the table alias	 
   public final static String NAME = "incidentContactOpenIncident";
	 
   // All field names of the table alias "incidentContactOpenIncident"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
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
   public final static String  daterouted = "daterouted";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateassigned = "dateassigned";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateclosed = "dateclosed";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  contact_key = "contact_key";
   
	 
              
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _Incident = "Incident";
      public final static String _Sales_Activity = "Sales Activity";
      public final static String _Customer_Management = "Customer Management";
      public final static String _Quote = "Quote";
      public final static String _Order = "Order";
      public final static String _Call = "Call";
      public final static String _Defect = "Defect";
      public final static String _Maintenance = "Maintenance";

   }
                       
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _New = "New";
      public final static String _Assigned = "Assigned";
      public final static String _In_Progress = "In Progress";
      public final static String _Routed = "Routed";
      public final static String _Closed = "Closed";

   }
                  
}