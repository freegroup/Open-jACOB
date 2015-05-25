/*
 * Created on 19.07.2004
 *
 */
package jacob.common.gui.task;

import jacob.model.Ext_system;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * @author achim
 * Bei Klicken auf einen Auftrag im IFB-Browser wird dieser in der Maske 
 * Edvin Auftrag bearbeiten angezeigt. (Anm: Fat Client geht in GDS Auftrag)
 */
public class TaskIFBrowser extends IBrowserEventHandler {

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, java.lang.String)
	 */
	public String filterCell(IClientContext context, IBrowser browser, int row,
			int column, String data) throws Exception {

		return data;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#onRecordSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void onRecordSelect(IClientContext context, IBrowser browser,
			IDataTableRecord selectedRecord) throws Exception {

		
		String pkey = selectedRecord.getStringValue("pkey");
		IDataBrowser taskbrowser;

		taskbrowser = context.getDataBrowser("taskBrowser");
		
			

		context.clearDomain();
		IDataTable tasktable = context.getDataTable("task");
		tasktable.qbeSetValue("pkey", pkey);
		
		taskbrowser.search("r_task", Filldirection.BOTH);
		taskbrowser.setSelectedRecordIndex(0);
		taskbrowser.propagateSelections();
    String targetForm = null;

		if (tasktable.getRecord(0).hasLinkedRecord(Ext_system.NAME))
		{
			IDataTableRecord extSystem = tasktable.getRecord(0).getLinkedRecord(Ext_system.NAME);
      if (Ext_system.systemtype_ENUM._EDVIN.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskEdvin";
      }       
      else if (Ext_system.systemtype_ENUM._SAPiPRO.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskSAP";
      }    
      else if (Ext_system.systemtype_ENUM._virtuell.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskSAP";
      }     
		}
    if (targetForm != null)
      context.setCurrentForm(targetForm);
	}

}
