/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:56 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class QuoteContact
{
   private QuoteContact(){}

   // the name of the table alias	 
   public final static String NAME = "quoteContact";
	 
   // All field names of the table alias "quoteContact"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  mr_ms = "mr_ms";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  lastname = "lastname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  firstname = "firstname";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  title = "title";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  department = "department";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  phone = "phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fax = "fax";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  email = "email";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  notes = "notes";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  salutationheader = "salutationheader";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  salutation = "salutation";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  privatephone = "privatephone";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  checked = "checked";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  organization_key = "organization_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  origin_key = "origin_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  mobile = "mobile";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  position_key = "position_key";
   
	 
           
   public final static class mr_ms_ENUM
   {
      private mr_ms_ENUM(){};
      public final static String _Mr_ = "Mr.";
      public final static String _Mrs_ = "Mrs.";

   }
                                                        
   public final static class checked_ENUM
   {
      private checked_ENUM(){};
      public final static String _checked = "checked";
      public final static String _not_checked = "not checked";
      public final static String _to_delete = "to delete";

   }
                  
}