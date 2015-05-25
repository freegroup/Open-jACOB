/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Quoteheader
{
   private Quoteheader(){}

   // the name of the table alias	 
   public final static String NAME = "quoteheader";
	 
   // All field names of the table alias "quoteheader"
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
     required: true<br>
     type:     DATE<br>
   */
   public final static String  valid_to = "valid_to";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  discount_amount = "discount_amount";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  discount_fix = "discount_fix";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  discount_percent = "discount_percent";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  base_amount = "base_amount";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  position_amount = "position_amount";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  pos_discount_amount = "pos_discount_amount";
   
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
     required: true<br>
     type:     LONG<br>
   */
   public final static String  contact_key = "contact_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  agent_key = "agent_key";
   
	 
                 
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Open = "Open";
      public final static String _Calculated = "Calculated";
      public final static String _Released = "Released";
      public final static String _Closed = "Closed";
      public final static String _Canceled = "Canceled";

   }
                                       
}