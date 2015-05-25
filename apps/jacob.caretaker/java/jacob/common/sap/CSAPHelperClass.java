/**
 * 
 */
package jacob.common.sap;

import jacob.common.AppLogger;
import jacob.model.Sapadmin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;

/**
 * @author e050_fwt-ant_o_test
 * 
 */
public class CSAPHelperClass
{
  private static final Logger fileLogger = Logger.getLogger(CSAPHelperClass.class.getName());
  private static boolean useFileLogger = false;

  private static final transient Log commonLogger = AppLogger.getLogger();
  
  private static final int FILE_COUNT = 50;
  private static final int FILE_MAX_SIZE = 16 * 1024 *1024;
  
  static
  {
    try
    {
      
      final String logFile = Bootstrap.getApplicationRootPath() + "../../logs/csap.log";

      File testfile = new File(logFile);
      // wirft eine Exception sofern der Pfad nicht existiert
      testfile.createNewFile();
      
      // erst alle handler (catalina.out) entfernen
      Handler[] myhandler = fileLogger.getHandlers();
      for (int i = 0; i < myhandler.length; i++)
      {
        fileLogger.removeHandler(myhandler[i]);
      }
      // Filelogger hinzufügen
      FileHandler fh = new FileHandler(logFile, FILE_MAX_SIZE, FILE_COUNT);
      fh.setFormatter(new LogFormatter());
      // fh.setFormatter(new SimpleFormatter());
      fileLogger.addHandler(fh);
      fileLogger.setLevel(Level.FINEST);
      // damit es nicht noch zusätzlich auf die console (catalina.out) geht
      // den LogRecord nicht weiterreichen
      fileLogger.setUseParentHandlers(false);

      commonLogger.info("CSAPHelperClass Logfile '" + logFile + "' initialisiert.");
      
      // alles hat geklappt -> logger freischalten
      useFileLogger = true;
    }
    catch (Exception ex)
    {
      commonLogger.warn("CSAPHelperClass Logfile failed: " + ex.toString());
    }
  }

  public final static class LogFormatter extends Formatter
  {
    static public final transient String RCS_ID = "";
    static public final transient String RCS_REV = "";

    transient private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SSS");

    public final String format(final LogRecord record)
    {
      return date.format(new Date(record.getMillis())) + /* " " + record.getLevel().toString() + */ ": " + this.formatMessage(record) + "\n";
    }
  }
  
  /**
   * Enthält Hilfsfunktionen für die SAP Schnittstele
   */
  public CSAPHelperClass()
  {
    // No Constructor
  }

  /**
   * Schreibt die InputParameter für einen RFC Call nach StdOut Überprüft, ob
   * das Debug Flag gesezt ist, und läuft nur dann!
   * 
   * @param oRFCInParam,
   *          Map, Die Inputparameter, die an den RFC Call übergeben werden
   * 
   * @exception UserException
   */
  public static void WriteSAPInputParam(Map oRFCInParam, String message)
  {
    if (CSAPHelperClass.isDebug())
    {
      Iterator oiterparam = oRFCInParam.entrySet().iterator();

      while (oiterparam.hasNext())
      {
        Map.Entry oentry = (Map.Entry) oiterparam.next();
        printDebug(message + oentry.getKey() + "  " + oentry.getValue());
      } // while
    }// if
    return;
  }
  public static void WriteSAPOutputParam(Map oData, String message)
  {
    if (CSAPHelperClass.isDebug())
    {
      Iterator oiter = oData.entrySet().iterator();
      while (oiter.hasNext())
      {
        Map.Entry oentry = (Map.Entry) oiter.next();
        // sBuf.append(oentry.getKey() + " " + oentry.getValue()+"\\r\\n");
        printDebug(message + oentry.getKey() + "  " + oentry.getValue());
      } // while}
    }// if

    return;
  }

  /**
   * Hilfsmethode zur Überprüfung, ob das DebugFlag im SAP Administrationsrecord
   * gesetzt ist
   * 
   * @exception Exception
   */
  public static boolean checkDebug() throws Exception
  {
    final Context oCtxt = Context.getCurrent();
    final IDataTable oSapAdm = oCtxt.getDataTable(Sapadmin.NAME);
    oSapAdm.qbeClear();
    oSapAdm.qbeSetValue(Sapadmin.active, "1");
    oSapAdm.search();
    if (oSapAdm.recordCount() == 1)
    {
      IDataTableRecord rec = oSapAdm.getRecord(0);
      if (rec.getintValue(Sapadmin.sapdebug) == 1)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Hilfsmethode zur Überprüfung, ob das DebugProperty für den aktuellen job
   * gesetzt ist
   * 
   * 
   * @exception Exception
   */
  public static boolean isDebug()
  {
    if (null == (Context.getCurrent().getProperty("SAPdebug")) || "0" == (Context.getCurrent().getProperty("SAPdebug"))) //
    {
      return false;
    }

    return true;
  }

  /**
   * Hilfsmethode zur Ausgabe der Debugwerte
   * 
   */
  public static void printDebug(String message)
  {
    if (isDebug())
    {
      if (useFileLogger)
        fileLogger.fine(message);
      else
        System.out.println(message);
    }
  }

  /**
   * Hilfsmethode zur Ausgabe der In die SapLoggerDatei
   * 
   */
  public static void printIntoSaplog(String message)
  {

  
      if (useFileLogger)
        fileLogger.fine(message);
      else
        System.out.println(message);

  }
}