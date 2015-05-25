/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:31:00 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Opentask
{
   private Opentask(){}

   // the name of the table alias	 
   public final static String NAME = "opentask";
	 
   // All field names of the table alias "opentask"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  call_key = "call_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  taskstatus = "taskstatus";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  summary = "summary";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  desription = "desription";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  action = "action";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  daterequested = "daterequested";
   
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
     required: true<br>
     type:     LONG<br>
   */
   public final static String  taskworkgroup_key = "taskworkgroup_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  taskowner_key = "taskOwner_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  taskagent_key = "taskAgent_key";
   
	 
              
   public final static class taskstatus_ENUM
   {
      private taskstatus_ENUM(){};
      public final static String _New = "New";
      public final static String _Assigned = "Assigned";
      public final static String _Owned = "Owned";
      public final static String _QA = "QA";
      public final static String _Closed = "Closed";

   }
                                    
}