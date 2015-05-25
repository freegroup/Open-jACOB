/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import de.tif.jacob.ruleengine.castor.*;

public abstract class DecisionExitValueModel extends ObjectModel implements AbstractSourceModel
{
	private final DecisionModel parent;
	
	public DecisionExitValueModel(DecisionModel parent)
	{
		this.parent = parent;
	}
  
	public DecisionModel getParent() 
	{
		return parent;
	}
	
	public AbstractTransitionModel getTransistionModel()
	{
		return getParent().getRulesetModel().getSourceTransition(this);
	}
	
	public abstract String getValue();
 }
