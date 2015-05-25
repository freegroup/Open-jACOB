/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.ruleengine.castor.Annotation;
import de.tif.jacob.util.StringUtil;

public class AnnotationModel  extends ObjectModel
{
  private final Annotation castor;
  private RulesetModel rulesetModel;
  
  public AnnotationModel(RulesetModel parent, Annotation castorRule)
  {
    this.rulesetModel = parent;
    this.castor = castorRule;
  }
  
  
  public AnnotationModel()
  {
    this.castor = new Annotation();
    this.castor.setData("note");
  }
  
 
  public int getPosX()
  {
    return castor.getX();
  }
  
  public int getPosY()
  {
    return castor.getY();
  }

  
  public Point getPosition()
  {
    return new Point(getPosX(), getPosY());
  }
  
  public void setBounds(Rectangle rect)
  {
    Rectangle save = getBounds();
    if(save.equals(rect))
      return;
    
    castor.setHeight(rect.height);
    castor.setWidth(rect.width);
    castor.setX(rect.x);
    castor.setY(rect.y);
    firePropertyChange(PROPERTY_POSITION_CHANGED, save,rect);
  }
  
  public Rectangle getBounds()
  {
    return new Rectangle(castor.getX(),castor.getY(), castor.getWidth(), castor.getHeight());
  }
  
  public void setPosition(Point p)
  {
    Point save = getPosition();
    castor.setX(p.x);
    castor.setY(p.y);
    firePropertyChange(PROPERTY_POSITION_CHANGED, save,p);
  }
  
  public void setText(String text)
  {
    String save =getText();
    if(StringUtil.saveEquals(save, text))
      return;
    
    castor.setData(text);
    firePropertyChange(PROPERTY_MODEL_CHANGED, save,text);
  }
  
  public String getText()
  {
    return castor.getData();
  }
  
  public Annotation getCastor()
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
