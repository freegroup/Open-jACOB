/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.EDVINActor;
import jacob.scheduler.system.callTaskSynchronizer.GenericActor;
import jacob.scheduler.system.callTaskSynchronizer.IActor;
import jacob.scheduler.system.callTaskSynchronizer.KANAActor;
import jacob.scheduler.system.callTaskSynchronizer.WarteActor;
import jacob.scheduler.system.callTaskSynchronizer.castor.Call;
import jacob.scheduler.system.callTaskSynchronizer.castor.Job;
import jacob.scheduler.system.callTaskSynchronizer.castor.Task;
import jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 *  
 */
public class CallTaskSynchronizer extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: CallTaskSynchronizer.java,v 1.22 2006/10/05 12:48:04 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.22 $";
	static protected final transient Log logger = AppLogger.getLogger();

	private Map actors = new HashMap();

	// Start the task every 5 seconds.
	//
	//final ScheduleIterator iterator = new SecondsIterator(5);

	// Start the task every 1 minutes
	//
	final ScheduleIterator iterator = new MinutesIterator(1);


	// Start the task every day at 8:00:00
	//
	//final ScheduleIterator iterator = new DailyIterator(8,0,0);


	// Start the task every week day at 17:30:00
	//
	//int[] weekdays = new int[] { Calendar.MONDAY, Calendar.TUESDAY,
	// Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
	//final ScheduleIterator iterator =new RestrictedDailyIterator(17, 30, 00,
	// weekdays);
	private void setErrorLog(IDataTransaction transaction, IDataTableRecord record, String msg) throws Exception
	{
		record.setValue(transaction, "error_state", "Ja");
		record.setValue(transaction, "loginfo", msg);
		transaction.commit();
	}


	public CallTaskSynchronizer()
	{
		actors.put("KANA4", new KANAActor());
		actors.put("EDVIN", new EDVINActor());
		actors.put("Generic", new GenericActor());
		actors.put("Warte", new WarteActor());
	}

	/*
	 * @see de.tif.jacob.scheduler.SchedulerTaskSystem#iterator()
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/*
	 * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
	 */
	public void run(TaskContextSystem context) throws Exception
	{
		IDataTable ttsImport = context.getDataTable("tts_import");
		ttsImport.clear();
		ttsImport.qbeClear();
		ttsImport.qbeSetValue("error_state", "Nein");
		ttsImport.search();
		if (logger.isDebugEnabled())
		{
			logger.debug("Anzahl der zu bearbeitenden Datensaetze in tts_import : " + ttsImport.recordCount());
		}
		boolean hasError = false;
		Exception globalExeption=null;
		try
		{
			int i = ttsImport.recordCount();
			while (i>0)
			{
				i=i-1;
				// get the XML document with the 'todo'
				//
				IDataTableRecord record = ttsImport.getRecord(i);
				String xmlDocument = record.getStringValue("xml");
				if (logger.isDebugEnabled())
				{
					logger.debug("bearbeite Satz: " + i + " mit pkey= "+ record.getStringValue("pkey"));
				}

				// create a transaction to delete/modify ONE record
				//
				IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
				try
				{
					Ttsjob ttsJob = (Ttsjob) Ttsjob.unmarshalTtsjob(new StringReader(xmlDocument));
					Job job = ttsJob.getJob();
					// determine the actor depending on the foreign system
					//
					IActor actor = (IActor) actors.get(ttsJob.getActorSystem().toString());

					if (job.getCall() != null)
					{
						Call call = job.getCall();
						if (call.getInsert() != null)
							actor.proceed(context, call.getInsert());
						else
							actor.proceed(context, call.getUpdate());
					}
					else
					{
						Task task = job.getTask();
						if (task.getInsert() != null)
							actor.proceed(context, task.getInsert());
						else
							actor.proceed(context, task.getUpdate());
					}
					// delete the record if we have success
					//
						record.delete(currentTransaction);

						// commit the delete
						//
						currentTransaction.commit();
					
					
				}
				catch (MarshalException e)
				{
					hasError = true;
					setErrorLog(currentTransaction, record, "Dokument ist kein gültiges XML-Dokument:" + SystemUtils.LINE_SEPARATOR + e.toString());
					
				}
				catch (ValidationException e)
				{
					hasError = true;
					setErrorLog(currentTransaction, record, "XML-Dokument entspricht nicht Schnittstellendefinition:" + SystemUtils.LINE_SEPARATOR + e.toString());
				}
				catch (RecordLockedException e)
				{
					// ignore: kein 'schlimmer' Fehler. Ein gelockter Record kann später
					// nochmal versucht werden.
				}
				catch (BusinessException e)
				{
					hasError = true;
					setErrorLog(currentTransaction, record, "Verstoß gegen Businessregel:" + SystemUtils.LINE_SEPARATOR + e.toString());
				}
				catch (Exception e)
				{
//				 einen unerwarteter Fehler später weiterreichen!
					globalExeption= e;
					hasError = true;
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("UnbekannterFehler : "+e.getMessage());
                    }
					String message = ExceptionUtils.getStackTrace(e);
					setErrorLog(currentTransaction, record, "Allgemeiner Fehler:" + SystemUtils.LINE_SEPARATOR + message);							
				}
				finally
				{
					currentTransaction.close();
				}
			}
		}
		finally
		{
			if (hasError)
				DataUtils.notifyAdministrator(context,"Fehler in CallTaskSynchronizer bitte in Tabelle tts_import und catalina.out schauen. ");

			if(globalExeption!=null)
				throw globalExeption;
			// Admin benachrichtigen, dass ein Fehler aufgetreten ist
		}
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
