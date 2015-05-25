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
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.impl.jad.castor.Search;
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
public class ActionTypeSearchUpdateRecord extends ActionType
{
  static public final transient String RCS_ID = "$Id: ActionTypeSearchUpdateRecord.java,v 1.3 2009/07/17 06:40:24 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final IRelationSet relationSet;
  private final Filldirection fillDirection;
  private final boolean safeMode;
  
  private final ActionTypeSearch searchAction;
  private final ActionTypeChangeUpdateRecord updateAction;

  public ActionTypeSearchUpdateRecord(IRelationSet relationSet, Filldirection fillDirection, boolean safeMode)
  {
    this.relationSet = relationSet;
    this.fillDirection = fillDirection;
    this.safeMode = safeMode;
    this.updateAction = new ActionTypeChangeUpdateRecord(ActionType.SCOPE_OUTERGROUP);
    this.searchAction = new ActionTypeSearch(relationSet,fillDirection,safeMode);
  }

	/**
	 * @return Returns the fillDirection.
	 */
	public final Filldirection getFillDirection()
	{
		return fillDirection;
	}

	/**
	 * @return Returns the relationSet.
	 */
	public final IRelationSet getRelationSet()
	{
		return relationSet;
	}

  public void execute(IActionTypeHandler actionHandler, IClientContext context, IActionEmitter emitter) throws Exception
  {
    actionHandler.execute(this,context,emitter);
  }

  public void onOuterGroupStatusChanged(IActionTypeHandler actionHandler, IClientContext context, IGuiElement target, GroupState status) throws Exception
  {
    actionHandler.onOuterGroupStatusChanged(this, context,target,status);
  }
  
	/* (non-Javado
	 * @see de.tif.jacob.core.definition.ActionType#toJacob(de.tif.jacob.core.jad.castor.Action)
	 */
	protected void toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorAction jacobAction)
	{
    Search jacobElement = new Search();
    jacobElement.setFilldirection(this.fillDirection.toJad());
    jacobElement.setRelationset(this.relationSet.getName());
    jacobElement.setSafeMode(isSafeMode());
    jacobAction.setSearch(jacobElement);
  }

	/**
	 * @return Returns the safeMode.
	 */
	public final boolean isSafeMode()
	{
		return safeMode;
	}

  public ActionTypeSearch getSearchAction()
  {
    return searchAction;
  }

  public ActionTypeChangeUpdateRecord getUpdateAction()
  {
    return updateAction;
  }

}
