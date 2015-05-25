/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:48 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Accountcode
{
   private Accountcode(){}

   // the name of the table alias	 
   public final static String NAME = "accountcode";
	 
   // All field names of the table alias "accountcode"
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  code = "code";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  contract_key = "contract_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  accountingstatus = "accountingstatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  center = "center";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  department = "department";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  parent_key = "parent_key";
   
	 
              
   public final static class accountingstatus_ENUM
   {
      private accountingstatus_ENUM(){};
      public final static String _ungueltig = "ungültig";
      public final static String _gueltig = "gültig";

   }
            
}