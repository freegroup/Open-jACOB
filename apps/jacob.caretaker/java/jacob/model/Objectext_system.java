/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:57 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Objectext_system
{
   private Objectext_system(){}

   // the name of the table alias	 
   public final static String NAME = "objectext_system";
	 
   // All field names of the table alias "objectext_system"
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
   public final static String  connect_info = "connect_info";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  connect_user = "connect_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  connect_pwd = "connect_pwd";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  systemtype = "systemtype";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  systemstatus = "systemstatus";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  flushcategories = "flushcategories";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  fetchhwgdata = "fetchhwgdata";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  objectrequired = "objectrequired";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  initialstatus = "initialstatus";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  endstatus = "endstatus";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  allowcancel = "allowcancel";
   
	 
                       
   public final static class systemtype_ENUM
   {
      private systemtype_ENUM(){};
      public final static String _EDVIN = "EDVIN";
      public final static String _GDS = "GDS";
      public final static String _virtuell = "virtuell";
      public final static String _SAPiPRO = "SAPiPRO";

   }
           
   public final static class systemstatus_ENUM
   {
      private systemstatus_ENUM(){};
      public final static String _inaktiv = "inaktiv";
      public final static String _aktiv = "aktiv";

   }
           
   public final static class flushcategories_ENUM
   {
      private flushcategories_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class fetchhwgdata_ENUM
   {
      private fetchhwgdata_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class objectrequired_ENUM
   {
      private objectrequired_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
           
   public final static class initialstatus_ENUM
   {
      private initialstatus_ENUM(){};
      public final static String _Neu = "Neu";
      public final static String _Angelegt = "Angelegt";
      public final static String _Freigegeben = "Freigegeben";
      public final static String _In_Arbeit = "In Arbeit";

   }
           
   public final static class endstatus_ENUM
   {
      private endstatus_ENUM(){};
      public final static String _Fertig_gemeldet = "Fertig gemeldet";
      public final static String _Dokumentiert = "Dokumentiert";
      public final static String _Abgerechnet = "Abgerechnet";

   }
           
   public final static class allowcancel_ENUM
   {
      private allowcancel_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
   
}