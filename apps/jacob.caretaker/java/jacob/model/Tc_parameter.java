/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:56 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Tc_parameter
{
   private Tc_parameter(){}

   // the name of the table alias	 
   public final static String NAME = "tc_parameter";
	 
   // All field names of the table alias "tc_parameter"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  orderprocess_key = "orderProcess_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  callcategory_key = "callCategory_key";
   
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  advisoryprocess_key = "advisoryProcess_key";
   
   /** 
     required: false<br>
     type:     TIME<br>
   */
   public final static String  forecast_sendtime = "forecast_sendtime";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  forecast_day_criteria = "forecast_day_criteria";
   
	 
                  
}