/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:31:00 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Quickcall
{
   private Quickcall(){}

   // the name of the table alias	 
   public final static String NAME = "quickcall";
	 
   // All field names of the table alias "quickcall"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
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
     type:     LONGTEXT<br>
   */
   public final static String  solutiontext = "solutiontext";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  calltype = "calltype";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callproduct_key = "callProduct_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  category_key = "category_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callworkgroup_key = "callWorkgroup_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  callowner_key = "callOwner_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  solution_key = "solution_key";
   
	 
                    
   public final static class calltype_ENUM
   {
      private calltype_ENUM(){};
      public final static String _Call = "Call";
      public final static String _Defect = "Defect";
      public final static String _Change_Request = "Change Request";

   }
                  
}