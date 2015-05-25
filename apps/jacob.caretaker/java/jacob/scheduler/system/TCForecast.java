/*
 * Created on 15.10.2004
 * by mike
 *
 */
package jacob.scheduler.system;

import jacob.common.tc.NoActiveCampaignException;
import jacob.common.tc.TC;
import jacob.model.Tc_notifyee;
import jacob.model.Tc_parameter;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.exception.UserException;
//import de.tif.jacob.messaging.Message;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.TransformerFactory;
import electric.proxy.IProxy;
import electric.registry.Registry;

/**
 * Versenden von TC Vorschauberichten.
 * 
 * @author Andreas Sonntag
 */
public final class TCForecast extends SchedulerTaskSystem
{
  static public final transient String RCS_ID = "$Id: TCForecast.java,v 1.3 2008/09/25 16:17:51 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private static final Logger logger = Logger.getLogger(TCForecast.class.getName());

  private static final String MIME_TYPE = "application/excel";
  private static final String FILE_NAME = "Vorschau.xls";
  
  private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm' Uhr'");
  
  private static long lastForecastTimeMillis = System.currentTimeMillis();

  // Start the task every minute
  //
  private final ScheduleIterator iterator = new MinutesIterator(1);

  public ScheduleIterator iterator()
  {
    return iterator;
  }

  public void run(TaskContextSystem context) throws Exception
  {
    IDataTableRecord parameterRecord = TC.getParameterRecord(context.getDataAccessor());
    Time forecastTime = parameterRecord.getTimeValue(Tc_parameter.forecast_sendtime);
    
    // keine Vorschauuhrzeit eingetragen -> abbrechen
    if (forecastTime == null)
      return;
    
    // Versendezeit an diesem Tag berechnen
    //
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, forecastTime.getHours());
    calendar.set(Calendar.MINUTE, forecastTime.getMinutes());
    calendar.set(Calendar.SECOND, forecastTime.getSeconds());
    calendar.set(Calendar.MILLISECOND, 0);
    
    // kein Versand am Wochenende
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
      return;
    
    long newForecastTimeMillis = calendar.getTimeInMillis();
    
    // Versenden nur wenn Versendezeit nicht in der Vergangenzeit liegt und
    // nicht schon ein Versand zu diesen Zeitpunkt stattgefunden hat
    //
    if (newForecastTimeMillis <= System.currentTimeMillis() && newForecastTimeMillis > lastForecastTimeMillis)
    {
      lastForecastTimeMillis = newForecastTimeMillis;
      
      String datetime = DATETIME_FORMAT.format(new Date(newForecastTimeMillis));
      
      try
      {
        IDataTable table = context.getDataAccessor().getTable(Tc_notifyee.NAME);
        table.qbeClear();
        table.qbeSetValue(Tc_notifyee.status, Tc_notifyee.status_ENUM._aktiv);
        if (table.search() == 0)
        {
          logger.info("Keine Vorschau-Empfänger vorhanden");
          return;
        }

        byte[] excelData = createExcelData(context, parameterRecord.getStringValue(Tc_parameter.forecast_day_criteria));
        if (excelData == null)
        {
          logger.info("Keine Vorschau-Daten vorhanden");
          return;
        }

        String body = "Räderwechsel Vorschau vom " + datetime + "\nDie Vorschau wurde als Anhang an diese Mail angefügt..";

        for (int i = 0; i < table.recordCount(); i++)
        {
          IDataTableRecord notifyeeRec = table.getRecord(i);
          String emailAddress = notifyeeRec.getStringValue(Tc_notifyee.email);
          // TODO: MIGRATION: neue Implementierung notwendig
//          send("email://" + emailAddress, body, excelData, FILE_NAME, MIME_TYPE);
          logger.info("TC-Vorschau an " + emailAddress + " gesendet");
        }
      }
      catch (NoActiveCampaignException ex)
      {
        // ignore
      }
    }
  }

  public static void show(IClientContext context) throws Exception
  {
    IDataTableRecord parameterRecord = TC.getParameterRecord(context.getDataAccessor());
    byte[] excelData = createExcelData(context, parameterRecord.getStringValue(Tc_parameter.forecast_day_criteria));
    if (excelData == null)
    {
      throw new UserException("Keine Vorschau-Daten vorhanden");
    }
    context.createDocumentDialog(MIME_TYPE, FILE_NAME, excelData).show();
  }

  /**
   * Achtung: Diese Methode muß bei der Umstellung auf jACOB 2.6 entfallen!
   */
  // TODO: MIGRATION: neu implementrieren
  /*
  private static void send(String toUrl, String message, byte[] attachment, String fileName, String mimeType) throws Exception
  {
    try
    {
      try
      {
        // obtain a proxy to the web service with the specified WSDL
        IProxy proxy = Registry.bind(Property.YAN_WSDL.getValue());

        // dynamically invoke exchange service using array of Strings
        proxy.invoke("send", new Object[]
          { toUrl, message, attachment, fileName, mimeType });
      }
      catch (electric.registry.RegistryException re)
      {
        throw re.getCause() == null ? re : re.getCause();
      }
    }
    catch (Exception ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      throw new Exception(ex);
    }
  }
  */

  /**
   * Achtung: Diese Methode muß bei der Umstellung auf jACOB 2.6 aktiviert
   * werden!
   */
