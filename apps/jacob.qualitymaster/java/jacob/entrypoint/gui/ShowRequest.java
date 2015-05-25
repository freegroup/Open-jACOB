/*
 * Created on Mar 31, 2004
 *  
 */
package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * @author Andreas Herz
 *  
 */
public class ShowRequest extends IGuiEntryPoint
{
	static public final transient String RCS_ID = "$Id: ShowRequest.java,v 1.10 2006/03/20 10:37:52 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.10 $";
	/*
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 */
	public void enter(IClientContext context, Properties props)throws Exception
	{
		  IDataTable table = context.getDataTable("request");
			table.qbeSetValue("pkey", props.getProperty("pkey"));
			table.search();
			if (table.recordCount() == 1)
			{
				IDataTableRecord userRecord = table.getRecord(0);
				context.getDataAccessor().propagateRecord(userRecord, Filldirection.BOTH);
			}
			else
			{
				// the elements will be displayed in the related search browser
				// automaticly.
				//
			}
	}

	/*
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getFocus() @author
	 *      Andreas Herz
	 */
	public String getDomain()
	{
		return "qualitymaster";
	}

	/*
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm() @author Andreas
	 *      Herz
	 */
	public String getForm()
	{
		return "request";
	}

	/*
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasNavigation() @author
	 *      Andreas Herz
	 */
	public boolean hasNavigation()
	{
		return true;
	}

	/*
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasSearchBrowser()
	 *      @author Andreas Herz
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/*
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasToolbar() @author
	 *      Andreas Herz
	 */
	public boolean hasToolbar()
	{
		return true;
	}
}
