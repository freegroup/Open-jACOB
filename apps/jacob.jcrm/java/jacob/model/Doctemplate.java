/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Doctemplate
{
   private Doctemplate(){}

   // the name of the table alias	 
   public final static String NAME = "doctemplate";
	 
   // All field names of the table alias "doctemplate"
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
     type:     DOCUMENT<br>
   */
   public final static String  template = "template";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  doctype = "doctype";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  extension = "extension";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  usedin = "usedin";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  contact_req = "contact_req";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  salesproject_req = "salesproject_req";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  product_key = "product_key";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  contracttype = "contracttype";
   
	 
                 
   public final static class doctype_ENUM
   {
      private doctype_ENUM(){};
      public final static String _Brochure = "Brochure";
      public final static String _Quote = "Quote";
      public final static String _Letter = "Letter";
      public final static String _Fax = "Fax";
      public final static String _RFP = "RFP";
      public final static String _Contract_Blank = "Contract Blank";
      public final static String _Contract = "Contract";
      public final static String _Other = "Other";

   }
              
   public final static class usedin_ENUM
   {
      private usedin_ENUM(){};
      public final static String _Activity = "Activity";
      public final static String _Contract = "Contract";
      public final static String _Quote = "Quote";
      public final static String _Order = "Order";

   }
                    
   public final static class contracttype_ENUM
   {
      private contracttype_ENUM(){};
      public final static String _License = "License";
      public final static String _Service = "Service";
      public final static String _Consultancy = "Consultancy";
      public final static String _Maintenance = "Maintenance";
      public final static String _Master_Agreement = "Master Agreement";

   }
   
}