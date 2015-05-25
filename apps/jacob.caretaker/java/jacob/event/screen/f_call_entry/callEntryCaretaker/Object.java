/*
 * Created on 27.09.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entry.callEntryCaretaker;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;

/**
 * Businessregel: Objektgewerk in der Meldung vorblenden, wenn kein <br>
 * Meldungsgewerk selektiert ist.
 * @author mike
 *
 */
public class Object extends IGroupListenerEventHandler
{
static public final transient String RCS_ID = "$Id: Object.java,v 1.1 2004/09/29 17:06:21 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		if (status != IGuiElement.SELECTED) return;
		
		IDataTable category = context.getDataTable("category");
		IDataTableRecord objectRec = context.getSelectedRecord();
		IDataAccessor accessor = context.getDataAccessor();
		if (category.recordCount() !=1 && objectRec.getValue("objectcategory_key")!=null)
		{
			category.qbeClear();
			category.clear();
			category.qbeSetKeyValue("pkey",objectRec.getValue("objectcategory_key") );
			category.search();
			if (category.recordCount()==1)
			{
				IRelationSet categoryReleationset = accessor.getApplication().getRelationSet("r_category");
				accessor.propagateRecord(category.getRecord(0),categoryReleationset,Filldirection.BACKWARD);
			}
		}
		IDataTable accountingcode = context.getDataTable("accountingcode");
		if (accountingcode.recordCount() !=1 && objectRec.getValue("accountingcode_key")!=null)
		{
			accountingcode.qbeClear();
			accountingcode.clear();
			accountingcode.qbeSetKeyValue("code",objectRec.getValue("accountingcode_key") );
			accountingcode.search();
			// suche ist local und somit kein propagate notwendig
		}
	}
}
