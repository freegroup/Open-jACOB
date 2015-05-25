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

package de.tif.jacob.core.definition;

import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
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
public abstract class ActionType
{
  static public final transient String RCS_ID = "$Id: ActionType.java,v 1.3 2009/07/17 06:40:25 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  public static class ExecuteScope
  {
    private ExecuteScope(){};
  }
  public final static ExecuteScope SCOPE_OUTERGROUP = new ExecuteScope();
  public final static ExecuteScope SCOPE_TABPANE = new ExecuteScope();
  
  public CastorAction toJacob()
  {
    CastorAction jacobAction = new CastorAction();
    toJacob(jacobAction);
    return jacobAction;
  }
  
  protected abstract void toJacob(CastorAction jacobAction);

  /**
   * Determines whether this action is executable within the given context.
   * 
   * @param context the context 
   * @param emitter the action emitter
   * @return <code>true</code> the action is executable, otherwise <code>false</code>
   */
  public boolean isExecutable(IClientContext context, IActionEmitter emitter)
  {
  	// default implementation: action is executable
  	return true;
  }
  
  public ExecuteScope getExecuteScope()
  {
    return SCOPE_OUTERGROUP;
  }
  
  public void onGroupStatusChanged(IActionTypeHandler actionHandler, IClientContext context, IGuiElement target, GroupState status) throws Exception
  {
    // do nothing per default
  }
  
  public abstract void execute(IActionTypeHandler actionHandler, IClientContext context, IActionEmitter emitter) throws Exception;
  public abstract void onOuterGroupStatusChanged(IActionTypeHandler actionHandler, IClientContext context, IGuiElement target, GroupState status) throws Exception;
}
