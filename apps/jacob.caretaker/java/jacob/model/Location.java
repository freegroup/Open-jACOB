/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Aug 14 14:46:51 CEST 2007
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

public final class Location
{
   private Location(){}

   // the name of the table alias	 
   public final static String NAME = "location";
	 
   // All field names of the table alias "location"
   /** 
     required: true<br>
     type:     INTEGER<br>
   */
   public final static String  pkey = "pkey";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  site_key = "site_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  sitepart_key = "sitepart_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  building_key = "building_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  buildingpart_key = "buildingpart_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  room_key = "room_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  plane_key = "plane_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  floor_key = "floor_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  buildpartobj_key = "buildpartobj_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  gdsbaxis_key = "gdsbaxis_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  gdszaxis_key = "gdszaxis_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  baxis_key = "baxis_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  zaxis_key = "zaxis_key";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  description = "description";
   
   /** 
     required: false<br>
     type:     TEXT<br>
   */
   public final static String  note = "note";
   
   /** 
     required: false<br>
     type:     ENUM<br>
   */
   public final static String  orientation = "orientation";
   
	 
                                                     
   public final static class orientation_ENUM
   {
      private orientation_ENUM(){};
      public final static String _N = "N";
      public final static String _NO = "NO";
      public final static String _O = "O";
      public final static String _SO = "SO";
      public final static String _S = "S";
      public final static String _SW = "SW";
      public final static String _W = "W";
      public final static String _NW = "NW";

   }
   
}