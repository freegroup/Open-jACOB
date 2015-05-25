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

import java.awt.Color;

import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andreas
 */
 public class Clusterconfig extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Clusterconfig.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private static final Color GREEN = new Color(2, 202, 26);
  private static final Color RED = new Color(255, 34, 5);

	/* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGroup)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGroup group) throws Exception
  {
    IGuiElement stateLabel = group.findByName("clusterState");

    if (ClusterManager.isEnabled())
    {
      if (ClusterManager.getInitializationError() == null)
      {
        stateLabel.setColor(GREEN);
        stateLabel.setLabel("Clustering is activated");
      }
      else
      {
        stateLabel.setColor(RED);
        stateLabel.setLabel("Activation of clustering failed");
      }
    }
    else
    {
      stateLabel.setColor(Color.ORANGE);
      stateLabel.setLabel("Clustering is not available");
    }
  }
}
