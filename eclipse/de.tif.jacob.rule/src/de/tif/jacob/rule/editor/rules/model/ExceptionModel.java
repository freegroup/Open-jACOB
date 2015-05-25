/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.rmi.server.UID;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.ruleengine.bo.Alert;
import de.tif.jacob.ruleengine.bo.UserExceptionBO;
import de.tif.jacob.ruleengine.bo.UserInformation;
import de.tif.jacob.ruleengine.castor.*;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class ExceptionModel extends AbstractBusinessObjectModel implements AbstractTargetModel
{
  public ExceptionModel()
  {
    super();
    BusinessObjectMethod  method =new BusinessObjectMethod();
    method.setMethodName("send");
    method.setBusinessClass(UserExceptionBO.class.getName());
    getCastor().setBusinessObjectMethod(method);
    getCastor().setPosX(100);
    getCastor().setPosY(100);
    getCastor().setRuleId(new UID().toString());
  }
  
  /**
   * @param castorRule
   */
  public ExceptionModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
  }
  
  public AbstractTargetModel getSuccessor()
  {
    return null;
  }
  
  public void setSuccessor(AbstractTargetModel successor)
  {
  }
  
  public void setBusinessObjectClass(String className)
  {
  }
  
  public String getImplementationClass()
  {
    return getCastor().getBusinessObjectMethod().getBusinessClass();
  }


  /**
   * 
   * @param method methodName signature
   */
  public void setBusinessObjectMethod(String method)
  {
  }
}
