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

function WorkflowPropertyWindow()
{
  PropertyWindow.call(this);

  this.setDimension(180,150);
  return this;
}

WorkflowPropertyWindow.prototype = new PropertyWindow;
WorkflowPropertyWindow.prototype.type="WorkflowPropertyWindow";

WorkflowPropertyWindow.prototype.createHTMLElement=function()
{
  var item = PropertyWindow.prototype.createHTMLElement.call(this);

  this.fillColorLabel = this.createLabel("Fill Color:", 15,120);
  this.fillColorLabel.style.color="#d0d0d0";
  item.appendChild(this.fillColorLabel);

  this.fillColorArea = this.createLabel("&nbsp;", 85,120);
  this.fillColorArea.style.width="50px";
  this.fillColorArea.style.border="1px solid #d0d0d0";
  this.fillColorArea.hostDialog = this;
  this.fillColorArea.onclick=function(){this.hostDialog.showFillColorDialog();};
  item.appendChild(this.fillColorArea);

  return item;
}

WorkflowPropertyWindow.prototype.onSelectionChanged=function(figure /*:Figure*/)
{
  PropertyWindow.prototype.onSelectionChanged.call(this,figure);

  if(figure != null && (typeof figure.getBackgroundColor =="function") && figure.getBackgroundColor()!=null)
  {
    this.fillColorArea.style.background = figure.getBackgroundColor().getHTMLStyle();
    this.fillColorArea.style.cursor="pointer";
    this.fillColorArea.style.border="1px solid gray";
    this.fillColorLabel.style.color="black";
  }
  else
  {
    this.fillColorArea.style.background = "transparent";
    this.fillColorArea.style.cursor=null;
    this.fillColorArea.style.border="1px solid #d0d0d0";
    this.fillColorLabel.style.color="#d0d0d0";
  }
}


WorkflowPropertyWindow.prototype.showFillColorDialog=function()
{
  if(typeof this.getCurrentSelection().getBackgroundColor != "function")
    return;

  var dialog = new BackgroundColorDialog(this.getCurrentSelection());
  dialog.setColor(this.getCurrentSelection().getBackgroundColor());
  this.workflow.showDialog(dialog);
}