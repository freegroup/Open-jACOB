/*
 * Created on 04.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

public abstract class AbstractTransitionModel extends ObjectModel
{
  public abstract AbstractTargetModel getToRuleModel();
  public abstract AbstractSourceModel getFromRuleModel();
  
  public abstract RulesetModel        getRulesetModel();
  
}
