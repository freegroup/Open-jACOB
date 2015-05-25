/**************************************************************************
 * Project  : jacob.audittracker
 * Date     : Tue Apr 10 21:35:36 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Group
{
   private Group(){}

   // the name of the table alias	 
   public final static String NAME = "group";
	 
   // All field names of the table alias "group"
   /** 
     required: true<br>
     type:     LONG<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: true<br>
     type:     TEXT<br>
   */
   public final static String  name = "name";
   
   /** 
     required: false<br>
     type:     LONG<br>
   */
   public final static String  parent_group_key = "parent_group_key";
   
	 
         
}