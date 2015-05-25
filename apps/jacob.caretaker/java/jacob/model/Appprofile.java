/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:48 CEST 2007
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
     * required: true<br>
     * type: INTEGER<br>
     */
   public final static String  pkey = "pkey";
   
   /**
     * required: true<br>
     * type: TEXT<br>
     */
   public final static String  faxprintdir = "faxprintdir";
   
   /**
     * required: true<br>
     * type: INTEGER<br>
     */
   public final static String  problemmanager_key = "problemmanager_key";
   
   /**
     * required: true<br>
     * type: TEXT<br>
     */
   public final static String  knowledgebase = "knowledgebase";
   
   /**
     * required: true<br>
     * type: INTEGER<br>
     */
   public final static String  contract_key = "contract_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  callcloseduration = "callcloseduration";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  checkobjcategory = "checkobjcategory";
   
   /**
     * required: true<br>
     * type: INTEGER<br>
     */
   public final static String  callworkgroup_key = "callworkgroup_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  gradinginterval = "gradinginterval";
   
   /**
     * required: true<br>
     * type: ENUM<br>
     */
   public final static String  ak_is_owner = "ak_is_owner";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  defaultxsl = "defaultxsl";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  faxdefaultxsl = "faxdefaultxsl";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  smsdefaultxsl = "smsdefaultxsl";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  employee_key = "employee_key";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  custfeedbacklogin = "custfeedbacklogin";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  mediatorroot = "mediatorroot";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  sequenceroot = "sequenceroot";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  webqagent_key = "webqagent_key";
   
   /**
     * required: false<br>
     * type: TIMESTAMP<br>
     */
   public final static String  last_edvinresolved = "last_edvinresolved";
   
   /**
     * required: false<br>
     * type: TIMESTAMP<br>
     */
   public final static String  last_filescan = "last_filescan";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  interfaceadmin_key = "interfaceadmin_key";
   
   /**
     * required: false<br>
     * type: ENUM<br>
     */
   public final static String  interfacechanel = "interfacechanel";
   
   /**
     * required: true<br>
     * type: INTEGER<br>
     */
   public final static String  warrentyend = "warrentyend";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  wprocess_key = "wprocess_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  warrentyagent_key = "warrentyagent_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  wmanager_key = "wmanager_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  mbtechgroup_key = "mbtechgroup_key";
   
   /**
     * required: false<br>
     * type: INTEGER<br>
     */
   public final static String  mbtechsystem_key = "mbtechsystem_key";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  filescanpath = "filescanpath";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
   public final static String  filescanlogfile = "filescanlogfile";
   
   /**
     * required: false<br>
     * type: TEXT<br>
     */
 public final static String  callclosedelay = "callclosedelay";	 
 
 /**
   * required: false<br>
   * type: TEXT<br>
   */
public final static String  calldokdelay = "calldokdelay";    
 
 
   public final static class ak_is_owner_ENUM
   {
      private ak_is_owner_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
                                            
   public final static class interfacechanel_ENUM
   {
      private interfacechanel_ENUM(){};
      public final static String _Email = "Email";
      public final static String _Signal = "Signal";

   }
                           
}