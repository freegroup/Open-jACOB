/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Contract
{
   private Contract(){}

   // the name of the table alias	 
   public final static String NAME = "contract";
	 
   // All field names of the table alias "contract"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  contractnumber = "contractnumber";
   
   /** 
     required: true<br>
     type:     DATE<br>
   */
   public final static String  startdate = "startdate";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  expirationdate = "expirationdate";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  allowcredit = "allowcredit";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  modifiedby = "modifiedby";
   
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
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  autorenewal = "autorenewal";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  cancellationdate = "cancellationdate";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  product_key = "product_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  technicalcontact_key = "technicalcontact_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  financialcontact_key = "financialcontact_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  mastercontract_key = "mastercontract_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  owner_key = "owner_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  salesproject_key = "salesproject_key";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  specialagreement = "specialagreement";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  amount = "amount";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  list_price = "list_price";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  maintenancepercent = "maintenancepercent";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  maintenancemode = "maintenancemode";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  uom = "uom";
   
   /** 
     required: false<br>
     type:     DECIMAL<br>
   */
   public final static String  unit_price = "unit_price";
   
	 
                    
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Draft = "Draft";
      public final static String _Pending = "Pending";
      public final static String _Active = "Active";
      public final static String _Expired = "Expired";
      public final static String _Cancelled = "Cancelled";

   }
                                
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _License = "License";
      public final static String _Service = "Service";
      public final static String _Consultancy = "Consultancy";
      public final static String _Maintenance = "Maintenance";
      public final static String _Master_Agreement = "Master Agreement";

   }
                                                        
   public final static class maintenancemode_ENUM
   {
      private maintenancemode_ENUM(){};
      public final static String _Contract_amount = "Contract amount";
      public final static String _List_price = "List price";

   }
           
   public final static class uom_ENUM
   {
      private uom_ENUM(){};
      public final static String _Piece = "Piece";
      public final static String _Meter = "Meter";
      public final static String _Inch = "Inch";
      public final static String _Liter = "Liter";
      public final static String _KG = "KG";

   }
      
}