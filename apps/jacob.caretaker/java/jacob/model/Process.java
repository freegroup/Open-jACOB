/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Wed Aug 22 16:27:00 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Process
{
   private Process(){}

   // the name of the table alias	 
   public final static String NAME = "process";
	 
   // All field names of the table alias "process"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  processname = "processname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  processstatus = "processstatus";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  process_key = "process_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  considerpriority = "considerpriority";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  synonyme = "synonyme";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  checkwarrenty = "checkwarrenty";
   
	 
                 
   public final static class processstatus_ENUM
   {
      private processstatus_ENUM(){};
      public final static String _0_default = "0-default";
      public final static String _Gueltig = "Gültig";
      public final static String _Nicht_zuordbar = "Nicht zuordbar";
      public final static String _Ungueltig = "Ungültig";

   }
               
}