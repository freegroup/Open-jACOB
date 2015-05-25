/**************************************************************************
 * Project  : jacob.qualitymaster
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
 * Mandatorsicht auf die Milestones
 *
 * Condition: <b>milestoneEntry.organization_key={MANDATORID}</b>
 * DB table: <b>milestone</b>
 *
 **/
public final class MilestoneEntry
{
   private MilestoneEntry(){}

   // the name of the table alias    
   public final static String NAME = "milestoneEntry";
     
   // All field names of the table alias "milestoneEntry"
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
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  history = "history";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>TEXT</b><br>
    **/
   public final static String  subject = "subject";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONGTEXT</b><br>
    **/
   public final static String  description = "description";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  actualdone = "actualdone";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>DATE</b><br>
    **/
   public final static String  plandone = "plandone";
   
   /** 
    * <br>
    * <br>
    * required: <b>true</b><br>
    * type:     <b>ENUM</b><br>
    **/
   public final static String  state = "state";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>LONG</b><br>
    **/
   public final static String  report_template_key = "report_template_key";
   
   /** 
    * <br>
    * <br>
    * required: <b>false</b><br>
    * type:     <b>INTEGER</b><br>
    **/
   public final static String  organization_key = "organization_key";
   
     
                          
   public final static class state_ENUM
   {
      private state_ENUM(){};
      public final static String _Red = "Red";
      public final static String _Yellow = "Yellow";
      public final static String _Green = "Green";
      public final static String _Done = "Done";
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