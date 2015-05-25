package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Aug 02 10:42:33 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;


import org.apache.commons.logging.Log;



 /**
  * The Event handler for the CallAttachmentAdd-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallAttachmentAdd extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallAttachmentAdd.java,v 1.3 2005/08/02 14:00:09 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  protected class AttachmentAddCallback implements IUploadDialogCallback
  {
    
    IDataTableRecord record;
    String attType;
    public AttachmentAddCallback(IDataTableRecord record, String type)
    {
      this.record =record;
      attType =type;
    }
    
    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext arg0)
    {
      //ignore
    }

    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onOk(de.tif.jacob.screen.IClientContext, java.lang.String, byte[])
     */
    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        IDataTable table =context.getDataTable("attachment");
        
        IDataTableRecord attachment = table.newRecord(trans);

        attachment.setValue(trans, "document",fileData);
        attachment.setValue(trans, "filename",fileName);
        attachment.setValue(trans, "datecreated","now");
        attachment.setValue(trans, attType,record.getValue("pkey"));
        
        trans.commit();
      }
      finally
      {
        trans.close(); 
      }
      context.refreshGroup();
    }
  }
	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
        IDataTableRecord record =context.getDataTable("call").getSelectedRecord();

        IUploadDialog dialog=context.createUploadDialog(new AttachmentAddCallback(record,"call_key"));
        dialog.show();

  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement element)throws Exception
	{
        element.setEnable(state==IGuiElement.SELECTED || state==IGuiElement.UPDATE);
	}
}

