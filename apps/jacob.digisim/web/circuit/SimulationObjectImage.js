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
function SimulationObjectImage()
{
  if(browserId!=null)
    ImageFigure.call(this,"resource_image.jsp?browser="+browserId+"&partName="+this.type);
  else
    ImageFigure.call(this,"public_resource_image.jsp?partName="+this.type);
  this.simulationIsRunning = false;
  this.oldCanDrag = false;
  this.oldSelectable=false;
}

SimulationObjectImage.prototype = new ImageFigure;
SimulationObjectImage.prototype.type="SimulationObjectImage";


SimulationObjectImage.prototype.calculate=function()
{
}

SimulationObjectImage.prototype.isCircuitPart=function()
{
  return true;
}

/**
 * @private
 **/
SimulationObjectImage.prototype.startSimulation=function()
{
  this.oldCanDrag = this.canDrag;
  this.oldSelectable = this.isSelectable();
  this.setCanDrag(false);
  this.setSelectable(false);
  this.simulationIsRunning = true;
}

SimulationObjectImage.prototype.stopSimulation=function()
{
  this.setCanDrag(this.oldCanDrag);
  this.setSelectable(this.oldSelectable);

  this.simulationIsRunning = false;
}

SimulationObjectImage.prototype.isSimulationRunning=function()
{
  return this.simulationIsRunning;
}


/**
 * Callback method for the context menu..
 **/
SimulationObjectImage.prototype.getContextMenu=function()
{
  var menu =new Menu();
  var oThis = this;

  menu.appendMenuItem(new MenuItem("Help", null,function(){HelpManager.show(oThis.type);}));
  return menu;
}


