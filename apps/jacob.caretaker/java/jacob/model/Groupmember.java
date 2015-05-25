/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:51 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Groupmember
{
   private Groupmember(){}

   // the name of the table alias	 
   public final static String NAME = "groupmember";
	 
   // All field names of the table alias "groupmember"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  employeegroup = "employeegroup";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  workgroupgroup = "workgroupgroup";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  notifymethod = "notifymethod";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  tier = "tier";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  accessallowed = "accessallowed";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  xsl_stylesheet = "xsl_stylesheet";
   
	 
                 
   public final static class notifymethod_ENUM
   {
      private notifymethod_ENUM(){};
      public final static String _Signal = "Signal";
      public final static String _Email = "Email";
      public final static String _Funkruf = "Funkruf";
      public final static String _FAX = "FAX";
      public final static String _Keine = "Keine";

   }
              
   public final static class accessallowed_ENUM
   {
      private accessallowed_ENUM(){};
      public final static String _lesen = "lesen";
      public final static String _lesen_schreiben = "lesen/schreiben";

   }
      
}