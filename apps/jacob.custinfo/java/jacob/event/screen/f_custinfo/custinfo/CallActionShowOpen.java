/*
 * Created on 06.05.2004
 * by mike
 *
 */
package jacob.event.screen.f_custinfo.custinfo;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 * 
 * @author mike
 *
 */
public class CallActionShowOpen implements ICallAction
{
	private String label;
	public CallActionShowOpen(String newButtonLabel)
	{
		label = newButtonLabel;
	}
	
	/* (non-Javadoc)
	 * @see jacob.event.screen.f_custinfo.customercall.ICallAction#performe(de.tif.jacob.screen.IClientContext, de.tif.jacob.core.data.IDataTable, de.tif.jacob.screen.IGuiElement)
	 */
	public void performe(IClientContext context, IDataTable callTable,IGuiElement button) throws Exception
	{
		String userKey = context.getUser().getKey();

		// set all search criteria
		//
		callTable.qbeSetValue("employeecall", userKey);
		
		callTable.qbeSetValue("callstatus", "Rückruf|Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen");
		context.getGroup().setLabel(button.getLabel());
		button.setLabel(label);
	}
}
