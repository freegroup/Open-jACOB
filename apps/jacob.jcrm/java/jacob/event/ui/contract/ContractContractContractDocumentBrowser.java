/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 19 03:29:00 CEST 2005
 */
package jacob.event.ui.contract;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class ContractContractContractDocumentBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, java.lang.String)
	 */
	public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#onRecordSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    context.createDocumentDialog(selectedRecord.getDocumentValue("docfile")).show();
	}
}
