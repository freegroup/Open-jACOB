/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:48 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Call
{
   private Call(){}

   // the name of the table alias	 
   public final static String NAME = "call";
	 
   // All field names of the table alias "call"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  mastercall_key = "mastercall_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  agentcall = "agentcall";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  employeecall = "employeecall";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  categorycall = "categorycall";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  workgroupcall = "workgroupcall";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  callstatus = "callstatus";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  escstatus = "escstatus";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  origin = "origin";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  phonetime = "phonetime";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  priority = "priority";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  problem = "problem";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datereported = "datereported";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateassigned = "dateassigned";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateresolved = "dateresolved";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  modifiedby = "modifiedby";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  cclist = "cclist";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  callbackmethod = "callbackmethod";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  problemtext = "problemtext";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  opentaskcount = "opentaskcount";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  closedtaskcount = "closedtaskcount";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  ksl = "ksl";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sl = "sl";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  autoclosed = "autoclosed";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  location_key = "location_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  object_key = "object_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountingcode_key = "accountingcode_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  coordinationtime = "coordinationtime";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecallconnected = "datecallconnected";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateowned = "dateowned";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datefirstcontact = "datefirstcontact";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  cti_phone = "cti_phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  affectedperson = "affectedperson";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datedocumented = "datedocumented";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateclosed = "dateclosed";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  totaltaskdoc = "totaltaskdoc";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  totaltasktimespent = "totaltasktimespent";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sd_time = "sd_time";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecallbackreq = "datecallbackreq";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  defaultcontract = "defaultcontract";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  closedby_sd = "closedby_sd";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  forwardbyphone = "forwardbyphone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  action = "action";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sl_overdue = "sl_overdue";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  date_sl_overdue = "date_sl_overdue";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  grade = "grade";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  gradestatement = "gradestatement";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  coordinationtime_m = "coordinationtime_m";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  coordinationtime_h = "coordinationtime_h";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  estimatedstart = "estimatedstart";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  estimateddone = "estimateddone";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  affectedperson_key = "affectedperson_key";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  totaltaskamount = "totaltaskamount";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountingcodetext = "accountingcodetext";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  personal_vote = "personal_vote";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  routinginfo = "routinginfo";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  customerquestion_k = "customerquestion_k";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sumcoordtime = "sumcoordtime";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  sumtaskamount = "sumtaskamount";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sumtaskdoc = "sumtaskdoc";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sumtasktimespent = "sumtasktimespent";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  totalcalltime = "totalcalltime";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  externalorderid = "externalorderid";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  notslrelevant = "notslrelevant";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  firstlevelclosed = "firstlevelclosed";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  warrentystatus = "warrentystatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  ordernumber = "ordernumber";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  warranty_case = "warranty_case";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  warranty_takeover = "warranty_takeover";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sap_ssle_nr = "sap_ssle_nr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sap_sm_nr = "sap_sm_nr";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sapimport = "sapimport";
   
	 
                          
   public final static class callstatus_ENUM
   {
      private callstatus_ENUM(){};
      public final static String _Rueckruf = "Rückruf";
      public final static String _Durchgestellt = "Durchgestellt";
      public final static String _AK_zugewiesen = "AK zugewiesen";
      public final static String _Fehlgeroutet = "Fehlgeroutet";
      public final static String _Verworfen = "Verworfen";
      public final static String _Angenommen = "Angenommen";
      public final static String _Fertig_gemeldet = "Fertig gemeldet";
      public final static String _Fertig_akzeptiert = "Fertig akzeptiert";
      public final static String _Dokumentiert = "Dokumentiert";
      public final static String _Geschlossen = "Geschlossen";

   }
              
   public final static class origin_ENUM
   {
      private origin_ENUM(){};
      public final static String _Tel_ = "Tel.";
      public final static String _Voice_Mail = "Voice Mail";
      public final static String _FAX = "FAX";
      public final static String _Email = "Email";
      public final static String _WebQ = "WebQ";
      public final static String _selbst = "selbst";
      public final static String _CTI = "CTI";

   }
              
   public final static class priority_ENUM
   {
      private priority_ENUM(){};
      public final static String _1_Normal = "1-Normal";
      public final static String _2_Kritisch = "2-Kritisch";
      public final static String _3_Produktion = "3-Produktion";
      public final static String _4_Notfall = "4-Notfall";

   }
                                
   public final static class callbackmethod_ENUM
   {
      private callbackmethod_ENUM(){};
      public final static String _Keine = "Keine";
      public final static String _SMS = "SMS";
      public final static String _Telefon = "Telefon";
      public final static String _Email = "Email";
      public final static String _FAX = "FAX";

   }
                                
   public final static class autoclosed_ENUM
   {
      private autoclosed_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
                                                                                                                                      
   public final static class warrentystatus_ENUM
   {
      private warrentystatus_ENUM(){};
      public final static String _ueberpruefen = "überprüfen";
      public final static String _verfolgen = "verfolgen";
      public final static String _nicht_verfolgen = "nicht verfolgen";
      public final static String _wird_verfolgt = "wird verfolgt";

   }
                     
}