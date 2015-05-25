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
function WorkflowPalette()
{
  ToolPalette.call(this,"Tools");
  this.setDimension(75,430);

  this.tool1  = new ToolAnnotation(this);
  this.tool2  = new ToolDecisionBoolean(this);
  this.tool3  = new ToolDecisionEnum(this);
  this.tool4  = new ToolEnd(this);
  this.tool5  = new ToolUserExceptionBO(this);
  this.tool6  = new ToolEmail(this);
  this.tool7  = new ToolUserInformation(this);
  this.tool8  = new ToolFunnel(this);
  this.tool9  = new ToolFieldModifier(this);
  this.tool10 = new ToolSave(this);

  this.saveTool = this.tool10;
  this.saveTool.setEnabled(false);

  this.tool1.setPosition(10,30);
  this.tool2.setPosition(10,70);
  this.tool3.setPosition(10,110);
  this.tool4.setPosition(10,150);
  this.tool5.setPosition(10,190);
  this.tool6.setPosition(10,230);
  this.tool7.setPosition(10,270);
  this.tool8.setPosition(10,310);
  this.tool9.setPosition(10,350);
  this.tool10.setPosition(10,390);

  this.addChild(this.tool1);
  this.addChild(this.tool2);
  this.addChild(this.tool3);
  this.addChild(this.tool4);
  this.addChild(this.tool5);
  this.addChild(this.tool6);
  this.addChild(this.tool7);
  this.addChild(this.tool8);
  this.addChild(this.tool9);
  this.addChild(this.tool10);

  return this;
}

WorkflowPalette.prototype = new ToolPalette;
WorkflowPalette.prototype.type="WorkflowPalette";


WorkflowPalette.prototype.dispose=function()
{
  ToolPalette.prototype.dispose.call(this);
  this.tool1.dispose();
  this.tool2.dispose();
}

WorkflowPalette.prototype.onSetDocumentDirty=function()
{
  this.saveTool.setEnabled(true);
}