/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

function jACOBCalendar(/*: String*/ containerId)
{
  var container = getObj(containerId);
  container.style.overflow = "hidden";
  this.lastAppointmentContainer =null;
  this.cells = new Array();
  this.cellDatas = new Array();
  this.height = parseInt(container.style.height);
  this.width  = parseInt(container.style.width);
  var cell = this.firstChild(container,"div");
  oThis = this;
  var rowIndex=0;
  while(cell!=null)
  {
      this.cells[rowIndex] = new Array();
      this.cellDatas[rowIndex] = new Array();

      for(var columnIndex=0;columnIndex<7;columnIndex++)
      {
         this.cells[rowIndex][columnIndex] = cell;
         cell.setAttribute("row",rowIndex);
         cell.setAttribute("column",columnIndex);
         cell.setAttribute("dayContainer","true");

         var cellData = new Object();
         cellData.currentX      = parseInt(cell.style.left);
         cellData.currentY      = parseInt(cell.style.top);
         cellData.currentWidth  = parseInt(cell.style.width);
         cellData.currentHeight = parseInt(cell.style.height);
         cellData.el            = cell;
         this.cellDatas[rowIndex][columnIndex] = cellData;

         var tmpClick=function()
         {
           if(window.event)
             oThis.onClick(window.event.srcElement);
           else
             oThis.onClick(this);
         };
         this.addEventListener(cell,"click",tmpClick,false);

         cell = this.nextSibling(cell,"div");
      }
      rowIndex++;
  }
}

jACOBCalendar.prototype.onClick = function(/*:HTMLElement*/ e)
{
  if(fireEventIsInProcess==true)
    return;
  // den übergeordneten container finden
  while(e.getAttribute("dayContainer")==null)
     e= e.parentNode;

  var currentContainer = this.lastChild(e,"div");
  var endHeight = 40;
  var endWidth  = 40;
  var expandedWidth = this.width-(endWidth*6);
  var expandedHeight= this.height-(endHeight*4);
  var clickRowIndex = parseInt(e.getAttribute("row"));
  var clickColumnIndex = parseInt(e.getAttribute("column"));
  var endFunct = function(){};

  if(this.lastAppointmentContainer!=null)
     this.lastAppointmentContainer.style.overflow="hidden";

  // collapse
  if(clickRowIndex == this.expandedRowIndex && clickColumnIndex == this.expandedColumnIndex)
  {
    this.expandedRowIndex = -1;
    this.expandedColumnIndex = -1;
    endHeight = this.height/5;
    endWidth  = this.width/7;
    endFunct=function()
             {
               currentContainer.scrollTop=0;
               currentContainer.scrollLeft=0;
             };
  }
  // expand
  else
  {
    this.expandedRowIndex = clickRowIndex;
    this.expandedColumnIndex = clickColumnIndex;
    endFunct=function()
             {
              currentContainer.style.overflow="auto";
              currentContainer.style.height=(expandedHeight-15)+"px";
             };
  }
  this.lastAppointmentContainer = currentContainer;

  var yPosSum =0;
  for(var row=0;row<this.cellDatas.size();row++)
  {
     var xPosSum =0;
     for(var column=0;column<this.cellDatas[0].size();column++)
     {
        var cellData = this.cellDatas[row][column];
        cellData.toX= xPosSum;
        cellData.toY= yPosSum;
        cellData.fromX = cellData.currentX;
        cellData.fromY= cellData.currentY;
        cellData.fromWidth = cellData.currentWidth;
        cellData.fromHeight= cellData.currentHeight;
        if(column==this.expandedColumnIndex)
        {
           xPosSum = xPosSum+expandedWidth;
           cellData.toWidth = expandedWidth;
        }
        else
        {
           xPosSum = xPosSum+endWidth;
           cellData.toWidth = endWidth;
        }
        if(row==this.expandedRowIndex)
           cellData.toHeight = expandedHeight;
        else
           cellData.toHeight = endHeight;
     }

     if(row!=this.expandedRowIndex)
        yPosSum = yPosSum+endHeight;
     else
        yPosSum = yPosSum+expandedHeight;
  }
  var expander = new fx.Expander(this.cellDatas,{onComplete:endFunct});
  expander.expand();
}

/**
 * Return the first HTMLElement with the handsover "type".
 *
 * @private
 **/
