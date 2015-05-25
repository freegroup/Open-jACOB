/*
 * Created on 15.10.2004
 * by mike
 *
 */
package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.config.Config;
import jacob.exception.BusinessException;
import jacob.model.Appprofile;
import jacob.scheduler.system.filescan.castor.JobType;
import jacob.scheduler.system.filescan.castor.TTSEnvelopeType;
import jacob.scheduler.system.filescan.castor.types.EventTypeEnum;
import jacob.scheduler.system.logger.LogFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;
import de.tif.jacob.util.file.Directory;

/**
 * @author mike
 *  
 */
/**
 * Implementierung der Schnittstelle zum Hochregallager Team Bernstein
 * 
 * @author mike
 *  
 */
public class Filescan extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: Filescan.java,v 1.17 2007/05/31 16:00:28 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.17 $";
  
	// Konstanten für Hashmap und Feldnamen
	public static final String OBJECT = "object_key";
	public static final String WORKGROUP = "workgroupcall";
	public static final String AGENT = "agentcall";
	public static final String CUSTOMER = "employeecall";
	public static final String PROCESS = "process_key";
	public static final String CATEGORY = "categorycall";
	public static final String PROBLEM = "problem";
	public static final String DATE = "date";
	public static final String STATUS = "callstatus";
	public static final String AFFECTEDPERSON = "affectedperson";
	transient private static final SimpleDateFormat DateTime = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");

  private static final transient Log commonLogger = AppLogger.getLogger();

	private static final Logger logger = Logger.getLogger(Filescan.class.getName());
  
  private static String filescanLogFile = null;
  
  private static boolean filescanPathMissing = false;

	private static void setupLogging(String logFile) throws Exception
  {
    if (logFile == null)
    {
      // commonLogger aktivieren
      filescanLogFile = null;
    }
    else
    {
      logFile = logFile.trim();

      if (logFile.equals(filescanLogFile))
        return;

      filescanLogFile = null;

      // erst alle handler (catalina.out) entfernen
      Handler[] myhandler = logger.getHandlers();
      for (int i = 0; i < myhandler.length; i++)
      {
        logger.removeHandler(myhandler[i]);
      }
      // Filelogger hinzufügen
      // MIKE: hochzählen der logfiles unterdrücken
      FileHandler fh = new FileHandler(logFile);
      fh.setFormatter(new LogFormatter());
      // fh.setFormatter(new SimpleFormatter());
      logger.addHandler(fh);
      logger.setLevel(Level.FINE);
      // damit es nicht noch zusätzlich auf die console (catalina.out) geht
      // den LogRecord nicht weiterreichen
      logger.setUseParentHandlers(false);

      filescanLogFile = logFile;
      commonLogger.info("Filescan Logfile '" + logFile + "' initialisiert.");
    }
  }
  
  private static boolean isFineLogging()
  {
    return filescanLogFile == null ? commonLogger.isDebugEnabled() : logger.isLoggable(Level.FINE);
  }
  
  private static void fineLogging(String message)
  {
    if (filescanLogFile == null)
      commonLogger.trace(message);
    else
      logger.fine(message);
  }
  
  private static void warnLogging(String message)
  {
    if (filescanLogFile == null)
      commonLogger.warn(message);
    else
      logger.warning(message);
  }
  
  private static void errorLogging(String message)
  {
    if (filescanLogFile == null)
      commonLogger.error(message);
    else
      logger.severe(message);
  }
  
	// Start the task every 1 minutes
	//
	final ScheduleIterator iterator = new MinutesIterator(1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
	 */
	public void run(TaskContextSystem context) throws Exception
	{
    IDataTableRecord appRecord = DataUtils.getAppprofileRecord(context);
    
    // setup logging if not already done
    setupLogging(appRecord.getStringValue(Appprofile.filescanlogfile));
    
    String path = appRecord.getStringValue(Appprofile.filescanpath);
    if (path == null || path.trim().length()==0)
    {
      if (filescanPathMissing == false)
      {
        warnLogging("Kein Filescan-Pfad definiert!");
        filescanPathMissing = true;
      }
        
      return;
    }
    filescanPathMissing = false;
    
		boolean isError = false;
		List myJobs = getJobs(path);
    
    if (myJobs.size() == 0)
      return;
      
    // Nachdem wir im Verzeichnis alle neuen XML-Dateien festgestellt haben,
    // warten wir etwas, damit wir sicher sind, daß diese komplett von der
    // Gegenseite geschrieben und geflushed sind. 
    // Note: Es trat in der Vergangenheit gelegentlich auf, daß die Bearbeitung
    //       mit einem XML-Parser-Fehler zurückkam.
    // Note2: Besser wäre es, wenn die Gegenseite die Datei zuerst unter einem
    //        temporären Namen (welcher nicht mit .xml endet) schreibt und diese
    //        dann in einer (atomaren?) Aktion umbenennt.
    //
    Thread.sleep(2000);
    
		Iterator it = myJobs.iterator();
		while (it.hasNext())
		{
			File file = (File) it.next();
			// Rename geht nicht gut
			//file.renameTo(new File(file.getAbsolutePath() + ".fail"));
      
			if (isFineLogging())
        fineLogging("process: " + file.getName());
      
      try
      {
        if (processJob(context, file) == true)
        {
          if (isFineLogging())
            fineLogging("Delete " + file.getName());
          FileUtils.forceDelete(file);
        }
        else
        {
          isError = true;
          file.renameTo(new File(file.getAbsolutePath() + ".fail"));
        }
      }
      catch (RecordLockedException ex)
      {
        // Ignorieren -> Wenn ein Datensatz gelockt ist, dann probieren wir es
        // später nochmals
      }
		}
		if (isError)
		{
      fineLogging("Benachrichtige den Administrator");
			DataUtils.notifyAdministrator(context, "filescan ist fehlgeschlagen. Bitte ins Logfile schauen.");
		}
	}



	/**
	 * holt sich alle notwendigen Parameter um eine Meldung zu erzeugen <br>
	 * aus dem XML Dokument
	 * 
	 * @param context
	 * @param newJob
	 * @param parameter
	 * @return @throws
	 *         Exception
	 */
	private static Map getParameter(TaskContextSystem context, JobType newJob, String currentFile) throws Exception
	{
		Map parameter = new HashMap();
		context.getDataAccessor().clear();
		// Tätigkeit speichern
		if (newJob.getActivity() != null)
		{
			parameter.put(PROCESS, newJob.getActivity().toString());
		}
		else
		{
			throw new BusinessException("Tätigkeit der Meldung ist nicht definiert");
		}
		// AK Schlüssel speichern
		if (newJob.getMaintenanceGroup() != null)
		{
			IDataTable workgroupTable = context.getDataTable("callworkgroup");
			workgroupTable.qbeSetValue("name", newJob.getMaintenanceGroup().toString());
			workgroupTable.qbeSetValue("groupstatus", "gültig");
			workgroupTable.search();
			if (workgroupTable.recordCount() == 1)
			{
				parameter.put(WORKGROUP, workgroupTable.getRecord(0).getSaveStringValue("pkey"));
			}
			else
			{
				throw new BusinessException("AK der Meldung ist nicht eindeutig");
			}
		}
		else
		{
			throw new BusinessException("AK der Meldung ist nicht definiert");
		}
		// Objekt Schlüssel speichern
		if (newJob.getObjectID() != null && newJob.getInstance() != null)
		{
			IDataTable objectTable = context.getDataTable("object");
			IDataTable extSystemTable = context.getDataTable("objectext_system");
			objectTable.qbeSetKeyValue("external_id", newJob.getObjectID().toString());
			extSystemTable.qbeSetKeyValue("name", newJob.getInstance().toString());
			extSystemTable.qbeSetValue("systemstatus", "aktiv");

			IDataBrowser objectBrowser = context.getDataBrowser("objectBrowser");
			objectBrowser.search("r_object");
			//lösche qbe in extSystem
			//MIKE: unsauber! ExtSystem ist in der Query von r_call über task und
			// object ohne outher Join
			extSystemTable.qbeClear();

			if (objectBrowser.recordCount() == 1)
			{
				IDataTableRecord objectRecord = objectBrowser.getRecord(0).getTableRecord();
				parameter.put(OBJECT, objectRecord.getSaveStringValue("pkey"));
				String categoryKey = objectRecord.getStringValue("objectcategory_key");
				if (categoryKey == null)
				{
          warnLogging(currentFile + " Kein Werk am Object vorhanden. Nehme Default");
					categoryKey = Config.getString("filescanDefaultCategoryKey");
				}
				parameter.put(CATEGORY, categoryKey);
			}
			else
			{
				throw new BusinessException("ObjektID ist nicht eindeutig");
			}

		}
		else
		{
			throw new BusinessException("ObjektID oder externes System ist nicht definiert");
		}
		//Aus appprofile den Melder und den Agenten holen
		String agent = DataUtils.getAppprofileValue(context, "webqagent_key");
		if (agent != null)
		{
			parameter.put(AGENT, agent);
			parameter.put(CUSTOMER, agent);
			IDataTable employee = context.getDataTable("employee");
			employee.qbeSetKeyValue("pkey", agent);
			employee.search();
			if (employee.recordCount() != 1)
				throw new BusinessException("Agent nicht gefunden");
			IDataTableRecord employeeRecord = employee.getRecord(0);
			parameter.put(AFFECTEDPERSON, employeeRecord.getSaveStringValue("fullname") + " Tel: " + employeeRecord.getSaveStringValue("phonecorr"));
		}
		else
		{
			throw new BusinessException("Webagent in Parametertabelle ist nicht definiert");
		}
		// Meldungstext
		if (newJob.getMessageText() != null)
		{
			parameter.put(PROBLEM, newJob.getMessageText().toString());
		}
		else
		{
			throw new BusinessException("Problembeschreibung ist nicht definiert");
		}
		// datereported
		parameter.put(DATE, newJob.getTimeStamp());
		return parameter;
	}

	/**
	 * fügt neuen Meldungsdatensatz ein Wichtig! Objekt Tätigkeit und AK sind
	 * schon in der QBE!
	 * 
	 * @param context
	 * @param parameter
	 */
	private static void createCall(TaskContextSystem context, Map parameter, String currentFile) throws Exception
	{
		IDataBrowser objectBrowser = context.getDataBrowser("objectBrowser");
		IDataTableRecord objectRecord = objectBrowser.getRecord(0).getTableRecord();
		IDataTransaction trans = context.getDataAccessor().newTransaction();
		try
		{
			IDataAccessor accessor = context.getDataAccessor();
			IDataTableRecord locationRecord;
			//neuen Ort anlegen
			if (objectRecord.hasLinkedRecord("objectlocation"))
			{
				locationRecord = accessor.cloneRecord(trans, objectRecord.getLinkedRecord("objectlocation"), "location");
			}
			else
			{
        fineLogging(currentFile + " Kein Ort am Object zur Erzeugung eines neuen Ortes vorhanden-Nehme leeren Ort.");
				locationRecord = context.getDataTable("location").newRecord(trans);
			}
			// neue Call anlegen
			IDataTableRecord callRecord = context.getDataTable("call").newRecord(trans);
			callRecord.setValue(trans, CUSTOMER, parameter.get(CUSTOMER).toString());
			callRecord.setValue(trans, AGENT, parameter.get(AGENT).toString());
			callRecord.setValue(trans, OBJECT, parameter.get(OBJECT).toString());
			callRecord.setValue(trans, CATEGORY, parameter.get(CATEGORY).toString());
			callRecord.setValue(trans, PROCESS, parameter.get(PROCESS).toString());
			callRecord.setValue(trans, WORKGROUP, parameter.get(WORKGROUP).toString());
			callRecord.setStringValueWithTruncation(trans, PROBLEM, parameter.get(PROBLEM).toString());
			callRecord.setStringValueWithTruncation(trans, AFFECTEDPERSON, parameter.get(AFFECTEDPERSON).toString());
			callRecord.setLinkedRecord(trans, locationRecord);
			callRecord.setValue(trans, "affectedperson_key", parameter.get(CUSTOMER).toString());
			callRecord.setValue(trans, AFFECTEDPERSON, parameter.get(AFFECTEDPERSON).toString());
			callRecord.setDateValue(trans, "datereported", (Date) parameter.get(DATE));
			callRecord.setDateValue(trans, "datecallconnected", (Date) parameter.get(DATE));
			callRecord.setValue(trans, "callbackmethod", "Keine");
			callRecord.appendLongTextValue(trans, "problemtext", parameter.get(PROBLEM).toString() + "\nObjekt  wurde um " + DateTime.format(parameter.get(DATE)).toString() + " von der Warte als gestört gemeldet.");
			trans.commit();
		}
		finally
		{
			trans.close();
		}
	}


	/**
	 * sucht nach den offen Calls und schließt den Wichtig! Objekt Tätigkeit und
	 * AK sind schon in der QBE!
	 * 
	 * @param context
	 * @param parameter
	 */
	private static void closeOpenCall(TaskContextSystem context, Map parameter, String currentFile) throws Exception
	{
		String callKey = "";
		try
		{
			IDataTable call = context.getDataTable("call");
			call.qbeSetKeyValue(OBJECT, parameter.get(OBJECT).toString());
			call.qbeSetKeyValue(AGENT, parameter.get(AGENT).toString());
			call.qbeSetKeyValue(CUSTOMER, parameter.get(CUSTOMER).toString());
			call.qbeSetKeyValue(WORKGROUP, parameter.get(WORKGROUP).toString());
			call.qbeSetValue("callstatus", "AK zugewiesen|Angenommen");
			IDataBrowser callBrowser = context.getDataBrowser("callAKBrowser");
			IRelationSet r_call = context.getApplicationDefinition().getRelationSet("r_call");
			callBrowser.search(r_call, Filldirection.BOTH);

			int nRecs = callBrowser.recordCount();
			if (isFineLogging())
        fineLogging(currentFile + " Es müssen " + nRecs + " Datensätze geschlossen werden.");
			for (int i = 0; i < nRecs; i++)
			{
				IDataTableRecord callRecord = callBrowser.getRecord(i).getTableRecord();
				callKey = callRecord.getSaveStringValue("pkey");
				//Meldungen schliessen
				if ("AK zugewiesen".equals(callRecord.getSaveStringValue(STATUS)))
				{
					IDataTransaction trans = context.getDataAccessor().newTransaction();
					try
					{
						callRecord.setValue(trans, STATUS, "Angenommen");
						callRecord.setValue(trans, "dateowned", "now");
						trans.commit();
					}
					finally
					{
						trans.close();
					}


				}
				if ("Angenommen".equals(callRecord.getSaveStringValue(STATUS)))
				{
					IDataTransaction trans = context.getDataAccessor().newTransaction();
					try
					{

						callRecord.setValue(trans, STATUS, "Fertig gemeldet");
						callRecord.setDateValue(trans, "dateresolved", (Date) parameter.get(DATE));
						callRecord.appendLongTextValue(trans, "problemtext", "Meldung wurde von der Warte um " + DateTime.format(parameter.get(DATE)).toString() + " fertig gemeldet.");
						trans.commit();
					}
					finally
					{
						trans.close();
					}
				}
				if ("Fertig gemeldet".equals(callRecord.getSaveStringValue(STATUS)))
				{
					IDataTransaction trans = context.getDataAccessor().newTransaction();
					try
					{
						callRecord.setValue(trans, STATUS, "Dokumentiert");
						trans.commit();
					}
					finally
					{
						trans.close();
					}

				}
			}
		}
		catch (BusinessException e)
		{
			//MIKE: fangen und neu werfern ist schlecht, sondern überall richtige
			// Meldungen( in Hooks) machen!
			throw new BusinessException("Fehler in Meldung " + callKey + ":" + SystemUtils.LINE_SEPARATOR + e.getMessage());
		}
	}

	/**
	 * Arbeitet jedes einzelne Dokument ab <br>
	 * come: schließt alle offenen Meldungen und legt ein neuen an <br>
	 * da nur eine Meldung für das Objekt erlaubt ist <br>
	 * gone: schließt die Meldung des Objekts
	 * 
	 * @param file
	 * @return
	 */
	private static boolean processJob(TaskContextSystem context, File file) throws RecordLockedException
	{
		String currentFile = file.getName();
		boolean result = false;
		try
		{
			// Die Schemadefinition macht es etwas unübersichtlich
            TTSEnvelopeType envelope = (TTSEnvelopeType) TTSEnvelopeType.unmarshalTTSEnvelopeType(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
            JobType newJob = envelope.getBody().getJob();


			Map parameter = getParameter(context, newJob, currentFile);
			if (newJob.getEvent() == EventTypeEnum.GONE)
			{
				// close all open calls
				closeOpenCall(context, parameter, currentFile);
			}
			if (newJob.getEvent() == EventTypeEnum.COME)
			{
				// close all open calls
				closeOpenCall(context, parameter, currentFile);
				// add new Call
				createCall(context, parameter, currentFile);
			}
			result = true;
		}
		catch (MarshalException e)
		{
      errorLogging(currentFile + " Dokument ist kein gültiges XML-Dokument:" + SystemUtils.LINE_SEPARATOR + e.toString());
		}
		catch (ValidationException e)
		{
      errorLogging(currentFile + " XML-Dokument entspricht nicht Schnittstellendefinition:" + SystemUtils.LINE_SEPARATOR + e.toString());
		}
    catch (IllegalStateException e)
    {
      errorLogging(e.toString());
    }
    catch (RecordLockedException e)
    {
      // Wenn ein Datensatz gelockt ist, dann probieren wir es später nochmals
      warnLogging(e.toString());
      throw e;
    }
		catch (BusinessException e)
		{
      errorLogging(currentFile + " Verstoß gegen Businessregel:" + SystemUtils.LINE_SEPARATOR + e.toString());
		}
		catch (Exception e)
		{
      errorLogging(currentFile + " Unerwarteter Fehler:" + SystemUtils.LINE_SEPARATOR + e.toString());
		}
		return result;
	}


	/**
	 * @return ArrayList of File
	 * @throws Exception
	 */
	private static ArrayList getJobs(String path) throws Exception
	{
		ArrayList jobs = new ArrayList();
		ArrayList files = Directory.getAll(new File(path), false);
		for (Iterator iter = files.iterator(); iter.hasNext();)
		{
			File element = (File) iter.next();
			if (element.getName().endsWith(".xml"))
			{
				jobs.add(element);
			}
		}
		return jobs;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.scheduler.SchedulerTask#hibernatedOnSchedule()
	 */
	public boolean hibernatedOnSchedule()
	{
		return false;
	}
}
