/**************************************************************************
 * Project  : jacob.lima
 * Date     : Tue Mar 07 14:04:46 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class License
{
   private License(){}

   // the name of the table alias	 
   public final static String NAME = "license";
	 
   // All field names of the table alias "license"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  creator_key = "creator_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  licensee_key = "licensee_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  engine_key = "engine_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  hash_key = "hash_key";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  user_count = "user_count";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  expiration_date = "expiration_date";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  is_demo = "is_demo";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  indefinite_flag = "indefinite_flag";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  modify_date = "modify_date";
   
	 
                                            
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _simple = "simple";
      public final static String _enhanced = "enhanced";

   }
      
}