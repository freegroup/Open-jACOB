/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Mar 04 01:33:18 CET 2006
 */
package jacob.event.ui.license;

import jacob.common.license.LicenseHelper;
import jacob.model.Engine;
import jacob.model.License;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the LicenseButtonUpdateRecord update button.<br>
 * 
 * @author andreas
 */
public class LicenseButtonUpdateRecord extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: LicenseButtonUpdateRecord.java,v 1.1 2006/03/07 19:20:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    IDataTableRecord licenseRecord = context.getSelectedRecord();

    boolean enhanced = License.type_ENUM._enhanced.equals(licenseRecord.getValue(License.type));
    if (enhanced && context.getGroup().getDataStatus() == IGuiElement.UPDATE)
    {
      // only enhanced license could be updated regarding new license key
      // generation

      IDataTableRecord engineRecord = licenseRecord.getLinkedRecord(Engine.NAME);

      return LicenseHelper.uploadPrivateKeyIfNecessary(context, engineRecord, new LicenseHelper.LoadCallback()
      {
        public void afterLoad(IClientContext context) throws Exception
        {
          IDataTableRecord licenseRecord = context.getSelectedRecord();
          IDataTransaction trans = licenseRecord.getCurrentTransaction();
          if (trans != null)
            trans.commit();
        }
      });
    }
    return true;
  }

  /**
   * This event method will be called, if the UPDATE action has been successfully executed.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   *     <li>IGuiElement.UPDATE</li>
   *     <li>IGuiElement.NEW</li>
   *     <li>IGuiElement.SEARCH</li>
   *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
