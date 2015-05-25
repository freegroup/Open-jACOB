/*
 * Created on 31.08.2004
 * by mike
 *
 */
package jacob.common.gui.errorcodetaskdata;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Setzt einen Defaultwert für errorcodetaskdataInvoicingtext
 * @author mike
 *
 */
public class ErrorcodetaskdataUpdate extends IActionButtonEventHandler
{
static public final transient String RCS_ID = "$Id: ErrorcodetaskdataUpdate.java,v 1.1 2004/08/31 15:21:42 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		if (button.getDataStatus() != IGuiElement.UPDATE) return true;
		
		IDataTableRecord contextRecord = context.getSelectedRecord();
		if (contextRecord.getValue("invoicingtext")== null)
		{
			IDataTable gdserrorcode = context.getDataTable("gdserrorcode");
			if (gdserrorcode.recordCount()==1)
			{
				IDataTransaction mytrans = contextRecord.getCurrentTransaction();
				contextRecord.setValue(mytrans,"invoicingtext",gdserrorcode.getRecord(0).getValue("codedescripition"));
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{

	}

}
