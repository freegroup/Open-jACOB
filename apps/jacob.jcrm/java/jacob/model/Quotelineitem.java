/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Quotelineitem
{
   private Quotelineitem(){}

   // the name of the table alias	 
   public final static String NAME = "quotelineitem";
	 
   // All field names of the table alias "quotelineitem"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  quantity = "quantity";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  price = "price";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  discount_percent = "discount_percent";
   
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
   public final static String  position_amount = "position_amount";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  baseprice = "baseprice";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  uom = "uom";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  quoteheader_key = "quoteheader_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  orderpart_key = "orderpart_key";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
	 
                                       
}