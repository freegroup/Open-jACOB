/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *����Elias Volanakis - initial API and implementation
 *******************************************************************************/
package de.tif.jacob.rule.editor.rules.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.tif.jacob.rule.editor.rules.commands.DeleteBusinessObjectCommand;
import de.tif.jacob.rule.editor.rules.commands.DeleteDecisionCommand;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;
import de.tif.jacob.rule.editor.rules.model.RuleModel;
import de.tif.jacob.rule.editor.rules.model.RulesetModel;

/**
 * This edit policy enables the removal of a Shapes instance from its container.
 */
public class ShapeComponentEditPolicy extends ComponentEditPolicy
{
  public ShapeComponentEditPolicy()
  {
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
   */
  protected Command createDeleteCommand(GroupRequest deleteRequest)
  {
    Object parent = getHost().getParent().getModel();
    Object child = getHost().getModel();
    if (parent instanceof RulesetModel && child instanceof RuleModel)
    {
      if(!((RuleModel)child).getId().equals(ObjectModel.START_NODE_ID))
      {
      	if(child instanceof DecisionModel)
          return new DeleteDecisionCommand((RulesetModel) parent, (DecisionModel)child);
        return new DeleteBusinessObjectCommand((RulesetModel) parent, (RuleModel)child);
      }
    	else
    		return null;
    }
    return super.createDeleteCommand(deleteRequest);
  }
}