/*
 * Created on 18.06.2004
 * by mike
 *
 */
package jacob.common.data;

import jacob.common.AppLogger;
import jacob.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.screen.IClientContext;

/**
 * Diese Klasse implementiert den Routingmechanismus von DaimlerChrysler 1. der
 * Vertrag ist ortsabhängig 2. die Tätigkeit ist abhängig vom Gewerk und Ort
 * 
 * @author mike
 *  
 */
public class Routing
{
	static public final transient String RCS_ID = "$Id: Routing.java,v 1.2 2005/06/27 12:23:20 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
	static protected final transient Log logger = AppLogger.getLogger();
    
    
    public static String getCustomerContractKey(IClientContext context) throws Exception
    {
        String contractKey = null;
        if (context.getDataTable("customerint").recordCount()!=1)
        {
            // kein Kunde also nichts zu finden
            return contractKey;
        }
        IDataTableRecord customerRec = context.getDataTable("customerint").getRecord(0);
        String site = customerRec.getSaveStringValue("emplsite_keycorr");
        String hpc = customerRec.getSaveStringValue("hpccorr");
        String accountingcode = null;
        IDataTable sitehpcaccounting = context.getDataTable("sitehpcaccounting");
        
        while (hpc.length()>0)
        {
            sitehpcaccounting.qbeClear();
            sitehpcaccounting.clear();
            sitehpcaccounting.qbeSetKeyValue("employeesite_key",site);
            sitehpcaccounting.qbeSetValue("hpc_initial","^"+hpc);
            sitehpcaccounting.search();
            if (sitehpcaccounting.recordCount() == 1)
            {
                hpc =""; // Schleife verlassen
                accountingcode = sitehpcaccounting.getRecord(0).getStringValue("accountingcode_key");
            }
            else
            {
               hpc = StringUtils.left(hpc,hpc.length()-1); 
            }
        }
        if (accountingcode !=null)
        {
            IDataTable accountingCodeTable = context.getDataTable("accountingcode");
            accountingCodeTable.qbeClear();
            accountingCodeTable.clear();
            accountingCodeTable.qbeSetKeyValue("code",accountingcode);
            accountingCodeTable.search();
            if (accountingCodeTable.recordCount() == 1)
            {
                contractKey =accountingCodeTable.getRecord(0).getStringValue("contract_key");
            }
        }
        logger.debug("getCustomerContractKey=" + contractKey);
        return contractKey;
    }

	/**
	 * @param accessor
	 * @param locationRecord
	 *          Störungsort
	 * @return pkey des Vertrages
	 * @throws Exception
	 */
	public static String getContractKey(IClientContext context) throws Exception
	{
		String contractKey = null;
        if (context.getDataTable("accountingcode").recordCount()==1)
        {
            // wenn am Accountingcode einer hängt nehmen
            contractKey =  context.getDataTable("accountingcode").getRecord(0).getStringValue("contract_key");
        }
        else
        {
            contractKey = getCustomerContractKey (context);
        }
        // DefaultContract falls kein contract gefunden
        if (contractKey == null)
        {
            contractKey = DataUtils.getAppprofileValue(context,"contract_key");
            if (contractKey == null)
                throw new BusinessException("Routing nicht möglich: Es ist kein Standardvertrag in der Administration definiert."
                        +"\nWenden Sie sich an den Administrator");
        }
        
        //MIKE: Vertrag finden
        if(logger.isDebugEnabled())
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
	public static IDataBrowser getProcessBrowser(IDataAccessor accessor, String categoryKey) throws Exception
	{
		IDataBrowser proccessBrowser = accessor.getBrowser("processBrowser");
		IDataTable categoryprocessTable = accessor.getTable("categoryprocess");
		IDataTable processTable = accessor.getTable("process");
		categoryprocessTable.qbeClear();
		categoryprocessTable.qbeSetValue("category_key", getCategoryConstraint(accessor, categoryKey));
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
                if (logger.isDebugEnabled())
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
		return aks;
	}
}
