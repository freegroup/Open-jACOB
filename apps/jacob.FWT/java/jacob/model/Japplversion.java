/**************************************************************************
 * Project  : jacob.FWT-Anwendungen
 * Date     : Wed May 10 21:09:33 CEST 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Japplversion
{
   private Japplversion(){}

   // the name of the table alias	 
   public final static String NAME = "japplversion";
	 
   // All field names of the table alias "japplversion"
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  version = "version";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  buildby = "buildby";
   
   /** 
     required: false<br>
     type:     TIMESTAMP<br>
   */
   public final static String  builddatetime = "builddatetime";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  installedsince = "installedsince";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  buildmachine = "buildmachine";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: true<br>
     type:     BINARY<br>
   */
   public final static String  content = "content";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  contentsize = "contentsize";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  installseqnbr = "installseqnbr";
   
	 
                       
   public final static class status_Enumeration
   {
      private status_Enumeration(){};
      public final static String _inactive = "inactive";
      public final static String _productive = "productive";
      public final static String _test = "test";

   }
                  
}