/*
 * Created on Mar 14, 2004
 *
 */
package jacob.common.gui.task;

import jacob.common.AppLogger;
import jacob.common.Edvin;
import jacob.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author Andreas Herz
 *
 */
public  class TaskGetSchadenscode extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: TaskGetSchadenscode.java,v 1.2 2004/11/16 18:36:34 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  static Log logger = AppLogger.getLogger();
  
  /* 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientContext, de.tif.jacob.screen.Button)
	 * @author Andreas Herz
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
    // generate the data itself. Retrieve them from the EDVIN database (5-Felder)
    // via native SQL, SOAP, ......
    // 
    IDataTableRecord taskworkGroup =context.getDataAccessor().getTable("taskworkgroup").getSelectedRecord();
    IDataTableRecord extSystem     =context.getDataAccessor().getTable("ext_system").getSelectedRecord();

    if(taskworkGroup!=null && extSystem!=null)
    {   
      // create the dialog
      //
      IGridTableDialog dialog = context.createGridTableDialog(button);

      // create the header for the selection grid dialog
      //
      String[] header = new String[]{"Code" , "Beschreibung"};
      dialog.setHeader(header);
      
      // retrieve the element where you want to fill with the user selection
      //
      ISingleDataGuiElement codeInput =(ISingleDataGuiElement) context.getGroup().findByName("taskEdvin_sha_code");
      ISingleDataGuiElement descInput =(ISingleDataGuiElement) context.getGroup().findByName("taskEdvin_sha_bez");
      
      
      String edvin = extSystem.getStringValue("name");
      String hwg   = taskworkGroup.getStringValue("hwg_name");
      String code  = codeInput.getValue();
      String desc  = descInput.getValue();
      
      dialog.setData(Edvin.getSchadencode(edvin,hwg,code,desc));
      
      // Connect the GUIELement with column numbers
      //
      dialog.connect(0,codeInput);
      dialog.connect(1,descInput);
      
      // show the dialog
      //
      dialog.show(300,200);
    }
    else
    {
      context.createMessageDialog("Kein Handwerkergrupper und externes System definiert.").show();
    }
	}

	/* 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.ClientContext, int, de.tif.jacob.screen.Button)
	 * @author Andreas Herz
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		button.setEnable(status==IGuiElement.NEW || status==IGuiElement.UPDATE );
	}
}
