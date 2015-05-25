/*
 * Created on 29.07.2004
 *
 */
package jacob.common;

import jacob.common.data.DataUtils;
import jacob.config.Config;
import jacob.exception.BusinessException;
import jacob.model.Ext_system;

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
	static public final transient String RCS_ID = "$Id: Task.java,v 1.33 2007/08/01 13:53:46 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.33 $";

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
	 * Überprüft die Edvin Codes ob sie richtig sind
	 * @param IDataTableRecord tasktask
	 * @throws Exception
	 */
	public static void validateEdvinCodes(IDataTableRecord task) throws Exception
	{
		
		
		IDataTableRecord extSystem = task.getLinkedRecord("ext_system");

		String edvin_beh_code = task.getStringValue("edvin_beh_code");
		String edvin_bgr_code = task.getStringValue("edvin_bgr_code");
		String edvin_btl_code = task.getStringValue("edvin_btl_code");
		String edvin_mah_code = task.getStringValue("edvin_mah_code");
		String edvin_sha_code = task.getStringValue("edvin_sha_code");
		boolean hasConstraint = (edvin_beh_code!=null) ||  (edvin_bgr_code!=null) || (edvin_btl_code!=null) 
		                        ||(edvin_mah_code!=null) || ( edvin_sha_code != null);
		boolean mustNULL = true;
		
		if (extSystem != null)
		{
			String type = extSystem.getSaveStringValue("systemtype");
			
			if (!"EDVIN".equals(extSystem.getSaveStringValue("systemtype")))
			{
				mustNULL = true;
			}
			else
			{
				mustNULL= false;
			}

			//TODO: FUP/EV HACK!
			// ----------------------------------
			try
			{
				Config.getString(extSystem.getSaveStringValue("name"));
			}
			catch (Exception e)
			{
				mustNULL = true;
			}
			// ----------------------------------
			
			boolean noTaskType = true;
			if (task.hasLinkedRecord("tasktype"))
			{
				noTaskType = false;
			}
			String edvin = extSystem.getSaveStringValue("name");
            String hwg ="";
            boolean noHWG;
            if (task.hasLinkedRecord("taskworkgroup"))
            {
                hwg= task.getLinkedRecord("taskworkgroup").getSaveStringValue("hwg_name");
                noHWG = false;
            }
            else
            {
                noHWG = true;
            }
			
			
			// kein Edvin Code also raus
			if (!hasConstraint)
			{
				logger.debug("validateEdvinCodes has no constraints ");
				return;
			}
			
			if ( hasConstraint && ( noHWG || noTaskType))
				throw new BusinessException("Ein EDVIN-Code ohne Angabe der Auftragsart oder HWG ist nicht erlaubt.");

			if ( hasConstraint && ( mustNULL))
				throw new BusinessException("Ein EDVIN-Code ist für dieses Abrechnungssystem nicht erlaubt.");

			// überprüfen ob der Code gültig ist!
			if (edvin_beh_code != null)
			{
				logger.debug("validateEdvinCodes check  Baueinheit");
				if (!Edvin.isValidBaueinheitForHwg(edvin, hwg, task.getStringValue("edvin_beh_code")))
					throw new BusinessException("Baueinheit ist nicht gültig.");
			}
			// überprüfen ob der Code gültig ist!
			if (edvin_bgr_code != null)
			{
				logger.debug("validateEdvinCodes check  Baugruppe");
				if (!Edvin.isValidBaugruppeForHwg(edvin, hwg, edvin_bgr_code))
					throw new BusinessException("Baugruppe ist nicht gültig.");
			}

			// überprüfen ob der Code gültig ist!
			if (edvin_btl_code != null)
			{
				logger.debug("validateEdvinCodes check  Bauteil");
				if (!Edvin.isValidBauteilForHwg(edvin, hwg, edvin_btl_code))
					throw new BusinessException("Bauteil ist nicht gültig.");
			}

			// überprüfen ob der Code gültig ist!
			if (edvin_mah_code != null)
			{
				if (!Edvin.isValidMassnahmencodeForHwg(edvin, hwg, edvin_mah_code))
					throw new BusinessException("Maßnahmencode ist nicht gültig.");
			}

			// überprüfen ob der Code gültig ist!
			if (edvin_sha_code != null)
			{
				if (!Edvin.isValidSchadenscodeForHwg(edvin, hwg, edvin_sha_code))
					throw new BusinessException("Schadenscode ist nicht gültig.");
			}

		}
		if ( hasConstraint && (mustNULL))
			throw new BusinessException("Ein EDVIN-Code ist für dieses Abrechnungssystem nicht erlaubt.");
    logger.debug("validateEdvinCodes return true");
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
				//TODO: FUP/EV HACK!
				// ----------------------------------
				try
				{
					Config.getString(extSystem.getSaveStringValue("name"));
				}
				catch (Exception e)
				{
					return; // FUP hat keine 5 Felder!
				}
				// ----------------------------------
				if (!task.hasLinkedRecord("tasktype"))
					throw new BusinessException("Es ist keine Auftragsart angegeben.");

				String edvin = extSystem.getSaveStringValue("name");
				String hwg = task.getLinkedRecord("taskworkgroup").getSaveStringValue("hwg_name");
				String code = task.getLinkedRecord("tasktype").getSaveStringValue("taskcode");

				Edvin.FieldConstraint contraint = Edvin.getFieldConstraints(edvin, hwg, code);

				if (contraint.stoerbeginn == Edvin.FieldConstraint.PFLICHTFELD && task.getValue("disruption_start") == null)
					throw new BusinessException("Störbeginn ist Pflichtfeld.");

				if (contraint.stoerdauer == Edvin.FieldConstraint.PFLICHTFELD && task.getValue("disruption_h") == null && task.getValue("disruption_m") == null)
					throw new BusinessException("Stördauer ist Pflichtfeld.");

				if (contraint.anl_ausfall == Edvin.FieldConstraint.PFLICHTFELD && task.getValue("productionloss_h") == null && task.getValue("productionloss_m") == null)
					throw new BusinessException("Anlagenausfall ist Pflichtfeld.");

				if (contraint.baueinheit == Edvin.FieldConstraint.PFLICHTFELD)
				{
					if (task.getValue("edvin_beh_code") == null)
						throw new BusinessException("Baueinheit ist Pflichtfeld.");
					else if (!Edvin.isValidBaueinheitForHwg(edvin, hwg, task.getStringValue("edvin_beh_code")))
						throw new BusinessException("Baueinheit ist nicht gültig.");
				}
				else
				{ // überprüfen ob der Code gültig ist!
					if (task.getValue("edvin_beh_code") != null)
						if (!Edvin.isValidBaueinheitForHwg(edvin, hwg, task.getStringValue("edvin_beh_code")))
							throw new BusinessException("Baueinheit ist nicht gültig.");
				}

				if (contraint.baugruppe == Edvin.FieldConstraint.PFLICHTFELD)
				{
					if (task.getValue("edvin_bgr_code") == null)
						throw new BusinessException("Baugruppe ist Pflichtfeld.");
					else if (!Edvin.isValidBaugruppeForHwg(edvin, hwg, task.getStringValue("edvin_bgr_code")))
						throw new BusinessException("Baugruppe ist nicht gültig.");
				}
				else
				{ // überprüfen ob der Code gültig ist!
					if (task.getValue("edvin_bgr_code") != null)
						if (!Edvin.isValidBaugruppeForHwg(edvin, hwg, task.getStringValue("edvin_bgr_code")))
							throw new BusinessException("Baugruppe ist nicht gültig.");
				}

				if (contraint.bauteil == Edvin.FieldConstraint.PFLICHTFELD)
				{
					if (task.getValue("edvin_btl_code") == null)
						throw new BusinessException("Bauteil ist Pflichtfeld.");
					else if (!Edvin.isValidBauteilForHwg(edvin, hwg, task.getStringValue("edvin_btl_code")))
						throw new BusinessException("Bauteil ist nicht gültig.");
				}
				else
				{ // überprüfen ob der Code gültig ist!
					if (task.getValue("edvin_btl_code") != null)
						if (!Edvin.isValidBauteilForHwg(edvin, hwg, task.getStringValue("edvin_btl_code")))
							throw new BusinessException("Bauteil ist nicht gültig.");
				}

				if (contraint.mass_code == Edvin.FieldConstraint.PFLICHTFELD)
				{
					if (task.getValue("edvin_mah_code") == null)
						throw new BusinessException("Maßnahmencode ist Pflichtfeld.");
					else if (!Edvin.isValidMassnahmencodeForHwg(edvin, hwg, task.getStringValue("edvin_mah_code")))
						throw new BusinessException("Maßnahmencode ist nicht gültig.");
				}
				else
				{ // überprüfen ob der Code gültig ist!
					if (task.getValue("edvin_mah_code") != null)
						if (!Edvin.isValidMassnahmencodeForHwg(edvin, hwg, task.getStringValue("edvin_mah_code")))
							throw new BusinessException("Maßnahmencode ist nicht gültig.");
				}

				if (contraint.schad_code == Edvin.FieldConstraint.PFLICHTFELD)
				{
					if (task.getValue("edvin_sha_code") == null)
						throw new BusinessException("Schadenscode ist Pflichtfeld.");
					else if (!Edvin.isValidSchadenscodeForHwg(edvin, hwg, task.getStringValue("edvin_sha_code")))
						throw new BusinessException("Schadenscode ist nicht gültig.");
				}
				else
				{ // überprüfen ob der Code gültig ist!
					if (task.getValue("edvin_sha_code") != null)
						if (!Edvin.isValidSchadenscodeForHwg(edvin, hwg, task.getStringValue("edvin_sha_code")))
							throw new BusinessException("Schadenscode ist nicht gültig.");
				}
			}
			else if ("virtuell".equals(type))
			{
				// ignore
			}
      else if (Ext_system.systemtype_ENUM._SAPiPRO.equals(type))
      {
        // Hier die SAP Ausnahmen behandeln
      }
			else
      {
				throw new BusinessException("Kein Abrechnungssystem definiert.");
      }
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
		newtask.setValue(newtrans, "issynchronized", "Nein");
		// Meldung verlinken
		IDataTable calltable = accessor.getTable("call");
		if (calltable.recordCount() == 1)
		{
			IDataTableRecord callRecord = calltable.getRecord(0);
			newtask.setLinkedRecord(newtrans, callRecord);
			String objectKey = callRecord.getStringValue("object_key");
			String callpriority = calltable.getRecord(0).getStringValue("priority");
			newtask.setValue(newtrans, "priority", callpriority);
			if (objectKey != null)
			{
				//wir haben ein Objekt!
				DataUtils.linkTable(accessor, newtrans, newtask, "object_key", "taskobject", "pkey", objectKey);
				// externes System holen Pflicht im Objekt! Achtung Objekt kann Status >5 bekommen haben!
                if (accessor.getTable("taskobject").recordCount()==1)
				{
                    String extSystemKey = accessor.getTable("taskobject").getRecord(0).getStringValue("ext_system_key");
                    DataUtils.linkTable(accessor, newtrans, newtask, "ext_system_key", "ext_system", "pkey", extSystemKey);
                }
			}
		}

	}
}
