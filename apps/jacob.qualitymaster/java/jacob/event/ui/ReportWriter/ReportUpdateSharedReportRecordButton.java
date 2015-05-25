package jacob.event.ui.ReportWriter;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * The event handler for the ReportUpdateSharedReportRecordButton update button.<br>
 * 
 * @author andherz
 */
public class ReportUpdateSharedReportRecordButton extends AbstractUpdateButton 
{
	static public final transient String RCS_ID = "$Id: ReportUpdateSharedReportRecordButton.java,v 1.1 2009-12-24 10:02:21 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    button.setEnable(false);
    button.setVisible(false);
    Report report = ReportProvider.get(context);
    if(report!=null)
    {
      button.setVisible(!context.getUser().getLoginId().equals(report.getOwnerId()));
      button.setEnable(!context.getUser().getLoginId().equals(report.getOwnerId()));
    }
	}
}
