/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Wed Aug 22 20:32:43 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Quickcalls
{
   private Quickcalls(){}

   // the name of the table alias	 
   public final static String NAME = "quickcalls";
	 
   // All field names of the table alias "quickcalls"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  problem = "problem";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  origin = "origin";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  problemtext = "problemtext";
   
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
     type:     INTEGER<br>
   */
   public final static String  category_key = "category_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  callworkgroup_key = "callworkgroup_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  callbackmethod = "callbackmethod";
   
	 
                 
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
                             
   public final static class callbackmethod_ENUM
   {
      private callbackmethod_ENUM(){};
      public final static String _Keine = "Keine";
      public final static String _SMS = "SMS";
      public final static String _Telefon = "Telefon";
      public final static String _Email = "Email";
      public final static String _FAX = "FAX";

   }
   
}