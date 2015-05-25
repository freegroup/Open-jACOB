/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Salesproject
{
   private Salesproject(){}

   // the name of the table alias	 
   public final static String NAME = "salesproject";
	 
   // All field names of the table alias "salesproject"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  product_key = "product_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  projectnumber = "projectnumber";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  salesrep_key = "salesrep_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  chance = "chance";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  focus = "focus";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  estclosed = "estclosed";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateclosed = "dateclosed";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  prod_value_min = "prod_value_min";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  prod_value_max = "prod_value_max";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  prod_value_est = "prod_value_est";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  prod_value_act = "prod_value_act";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  srv_value_min = "srv_value_min";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  srv_value_max = "srv_value_max";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  srv_value_est = "srv_value_est";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  srv_value_act = "srv_value_act";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  reminder = "reminder";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  no_products = "no_products";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  contact_key = "contact_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
	 
                       
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _Opportunity = "Opportunity";
      public final static String _Prospect = "Prospect";
      public final static String _Suspect = "Suspect";
      public final static String _Lead = "Lead";

   }
           
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Open = "Open";
      public final static String _Sold = "Sold";
      public final static String _Lost = "Lost";

   }
              
   public final static class chance_ENUM
   {
      private chance_ENUM(){};
      public final static String _50_Percent = "50%";
      public final static String _60_Percent = "60%";
      public final static String _90_Percent = "90%";

   }
                                                         
}