/*
 * Created on Mar 31, 2004
 *
 */
package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;


/**
 * @author Mike
 *
 */
public class ShowMyCalls extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: ShowMyCalls.java,v 1.6 2004/08/13 16:23:01 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 * @author Mike
	 */
	public void enter(IClientContext context, Properties props) throws Exception
	{
			IDataTable table = context.getDataTable("call");
			table.qbeSetValue("employeecall", context.getUser().getKey());
			table.qbeSetValue("callstatus","Rückruf|Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen");	
			IDataBrowser browser = context.getDataAccessor().getBrowser("custinfocallBrowser");
			browser.search("r_call",Filldirection.BOTH);

			if (browser.recordCount() >= 1)
			{
				browser.setSelectedRecordIndex(0);
				browser.propagateSelections();
			}
			else if (browser.recordCount() == 0)
			{
				alert("Zur Zeit sind keine Aufträge von Ihnen in Bearbeitung");
			}
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getDomain()
	 * @author Mike
	 */
	public String getDomain()
	{
		return "f_custinfo";
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm()
	 * @author Mike
	 */
	public String getForm()
	{
		return "custinfo";
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasNavigation()
	 * @author Mike
	 */
	public boolean hasNavigation()
	{
		return false;
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasSearchBrowser()
	 * @author Mike
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasToolbar()
	 * @author Mike
	 */
	public boolean hasToolbar()
	{
		return false;
	}
}
