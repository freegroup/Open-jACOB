/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:53 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Tasktype
{
   private Tasktype(){}

   // the name of the table alias	 
   public final static String NAME = "tasktype";
	 
   // All field names of the table alias "tasktype"
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  taskcode = "taskcode";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  tasktypestatus = "tasktypestatus";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  costtype = "costtype";
   
	 
              
   public final static class tasktypestatus_ENUM
   {
      private tasktypestatus_ENUM(){};
      public final static String _ungueltig = "ungültig";
      public final static String _gueltig = "gültig";

   }
      
}