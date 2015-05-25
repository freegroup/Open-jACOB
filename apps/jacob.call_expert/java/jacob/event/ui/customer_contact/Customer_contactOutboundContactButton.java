/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Feb 18 13:04:09 CET 2009
 */
package jacob.event.ui.customer_contact;

import jacob.common.media.IMediaChannelHandler;
import jacob.model.Customer_contact;
import jacob.model.Customer_contact_type;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Customer_contactOutboundContactButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class Customer_contactOutboundContactButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Customer_contactOutboundContactButton.java,v 1.13 2009/10/08 10:32:01 A.Boeken Exp $";
	static public final transient String RCS_REV = "$Revision: 1.13 $";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord contactRecord = context.getDataTable(Customer_contact.NAME).getSelectedRecord();
    if (contactRecord != null)
    {
      String mediaHandler = contactRecord.getLinkedRecord(Customer_contact_type.NAME).getStringValue(Customer_contact_type.java_outbound_handler_class);
      if (mediaHandler != null)
      {
        Object obj = Class.forName(mediaHandler).newInstance();
        if (obj instanceof IMediaChannelHandler)
        {
          IMediaChannelHandler handler = (IMediaChannelHandler) obj;
          handler.createNew(context, contactRecord);
        }
      }
    }

  }
}
