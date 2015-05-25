/*
 * Created on 11.01.2006 by mike
 * 
 *
 */
package jacob.event.ui.opencalls;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class OpencallsBrowser extends IBrowserEventHandler
{

  public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
  {
    
    return value;
  }

  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    context.createMessageDialog("Test").show();
  }

}
