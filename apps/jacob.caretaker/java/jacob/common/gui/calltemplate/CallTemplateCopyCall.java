package jacob.common.gui.calltemplate;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Aug 11 17:11:36 CEST 2005
 *
 */
import java.util.Vector;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * This is an event handler for the  New-Button.
 * 
 * @author mike
 *
 */
public class CallTemplateCopyCall extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallTemplateCopyCall.java,v 1.3 2005/09/26 10:31:13 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  static private final Vector accessRoles = new Vector();
  
  static
  {
      //accessRoles.add("CQ_PM");
      accessRoles.add("CQ_ADMIN");
      //accessRoles.add("CQ_AGENT");
      //accessRoles.add("CQ_SUPERAK");
      //accessRoles.add("CQ_SDADMIN");
  }


  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the NEW action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

  /**
   * This event method will be called if the NEW action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
        
        // ist ein Call da ?
        
        if (context.getDataTable("call").recordCount()!=1)
        {
           return; 
        }
        IDataTableRecord callTemplate = context.getSelectedRecord();
        IDataTableRecord call = context.getDataTable("call").getRecord(0);
        IDataTransaction trans = callTemplate.getCurrentTransaction();
        callTemplate.setValue(trans,"action",call.getValue("action"));
        callTemplate.setValue(trans,"autoclosed",call.getValue("autoclosed"));
        callTemplate.setValue(trans,"callbackmethod",call.getValue("callbackmethod"));
        callTemplate.setValue(trans,"priority",call.getValue("priority"));
        callTemplate.setValue(trans,"problem",call.getValue("problem"));
        callTemplate.setValue(trans,"problemtext",call.getValue("problemtext"));
        callTemplate.setValue(trans,"name","Vorlage aus Meldung: " + call.getSaveStringValue("pkey"));
	}

  /**
   *
   * @param context The current client context
   * @param status  The current state of the group
   * @param emitter  The corresbonding button/emitter to this event handler
   * 
   */
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement button) throws Exception 
	{
        if (!context.getUser().hasOneRoleOf(accessRoles)|| context.getDataTable("call").recordCount()!=1)
        {
            button.setEnable(false);
        }
        
	}
}
