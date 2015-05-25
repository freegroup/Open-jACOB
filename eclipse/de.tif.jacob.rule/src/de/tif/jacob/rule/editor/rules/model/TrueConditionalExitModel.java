/*
 * Created on 11.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import de.tif.jacob.ruleengine.castor.ConditionalExit;

public class TrueConditionalExitModel extends ConditionalExitModel
{

  public TrueConditionalExitModel(DecisionModel parent)
  {
    super(parent);
    getCastor().setValue(Boolean.TRUE.toString());
  }

  /**
   * @param parent
   * @param onFalse
   */
  public TrueConditionalExitModel(DecisionModel parent, ConditionalExit onFalse)
  {
    super(parent, onFalse);
    getCastor().setValue(Boolean.TRUE.toString());
  }
}
