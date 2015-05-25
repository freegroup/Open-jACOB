/*
 * Created on 27.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;


public class TransitionModel extends AbstractTransitionModel
{
  private final AbstractSourceModel fromModel;
  private final AbstractTargetModel toModel;
  private RulesetModel rulesetModel;
  

  public TransitionModel(RulesetModel rulesetModel, AbstractSourceModel from, AbstractTargetModel to)
  {
    this.fromModel = from;
    this.toModel   = to;
    this.rulesetModel = rulesetModel;
  }

  public AbstractSourceModel getFromRuleModel()
  {
    return fromModel;
  }

  public AbstractTargetModel getToRuleModel()
  {
    return toModel;
  }
 
  public RulesetModel getRulesetModel()
  {
    return rulesetModel;
  }
  
  public void getRulesetModel(RulesetModel model)
  {
    this.rulesetModel=model;
  }
}
