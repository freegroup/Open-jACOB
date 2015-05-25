/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package jacob.event.ui.japplversion;

import jacob.model.Activesession;
import jacob.model.Dapplversion;
import jacob.model.Japplversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidApplicationException;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the JapplversionUpload generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class JapplversionUpload extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: JapplversionUpload.java,v 1.4 2009/03/03 00:19:38 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private static class LogoutCallback implements IFormDialogCallback
  {
    private final IDataTable activesessionTable;

    private LogoutCallback(IDataTable activesessionTable)
    {
      this.activesessionTable = activesessionTable;
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("logout".equals(buttonId))
      {
        IDataTransaction trans = this.activesessionTable.getAccessor().newTransaction();
        try
        {
          for (int i = 0; i < activesessionTable.recordCount(); i++)
          {
            activesessionTable.getRecord(i).delete(trans);
          }
          trans.commit();
        }
        finally
        {
          trans.close();
        }
        
        IMessageDialog dialog = context.createMessageDialog("Application successfully deployed and user sessions logged out.");
        dialog.show();
        return;
      }

      IMessageDialog dialog = context.createMessageDialog(new de.tif.jacob.i18n.CoreMessage("APPLICATION_SUCCESSFUL_DEPLOYED"));
      dialog.show();
    }
  }
  
  private class UploadCallback implements IUploadDialogCallback
  {
    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
      try
      {
        synchronized (DeployManager.MUTEX)
        {
          String path = Bootstrap.getJacappPath();

          // deploy jacapp-File
          File jacobFile = new File(path + fileName);
          OutputStream out = new FileOutputStream(jacobFile);
          try
          {
            out.write(fileData);
          }
          finally
          {
            out.close();
          }
          DeployEntry deployEntry = DeployManager.deploy(jacobFile.getAbsolutePath());

          // and backfill installed application
          IDataAccessor accessor = context.getDataAccessor();
          accessor.qbeClearAll();
          IDataTable japplversionTable = context.getDataTable();
          japplversionTable.qbeSetKeyValue(Japplversion.name, deployEntry.getName());
          japplversionTable.qbeSetKeyValue(Japplversion.version, deployEntry.getVersion().toShortString());
          IDataBrowser japplversionBrowser = context.getGroup().getBrowser().getData();
          japplversionBrowser.search(accessor.getApplication().getDefaultRelationSet(), Filldirection.BOTH);
          japplversionBrowser.setSelectedRecordIndex(0);
          japplversionBrowser.propagateSelections();

          // and backfill deployed application
          accessor.qbeClearAll();
          IDataTable dapplversionTable = accessor.getTable(Dapplversion.NAME);
          dapplversionTable.qbeSetKeyValue(Dapplversion.name, deployEntry.getName());
          dapplversionTable.qbeSetKeyValue(Dapplversion.version, deployEntry.getVersion());
          IDataBrowser dapplversionBrowser = accessor.getBrowser("dapplversionBrowser");
          dapplversionBrowser.search(accessor.getApplication().getDefaultRelationSet(), Filldirection.BOTH);
          dapplversionBrowser.setSelectedRecordIndex(0);
          dapplversionBrowser.propagateSelections();
          
          // IBIS: Funktioniert leider noch nicht im Cluster-Mode!!!
          //
          if (deployEntry.getStatus().isActive())
          {
            IDataTable activesessionTable = accessor.newAccessor().getTable(Activesession.NAME);
            activesessionTable.qbeSetKeyValue(Activesession.applicationname, deployEntry.getName());
            activesessionTable.qbeSetKeyValue(Activesession.applicationversion, deployEntry.getVersion());
            if (activesessionTable.search() > 0)
            {
              // Retrieve all appropriate user sessions.
              //
              String[] entries = new String[activesessionTable.recordCount()];
              for (int i = 0; i < activesessionTable.recordCount(); i++)
              {
                IDataTableRecord record = activesessionTable.getRecord(i);

                entries[i] = record.getStringValue(Activesession.userid) + " since " + record.getStringValue(Activesession.activesince);
              }

              // Create a FormDialog with a list of active sessions.
              //
              FormLayout layout = new FormLayout("10dlu,grow,10dlu", // columns
                  "10dlu,p,10dlu,grow,10dlu"); // rows
              CellConstraints cc = new CellConstraints();
              IFormDialog dialog = context.createFormDialog("Application deployed", layout, new LogoutCallback(activesessionTable));
              dialog.addLabel("Active user sessions found:", cc.xy(1, 1));
              dialog.addListBox("sessions", entries, -1, cc.xy(1, 3));
              dialog.addSubmitButton("logout", "Log out all");
              dialog.addSubmitButton("ignore", "Ignore");
              // wird der Default-Cancelbutton gedrückt, so nicht onSubmit() gerufen
              dialog.setCancelButton(null);

              dialog.show(250, 300);
              return;
            }
          }

          IMessageDialog dialog = context.createMessageDialog(new de.tif.jacob.i18n.CoreMessage("APPLICATION_SUCCESSFUL_DEPLOYED"));
          dialog.show();
        }
      }
      catch (InvalidApplicationException e)
      {
        alert("Error: " + e.getMessage());
      }
      catch (Exception e)
      {
        ExceptionHandler.handle(context, e);
      }
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IUploadDialog dialog = context.createUploadDialog(new UploadCallback());
    dialog.show();
  }
}
