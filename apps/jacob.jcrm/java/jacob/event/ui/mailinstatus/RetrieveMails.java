package jacob.event.ui.mailinstatus;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Oct 07 13:31:56 CEST 2005
 *
 */
import jacob.scheduler.system.EmailInTask;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


 /**
  * The Event handler for the RetrieveMails-Button.<br>
  * The onAction will be called, if the user clicks on this button.<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andreas
  *
  */
public class RetrieveMails extends IButtonEventHandler 
{
	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IDataTableRecord mailinstatusRecord = context.getSelectedRecord();
		
		EmailInTask.fetchMails(mailinstatusRecord, 10);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}

