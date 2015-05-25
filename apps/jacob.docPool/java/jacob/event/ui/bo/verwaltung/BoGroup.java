/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jun 28 17:41:41 CEST 2010
 */
package jacob.event.ui.bo.verwaltung;
import jacob.common.AppLogger;
import jacob.model.Bo;
import jacob.relationset.BoRelationset;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;
/**
 * 
 * @author andherz
 */
public class BoGroup extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: BoGroup.java,v 1.2 2010-07-16 14:26:14 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return BoBrowserEventHandler.class;
  }

  /**
   * Will be called, if there is a state change from visible=>hidden.
   * 
   * This happens, if the user switches the Domain or Form which contains this
   * group.
   * 
   * @param context
   *          The current client context
   * @param group
   *          The corresponding group for this event
   */
  public void onHide(IClientContext context, IGroup group) throws Exception
  {
    // insert your code here
  }

  @Override
  public void onShow(IClientContext context, IGroup group) throws Exception
  {
    IDataBrowser browser = group.getBrowser().getData();
    if (browser.recordCount() != 0)
      return;
    context.getDataAccessor().qbeClearAll();
    IDataTable boTable = context.getDataTable(Bo.NAME);
    boTable.qbeSetKeyValue(Bo.parent_bo_key, "null");
    browser.search(BoRelationset.NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
      context.getGUIBrowser().expand(context, browser.getRecord(0));
    }
  }
}
