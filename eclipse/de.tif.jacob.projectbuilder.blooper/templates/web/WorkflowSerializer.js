/* This notice must be untouched at all times.

Open-jACOB Draw2D
The latest version is available at
http://www.openjacob.org

Copyright (c) 2006 Andreas Herz. All rights reserved.
Created 5. 11. 2006 by Andreas Herz (Web: http://www.freegroup.de )

LICENSE: LGPL

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License (LGPL) as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA,
or see http://www.gnu.org/copyleft/lesser.html
*/
function WorkflowSerializer()
{

  return this;
}

WorkflowSerializer.prototype = new Object;
WorkflowSerializer.prototype.type="WorkflowSerializer";

WorkflowSerializer.prototype.toXML=function(workflow /*:Workflow*/)
{
  var xml = '<?xml version="1.0" encoding="ISO-8859-1"?>\n';

  xml = xml+'<ruleset xmlns="http://www.example.org/ruleset" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ruleset">\n';
  var figures = workflow.getFigures();
  for(var i=0;i<figures.getSize();i++)
  {
      var figure = figures.get(i);
      var businessClass = figure.getProperty("businessClass");
      var decisionClass = figure.getProperty("decisionClass");
      if(businessClass!=null || decisionClass!=null)
      {
        var methodName = figure.getProperty("methodName");
        var ruleId     = figure.getProperty("ruleId");
        xml = xml +'<rule posX="'+figure.getX()+'" posY="'+figure.getY()+'" ruleId="'+ruleId+'">\n';
        if(decisionClass!=null)
        {
          var signature = figure.getProperty("signature");
          var type = figure.getProperty("type");
          xml = xml+ '<decision decisionClass="'+decisionClass+'" methodName="'+methodName+'" signature="'+signature+'" type="'+type+'" xmlns="">\n';
          // Alle moeglichen Ausgaenge auffuehren
          //
          xml = xml + this.getConditionalExits(workflow, figure);

          // Alle Parameter des BO rausschreiben
          //
          xml = xml + this.getParameterTags(workflow,figure);

          xml = xml + '</decision>\n';
        }
        else
        {
          var nextRuleId = null;
          if(figure.getOutputPort)
            nextRuleId = this.getNextRuleId(workflow, figure.getOutputPort());
          if(nextRuleId!=null)
            xml = xml+ '<businessObjectMethod nextRuleId="'+nextRuleId+'" businessClass="'+businessClass+'" methodName="'+methodName+'" xmlns="">\n';
          else
            xml = xml+ '<businessObjectMethod businessClass="'+businessClass+'" methodName="'+methodName+'" xmlns="">\n';
  
          // Alle Parameter des BO rausschreiben
          //
          xml = xml + this.getParameterTags(workflow,figure);
  
          xml = xml + '</businessObjectMethod>\n';
        }
        xml = xml +'</rule>\n';
      }
      else if(figure.type =="Annotation")
      {
        xml = xml +'<annotation>\n';
        xml = xml +'<x>'+figure.getX()+'</x>\n';
        xml = xml +'<y>'+figure.getY()+'</y>\n';
        xml = xml +'<width>'+figure.getWidth()+'</width>\n';
        xml = xml +'<height>'+figure.getHeight()+'</height>\n';
        xml = xml +'<data>'+figure.getText()+'</data>\n';
        xml = xml +'<color>'+figure.getBackgroundColor().hex()+'</color>\n';
        xml = xml +'</annotation>\n';
      }
  }

  xml = xml +'</ruleset>\n'
  return xml;
}


WorkflowSerializer.prototype.getNextRuleId=function(workflow /*:Workflow*/, port /*:Port*/)
{
  if(port ==null)
    return null;

  var lines = workflow.getLines();
  for(var i=0;i<lines.getSize();i++)
  {
    var line = lines.get(i);
    if(line instanceof Connection)
    {
      if(line.getSource()==port)
      {
        return line.getTarget().getParent().getProperty("ruleId");
      }
      if(line.getTarget()==port)
      {
        return line.getSource().getParent().getProperty("ruleId");
      }
    }
  }
  return null;
}

WorkflowSerializer.prototype.getParameterTags=function(workflow /*:Workflow*/, figure /*:Node*/)
{
  var parameter="";
  if(figure.type == "DecisionEnum")
  {
    var v = figure.getEnumField().split(".");
    parameter = parameter+ '<parameter>'+v[0]+'</parameter>\n';
    parameter = parameter+ '<parameter>'+v[1]+'</parameter>\n';
  }
  else
  {
    // Alle Parameter des BO rausschreiben
    //
    for(var i=0;;i++)
    {
      var value = figure.getProperty("parameter_"+i);
      if(value==null)
        break;
      parameter = parameter+ '<parameter>'+value+'</parameter>\n';
    }
  }
  return parameter;
}

WorkflowSerializer.prototype.getConditionalExits=function(workflow /*:Workflow*/, figure /*:Node*/)
{
  var text = "";
  if(figure.type == "DecisionBoolean")
  {
    var falsePort = figure.getFalsePort();
    var truePort  = figure.getTruePort();
    var onFalseBO = this.getNextRuleId(workflow, falsePort);
    var onTrueBO  = this.getNextRuleId(workflow, truePort);
    if(onFalseBO==null)
      text = text + '<conditionalExit value="false"/>\n';
    else
      text = text + '<conditionalExit value="false" ruleId="'+onFalseBO+'"/>\n';

    if(onTrueBO==null)
      text = text + '<conditionalExit value="true"/>\n';
    else
      text = text + '<conditionalExit value="true" ruleId="'+onTrueBO+'"/>\n';
  }
  else
  {
    var ports = figure.getOutputPorts();
    for(var i=0;i<ports.getSize();i++)
    {
      var port = ports.get(i);
      var nextRule = this.getNextRuleId(workflow, port);
      if(nextRule==null)
        text = text + '<conditionalExit value="'+port.getName()+'"/>\n';
      else
        text = text + '<conditionalExit value="'+port.getName()+'" ruleId="'+nextRule+'"/>\n';
      
    }
  }
  return text;
}

