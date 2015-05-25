package de.tif.jacob.rule.editor.rules.model;

import de.tif.jacob.ruleengine.castor.Rule;

public final class BusinessObjectModel extends AbstractBusinessObjectModel  implements AbstractTargetModel, AbstractSourceModel
{

	public BusinessObjectModel() 
	{
		super();
	}

	public BusinessObjectModel(RulesetModel parent, Rule castorRule) 
	{
		super(parent, castorRule);
	}
	
}
