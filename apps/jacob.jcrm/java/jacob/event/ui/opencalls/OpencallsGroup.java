/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jan 10 17:55:16 CET 2006
 */
package jacob.event.ui.opencalls;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * 
 * @author mike
 */
public class OpencallsGroup extends IGroupListenerEventHandler
{
  static public final transient String RCS_ID = "$Id: OpencallsGroup.java,v 1.1 2006/01/12 09:36:24 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

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
   * @param emitter
   *          The corresponding GUI element of this event handler
   * 
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {

    if (status == IGuiElement.SEARCH)
    {
      IDataBrowser browser = context.getDataBrowser();
      browser.search(IRelationSet.LOCAL_NAME);
      if (browser.recordCount() > 0)
      {
        browser.setSelectedRecordIndex(0);
        browser.propagateSelections();
      }
    }
  }
}
