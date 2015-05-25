/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.ruleengine.bo.Alert;
import de.tif.jacob.ruleengine.bo.Email;
import de.tif.jacob.ruleengine.bo.NOP;
import de.tif.jacob.ruleengine.bo.UserExceptionBO;
import de.tif.jacob.ruleengine.bo.UserInformation;
import de.tif.jacob.ruleengine.castor.Annotation;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.ruleengine.castor.Ruleset;
import de.tif.jacob.ruleengine.castor.types.DecisionType;
import de.tif.jacob.util.StringUtil;

public class RulesetModel extends ObjectModel   implements PropertyChangeListener
{
  private final Ruleset castor;
  
  private final ArrayList comments        = new ArrayList();
  private final ArrayList decisions       = new ArrayList();
  private final ArrayList businessObjects = new ArrayList();
  private final ArrayList transitions     = new ArrayList();
  private final ArrayList annotations     = new ArrayList();
  
  public RulesetModel(InputStream is) throws Exception
  {
	
    castor = (Ruleset)Ruleset.unmarshalRuleset(new InputStreamReader(is,"ISO-8859-1") );

    for(int i=0; i< castor.getAnnotationCount();i++)
    {
      Annotation annotation = castor.getAnnotation(i);
      annotations.add(new AnnotationModel(this, annotation));
    }
    
    //
    for (int i=0;i<castor.getRuleCount();i++)
    {
      Rule rule = castor.getRule(i);
      if(rule.getBusinessObjectMethod()!=null)
      {
      	RuleModel model;
      
      	if(rule.getRuleId().equals(RuleModel.START_NODE_ID))
      	{
      		model=new StartModel(this,rule);
      	}
      	else
      	{
          if(Alert.class.getName().equals(rule.getBusinessObjectMethod().getBusinessClass()))
            model = new MessageAlertModel(this, rule);
          else if(UserInformation.class.getName().equals(rule.getBusinessObjectMethod().getBusinessClass()))
            model = new MessageDialogModel(this, rule);
          else if(Email.class.getName().equals(rule.getBusinessObjectMethod().getBusinessClass()))
            model = new MessageEMailModel(this, rule);
          else if(UserExceptionBO.class.getName().equals(rule.getBusinessObjectMethod().getBusinessClass()))
            model = new ExceptionModel(this, rule);
          else if(NOP.class.getName().equals(rule.getBusinessObjectMethod().getBusinessClass()))
            model = new FunnelModel(this, rule);
          else
            model = new BusinessObjectModel(this, rule);
      	}
      	model.addPropertyChangeListener(this);
        businessObjects.add(model);
      }
      else if(rule.getDecision()!=null)
      {
        DecisionModel model=null;
        switch(rule.getDecision().getType().getType())
        {
	        case DecisionType.BOOLEAN_TYPE:
            model = new BooleanDecisionModel(this,rule);
	        	break;
	        case DecisionType.ENUM_TYPE:
            model = new EnumDecisionModel(this,rule);
	        	break;
	        default:
            throw new Exception("Invalid model");
        }

        CommentModel comment =model.getCommentModel();
        model.setCommentModel(comment);
        model.addPropertyChangeListener(this);
        decisions.add(model);
        comments.add(comment);
      }
    }
    
    
    // alle �berg�nge zwischen den Regeln finden
    //
    Iterator iter = businessObjects.iterator();
    while(iter.hasNext())
    {
    	Object obj = iter.next();
    	if(obj instanceof AbstractSourceModel)
    	{
	      AbstractSourceModel model = (AbstractSourceModel)obj;
	      AbstractTargetModel successor = model.getSuccessor();
	      if(successor!=null)
	      {
	      	TransitionModel trans = new TransitionModel(this, model,successor);
	      	trans.addPropertyChangeListener(this);
	        transitions.add(trans);
	      }
    	}
    }
    
    iter = decisions.iterator();
    while(iter.hasNext())
    {
      DecisionModel model = (DecisionModel)iter.next();
      Iterator exitIter = model.getConditionalExitModels().iterator();
      while(exitIter.hasNext())
      {
        ConditionalExitModel exit = (ConditionalExitModel)exitIter.next();
        AbstractTargetModel successor = exit.getSuccessor();
        if(successor!=null)
        {
          AbstractTransitionModel trans = new TransitionModel(this, exit,successor);
          trans.addPropertyChangeListener(this);
          transitions.add(trans);
        }
      }
    }
  }
  
  
  public boolean addElement(AnnotationModel model)
  {
    annotations.add(model);
    castor.addAnnotation(model.getCastor());
    
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null, model);
    return true;
  }
  
  public boolean addElement(RuleModel model)
  {
  	if(model instanceof AbstractBusinessObjectModel)
  		return addElement((AbstractBusinessObjectModel)model);
  	else if (model instanceof DecisionModel)
  		return addElement((DecisionModel)model);
  	return false;
  }
  
  private boolean addElement(AbstractBusinessObjectModel model)
  {
    businessObjects.add(model);
    castor.addRule(model.getCastor());
    
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null, model);
    return true;
  }
  

  
  private boolean addElement(DecisionModel model)
  {
    decisions.add(model);
    comments.add(model.getCommentModel());
    castor.addRule(model.getCastor());
    
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null, model);
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null, model.getCommentModel());
    Iterator iter = model.getConditionalExitModels().iterator();
    while(iter.hasNext())
    {
      ConditionalExitModel exit = (ConditionalExitModel)iter.next();
      if(exit.getTransistionModel()!=null)
      {
        model.firePropertyChange(SOURCE_CONNECTIONS_PROP, model, null);
        exit.getTransistionModel().getToRuleModel().firePropertyChange(TARGET_CONNECTIONS_PROP, null, model);
      }
    }
    return true;
  }
   
  public boolean addElement(AbstractTransitionModel model)
  {
    transitions.add(model);

    AbstractSourceModel from = model.getFromRuleModel();
    AbstractTargetModel to   = model.getToRuleModel();
    
    from.setSuccessor(to);
    
    from.firePropertyChange(SOURCE_CONNECTIONS_PROP,null,from);
    to.firePropertyChange(TARGET_CONNECTIONS_PROP,null,to);
    
    firePropertyChange(PROPERTY_ELEMENT_ADDED,null, model);
    return true;
  }

  public boolean removeElement(AnnotationModel model)
  {
    annotations.remove(model);
    for(int i=0;i<castor.getAnnotationCount();i++)
    {
      Annotation rule = castor.getAnnotation(i);
      if(rule.equals(model.getCastor()))
      {
        castor.removeRule(i);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
        break;
      }
    }
    return true;
  }
  

  public boolean removeElement(RuleModel model)
  {
  	if (model instanceof DecisionModel)
  		return removeElement((DecisionModel)model);
  	if(model instanceof AbstractBusinessObjectModel)
  		return removeElement((AbstractBusinessObjectModel)model);
  	return false;
  }
  
  private boolean removeElement(AbstractBusinessObjectModel model)
  {
    businessObjects.remove(model);
    model.removePropertyChangeListener(this);
    for(int i=0;i<castor.getRuleCount();i++)
    {
      Rule rule = castor.getRule(i);
      if(rule.getRuleId().equals(model.getId()))
      {
        castor.removeRule(i);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
        break;
      }
    }
    return true;
  }
  
  
  private boolean removeElement(DecisionModel model)
  {
    decisions.remove(model);
    comments.remove(model.getCommentModel());
    model.removePropertyChangeListener(this);
    model.getCommentModel().removePropertyChangeListener(this);
    for(int i=0;i<castor.getRuleCount();i++)
    {
      Rule rule = castor.getRule(i);
      if(rule.getRuleId().equals(model.getId()))
      {
        castor.removeRule(i);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, model.getCommentModel(), null);
        Iterator iter = model.getConditionalExitModels().iterator();
        while(iter.hasNext())
        {
          ConditionalExitModel exit = (ConditionalExitModel)iter.next();
          if(exit.getTransistionModel()!=null)
          {
            model.firePropertyChange(SOURCE_CONNECTIONS_PROP, model, null);
            exit.getTransistionModel().getToRuleModel().firePropertyChange(TARGET_CONNECTIONS_PROP, model, null);
          }
        }
        break;
      }
    }
    return true;
  }

  public boolean removeElement(AbstractTransitionModel model)
  {
    transitions.remove(model);
    model.removePropertyChangeListener(this);
    model.getFromRuleModel().setSuccessor(null);
    model.getFromRuleModel().firePropertyChange(SOURCE_CONNECTIONS_PROP, model, null);
    model.getToRuleModel().firePropertyChange(TARGET_CONNECTIONS_PROP, model, null);
    
    return true;
  }
  
  public Ruleset getCastor()
  {
    return castor;
  }
  
  protected AbstractTargetModel getTargetModel(String ruleId)
  {
    Iterator iter = businessObjects.iterator();
    while(iter.hasNext())
    {
      RuleModel model =(RuleModel)iter.next();
      
      if(model instanceof AbstractTargetModel && model.getId().equals(ruleId))
        return (AbstractTargetModel)model;
    }
    
    iter = decisions.iterator();
    while(iter.hasNext())
    {
      RuleModel model =(RuleModel)iter.next();
      if(model instanceof AbstractTargetModel && model.getId().equals(ruleId))
        return (AbstractTargetModel)model;
    }
    return null;
  }
 
  protected AbstractSourceModel getSourceModel(String ruleId)
  {
    Iterator iter = businessObjects.iterator();
    while(iter.hasNext())
    {
      RuleModel model =(RuleModel)iter.next();
      if(model instanceof AbstractSourceModel && model.getId().equals(ruleId))
        return (AbstractSourceModel)model;
    }
    
    iter = decisions.iterator();
    while(iter.hasNext())
    {
      RuleModel model =(RuleModel)iter.next();
      if(model instanceof AbstractSourceModel && model.getId().equals(ruleId))
        return (AbstractSourceModel)model;
    }
    return null;
  }
  
  public List getSourceTransitions(AbstractSourceModel model)
  {
    List result = new ArrayList();
    Iterator iter = transitions.iterator();
    while(iter.hasNext())
    {
      AbstractTransitionModel transition = (AbstractTransitionModel)iter.next();
      if(transition.getFromRuleModel() ==model)
        result.add(transition);
    }
    return result;
  }
  
  
  public AbstractTransitionModel getSourceTransition(DecisionExitValueModel model)
  {
    Iterator iter = transitions.iterator();
    while(iter.hasNext())
    {
      AbstractTransitionModel transition = (AbstractTransitionModel)iter.next();
      if(transition.getFromRuleModel()==model)
        return transition;
    }
    return null;
  }

  public List getTargetTransitions(AbstractTargetModel model)
  {
    List result = new ArrayList();
    Iterator iter = transitions.iterator();
    while(iter.hasNext())
    {
      AbstractTransitionModel transition = (AbstractTransitionModel)iter.next();
      if(transition.getToRuleModel() ==model)
        result.add(transition);
    }
    
    return result;
  }
  
  /** 
   * Return a List of Shapes in this diagram. 
   */
  public List getChildren() 
  {
    List result = new ArrayList();

    result.addAll(annotations);
    result.addAll(comments);
    result.addAll(decisions);
    result.addAll(businessObjects);
    
    return result;
  }
  
  public void propertyChange(PropertyChangeEvent ev)
	{
  	firePropertyChange(PROPERTY_MODEL_CHANGED,null,this);
	}

	public List getCommentModels() 
	{
		return comments;
	}
}
