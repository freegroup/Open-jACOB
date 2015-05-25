/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:57 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Saplog
{
   private Saplog(){}

   // the name of the table alias	 
   public final static String NAME = "saplog";
	 
   // All field names of the table alias "saplog"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  ttscallid = "ttscallid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sapssleid = "sapssleid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sapsmid = "sapsmid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  modus = "modus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  message = "message";
   
   /** 
     required: true<br>
     type:     TIMESTAMP<br>
   */
   public final static String  datecreated = "datecreated";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  errorstatus = "errorstatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  saptaskid = "saptaskid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  ttstaskid = "ttstaskid";
   /** 
   required: false<br>
   type:     TEXT<br>
 */
 public final static String  object = "object";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  detail = "detail";
   
	 
                                 
}