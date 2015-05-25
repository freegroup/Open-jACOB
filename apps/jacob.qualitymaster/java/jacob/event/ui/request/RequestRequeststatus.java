/*
 * Created on Jun 11, 2004
 *
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author andherz
 */
public class RequestRequeststatus extends IComboBoxEventHandler
{
  static public  final transient String RCS_ID = "$Id: RequestRequeststatus.java,v 1.4 2008/06/24 14:15:28 sonntag Exp $";
  static public  final transient String RCS_REV = "$Revision: 1.4 $";
  
  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Called if the user changed the selection during the NEW or UPDATE state 
   * of the related table record.
   * 
   * @param context The current work context of the jACOB application. 
   * @param emitter The emitter of the event.
   */
  public void onSelect(IClientContext context, IComboBox emitter) throws Exception
  {
    String comboState =emitter.getValue();
		ISingleDataGuiElement field = (ISingleDataGuiElement)context.getGroup().findByName("requestOwner");
   	field.setRequired(!"New".equals(comboState));
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
    IDataTableRecord record =context.getSelectedRecord();
    if(record!=null)
    {
      String comboState =record.getStringValue("state");
      comboBox.enableOptions(false);
      
      // nur der Tester eines Records darf den Status "QA" eines Request ändern
      //
        
      if(status==IGuiElement.NEW)
      {  
        comboBox.enableOption("New",true);
        comboBox.enableOption("Proved",true);
        comboBox.enableOption("In progress",true);
        comboBox.enableOption("QA",true);
      }
      // Allgemeine Statusübergange
      //
      else if(status==IGuiElement.UPDATE)
      {  
       	// TODO: Macht der Status "QA Customer" irgendwelchen Sinn, wenn der Kunde den Request (d.h. die Aufgabe)
        // gar nicht sieht?
        if(comboState.equals("New"))
        {  
          comboBox.enableOption("New",true);
          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
          comboBox.enableOption("Obsolete",true);
          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("Proved") )
        {  
//          comboBox.enableOption("New",true);
          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
          comboBox.enableOption("Obsolete",true);
          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("In progress"))
        {  
//          comboBox.enableOption("New",true);
          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
          comboBox.enableOption("Obsolete",true);
          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("QA") && !context.getUser().getKey().equals(record.getSaveStringValue("tester_key")))
        {
          // nur der Tester darf den status QA verlassen
//          comboBox.enableOption("New",true);
//          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
//          comboBox.enableOption("Obsolete",true);
//          comboBox.enableOption("Declined",true);
//          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("QA") )
        {  
//          comboBox.enableOption("New",true);
//          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
//          comboBox.enableOption("Obsolete",true);
//          comboBox.enableOption("Declined",true);
//          comboBox.enableOption("Duplicate",true);
          comboBox.enableOption("Done",true);
        }
        else if(comboState.equals("QA Customer") )
        {  
//          comboBox.enableOption("New",true);
//          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
          comboBox.enableOption("QA Customer",true);
//          comboBox.enableOption("Obsolete",true);
//          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Done",true);
//          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("Obsolete") )
        {
//          comboBox.enableOption("New",true);
//          comboBox.enableOption("Proved",true);
//          comboBox.enableOption("In progress",true);
//          comboBox.enableOption("QA",true);
//          comboBox.enableOption("QA Customer",true);
//          comboBox.enableOption("Obsolete",true);
//          comboBox.enableOption("Declined",true);
//          comboBox.enableOption("Duplicate",true);
        }
        else if(comboState.equals("Declined") )
        {
        	/*
          comboBox.enableOption("New",true);
          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
          comboBox.enableOption("QA Customer",true);
          comboBox.enableOption("Obsolete",true);
          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Duplicate",true);
          */
        }
        else if(comboState.equals("Duplicate") )
        {   
//          comboBox.enableOption("New",true);
//          comboBox.enableOption("Proved",true);
          comboBox.enableOption("In progress",true);
          comboBox.enableOption("QA",true);
          comboBox.enableOption("QA Customer",true);
          comboBox.enableOption("Obsolete",true);
          comboBox.enableOption("Declined",true);
          comboBox.enableOption("Duplicate",true);
        }
        else
        {
        }
      }
      else
      {  
        comboBox.enableOptions(true);
      }
    }
    else
      comboBox.enableOptions(true);
  }
}
