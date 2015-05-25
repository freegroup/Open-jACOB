/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:53 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Taskobject
{
   private Taskobject(){}

   // the name of the table alias	 
   public final static String NAME = "taskobject";
	 
   // All field names of the table alias "taskobject"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  external_id = "external_id";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  objstatus = "objstatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  assettag = "assettag";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  ext_system_key = "ext_system_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  location_key = "location_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  objectcategory_key = "objectcategory_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountingcode_key = "accountingcode_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  vendor = "vendor";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  change_user = "change_user";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  priority = "priority";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  warranty_begin = "warranty_begin";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  warranty_end = "warranty_end";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  warranty_extension = "warranty_extension";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  warranty_controled = "warranty_controled";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  supplierid = "supplierid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  object_above = "object_above";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  specification = "specification";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  cmbtnumber = "cmbtnumber";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  hwtype = "hwtype";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  standard = "standard";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  hwswinfo = "hwswinfo";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  objectowner_key = "objectowner_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sap_old_edivin_status = "sap_old_edivin_status";
   
	 
                 
   public final static class objstatus_ENUM
   {
      private objstatus_ENUM(){};
      public final static String _0_Default = "0-Default";
      public final static String _in_Betrieb = "in Betrieb";
      public final static String _in_Reparatur = "in Reparatur";
      public final static String _außer_Betrieb = "außer Betrieb";
      public final static String _im_Depot = "im Depot";
      public final static String _verschrottet = "verschrottet";
      public final static String _geloescht = "gelöscht";

   }
                                      
   public final static class priority_ENUM
   {
      private priority_ENUM(){};
      public final static String _1_Normal = "1-Normal";
      public final static String _2_Kritisch = "2-Kritisch";
      public final static String _3_Produktion = "3-Produktion";

   }
                                   
   public final static class hwtype_ENUM
   {
      private hwtype_ENUM(){};
      public final static String _Laptop = "Laptop";
      public final static String _Desktop = "Desktop";
      public final static String _Drucker = "Drucker";
      public final static String _Sonstiges = "Sonstiges";

   }
           
   public final static class standard_ENUM
   {
      private standard_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
            
}