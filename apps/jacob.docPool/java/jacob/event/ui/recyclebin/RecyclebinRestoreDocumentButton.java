/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 06 08:52:50 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.common.BoUtil;
import jacob.common.DocumentUtil;
import jacob.common.FolderUtil;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RecyclebinRestoreDocumentButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class RecyclebinRestoreDocumentButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RecyclebinRestoreDocumentButton.java,v 1.3 2010-09-21 08:43:44 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
	  IDataTransaction trans = context.getDataAccessor().newTransaction();
	  
		IDataTableRecord currentRecord = context.getSelectedRecord();
		String orig_path = currentRecord.getSaveStringValue(Recyclebin.original_path);
    String orig_pkey = currentRecord.getSaveStringValue(Recyclebin.original_pkey);
    String synonyme = currentRecord.getStringValue(Recyclebin.synonyme);
    String name = currentRecord.getSaveStringValue(Recyclebin.name);
		byte[] data = currentRecord.getDocumentValue(Recyclebin.content).getContent();
		
		String path = orig_path.substring(0,orig_path.length()-name.length()-1);
		
		try
		{
		  IDataTableRecord folder = FolderUtil.createFolder(context,trans,  null, path);
		  IDataTableRecord document = DocumentUtil.restoreDocument(context, trans, folder.getStringValue(Folder.pkey),orig_pkey, synonyme, data, name);

		  // Index des Records im Browser merken
      int index = context.getDataBrowser().getSelectedRecordIndex();
      
      currentRecord.delete(trans);
      
		  trans.commit();
		  
      context.getDataBrowser().removeRecord(context.getGUIBrowser().getSelectedBrowserRecord(context));

      // Neuen Record in dem Papierkorb selektieren
      if(context.getDataBrowser().recordCount()!=0)
      {
        if(index>0)
          context.getGUIBrowser().setSelectedRecordIndex(context, index-1);
        else
          context.getGUIBrowser().setSelectedRecordIndex(context, 0);
      }
      
      IDataTableRecord bo = BoUtil.findByPkey(context, document.getSaveStringValue(Document.pkey));
  		
  		// Das Document/Bo in den zwei beteiligten Formen/Browser einfügen (synchronisieren der Sicht)
  		IForm form = (IForm)context.getApplication().findByName("verwaltung_overview");
  		if(form!=null)
  		{
  		  IBrowser browser = form.getCurrentBrowser();
  		  browser.add(context, bo);
        browser.ensureVisible(context, bo);
  		}

      form = (IForm)context.getApplication().findByName("documents_overview");
      if(form!=null)
      {
        IBrowser browser = form.getCurrentBrowser();
        browser.add(context, bo);
        browser.ensureVisible(context, bo);
      }
      
  		context.showTransparentMessage("Dokument wurde erfolgreich wieder hergestellt.");
		}
		finally
		{
		  trans.close();
		}
	}
   
}

