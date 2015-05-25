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

package jacob.event.ui.datasource;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DatasourceSetup extends IButtonEventHandler
{
  /**
   * @author andreas
   *
   */
  private class ReconfigureCallback implements IOkCancelDialogCallback
	{
		public void onCancel(IClientContext context)
		{
		}
		
		public void onOk(IClientContext context) throws Exception
		{
		  DatasourceReconfigure.reconfigure(context, false);
		}
  }

  /**
   * @author Andreas
   *
   * To change the template for this generated type comment go to
   * Window - Preferences - Java - Code Generation - Code and Comments
   */
  private class SetupCallback implements IOkCancelDialogCallback
	{
		public void onCancel(IClientContext context)
		{
		}
		
		public void onOk(IClientContext context) throws Exception
		{
	    IDataTableRecord record = context.getSelectedRecord();
	    
	    DataSource dataSource = DataSource.get(record.getStringValue("name"));
	    if (!(dataSource instanceof SQLDataSource))
	    {
	      context.createMessageDialog("Only SQL data sources could be set up").show();
	      return;
	    }
	    
	    SQLDataSource sqlDataSource = (SQLDataSource) dataSource;
		  
		  // get reconfigure implementation
		  Reconfigure reconfigure = sqlDataSource.getReconfigureImpl();
		  
		  // fetch current schema
		  ISchemaDefinition currentSchema = reconfigure.fetchSchemaInformation();
		  
			// setup datasource
		  sqlDataSource.setup(currentSchema);
			
	    context.createOkCancelDialog("The data source has been successfully set up.\r\n"+
	        "To make the data source available for an application a reconfigure operation has to be executed.\r\n"+
	        "Do you want to proceed?",new ReconfigureCallback()).show();
		}
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
		IOkCancelDialog dialog = context.createOkCancelDialog("Do you really want to set up this data source?",new SetupCallback());
	  dialog.show();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
  {
    // enable button for jACOB data sources only
    IDataTableRecord datasourceRecord = context.getDataTable().getSelectedRecord();
    boolean enable = status == IGuiElement.SELECTED && null != datasourceRecord && "jACOB".equals(datasourceRecord.getValue("adjustment"));
    button.setEnable(enable);
  }
}
