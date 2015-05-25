/*
 * Created on Jun 11, 2004
 *
 */
package jacob.event.ui.request_enabler;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andherz
 */
public class Request_enablerState extends IComboBoxEventHandler
{
  static public  final transient String RCS_ID = "$Id: Request_enablerState.java,v 1.1 2007/11/25 22:19:38 freegroup Exp $";
  static public  final transient String RCS_REV = "$Revision: 1.1 $";
  
  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Called if the user changed the selection during the NEW or UPDATE state 
   * of the related table record.
   * 
   * @param context The current work context of the jACOB application. 
   * @param emitter The emitter of the event.
   */
  public void onSelect(IClientContext context, IComboBox comboBox) throws Exception
  {
  	IGuiElement.GroupState status = context.getGroup().getDataStatus();
    if(status==IGuiElement.UPDATE || status==IGuiElement.NEW)
    {
    	IGuiElement flag1 = context.getGroup().findByName("request_flag1");
    	IGuiElement flag2 = context.getGroup().findByName("request_flag2");
    	IGuiElement flag3 = context.getGroup().findByName("request_flag3");
    	IGuiElement flag4 = context.getGroup().findByName("request_flag4");
    	
    	flag1.setEnable(false);
    	flag2.setEnable(false);
    	flag3.setEnable(false);
    	flag4.setEnable(false);

    	if(comboBox.getValue().equals("open"))
    	{
      	flag1.setEnable(true);
      	return;
    	}
    	if(comboBox.getValue().equals("closed"))
    	{
      	flag2.setEnable(true);
      	return;
    	}
    	if(comboBox.getValue().equals("progress"))
    	{
      	flag3.setEnable(true);
      	return;
    	}
    	if(comboBox.getValue().equals("rejected"))
    	{
      	flag4.setEnable(true);
      	return;
    	}
    }
  }

  /**
   * The event handler if the group status has been changed.<br>
   * This is a good place to enabel/disable some combo box entries in relation to the state of the
   * selected record.<br>
   * <br>
   * Note: You can only enabel/disable <b>valid</b> enum values of the corresponding table field.<br>
   * <br>
   * <code>
	 *	 IComboBox comboBox =(IComboBox)emitter;
	 *	 // remove all como box entries
	 *	 comboBox.enableOptions(false);
	 *   if(..some condition...)
	 *   {  
	 *     comboBox.enableOption("Duplicate",true);
	 *     comboBox.enableOption("Declined",true);
	 *   }
	 *   else if(...another condition...)
	 *   {  
	 *     comboBox.enableOption("Proved",true);
	 *     comboBox.enableOption("In progress",true);
	 *     comboBox.enableOption("QA",true);
	 *   }
	 *   else // enable all options
	 *   	comboBox.enableOptions(true);
   * 
   * </code>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
  {
    IComboBox comboBox =(IComboBox)emitter;
    // An existing request is in the UPDATE mode
    // Implement some business logic for the GUI.
    //
    
  }
}
