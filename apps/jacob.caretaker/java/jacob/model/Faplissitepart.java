/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Sun Jun 08 15:42:20 CEST 2008
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Faplissitepart
{
   private Faplissitepart(){}

   // the name of the table alias	 
   public final static String NAME = "faplissitepart";
	 
   // All field names of the table alias "faplissitepart"
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  faplisstatus = "faplisstatus";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  site_key = "site_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  shortname = "shortname";
   
	 
              
   public final static class faplisstatus_ENUM
   {
      private faplisstatus_ENUM(){};
      public final static String _ungueltig = "ung�ltig";
      public final static String _gueltig = "g�ltig";

   }
         
}