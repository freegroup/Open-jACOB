package jacob.event.ui.{modulename};

import jacob.common.AppLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.axis.utils.ArrayUtil;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;


/**
 * The event handler for the ReportUpdateRecordButton update button.<br>
 * 
 * @author andherz
 */
public class ReportUpdateRecordButton extends AbstractUpdateButton 
{
	static public final transient String RCS_ID = "$Id: ReportUpdateRecordButton.java,v 1.1 2009/12/14 23:18:52 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    button.setEnable(false);
    button.setVisible(false);
    Report report = ReportProvider.get(context);
    if(report!=null)
    {
      button.setVisible(context.getUser().getLoginId().equals(report.getOwnerId()));
      button.setEnable(context.getUser().getLoginId().equals(report.getOwnerId()));
    }
	}
}
