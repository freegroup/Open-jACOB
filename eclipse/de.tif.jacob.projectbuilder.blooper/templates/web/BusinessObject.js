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

function BusinessObject()
{
  this.setDimension(125,50);
  ImageFigure.call(this,this.type+".png");
  this.inputPort = null;
  this.outputPort = null;
  this.setProperty("ruleId",this.id);

  return this;
}

BusinessObject.prototype = new ImageFigure;
BusinessObject.prototype.type="BusinessObject";

BusinessObject.prototype.createHTMLElement=function()
{
    var item = ImageFigure.prototype.createHTMLElement.call(this);
    item.style.width=this.width+"px";
    item.style.height=this.height+"px";
    item.style.margin="0px";
    item.style.padding="0px";
    item.style.border="0px";
//    item.style.backgroundImage="url("+this.url+")";
    return item;
}

BusinessObject.prototype.getInputPort=function()
{
  return this.inputPort;
}

BusinessObject.prototype.getOutputPort=function()
{
  return this.outputPort;
}

BusinessObject.prototype.hasOutputPort=function()
{
  return true;
}

BusinessObject.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  ImageFigure.prototype.setWorkflow.call(this,workflow);

  if(workflow==null)
    return;
  this.inputPort = new InputPort();
  this.inputPort.setWorkflow(workflow);
  this.addPort(this.inputPort,0,this.height/2);

  if(this.hasOutputPort()==true)
  {
    this.outputPort = new OutputPort();
    this.outputPort.setWorkflow(workflow);
    this.outputPort.setMaxFanOut(1);
    this.addPort(this.outputPort,this.width,this.height/2);
  }
}

BusinessObject.prototype.setBusinessClass=function(/*:String*/ className, /*:String*/ methodName)
{
  this.setProperty("businessClass",className);
  this.setProperty("methodName",methodName);
}

