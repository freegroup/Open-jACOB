/**************************************************************************
 * Project  : jacob.email
 * Date     : Tue Nov 20 10:36:06 CET 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class CommonContact
{
   private CommonContact(){}

   // the name of the table alias	 
   public final static String NAME = "commonContact";
	 
   // All field names of the table alias "commonContact"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  fullname = "fullname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  lastname = "lastname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  firstname = "firstname";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  person_key = "person_key";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: false<br>
     type:     DOCUMENT<br>
   */
   public final static String  image = "image";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  work_email = "work_email";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  work_phone = "work_phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  work_fax = "work_fax";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  privat_email = "privat_email";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  private_phone = "private_phone";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  private_fax = "private_fax";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  mandator_id = "mandator_id";
   
	 
                       
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _privat = "privat";
      public final static String _common = "common";

   }
                           
}