/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 20 13:04:05 CET 2010
 */
package jacob.event.ui.datasource;

import jacob.model.Datasource;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the DatasourceNew new button.<br>
 * 
 * @author andreas
 */
public final class DatasourceNew extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DatasourceNew.java,v 1.1 2010/01/20 12:23:08 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    return true;
  }

  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    // if we do not perform a copy operation, than adjustment should default to
    // jACOB!
    IDataTableRecord newRecord = context.getSelectedRecord();
    if (newRecord.hasNullValue(Datasource.adjustment))
      newRecord.setValue(newRecord.getCurrentTransaction(), Datasource.adjustment, Datasource.adjustment_ENUM._jACOB);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
