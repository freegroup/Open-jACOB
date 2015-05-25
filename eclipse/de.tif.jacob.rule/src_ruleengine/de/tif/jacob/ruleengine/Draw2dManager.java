/*
 * Created on 14.11.2006
 *
 */
package de.tif.jacob.ruleengine;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import de.tif.jacob.ruleengine.bo.Alert;
import de.tif.jacob.ruleengine.bo.Email;
import de.tif.jacob.ruleengine.bo.End;
import de.tif.jacob.ruleengine.bo.FieldModifier;
import de.tif.jacob.ruleengine.bo.Funnel;
import de.tif.jacob.ruleengine.bo.NOP;
import de.tif.jacob.ruleengine.bo.TwitterStatus;
import de.tif.jacob.ruleengine.bo.UserExceptionBO;
import de.tif.jacob.ruleengine.bo.UserInformation;
import de.tif.jacob.ruleengine.castor.Annotation;
import de.tif.jacob.ruleengine.castor.BusinessObjectMethod;
import de.tif.jacob.ruleengine.castor.ConditionalExit;
import de.tif.jacob.ruleengine.castor.Decision;
import de.tif.jacob.ruleengine.castor.Rule;
import de.tif.jacob.ruleengine.castor.Ruleset;
import de.tif.jacob.ruleengine.castor.types.DecisionType;
import de.tif.jacob.util.StringUtil;

public class Draw2dManager
{
  static Map class2JavascriptObjects = new HashMap();
  static
  {
    class2JavascriptObjects.put(UserExceptionBO.class.getName(), "UserExceptionBO");
    class2JavascriptObjects.put(Email.class.getName(), "Email");
    class2JavascriptObjects.put(Alert.class.getName(), "Alert");
    class2JavascriptObjects.put(UserInformation.class.getName(), "UserInformation");
    class2JavascriptObjects.put(NOP.class.getName(), "NOP");
    class2JavascriptObjects.put(End.class.getName(), "End");
    class2JavascriptObjects.put(Funnel.class.getName(), "Funnel");
    class2JavascriptObjects.put(FieldModifier.class.getName(), "FieldModifier");
    class2JavascriptObjects.put(TwitterStatus.class.getName(), "TwitterStatus");
  }
  