jACOBCalendar.prototype.firstChild = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var child = e ? e.firstChild : null;
  while (child) 
  {
    if(child.nodeType == 1 && (!type || child.nodeName.toLowerCase() == type.toLowerCase()))
    {
       break;
    }
    child = child.nextSibling;
  }
  return child;
}

jACOBCalendar.prototype.lastChild = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var child = e ? e.lastChild : null;
  while (child) 
  {
    if (child.nodeType == 1 && (!type || child.nodeName.toLowerCase() == type.toLowerCase()))
    {
       break;
    }
    child = child.previousSibling;
  }
  return child;
}
/**
 * Return the next HTMLElement with the handsover "type".
 * @private
 **/
jACOBCalendar.prototype.nextSibling = function(/*HTMLElement*/ e, /*:String*/ type)
{
  var sibling = e ? e.nextSibling : null;
  while (sibling) {
    if (sibling.nodeType == 1 && (!type || sibling.nodeName.toLowerCase() == type.toLowerCase())){break;}
    sibling = sibling.nextSibling;
  }
  return sibling;
}

jACOBCalendar.prototype.addEventListener=function(e,eT,eL,cap)
{
  eT=eT.toLowerCase();
  if(e.addEventListener)e.addEventListener(eT,eL,cap||false);
  else if(e.attachEvent)e.attachEvent('on'+eT,eL);
  else {
    var o=e['on'+eT];
    e['on'+eT]=typeof o=='function' ? function(v){o(v);eL(v);} : eL;
  }
}

fx.Expander = Class.create();
Object.extend(Object.extend(fx.Expander.prototype, fx.Base.prototype), {	
    initialize: function(cellDatas,options) {
        this.cellDatas = cellDatas;
	this.options = {
		duration: 500,
		onComplete: '',
		transition: fx.sinoidal
	}
	Object.extend(this.options, options || {});
    },

    step: function() {
	var time  = (new Date).getTime();
	if (time >= this.options.duration+this.startTime) {
            for(var row=0;row<this.cellDatas.size();row++)
            {
               for(var column=0;column<this.cellDatas[0].size();column++)
               {
                 var cellData = this.cellDatas[row][column];
                 cellData.currentX = cellData.toX;
                 cellData.currentY= cellData.toY;
                 cellData.currentWidth = cellData.toWidth;
                 cellData.currentHeight= cellData.toHeight;
                 cellData.el.style.width  = cellData.currentWidth+"px";
                 cellData.el.style.height = cellData.currentHeight+"px";
                 cellData.el.style.left   = cellData.currentX+"px";
                 cellData.el.style.top    = cellData.currentY+"px";
               }
            }
            clearInterval (this.timer);
            this.timer = null;
            if (this.options.onComplete) setTimeout(this.options.onComplete.bind(this), 10);
	}
	else {
            for(var row=0;row<this.cellDatas.size();row++)
            {
               for(var column=0;column<this.cellDatas[0].size();column++)
               {
                 var cellData = this.cellDatas[row][column];
                 var trans = this.options.transition((time - this.startTime) / (this.options.duration));
                 cellData.currentWidth = trans * (cellData.toWidth-cellData.fromWidth) + cellData.fromWidth;
                 cellData.currentHeight= trans * (cellData.toHeight-cellData.fromHeight) + cellData.fromHeight;
                 cellData.currentX = trans * (cellData.toX-cellData.fromX) + cellData.fromX;
                 cellData.currentY= trans * (cellData.toY-cellData.fromY) + cellData.fromY;

                 cellData.el.style.width  = cellData.currentWidth+"px";
                 cellData.el.style.height = cellData.currentHeight+"px";
                 cellData.el.style.left   = cellData.currentX+"px";
                 cellData.el.style.top    = cellData.currentY+"px";
               }
            }
	}
    },

    expand: function() {
	if (this.timer != null) return;
        for(var row=0;row<this.cellDatas.size();row++)
        {
           for(var column=0;column<this.cellDatas[0].size();column++)
           {
             var cellData = this.cellDatas[row][column];
             cellData.fromWidth = cellData.currentWidth;
             cellData.fromHeight= cellData.currentHeight;
             cellData.fromX = cellData.currentX;
             cellData.fromY= cellData.currentY;
           }
        }
	this.startTime = (new Date).getTime();
	this.timer = setInterval (this.step.bind(this), 13);
     },

    clearTimer: function() {
	clearInterval(this.timer);
	this.timer = null;
    }
});