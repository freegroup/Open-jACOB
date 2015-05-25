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

import java.util.Map;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UnavailableDatasourceException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the DatasourceTest generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class DatasourceTest extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: DatasourceTest.java,v 1.3 2009/07/30 17:32:03 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Callback for displaying connection test info.
   * 
   * @author Andreas Sonntag
   */
  private static class ConnectionTestInfoCallback implements IOkCancelDialogCallback
  {
    private final String info;

    private ConnectionTestInfoCallback(String info)
    {
      this.info = info;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext context) throws Exception
    {
      // nothing to do
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
     */
    public void onOk(IClientContext context) throws Exception
    {
      // Create a FormDialog containing the problem of the test.
      //
      FormLayout layout = new FormLayout("10dlu, grow, 10dlu", // columns
          "10dlu, p, 10dlu, grow, 20dlu"); // rows
      CellConstraints cc = new CellConstraints();
      IFormDialog dialog = context.createFormDialog("Show connection test information", layout, new DummyCallback());
      dialog.addLabel("Connection test information:", cc.xy(1, 1));
      dialog.addTextArea("info", this.info, cc.xy(1, 3));

      // Show the dialog with a prefered size. The dialog tries to resize to the optimum size!
      dialog.show(500, 200);
    }
  }
  
  /**
   * Dummy callback
   * 
   * @author Andreas Sonntag
   */
  private static class DummyCallback implements IFormDialogCallback
  {
    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      // do nothing
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord record = context.getSelectedRecord();

    try
    {
      // fetch data source object
      DataSource dataSource = DataSource.get(record.getStringValue("name"));

      // and execute the test
      String info = dataSource.test();

      // if no exception has been throw, we have been successful
      context.createOkCancelDialog("Connection test has been successful!\r\nShow additional connection test information?", new ConnectionTestInfoCallback(info)).show();
    }
    catch (UnavailableDatasourceException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      // test failed
      //
      ExceptionHandler.handleNoReport(context, ex);
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED);
  }
}
