package jacob.event.ui.engine;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri May 13 16:37:11 CEST 2005
 *
 */
import jacob.model.Engine;
import jacob.model.Engine_creator;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.license.EncryptionUtil;
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
public class EngineButtonNewRecord extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: EngineButtonNewRecord.java,v 1.3 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

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
    IDataTableRecord engineRecord = context.getSelectedRecord();
    if (engineRecord != null)
    {
      String version = engineRecord.getSaveStringValue(Engine.version);
      context.createMessageDialog(new ApplicationMessage("EngineButtonNewRecord.UseExistingKey", version)).show();
    }
    else
    {
      context.createMessageDialog(new ApplicationMessage("EngineButtonNewRecord.CreateNewKey")).show();
    }
    return true;
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
    // creator setzen
    IDataTable table = context.getDataTable(Engine_creator.NAME);
    IUser user = context.getUser();
    table.qbeClear();
    table.qbeSetKeyValue(Engine_creator.pkey, user.getKey());
    table.search();
    
    IDataTableRecord engineRecord = context.getSelectedRecord();
    if (engineRecord.getDocumentValue(Engine.public_key) != null)
    {
      // bestehender Key wird belassen
    }
    else
    {
      EncryptionUtil keygen = new EncryptionUtil();
      keygen.generateKeys();
      DataDocumentValue doc = DataDocumentValue.create("public" + engineRecord.getValue(Engine.pkey) + ".key", keygen.getPublic().getEncoded());
      engineRecord.setDocumentValue(engineRecord.getCurrentTransaction(), Engine.public_key, doc);

      context.createDocumentDialog(null, "private" + engineRecord.getValue(Engine.pkey) + ".key", keygen.getPrivate().getEncoded()).show();
    }
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
