/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.ruleengine.castor.ConditionalExit;
import de.tif.jacob.ruleengine.castor.Decision;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.ruleengine.castor.types.DecisionType;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class BooleanDecisionModel extends DecisionModel
{
  public BooleanDecisionModel()
  {
    super();
    
    addElement(new TrueConditionalExitModel(this));
    addElement(new FalseConditionalExitModel(this));
    getCastor().getDecision().setType(DecisionType.BOOLEAN);
  }
  
  /**
   * @param castorRule
   */
  public BooleanDecisionModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
  }

  protected ConditionalExitModel createExitFromCastor(ConditionalExit exit)
  {
    if(exit.getValue().equals(Boolean.TRUE.toString()))
      return new TrueConditionalExitModel(this, exit);
    else
      return new FalseConditionalExitModel(this, exit);
  }
}
