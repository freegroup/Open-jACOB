package jacob.common.tc;

import jacob.common.data.DataUtils;
import jacob.event.data.Tc_orderTableRecord;
import jacob.exception.BusinessException;
import jacob.model.Object;
import jacob.model.Tc_campaign;
import jacob.model.Tc_parameter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.util.StringUtil;

public class TC
{
  static public final transient String RCS_ID = "$Id: TC.java,v 1.8 2008/04/29 07:51:44 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  /**
   * Version since the new chart implementation exists
   * <p>
   * Note: Since jACOB version 2.6 we have an own chart implementation which
   * does not have the bug anymore
   */
  private static final Version NEW_CHART_VERSION = new Version(2, 6);
  
  private static final boolean OLD_CHARTS = Version.ENGINE.compareTo(NEW_CHART_VERSION) <0;
  
  /**
   * Adjust 0 to a very small value due to bar chart bug.
   * 
   * @param value
   * @return
   */
  public static double adjustHack(int value)
  {
    if (value == 0 && OLD_CHARTS)
      return 0.01;
    return value;
  }
  
  public static String getParameter(IClientContext context, String fieldName) throws Exception
  {
    return getParameterRecord(context.getDataAccessor()).getStringValue(fieldName);
  }

  public static String getParameter(IDataAccessor accessor, String fieldName) throws Exception
  {
    return getParameterRecord(accessor).getStringValue(fieldName);
  }

  public static IDataTableRecord getParameterRecord(IDataAccessor accessor) throws Exception
  {
    IDataTable table = accessor.getTable(Tc_parameter.NAME);

    // the one and only record is already there?
    IDataTableRecord parameterRecord = table.getSelectedRecord();
    if (null == parameterRecord)
    {
      table.qbeClear();
      if (table.search() != 1)
      {
        throw new BusinessException("Es wurden noch nicht alle Parameter konfiguriert. Administrator informieren!");
      }
      parameterRecord = table.getRecord(0);
    }
    return parameterRecord;
  }

  public static IDataTableRecord getActiveCampagne(IClientContext context) throws Exception, NoActiveCampaignException
  {
    // create new accessor to avoid destroying displayed data
    return getActiveCampagne(context.getDataAccessor().newAccessor());
  }

  /**
   * Returns the one and only active campagne.
   * 
   * @param accessor
   * @return
   * @throws Exception on any error
   * @throws NoActiveCampaignException if no active campagne exists
   */
  public static IDataTableRecord getActiveCampagne(IDataAccessor accessor) throws Exception, NoActiveCampaignException
  {
    IDataTable table = accessor.getTable(Tc_campaign.NAME);
    table.qbeClear();
    table.qbeSetValue(Tc_campaign.active, "!0");
    int res = table.search();
    if (res == 0)
      throw new NoActiveCampaignException("Es ist derzeit keine aktive Kampagne vorhanden.");
    if (res > 1)
    {
      // should never occur
      throw new NoActiveCampaignException("Es sind mehrere aktive Kampagnen vorhanden. Administrator informieren!");
    }
    return table.getSelectedRecord();
  }

  private static class AskDescriptionCallback implements IAskDialogCallback
  {
    private final String processKey;

    private AskDescriptionCallback(String processKey)
    {
      this.processKey = processKey;
    }

    public void onCancel(IClientContext context)
    {
    }

    public void onOk(IClientContext context, String description) throws Exception
    {
      askCoordtimeCallback(context, new AskCoordtimeCallback(description, processKey));
    }
  }

  private static class AskCoordtimeCallback implements IAskDialogCallback
  {
    private final String description;
    private final String processKey;
    private final IDataTableRecord callRecord;

    private AskCoordtimeCallback(String description, String processKey)
    {
      this.description = description;
      this.processKey = processKey;
      this.callRecord = null;
    }

    private AskCoordtimeCallback(IDataTableRecord callRecord)
    {
      this.description = null;
      this.processKey = null;
      this.callRecord = callRecord;
    }

    /* Do noting if the users cancel the AskDialog */
    public void onCancel(IClientContext context)
    {
    }

