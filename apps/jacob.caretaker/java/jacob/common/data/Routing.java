/*
 * Created on 18.06.2004
 * by mike
 *
 */
package jacob.common.data;

import jacob.common.AppLogger;
import jacob.model.Contract;
import jacob.model.Location;
import jacob.model.Locationcontract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;

/**
 * Diese Klasse implementiert den Routingmechanismus von DaimlerChrysler 1. der
 * Vertrag ist ortsabhängig 2. die Tätigkeit ist abhängig vom Gewerk und Ort
 * 
 * @author mike
 *  
 */
public class Routing
{
	static public final transient String RCS_ID = "$Id: Routing.java,v 1.29 2008/04/29 16:50:29 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.29 $";
	static protected final transient Log logger = AppLogger.getLogger();

	/**
	 * @param locationRecord
	 *          Störungsort
	 * @return pkey des Vertrages
	 * @throws Exception
	 */
	public static String getContractKey(IDataTableRecord locationRecord) throws Exception
	{
    IDataAccessor accessor = locationRecord.getAccessor().newAccessor();
    
		String contractKey = null;
		String siteKey = locationRecord.getStringValue(Location.site_key);
		String sitepartKey = locationRecord.getStringValue(Location.sitepart_key);
		String buildingKey = locationRecord.getStringValue(Location.building_key);
		String buildingpartKey = locationRecord.getStringValue(Location.buildingpart_key);
    
    final String RELATIONSET = "r_locationcontract";
    
    // Achtung: Wir berücksichtigen nur Einträge von Verträgen, welche aktiv sind.
    //
    IDataTable iContractTable = accessor.getTable(Contract.NAME);
    iContractTable.qbeSetKeyValue(Contract.contractstatus, Contract.contractstatus_ENUM._aktiv);
    
		IDataTable locationContractTable = accessor.getTable(Locationcontract.NAME);
		if (siteKey == null)
		{
			siteKey = "NULL";
		}
		if (sitepartKey == null)
		{
			sitepartKey = "NULL";
		}
		if (buildingKey == null)
		{
			buildingKey = "NULL";
		}
		if (buildingpartKey == null)
		{
			buildingpartKey = "NULL";
		}
		locationContractTable.clear();
		locationContractTable.qbeClear();
		locationContractTable.qbeSetValue(Locationcontract.site_key, siteKey);
		locationContractTable.qbeSetValue(Locationcontract.sitepart_key, sitepartKey);
		locationContractTable.qbeSetValue(Locationcontract.building_key, buildingKey);
		locationContractTable.qbeSetValue(Locationcontract.buildingpart_key, buildingpartKey);
		// optimierte Suche so das nicht unnötig auf NULL mehrmals gesucht wird
		//Suche nach site, sitepart,building, buildingpart
		if (!buildingpartKey.equals("NULL")) // nur suchen wenn vorhanden
		{
			locationContractTable.search(RELATIONSET);
			if (locationContractTable.recordCount() != 1) //Suche nach site, sitepart,building
			{
				locationContractTable.qbeSetValue(Locationcontract.buildingpart_key, "NULL");
				locationContractTable.search(RELATIONSET);
				if (locationContractTable.recordCount() != 1) //  Suche nach site, sitepart
				{
					locationContractTable.qbeSetValue(Locationcontract.building_key, "NULL");
					locationContractTable.search(RELATIONSET);
					if (locationContractTable.recordCount() != 1) //  Suche nach site
					{
						locationContractTable.qbeSetValue(Locationcontract.sitepart_key, "NULL");
						locationContractTable.search(RELATIONSET);
					}
				}
			}
		}
		else
		{
			if (!buildingKey.equals("NULL")) //  nur site, sitepart und building constraint
			{
				locationContractTable.search(RELATIONSET);

				if (locationContractTable.recordCount() != 1) //  Suche nach site,sitepart
				{
					locationContractTable.qbeSetValue(Locationcontract.building_key, "NULL");
					locationContractTable.search(RELATIONSET);
					if (locationContractTable.recordCount() != 1) //  Suche nach site
					{
						locationContractTable.qbeSetValue(Locationcontract.sitepart_key, "NULL");
						locationContractTable.search(RELATIONSET);
					}
				}
			}
			else
			{
				if (!sitepartKey.equals("NULL")) // nur site und sitepart constraint
				{
					locationContractTable.search(RELATIONSET);

					if (locationContractTable.recordCount() != 1) 
					// sitepart
					{
						locationContractTable.qbeSetValue(Locationcontract.sitepart_key, "NULL");
						locationContractTable.search(RELATIONSET);
					}
				}
				else
				{
					locationContractTable.search(RELATIONSET); // nur site constraint
				}
			}
		}


		if (locationContractTable.recordCount() == 1)
		{
			contractKey = locationContractTable.getRecord(0).getStringValue(Locationcontract.contract_key);
		}
    if (logger.isDebugEnabled())
      logger.debug("contractKey=" + contractKey);
		return contractKey;
	}

