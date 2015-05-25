package jacob.event.ui.call;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;

/**
 * @author mike
 *
 */
public class Hasattachment extends ICheckBoxEventHandler
{
    /* 
     * pruefen ob der selektierte Call ein Attachment hat
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
    {
        
        ICheckBox checkBox = (ICheckBox)emitter;
        
        if( status == IGuiElement.SELECTED)
        {
            IDataTableRecord call = context.getSelectedRecord();
            if(DataUtils.inDatabase(call,"attachment","callattachment",call.getSaveStringValue("pkey")))
            {
                checkBox.setValue("1");
            }
            else
            {
                checkBox.setValue("0");
            }
            
        }
        else
        {
            checkBox.setValue("0");
        }
    }
  static public  final transient String RCS_ID = "$Id: Hasattachment.java,v 1.2 2005/06/27 12:23:20 mike Exp $";
  static public  final transient String RCS_REV = "$Revision: 1.2 $";
  
  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler method will be called if he user set a mark at a
   * HTML-CheckBox.
   * 
   * @param checkBox The CheckBox itself
   * @param context The current context of the application
   *
   */
  public void onCheck(IClientContext context, ICheckBox checkBox)throws Exception
  {
    // MIKE: OnGroupstatusChanged fehlt
  }
  
  /**
   * This event handler method will be called if the user uncheck an
   * HTML CheckBox.
   * 
   * @param checkBox The CheckBox itself
   * @param context The current context of the application
   * 
   */
  public void onUncheck(IClientContext context, ICheckBox checkBox)throws Exception
  {
  }
}