    /* Called if the user press [ok] in the AskDialog */
    public void onOk(IClientContext context, String coordtime) throws Exception
    {
      // String in Integer umwandeln
      //
      int minutes;
      try
      {
        minutes = Integer.parseInt(StringUtil.toSaveString(coordtime).trim());
      }
      catch (NumberFormatException e)
      {
        // Noch mal versuchen!
        askCoordtimeCallback(context, this);
        return;
      }

      IDataAccessor accessor = context.getDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        if (this.callRecord != null)
        {
          // nur Koordinationszeit im Call setzen
          setCoordMinutes(this.callRecord, transaction, minutes);
        }
        else
        {
          // kompletten Call mit Koordinationszeit erzeugen
          createFirstLevelCall(context, transaction, description, processKey, minutes);
        }
        transaction.commit();
      }
      finally
      {
        transaction.close();
      }
    }
  }

  public static void createAdvisoryCall(IClientContext context) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    String processKey = getParameter(accessor, "advisoryProcess_key");
    IAskDialog dialog = context.createAskDialog("Wie soll die Ticket-Beschreibung lauten?", "Beratung Räderwechsel", new AskDescriptionCallback(processKey));
    dialog.show();
  }

  public static IDataTableRecord createOrderCall(Context context, IDataTransaction transaction, Timestamp slot) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    IDataTableRecord objectRec = accessor.getTable("tc_object").getSelectedRecord();

    // Beschreibung und Tätigkeit bestimmen
    //
    StringBuffer description = new StringBuffer();
    description.append("Terminbuchung Räderwechsel");
    if (objectRec != null)
    {
      description.append(" für ");
      description.append(objectRec.getStringValue("name"));
      if (slot != null)
        description.append(datetimeFormatter.format(slot));
    }

    String processKey = getParameter(accessor, "orderProcess_key");

    return createFirstLevelCall(context, transaction, description.toString(), processKey, 0);
  }

  public static IDataTableRecord createStornoCall(Context context, IDataTransaction transaction, Timestamp slot) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();
    IDataTableRecord objectRec = accessor.getTable("tc_object").getSelectedRecord();

    // Beschreibung und Tätigkeit bestimmen
    //
    StringBuffer description = new StringBuffer();
    description.append("Terminstornierung Räderwechsel");
    if (objectRec != null)
    {
      description.append(" für ");
      description.append(objectRec.getStringValue("name"));
      if (slot != null)
        description.append(datetimeFormatter.format(slot));
    }

    String processKey = getParameter(accessor, "orderProcess_key");

    return createFirstLevelCall(context, transaction, description.toString(), processKey, 0);
  }
  
  public static boolean requestAndSetCoordinationTime(IClientContext context)
  {
    IDataTableRecord callRecord = Tc_orderTableRecord.getOrderTicket(context);
    if (callRecord != null)
    {
      askCoordtimeCallback(context, new AskCoordtimeCallback(callRecord));
      return true;
    }
    return false;
  }

  private static final DateFormat datetimeFormatter = new SimpleDateFormat(" 'am' dd.MM.yy', um' HH:mm 'Uhr'");

  private static void askCoordtimeCallback(IClientContext context, AskCoordtimeCallback callback)
  {
    IAskDialog dialog = context.createAskDialog("Wie groß war der Koordinationsaufwand in Minuten?", "0", callback);
    dialog.show();
  }

  private static IDataTableRecord createFirstLevelCall(Context context, IDataTransaction transaction, String description, String processKey, int coordMinutes) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor();

    IDataTableRecord callRec = accessor.getTable("call").newRecord(transaction);

    // Gewerk setzen
    callRec.setValue(transaction, "categorycall", getParameter(accessor, "callCategory_key"));

    // Den Kunden als Melder und betroffene Person setzen
    //
    IDataTableRecord customerRec = accessor.getTable("tc_customer").getSelectedRecord();
    if (customerRec == null)
      throw new BusinessException("Kein Kunde vorhanden");
    callRec.setValue(transaction, "employeecall", customerRec.getValue("pkey"));
    callRec.setValue(transaction, "affectedperson_key", customerRec.getOldValue("pkey"));
    callRec.setValue(transaction, "affectedperson", customerRec.getStringValue("fullname") + " Tel: " + customerRec.getSaveStringValue("phonecorr"));

    // Das Objekt falls vorhanden setzen
    IDataTableRecord objectRec = accessor.getTable("tc_object").getSelectedRecord();
    if (objectRec != null)
    {
      callRec.setValue(transaction, "object_key", objectRec.getValue("pkey"));
    }

    // Beschreibung und Tätigkeit setzen
    //
    callRec.setStringValueWithTruncation(transaction, "problem", description.toString());
    callRec.setValue(transaction, "process_key", processKey);

    // Den Agenten setzen
    callRec.setValue(transaction, "agentcall", context.getUser().getKey());

    // SD als AK setzen
    //
    String SD_Key = DataUtils.getAppprofileValue(context, "callworkgroup_key");
    DataUtils.linkTable(context, transaction, callRec, "workgroupcall", "callworkgroup", "pkey", SD_Key);

    // Koordinationsaufwand speichern
    //
    setCoordMinutes(callRec, transaction, coordMinutes);

    if (callRec.getValue("datereported") == null)
    {
      callRec.setValue(transaction, "datereported", "now");
    }
    if (callRec.getValue("dateassigned") == null)
    {
      callRec.setValue(transaction, "dateassigned", "now");
    }
    if (callRec.getValue("dateowned") == null)
    {
      callRec.setValue(transaction, "dateowned", "now");
    }
    if (callRec.getValue("dateresolved") == null)
    {
      callRec.setValue(transaction, "dateresolved", "now");
    }
    if (callRec.getValue("callbackmethod") == null)
    {
      callRec.setValue(transaction, "callbackmethod", "Keine");
    }

    callRec.setValue(transaction, "callstatus", "Dokumentiert");
    callRec.setValue(transaction, "datedocumented", "now");
    // Flag setzen firstlevelclosedcall
    callRec.setValue(transaction, "firstlevelclosed", "1");
    
    return callRec;
  }
  
  private static void setCoordMinutes(IDataTableRecord callRec, IDataTransaction transaction, int coordMinutes) throws Exception
  {
    callRec.setLongValue(transaction, "coordinationtime", coordMinutes * 60);
    callRec.setLongValue(transaction, "coordinationtime_h", coordMinutes / 60);
    callRec.setLongValue(transaction, "coordinationtime_m", coordMinutes % 60);
  }
}
