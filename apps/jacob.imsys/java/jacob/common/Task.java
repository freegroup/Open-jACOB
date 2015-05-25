/*
 * Created on 29.07.2004
 *
 */
package jacob.common;

import jacob.common.data.DataUtils;
import jacob.config.Config;
import jacob.exception.BusinessException;

import java.util.Date;
import java.util.Vector;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;

/**
 * @author achim
 *  
 */
public class Task
{
	static public final transient String RCS_ID = "$Id: Task.java,v 1.3 2005/06/27 12:23:20 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	static protected final transient Log logger = AppLogger.getLogger();
	static private final Vector globalAccessRoles = new Vector();

	static
	{
		globalAccessRoles.add("CQ_PM");
		globalAccessRoles.add("CQ_ADMIN");
		globalAccessRoles.add("CQ_AGENT");
		globalAccessRoles.add("CQ_SUPERAK");
		globalAccessRoles.add("CQ_SDADMIN");
	}

	/**
	 * Search calls of the current user
	 * 
	 * @param context
	 *          ClientContext
	 * @param statusConstraint
	 * @param relationSet
	 */
	public static void findByUserAndState(IClientContext context, IUser user, String statusConstraint, String relationSet) throws Exception
	{
		IDataAccessor accessor = context.getDataAccessor(); // current data
																												// connection
		IDataBrowser browser = context.getDataBrowser(); // the current browser
		IDataTable taskTable = context.getDataTable(); // the table in which the
																									 // actions performes
		IDataTable groupmemberTable = accessor.getTable("groupmember"); //	the
																																		// groupmember
																																		// table to
																																		// perform
																																		// a
																																		// constraint


		accessor.qbeClearAll();

		String userKey = user.getKey();

		// set search criteria
		taskTable.qbeSetValue("taskstatus", statusConstraint);

		groupmemberTable.qbeSetValue("employeegroup", userKey);
		//groupmemberTable.qbeSetValue("accessallowed", "lesen/schreiben");


		// do the search itself
		//
		browser.search(relationSet, Filldirection.BOTH);

		// display the result set
		//
		context.getGUIBrowser().setData(context, browser);
	}

	/**
	 * Set calls.callstatus = newStatus
	 * 
	 * @param trans
	 *          The transaction to work or null if the function must create its
	 *          own transaction
	 * @param context
	 * @param newStatus
	 */
	public static void setStatus(IDataTransaction trans, Context context, IDataTableRecord task, String newStatus) throws Exception
	{
		IDataTransaction currentTransaction = trans != null ? trans : context.getDataAccessor().newTransaction();

		try
		{
			task.setValue(currentTransaction, "taskstatus", newStatus);
			if (trans == null)
				currentTransaction.commit();
		}
		finally
		{
			if (trans == null)
				currentTransaction.close();
		}
		logger.debug("commit done");
	}


	/**
	 * Überprüft den Dokumentationsstatus eines Task. <br>
	 * Dieser wird evetuell mit der korrespondierenden EDVIN-Datenbank
	 * verifiziert. <br>
	 * 
	 * @param trans
	 * @param task
	 */
	public static void checkDocumented(IDataTransaction trans, IDataTableRecord task) throws Exception
	{
		//MIKE: man kann dokumentieren ohne die Zeiten einzugeben!
		// Arbeitsbeginn sollte vor Arbeitsende liegen
		//
		Date start = task.getTimestampValue("taskstart");
		Date end = task.getTimestampValue("taskdone");
		if (start == null || end == null || start.compareTo(end) > 0)
			throw new BusinessException("Das Arbeitsende kann nicht vor Arbeitsbeginn liegen.");

		// Die Anzahl der Bearbeiter sollte einen schlüssigen Wert haben
		//
		int resolver = task.getintValue("no_resolver");
		if (resolver < 0 || resolver > 999)
			throw new BusinessException("Die Anzahl der Arbeiter stimmt nicht.");

		// Die Arbeitszeit sollte einen schlüssigen Wert haben
		//
		long timespent = task.getlongValue("totaltimespent");
		if (timespent < 0 || timespent > 3600000)
			throw new BusinessException("Der Durchführungsaufwand ist ungültig.");

		// Der Dokumentationsaufwand sollte sinn machen
		//
		long documentation = task.getlongValue("timedocumentation");
		if (documentation < 0 || documentation > 36000)
			throw new BusinessException("Der Dokumentationsaufwand ist ungültig.");

		IDataTableRecord extSystem = task.getLinkedRecord("ext_system");
		if (extSystem != null)
		{
			String type = extSystem.getSaveStringValue("systemtype");
			if ("GDS".equals(type))
			{
				if (task.getAccessor().getTable("errorcodetaskdata").exists("task_key", task.getValue("pkey")))
					throw new BusinessException("Es ist keine GDS Rückmeldung angegeben.");
			}
			// Falls es sich um ein EDVIN Auftrag handelt muss dieser gegen EDVIN auf
			// schlüssigkeit überprüft
			// werden.
			//
			else if ("EDVIN".equals(extSystem.getSaveStringValue("systemtype")))
			{
//            ignore
			}
			else if ("virtuell".equals(type))
			{
				// ignore
			}
			else
				throw new BusinessException("Kein Abrechnungssystem definiert.");
		}
	}

