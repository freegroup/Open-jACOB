/*
 * Created on Mar 31, 2004
 *
 */
package jacob.entrypoint.gui;

import java.security.GeneralSecurityException;
import java.util.Properties;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.screen.IClientContext;

/**
 * @author Mike
 *
 */
public class NewCall extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: NewCall.java,v 1.2 2004/07/12 06:52:36 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 * @author Mike
	 */
	public void enter(IClientContext context, Properties props) throws Exception
	{
			IDataTable table = context.getDataTable("customerint");
			table.qbeSetValue("pkey", context.getUser().getKey());
			IDataBrowser browser = context.getDataAccessor().getBrowser("customerintBrowser");
			browser.search("r_customer",Filldirection.BOTH);
			if (browser.recordCount() == 1)
			{
				browser.setSelectedRecordIndex(0);
				browser.propagateSelections();
			}
			else
			{
				// should never happen
				// TARRAGON: throw an exception			
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
		return "customercall";
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
		return false;
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
