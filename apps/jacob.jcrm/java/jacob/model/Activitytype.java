/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Activitytype
{
   private Activitytype(){}

   // the name of the table alias	 
   public final static String NAME = "activitytype";
	 
   // All field names of the table alias "activitytype"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  contact_req = "contact_req";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  project_req = "project_req";
   
	 
               
}