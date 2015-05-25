/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:57 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Tc_notifyee
{
   private Tc_notifyee(){}

   // the name of the table alias	 
   public final static String NAME = "tc_notifyee";
	 
   // All field names of the table alias "tc_notifyee"
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
	 
              
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _aktiv = "aktiv";
      public final static String _inaktiv = "inaktiv";

   }
   
}