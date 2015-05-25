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

package jacob.event.ui.jan_queue;

import jacob.common.ui.RemoveBrowserButtonEventHandler;
import de.tif.jacob.screen.IClientContext;


/**
 * The event handler for the Jan_queueRemoveBrowser generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Jan_queueRemoveBrowser extends RemoveBrowserButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: Jan_queueRemoveBrowser.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  protected String getRecordEntityName(IClientContext context)
  {
    return "jAN messages";
  }
}
