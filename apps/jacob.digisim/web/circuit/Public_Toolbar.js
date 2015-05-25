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

/**
 * 
 * @constructor
 */
function Public_Toolbar()
{
  this.currentSelection = null;
  ToolPalette.call(this,"Toolbar");
  this.setDimension(300,50);

  this.tool1 = new ToolRunSimulation(this);
  this.tool1.setPosition(10,5);
  this.addChild(this.tool1);

  return this;
}
Public_Toolbar.prototype = new ToolPalette;
Public_Toolbar.prototype.type="Public_Toolbar";


/**
 * @private
 **/
Public_Toolbar.prototype.dispose=function()
{
  ToolPalette.prototype.dispose.call(this);
}

Public_Toolbar.prototype.onSetDocumentDirty=function()
{
}