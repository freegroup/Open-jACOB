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
function SimulationObjectRectangle()
{
  Node.call(this);
  this.simulationIsRunning = false;
  this.oldCanDrag = false;
  this.oldSelectable=false;
  this.setColor(new Color(64,64,0));
}

SimulationObjectRectangle.prototype = new Node;
SimulationObjectRectangle.prototype.type="SimulationObjectRectangle";


SimulationObjectRectangle.prototype.calculate=function()
{
}

SimulationObjectRectangle.prototype.isCircuitPart=function()
{
  return true;
}

/**
 * @private
 **/
SimulationObjectRectangle.prototype.startSimulation=function()
{
  this.oldCanDrag = this.canDrag;
  this.oldSelectable = this.isSelectable();
  this.setCanDrag(false);
  this.setSelectable(false);
  this.simulationIsRunning = true;
}

SimulationObjectRectangle.prototype.stopSimulation=function()
{
  this.setCanDrag(this.oldCanDrag);
  this.setSelectable(this.oldSelectable);

  this.simulationIsRunning = false;
}

SimulationObjectRectangle.prototype.isSimulationRunning=function()
{
  return this.simulationIsRunning;
}


/**
 * @private
 * @param {String} text The text fo the label
 * @param {int} x x position of the label
 * @param {int} y y position of the label
 * @type HTMLElement
 **/
SimulationObjectRectangle.prototype.createLabel=function(text /*:String*/, x /**:int*/, y /*:int*/)
{
  var element = document.createElement("div");
  element.style.left = x+"px";
  element.style.top  = y+"px";
  element.style.font="normal 11px verdana";
  element.style.position ="absolute";
  element.innerHTML=text;
  return element;
}


/**
 * Callback method for the context menu..
 **/
SimulationObjectRectangle.prototype.getContextMenu=function()
{
  var menu =new Menu();
  var oThis = this;

  menu.appendMenuItem(new MenuItem("Help", null,function(){HelpManager.show(oThis.type);}));
  return menu;
}
