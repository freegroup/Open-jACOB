package jacob.event.ui.{modulename};

import jacob.common.AppLogger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;

import de.tif.jacob.report.ReportNotifyee;
import de.tif.jacob.report.impl.Report;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ReportRecordSaveAsButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ReportRecordSaveAsButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ReportRecordSaveAsButton.java,v 1.2 2010/03/01 09:31:24 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    Report report = ReportProvider.get(context);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ReportNotifyee n = report.getReportNotifyee(context.getUser());
    String mimeType = n!=null?n.getMimeType():"text/plain";
    report.render(out,mimeType);
    if("text/formatted".equals(mimeType))
        context.createDocumentDialog("text/plain", report.getName()+".txt",out.toByteArray()).show();
    else if("text/plain".equals(mimeType))
      context.createDocumentDialog(mimeType, report.getName()+".txt",out.toByteArray()).show();
    else if("application/excel".equals(mimeType))
      context.createDocumentDialog(mimeType, report.getName()+".xls",out.toByteArray()).show();
    else
      context.createDocumentDialog(mimeType, report.getName(),out.toByteArray()).show();
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    Report report = ReportProvider.get(context);
		button.setEnable(report!=null);
	}
}

