/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:31:00 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Solution
{
   private Solution(){}

   // the name of the table alias	 
   public final static String NAME = "solution";
	 
   // All field names of the table alias "solution"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  problem = "problem";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  solnstatus = "solnstatus";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  summary = "summary";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  solntext = "solntext";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  document = "document";
   
	 
              
   public final static class solnstatus_ENUM
   {
      private solnstatus_ENUM(){};
      public final static String _Draft = "Draft";
      public final static String _Reviewed = "Reviewed";
      public final static String _Published = "Published";
      public final static String _Retired = "Retired";

   }
            
}