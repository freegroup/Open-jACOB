/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Chartconfig
{
   private Chartconfig(){}

   // the name of the table alias	 
   public final static String NAME = "chartconfig";
	 
   // All field names of the table alias "chartconfig"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  header = "header";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  incidentdisplay = "incidentdisplay";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  activitydisplay = "activitydisplay";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  linechartmonth = "linechartmonth";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showincident = "showincident";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showactivity = "showactivity";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showquote = "showquote";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showorder = "showorder";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showcalls = "showcalls";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  showdefects = "showdefects";
   
	 
              
   public final static class incidentdisplay_ENUM
   {
      private incidentdisplay_ENUM(){};
      public final static String _Type = "Type";
      public final static String _Status = "Status";

   }
           
   public final static class activitydisplay_ENUM
   {
      private activitydisplay_ENUM(){};
      public final static String _Type = "Type";
      public final static String _Status = "Status";

   }
                        
}