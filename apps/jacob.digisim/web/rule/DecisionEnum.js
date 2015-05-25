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
function DecisionEnum()
{
  this.label = new Label("");
  this.label.setCanDrag(false);
  this.label.setSelectable(false);
  this.label.setBackgroundColor(new Color(255,255,255));
  this.label.setBorder(new LineBorder());
 
  Node.call(this);

  this.setDimension(50,50);
  this.inputPort = null;
  this.outputPorts = new Array();

  this.setProperty("decisionClass","de.tif.jacob.ruleengine.decision.EnumDecision");
  this.setProperty("methodName","getValue");
  this.setProperty("type","enum");
  this.setProperty("ruleId",this.id);

  this.setBackgroundColor(new Color(245,245,115));
  this.setResizeable(false);

  return this;
}

DecisionEnum.prototype = new Node;
DecisionEnum.prototype.type="DecisionEnum";


DecisionEnum.prototype.dispose=function()
{
  this.workflow.removeFigure(this.label);

  Image.prototype.dispose.call(this);
}

DecisionEnum.prototype.onDrag = function()
{
  /*:NAMESPACE*/Node.prototype.onDrag.call(this);
  this.updateLabel();
}


DecisionEnum.prototype.setPosition=function(xPos /*int*/, yPos /*int*/)
{
  /*:NAMESPACE*/Node.prototype.setPosition.call(this,xPos,yPos);
  this.updateLabel();
}


DecisionEnum.prototype.getInputPort=function()
{
  return this.inputPort;
}


DecisionEnum.prototype.setWorkflow=function(/*:Workflow*/ workflow)
{
  /*:NAMESPACE*/Node.prototype.setWorkflow.call(this,workflow);

  if(workflow==null)
    return;

  workflow.addFigure(this.label,this.x-20,this.y-10);

  this.inputPort = new InputPort();
  this.inputPort.setBackgroundColor(new Color(245,245,115));
  this.inputPort.setColor(new Color(115,115,115));
  this.inputPort.setWorkflow(workflow);
  this.inputPort.setDirection(4);
  this.addPort(this.inputPort,0,25);

  for(var i =0; i<this.outputPorts.length;i++)
  {
      this.outputPorts[i].setWorkflow(this.workflow);
      this.addPort(this.outputPorts[i],this.getWidth(),25+25*i);
  }
}

DecisionEnum.prototype.onDoubleClick=function()
{
  var dialog = new EnumDialog(this);
  this.workflow.showDialog(dialog);
}

DecisionEnum.prototype.setEnumValues=function(/*:String[]*/ values)
{
  // Eventuell alte Resource freigeben
  //
  for(var i = 0; i< this.outputPorts.length;i++)
  {
    // Deco label entfernen
    this.html.removeChild(this.outputPorts[i].decoLabel.getHTMLElement());
    // Port entfernen
    this.removePort(this.outputPorts[i]);
  }

  var portCount = values.length;
  // Höhe des Elementes anpassen
  //
  var newHeight = 25 + portCount*25;
  var newWidth  = 150;

  this.setDimension(newWidth,newHeight);

  this.outputPorts = new Array();
  for(var i = 0;i<portCount;i++)
  {
    this.outputPorts[i]= new OutputPort();
    this.outputPorts[i].setBackgroundColor(new Color(115,245,115));
    this.outputPorts[i].setColor(new Color(115,115,115));
    this.outputPorts[i].setDirection(2);
    this.outputPorts[i].setMaxFanOut(1);
    this.outputPorts[i].setProperty("value",values[i]);

    // Label des Ports bestimmen
    //
    this.outputPorts[i].decoLabel = new Label(values[i]+" =");
    this.outputPorts[i].decoLabel.setPosition(0,25+25*i-8);
    this.outputPorts[i].decoLabel.setDimension(newWidth-15,15);
    this.outputPorts[i].decoLabel.setWordwrap(false);
    this.outputPorts[i].decoLabel.setFontSize(8);
    this.outputPorts[i].decoLabel.setAlign("right");
    this.html.appendChild(this.outputPorts[i].decoLabel.getHTMLElement());
    if(this.workflow!=null)
    { 
      this.outputPorts[i].setWorkflow(this.workflow);
      this.addPort(this.outputPorts[i],this.getWidth(),25+25*i);
      this.outputPorts[i].paint();
    }
  }
}

DecisionEnum.prototype.setEnumField=function(/*:String */ enumField)
{
  var v = enumField.split(".");
  this.setProperty("parameter_0", v[0]); // alias
  this.setProperty("parameter_1", v[1]); // column
  this.updateLabel();
}

/**
 * 
 * @returns The current table.filed of the decision object
 * @type String
 **/
DecisionEnum.prototype.getEnumField=function()
{
  return this.getProperty("parameter_0")+"."+this.getProperty("parameter_1");
}

DecisionEnum.prototype.getOutputPorts=function()
{
  return this.outputPorts;
}

DecisionEnum.prototype.updateLabel=function()
{
  var alias=this.getProperty("parameter_0");
  var field=this.getProperty("parameter_1");

  this.label.setStyledText("<table cellspacing='0' cellpadding='0' ><tr><td style='color:blue;padding-right:10px'>Enum Field:</td><td>"+alias+"."+field+"</td></tr></table>");
  var xpos = this.getX()+(this.getWidth()/2)-(this.label.getWidth()/2);
  this.label.setPosition(xpos,this.y-this.label.getHeight()-2);
}
