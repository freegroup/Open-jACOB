package jacob.event.ui.activity;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 06 14:44:20 CEST 2005
 *
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the ActivityCopyToOutLook-Button. <br>
  * The onAction will be called, if the user clicks on this button. <br>
  * Insert your custom code in the onAction-method. <br>
  * 
  * @author andreas
  *  
  */
public class ActivityCopyToOutLook extends IButtonEventHandler 
{
	/**
   * The user has been click on the corresponding button. <br>
   * Be in mind: The currentRecord can be null if the button has not the
   * [selected] flag. <br>
   * The selected flag warranted that the event can only be fired if the <br>
   * selectedRecord!=null. <br>
   * 
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IDataTableRecord activity = context.getSelectedRecord();
        
        StringBuffer vCal = new StringBuffer();
        vCal.append("BEGIN:VCALENDAR\n");
        vCal.append("BEGIN:VEVENT\n");
//      Start date in vcalendarformat
        vCal.append("DTSTART:");
        DateFormat format;
        format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        vCal.append(format.format(activity.getDateValue("plan_start")));
        vCal.append("\n");
//      End date in vcalendarformat
        vCal.append("DTEND:");
        vCal.append(format.format(activity.getDateValue("plan_done")));
        vCal.append("\n");
        vCal.append("DESCRIPTION:");
//      CR/LF in Longtext to Valendar format (0=D0=A)
//      vCal.append(StringUtils.replace(activity.getSaveStringValue("notes"),"\r\n","=0D=0A"));
        vCal.append(StringUtils.replace(activity.getSaveStringValue("notes"),"\r\n","\\n"));
        vCal.append("\n");
//      Summary
        vCal.append("SUMMARY:");
        vCal.append(activity.getSaveStringValue("description"));
        vCal.append("\n");
        vCal.append("END:VEVENT\n");
        vCal.append("END:VCALENDAR");
        context.createDocumentDialog("text/x-vCalendar","app.vcs",vCal.toString().getBytes()).show();
  }
   
	/*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement.GroupState,
   *      de.tif.jacob.screen.IGuiElement)
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}