//  private static void sendNew(String sendUrl, String message, byte[] attachment, String fileName, String mimeType) throws Exception
//  {
//    Message msg = new Message(sendUrl, message);
//    if (mimeType.equals("application/excel") || mimeType.equals("application/vnd.ms-excel"))
//    {
//      msg.addAttachment("application/vnd.ms-excel", fileName, attachment);
//    }
//    else
//    {
//      msg.addAttachment(mimeType, fileName, attachment);
//    }
//    msg.setSender(null);
//    msg.setSenderAddr(null);
//    msg.send();
//  }

  public boolean hibernatedOnSchedule()
  {
    return false;
  }

  private static byte[] createExcelData(Context context, String dayCriteria) throws Exception, NoActiveCampaignException
  {
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context.getDataAccessor());

    // retrieve capacity records
    //
    // Achtung: Da Oracle 8 scheinbar keine mehrstufigen OUTER JOINS kann,
    // werden erst alle Kapazitäten ohne Buchungsinfos (capacity0Browser)
    // und anschließend alle Kapazitäten mit Buchungen geholt
    // (capacityBrowser).
    //
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTable capacityTable = accessor.getTable("tc_capacity");
    capacityTable.qbeClear();
    capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
    capacityTable.qbeSetValue("slot", dayCriteria);
    IDataBrowser capacity0Browser = accessor.getBrowser("tc_capacityOrderList0Browser");
    capacity0Browser.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    capacity0Browser.search("r_tc_capacity_platform");

    // check whether this date is alright
    //
    if (capacity0Browser.recordCount() == 0)
    {
      return null;
    }

    // Map(IDataKeyValue->IDataBrowserRecord)
    Map bookedCapacities = new HashMap();
    IDataBrowser capacityBrowser = accessor.getBrowser("tc_capacityOrderListBrowser");
    capacityBrowser.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    capacityBrowser.search("r_tc_platform");
    for (int j = 0; j < capacityBrowser.recordCount(); j++)
    {
      IDataBrowserRecord browserRec = capacityBrowser.getRecord(j);
      bookedCapacities.put(browserRec.getPrimaryKeyValue(), browserRec);
    }

    IBrowserDefinition browserDef = accessor.getApplication().getBrowserDefinition(capacityBrowser.getName());
    List browserFields = browserDef.getBrowserFields();
    int browserFieldSize = browserFields.size();
    IBrowserDefinition browser0Def = accessor.getApplication().getBrowserDefinition(capacity0Browser.getName());
    int browser0FieldSize = browser0Def.getBrowserFields().size();

    // build excel header
    //
    // Achtung: Hier wird noch eine zusätzliche Spalte eingefügt, um den
    // Slot-Zeitstempel in
    // Datum und Uhrzeit zu trennen.
    //
    final int ADDITIONAL_COLUMNS = 1;
    int columnNumber = browserFields.size() + ADDITIONAL_COLUMNS;
    String[] header = new String[columnNumber];
    for (int i = 0; i < browserFields.size(); i++)
    {
      IBrowserField browserField = (IBrowserField) browserFields.get(i);
      switch (i)
      {
        case 0:
          header[0] = browserField.getLabel();
          break;
        case 1:
          header[1] = "Datum";
          header[2] = "Uhrzeit";
          break;
        default:
          header[i + ADDITIONAL_COLUMNS] = browserField.getLabel();
          break;
      }
    }

    // build excel data
    //
    Locale locale = context.getApplicationLocale();
    String[][] data = new String[capacity0Browser.recordCount()][columnNumber];
    for (int j = 0; j < capacity0Browser.recordCount(); j++)
    {
      IDataBrowserRecord browserRec = capacity0Browser.getRecord(j);
      IDataBrowserRecord bookedRec = (IDataBrowserRecord) bookedCapacities.get(browserRec.getPrimaryKeyValue());
      for (int i = 0; i < browser0FieldSize; i++)
      {
        String value = browserRec.getSaveStringValue(i, locale);
        switch (i)
        {
          case 0:
            data[j][i] = value;
            break;
          case 1:
            // separate date: "16.04.2007 12:45" -> "16.04.2007","12:45"
            int pos = value.indexOf(" ");
            data[j][1] = value.substring(0, pos);
            data[j][2] = value.substring(pos + 1);
            break;
          default:
            data[j][i + ADDITIONAL_COLUMNS] = value;
            break;
        }
      }
      for (int i = browser0FieldSize; i < browserFieldSize; i++)
      {
        if (bookedRec == null)
        {
          data[j][i + ADDITIONAL_COLUMNS] = (i == browserFieldSize - 1) ? "nicht gebucht" : "";
        }
        else
        {
          data[j][i + ADDITIONAL_COLUMNS] = bookedRec.getSaveStringValue(i, locale);
        }
      }
    }

    // create excel data
    //
    ByteArrayOutputStream stream = new ByteArrayOutputStream(128 * capacity0Browser.recordCount() + 1024);
    ITransformer trans = TransformerFactory.get(MIME_TYPE);
    trans.transform(stream, header, data);
    stream.close();

    return stream.toByteArray();
  }

}
