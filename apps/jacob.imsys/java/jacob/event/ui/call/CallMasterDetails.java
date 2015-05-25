package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Jun 03 15:21:03 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;



 /**
  * The Event handler for the CallMasterDetails-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallMasterDetails extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallMasterDetails.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
    public void onAction(IClientContext context, IGuiElement button)  throws Exception
    {
      String masterKey = context.getSelectedRecord().getStringValue("mastercall_key");
      context.clearDomain();
      IDataBrowser browser =context.getDataBrowser();
      IDataTable   callTable=context.getDataTable();
      callTable.qbeSetValue("pkey",masterKey);
      browser.search("r_call", Filldirection.BOTH);
      context.getGUIBrowser().setData(context, browser);
    }

    
    /* 
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
    {
      emitter.setEnable(status==IGuiElement.SELECTED &&(context.getSelectedRecord().getValue("mastercall_key")!=null));
    }
}