/*
 * Created on Jun 11, 2004
 *
 */
package jacob.event.ui.incident;

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
public class IncidentState extends IComboBoxEventHandler
{
  static public  final transient String RCS_ID = "$Id: IncidentState.java,v 1.1 2005/08/31 21:05:44 herz Exp $";
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
  public void onSelect(IClientContext context, IComboBox emitter)
  {
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
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
  {
    IComboBox comboBox =(IComboBox)emitter;
    // Always use the default state during the creation of a record
    //
    if(state==IGuiElement.NEW)
    	comboBox.setEnable(false);
  }
}