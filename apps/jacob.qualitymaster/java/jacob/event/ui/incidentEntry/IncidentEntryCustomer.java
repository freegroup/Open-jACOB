/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 21 18:00:30 CEST 2009
 */
package jacob.event.ui.incidentEntry;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;


/**
 *
 * @author andreas
 */
public final class IncidentEntryCustomer extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: IncidentEntryCustomer.java,v 1.1 2009-07-21 20:51:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
	{
		return true;
	}
  
	public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreignField) throws Exception
	{
	}
  
	public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
	{
	}

  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // Kunden dürfen den Ersteller erst mal nicht ändern
    IForeignField foreignField = (IForeignField) element;
    foreignField.setEditable(state == IGuiElement.SEARCH);
  }
}
