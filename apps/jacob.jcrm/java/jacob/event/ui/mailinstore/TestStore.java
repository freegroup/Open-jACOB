package jacob.event.ui.mailinstore;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Oct 07 13:51:41 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.scheduler.system.EmailInTask;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the TestStore-Button. <br>
 * The onAction will be called, if the user clicks on this button. <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author andreas
 *  
 */
public class TestStore extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: TestStore.java,v 1.1 2005/10/12 15:29:01 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord mailinstoreRecord = context.getSelectedRecord();

    try
    {
      int messages = EmailInTask.testStore(mailinstoreRecord);
      context.createMessageDialog(new ApplicationMessage("MAILSTORE_TEST_SUCCESSFUL", new Integer(messages))).show();
    }
    catch (Exception ex)
    {
      throw new UserException(new ApplicationMessage("MAILSTORE_TEST_FAILED", ex.toString()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement.GroupState,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
