/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:57 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Sapadmin
{
   private Sapadmin(){}

   // the name of the table alias	 
   public final static String NAME = "sapadmin";
	 
   // All field names of the table alias "sapadmin"
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
   required: true<br>
   type:     TEXT<br>
 */
 public final static String  imp_max_recs = "imp_max_recs";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
  
   public final static String  maxconnections = "maxconnections";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  sap_client = "sap_client";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  sap_user = "sap_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pwd = "pwd";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  language = "language";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  host = "host";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  sytemid = "sytemid";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  active = "active";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  ext_system_key = "ext_system_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  obj_preview_records = "obj_preview_records";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  ext_systemsaptask_key = "ext_systemsaptask_key";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  objectheader = "objectheader";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  sapdebug = "sapdebug";
   /** 
   required: true<br>
   type:     TIMESTAMP<br>
   */
   public final static String  lastobj_job_exe = "lastobj_job_exe";   
   
   /** 
   required: true<br>
   type:     Text<br>
   */
   public final static String  sap_faplissite_key = "sap_faplissite_key";
   
                          
   public final static class language_ENUM
   {
      private language_ENUM(){};
      public final static String _DE = "DE";
      public final static String _EN = "EN";
      public final static String _FR = "FR";
      public final static String _ES = "ES";

   }
                           
}