	/**
	 * @param trans
	 *          The transaction to work or null if the function must create its
	 *          own transaction
	 * @param context
	 * @throws Exception
	 */
	public static void setDocumented(IDataTransaction trans, IDataTableRecord task) throws Exception
	{
		IDataTransaction currentTransaction = trans != null ? trans : task.getAccessor().newTransaction();

		if (!task.getSaveStringValue("taskstatus").equals("Fertig gemeldet"))
			throw new BusinessException("Nur Aufträge im Status [Fertig gemeldet] können dokumentiert werden.[" + task.getSaveStringValue("taskstatus") + "]");

		try
		{
			if (task.getSaveStringValue("taskstart").length() == 0)
				task.setValue(currentTransaction, "taskstart", task.getValue("daterequested"));

			if (task.getSaveStringValue("timedoc_h").length() == 0)
				task.setValue(currentTransaction, "timedoc_h", "0");

			if (task.getSaveStringValue("timedoc_m").length() == 0)
				task.setValue(currentTransaction, "timedoc_m", "00");

			if (task.getSaveStringValue("timedoc_m").length() == 0)
				task.setValue(currentTransaction, "timedoc_m", "00");

			if (task.getSaveStringValue("timedocumentation").length() == 0)
				task.setValue(currentTransaction, "timedocumentation", "0");

			if (task.getSaveStringValue("no_resolver").length() == 0)
				task.setValue(currentTransaction, "no_resolver", "1");

			task.setValue(currentTransaction, "taskstatus", "Dokumentiert");
			if (trans == null)
				currentTransaction.commit();
		}
		finally
		{
			if (trans == null)
				currentTransaction.close();
		}
	}


	/**
	 * wenn der User nur Rolle AK hat, dann muss die Tabelle groupmember <br>
	 * mit dem User.key eingeschränkt werden
	 * 
	 * @param context
	 */
	public static void setGroubmemberConstraint(IClientContext context) throws Exception
	{
		IUser user = context.getUser();
		if (!user.hasOneRoleOf(globalAccessRoles))
		{
			IDataTable grpmember = context.getDataTable("groupmember");
			grpmember.qbeClear();
			grpmember.qbeSetValue("employeegroup", user.getKey());
		}

	}

	public static void setDefault(IDataAccessor accessor, IDataTableRecord newtask, IDataTransaction newtrans) throws Exception
	{
		newtask.setStringValue(newtrans, "escstatus", null);
		newtask.setValue(newtrans, "cclist", null);
		newtask.setValue(newtrans, "taskno", null);
		newtask.setValue(newtrans, "taskstart", null);
		newtask.setValue(newtrans, "taskdone", null);
		newtask.setValue(newtrans, "no_resolver", null);
		newtask.setValue(newtrans, "accountdate", null);
		newtask.setValue(newtrans, "timedocumentation", null);
		newtask.setValue(newtrans, "pm_order", null);
		newtask.setValue(newtrans, "totaltimespent", null);
		newtask.setValue(newtrans, "datedocumented", null);
		newtask.setValue(newtrans, "dateresolved", null);
		newtask.setValue(newtrans, "transmitted", "false");
		newtask.setValue(newtrans, "resolved_extsystem", "Nein");
		newtask.setValue(newtrans, "disruption_h", null);
		newtask.setValue(newtrans, "disruption_m", null);
		newtask.setValue(newtrans, "disruption_start", null);
		newtask.setValue(newtrans, "edvin_beh_bez", null);
		newtask.setValue(newtrans, "edvin_beh_code", null);
		newtask.setValue(newtrans, "edvin_bgr_bez", null);
		newtask.setValue(newtrans, "edvin_bgr_code", null);
		newtask.setValue(newtrans, "edvin_btl_bez", null);
		newtask.setValue(newtrans, "edvin_btl_code", null);
		newtask.setValue(newtrans, "edvin_mah_bez", null);
		newtask.setValue(newtrans, "edvin_mah_code", null);
		newtask.setValue(newtrans, "edvin_sha_bez", null);
		newtask.setValue(newtrans, "edvin_sha_code", null);
		newtask.setValue(newtrans, "productionloss_h", null);
		newtask.setValue(newtrans, "productionloss_m", null);
		// Meldung verlinken
		IDataTable calltable = accessor.getTable("call");
		if (calltable.recordCount() == 1)
		{

			IDataTableRecord callRecord = calltable.getRecord(0);
            if (newtask.getValue("summary")==null)
            {
                newtask.setValue(newtrans, "summary", callRecord.getStringValue("problem"));
            }
			newtask.setLinkedRecord(newtrans, callRecord);
			String objectKey = callRecord.getStringValue("object_key");
			if (objectKey != null)
			{
				//wir haben ein Objekt!
				DataUtils.linkTable(accessor, newtrans, newtask, "object_key", "taskobject", "pkey", objectKey);
				// externes System holen Pflicht im Objekt!
				String extSystemKey = accessor.getTable("taskobject").getRecord(0).getStringValue("ext_system_key");
				DataUtils.linkTable(accessor, newtrans, newtask, "ext_system_key", "ext_system", "pkey", extSystemKey);
			}
		}

	}
}
