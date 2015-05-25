/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Wed Aug 22 16:26:49 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Category
{
   private Category(){}

   // the name of the table alias	 
   public final static String NAME = "category";
	 
   // All field names of the table alias "category"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  categorylevel = "categorylevel";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  gdsalias = "gdsalias";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  categorystatus = "categorystatus";
   
   /** 
     required: false<br>
     type:     INTEGER<br>
   */
   public final static String  parentcategory_key = "parentcategory_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  department = "department";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  team = "team";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  responsible = "responsible";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datemodified = "datemodified";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  change_user = "change_user";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  longname = "longname";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  edvinalias = "edvinalias";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  synonyme = "synonyme";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  version = "version";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  locationrequired = "locationrequired";
   
	 
                    
   public final static class categorystatus_ENUM
   {
      private categorystatus_ENUM(){};
      public final static String _0_default = "0-default";
      public final static String _Gueltig = "Gültig";
      public final static String _Keine_Zuordnung = "Keine Zuordnung";
      public final static String _3_default = "3-default";
      public final static String _4_default = "4-default";
      public final static String _5_default = "5-default";
      public final static String _6_default = "6-default";
      public final static String _Ungueltig = "Ungültig";

   }
                                         
   public final static class locationrequired_ENUM
   {
      private locationrequired_ENUM(){};
      public final static String _Nein = "Nein";
      public final static String _Ja = "Ja";

   }
   
}