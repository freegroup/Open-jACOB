/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class ContractOrganization
{
   private ContractOrganization(){}

   // the name of the table alias	 
   public final static String NAME = "contractOrganization";
	 
   // All field names of the table alias "contractOrganization"
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
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  address = "address";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  city = "city";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  zip = "zip";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  country = "country";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fax = "fax";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pobox = "pobox";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  pozip = "pozip";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  emp_count = "emp_count";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  turnover = "turnover";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  founded = "founded";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  product_in_use = "product_in_use";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  checked = "checked";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  orgorigin_key = "orgorigin_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  branch_key = "branch_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  porganization_key = "porganization_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  orgowner_key = "orgowner_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  primarycontact_key = "primaryContact_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  webpage = "webpage";
   
	 
                                                  
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _Lead = "Lead";
      public final static String _Suspect = "Suspect";
      public final static String _Prospect = "Prospect";
      public final static String _Opportunity = "Opportunity";
      public final static String _Customer = "Customer";
      public final static String _Consultant = "Consultant";
      public final static String _Partner = "Partner";
      public final static String _Supplier = "Supplier";
      public final static String _Press = "Press";
      public final static String _Not_Active = "Not Active";

   }
              
   public final static class emp_count_ENUM
   {
      private emp_count_ENUM(){};
      public final static String _1_10 = "1-10";
      public final static String _11_50 = "11-50";
      public final static String _51_100 = "51-100";
      public final static String _101_200 = "101-200";
      public final static String _201_500 = "201-500";
      public final static String _501_1000 = "501-1000";
      public final static String _1001_or_more = "1001 or more";

   }
                    
   public final static class checked_ENUM
   {
      private checked_ENUM(){};
      public final static String _checked = "checked";
      public final static String _not_checked = "not checked";
      public final static String _to_delete = "to delete";

   }
                     
}