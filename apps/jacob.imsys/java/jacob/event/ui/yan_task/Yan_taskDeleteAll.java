/*
 * Created on 16.06.2004
 * by mike
 *
 */
package jacob.event.ui.yan_task;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Schnellbutton um alle yantask Datensätze mit Error = Ja zu löschen
 * @author mike
 *
 */
public class Yan_taskDeleteAll extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Yan_taskDeleteAll.java,v 1.1 2005/06/02 16:29:46 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  class DeleteAllCallback implements IOkCancelDialogCallback
	{
		public void onCancel(IClientContext context)
		{
		}
		public void onOk(IClientContext context) throws Exception
		{
			IDataTransaction trans = context.getDataAccessor().newTransaction(); 
			try
			{	
				IDataTable table = context.getDataTable();
				table.qbeClear();
				table.qbeSetValue("error","Ja");
				table.searchAndDelete(trans);
				trans.commit();
				context.clearDomain();
			}
			finally 
			{
				trans.close();
			}
		}
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IOkCancelDialog dialog = context.createOkCancelDialog("Wollen Sie wirklich alle Datensätze mit Fehlerstatus löschen?",new DeleteAllCallback());
	  dialog.show();
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}
