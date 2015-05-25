/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:51 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Objaccountingcode
{
   private Objaccountingcode(){}

   // the name of the table alias	 
   public final static String NAME = "objaccountingcode";
	 
   // All field names of the table alias "objaccountingcode"
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