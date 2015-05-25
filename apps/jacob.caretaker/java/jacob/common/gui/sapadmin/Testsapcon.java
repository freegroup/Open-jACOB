package jacob.common.gui.sapadmin;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed May 23 14:31:43 CEST 2007
 *
 */
import jacob.common.AppLogger;
import jacob.common.sap.CResult;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.CTestConnection;
import jacob.common.sap.ConnManager;
import jacob.model.Sapadmin;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the Testsapcon-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author E050_FWT-ANT_o_test
 * 
 */
public class Testsapcon extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Testsapcon.java,v 1.2 2007/08/13 14:54:04 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has been click on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    alert(CTestConnection.TestCon());

  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    if (!status.equals(IGuiElement.SEARCH))
    {
      
      button.setEnable(true);

      IDataTableRecord rec = context.getSelectedRecord();
      if (rec.getSaveStringValue(Sapadmin.active).equals("1"))
      {

        button.setEnable(true);
      }
      else
      {

        button.setEnable(false);
      }
    }
    else
    {

      button.setEnable(false);
    }
  }
  // button.setEnable(true/false);
}
