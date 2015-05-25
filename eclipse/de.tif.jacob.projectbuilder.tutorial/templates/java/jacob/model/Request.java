/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Jan 11 19:59:47 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Request
{
   private Request(){}

   // the name of the table alias	 
   public final static String NAME = "request";
	 
   // All field names of the table alias "request"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  state = "state";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  any_flag1 = "any_flag1";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  any_flag2 = "any_flag2";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  any_flag3 = "any_flag3";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  any_flag4 = "any_flag4";
   
	 
           
   public final static class state_Enumeration
   {
      private state_Enumeration(){};
      public final static String _open = "open";
      public final static String _closed = "closed";
      public final static String _progress = "progress";
      public final static String _rejected = "rejected";
      public final static String _ = "";

   }
                  
}