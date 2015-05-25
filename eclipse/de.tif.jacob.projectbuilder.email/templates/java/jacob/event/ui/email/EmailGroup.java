/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 17 23:06:22 CET 2006
 */
package jacob.event.ui.email;

import jacob.common.AppLogger;
import jacob.common.EmailAddress;
import jacob.model.Configuration;
import jacob.model.Email;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 * 
 * @author andherz
 */
public class EmailGroup extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: EmailGroup.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  public void checkEmailAdressInAddressBook(IClientContext context, IDataTableRecord mailRecord) throws Exception
  {
    Integer saveIncomingEmailaddress = (Integer) context.getUser().getProperty(Configuration.save_incoming_emailaddress);
    if (saveIncomingEmailaddress.intValue() == 1)
    {
      String emailAddress = mailRecord.getSaveStringValue(Email.from);
      EmailAddress.add(context, emailAddress);
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
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
   *          The new status of the group.
   * @param emitter
   *          The corresponding GUI element of this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGroup group) throws Exception
  {
    if (state != IGuiElement.SELECTED)
      return;

    
    IDataTableRecord mailRecord = context.getSelectedRecord();
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      mailRecord.setIntValue(trans, Email.seen, 1);
      trans.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      trans.close();
    }
    // add email adress to adressbook if wanted
   
    checkEmailAdressInAddressBook(context, mailRecord);
  }

 
}
