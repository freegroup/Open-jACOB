/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.util.Arrays;
import de.tif.jacob.designer.model.FieldModelTypeEnum;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.ruleengine.castor.ConditionalExit;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.ruleengine.castor.types.DecisionType;
import de.tif.jacob.ruleengine.decision.EnumDecision;

public class EnumDecisionModel extends DecisionModel
{
  public EnumDecisionModel()
  {
    super();
    getCastor().getDecision().setType(DecisionType.ENUM);
    getCastor().getDecision().setDecisionClass(EnumDecision.class.getName());
    getCastor().getDecision().setMethodName("getValue");
  }
  
  /**
   * @param castorRule
   */
  public EnumDecisionModel(RulesetModel parent, Rule castorRule)
  {
    super(parent, castorRule);
  }

  protected ConditionalExitModel createExitFromCastor(ConditionalExit exit)
  {
      return new ConditionalExitModel(this, exit);
  }
  
  public void setField(TableAliasModel alias, FieldModelTypeEnum enumField)
  {
  	// name des Datenbankfeldes abspeichern
  	//
  	String param[] = new String[2];
  	param[0] = alias.getName();
  	param[1] = enumField.getFieldModel().getName();

    // keine Änderung
    //
    if(Arrays.equals(param,getParameters()))
      return;
    
  	setParameters(param);
  	
  	// alle möglichen werte als ConditionalExits eintragen
  	//
  	cleanChildren();
  	String values[] = enumField.getEnumValues();
  	for(int i=0;i<values.length;i++)
  	{
  		ConditionalExitModel exitModel = new ConditionalExitModel(this,values[i]);
      getCastor().getDecision().addConditionalExit(exitModel.getCastor());
      exits.add(exitModel);
  	}
  	firePropertyChange(PROPERTY_MODEL_CHANGED,null,this);
  }
  
  public String getDisplayLabel()
  {
    String param[] = getParameters();
    if(param==null || param.length!=2)
      return "<UNSET>";
    return  param[0]+"."+param[1];
  }
}
