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
public interface ICallAction
{
	public void performe(IClientContext context, IDataTable callTable, IGuiElement button) throws Exception;
}
