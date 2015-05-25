/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 16:38:29 CEST 2010
 */
package jacob.event.ui.recyclebin;
import jacob.common.BoUtil;
import jacob.common.DocumentUtil;
import jacob.common.FolderUtil;
import jacob.model.Folder;
import jacob.model.Recyclebin;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
import de.tif.jacob.util.StringUtil;
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
  static public final transient String RCS_ID = "$Id: RecyclebinRestoreFolderButton.java,v 1.3 2010-09-21 11:28:20 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

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

      // erst alle properties einlesen. Diese werden für die Restaurierung benötigt.
      //
      Map<String,Properties> file2Properties = new HashMap<String,Properties>();
      while ((entry = in.getNextEntry()) != null)
      {
          String abs_pfad = entry.getName();
          if(!abs_pfad.endsWith(FolderUtil.PROPERTYFILE_SUFFIX))
            continue;
          Properties prop = new Properties();
          prop.load(in);
          abs_pfad = StringUtil.replace(abs_pfad, FolderUtil.PROPERTYFILE_SUFFIX, "");
          file2Properties.put(abs_pfad, prop);
      }
      // Alle Dateien wieder herstellen
      //
      in = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(content)));
      while ((entry = in.getNextEntry()) != null)
      {
        String abs_pfad = entry.getName();
        if(abs_pfad.endsWith("/"))
          abs_pfad = abs_pfad.substring(0,abs_pfad.length()-1);
        Properties props = file2Properties.get(abs_pfad);
        if(entry.isDirectory())
        {
          IDataTableRecord folder =FolderUtil.createFolder(context, trans, null, abs_pfad);
          folder.setValue(trans, Folder.synonyme, props.getProperty(Recyclebin.synonyme));
          // pkey von Folder nicht wieder herstellen. Da müste auch auch das BO angepasst werden...
        }
        else
        {
          int i = abs_pfad.lastIndexOf("/");
          String pfad = abs_pfad.substring(0,i);
          String name = abs_pfad.substring(i+1,abs_pfad.length());
          
          if(name.endsWith(FolderUtil.PROPERTYFILE_SUFFIX))
            continue;
          
          byte[] data = IOUtils.toByteArray(in);
          IDataTableRecord folder = FolderUtil.createFolder(context, trans, null, pfad);
          Properties folderProperties = file2Properties.get(pfad);
          Properties fileProperties = file2Properties.get(abs_pfad);
          if(folderProperties!=null)
          {
            folder.setValue(trans, Folder.synonyme, folderProperties.getProperty(Recyclebin.synonyme));
          }
          if(firstFolder==null)
            firstFolder=folder;
          DocumentUtil.restoreDocument(context, trans, folder.getStringValue(Folder.pkey),fileProperties.getProperty(Recyclebin.original_pkey), fileProperties.getProperty(Recyclebin.synonyme), data, name);
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
