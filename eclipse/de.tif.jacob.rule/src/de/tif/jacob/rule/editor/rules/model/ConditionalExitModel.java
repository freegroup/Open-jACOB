/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import de.tif.jacob.ruleengine.castor.ConditionalExit;


public class ConditionalExitModel extends DecisionExitValueModel
{
	private final ConditionalExit castor ;
	
  public ConditionalExitModel(DecisionModel parent)
  {
    super(parent);
    this.castor = new ConditionalExit();
  }

  public ConditionalExitModel(DecisionModel parent, String value)
  {
    super(parent);
    this.castor = new ConditionalExit();
    this.castor.setValue(value);
  }
 
  
  public String getValue()
  {
    return getCastor().getValue();
  }
  
  /**
   * @param castorRule
   */
  public ConditionalExitModel(DecisionModel parent, ConditionalExit onFalse)
  {
  	super(parent);
    this.castor = onFalse;
  }
  
  public AbstractTargetModel getSuccessor()
  {
    if(getCastor().getRuleId()!=null)
      return getParent().getRulesetModel().getTargetModel(getCastor().getRuleId());
    return null;
  }
  
	public void setSuccessor(AbstractTargetModel succ) 
	{
		getCastor().setRuleId(succ==null?null:succ.getId());
	}

	public ConditionalExit getCastor() 
	{
		return castor;
	}

  public RulesetModel getRulesetModel()
  {
    return getParent().getRulesetModel();
  }
  
}
