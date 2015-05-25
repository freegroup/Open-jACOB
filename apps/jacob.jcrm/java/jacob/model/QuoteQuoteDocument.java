/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class QuoteQuoteDocument
{
   private QuoteQuoteDocument(){}

   // the name of the table alias	 
   public final static String NAME = "quoteQuoteDocument";
	 
   // All field names of the table alias "quoteQuoteDocument"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  activity_key = "activity_key";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  docname = "docname";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  doctype = "doctype";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  creator = "creator";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: true<br>
     type:     DOCUMENT<br>
   */
   public final static String  docfile = "docfile";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  incident_key = "incident_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  salesproject_key = "salesproject_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  quoteheader_key = "quoteheader_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  orderheader_key = "orderheader_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  contract_key = "contract_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  call_key = "call_key";
   
	 
                 
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
                              
}