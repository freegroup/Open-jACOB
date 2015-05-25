/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.ruleengine.castor.Rule;

public abstract class RuleModel  extends ObjectModel
{
  private final Rule castor;
  private RulesetModel rulesetModel;
  
  public RuleModel(RulesetModel parent, Rule castorRule)
  {
    this.rulesetModel = parent;
    this.castor = castorRule;
  }
  
  public abstract String getImplementationClass();
  
  public RuleModel(Rule castorRule)
  {
    this.castor = castorRule;
  }
  
  public String getId()
  {
    return getCastor().getRuleId();
  }

 
  public int getPosX()
  {
    return castor.getPosX();
  }
  
  public int getPosY()
  {
    return castor.getPosY();
  }

  
  public Point getPosition()
  {
    return new Point(getPosX(), getPosY());
  }
  
  public void setPosition(Point p)
  {
    Point save = getPosition();
    castor.setPosX(p.x);
    castor.setPosY(p.y);
    firePropertyChange(PROPERTY_POSITION_CHANGED, save,p);
  }
  
  public Rule getCastor()
  {
    return castor;
  }

  public RulesetModel getRulesetModel()
  {
    return rulesetModel;
  }

  public void setRulesetModel(RulesetModel rulesetModel)
  {
    this.rulesetModel = rulesetModel;
  }
  
 
}
