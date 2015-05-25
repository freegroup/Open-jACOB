/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 16:38:29 CEST 2010
 */
package jacob.event.ui.recyclebin;
import jacob.common.BoUtil;
import jacob.common.DocumentUtil;
import jacob.common.FolderUtil;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Recyclebin;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * The event handler for the RecyclebinRestoreButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class RecyclebinRestoreFolderButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinRestoreFolderButton.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

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
    IDataTableRecord currentRecord = context.getSelectedRecord();
    byte[] content = currentRecord.getDocumentValue(Recyclebin.content).getContent();
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    
    // Index des Records im Browser merken
    int index = context.getDataBrowser().getSelectedRecordIndex();

    try
    {
      ZipInputStream in = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(content)));
      ZipEntry entry;
      IDataTableRecord firstFolder = null;
      
      while ((entry = in.getNextEntry()) != null)
      {
        if(entry.isDirectory())
        {
          FolderUtil.createFolder(context, trans, null, entry.getName());
        }
        else
        {
          String abs_pfad = entry.getName();
          int i = abs_pfad.lastIndexOf("/");
          String pfad = abs_pfad.substring(0,i);
          String name = abs_pfad.substring(i+1,abs_pfad.length());
          byte[] data = IOUtils.toByteArray(in);
          IDataTableRecord folder = FolderUtil.createFolder(context, trans, null, pfad);
          if(firstFolder==null)
            firstFolder=folder;
          DocumentUtil.createDocument(context, trans, folder.getStringValue(Folder.pkey), data, name);
        }
      }
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

      if(firstFolder!=null)
      {
        IDataTableRecord bo = BoUtil.findByPkey(context, firstFolder.getSaveStringValue(Folder.pkey));
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
      }
      context.showTransparentMessage("Ordner wurde erfolgreich wieder hergestellt");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
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
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
  
  public static void main(String[] args)
  {
    String name = "/pfad1/pfad2/pfad3/dateiname.pdf";
    int index = name.lastIndexOf("/");
    System.out.println(name.substring(0,index));
    System.out.println(name.substring(index+1,name.length()));
  }
}
