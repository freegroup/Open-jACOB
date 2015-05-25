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

function ToolSave(palette /*:PaletteWindow*/)
{
  Button.call(this,palette);

  this.setDimension(24,18);
  this.setTooltip("Save");
}

ToolSave.prototype = new Button;
ToolSave.prototype.type="ToolSave";

ToolSave.prototype.execute=function()
{
  oThis = this;
  if (window.XMLHttpRequest) req = new XMLHttpRequest();
  else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
  else return; // fall on our sword
  req.open("POST", "save.jsp?pkey="+pkey+"&browser="+browserId);
  req.setRequestHeader('content-type', 'text/plain');
  req.onreadystatechange = function ()
  {
      if (req.readyState == 4) 
      {
        oThis.setEnabled(false);
      }
  };
  req.send(new WorkflowSerializer().toXML(this.palette.workflow));
}