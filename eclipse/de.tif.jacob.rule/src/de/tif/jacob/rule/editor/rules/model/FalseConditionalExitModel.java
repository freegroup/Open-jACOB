/*
 * Created on 11.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import de.tif.jacob.ruleengine.castor.ConditionalExit;

public class FalseConditionalExitModel extends ConditionalExitModel
{

  public FalseConditionalExitModel(DecisionModel parent)
  {
    super(parent);
    getCastor().setValue(Boolean.FALSE.toString());
  }

  /**
   * @param parent
   * @param onFalse
   */
  public FalseConditionalExitModel(DecisionModel parent, ConditionalExit onFalse)
  {
    super(parent, onFalse);
    getCastor().setValue(Boolean.FALSE.toString());
  }
}
