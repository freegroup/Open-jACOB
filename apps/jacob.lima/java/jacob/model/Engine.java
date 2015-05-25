/**************************************************************************
 * Project  : jacob.lima
 * Date     : Tue Mar 07 14:04:46 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Engine
{
   private Engine(){}

   // the name of the table alias	 
   public final static String NAME = "engine";
	 
   // All field names of the table alias "engine"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  version = "version";
   
   /** 
     required: true<br>
     type:     DOCUMENT<br>
   */
   public final static String  public_key = "public_key";
   
   /** 
     required: true<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  create_date = "create_date";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  engine_creator_key = "engine_creator_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  license_type = "license_type";
   
	 
                          
   public final static class license_type_ENUM
   {
      private license_type_ENUM(){};
      public final static String _simple = "simple";
      public final static String _enhanced = "enhanced";

   }
   
}