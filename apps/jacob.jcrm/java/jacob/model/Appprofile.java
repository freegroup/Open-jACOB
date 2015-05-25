/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:55 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Appprofile
{
   private Appprofile(){}

   // the name of the table alias	 
   public final static String NAME = "appprofile";
	 
   // All field names of the table alias "appprofile"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_chance1 = "so_chance1";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_chance2 = "so_chance2";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_chance3 = "so_chance3";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  so_value1 = "so_value1";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  so_value2 = "so_value2";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  so_value3 = "so_value3";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_type1 = "so_type1";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_type2 = "so_type2";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  so_type3 = "so_type3";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  so_delay1 = "so_delay1";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  so_delay2 = "so_delay2";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  so_delay3 = "so_delay3";
   
	 
           
   public final static class so_chance1_ENUM
   {
      private so_chance1_ENUM(){};
      public final static String _50_Percent = "50%";
      public final static String _60_Percent = "60%";
      public final static String _90_Percent = "90%";

   }
           
   public final static class so_chance2_ENUM
   {
      private so_chance2_ENUM(){};
      public final static String _50_Percent = "50%";
      public final static String _60_Percent = "60%";
      public final static String _90_Percent = "90%";

   }
           
   public final static class so_chance3_ENUM
   {
      private so_chance3_ENUM(){};
      public final static String _50_Percent = "50%";
      public final static String _60_Percent = "60%";
      public final static String _90_Percent = "90%";

   }
                    
   public final static class so_type1_ENUM
   {
      private so_type1_ENUM(){};
      public final static String _Opportunity = "Opportunity";
      public final static String _Prospect = "Prospect";
      public final static String _Suspect = "Suspect";
      public final static String _Lead = "Lead";

   }
           
   public final static class so_type2_ENUM
   {
      private so_type2_ENUM(){};
      public final static String _Opportunity = "Opportunity";
      public final static String _Prospect = "Prospect";
      public final static String _Suspect = "Suspect";
      public final static String _Lead = "Lead";

   }
           
   public final static class so_type3_ENUM
   {
      private so_type3_ENUM(){};
      public final static String _Opportunity = "Opportunity";
      public final static String _Prospect = "Prospect";
      public final static String _Suspect = "Suspect";
      public final static String _Lead = "Lead";

   }
            
}