/*
 * Created on 15.10.2004
 * by mike
 *
 */
package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.Job;
import jacob.scheduler.system.callTaskSynchronizer.castor.Task;
import jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType;
import jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * Implementierung der Schnittstelle EdvinResolved
 * Aufträe, die in EDVIN fertig/dokumentiert sind müssen <br>
 * im TTS nachgezogen werden (EIMAN)
 * 
 * @author mike
 *  
 */
public class EdvinResolved extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: EdvinResolved.java,v 1.10 2006/09/28 08:43:17 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.10 $";
	static protected final transient Log logger = AppLogger.getLogger();

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
    boolean hasError = false;
    try
    {
      List myJobs = getJobs(context);
      Iterator it = myJobs.iterator();
      while (it.hasNext())
      {
        IDataTableRecord task = (IDataTableRecord) it.next();
        
        if (logger.isDebugEnabled())
          logger.debug("Bearbeite Auftrag " + task.getValue("taskno"));
        
        try
        {
          if (processJob(context, task) == true)
          {
            if (logger.isDebugEnabled())
              logger.debug("Bearbeitung von [" + task.getValue("taskno") + "] erfolgreich.");
          }
          else
          {
            hasError = true;
            if (logger.isWarnEnabled())
              logger.warn("Auftrag [" + task.getValue("taskno") + "] ist nicht erfolgreich bearbeitet worden.");
          }
        }
        catch (RecordLockedException ex)
        {
          // Auftrag ist geloggt, ignorieren und später versuchen
          if (logger.isInfoEnabled())
            logger.info("Auftrag [" + task.getValue("taskno") + "] ist zur Zeit gesperrt: " + ex.getMessage());
        }
      }
    }
    catch (Exception e)
    {
      logger.warn("Unerwarteter Abbruch", e);
    }
    if (hasError)
      DataUtils.notifyAdministrator(context, "EdvinResolved ist fehlgeschlagen. Bitte in catalina.out schauen.");
  }

	/**
	 * Erzeugt einen Eintrag in die tts_import Tabelle um einen Auftrag zu aktualiseieren
	 * @param context
	 * @param trans
	 * @param task
	 */
	private static void createImportRecord(TaskContextSystem context,IDataTransaction trans, IDataTableRecord taskRecord) throws Exception
	{
		
    // TtsJob zusammenbastellen
    TaskUpdateType taskUpdateType = new TaskUpdateType();
    taskUpdateType.setTaskno(taskRecord.getSaveStringValue("taskno"));
    taskUpdateType.setTaskstatus(TaskstatusType.DOKUMENTIERT);
    Task task = new Task();
    task.setUpdate(taskUpdateType);
    Job job = new Job();
    job.setTask(task);
    Ttsjob ttsJob = new Ttsjob();
    ttsJob.setActorSystem(ActorSystemType.EDVIN);
    ttsJob.setJob(job);
    
    // XML formatieren
    StringWriter sw = new StringWriter();
    Document doc = XMLUtils.newDocument();
    Marshaller.marshal(ttsJob,doc);
    org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
    outFormat.setIndenting(true);
    outFormat.setIndent(2);
    outFormat.setLineWidth(200);
    outFormat.setEncoding("ISO-8859-1");
    org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(sw, outFormat);
    xmlser.serialize(doc); 
    
    // tts_import Datensatz speichern
    IDataTable ttsImport=context.getDataTable("tts_import");
    IDataTableRecord ttsImportRecord = ttsImport.newRecord(trans);
    ttsImportRecord.setValue(trans,"origin","EDVIN Resolved");
    ttsImportRecord.setValue(trans,"xml",sw.toString());
	}

	/**
	 * @param context
	 * @param task
	 * @return 
	 * @throws Exception
	 */
	private static boolean processJob(TaskContextSystem context, IDataTableRecord task) throws RecordLockedException
  {
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      task.setValue(trans, "issynchronized", "Ja");
      createImportRecord(context, trans, task);
      trans.commit();
      return true;
    }
    catch (RecordLockedException e)
    {
      // Auftrag ist geloggt, ignorieren und später versuchen
      throw e;
    }
    catch (BusinessException e)
    {
      if (logger.isInfoEnabled())
        logger.info("Verstoß gegen Businessregel:" + SystemUtils.LINE_SEPARATOR + e.toString());
    }
    catch (Exception e)
    {
      if (logger.isWarnEnabled())
        logger.warn("Unerwarteter Fehler:" + SystemUtils.LINE_SEPARATOR + e.getMessage(), e);
    }
    finally
    {
      trans.close();
    }
    return false;
  }


	/**
	 * @return ArrayList of taskrecords
	 * @throws Exception
	 */
	private static ArrayList getJobs(TaskContextSystem context) throws Exception
	{
		// Aufträge suchen
		
	//	IDataTable tasktable = context.getDataTable("task");
		// Da in der Vergangenheit an der Edvinschnittstelle nicht konsistente Daten
		// übergeben wurden ist es besser den Flag issynchronized nicht mit task sondern
		// mit alias taskvie zu machen. Dann tritt der Fehler erst im CallTaskSynchronizer
		// auf und ist richtig protokolliert.
		IDataTable tasktable = context.getDataTable("taskview");
		tasktable.clear();
		tasktable.qbeClear();
		tasktable.qbeSetKeyValue("resolved_extsystem","Ja");
		tasktable.qbeSetValue("taskstatus","In Arbeit|Fertig gemeldet");
		tasktable.qbeSetKeyValue("issynchronized","Nein");
		tasktable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
		tasktable.search();
		ArrayList jobs = new ArrayList();
		for (int i = 0; i < tasktable.recordCount(); i++)
		{
			jobs.add(tasktable.getRecord(i));
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
