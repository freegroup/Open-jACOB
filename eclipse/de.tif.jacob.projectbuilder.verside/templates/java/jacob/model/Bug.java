/**************************************************************************
 * Project  : jacob.{applicationName}
 * Date     : Wed Oct 11 11:02:26 CEST 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Bug
{
   private Bug(){}

   // the name of the table alias	 
   public final static String NAME = "bug";
	 
   // All field names of the table alias "bug"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  application = "application";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  title = "title";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  state = "state";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  grade = "grade";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  reproduceabitlity = "reproduceabitlity";
   
	 
                    
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _verified = "verified";
      public final static String _closed = "closed";

   }
           
   public final static class grade_ENUM
   {
      private grade_ENUM(){};
      public final static String _Missing_Functionality = "Missing Functionality";
      public final static String _Remark = "Remark";
      public final static String _Cosmetic = "Cosmetic";
      public final static String _Crash = "Crash";
      public final static String _Wrong_Behaviour = "Wrong Behaviour";
      public final static String _Bug = "Bug";

   }
           
   public final static class reproduceabitlity_ENUM
   {
      private reproduceabitlity_ENUM(){};
      public final static String _Once = "Once";
      public final static String _Sometimes = "Sometimes";
      public final static String _Often = "Often";
      public final static String _Always = "Always";

   }
   
}