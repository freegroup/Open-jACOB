/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Mailinstore
{
   private Mailinstore(){}

   // the name of the table alias	 
   public final static String NAME = "mailinstore";
	 
   // All field names of the table alias "mailinstore"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  protocol = "protocol";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  host = "host";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  user = "user";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  password = "password";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  folder = "folder";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  port = "port";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  htmlcontentmode = "htmlcontentmode";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  interval = "interval";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  workgroup_key = "workgroup_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  maxattachmentsize = "maxattachmentsize";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  skipunknowncontacts = "skipunknowncontacts";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  delete_inaccessible = "delete_inaccessible";
   
	 
                             
   public final static class htmlcontentmode_ENUM
   {
      private htmlcontentmode_ENUM(){};
      public final static String _skip = "skip";
      public final static String _attach = "attach";

   }
           
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _inactive = "inactive";
      public final static String _active = "active";

   }
                     
}