/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Orderlineitem
{
   private Orderlineitem(){}

   // the name of the table alias	 
   public final static String NAME = "orderlineitem";
	 
   // All field names of the table alias "orderlineitem"
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
   public final static String  position_amount = "position_amount";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  uom = "uom";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  orderheader_key = "orderheader_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  orderpart_key = "orderpart_key";
   
	 
                           
}