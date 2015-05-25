/*
 * Created on 31.08.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entry.callEntryCaretaker;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
/**
 * kopiert wenn vorhanden den Ort des Objekts <br>
 * wenn kein Objektort vorhanden ist, dann ist er ein <br>
 * normaler New-Button
 * @author mike
 *
 */
public class LocationCopy extends IActionButtonEventHandler
{
static public final transient String RCS_ID = "$Id: LocationCopy.java,v 1.2 2005/03/03 15:38:12 sonntag Exp $";
static public final transient String RCS_REV = "$Revision: 1.2 $";
static protected final transient Log logger = AppLogger.getLogger();
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		IDataTable objectlocationTable = context.getDataTable("objectlocation");
		if (objectlocationTable.recordCount()!=1)
		{
			// normaler New Button
			return true;
		}
		//Objektort kopieren und anzeigen
		IDataTable locationTable = context.getDataTable("location");
		IDataTransaction trans = locationTable.startNewTransaction();
		try
		{
			IDataTableRecord objectLocationRecord = objectlocationTable.getRecord(0);
			IDataAccessor accessor = context.getDataAccessor();
			IDataTableRecord clonedRecord = accessor.cloneRecord(trans, objectLocationRecord, "location");
			trans.commit();
			if (logger.isDebugEnabled())
				logger.debug("Objektlocation kopiert");
			accessor.propagateRecord(clonedRecord, Filldirection.BACKWARD);	
		}
		finally
		{
			trans.close();
		}
		// New-Action anbrechen, da der Ort ja da ist.
		return false;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
		

	}

}
