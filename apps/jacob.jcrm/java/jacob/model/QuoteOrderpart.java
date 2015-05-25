/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class QuoteOrderpart
{
   private QuoteOrderpart(){}

   // the name of the table alias	 
   public final static String NAME = "quoteOrderpart";
	 
   // All field names of the table alias "quoteOrderpart"
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
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  uom = "uom";
   
   /** 
     required: true<br>
     type:     DECIMAL<br>
   */
   public final static String  price = "price";
   
	 
                    
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