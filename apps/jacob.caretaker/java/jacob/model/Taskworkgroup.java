/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:53 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Taskworkgroup
{
   private Taskworkgroup(){}

   // the name of the table alias	 
   public final static String NAME = "taskworkgroup";
	 
   // All field names of the table alias "taskworkgroup"
   /** 
     required: true<br>
     type:     INTEGER<br>
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
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  notifymethod = "notifymethod";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fax = "fax";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  wrkgrptype = "wrkgrptype";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountingcode = "accountingcode";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hwg_title = "hwg_title";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hwg_shift = "hwg_shift";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hwg_manager = "hwg_manager";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  hwg_costingtype = "hwg_costingtype";
   
   /** 
     required: false<br>
     type:     FLOAT<br>
   */
   public final static String  hwg_rate = "hwg_rate";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  hwg_noemployees = "hwg_noemployees";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hwg_weekdays = "hwg_weekdays";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  hwg_actualcost = "hwg_actualcost";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  hwg_timelimit = "hwg_timelimit";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  hwg_hours = "hwg_hours";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  ak_site = "ak_site";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hwg_name = "hwg_name";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  orgexternal_key = "orgexternal_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  migration = "migration";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  groupconferencecall = "groupconferencecall";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  autodocumented = "autodocumented";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  groupstatus = "groupstatus";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  notifyowngroup = "notifyowngroup";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  activitytype = "activitytype";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  autoclosed = "autoclosed";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  sap_ak = "sap_ak";
   
	 
                    
   public final static class notifymethod_ENUM
   {
      private notifymethod_ENUM(){};
      public final static String _Bearbeiter = "Bearbeiter";
      public final static String _Signal = "Signal";
      public final static String _Email = "Email";
      public final static String _FAX = "FAX";
      public final static String _Funkruf = "Funkruf";
      public final static String _Drucker = "Drucker";
      public final static String _Keine = "Keine";

   }
                 
   public final static class wrkgrptype_ENUM
   {
      private wrkgrptype_ENUM(){};
      public final static String _AK = "AK";
      public final static String _HWG = "HWG";
      public final static String _TBP = "TBP";
      public final static String _OWNER = "OWNER";

   }
                          
   public final static class hwg_costingtype_ENUM
   {
      private hwg_costingtype_ENUM(){};
      public final static String _0_default = "0-default";
      public final static String _Eigen = "Eigen";
      public final static String _Fremd = "Fremd";

   }
                    
   public final static class hwg_actualcost_ENUM
   {
      private hwg_actualcost_ENUM(){};
      public final static String _0_Default = "0-Default";
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";

   }
                 
   public final static class ak_site_ENUM
   {
      private ak_site_ENUM(){};
      public final static String _50 = "50";
      public final static String _59 = "59";
      public final static String _66 = "66";

   }
                 
   public final static class migration_ENUM
   {
      private migration_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
              
   public final static class groupconferencecall_ENUM
   {
      private groupconferencecall_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class autodocumented_ENUM
   {
      private autodocumented_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class groupstatus_ENUM
   {
      private groupstatus_ENUM(){};
      public final static String _ungueltig = "ungültig";
      public final static String _gueltig = "gültig";

   }
           
   public final static class notifyowngroup_ENUM
   {
      private notifyowngroup_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class activitytype_ENUM
   {
      private activitytype_ENUM(){};
      public final static String _Eigen_ANG_102008 = "Eigen ANG 102008";
      public final static String _Eigen_LE_103008 = "Eigen LE 103008";
      public final static String _Fremd_950000 = "Fremd 950000";

   }
           
   public final static class autoclosed_ENUM
   {
      private autoclosed_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
      
}