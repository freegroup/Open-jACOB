/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:57 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Sapcallexchange
{
   private Sapcallexchange(){}

   // the name of the table alias	 
   public final static String NAME = "sapcallexchange";
	 
   // All field names of the table alias "sapcallexchange"
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
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  action = "action";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  type = "type";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_ak = "imp_ak";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_description = "imp_description";
   
   /** 
     required: false<br>
     type:     LONGTEXT<br>
   */
   public final static String  imp_longtext = "imp_longtext";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_location = "imp_location";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_equipment = "imp_equipment";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_techplatz = "imp_techplatz";
   
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
   public final static String  message = "message";
   
   /** 
     required: true<br>
     type:     ENUM<br>
   */
   public final static String  status = "status";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_priority = "imp_priority";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_callstatus = "imp_callstatus";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  exc_table = "exc_table";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  saptaskid = "saptaskid";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  ttstaskpkey = "ttstaskpkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  ttstaskno = "ttstaskno";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_callenddate = "imp_callenddate";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_callendtime = "imp_callendtime";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_costcenter = "imp_costcenter";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_probstartdate = "imp_t_probstartdate";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_probenddate = "imp_t_probenddate";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_probstarttime = "imp_t_probstarttime";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_probendtime = "imp_t_probendtime";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_datereleased = "imp_t_datereleased";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_datetechclosed = "imp_t_datetechclosed";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_dateclosed = "imp_t_dateclosed";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_totaltimespent = "imp_t_totaltimespent";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  imp_t_tottimesp_uom = "imp_t_tottimesp_uom";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  imp_t_hwg = "imp_t_hwg";
   
   /** 
   required: false<br>
   type:     TEXT<br>
 */
 public final static String  sl = "sl";
                    
   public final static class action_ENUM
   {
      private action_ENUM(){};
      public final static String _create = "create";
      public final static String _update = "update";
      public final static String _delete = "delete";
      public final static String _import = "import";
      public final static String _close = "close";
      public final static String _writeback = "writeback";

   }
           
   public final static class type_ENUM
   {
      private type_ENUM(){};
      public final static String _import = "import";
      public final static String _export = "export";

   }
                                      
   public final static class status_ENUM
   {
      private status_ENUM(){};
      public final static String _ToDo = "ToDo";
      public final static String _Done = "Done";

   }
                 
   public final static class exc_table_ENUM
   {
      private exc_table_ENUM(){};
      public final static String _call = "call";
      public final static String _task = "task";

   }
                                                     
   public final static class imp_t_tottimesp_uom_ENUM
   {
      private imp_t_tottimesp_uom_ENUM(){};
      public final static String _H = "H";
      public final static String _MIN = "MIN";

   }
      
}