/**************************************************************************
 * Project  : jacob.FWT-Anwendungen
 * Date     : Wed May 10 21:09:33 CEST 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Myaccess
{
   private Myaccess(){}

   // the name of the table alias	 
   public final static String NAME = "myaccess";
	 
   // All field names of the table alias "myaccess"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  loginname = "loginname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  employeeid = "employeeid";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  availability = "availability";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  department = "department";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  privatephone = "privatephone";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  supportrole = "supportrole";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phonecorr = "phonecorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  buildingcorr = "buildingcorr";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  employeetype = "employeetype";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  firstnamecorr = "firstnamecorr";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  lastnamecorr = "lastnamecorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  emailcorr = "emailcorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  faxcorr = "faxcorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  mobilecorr = "mobilecorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sitepartcorr = "sitepartcorr";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  employeestatus = "employeestatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  accountingcodecorr = "accountingcodecorr";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  pm_role = "pm_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  ak_role = "ak_role";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  hpccorr = "hpccorr";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  warte_role = "warte_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  sdadmin_role = "sdadmin_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  admin_role = "admin_role";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  emplsite_keycorr = "emplsite_keycorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  roomcorr = "roomcorr";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  userid = "userid";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  superak_role = "superak_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  bp_role = "bp_role";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  lpr_ip = "lpr_ip";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  mbtech_role = "mbtech_role";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  passwdhash = "passwdhash";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  pqp_role = "pqp_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  prj_role = "prj_role";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  net_role = "net_role";
   
	 
                                      
   public final static class employeetype_Enumeration
   {
      private employeetype_Enumeration(){};
      public final static String _Intern = "Intern";
      public final static String _Extern = "Extern";

   }
                             
   public final static class employeestatus_Enumeration
   {
      private employeestatus_Enumeration(){};
      public final static String _DBCS = "DBCS";
      public final static String _von_Hand = "von Hand";
      public final static String _DBCS_Gelöscht = "DBCS Gelöscht";
      public final static String _von_Hand_gelöscht = "von Hand gelöscht";

   }
                                                         
}