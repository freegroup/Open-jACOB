/**************************************************************************
 * Project  : jacob.audittracker
 * Date     : Tue Apr 10 21:35:36 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Object
{
   private Object(){}

   // the name of the table alias	 
   public final static String NAME = "object";
	 
   // All field names of the table alias "object"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  last_change = "last_change";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  remark = "remark";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  responsible_design_key = "responsible_design_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  responsible_technic_key = "responsible_technic_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  responsible_development_key = "responsible_development_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  responsible_standardization_key = "responsible_standardization_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  contact_technic_key = "contact_technic_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  vendor_key = "vendor_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pilot_site = "pilot_site";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pilot_building = "pilot_building";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pilot_name = "pilot_name";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  pilot_implemented = "pilot_implemented";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  pilot_planed = "pilot_planed";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  pilot_comment = "pilot_comment";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  sps_comment = "sps_comment";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sps_partnumber = "sps_partnumber";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sps_partnumber_f = "sps_partnumber_f";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sps_partnumber_s = "sps_partnumber_s";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_partavailable = "sps_partavailable";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_partavailable_f = "sps_partavailable_f";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_partavailable_s = "sps_partavailable_s";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  responsible_pilot_key = "responsible_pilot_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_documentation = "sps_documentation";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_documentation_f = "sps_documentation_f";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  sps_documentation_s = "sps_documentation_s";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_hmi_template_key = "implementor_hmi_template_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_sps_key = "implementor_sps_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_hardware_key = "implementor_hardware_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_hardwaretemplate_key = "implementor_hardwaretemplate_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_sps_s_key = "implementor_sps_s_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_sps_f_key = "implementor_sps_f_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  implementor_robot_key = "implementor_robot_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  objectgroup_key = "objectgroup_key";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  responsible_remark = "responsible_remark";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  implementation_remark = "implementation_remark";
   
	 
                                                  
   public final static class pilot_implemented_ENUM
   {
      private pilot_implemented_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";

   }
           
   public final static class pilot_planed_ENUM
   {
      private pilot_planed_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";

   }
                          
   public final static class sps_partavailable_ENUM
   {
      private sps_partavailable_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";
      public final static String _Nicht_Notwendig = "Nicht Notwendig";

   }
           
   public final static class sps_partavailable_f_ENUM
   {
      private sps_partavailable_f_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";
      public final static String _Nicht_Notwendig = "Nicht Notwendig";

   }
           
   public final static class sps_partavailable_s_ENUM
   {
      private sps_partavailable_s_ENUM(){};
      public final static String _Ja = "Ja";
      public final static String _Nein = "Nein";
      public final static String _Nicht_Notwendig = "Nicht Notwendig";

   }
              
   public final static class sps_documentation_ENUM
   {
      private sps_documentation_ENUM(){};
      public final static String _Vorhanden = "Vorhanden";
      public final static String _Keine = "Keine";

   }
           
   public final static class sps_documentation_f_ENUM
   {
      private sps_documentation_f_ENUM(){};
      public final static String _Vorhanden = "Vorhanden";
      public final static String _Keine = "Keine";

   }
           
   public final static class sps_documentation_s_ENUM
   {
      private sps_documentation_s_ENUM(){};
      public final static String _Vorhanden = "Vorhanden";
      public final static String _Keine = "Keine";

   }
                                    
}