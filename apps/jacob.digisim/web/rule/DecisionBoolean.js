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
function DecisionBoolean()
{
  this.label = new Label("");
  this.label.setCanDrag(false);
  this.label.setSelectable(false);
  this.label.setBackgroundColor(new Color(255,255,255));
  this.label.setBorder(new LineBorder());
  Image.call(this,this.type+".png");

  this.setDimension(50,50);
  this.inputPort = null;
  this.falsePort = null;
  this.truePort  = null;

  this.setProperty("decisionClass","de.tif.jacob.ruleengine.decision.BooleanDecision");
  this.setProperty("methodName","hasZeroLenght");
  this.setProperty("type","boolean");
  this.setProperty("parameter_0","[YourValueHere]");
  this.setProperty("ruleId",this.id);

  return this;
}

DecisionBoolean.prototype = new Image;
DecisionBoolean.prototype.type="DecisionBoolean";


DecisionBoolean.prototype.dispose=function()
{
  this.workflow.removeFigure(this.label);

  Image.prototype.dispose.call(this);

}

DecisionBoolean.prototype.getInputPort=function()
{
  return this.inputPort;
}

DecisionBoolean.prototype.getTruePort=function()
{
  return this.truePort;
}

DecisionBoolean.prototype.getFalsePort=function()
{
  return this.falsePort;
}

DecisionBoolean.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  Image.prototype.setWorkflow.call(this,workflow);

  if(workflow==null)
    return;

  workflow.addFigure(this.label,this.x-20,this.y-10);
  this.inputPort = new InputPort();
  this.falsePort = new OutputPort();
  this.truePort  = new OutputPort();

  this.falsePort.setBackgroundColor(new Color(245,115,115));
  this.truePort.setBackgroundColor(new Color(115,245,115));
  this.inputPort.setBackgroundColor(new Color(245,245,115));
  this.inputPort.setColor(new Color(115,115,115));

  this.inputPort.setWorkflow(workflow);
  this.inputPort.setDirection(4);

  this.falsePort.setWorkflow(workflow);
  this.falsePort.setDirection(3);
  this.falsePort.setMaxFanOut(1);
  this.falsePort.setProperty("value","false");

  this.truePort.setWorkflow(workflow);
  this.truePort.setDirection(2);
  this.truePort.setMaxFanOut(1);
  this.truePort.setProperty("value","true");

  this.addPort(this.inputPort,0,25);
  this.addPort(this.falsePort,25,50);
  this.addPort(this.truePort,50,25);
}

DecisionBoolean.prototype.onDrag = function()
{
  Image.prototype.onDrag.call(this);
  this.updateDecisionClassLabel();
}

DecisionBoolean.prototype.setPosition=function(xPos /*int*/, yPos /*int*/)
{
  Image.prototype.setPosition.call(this,xPos,yPos);
  this.updateDecisionClassLabel();
}

DecisionBoolean.prototype.onDoubleClick=function()
{
  var dialog = new DecisionDialog(this);
  this.workflow.showDialog(dialog);
}

DecisionBoolean.prototype.setDecisionClass=function(decisionClass /*:String*/, methodName /*:String*/)
{
  this.setProperty("decisionClass",decisionClass);
  this.setProperty("methodName",methodName);
  this.updateDecisionClassLabel();
}

DecisionBoolean.prototype.updateDecisionClassLabel=function()
{
  var dClass=this.getProperty("decisionClass");
  var dMethod=this.getProperty("methodName");

  var parts = dClass.split(".");
//  var s = parts[parts.length-1]+"."+dMethod;
  var s = dMethod;
  var s= '<table cellspacing="0" cellpadding="0" ><tr><td style="color:blue;">'+dMethod+' </td>';
  for(var i =0; i<4;i++)
  {
    var param = this.getProperty("parameter_"+i);
    if(param==null)
      break
    param=param+"<td>";
    if(i==0)
      s= s+"<td style='color:blue;'>( </td><td>"+param+"<td>";
    else
      s= s+"<td style='color:blue;'>, </td><td>"+param+"</td>";
  }
  s=s+"<td style='color:blue;'> )</td></tr></table>";
//  this.label.setText(s);
  this.label.setStyledText(s);
  var xpos = this.getX()+(this.getWidth()/2)-(this.label.getWidth()/2)+10;
  this.label.setPosition(xpos,this.y-this.label.getHeight()-3);
  
}
