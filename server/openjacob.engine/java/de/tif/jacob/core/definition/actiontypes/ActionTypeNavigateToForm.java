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
import de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.impl.IActionTypeHandler;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ActionTypeNavigateToForm extends ActionType
{
  private final String formToNavigate;
  
	/**
	 * 
	 */
	public ActionTypeNavigateToForm(String formToNavigate)
	{
    if (null == formToNavigate)
      throw new NullPointerException("A form name is missing");
    this.formToNavigate = formToNavigate; 
	}

	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ActionType#execute(de.tif.jacob.screen.IActionTypeHandler, de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public void execute(IActionTypeHandler actionHandler, IClientContext context, IActionEmitter emitter) throws Exception
	{
    actionHandler.execute(this,context,emitter);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ActionType#onGroupStatusChanged(de.tif.jacob.screen.IActionTypeHandler, de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement, int)
	 */
	public void onOuterGroupStatusChanged(IActionTypeHandler actionHandler, IClientContext context, IGuiElement target, GroupState status) throws Exception
	{
    actionHandler.onOuterGroupStatusChanged(this,context,target,status);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ActionType#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorAction)
	 */
	protected void toJacob(CastorAction jacobAction)
	{
    NavigateToForm jacobElement = new NavigateToForm();
    jacobElement.setFormName(this.formToNavigate);
    jacobAction.setNavigateToForm(jacobElement);
  }

	/**
	 * @return Returns the formToNavigate.
	 */
	public String getFormToNavigate()
	{
		return formToNavigate;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.ActionType#isExecutable(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean isExecutable(IClientContext context, IActionEmitter emitter)
	{
		try
		{
			// Note: Since one and the same form might be added to different domains and form navigation should
			// be within the domain itself, we must calculate whether navigation is possible at runtime.
			IGuiElement element = context.getDomain().findByName(this.formToNavigate); 
			return element instanceof IForm;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

}
