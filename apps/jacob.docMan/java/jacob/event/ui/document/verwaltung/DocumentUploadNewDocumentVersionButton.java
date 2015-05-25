/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 17:24:54 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import jacob.browser.DocversionsIntNumberBrowser;
import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Docversions;
import jacob.model.Editor;
import jacob.relationset.BoRelationset;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the DocumentUploadNewDocumentVersionButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentUploadNewDocumentVersionButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DocumentUploadNewDocumentVersionButton.java,v 1.2 2010-09-17 08:57:17 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IUploadDialog dialog = context.createUploadDialog("Dokument hochladen", "Dokument auswählen, das hochgeladen werden soll", new IUploadDialogCallback()
    {
      public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
      {
        IDataTableRecord boRecord = context.getSelectedRecord();
        IDataTableRecord documentRecord = boRecord.getLinkedRecord(Document.NAME);

        if (documentRecord != null)
        {
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            //Version erstellen
            IDataTable docversion = context.getDataTable(Docversions.NAME);
            docversion.qbeClear();
            docversion.qbeSetKeyValue(Docversions.document_key, documentRecord.getValue(Document.pkey));
            
            IDataBrowser dvBrowser = context.getDataBrowser(DocversionsIntNumberBrowser.NAME);
            dvBrowser.search(IRelationSet.LOCAL_NAME);
            Integer versionNo =0;
            if (dvBrowser.recordCount()==0)
            {
              versionNo = 1;
            }
            else 
            {
              versionNo=((Integer) dvBrowser.getRecord(0).getTableRecord().getValue(Docversions.internalversion))+1;
              
            }
            System.out.println("versionNo " + versionNo);
            IDataTableRecord docversionrec = docversion.newRecord(trans);
            docversionrec.setValue(trans, Docversions.internalversion, versionNo);
            //Hack
            docversionrec.setValue(trans, Docversions.manualversion, versionNo);
            docversionrec.setValue(trans, Docversions.content, documentRecord.getValue(Document.content));
            docversionrec.setValue(trans, Docversions.create_date, documentRecord.getValue(Document.create_date));
            docversionrec.setLinkedRecord(trans, documentRecord);
            documentRecord.setValue(trans, Document.locked, 0);
            documentRecord.setValue(trans, Document.lockedsince, null);
            documentRecord.resetLinkedRecord(trans, Editor.NAME);
            //Ende Version
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
   * This is a good place to enable/disable the button on relation to the group state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    IDataTableRecord docrec = context.getDataTable(Document.NAME).getSelectedRecord();
    if (null != docrec)
    {

      if (docrec.getintValue(Document.locked) == 1)
      {
        System.out.println("Key" + docrec.getLinkedRecord(Editor.NAME).getSaveStringValue(Editor.pkey));
        if (context.getUser().getKey().equals(docrec.getLinkedRecord(Editor.NAME).getSaveStringValue(Editor.pkey)))
        {
          button.setEnable(true);
        }
        else
        {
          button.setEnable(false);
        }

      }
      else
      {
        button.setEnable(true);
      }
    }
  }
}
