/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.ruleengine.castor.Rule;

public class StartModel extends RuleModel implements AbstractSourceModel
{
  public StartModel(RulesetModel parent, Rule castorRule)
  {
  	super(parent,castorRule);
  }
  
  public String getId()
  {
    return RuleModel.START_NODE_ID;
  }

  public int getPosX()
  {
    return 20;
  }
  
  public int getPosY()
  {
    return 200;
  }
  
  public Point getPosition()
  {
    return new Point(getPosX(), getPosY());
  }
  
  public void setPosition(Point p)
  {
  }
  
  public void setPosX(int posX)
  {
  }
  
  public void setPosY(int posY)
  {
  }
  
  public String getImplementationClass()
  {
    return getCastor().getBusinessObjectMethod().getBusinessClass();
  }

  public AbstractTargetModel getSuccessor()
  {
    if(getCastor().getBusinessObjectMethod().getNextRuleId()!=null)
      return getRulesetModel().getTargetModel(getCastor().getBusinessObjectMethod().getNextRuleId());
    return null;
  }
  
  public void setSuccessor(AbstractTargetModel successor)
  {
    if(successor!=null)
      getCastor().getBusinessObjectMethod().setNextRuleId(successor.getId());
    else
      getCastor().getBusinessObjectMethod().setNextRuleId(null);
  }
}
