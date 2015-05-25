/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 17 11:41:55 CET 2006
 */
package jacob.event.ui.category;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class CategoryOrgcategoryIFBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: CategoryOrgcategoryIFBrowser.java,v 1.1 2006/02/24 02:16:16 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
	{
		return value;
	}
  
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
	}
}
