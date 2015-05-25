/*
 * Created on May 25, 2004
 *
 */
package jacob.common.gui.attachment;


import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 *
 */
public abstract class AttachmentAdd extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: AttachmentAdd.java,v 1.9 2005/03/17 09:38:51 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

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
    public void onOk(IClientContext context, final String fileName, final byte[] fileData) throws Exception
    {
    	// um auf filName und fildata im 2ten Callback zuzugreifen müssen diese final sein! (hat was mit dem Speicher zu tun ?!
      IAskDialog dialog = context.createAskDialog("Geben Sie bitte eine kurze Beschreibung ein",new IAskDialogCallback()
      	   {
        public void onOk(IClientContext context, String description) throws Exception
        {      
          // wenn OK, dann den Datensatz speichern darstellen
          IDataTransaction trans =context.getDataAccessor().newTransaction();
          try
          {
            IDataTable table =context.getDataTable("attachment");
            
            IDataTableRecord attachment = table.newRecord(trans);

            attachment.setValue(trans, "document",fileData);
            attachment.setValue(trans, "filename",fileName);
            attachment.setValue(trans, "docname",fileName);
            attachment.setValue(trans, "docdescr",description);
            attachment.setValue(trans, "datecreated","now");
            attachment.setValue(trans, attType,record.getValue("pkey"));
            
            trans.commit();
          }
          finally
          {
          	trans.close(); 
          }      
         }
        public void onCancel(IClientContext context) throws Exception {}
      });
			dialog.show();


    }
  }
  

  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement element)   throws Exception
  {
    element.setEnable(state==IGuiElement.SELECTED);
  }
}
