/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Sep 16 17:26:37 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.model.Document;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IStackContainerEventHandler;

/**
 * 
 * @author achim
 */
public class DocumentLockHandlingContainer extends IStackContainerEventHandler
{
  static public final transient String RCS_ID = "$Id: DocumentLockHandlingContainer.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /*
   * This event method will be called, if the status of the corresponding group has been changed. Derived event handlers could overwrite this method, e.g. to enable/disable GUI elements in relation to the group state. <br> Possible group state values are defined in {@link IGuiElement}:<br> <ul> <li>{@link IGuiElement#UPDATE}</li> <li>{@link IGuiElement#NEW}</li> <li>{@link IGuiElement#SEARCH}</li> <li>{@link IGuiElement#SELECTED}</li> </ul>
   * 
   * @param context The current client context
   * 
   * @param state The new group state
   * 
   * @param element The corresponding GUI element to this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IStackContainer element) throws Exception
  {
    IDataTableRecord docrec = context.getDataTable(Document.NAME).getSelectedRecord();
    if (null != docrec)
    {

      if (docrec.getintValue(Document.locked) == 1)
      {
        element.setVisible(true);
      }
      else
      {
        element.setVisible(false);
      }
    }
    else
    {
      element.setVisible(false);
    }

  }
}
