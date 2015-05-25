package jacob.event.ui.license;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri May 13 16:30:41 CEST 2005
 *
 */
import jacob.common.license.LicenseHelper;
import jacob.model.Engine;
import jacob.model.License;
import jacob.model.License_creator;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;

/**
 * This is an event handler for the New-Button.
 * 
 * @author andherz
 * 
 */
public class LicenseButtonNewRecord extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: LicenseButtonNewRecord.java,v 1.2 2006/03/07 19:20:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * This event handler will be called if the corresponding button has been
   * pressed. You can prevent the execution of the NEW action if you return
   * [false].<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action
   *         else return [true]
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    IDataTable engineTable = context.getDataTable(Engine.NAME);
    IDataTableRecord engineRecord = engineTable.getSelectedRecord();
    if (null == engineRecord)
    {
      context.createMessageDialog(new ApplicationMessage("LicenseButtonNewRecord.SelectEngine")).show();
      return false;
    }

    return LicenseHelper.uploadPrivateKeyIfNecessary(context, engineRecord, new LicenseHelper.LoadCallback()
    {
      /*
       * (non-Javadoc)
       * 
       * @see jacob.common.license.LicenseHelper.LoadCallback#afterLoad(de.tif.jacob.screen.IClientContext)
       */
      public void afterLoad(IClientContext context) throws Exception
      {
        // create new license key record
        IDataTable licenseTable = context.getDataTable(License.NAME);
        IDataTransaction transaction = licenseTable.startNewTransaction();
        licenseTable.newRecord(transaction);

        // initialize creator record
        setLicenseCreator(context);
      }
    });
  }

  private static void setLicenseCreator(IClientContext context) throws Exception
  {
    // creator setzen
    IDataTable table = context.getDataTable(License_creator.NAME);
    IUser user = context.getUser();
    table.qbeClear();
    table.qbeSetKeyValue(License_creator.pkey, user.getKey());
    table.search();
  }

  /**
   * This event method will be called if the NEW action has been successfully
   * done.<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    setLicenseCreator(context);
  }

  /**
   * @param context
   *          The current client context
   * @param status
   *          The current state of the group
   * @param emitter
   *          The corresbonding button/emitter to this event handler
   * 
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {
  }
}
