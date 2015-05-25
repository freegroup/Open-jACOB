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

package de.tif.jacob.core.definition.actiontypes;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
import de.tif.jacob.core.definition.impl.jad.castor.ClearGroup;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.impl.IActionTypeHandler;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ActionTypeClearGroup extends ActionType
{
  static public final transient String RCS_ID = "$Id: ActionTypeClearGroup.java,v 1.2 2009/04/03 07:26:04 achim_boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void execute(IActionTypeHandler actionHandler, IClientContext context, IActionEmitter emitter) throws Exception
  {
    actionHandler.execute(this,context,emitter);
  }

  public void onOuterGroupStatusChanged(IActionTypeHandler actionHandler, IClientContext context, IGuiElement target, GroupState status) throws Exception
  {
    actionHandler.onOuterGroupStatusChanged(this, context,target,status);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ActionType#toJacob(de.tif.jacob.core.jad.castor.Action)
	 */
	protected void toJacob(CastorAction jacobAction)
	{
		ClearGroup jacobElement = new ClearGroup();
    jacobAction.setClearGroup(jacobElement);
	}

}
