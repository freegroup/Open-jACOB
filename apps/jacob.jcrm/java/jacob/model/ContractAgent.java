/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class ContractAgent
{
   private ContractAgent(){}

   // the name of the table alias	 
   public final static String NAME = "contractAgent";
	 
   // All field names of the table alias "contractAgent"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  loginname = "loginname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  employeeid = "employeeid";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  availability = "availability";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  firstname = "firstname";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  lastname = "lastname";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  title = "title";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  department = "department";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  building = "building";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  site = "site";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  location = "location";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fax = "fax";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pager = "pager";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  notifymethod = "notifymethod";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  ownerrole = "ownerrole";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sign_prefix = "sign_prefix";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  passwdhash = "passwdhash";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  discount_allowance = "discount_allowance";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  mobile = "mobile";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  manager_key = "manager_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  adminrole = "adminrole";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  productmanagerrole = "productmanagerrole";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  salesmanagerrole = "salesmanagerrole";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  salesagentrole = "salesagentrole";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  incidentrole = "incidentrole";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  callrole = "callrole";
   
   /** 
     required: false<br>
     type:     BOOLEAN<br>
   */
   public final static String  agentrole = "agentrole";
   
	 
                                                        
   public final static class notifymethod_ENUM
   {
      private notifymethod_ENUM(){};
      public final static String _Alert = "Alert";
      public final static String _Email = "Email";
      public final static String _Fax = "Fax";
      public final static String _Pager = "Pager";

   }
                                                
}