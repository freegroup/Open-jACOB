/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 15:47:58 CEST 2010
 */
package jacob.event.ui.document;

import jacob.common.BoUtil;
import jacob.model.Document;
import jacob.model.Parent_bo;
import jacob.relationset.BoRelationset;

import org.apache.commons.io.FilenameUtils;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 * The event handler for the DocumentRenameDocumentButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class GenericRenameDocumentButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: GenericRenameDocumentButton.java,v 1.1 2010-07-16 14:26:15 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	class RenameDocumentCallback implements IAskDialogCallback
	{
	  private IDataTableRecord recordToRename;
	  private String originalName;
	  
	  public RenameDocumentCallback(IDataTableRecord recordToRename) throws Exception
	  {
	    this.recordToRename = recordToRename;
	    this.originalName = recordToRename.getSaveStringValue(Document.name);
	  }
	  
    public void onOk(IClientContext context, String value) throws Exception
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        IDataTableRecord boRecord = BoUtil.findByPkey(context, recordToRename.getStringValue(Document.pkey));
        IDataTableRecord parentBoRecord = boRecord.getLinkedRecord(Parent_bo.NAME);
        
        byte [] data = recordToRename.getDocumentValue(Document.content).getContent();
        String name = recordToRename.getStringValue(Document.name);
        String ext_orig = FilenameUtils.getExtension(name);
        String ext_new = FilenameUtils.getExtension(value);
        // der original prefix einer Datei kann nicht geändert werden
        //
        if(StringUtil.saveEquals(ext_orig, ext_new)==false)
          value = value + "."+ext_orig;
        
        // nothing to do
        if(originalName.equals(value))
          return;
        
        if(BoUtil.exist(context, parentBoRecord, value))
        {
          IAskDialog dialog = context.createAskDialog("Datei '"+value+"' bereits vorhanden.\n\nNeuer Name",recordToRename.getSaveStringValue(Document.name),new RenameDocumentCallback(recordToRename));
          dialog.show();
        }
        else
        {
          recordToRename.setValue(trans, Document.name, value);
          recordToRename.setValue(trans, Document.content, DataDocumentValue.create(value, data));
          trans.commit();
          context.getDataAccessor().propagateRecord(boRecord, BoRelationset.NAME, Filldirection.BOTH);
        }
      }
      finally
      {
        trans.close();
      }
    }
    
    public void onCancel(IClientContext context) throws Exception
    {
    }
	}
	
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
    IDataTableRecord currentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
    IAskDialog dialog = context.createAskDialog("Name",currentRecord.getSaveStringValue(Document.name),new RenameDocumentCallback(currentRecord));
    dialog.show();
	}
 }
