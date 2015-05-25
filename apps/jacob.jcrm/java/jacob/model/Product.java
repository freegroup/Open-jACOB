/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:50 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Product
{
   private Product(){}

   // the name of the table alias	 
   public final static String NAME = "product";
	 
   // All field names of the table alias "product"
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
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  productnumber = "productnumber";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  contractprefix = "contractprefix";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  manager_key = "manager_key";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  callproduct = "callproduct";
   
	 
                    
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _Future = "Future";
      public final static String _Available = "Available";
      public final static String _Stopped = "Stopped";
      public final static String _Rejected = "Rejected";

   }
            
}