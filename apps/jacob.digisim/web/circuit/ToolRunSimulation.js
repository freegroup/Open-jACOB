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

function ToolRunSimulation(palette /*:PaletteWindow*/)
{
  Button.call(this,palette);
  this.down = false;
  this.timer = 0;
  return this;
}

ToolRunSimulation.prototype = new ToggleButton;
ToolRunSimulation.prototype.type="ToolRunSimulation";


ToolRunSimulation.prototype.execute=function()
{
  if(this.isDown())
  {
    this.palette.workflow.setCurrentSelection(null);
    this.timer = window.setInterval("ToolRunSimulation_run()",100);
    var figures = workflow.getFigures();
    for(key in figures)
    {
      var figure = figures[key];
      if(figure!=null && figure.startSimulation)
        figure.startSimulation();
    }
  }
  else
  {
    window.clearInterval(this.timer);
    var figures = workflow.getFigures();
    for(key in figures)
    {
      var figure = figures[key];
      if(figure!=null && figure.stopSimulation)
        figure.stopSimulation();
    }
  }
}

function ToolRunSimulation_run()
{
  var figures = workflow.getFigures();
  for(key in figures)
  {
    var figure = figures[key];
    if(figure!=null && figure.calculate)
      figure.calculate();
  }

  var lines = workflow.getLines();
  for(key in lines)
  {
    var line = lines[key];
    if(line!=null)
    {
      if(line.getSource().type == "OutputPort")
      {
        var value = line.getSource().getProperty("value");
        line.getTarget().setProperty("value",value);
      }
      else
      {
        var value = line.getTarget().getProperty("value");
        line.getSource().setProperty("value",value);
      }
      if(value==true)
        var color = new Color(255,0,0);
      else
        var color = new Color(0,0,255);
      line.setColor(color);
      line.getSource().setBackgroundColor(color);
      line.getTarget().setBackgroundColor(color);
    }
  }
}

