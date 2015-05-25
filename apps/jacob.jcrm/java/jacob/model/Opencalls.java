/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:31:00 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Opencalls
{
   private Opencalls(){}

   // the name of the table alias	 
   public final static String NAME = "opencalls";
	 
   // All field names of the table alias "opencalls"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  problem = "problem";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  problemtext = "problemtext";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datereported = "datereported";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  callstatus = "callstatus";
   
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
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  date_qa = "date_qa";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateclosed = "dateclosed";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  solutiontext = "solutiontext";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  calltype = "calltype";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  solution_key = "solution_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callworkgroup_key = "callWorkgroup_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callowner_key = "callOwner_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  callagent_key = "callAgent_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  incident_key = "incident_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callproduct_key = "callProduct_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  category_key = "category_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  callcontact_key = "callContact_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  servicelevel_key = "servicelevel_key";
   
	 
                       
   public final static class callstatus_ENUM
   {
      private callstatus_ENUM(){};
      public final static String _New = "New";
      public final static String _Assigned = "Assigned";
      public final static String _Owned = "Owned";
      public final static String _QA = "QA";
      public final static String _Closed = "Closed";

   }
                          
   public final static class calltype_ENUM
   {
      private calltype_ENUM(){};
      public final static String _Call = "Call";
      public final static String _Defect = "Defect";
      public final static String _Change_Request = "Change Request";

   }
                              
}