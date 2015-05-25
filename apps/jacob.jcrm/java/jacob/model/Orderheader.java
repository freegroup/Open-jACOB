/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Orderheader
{
   private Orderheader(){}

   // the name of the table alias	 
   public final static String NAME = "orderheader";
	 
   // All field names of the table alias "orderheader"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  amount = "amount";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  quoteheader_key = "quoteheader_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  contact_key = "contact_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  contract_key = "contract_key";
   
	 
                 
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Open = "Open";
      public final static String _Confirmed = "Confirmed";
      public final static String _Released = "Released";
      public final static String _Closed = "Closed";
      public final static String _Canceled = "Canceled";

   }
                           
}