/*
 * Created on 24.04.2004
 *
 */
package jacob.common.gui.category;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * kopiert den Ort des Objects (objectlocation) in den Alias location  <br>
 * und zeigt Form locationAgent an
 * @author achim
 *
 */
public class CategoryGotoLocation extends IButtonEventHandler 
{
	static protected final transient Log logger = AppLogger.getLogger();
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTable locationTable = context.getDataTable("location");
		IDataTransaction trans = locationTable.startNewTransaction();
		
    IDataTable objectLocation = context.getDataTable("objectlocation");
		if (objectLocation.recordCount() == 1)
		{
			IDataTableRecord objectLocationRecord = objectLocation.getRecord(0);
			IDataAccessor accessor = context.getDataAccessor();
			IDataTableRecord clonedRecord = accessor.cloneRecord(trans, objectLocationRecord, "location");
			if (logger.isDebugEnabled())
				logger.debug("Objektlocation kopiert");

			accessor.propagateRecord(clonedRecord, Filldirection.BACKWARD);
		}
		else
		{
			locationTable.newRecord(trans);
		}
		context.setCurrentForm("locationAgent");	  
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status,	IGuiElement button)throws Exception 
	{
	}
}
