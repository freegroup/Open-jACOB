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

package jacob.event.ui.clusterconfig;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the ClusterconfigResetToFactory generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ClusterconfigResetToFactory extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ClusterconfigResetToFactory.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDialog dialog = context.createOkCancelDialog("Do you really want to reset the cluster configuration to factory?", new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {
        IDataTableRecord factoryConfig = ClusterManager.getProvider().resetConfigurationToFactory();
        
        if (factoryConfig == null)
          throw new UserException("Reset of cluster configuration not supported");

        // and backfill configuration record
        IDataAccessor accessor = context.getDataAccessor();
        accessor.qbeClearAll();
        IDataTable clusterConfigTable = context.getDataTable();
        clusterConfigTable.qbeSetPrimaryKeyValue(factoryConfig.getPrimaryKeyValue());
        IDataBrowser clusterConfigBrowser = context.getGroup().getBrowser().getData();
        clusterConfigBrowser.search(accessor.getApplication().getDefaultRelationSet(), Filldirection.BOTH);
        clusterConfigBrowser.setSelectedRecordIndex(0);
        clusterConfigBrowser.propagateSelections();
      }

      public void onCancel(IClientContext context) throws Exception
      {
        // nothing to do here
      }
    });
    dialog.show();
  }
}
