/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.ruleengine.castor.ConditionalExit;
import de.tif.jacob.ruleengine.castor.Decision;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public abstract class DecisionModel extends RuleModel implements AbstractTargetModel
{
  protected List exits = new ArrayList();
  private CommentModel commentModel;
  
  public DecisionModel()
  {
    super(new Rule());
    commentModel = new CommentModel(this);    

    Decision  decision =new Decision();
    
    decision.setMethodName("unset");
    decision.setDecisionClass("class name");
    
    getCastor().setDecision(decision);
    getCastor().setPosX(100);
    getCastor().setPosY(100);
    getCastor().setRuleId(new UID().toString());
  }
  
  /**
   * @param castorRule
   */
  public DecisionModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
    commentModel = new CommentModel(this);
    for(int i = 0; i<castorRule.getDecision().getConditionalExitCount();i++)
    {
      exits.add(createExitFromCastor( castorRule.getDecision().getConditionalExit(i)));
    }
  }


  protected ConditionalExitModel createExitFromCastor(ConditionalExit exit)
  {
    return new ConditionalExitModel(this, exit);
  }
  
  public void addElement(ConditionalExitModel exitModel )
  {
    getCastor().getDecision().addConditionalExit(exitModel.getCastor());
    exits.add(exitModel);
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null,exitModel);
  }
  
  public List getChildren() 
  {
    return exits;
  }

  public void cleanChildren()
  {
  	while(exits.size()>0)
  	{
  		ConditionalExitModel exit=(ConditionalExitModel) exits.remove(0);
      if(exit.getTransistionModel()!=null)
        getRulesetModel().removeElement(exit.getTransistionModel());
      
  		getCastor().getDecision().removeConditionalExit(0);
    	firePropertyChange(PROPERTY_ELEMENT_REMOVED,exit, null);
  	}
  }
  
  public Rectangle getBounds()
  {
  	return new Rectangle(getPosition(), new Dimension(50,50));
  }
    
  
  public void setDecisionClass(String className)
  {
  	String save = getImplementationClass();
  	if(StringUtil.saveEquals(save,className))
  		return;
  	
  	getCastor().getDecision().setDecisionClass(className);
  	firePropertyChange(PROPERTY_MODEL_CHANGED,save, className);
  }

  
  /**
   * 
   * @param method methodName signature
   */
  public void setDecisionMethod(String method)
  {
  	String save = getDecisionMethodName();
  	if(StringUtil.saveEquals(save,method))
  		return;

  	int index = method.indexOf("(");
  	String methodName = method.substring(0,index);
  	String signature  = method.substring(index);
  	getCastor().getDecision().setMethodName(methodName);
  	getCastor().getDecision().setSignature(signature);
  	firePropertyChange(PROPERTY_MODEL_CHANGED,save, method);
  }

  public String getDisplayDecisionClassName()
  {
    return ClassUtil.getShortClassName(getCastor().getDecision().getDecisionClass());
  }

  public String getImplementationClass()
  {
  	return getCastor().getDecision().getDecisionClass();
  }
  
  public String getDecisionMethodName()
  {
    return getCastor().getDecision().getMethodName()+getCastor().getDecision().getSignature();
  }
  
  public String getDisplayLabel()
  {
    String label = getCastor().getDecision().getMethodName()+"( ";
    String[] params = getParameters();
    for(int i=0;i<params.length;i++)
    {
      label = label+params[i];
      if((i+1)<params.length)
        label = label +", ";
    }
    label = label +")";

    return label;
  }
  
  public String[] getParameters()
  {
  	return getCastor().getDecision().getParameter();
  }
  
  public void setParameters(String[] parameters)
  {
  	String[] save = getParameters();
 
    if(Arrays.equals(parameters,save))
      return;
   	
  	getCastor().getDecision().setParameter(parameters);
  	firePropertyChange(PROPERTY_PARAMETERS_CHANGED,save, parameters);
  }

  /**
   * 
   * @return List[ConditionalExitModel]
   */
  public List getConditionalExitModels()
  {
    return exits;
  }

	public CommentModel getCommentModel() {
		return commentModel;
	}

	public void setCommentModel(CommentModel commentModel) {
		this.commentModel = commentModel;
	}
}
