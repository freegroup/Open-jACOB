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

function EnumDialog(figure /*:Figure*/)
{
  this.figure = figure;
  this.inputDiv = null
  this.inputTo = null;

  this.currentHighlight = null;
  this.method2HTML = new Object();

  this.selectedField = null;

  Dialog.call(this);
  this.setDimension(400,320);
  this.decision2Description= new Object();

  return this;
}
EnumDialog.prototype = new Dialog;
EnumDialog.prototype.type="EnumDialog";


EnumDialog.prototype.createHTMLElement=function()
{
  var item = Dialog.prototype.createHTMLElement.call(this);

  var label = document.createElement("div");
  label.innerHTML ="Table Enum Fields";
  label.style.left = "10px";
  label.style.top  = "30px";
  label.style.width= "200px";
  label.style.height="30px";
  label.style.position ="absolute";
  item.appendChild(label);

  this.inputDiv = document.createElement("div");
  this.inputDiv.style.position="absolute";
  this.inputDiv.style.left = "10px";
  this.inputDiv.style.top = "50px";
  this.inputDiv.style.width="200px";
  this.inputDiv.style.height="300px";
  this.inputDiv.style.overflow="auto";
  this.inputDiv.style.border="1px solid black";
  this.inputDiv.style.font="normal 10px verdana";
  item.appendChild(this.inputDiv);

  this.createList(this.inputDiv);

  return item;
}

EnumDialog.prototype.setDimension=function( w /*:int*/, h /*:int*/)
{
  Dialog.prototype.setDimension.call(this,w,h);
  if(this.inputDiv!=null)
  {
    this.inputDiv.style.height=(h-100)+"px";
    this.inputDiv.style.width=(w-20)+"px";
  }
}

EnumDialog.prototype.onOk=function()
{
  Dialog.prototype.onOk.call(this);
  var oThis = this;
  if (window.XMLHttpRequest) req = new XMLHttpRequest();
  else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
  else return; // fall on our sword
  req.open("POST", "getEnumValues.jsp?browser="+browserId+"&field="+this.selectedField);
  req.setRequestHeader('content-type', 'text/plain');
  req.onreadystatechange = function ()
  {
      if (req.readyState == 4)
      {
        var values = eval(req.responseText);
        oThis.figure.setEnumField(oThis.selectedField);
        oThis.figure.setEnumValues(values);
      }
  };
  req.send(null);
}


EnumDialog.prototype.createList=function(parentDiv /*:HTMLElement*/)
{
    var oThis = this;
    if (window.XMLHttpRequest) req = new XMLHttpRequest();
    else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
    else return; // fall on our sword
    req.open("POST", "getEnumTableFields.jsp?browser="+browserId);
    req.setRequestHeader('content-type', 'text/plain');
    req.onreadystatechange = function ()
    {
        if (req.readyState == 4) 
        {
          var list = document.createElement("ul");

          var values = eval(req.responseText);
          for(var i=0;i< values.length;i++)
          {
            var value = values[i];
            var node = document.createElement("li");
            if(value==oThis.figure.getEnumField())
            {
              oThis.currentHighlight = node;
              oThis.currentHighlight.style.fontWeight="bold";
            }
            node.onclick=function(){this.parent.select(this,this.enum_field)};
            node.parent =oThis;
            node.enum_field=value;
            node.appendChild(document.createTextNode(value));
            list.appendChild(node);
          }

          parentDiv.appendChild(list);
        }
    };
    req.send(null);

}

EnumDialog.prototype.select=function(element /*:HTMLElement*/, enum_field /*:String*/)
{
  this.selectedField  = enum_field;

  if(this.currentHighlight!=null)
    this.currentHighlight.style.fontWeight="normal";
  this.currentHighlight = element;
  if(this.currentHighlight!=null)
    this.currentHighlight.style.fontWeight="bold";
}
