package jacob.common.gui.sap_objimport;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Aug 14 13:38:59 CEST 2007
 *
 */
import jacob.common.AppLogger;
import jacob.common.sap.CImportObjects;
import jacob.common.sap.CSAPHelperClass;
import jacob.common.sap.ConnManager;
import jacob.common.sap.DateFormater;

import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.DatetimeUtil;

/**
 * The Event handler for the Sap_imp_obj-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author achim
 * 
 */
public class Sap_imp_obj extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Sap_imp_obj.java,v 1.6 2008/01/24 07:41:49 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has been click on the corresponding button.<br>
   * Be in mind: The currentRecord can be null if the button has not the
   * [selected] flag.<br>
   * The selected flag warranted that the event can only be fired if the<br>
   * selectedRecord!=null.<br>
   * 
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    ConnManager oConMan = new ConnManager();
    String sInputFromDate = context.getGroup().getInputFieldValue("fromDate");
    String sObject = context.getGroup().getInputFieldValue("impSapObj");
    String sFromDate;
    String sFromTime;
    // SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    if (sInputFromDate == null || sInputFromDate.equals(""))
    {
      sFromDate = DateFormater.GetStringDate(new Date());
      sFromTime = "000000";
    }
    else
    {
      Date date = DatetimeUtil.convertToTimestamp(sInputFromDate);
      sFromDate = DateFormater.GetStringDate(date);
      sFromTime = DateFormater.GetStringTime(date);
    }
    if (sObject == null || sObject.equals(""))
    {
      sObject = "";
    }
    // System.out.println("FromDate: "+ sFromDate);
    CSAPHelperClass.printDebug("Date " + sFromDate);
    CSAPHelperClass.printDebug("Time " + sFromTime);
    CSAPHelperClass.printDebug("Object " + sObject);
    CImportObjects.getSAPObjects(oConMan, sFromDate, sFromTime, sObject);
    // CImportObjects.getSAPObjStatus(oConMan);
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
   * Be in mind: The currentRecord can be null if the button has not the
   * [selected] flag.<br>
   * The selected flag warranted that the event can only be fired if the<br>
   * selectedRecord!=null.<br>
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
    // You can enable/disable the button in relation to your conditions.
    //
    // button.setEnable(true/false);
  }
}
