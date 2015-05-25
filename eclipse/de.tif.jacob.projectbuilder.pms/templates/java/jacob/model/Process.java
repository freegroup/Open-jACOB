/**************************************************************************
 * Project  : jacob.ProcessManagementSystem
 * Date     : Tue Oct 31 12:08:25 CET 2006
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Process
{
   private Process(){}

   // the name of the table alias	 
   public final static String NAME = "process";
	 
   // All field names of the table alias "process"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  valid_from = "valid_from";
   
   /** 
     required: false<br>
     type:     DATE<br>
   */
   public final static String  valid_to = "valid_to";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  title = "title";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  tags = "tags";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  remark = "remark";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  responsible_remark = "responsible_remark";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  aspect = "aspect";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  process_responsible_key = "process_responsible_key";
   
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  process_owner_key = "process_owner_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  process_owner_substitute_key = "process_owner_substitute_key";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  parent_process_key = "parent_process_key";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  guideline = "guideline";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  risk_evaluation = "risk_evaluation";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  case_of_need = "case_of_need";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  history = "history";
   
	 
                                                   
}