  public static void main(String[] args) throws Exception
  {
    try
    {
//      InputStream stream = Draw2dManager.class.getResourceAsStream("notification.ruleset");
   InputStream stream = Draw2dManager.class.getResourceAsStream("test.ruleset");
      System.out.println(toDraw2D(stream));
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static String toDraw2D(InputStream stream) throws Exception
  {
      StringBuffer sb = new StringBuffer();
      Ruleset ruleset=(Ruleset)Ruleset.unmarshalRuleset(new InputStreamReader(stream,"ISO-8859-1"));
      sb.append("var annotation=null;\n");
      for (int i = 0; i < ruleset.getAnnotationCount(); i++)
      {
        Annotation annotation = ruleset.getAnnotation(i);
        String string = annotation.getData();
        string = string.replaceAll("\\n","%0D%0A");
        sb.append("annotation = new Annotation(unescape(\""+string+"\"));\n");
        sb.append("annotation.setDimension("+annotation.getWidth()+","+annotation.getHeight()+");\n");
        if(annotation.getColor()!=null)
          sb.append("annotation.setBackgroundColor(new Color(\""+annotation.getColor()+"\"));\n");
        sb.append("annotation.setDimension("+annotation.getWidth()+","+annotation.getHeight()+");\n");
        
        sb.append("workflow.addFigure(annotation,"+annotation.getX()+","+annotation.getY()+");\n");
      }
      sb.append("\n\n/* Create the used objects */\n");
      sb.append("var objectMap = new Object();\n"); 
      sb.append("var object = null;\n"); 
      for (int i = 0; i < ruleset.getRuleCount(); i++)
      {
        Rule rule = ruleset.getRule(i);
        if(rule.getRuleId().equals("start"))
        {
          sb.append("object = new Start();\n");
          sb.append("object.setProperty(\"businessClass\",\"unused\");\n");
          sb.append("object.setProperty(\"methodName\",\"unused\");\n");
        }
        else if(rule.getDecision()!=null)
        {
           Decision decision = rule.getDecision();
           if(decision.getType().equals(DecisionType.BOOLEAN))
           {
             sb.append("object = new DecisionBoolean();\n");
             for (int j = 0; j < decision.getParameterCount(); j++)
             {
               String string = decision.getParameter(j);
               string = string.replaceAll("\\n","%0D%0A");
               sb.append("object.setProperty(\"parameter_"+j+"\",\""+string+"\");\n");
             }
           }
           else if(decision.getType().equals(DecisionType.ENUM))
           {
             sb.append("object = new DecisionEnum();\n");
             for (int j = 0; j < decision.getParameterCount(); j++)
             {
               String string = decision.getParameter(j);
               string = string.replaceAll("\\n","%0D%0A");
               sb.append("object.setProperty(\"parameter_"+j+"\",\""+string+"\");\n");
             }
             // Alle zulässigen Ausgänge der EnumDecision eintragen
             //
             String enums = "new Array(";
             for(int ce=0;ce<decision.getConditionalExitCount();ce++)
             {
               ConditionalExit exit = decision.getConditionalExit(ce);
               enums = enums + "\""+exit.getValue()+"\"";
               if((ce+1)<decision.getConditionalExitCount())
                 enums = enums+",";
             }
             enums = enums+")";
             sb.append("object.setEnumValues(");
             sb.append(enums);
             sb.append(");\n");
           }
           else
           {
             sb.append("object = new DecisionString();\n");
           }
           sb.append("object.setProperty(\"decisionClass\",\""+decision.getDecisionClass()+"\");\n");
           sb.append("object.setProperty(\"methodName\",\""+decision.getMethodName()+"\");\n");
           sb.append("object.setProperty(\"signature\",\""+decision.getSignature()+"\");\n");
        }
        else
        {
          BusinessObjectMethod boMethod = rule.getBusinessObjectMethod();
          String javascriptObj = (String)class2JavascriptObjects.get(boMethod.getBusinessClass());
          if(javascriptObj==null)
            javascriptObj = "BusinessObject";
          sb.append("object = new "+javascriptObj+"();\n");
          sb.append("object.setProperty(\"businessClass\",\""+boMethod.getBusinessClass()+"\");\n");
          sb.append("object.setProperty(\"methodName\",\""+boMethod.getMethodName()+"\");\n");
          for (int j = 0; j < boMethod.getParameterCount(); j++)
          {
            String string = boMethod.getParameter(j);
            string = string.replaceAll("\\n","%0D%0A");
            sb.append("object.setProperty(\"parameter_"+j+"\",unescape(\""+string+"\"));\n");
          }
        }
        sb.append("object.setProperty(\"ruleId\",\""+rule.getRuleId()+"\");\n");
        sb.append("objectMap['"+rule.getRuleId()+"'] = object;\n");
      }
      
      sb.append("\n\n/* Add the objects to the workflow */\n");
      for (int i = 0; i < ruleset.getRuleCount(); i++)
      {
        Rule rule = ruleset.getRule(i);
        sb.append("workflow.addFigure(objectMap['"+rule.getRuleId()+"'],"+rule.getPosX()+","+rule.getPosY()+");\n");
      }

      sb.append("\n\n/* Create the connections between this objects */\n");
      sb.append("var connection = null;\n");
      for (int i = 0; i < ruleset.getRuleCount(); i++)
      {
        Rule rule = ruleset.getRule(i);
        String current = rule.getRuleId();
        if(rule.getBusinessObjectMethod()!=null && rule.getBusinessObjectMethod().getNextRuleId()!=null)
        {
          String next = rule.getBusinessObjectMethod().getNextRuleId();
          String self = rule.getRuleId();
          sb.append("connection = new Connection();\n");
          sb.append("connection.setSource(objectMap['"+self+"'].getOutputPort());\n");
          sb.append("connection.setTarget(objectMap['"+next+"'].getInputPort());\n");
          sb.append("workflow.addFigure(connection);\n");
        }
        else if(rule.getDecision()!=null)
        {
           Decision decision = rule.getDecision();
           for(int e=0; e<decision.getConditionalExitCount();e++)
           {
             ConditionalExit exit = decision.getConditionalExit(e);
             if(exit.getRuleId()!=null)
             {
               String next = exit.getRuleId();
               sb.append("connection = new Connection();\n");
               sb.append("connection.setSource(objectMap['"+current+"'].getPort(\""+exit.getValue()+"\"));\n");
               sb.append("connection.setTarget(objectMap['"+next+"'].getInputPort());\n");
               sb.append("workflow.addFigure(connection);\n");
//               sb.append()
             }
           }
        }
        else
        {
           //sb.append("objectMap['"+rule.getRuleId()+" = new BusinessObjectMethod();\n");
        }
      }
      return sb.toString();
    }
}
