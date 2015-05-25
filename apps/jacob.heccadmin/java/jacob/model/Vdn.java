/**************************************************************************
 * Project  : jacob.heccadmin
 * Date     : Tue Apr 28 17:06:40 CEST 2009
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * 
 *
 * Condition: <b></b>
 * DB table: <b>vdn</b>
 *
 **/
public final class Vdn
{
   private Vdn(){}

   // the name of the table alias	 
   public final static String NAME = "vdn";
	 
   // All field names of the table alias "vdn"
   /** 
    * The VDN number<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  vdnnumber = "vdnnumber";
   
   /** 
    * The VDN name<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  vdnname = "vdnname";
   
   /** 
    * The application to open on screen pops<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  application = "application";
   
   /** 
    * The number of the menu htat lead to this VDN<br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  menuoption = "menuoption";
   
   /** 
    * Whether or not this VDN should have a SAC check enabled<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  sacline = "sacline";
   
   /** 
    * Whether or not there is a support limitation on this VDN<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  supportlimitation = "supportlimitation";
   
   /** 
    * The amount with which the call limit should be increased once<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  sacincrease = "sacincrease";
   
   /** 
    * Whether or not there is an emergency (e.g. high volume) on the VDN<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  emergency = "emergency";
   
   /** 
    * Whether or not the customer search should be enabled<br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  disablecustomersearch = "disablecustomersearch";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  active = "active";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  line_key = "line_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  slapercentage = "slapercentage";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  slaseconds = "slaseconds";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  hanguptime = "hanguptime";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  outboundcode = "outboundcode";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  useinstats = "useinstats";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  allowvdnoverride = "allowvdnoverride";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  cor = "cor";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  vector = "vector";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  measured = "measured";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  originvdn = "originvdn";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  skill1 = "skill1";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  skill2 = "skill2";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  skill3 = "skill3";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  vdnsearch = "vdnsearch";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  history = "history";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  changed_by = "changed_by";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  change_date = "change_date";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  created_by = "created_by";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  create_date = "create_date";
   
	 
                    
   public final static class sacline_ENUM
   {
      private sacline_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
           
   public final static class supportlimitation_ENUM
   {
      private supportlimitation_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
              
   public final static class emergency_ENUM
   {
      private emergency_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
           
   public final static class disablecustomersearch_ENUM
   {
      private disablecustomersearch_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
           
   public final static class active_ENUM
   {
      private active_ENUM(){};
      public final static String _active = "active";
      public final static String _inactive = "inactive";

   }
                          
   public final static class useinstats_ENUM
   {
      private useinstats_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
           
   public final static class allowvdnoverride_ENUM
   {
      private allowvdnoverride_ENUM(){};
      public final static String _no = "no";
      public final static String _yes = "yes";

   }
                                          
  /**
   * Create a new Record within the current DataAccessor of the Context with a new transaction
   **/
  public static IDataTableRecord newRecord(Context context) throws Exception
  {
    return newRecord(context,context.getDataAccessor().newTransaction());
  }

  /**
   * Create a new Record within the current DataAccessor of the Context and the handsover
   * transaction.
   **/
  public static IDataTableRecord newRecord(Context context, IDataTransaction trans) throws Exception
  {
    return newRecord(context.getDataAccessor(),trans);
  }

  /**
   * Create a new Record within the hands over DataAccessor and a new transaction.
   **/
  public static IDataTableRecord newRecord(IDataAccessor acc) throws Exception
  {
    return acc.getTable(NAME).newRecord(acc.newTransaction());
  }

  /**
   * Create a new Record within the hands over DataAccessor and transaction.
   **/
  public static IDataTableRecord newRecord(IDataAccessor acc, IDataTransaction trans) throws Exception
  {
    return acc.getTable(NAME).newRecord(trans);
  }
}