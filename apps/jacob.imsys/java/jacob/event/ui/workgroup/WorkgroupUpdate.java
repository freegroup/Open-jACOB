package jacob.event.ui.workgroup;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 02 13:56:25 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * This is an event handler for a update button.
 * 
 * @author mike
 *
 */
public class WorkgroupUpdate extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: WorkgroupUpdate.java,v 1.1 2005/06/02 16:29:45 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler will be called if the corresponding button has been pressed.
   * You can prevent the execution of the update action if you return [false].<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action else return [true]
   */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
        // Der Update-Button ist ein Tri-State Button. Man mus sich den Status der
        // Gruppe ansehen.
        //
        if(context.getGroup().getDataStatus()==IGuiElement.UPDATE)
        {  
              String hwg_name     = context.getGroup().getInputFieldValue("workgroupHwg_name");
              String selectedPkey = context.getSelectedRecord().getStringValue("pkey");
              
              // Neuen DatenAccessor anlegen um die derzeitigen ƒnderungen/Vorg‰ngen in dem 
              // Standart Accessor nicht zu ver‰ndern. SEHR wichtig!
              //
              IDataAccessor accessor=context.getDataAccessor().newAccessor();
              IDataTable workgroup=accessor.getTable("workgroup");
              
              // Gibt es eine HWG-Gruppe mit dem selben Namen und ist es NICHT der gerade
              // selektierte Datensatz?
              //
              if(hwg_name!=null && hwg_name.length()>0)
              { 
                  workgroup.qbeSetKeyValue("hwg_name",hwg_name);
                  workgroup.qbeSetValue("pkey","!"+selectedPkey);
                  if(workgroup.search()>0)
                  {
                    // Fehlermeldung anzeigen .....
                  IMessageDialog dialog =context.createMessageDialog("Das Feld 'HWG-EDVIN' muﬂ eindeutig sein. Eintrag ist bereits vergeben.");
                  dialog.show();
                  // .... und speichern verhindern.
                  return false;
                  }
               }
        }
         
            return true;
        }

  /**
   * This event method will be called if the update action has been successfully done.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	  // the record has been successfull switch to the update mode or has been successfull saved
	  //
	}

  /**
   * The event handle if the status of the group has been changed.
   * This is a good place to enable/disable the button on relation to the
   * group state.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param button  The corresbonding button to this event handler
   * 
   */
	public void onGroupStatusChanged(IClientContext context, GroupState status,	IGuiElement button) throws Exception 
	{
	  // you can enable/disable the update button
	  //
	  //button.setEnable(true/false);
	}
}
