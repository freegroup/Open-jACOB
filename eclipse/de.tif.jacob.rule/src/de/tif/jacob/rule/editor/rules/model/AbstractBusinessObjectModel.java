/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.rmi.server.UID;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.ruleengine.castor.*;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public abstract class AbstractBusinessObjectModel extends RuleModel
{

  public AbstractBusinessObjectModel()
  {
    super(new Rule());
    BusinessObjectMethod  method =new BusinessObjectMethod();
    method.setMethodName("method");
    method.setBusinessClass("class name");
    getCastor().setBusinessObjectMethod(method);
    getCastor().setPosX(100);
    getCastor().setPosY(100);
    getCastor().setRuleId(new UID().toString());
  }
  
  /**
   * @param castorRule
   */
  public AbstractBusinessObjectModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
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
  
  public void setBusinessObjectClass(String className)
  {
  	String save = getImplementationClass();
  	if(StringUtil.saveEquals(save,className))
  		return;
  	
  	getCastor().getBusinessObjectMethod().setBusinessClass(className);
  	firePropertyChange(PROPERTY_MODEL_CHANGED,save, className);
  }

  
  /**
   * 
   * @param method methodName signature
   */
  public void setBusinessObjectMethod(String method)
  {
  	String save = getBusinessObjectMethod();
  	if(StringUtil.saveEquals(save,method))
  		return;

  	int index = method.indexOf("(");
  	String methodName = method.substring(0,index);
  	String signature  = method.substring(index);
  	getCastor().getBusinessObjectMethod().setMethodName(methodName);
  	getCastor().getBusinessObjectMethod().setSignature(signature);
  	firePropertyChange(PROPERTY_MODEL_CHANGED,save, method);
  }

  public String getDisplayBusinessObjectClass()
  {
    return ClassUtil.getShortClassName(getCastor().getBusinessObjectMethod().getBusinessClass());
  }
  
  public String getImplementationClass()
  {
  	return getCastor().getBusinessObjectMethod().getBusinessClass();
  }

  
  public String getDisplayBusinessObjectMethod()
  {
    return getCastor().getBusinessObjectMethod().getMethodName();
  }

  public String getBusinessObjectMethod()
  {
  	return getCastor().getBusinessObjectMethod().getMethodName()+getCastor().getBusinessObjectMethod().getSignature();
  }
  
  public String[] getParameters()
  {
  	return getCastor().getBusinessObjectMethod().getParameter();
  }
  
  public void setParameters(String[] parameters)
  {
  	String[] save = getParameters();
  	
  	getCastor().getBusinessObjectMethod().setParameter(parameters);
  	firePropertyChange(PROPERTY_PARAMETERS_CHANGED,save, parameters);
  }
}
