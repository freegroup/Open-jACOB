/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system.callTaskSynchronizer;

import jacob.common.AppLogger;
import jacob.common.Task;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType;
import jacob.scheduler.system.callTaskSynchronizer.castor.CallUpdateType;
import jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType;
import jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.scheduler.TaskContextSystem;

/**
 *  
 */
public class EDVINActor implements IActor
{
	static public final transient String RCS_ID = "$Id: EDVINActor.java,v 1.8 2006/10/05 12:48:05 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.8 $";
	static protected final transient Log logger = AppLogger.getLogger();

	/*
	 * Edvinauftrag wird fertig gemeldet und dokumentiert
	 * 
	 * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskUpdateType)
	 */
	public void proceed(TaskContextSystem context, TaskUpdateType update) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("bearbeite EDVIN TaskUpdateType mit taskno= " + update.getTaskno());
		}

		IDataAccessor accessor = context.getDataAccessor().newAccessor();
		accessor.clear();
		IDataTable taskTable = accessor.getTable("task");
		taskTable.qbeSetKeyValue("taskno", update.getTaskno());
		IDataBrowser taskBrowser = accessor.getBrowser("taskBrowser");
		taskBrowser.search("r_task", Filldirection.BOTH);
		if (taskBrowser.recordCount() != 1)
		{
			throw new BusinessException("Auftrag " + update.getTaskno() + " ist nicht eindeutig in DB");
		}
		IDataTransaction trans = taskTable.startNewTransaction();
		// MIKE: Die Methode getRecordToUpdate() sollte eigentlich engine-intern sein!!!
		//       Gibt es eine andere Möglichkeit?
		IDataTableRecord task = ((DataBrowser) taskBrowser).getRecordToUpdate(trans, 0);
		try
		{

			String taskStatus = task.getSaveStringValue("taskstatus");
			if (logger.isDebugEnabled())
			{
				logger.debug("Vor fertig melden Taskstataus= " + taskStatus);
			}

			if ("In Arbeit".equals(taskStatus))
			{
				task.setStringValue(trans, "taskstatus", "Fertig gemeldet");
				trans.commit();
				logger.debug("Task auf 'Fertig gemeldet' commited");
			}

		}
		finally
		{
			trans.close();
		}
		String taskStatus = task.getSaveStringValue("taskstatus"); // Status
																															 // nochmal holen
		if (logger.isDebugEnabled())
		{
			logger.debug("Taskstataus vor dem Versuch zu dokumentieren= " + taskStatus);
		}

		if ("Fertig gemeldet".equals(taskStatus) && "Dokumentiert".equals(update.getTaskstatus().toString()))
		{
			// Task.setDocumented holt sich selbste eine Transaction
			Task.setDocumented(null, task);
			logger.debug("Task auf 'Dokumentiert' commited");
		}

	}

	/*
	 * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskInsertType)
	 */
	public void proceed(TaskContextSystem context, TaskInsertType insert) throws Exception
	{
		throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.insert) verweigert.");
	}

	/*
	 * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallUpdateType)
	 */
	public void proceed(TaskContextSystem context, CallUpdateType update) throws Exception
	{
		throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.update) verweigert.");
	}

	/*
	 * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallInsertType)
	 */
	public void proceed(TaskContextSystem context, CallInsertType insert) throws Exception
	{
		throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.insert) verweigert.");
	}
}
