/**************************************************************************
 * Project  : jacob.jcrm
 * Date     : Mon Nov 13 11:30:55 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Reminderalert
{
   private Reminderalert(){}

   // the name of the table alias	 
   public final static String NAME = "reminderalert";
	 
   // All field names of the table alias "reminderalert"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  addressee = "addressee";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  tablename = "tablename";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  tablekey = "tablekey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  message = "message";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  dateposted = "dateposted";
   
	 
                  
}