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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the JapplversionChangeUpdate update button.<br>
 * 
 * @author andreas
 */
public class JapplversionChangeUpdate extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: JapplversionChangeUpdate.java,v 1.1 2007/01/19 07:44:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    return true;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord contextRecord = context.getSelectedRecord();

    // a record exists and we have just requested to store modifications, i.e. we are back
    // in the selected mode.
    if (contextRecord != null && context.getGroup().getDataStatus() == IGuiElement.SELECTED)
    {
      // immediatly process required deployment operations instead of waiting for DeployWatch task
      // to do this assynchronously.
      DeployManager.checkInstalledApplication(contextRecord);
    }
  }
}
