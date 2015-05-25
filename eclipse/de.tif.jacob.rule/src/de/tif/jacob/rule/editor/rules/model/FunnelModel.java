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
import de.tif.jacob.ruleengine.bo.NOP;
import de.tif.jacob.ruleengine.castor.*;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class FunnelModel extends AbstractBusinessObjectModel implements AbstractMultiTargetModel, AbstractSourceModel
{
  public FunnelModel()
  {
    super();
    BusinessObjectMethod  method =new BusinessObjectMethod();
    method.setMethodName("nop");
    method.setBusinessClass(NOP.class.getName());
    method.setParameter(new String[0]);
    getCastor().setBusinessObjectMethod(method);
    getCastor().setPosX(100);
    getCastor().setPosY(100);
    getCastor().setRuleId(new UID().toString());
  }
  
  /**
   * @param castorRule
   */
  public FunnelModel(RulesetModel parent, Rule castorRule)
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
   * NOP hat keine Parameter
   */
  public void setParameters(String[] parameters)
  {
    super.setParameters(null);
  }

  /**
   * 
   * @param method methodName signature
   */
  public void setBusinessObjectMethod(String method)
  {
  }
}
