/**************************************************************************
 * Project  : jacob.caretaker
 * Date     : Tue Apr 29 17:52:09 CEST 2008
 * 
 * THIS IS A GENERATED FILE - DO NOT CHANGE!
 *
 *************************************************************************/
package jacob.model;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * 
 *
 * Condition: <b></b>
 * DB table: <b>contract</b>
 *
 **/
public final class Contract
{
   private Contract(){}

   // the name of the table alias	 
   public final static String NAME = "contract";
	 
   // All field names of the table alias "contract"
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  pkey = "pkey";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  spechandlecontract = "spechandlecontract";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  contractnum = "contractnum";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  startdate = "startdate";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  expirationdate = "expirationdate";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  contractstatus = "contractstatus";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  duration = "duration";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  ponumber = "ponumber";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  servicelevel = "servicelevel";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  entitlementtype = "entitlementtype";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  bot = "bot";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  botwarnlevel = "botwarnlevel";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  bop = "bop";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  bopwarnlevel = "bopwarnlevel";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  warnmessage = "warnmessage";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  allowcredit = "allowcredit";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  timetorespond = "timetorespond";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  timetoclose = "timetoclose";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TIMESTAMP</b><br>
    **/
   public final static String  datemodified = "datemodified";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  modifiedby = "modifiedby";
   
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
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  notes = "notes";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  description = "description";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  security = "security";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  allowedto = "allowedto";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  sla_rla = "sla_rla";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  smc_customer_key = "smc_customer_key";
   
	 
                       
   public final static class contractstatus_ENUM
   {
      private contractstatus_ENUM(){};
      public final static String _Warten = "Warten";
      public final static String _aktiv = "aktiv";
      public final static String _beendet = "beendet";

   }
           
   public final static class duration_ENUM
   {
      private duration_ENUM(){};
      public final static String _30_Tage = "30 Tage";
      public final static String _60_Tage = "60 Tage";
      public final static String _90_Tage = "90 Tage";
      public final static String _6_Monate = "6 Monate";
      public final static String _1_Jahr = "1 Jahr";
      public final static String _2_Jahre = "2 Jahre";
      public final static String _3_Jahre = "3 Jahre";
      public final static String _4_Jahre = "4 Jahre";
      public final static String _5_Jahre = "5 Jahre";
      public final static String _unbefristet = "unbefristet";

   }
              
   public final static class servicelevel_ENUM
   {
      private servicelevel_ENUM(){};
      public final static String _4_Stunden = "4 Stunden";
      public final static String _24_Stunden = "24 Stunden";
      public final static String _7x24_Stunden = "7x24 Stunden";
      public final static String _standard = "standard";
      public final static String _spezial = "spezial";

   }
           
   public final static class entitlementtype_ENUM
   {
      private entitlementtype_ENUM(){};
      public final static String _BOP = "BOP";
      public final static String _BOT = "BOT";
      public final static String _Gebuehr = "Gebühr";
      public final static String _unbegrenzt = "unbegrenzt";

   }
                                                  
   public final static class security_ENUM
   {
      private security_ENUM(){};
      public final static String _01___keine = "01 - keine";
      public final static String _02___keine_wenn___800_DM_pro_einkauf__personlich_ = "02 - keine wenn < 800 DM pro einkauf (personlich)";
      public final static String _03___angabe_des_TAN___codes_zur_authentifizierung__schriftlich_ = "03 - angabe des TAN - codes zur authentifizierung (schriftlich)";
      public final static String _04___Angabe_des_TAN___codes_zur_authentifizierung__mundlich_ = "04 - Angabe des TAN - codes zur authentifizierung (mundlich)";
      public final static String _05___genehmigung_iv___koordinator__schriftlich__mit_angabe_des_dateneigners_stellvertreters = "05 - genehmigung iv - koordinator (schriftlich) mit angabe des dateneigners/stellvertreters";
      public final static String _06___genehmigung_iv___koordinator__schriftlich_ = "06 - genehmigung iv - koordinator (schriftlich)";
      public final static String _07___genehmigung_iv___koordinator__schriftlich_met_begruendung_ = "07 - genehmigung iv - koordinator (schriftlich met begründung)";
      public final static String _08___genehmigung_iv___koordinator__mundlich_ = "08 - genehmigung iv - koordinator (mundlich)";
      public final static String _09___genehmigung_des_dateneigners__schriftlich_ = "09 - genehmigung des dateneigners (schriftlich)";
      public final static String _10___genehmigung_des_kostenstellenverantwortlichen__schriftlich_ = "10 - genehmigung des kostenstellenverantwortlichen (schriftlich)";

   }
           
   public final static class allowedto_ENUM
   {
      private allowedto_ENUM(){};
      public final static String _1___anwender = "1-  anwender";
      public final static String _2___anwender_iv___koordinator = "2 - anwender/iv - koordinator";
      public final static String _3___iv___koordinator = "3 - iv - koordinator";

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