	/**
	 * gibt QBE-Constraint in Form key1|key2|.. des Gewerkebaums zurück
	 * 
	 * @param accessor
	 * @param categoryKey
	 * @return @throws
	 *         Exception
	 */
	public static String getCategoryConstraint(IDataAccessor accessor, String categoryKey) throws Exception
	{
		StringBuffer categoryConstraint = new StringBuffer();
		categoryConstraint.append(categoryKey);

		IDataAccessor searchAccessor = accessor.newAccessor();
		IDataTable categoryTable = searchAccessor.getTable("category");
		IDataTableRecord categoryRecord;
		int counter = 0; // dirty! need to safe for infinity loop
		// Gewerkdatensatz holen
		categoryTable.qbeClear();
		categoryTable.qbeSetValue("pkey", categoryKey);
		categoryTable.search();
		while (categoryTable.recordCount() == 1 && counter < 100)
		{
			categoryRecord = categoryTable.getRecord(0);
			String parentKey = categoryRecord.getStringValue("parentcategory_key");
			if (null == parentKey)
				break;

			categoryConstraint.append("|").append(parentKey);
			categoryTable.qbeClear();
			categoryTable.qbeSetValue("pkey", parentKey);
			categoryTable.search();
			counter++;
		}
		logger.debug("categoryConstraint=" + categoryConstraint);
		return categoryConstraint.toString();
	}

	/**
	 * die Tätigkeiten des Vertrags und des Gewerks mit Vatergewerke
	 * 
	 * @param accessor
	 * @param categoryKey
	 * @param contractKey
	 * @return die Tätigkeiten des Vertrags und des Gewerks mit Vatergewerke
	 * @throws Exception
	 */
	public static IDataBrowser getProcessBrowser(IDataAccessor accessor, String categoryKey, String contractKey) throws Exception
	{
		IDataBrowser proccessBrowser = accessor.getBrowser("processBrowser");
		IDataTable categoryprocessTable = accessor.getTable("categoryprocess");
		IDataTable processTable = accessor.getTable("process");
		categoryprocessTable.qbeClear();
		categoryprocessTable.qbeSetValue("category_key", getCategoryConstraint(accessor, categoryKey));
		categoryprocessTable.qbeSetValue("contract_key", contractKey);
		processTable.qbeSetValue("processstatus", "Gültig");
		proccessBrowser.search("r_processsearch");
		logger.debug("ProcessBrowser.Recordcount=" + proccessBrowser.recordCount());
		return proccessBrowser;
	}

	/**
	 * die Routingdatensätze
	 * 
	 * @param accessor
	 * @param categoryKey
	 * @param processKey
	 * @param contractKey
	 * @return Collection[AuftragsKoodinator]
	 * @throws Exception
	 */
	public static List getRoutingAK(IDataAccessor accessor, String categoryKey, String processKey, String contractKey) throws Exception
	{
		// create a customizable IDataBrowser
		//
		IDataAccessor newAccessor = accessor.newAccessor();

		AdhocBrowserDefinition browserDef = new AdhocBrowserDefinition(newAccessor.getApplication(), "categoryprocess");
		browserDef.addBrowserField("categoryprocess", "workgroup_key", "workgroup_key", SortOrder.NONE);
		browserDef.addBrowserField("categoryworkgroup", "name", "name", SortOrder.ASCENDING);
		browserDef.addBrowserField("categoryworkgroup", "description", "description", SortOrder.NONE);
		browserDef.addBrowserField("categoryprocess", "routinginfo", "routinginfo", SortOrder.NONE);

		IDataBrowser routingBrowser = newAccessor.createBrowser(browserDef);
		routingBrowser.setMaxRecords(IDataBrowser.UNLIMITED_RECORDS);
		IDataTable categoryprocessTable = newAccessor.getTable("categoryprocess");
		IDataTable categoryworkgroup = newAccessor.getTable("categoryworkgroup");
		categoryprocessTable.clear();
		boolean bFinish = (categoryKey == null) || (contractKey == null) || (processKey == null);
		while (!bFinish)
		{
			categoryprocessTable.qbeClear();
			categoryprocessTable.qbeSetValue("category_key", categoryKey);
			categoryprocessTable.qbeSetValue("process_key", processKey);
			categoryprocessTable.qbeSetValue("contract_key", contractKey);
			categoryworkgroup.qbeClear();
			categoryworkgroup.qbeSetValue("groupstatus", "gültig");
			routingBrowser.search("r_routing", Filldirection.BACKWARD);

			if (routingBrowser.recordCount() == 0)
			{
				categoryKey = DataUtils.getValueWhere(newAccessor, "category", "pkey", categoryKey, "parentcategory_key");
				logger.debug("nächstes Gewerk:" + categoryKey);
				if (categoryKey == null)
				{
					bFinish = true;
				}
			}
			else
			{
				bFinish = true;
			}
		}
		List aks = new ArrayList();
		if (routingBrowser.recordCount() > 0)
		{
			for (int i = 0; i < routingBrowser.recordCount(); i++)
			{
				IDataBrowserRecord record = routingBrowser.getRecord(i);

				// AuftragsKoodinator(Pkey,Name,Description, Routinginfo)
				String routinginfo = record.getSaveStringValue(3);
				aks.add(new AuftragsKoordinator(record.getSaveStringValue(0), record.getSaveStringValue(1), record.getSaveStringValue(2), routinginfo));
			}
		}
		else
		{
			String workgroupKey = DataUtils.getAppprofileValue(newAccessor, "problemmanager_key");
			if (workgroupKey == null)
			{
				throw new Exception("Kein Problemmanager parametriert!");
			}
			String workgroupName = DataUtils.getValueWhere(newAccessor, "workgroup", "pkey", workgroupKey, "name");
			aks.add(new AuftragsKoordinator(workgroupKey, workgroupName, "", "kein Routing gefunden"));
		}
		return aks;
	}
}
