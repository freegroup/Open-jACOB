/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.rmi.server.UID;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import de.tif.jacob.ruleengine.bo.Alert;
import de.tif.jacob.ruleengine.bo.Email;
import de.tif.jacob.ruleengine.castor.*;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class MessageEMailModel extends AbstractBusinessObjectModel implements AbstractTargetModel, AbstractSourceModel
{
  public MessageEMailModel()
  {
    super();
    BusinessObjectMethod  method =new BusinessObjectMethod();
    method.setMethodName("send");
    method.setBusinessClass(Email.class.getName());
    method.setParameter(new String[3]);
    getCastor().setBusinessObjectMethod(method);
    getCastor().setPosX(100);
    getCastor().setPosY(100);
    getCastor().setRuleId(new UID().toString());
   
  }
  
  /**
   * @param castorRule
   */
  public MessageEMailModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
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
  
  public String getTo()
  {
    return StringUtil.toSaveString(getParameters()[0]);
  }
  
  public String getSubject()
  {
    return StringUtil.toSaveString(getParameters()[1]);
  }
  
  public String getMessage()
  {
    return StringUtil.toSaveString(getParameters()[2]);
  }
  
  public void setTo(String to)
  {
    String p[] = getParameters();
    p[0]=to;
    setParameters(p);
  }
  
  public void setSubject(String subject)
  {
    String p[] = getParameters();
    p[1]=subject;
    setParameters(p);
  }
  
  public void setMessage(String message)
  {
    String p[] = getParameters();
    p[2]=message;
    setParameters(p);
  }
}
