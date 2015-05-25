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
  this.setDimension(119,532);
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
  this.tool11 = new ToolTwitterStatus(this);
  this.tool12 = new ToolDelete(this);
  this.undoTool = new ToolUndo(this);
  this.redoTool = new ToolRedo(this);

  this.undoTool.setEnabled(false);
  this.redoTool.setEnabled(false);

  this.saveTool = this.tool10;
  this.saveTool.setEnabled(false);

  this.deleteTool = this.tool12;
  this.deleteTool.setEnabled(false);

  this.tool1.setPosition(15,110); // Annotation
  this.tool2.setPosition(15,385);  // Decision Boolean
  this.tool3.setPosition(65,385);
  this.tool4.setPosition(15,465); // End
  this.tool5.setPosition(65,465); // Exception
  this.tool6.setPosition(15,265); // eMail
  this.tool7.setPosition(65,305); // User Notification
  this.tool8.setPosition(15,425); // Funnel
  this.tool9.setPosition(15,185); // Modification
  this.tool10.setPosition(10,40); // Save
  this.tool11.setPosition(15,305); // Twitter
  this.deleteTool.setPosition(90,40); // Delete
  this.undoTool.setPosition(35,40); // Undo
  this.redoTool.setPosition(60,40); // Redo

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
  this.addChild(this.tool11);
  this.addChild(this.tool12);
  this.addChild(this.undoTool);
  this.addChild(this.redoTool);
}

WorkflowPalette.prototype = new ToolPalette;
WorkflowPalette.prototype.type="WorkflowPalette";


/**
 * @private
 **/
WorkflowPalette.prototype.createHTMLElement=function()
{
  var item = ToolPalette.prototype.createHTMLElement.call(this);
  item.style.backgroundImage="url(palette_bg.png)";
  item.style.overflow="hidden";
  item.style.border= "0px";

  item.style.MozBorderRadius="13px";
  item.style.MozBoxShadow ="5px 5px 5px rgba(0, 0, 0, 0.4)";

  item.style.WebkitBorderRadius="13px";
  item.style.WebkitBoxShadow ="5px 5px 5px rgba(0, 0, 0, 0.4)";

  this.titlebar.style.background="";
  this.titlebar.style.border="0px";

  this.scrollarea.style.overflow="hidden";
  this.scrollarea.style.border="0px";
 
  this.createLabel(15,85,"Documentation");
  this.createLabel(15,165,"Modification");
  this.createLabel(15,245,"Notification");
  this.createLabel(15,365,"Flow Control");

  return item;
}

WorkflowPalette.prototype.dispose=function()
{
  ToolPalette.prototype.dispose.call(this);
  this.tool1.dispose();
  this.tool2.dispose();
}

WorkflowPalette.prototype.onSetDocumentDirty=function()
{
  this.saveTool.setEnabled(true);
  this.undoTool.setEnabled(this.workflow.getCommandStack().canUndo());
  this.redoTool.setEnabled(this.workflow.getCommandStack().canRedo());
}

WorkflowPalette.prototype.createLabel=function(/*:int*/ x, /*:int*/ y, /*:String*/ text)
{
  var label = document.createElement("div");
  label.innerHTML=text;
  label.style.position="absolute"
  label.style.left=x+"px";
  label.style.top=y+"px";
  label.style.fontSize="10px";
  label.style.color="white";
  
  this.scrollarea.appendChild(label);
}

WorkflowPalette.prototype.onSelectionChanged=function(/*:Figure*/ figure)
{
  ToolPalette.prototype.onSelectionChanged.call(this,figure);

  this.deleteTool.setEnabled(figure!=null && figure.isDeleteable()==true);
}

