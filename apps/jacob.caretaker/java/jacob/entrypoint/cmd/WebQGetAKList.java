/*
 * Für WebQ müssen bei der Tätigkeitssuche erst die Gewerke-Keys gesucht werden
 * in Abhängigkeit des Vertrags (Ort)
 * Created on 22.06.2004
 * by mike
 *
 */
package jacob.entrypoint.cmd;



import jacob.common.AppLogger;
import jacob.common.data.AuftragsKoordinator;
import jacob.common.data.DataUtils;
import jacob.common.data.Routing;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 * @author mike
 *  
 */
public class WebQGetAKList implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: WebQGetAKList.java,v 1.13 2008/04/29 16:50:29 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.13 $";

	static Log logger = AppLogger.getLogger();

	private void returnProblemmanager(CmdEntryPointContext context, IDataAccessor acessor) throws IOException
	{
		try
		{
			String workgroupKey;
			String workgroupName;
			String ioString;
			workgroupKey = DataUtils.getAppprofileValue(acessor, "problemmanager_key");
			workgroupName = DataUtils.getValueWhere(acessor, "workgroup", "pkey", workgroupKey, "name");
			ioString = workgroupKey + "|" + workgroupName + "|" + "kein Routing in WebQ gefunden\n";

			context.getStream().write(ioString.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType()
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/plain";
	}

	/*
	 * http://localhost:8080/etr/cmdenter?entry=WebQGetAKList&app=caretaker&user=caretaker&pwd=caretaker&category_key=XXX&process_key=YYY&location_key=ZZZ
	 * 
	 * @see de.tif.jacob.entrypoint.ICmdEntryPoint#enter(de.tif.jacob.entrypoint.CmdEntryPointContext,
	 *      java.util.Properties)
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws IOException
	{
		IDataAccessor acessor = context.getDataAccessor();
		try
		{
			String category_key = properties.getProperty("category_key");
			String process_key = properties.getProperty("process_key");
			String location_key = properties.getProperty("location_key");
			IDataTable locationTable = acessor.getTable("location");
			locationTable.qbeClear();
			locationTable.qbeSetValue("pkey", location_key);
			locationTable.search();
			if (locationTable.recordCount() != 1)
			{
				returnProblemmanager(context, acessor);
				return;
			}
			String contract_key = Routing.getContractKey(locationTable.getRecord(0));
			Collection aks = Routing.getRoutingAK(context.getDataAccessor(), category_key, process_key, contract_key);
			Iterator iter = aks.iterator();
			while (iter.hasNext())
			{
				AuftragsKoordinator ak = (AuftragsKoordinator) iter.next();
				String ioString = ak.getPkey() + "|" + ak.getName() + "|" + ak.getRoutingInfo() + "\n";
				context.getStream().write(ioString.getBytes());
			}
		}
		catch (Exception e)
		{
			throw new IOException("InternalError:" + e.toString());
		}
	}

}
