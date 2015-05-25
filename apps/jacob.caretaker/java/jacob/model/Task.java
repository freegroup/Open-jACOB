/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:53 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Task
{
   private Task(){}

   // the name of the table alias	 
   public final static String NAME = "task";
	 
   // All field names of the table alias "task"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  calltask = "calltask";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  summary = "summary";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  sequence = "sequence";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  daterequested = "daterequested";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateowned = "dateowned";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  priority = "priority";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  taskstatus = "taskstatus";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  escstatus = "escstatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  cclist = "cclist";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  taskstart = "taskstart";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  taskdone = "taskdone";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  no_resolver = "no_resolver";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  workable = "workable";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  accountdate = "accountdate";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  timedocumentation = "timedocumentation";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  smcerrorcode_key = "smcerrorcode_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  tasktype_key = "tasktype_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  feedback = "feedback";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pm_order = "pm_order";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  change_user = "change_user";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  totaltimespent = "totaltimespent";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  ext_system_key = "ext_system_key";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datedocumented = "datedocumented";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateresolved = "dateresolved";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  transmitted = "transmitted";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  resolved_extsystem = "resolved_extsystem";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  create_user = "create_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  resolved_user = "resolved_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  action = "action";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  workgrouptask = "workgrouptask";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  totaltimespent_m = "totaltimespent_m";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  totaltimespent_h = "totaltimespent_h";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  timedoc_m = "timedoc_m";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  timedoc_h = "timedoc_h";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  extsystem_id = "extsystem_id";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  estimateddone = "estimateddone";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  estimatedstart = "estimatedstart";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  object_key = "object_key";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateclosed = "dateclosed";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecanceld = "datecanceld";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  groundplotrequired = "groundplotrequired";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  amount = "amount";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_beh_bez = "edvin_beh_bez";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_beh_code = "edvin_beh_code";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_bgr_bez = "edvin_bgr_bez";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_bgr_code = "edvin_bgr_code";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_btl_code = "edvin_btl_code";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_btl_bez = "edvin_btl_bez";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_sha_code = "edvin_sha_code";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_sha_bez = "edvin_sha_bez";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_mah_code = "edvin_mah_code";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvin_mah_bez = "edvin_mah_bez";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  disruption_start = "disruption_start";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  disruption_h = "disruption_h";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  disruption_m = "disruption_m";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  productionloss_m = "productionloss_m";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  productionloss_h = "productionloss_h";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  taskno = "taskno";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  extsystem_subid = "extsystem_subid";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  issynchronized = "issynchronized";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sap_auftrag = "sap_auftrag";
   
	 
                          
   public final static class priority_ENUM
   {
      private priority_ENUM(){};
      public final static String _1_Normal = "1-Normal";
      public final static String _2_Kritisch = "2-Kritisch";
      public final static String _3_Produktion = "3-Produktion";
      public final static String _4_Notfall = "4-Notfall";

   }
           
   public final static class taskstatus_ENUM
   {
      private taskstatus_ENUM(){};
      public final static String _Neu = "Neu";
      public final static String _Angelegt = "Angelegt";
      public final static String _Freigegeben = "Freigegeben";
      public final static String _Storniert = "Storniert";
      public final static String _In_Arbeit = "In Arbeit";
      public final static String _Fertig_gemeldet = "Fertig gemeldet";
      public final static String _Dokumentiert = "Dokumentiert";
      public final static String _Abgerechnet = "Abgerechnet";
      public final static String _Abgeschlossen = "Abgeschlossen";

   }
                                   
   public final static class workable_ENUM
   {
      private workable_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";

   }
                       
   public final static class feedback_ENUM
   {
      private feedback_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
                             
   public final static class transmitted_ENUM
   {
      private transmitted_ENUM(){};
      public final static String _false = "false";
      public final static String _true = "true";

   }
           
   public final static class resolved_extsystem_ENUM
   {
      private resolved_extsystem_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
                                                                                                              
   public final static class issynchronized_ENUM
   {
      private issynchronized_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
      
}