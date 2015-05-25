/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 17:24:54 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.BoUtil;
import jacob.common.DocumentUtil;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Parent_bo;
import jacob.relationset.BoRelationset;

import org.apache.commons.logging.Log;


/**
 * The event handler for the DocumentUploadNewDocumentVersionButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentUploadNewDocumentVersionButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DocumentUploadNewDocumentVersionButton.java,v 1.2 2010-07-16 14:26:13 herz Exp $";
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
    IUploadDialog dialog = context.createUploadDialog("Dokument hochladen", "Dokument auswählen welches hochgeladen werden soll", new IUploadDialogCallback()
    {
      public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
      {
        IDataTableRecord boRecord = context.getSelectedRecord();
        IDataTableRecord documentRecord = boRecord.getLinkedRecord(Document.NAME);
        
        if(documentRecord!=null)
        {
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            documentRecord.setDocumentValue(trans, Document.content, DataDocumentValue.create(fileName, fileData));
            documentRecord.setValue(trans, Document.name, fileName);
            trans.commit();
            context.getDataAccessor().propagateRecord(context.getSelectedRecord(), BoRelationset.NAME, Filldirection.BOTH);
          }
          finally
          {
            trans.close();
          }
        }
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
        // TODO Auto-generated method stub
      }
    });
    dialog.show();
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
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}

