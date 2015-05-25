/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:56 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Calltemplate
{
   private Calltemplate(){}

   // the name of the table alias	 
   public final static String NAME = "calltemplate";
	 
   // All field names of the table alias "calltemplate"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  action = "action";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  nextcreatedate = "nextcreatedate";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  autoclosed = "autoclosed";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  callbackmethod = "callbackmethod";
   
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
     type:     LONGTEXT<br>
   */
   public final static String  problemtext = "problemtext";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  customerint_key = "customerint_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  callworkgroup_key = "callworkgroup_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountcode_key = "accountcode_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  object_key = "object_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  location_key = "location_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  category_key = "category_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  repeatintervalunit = "repeatintervalunit";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  repeatinterval = "repeatinterval";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  error = "error";
   
	 
                    
   public final static class autoclosed_ENUM
   {
      private autoclosed_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

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
           
   public final static class priority_ENUM
   {
      private priority_ENUM(){};
      public final static String _1_Normal = "1-Normal";
      public final static String _2_Kritisch = "2-Kritisch";
      public final static String _3_Produktion = "3-Produktion";
      public final static String _4_Notfall = "4-Notfall";

   }
                                      
   public final static class repeatintervalunit_ENUM
   {
      private repeatintervalunit_ENUM(){};
      public final static String _taeglich = "täglich";
      public final static String _woechentlich = "wöchentlich";
      public final static String _monatlich = "monatlich";
      public final static String _jaehrlich = "jährlich";
      public final static String _keine = "keine";

   }
         
}